package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Contains tests for the {@link SimpleBloomFilter} class, focusing on its behavior
 * under specific conditions.
 */
public class SimpleBloomFilterTest {

    /**
     * Tests that attempting to construct a SimpleBloomFilter with a very large shape
     * that requires a huge memory allocation correctly throws an OutOfMemoryError.
     * This test validates the filter's behavior under extreme resource constraints.
     */
    @Test
    public void constructorWithExtremelyLargeShapeShouldThrowOutOfMemoryError() {
        // Arrange: Define a shape with a number of bits close to Integer.MAX_VALUE.
        // Instantiating a Bloom filter with this shape requires allocating a long[] array
        // of approximately 256MB, which is designed to trigger an OutOfMemoryError
        // on a typical test environment configuration.
        final int hugeSize = 2_147_483_602; // A value near Integer.MAX_VALUE
        final Shape extremelyLargeShape = Shape.fromNM(hugeSize, hugeSize);

        // Act & Assert: Verify that the constructor throws an OutOfMemoryError.
        try {
            new SimpleBloomFilter(extremelyLargeShape);
            fail("Expected an OutOfMemoryError, but the Bloom filter was created successfully. " +
                 "This may happen on a system with very large available heap space.");
        } catch (final OutOfMemoryError expected) {
            // This is the expected outcome. The test passes.
        }
    }
}