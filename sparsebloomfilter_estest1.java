package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import java.util.function.IntPredicate;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link SparseBloomFilter}.
 */
public class SparseBloomFilterTest {

    /**
     * Tests that calling processIndices() on an empty filter returns true.
     * The contract states that the method returns true if the predicate is true for all
     * indices. For an empty filter, this condition is vacuously true, and the
     * predicate should never be called.
     */
    @Test
    public void testProcessIndicesOnEmptyFilterReturnsTrue() {
        // Arrange: Create an empty filter with a standard shape.
        Shape shape = Shape.fromKM(10, 1024); // K=10 hash functions, M=1024 bits
        SparseBloomFilter emptyFilter = new SparseBloomFilter(shape);

        // Arrange: Create a predicate that will fail the test if it is ever invoked.
        IntPredicate failingPredicate = index -> {
            fail("Predicate should not be called for an empty filter.");
            return false; // This line is unreachable
        };

        // Act: Process the indices of the empty filter.
        boolean result = emptyFilter.processIndices(failingPredicate);

        // Assert: The result should be true, as no indices were processed.
        assertTrue("processIndices on an empty filter should return true", result);
    }
}