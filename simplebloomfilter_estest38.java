package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.LongBiPredicate;
import org.junit.Test;

/**
 * Unit tests for the {@code SimpleBloomFilter#processBitMapPairs} method.
 */
public class SimpleBloomFilterProcessBitMapPairsTest {

    /**
     * Tests that {@code processBitMapPairs} short-circuits and returns {@code false}
     * as soon as the provided predicate returns {@code false}.
     */
    @Test
    public void testProcessBitMapPairsReturnsFalseAndShortCircuitsWhenPredicateIsFalse() {
        // Arrange
        // Create a shape that requires more than one long in its bitmap array (e.g., 75 bits > 64 bits)
        // to properly test the short-circuiting behavior.
        Shape shape = Shape.fromKM(10, 75);
        SimpleBloomFilter filter1 = new SimpleBloomFilter(shape);
        SimpleBloomFilter filter2 = new SimpleBloomFilter(shape);

        // Create a mock predicate that will always return false.
        LongBiPredicate alwaysFalsePredicate = mock(LongBiPredicate.class);
        when(alwaysFalsePredicate.test(anyLong(), anyLong())).thenReturn(false);

        // Act
        // The processBitMapPairs method iterates through the bitmap arrays of both filters,
        // applying the predicate to each pair of corresponding long values.
        boolean result = filter1.processBitMapPairs(filter2, alwaysFalsePredicate);

        // Assert
        // The method should return false because the predicate returned false.
        assertFalse("The result should be false when the predicate returns false.", result);

        // The method should have stopped processing after the first pair.
        // Since the bitmap for this shape consists of 2 longs, a non-short-circuiting
        // implementation would call the predicate twice. We verify it was called only once.
        verify(alwaysFalsePredicate, times(1)).test(0L, 0L);
    }
}