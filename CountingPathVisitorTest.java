package org.apache.commons.io.file;

import static org.apache.commons.io.file.CounterAssertions.assertCounts;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for the {@link CountingPathVisitor} class.
 */
class CountingPathVisitorTest extends TestArguments {

    /**
     * Verifies that the visitor starts with zero counts for files, directories, and sizes.
     *
     * @param visitor the CountingPathVisitor instance to check.
     */
    private void verifyInitialCountsAreZero(final CountingPathVisitor visitor) {
        Assertions.assertEquals(CountingPathVisitor.withLongCounters(), visitor, "Expected zero counts with long counters");
        Assertions.assertEquals(CountingPathVisitor.withBigIntegerCounters(), visitor, "Expected zero counts with BigInteger counters");
    }

    /**
     * Tests counting in an empty directory.
     *
     * @param visitor the CountingPathVisitor instance used for counting.
     * @throws IOException if an I/O error occurs.
     */
    @ParameterizedTest
    @MethodSource("countingPathVisitors")
    void testCountInEmptyDirectory(final CountingPathVisitor visitor) throws IOException {
        verifyInitialCountsAreZero(visitor);
        try (TempDirectory tempDir = TempDirectory.create(getClass().getCanonicalName())) {
            assertCounts(1, 0, 0, PathUtils.visitFileTree(visitor, tempDir.get()));
        }
    }

    /**
     * Tests counting in a directory with one file of size 0.
     *
     * @param visitor the CountingPathVisitor instance used for counting.
     * @throws IOException if an I/O error occurs.
     */
    @ParameterizedTest
    @MethodSource("countingPathVisitors")
    void testCountInDirectoryWithOneFileSizeZero(final CountingPathVisitor visitor) throws IOException {
        verifyInitialCountsAreZero(visitor);
        assertCounts(1, 1, 0, PathUtils.visitFileTree(visitor,
                "src/test/resources/org/apache/commons/io/dirs-1-file-size-0"));
    }

    /**
     * Tests counting in a directory with one file of size 1.
     *
     * @param visitor the CountingPathVisitor instance used for counting.
     * @throws IOException if an I/O error occurs.
     */
    @ParameterizedTest
    @MethodSource("countingPathVisitors")
    void testCountInDirectoryWithOneFileSizeOne(final CountingPathVisitor visitor) throws IOException {
        verifyInitialCountsAreZero(visitor);
        assertCounts(1, 1, 1, PathUtils.visitFileTree(visitor,
                "src/test/resources/org/apache/commons/io/dirs-1-file-size-1"));
    }

    /**
     * Tests counting in a directory with two subdirectories, each containing one file of size 1.
     *
     * @param visitor the CountingPathVisitor instance used for counting.
     * @throws IOException if an I/O error occurs.
     */
    @ParameterizedTest
    @MethodSource("countingPathVisitors")
    void testCountInDirectoryWithTwoFilesSizeTwo(final CountingPathVisitor visitor) throws IOException {
        verifyInitialCountsAreZero(visitor);
        assertCounts(3, 2, 2, PathUtils.visitFileTree(visitor,
                "src/test/resources/org/apache/commons/io/dirs-2-file-size-2"));
    }

    /**
     * Tests the toString method of the CountingPathVisitor.
     *
     * @param visitor the CountingPathVisitor instance to test.
     */
    @ParameterizedTest
    @MethodSource("countingPathVisitors")
    void testToStringMethod(final CountingPathVisitor visitor) {
        // Ensure that calling toString does not cause any exceptions.
        visitor.toString();
    }
}