import java.nio.file.*;
import java.util.*;

public class DNAMatcherCLI {

    public static void main(String[] args) throws Exception {
      

        if (args.length < 2) {
            System.out.println("Usage: java DNAMatcherCLI <path> <numThreads> <pattern1> <pattern2> ...");
            return;
        }
        String pathString = args[0];
        Path path = Paths.get(pathString);
        int numThreads = Integer.parseInt(args[1]);
        List<String> patterns = Arrays.asList(Arrays.copyOfRange(args, 2, args.length));


        

        DNAParallelMatcher matcher = new DNAParallelMatcher(path, patterns, numThreads );
       

        System.out.println("[Running Sequential Mode]");
        long t1 = matcher.runSequentialSearch();
        System.out.println("Sequential Time (ms): " + t1);

        System.out.println("[Running Parallel Mode]");
        long t2 = matcher.runParallelSearch();
        System.out.println("Parallel Matches: " + matcher.getTotalMatches());
        System.out.println("Parallel Time (ms): " + t2);

        System.out.printf("Speed-up: %.2fx\n", (double)t1/t2);
        
    }
}