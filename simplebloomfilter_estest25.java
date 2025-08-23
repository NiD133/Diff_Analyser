package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;

/**
 * Contains tests for the {@link SimpleBloomFilter} class.
 */
public class SimpleBloomFilterTest {

    /**
     * Tests that calling merge() with a null BloomFilter argument
     * correctly throws a NullPointerException. This ensures the method
     * adheres to the common contract of rejecting null inputs for this parameter.
     */
    @Test(expected = NullPointerException.class)
    public void mergeWithNullBloomFilterShouldThrowNullPointerException() {
        // Arrange: Create a filter instance. The specific shape is not important for this test.
        Shape shape = Shape.fromNM(10, 100);
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);

        // Act: Attempt to merge with a null filter.
        // The @Test(expected) annotation handles the assertion.
        filter.merge((BloomFilter<?>) null);
    }
}