package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.function.LongPredicate;

/**
 * Contains tests for the {@link SparseBloomFilter#processBitMaps(LongPredicate)} method.
 */
public class SparseBloomFilterProcessBitMapsTest {

    /**
     * Verifies that processBitMaps() returns false for an empty filter.
     *
     * <p>The contract of the method specifies that it should return false if no
     * bitmaps were processed. This test ensures that for an empty filter, the
     * predicate is never called and the method correctly returns false.</p>
     */
    @Test
    public void testProcessBitMapsOnEmptyFilterReturnsFalse() {
        // Arrange: Create an empty SparseBloomFilter.
        final Shape shape = Shape.fromKM(10, 100); // A typical, representative shape.
        final SparseBloomFilter emptyFilter = new SparseBloomFilter(shape);

        // A predicate that will fail the test if it is ever invoked.
        final LongPredicate failingPredicate = bitmap -> {
            fail("Predicate should not be called for an empty filter.");
            return true; // This line is unreachable.
        };

        // Act: Process the bitmaps of the empty filter.
        final boolean result = emptyFilter.processBitMaps(failingPredicate);

        // Assert: The method should return false, indicating no bitmaps were processed.
        assertFalse("processBitMaps() should return false for an empty filter.", result);
    }
}