package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;

/**
 * This test suite focuses on the behavior of the {@code contains(IndexExtractor)} method
 * in the {@link SparseBloomFilter} class, particularly its handling of invalid arguments.
 */
public class SparseBloomFilterContainsTest {

    /**
     * Tests that calling {@code contains(IndexExtractor)} with a null argument
     * correctly throws a {@code NullPointerException}. This ensures the method
     * is robust against invalid input.
     */
    @Test(expected = NullPointerException.class)
    public void testContainsWithNullIndexExtractorThrowsNullPointerException() {
        // Arrange: Create a standard shape and an empty sparse Bloom filter.
        // The specific dimensions of the shape are not relevant to this test.
        Shape shape = Shape.fromNM(10, 1);
        SparseBloomFilter filter = new SparseBloomFilter(shape);

        // Act & Assert: Call the method under test with a null argument.
        // The @Test(expected=...) annotation asserts that a NullPointerException is thrown.
        filter.contains((IndexExtractor) null);
    }
}