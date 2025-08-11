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
     * Helper method to clean an empty directory and assert the expected counts.
     *
     * @param visitor The CleaningPathVisitor instance to use.
     * @throws IOException If an I/O error occurs.
     */
    private void cleanEmptyDirectoryAndAssert(final CleaningPathVisitor visitor) throws IOException {
        Files.walkFileTree(tempDir, visitor);
        assertCounts(1, 0, 0, visitor);
    }

    /**
     * Tests cleaning an empty directory using various CleaningPathVisitor instances.
     */
    @ParameterizedTest
    @MethodSource("cleaningPathVisitors")
    void testCleanEmptyDirectory(final CleaningPathVisitor visitor) throws IOException {
        cleanEmptyDirectoryAndAssert(visitor);
    }

    /**
     * Tests cleaning an empty directory with a null constructor argument.
     */
    @ParameterizedTest
    @MethodSource("pathCounters")
    void testCleanEmptyDirectoryWithNullConstructorArg(final PathCounters pathCounters) throws IOException {
        cleanEmptyDirectoryAndAssert(new CleaningPathVisitor(pathCounters, (String[]) null));
    }

    /**
     * Tests cleaning a directory with one file of size 0.
     */
    @ParameterizedTest
    @MethodSource("cleaningPathVisitors")
    void testCleanDirectoryWithOneFileSizeZero(final CleaningPathVisitor visitor) throws IOException {
        PathUtils.copyDirectory(Paths.get("src/test/resources/org/apache/commons/io/dirs-1-file-size-0"), tempDir);
        final CleaningPathVisitor resultVisitor = PathUtils.visitFileTree(visitor, tempDir);
        assertCounts(1, 1, 0, resultVisitor);
        assertSame(visitor, resultVisitor);
        assertVisitorEquality(resultVisitor);
    }

    /**
     * Tests cleaning a directory with one file of size 1.
     */
    @ParameterizedTest
    @MethodSource("cleaningPathVisitors")
    void testCleanDirectoryWithOneFileSizeOne(final CleaningPathVisitor visitor) throws IOException {
        PathUtils.copyDirectory(Paths.get("src/test/resources/org/apache/commons/io/dirs-1-file-size-1"), tempDir);
        final CleaningPathVisitor resultVisitor = PathUtils.visitFileTree(visitor, tempDir);
        assertCounts(1, 1, 1, resultVisitor);
        assertSame(visitor, resultVisitor);
        assertVisitorEquality(resultVisitor);
    }

    /**
     * Tests cleaning a directory with one file of size 1, skipping that file.
     */
    @ParameterizedTest
    @MethodSource("pathCounters")
    void testCleanDirectoryWithOneFileSizeOneAndSkip(final PathCounters pathCounters) throws IOException {
        PathUtils.copyDirectory(Paths.get("src/test/resources/org/apache/commons/io/dirs-1-file-size-1"), tempDir);
        final String skipFileName = "file-size-1.bin";
        final CleaningPathVisitor visitor = new CleaningPathVisitor(pathCounters, skipFileName);
        final CleaningPathVisitor resultVisitor = PathUtils.visitFileTree(visitor, tempDir);
        assertCounts(1, 1, 1, resultVisitor);
        assertSame(visitor, resultVisitor);
        assertTrue(Files.exists(tempDir.resolve(skipFileName)));
        Files.delete(tempDir.resolve(skipFileName));
        assertVisitorEquality(resultVisitor);
    }

    /**
     * Tests cleaning a directory with two subdirectories, each containing one file of size 1.
     */
    @ParameterizedTest
    @MethodSource("cleaningPathVisitors")
    void testCleanDirectoryWithTwoFilesSizeTwo(final CleaningPathVisitor visitor) throws IOException {
        PathUtils.copyDirectory(Paths.get("src/test/resources/org/apache/commons/io/dirs-2-file-size-2"), tempDir);
        final CleaningPathVisitor resultVisitor = PathUtils.visitFileTree(visitor, tempDir);
        assertCounts(3, 2, 2, resultVisitor);
        assertSame(visitor, resultVisitor);
        assertVisitorEquality(resultVisitor);
    }

    /**
     * Tests the equals and hashCode methods of CleaningPathVisitor.
     */
    @Test
    void testEqualsAndHashCode() {
        final CountingPathVisitor visitor0 = CleaningPathVisitor.withLongCounters();
        final CountingPathVisitor visitor1 = CleaningPathVisitor.withLongCounters();
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
     * Asserts the equality and hash code consistency of the given visitor.
     *
     * @param visitor The CleaningPathVisitor instance to check.
     */
    private void assertVisitorEquality(final CleaningPathVisitor visitor) {
        assertNotEquals(visitor, CleaningPathVisitor.withLongCounters());
        assertNotEquals(visitor.hashCode(), CleaningPathVisitor.withLongCounters().hashCode());
        assertEquals(visitor, visitor);
        assertEquals(visitor.hashCode(), visitor.hashCode());
    }
}