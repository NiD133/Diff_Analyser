package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;

/**
 * Unit tests for {@link SimpleBloomFilter}.
 */
public class SimpleBloomFilterTest {

    /**
     * Tests that merge() throws a NullPointerException when given a null IndexExtractor.
     * A Bloom filter cannot merge indices from a null source, so the method must
     * reject this invalid argument to ensure correctness and prevent runtime errors.
     */
    @Test(expected = NullPointerException.class)
    public void mergeWithNullIndexExtractorShouldThrowNullPointerException() {
        // Arrange: Create a filter with any valid shape. The specific parameters
        // are not important for this null-check test.
        Shape shape = Shape.fromKM(10, 100);
        SimpleBloomFilter bloomFilter = new SimpleBloomFilter(shape);

        // Act & Assert: Attempting to merge with a null extractor should throw
        // a NullPointerException. The @Test(expected=...) annotation handles the assertion.
        bloomFilter.merge((IndexExtractor) null);
    }
}