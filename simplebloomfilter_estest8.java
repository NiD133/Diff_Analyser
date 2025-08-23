package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.LongPredicate;
import org.junit.Test;

/**
 * Contains tests for the {@code processBitMaps} method in {@link SimpleBloomFilter}.
 */
public class SimpleBloomFilterProcessBitMapsTest {

    /**
     * Tests that {@code processBitMaps} returns {@code false} and stops processing
     * (short-circuits) as soon as the provided predicate returns {@code false}.
     */
    @Test
    public void testProcessBitMapsShortCircuitsAndReturnsFalseWhenPredicateIsFalse() {
        // Arrange
        // Create a shape that results in a bitmap with more than one long value (2 in this case),
        // which is necessary to properly verify the short-circuiting behavior.
        final Shape shape = Shape.fromNM(100, 10);
        final SimpleBloomFilter filter = new SimpleBloomFilter(shape);

        // Create a predicate that counts its invocations and returns false on the first call.
        final AtomicInteger callCount = new AtomicInteger(0);
        final LongPredicate stoppingPredicate = bitMap -> {
            callCount.incrementAndGet();
            return false; // Signal to stop processing
        };

        // Act
        final boolean result = filter.processBitMaps(stoppingPredicate);

        // Assert
        assertFalse("The method should return false when the predicate returns false.", result);
        assertEquals("The predicate should have been called only once due to short-circuiting.", 1, callCount.get());
    }
}