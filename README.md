# Parallel DNA Sequence Matcher (Java, Eclipse Project)

This project implements a high-performance DNA sequence matcher using Java concurrency and parallel computing techniques. It's designed to search for specific DNA patterns in very large DNA files and demonstrate the power of multi-core CPU utilization.

---

##  Features

-  Parallel pattern matching using `ExecutorService` and `LongAdder`
-  Sequential baseline for benchmarking
-  Performance reporting with real-time speed-up measurement
-  JUnit 5 test suite for correctness and edge-case coverage
-  CPU profiling tool to track core usage
-  CLI interface for user input
-  Python script to plot speed-up vs. threads
-  Eclipse-compatible project structure

---

##  Project Structure

| File / Folder            | Description                                           |
|--------------------------|-------------------------------------------------------|
| `src/`                   | Java source code                                      |
| ├── `DNAParallelMatcher.java` | Core engine: handles both sequential and parallel matching |
| ├── `DNAMatcherCLI.java`      | CLI menu that prompts user for input and shows results     |
| ├── `DnaParallelMatcherTest.java` | JUnit tests to validate correctness                   |
| ├── `Profiler.java`           | CPU/system load logger during matching process         |
| `sample_dna.txt`         | A large DNA file (~120 MB) to test performance          |
| `test_dna.txt`           | Small DNA file for unit testing                       |
| `python/scaling_plot.py` | Python script to generate speed-up vs thread plots    |

---

##  How to Run 

1. Import Project
   - Open Eclipse → `File` → `Import` → `Existing Projects into Workspace`
   - Select the unzipped project folder

2. Run the Matcher
   - Right-click `DNAMatcherCLI.java` → `Run As` → `Java Application`
   - Follow the CLI prompts:
     ```
The entry point is DNAMatcherCLI.java. It accepts command-line arguments that are passed directly to the DNAParallelMatcher class.

These arguments are:

1. The path to a DNA file (e.g., sample_dna.txt)
2. The number of threads to use for parallel search
3. One or more DNA patterns to search for (e.g., AGCT, GATTACA)

These values are passed into the constructor:

```java
DNAParallelMatcher matcher = new DNAParallelMatcher(path, patterns, numThreads)
     ```

3. Run Unit Tests
   - Right-click `DnaParallelMatcherTest.java` → `Run As` → `JUnit Test`
   - Make sure all tests pass 

4. Profile CPU Load
   - Right-click `Profiler.java` → `Run As` → `Java Application`
   - Monitor real-time CPU usage during execution

---

##  Speed-up Plotting

To plot speed-up vs thread count:

1. Edit or create `scaling.csv`:
   ```csv
   threads,speedup
   1,1.0
   2,1.7
   4,2.9
   8,3.8






-----------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------


## How to Build and Run (with Docker)

Make sure Docker is installed and running.

1. Download the tar archive of the project from Google Drive and extract it:
Right-click → Extract All

Then open the extracted dna-matcher folder in your terminal

2. Build the Docker image:	//////////////docker build -t dna-matcher . ////////////////

3. Run the matcher: 
		///////////////docker run --rm dna-matcher sample_dna.txt AGCT GATTACA 4////////////////////

Where:

sample_dna.txt → input file (included)

4 → number of threads

AGCT, GATTACA → patterns to search

feel free to change the inputs 

-----------------------------------------------------------

Sample Output

[Sequential Run]
Total Matches: 4
Time: 123 ms

[Parallel Run]
Total Matches: 4
Time: 42 ms
Speed-up: 2.92×
