package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;

/**
 * This test suite contains specific test cases for the SparseBloomFilter class.
 */
// The original class name is kept to show the direct improvement.
// In a real-world scenario, this would be renamed to something like SparseBloomFilterTest.
public class SparseBloomFilter_ESTestTest26 extends SparseBloomFilter_ESTest_scaffolding {

    /**
     * Tests that calling contains() with a BitMapExtractor configured with an
     * impractically large number of bits throws an OutOfMemoryError.
     *
     * This scenario simulates a misconfiguration where the extractor might attempt
     * to allocate memory for a bitmap of size Integer.MAX_VALUE, which should fail.
     */
    @Test(timeout = 4000, expected = OutOfMemoryError.class)
    public void contains_whenBitMapExtractorHasExcessiveSize_throwsOutOfMemoryError() {
        // Arrange: Create a standard filter and a misconfigured extractor.
        Shape shape = Shape.fromNM(3, 3);
        SparseBloomFilter filter = new SparseBloomFilter(shape);

        // This extractor is created with a limit of Integer.MAX_VALUE. The implementation
        // of contains() is expected to trigger an operation on this extractor that
        // attempts to allocate a massive amount of memory, leading to an error.
        BitMapExtractor extractorWithHugeSize = BitMapExtractor.fromIndexExtractor(filter, Integer.MAX_VALUE);

        // Act & Assert: The call to contains() is expected to throw an OutOfMemoryError.
        // The 'expected' attribute in the @Test annotation handles the assertion.
        filter.contains(extractorWithHugeSize);
    }
}