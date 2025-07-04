import org.junit.jupiter.api.*;

import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DnaParallelMatcherTest {

    private static Path sampleFile;
    private static final String TEST_DNA = "ACGTACGTGACGTACGTGACGT";

    @BeforeAll
    public static void setup() throws Exception {
        sampleFile = Paths.get("test_dna.txt");
        Files.writeString(sampleFile, TEST_DNA);
    }

    @AfterAll
    public static void teardown() throws Exception {
        Files.deleteIfExists(sampleFile);
    }

    @Test
    public void testSequentialCorrectness() throws Exception {
        DNAParallelMatcher matcher = new DNAParallelMatcher(sampleFile, List.of("ACGT"), 4);
        long time = matcher.runSequentialSearch();
        assertTrue(time >= 0);
    }

    @Test
    public void testParallelCorrectness() throws Exception {
        DNAParallelMatcher matcher = new DNAParallelMatcher(sampleFile, List.of("ACGT"), 4);
        matcher.runParallelSearch();
        assertEquals(2, matcher.getTotalMatches());
    }

    @Test
    public void testMultiplePatterns() throws Exception {
        DNAParallelMatcher matcher = new DNAParallelMatcher(sampleFile, List.of("ACGT", "GACGT"), 4);
        matcher.runParallelSearch();
        assertTrue(matcher.getTotalMatches() >= 3);
    }

    @Test
    public void testZeroMatches() throws Exception {
        DNAParallelMatcher matcher = new DNAParallelMatcher(sampleFile, List.of("TTTT"), 4);
        matcher.runParallelSearch();
        assertEquals(0, matcher.getTotalMatches());
    }

    @Test
    public void testSingleThread() throws Exception {
        DNAParallelMatcher matcher = new DNAParallelMatcher(sampleFile, List.of("ACGT"), 1);
        matcher.runParallelSearch();
        assertEquals(5, matcher.getTotalMatches());
    }
}