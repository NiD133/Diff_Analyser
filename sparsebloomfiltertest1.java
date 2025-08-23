package org.apache.commons.collections4.bloomfilter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@code processBitMaps} method in {@link SparseBloomFilter} with various edge cases.
 */
public class SparseBloomFilterProcessBitMapsTest extends AbstractBloomFilterTest<SparseBloomFilter> {

    @Override
    protected SparseBloomFilter createEmptyFilter(final Shape shape) {
        return new SparseBloomFilter(shape);
    }

    /**
     * Tests that processing stops immediately if the predicate returns false on the first bitmap.
     */
    @Test
    void processBitMaps_shouldExitEarly_whenPredicateReturnsFalseOnFirstCall() {
        // Arrange
        // Create a filter with indices that span multiple bitmaps.
        int[] indices = {1, 65}; // Indices in first (0-63) and second (64-127) bitmaps.
        BloomFilter bloomFilter = createFilter(getTestShape(), IndexExtractor.fromIndexArray(indices));
        final AtomicInteger callCounter = new AtomicInteger(0);

        // Act
        // The predicate increments the counter and immediately returns false, which should terminate processing.
        final boolean result = bloomFilter.processBitMaps(bitmap -> {
            callCounter.incrementAndGet();
            return false;
        });

        // Assert
        assertFalse(result, "Processing should be terminated by the predicate");
        assertEquals(1, callCounter.get(), "Predicate should have been called only once");
    }

    /**
     * Tests that processing stops on the second bitmap if the predicate returns false on the second call.
     */
    @Test
    void processBitMaps_shouldExitEarly_whenPredicateReturnsFalseOnSecondCall() {
        // Arrange
        // Create a filter with indices that span multiple bitmaps.
        int[] indices = {1, 65}; // Indices in first and second bitmaps.
        BloomFilter bloomFilter = createFilter(getTestShape(), IndexExtractor.fromIndexArray(indices));
        final AtomicInteger callCounter = new AtomicInteger(0);

        // Act
        // The predicate returns true for the first call and false for the second,
        // terminating processing on the second bitmap.
        final boolean result = bloomFilter.processBitMaps(bitmap -> callCounter.getAndIncrement() == 0);

        // Assert
        assertFalse(result, "Processing should be terminated on the second call");
        assertEquals(2, callCounter.get(), "Predicate should have been called twice");
    }

    /**
     * Tests that all bitmaps are processed, including empty ones, if the predicate always returns true.
     */
    @Test
    void processBitMaps_shouldProcessAllBitmaps_whenPredicateAlwaysReturnsTrue() {
        // Arrange
        // getTestShape() creates a filter with 72 bits, which requires 2 longs for its bitmap representation.
        final int expectedBitmapCount = 2;
        // Create a filter with indices only in the first bitmap.
        int[] indices = {1, 2, 3, 4};
        BloomFilter bloomFilter = createFilter(getTestShape(), IndexExtractor.fromIndexArray(indices));
        final AtomicInteger callCounter = new AtomicInteger(0);

        // Act
        // The predicate always returns true, so all bitmaps should be processed.
        final boolean result = bloomFilter.processBitMaps(bitmap -> {
            callCounter.incrementAndGet();
            return true;
        });

        // Assert
        assertTrue(result, "Processing should complete successfully");
        assertEquals(expectedBitmapCount, callCounter.get(), "Predicate should be called for each bitmap");
    }

    /**
     * Tests that processing stops on a trailing empty bitmap if the predicate returns false.
     */
    @Test
    void processBitMaps_shouldExitEarly_whenPredicateReturnsFalseOnEmptyTrailingBitmap() {
        // Arrange
        // getTestShape() creates a filter with 72 bits (2 bitmaps).
        // Create a filter with indices only in the first bitmap.
        int[] indices = {1, 2, 3, 4};
        BloomFilter bloomFilter = createFilter(getTestShape(), IndexExtractor.fromIndexArray(indices));
        final AtomicInteger callCounter = new AtomicInteger(0);

        // Act
        // The predicate returns true for the first (non-empty) bitmap and false for the second (empty) one.
        final boolean result = bloomFilter.processBitMaps(bitmap -> callCounter.getAndIncrement() == 0);

        // Assert
        assertFalse(result, "Processing should be terminated on the second (empty) bitmap");
        assertEquals(2, callCounter.get(), "Predicate should have been called for both bitmaps");
    }
}