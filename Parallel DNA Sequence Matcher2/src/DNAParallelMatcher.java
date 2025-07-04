//import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;

public class DNAParallelMatcher {

    private final ExecutorService executor;
    private final int numThreads;
    private final Path inputPath;
    private final List<String> patterns;
    private final LongAdder totalMatches = new LongAdder();

    public DNAParallelMatcher(Path inputPath, List<String> patterns, int numThreads) {
        this.inputPath = inputPath;
        this.patterns = patterns;
        this.numThreads = numThreads;
        this.executor = Executors.newFixedThreadPool(numThreads);
    }

    public long runParallelSearch() throws Exception {
        long startTime = System.nanoTime();

        try (FileChannel channel = FileChannel.open(inputPath, StandardOpenOption.READ)) {
            long fileSize = channel.size();
            long chunkSize = fileSize / numThreads;
            List<Future<Void>> futures = new ArrayList<>();

            for (int i = 0; i < numThreads; i++) {
                long start = i * chunkSize;
                long end = (i == numThreads - 1) ? fileSize : (i + 1) * chunkSize;

                futures.add(executor.submit(new DNAChunkProcessor(channel, start, end, patterns, totalMatches)));
            }
            for (Future<Void> f : futures) f.get();
        }

        long endTime = System.nanoTime();
        executor.shutdown();
        return (endTime - startTime) / 1_000_000;
    }

    public long runSequentialSearch() throws Exception {
        long startTime = System.nanoTime();
        long matches = 0;
        String content = Files.readString(inputPath, StandardCharsets.UTF_8);

        for (String pattern : patterns) {
            int index = 0;
            while ((index = content.indexOf(pattern, index)) != -1) {
                matches++;
                index += pattern.length();
            }
        }

        long endTime = System.nanoTime();
        System.out.println("[Sequential] Matches: " + matches);
        return (endTime - startTime) / 1_000_000;
    }

    public long getTotalMatches() {
        return totalMatches.longValue();
    }

    static class DNAChunkProcessor implements Callable<Void> {
        private final FileChannel channel;
        private final long start;
        private final long end;
        private final List<String> patterns;
        private final LongAdder adder;

        public DNAChunkProcessor(FileChannel channel, long start, long end, List<String> patterns, LongAdder adder) {
            this.channel = channel;
            this.start = start;
            this.end = end;
            this.patterns = patterns;
            this.adder = adder;
        }

        @Override
        public Void call() throws Exception {
            long size = end - start;
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, start, size);
            byte[] bytes = new byte[(int) size];
            buffer.get(bytes);
            String segment = new String(bytes, StandardCharsets.UTF_8);

            for (String pattern : patterns) {
                int index = 0;
                while ((index = segment.indexOf(pattern, index)) != -1) {
                    adder.increment();
                    index += pattern.length();
                }
            }
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.out.println("Usage: java DNAParallelMatcher <file-path> <threads> <pattern1> [<pattern2> ...]");
            return;
        }

        Path filePath = Paths.get(args[0]);
        int threads = Integer.parseInt(args[1]);
        List<String> patterns = Arrays.asList(Arrays.copyOfRange(args, 2, args.length));

        DNAParallelMatcher matcher = new DNAParallelMatcher(filePath, patterns, threads);

        System.out.println("[Sequential Run]");
        long seqTime = matcher.runSequentialSearch();
        System.out.println("Time Taken (ms): " + seqTime);

        System.out.println("[Parallel Run]");
        long parTime = matcher.runParallelSearch();
        System.out.println("Total Matches: " + matcher.getTotalMatches());
        System.out.println("Time Taken (ms): " + parTime);

        double speedUp = (double) seqTime / parTime;
        System.out.printf("Speed-up: %.2f×\n", speedUp);
    }
}