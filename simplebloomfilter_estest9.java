package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.ArgumentMatchers.anyLong;

import java.util.function.LongBiPredicate;

/**
 * Contains tests for the {@code SimpleBloomFilter#processBitMapPairs} method.
 */
public class SimpleBloomFilter_ESTestTest9 extends SimpleBloomFilter_ESTest_scaffolding {

    /**
     * Tests that {@code processBitMapPairs} returns {@code true} when the provided
     * predicate returns {@code true} for any pair of corresponding bit maps.
     */
    @Test(timeout = 4000)
    public void testProcessBitMapPairsReturnsTrueWhenPredicateIsTrue() {
        // Arrange
        // 1. Define a shape for the Bloom filters.
        final Shape shape = Shape.fromNM(10, 100); // 10 items, 100 bits

        // 2. Create two non-empty filters. This ensures there are bit maps to process,
        // allowing the predicate to be called.
        final SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        final SimpleBloomFilter otherFilter = new SimpleBloomFilter(shape);
        final Hasher hasher = new SimpleHasher(1, 1); // A simple hasher to set some bits
        filter.merge(hasher);
        otherFilter.merge(hasher);

        // 3. Create a mock predicate that always returns true. The method under test
        // should stop processing and return true on the first invocation of the predicate.
        final LongBiPredicate alwaysTruePredicate = mock(LongBiPredicate.class);
        when(alwaysTruePredicate.test(anyLong(), anyLong())).thenReturn(true);

        // Act
        // Execute the method under test.
        final boolean result = filter.processBitMapPairs(otherFilter, alwaysTruePredicate);

        // Assert
        // The method should return true because the predicate returned true.
        assertTrue("Expected processBitMapPairs to return true when the predicate is true.", result);

        // Also, verify that the predicate was actually called. This confirms that the
        // bit map pairs were processed as expected.
        verify(alwaysTruePredicate, atLeastOnce()).test(anyLong(), anyLong());
    }
}