package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;

/**
 * Tests for {@link SimpleBloomFilter}.
 */
public class SimpleBloomFilterTest {

    /**
     * Tests that {@code processBitMapPairs} throws a NullPointerException when the
     * provided LongBiPredicate is null.
     */
    @Test(expected = NullPointerException.class)
    public void testProcessBitMapPairsWithNullPredicateThrowsNullPointerException() {
        // Arrange: Create a filter to call the method on.
        // The shape parameters are arbitrary but valid.
        final Shape shape = Shape.fromNMK(15, 15, 15);
        final SimpleBloomFilter filter = new SimpleBloomFilter(shape);

        // Act: Call the method with a null predicate.
        // The other filter can be any valid BitMapExtractor, including itself.
        // The @Test(expected) annotation will assert that a NullPointerException is thrown.
        filter.processBitMapPairs(filter, null);
    }
}