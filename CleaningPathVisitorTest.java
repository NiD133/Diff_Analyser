package org.apache.commons.io.file;

import static org.apache.commons.io.file.CounterAssertions.assertCounts;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.file.Counters.PathCounters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for {@link CleaningPathVisitor}.
 */
class CleaningPathVisitorTest extends TestArguments {

    @TempDir
    private Path tempDir;

    /**
     * Helper method to apply a CleaningPathVisitor to an empty directory and assert the expected counts.
     *
     * @param visitor the CleaningPathVisitor to apply
     * @throws IOException if an I/O error occurs
     */
    private void cleanAndAssertEmptyDirectory(final CleaningPathVisitor visitor) throws IOException {
        Files.walkFileTree(tempDir, visitor);
        assertCounts(1, 0, 0, visitor);
    }

    /**
     * Tests cleaning an empty directory using various CleaningPathVisitor instances.
     *
     * @param visitor the CleaningPathVisitor instance
     * @throws IOException if an I/O error occurs
     */
    @ParameterizedTest
    @MethodSource("cleaningPathVisitors")
    void testCleanEmptyDirectory(final CleaningPathVisitor visitor) throws IOException {
        cleanAndAssertEmptyDirectory(visitor);
    }

    /**
     * Tests cleaning an empty directory with a null constructor argument for path counters.
     *
     * @param pathCounters the PathCounters instance
     * @throws IOException if an I/O error occurs
     */
    @ParameterizedTest
    @MethodSource("pathCounters")
    void testCleanEmptyDirectoryWithNullPathCounters(final PathCounters pathCounters) throws IOException {
        cleanAndAssertEmptyDirectory(new CleaningPathVisitor(pathCounters, (String[]) null));
    }

    /**
     * Tests cleaning a directory containing one file of size 0.
     *
     * @param visitor the CleaningPathVisitor instance
     * @throws IOException if an I/O error occurs
     */
    @ParameterizedTest
    @MethodSource("cleaningPathVisitors")
    void testCleanDirectoryWithOneFileSize0(final CleaningPathVisitor visitor) throws IOException {
        Path sourceDir = Paths.get("src/test/resources/org/apache/commons/io/dirs-1-file-size-0");
        PathUtils.copyDirectory(sourceDir, tempDir);
        CleaningPathVisitor resultVisitor = PathUtils.visitFileTree(visitor, tempDir);
        assertCounts(1, 1, 0, resultVisitor);
        assertSame(visitor, resultVisitor);
        assertVisitorEquality(resultVisitor);
    }

    /**
     * Tests cleaning a directory containing one file of size 1.
     *
     * @param visitor the CleaningPathVisitor instance
     * @throws IOException if an I/O error occurs
     */
    @ParameterizedTest
    @MethodSource("cleaningPathVisitors")
    void testCleanDirectoryWithOneFileSize1(final CleaningPathVisitor visitor) throws IOException {
        Path sourceDir = Paths.get("src/test/resources/org/apache/commons/io/dirs-1-file-size-1");
        PathUtils.copyDirectory(sourceDir, tempDir);
        CleaningPathVisitor resultVisitor = PathUtils.visitFileTree(visitor, tempDir);
        assertCounts(1, 1, 1, resultVisitor);
        assertSame(visitor, resultVisitor);
        assertVisitorEquality(resultVisitor);
    }

    /**
     * Tests cleaning a directory with one file of size 1, skipping the file.
     *
     * @param pathCounters the PathCounters instance
     * @throws IOException if an I/O error occurs
     */
    @ParameterizedTest
    @MethodSource("pathCounters")
    void testCleanDirectoryWithOneFileSize1AndSkip(final PathCounters pathCounters) throws IOException {
        Path sourceDir = Paths.get("src/test/resources/org/apache/commons/io/dirs-1-file-size-1");
        PathUtils.copyDirectory(sourceDir, tempDir);
        String skipFileName = "file-size-1.bin";
        CleaningPathVisitor visitor = new CleaningPathVisitor(pathCounters, skipFileName);
        CleaningPathVisitor resultVisitor = PathUtils.visitFileTree(visitor, tempDir);
        assertCounts(1, 1, 1, resultVisitor);
        assertSame(visitor, resultVisitor);
        assertTrue(Files.exists(tempDir.resolve(skipFileName)));
        Files.delete(tempDir.resolve(skipFileName));
        assertVisitorEquality(resultVisitor);
    }

    /**
     * Tests cleaning a directory with two subdirectories, each containing one file of size 1.
     *
     * @param visitor the CleaningPathVisitor instance
     * @throws IOException if an I/O error occurs
     */
    @ParameterizedTest
    @MethodSource("cleaningPathVisitors")
    void testCleanDirectoryWithTwoFilesSize2(final CleaningPathVisitor visitor) throws IOException {
        Path sourceDir = Paths.get("src/test/resources/org/apache/commons/io/dirs-2-file-size-2");
        PathUtils.copyDirectory(sourceDir, tempDir);
        CleaningPathVisitor resultVisitor = PathUtils.visitFileTree(visitor, tempDir);
        assertCounts(3, 2, 2, resultVisitor);
        assertSame(visitor, resultVisitor);
        assertVisitorEquality(resultVisitor);
    }

    /**
     * Tests the equals and hashCode methods of CleaningPathVisitor.
     */
    @Test
    void testEqualsAndHashCode() {
        CountingPathVisitor visitor0 = CleaningPathVisitor.withLongCounters();
        CountingPathVisitor visitor1 = CleaningPathVisitor.withLongCounters();
        assertEquals(visitor0, visitor0);
        assertEquals(visitor0, visitor1);
        assertEquals(visitor1, visitor0);
        assertEquals(visitor0.hashCode(), visitor0.hashCode());
        assertEquals(visitor0.hashCode(), visitor1.hashCode());
        assertEquals(visitor1.hashCode(), visitor0.hashCode());
        visitor0.getPathCounters().getByteCounter().increment();
        assertEquals(visitor0, visitor0);
        assertNotEquals(visitor0, visitor1);
        assertNotEquals(visitor1, visitor0);
        assertEquals(visitor0.hashCode(), visitor0.hashCode());
        assertNotEquals(visitor0.hashCode(), visitor1.hashCode());
        assertNotEquals(visitor1.hashCode(), visitor0.hashCode());
    }

    /**
     * Asserts the equality and hash code consistency of a CleaningPathVisitor.
     *
     * @param visitor the CleaningPathVisitor to check
     */
    private void assertVisitorEquality(CleaningPathVisitor visitor) {
        assertNotEquals(visitor, CleaningPathVisitor.withLongCounters());
        assertNotEquals(visitor.hashCode(), CleaningPathVisitor.withLongCounters().hashCode());
        assertEquals(visitor, visitor);
        assertEquals(visitor.hashCode(), visitor.hashCode());
    }
}