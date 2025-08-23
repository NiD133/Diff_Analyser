package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.function.IntPredicate;

/**
 * Contains tests for the {@link SimpleBloomFilter} class.
 */
public class SimpleBloomFilterTest {

    /**
     * Tests that {@code processIndices} correctly returns {@code false} and stops
     * processing as soon as the provided predicate returns {@code false}.
     */
    @Test
    public void processIndicesShouldReturnFalseWhenPredicateReturnsFalse() {
        // Arrange
        // 1. Create a filter with a specific shape.
        Shape shape = Shape.fromKM(10, 100); // 10 hash functions, 100 bits
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);

        // 2. Populate the filter with some indices by merging a hasher.
        // This ensures there are active indices to be processed.
        Hasher hasher = new EnhancedDoubleHasher("Hello, World!".getBytes());
        boolean wasFilterChanged = filter.merge(hasher);
        assertTrue("The filter should be modified by the merge operation", wasFilterChanged);
        assertFalse("The filter should not be empty after merging", filter.isEmpty());

        // 3. Define a predicate that always returns false.
        // This predicate will cause the processing to stop on the very first index.
        IntPredicate predicateThatStopsProcessing = index -> false;

        // Act
        // Attempt to process the indices. The method should return false because
        // the predicate will immediately return false.
        boolean processingResult = filter.processIndices(predicateThatStopsProcessing);

        // Assert
        // Verify that the processing was halted and returned false as expected.
        assertFalse("processIndices should return false if the predicate returns false", processingResult);
    }
}