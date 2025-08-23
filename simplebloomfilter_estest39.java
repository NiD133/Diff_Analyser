package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import java.util.function.LongBiPredicate;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * This class contains tests for the SimpleBloomFilter.
 */
public class SimpleBloomFilterTest {

    /**
     * Tests that processBitMapPairs() correctly stops processing and returns false
     * as soon as the provided predicate returns false. This demonstrates the
     * short-circuiting behavior of the method.
     */
    @Test
    public void testProcessBitMapPairsStopsAndReturnsFalseWhenPredicateIsFalse() {
        // Arrange
        // 1. Define a shape for the Bloom filters. This shape requires a bitmap
        //    array of length 2 (since 70 bits > 64 bits).
        Shape shape = Shape.fromNM(70, 1);
        SimpleBloomFilter filter1 = new SimpleBloomFilter(shape);
        SimpleBloomFilter filter2 = new SimpleBloomFilter(shape);

        // 2. Create a mock predicate that always returns false.
        //    The LongBiPredicate is applied to each pair of 64-bit chunks (longs)
        //    from the two filters' bitmaps.
        LongBiPredicate falsePredicate = mock(LongBiPredicate.class);
        when(falsePredicate.test(anyLong(), anyLong())).thenReturn(false);

        // Act
        // 3. Call the method under test. It should apply the predicate to the first
        //    pair of longs from the bitmaps, receive 'false', and immediately return.
        boolean result = filter1.processBitMapPairs(filter2, falsePredicate);

        // Assert
        // 4. The overall result should be false, as dictated by our mock predicate.
        assertFalse("The method should return false if the predicate returns false.", result);

        // 5. Verify that the predicate was actually called. This ensures the test is
        //    meaningful and didn't pass just because the processing loop was never entered.
        //    We expect it to be called at least once before short-circuiting.
        verify(falsePredicate, atLeastOnce()).test(anyLong(), anyLong());
    }
}