import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.*;

public class Profiler {

    public static void profile(Runnable task, int durationSeconds) throws InterruptedException {
        System.out.println("Profiling CPU usage...");
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Cast to com.sun.management.OperatingSystemMXBean to get extended methods
        OperatingSystemMXBean osBean =
            (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        scheduler.scheduleAtFixedRate(() -> {
            @SuppressWarnings("deprecation")
			double cpuLoad = osBean.getSystemCpuLoad();  // Returns value between 0.0 and 1.0
            if (cpuLoad < 0) {
                System.out.println("CPU Load: Not available");
            } else {
                System.out.printf("CPU Load: %.2f%%\n", cpuLoad * 100);
            }
        }, 0, 1, TimeUnit.SECONDS);

        Thread taskThread = new Thread(task);
        taskThread.start();

        taskThread.join();
        scheduler.shutdownNow();
        System.out.println("Done profiling.");
    }

    public static void main(String[] args) throws Exception {
        Runnable dnaTask = () -> {
            try {
                DNAParallelMatcher matcher = new DNAParallelMatcher(
                        Path.of("big_dna_seq.txt"), List.of("ACGT", "GACGT"), 4);
                matcher.runParallelSearch();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        profile(dnaTask, 5);
    }
}