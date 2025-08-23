package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import java.util.function.IntPredicate;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.verify;

/**
 * Contains tests for the {@link SimpleBloomFilter#processIndices(IntPredicate)} method.
 */
public class SimpleBloomFilterProcessIndicesTest {

    /**
     * Tests that calling {@code processIndices} on an empty Bloom filter returns true
     * and does not invoke the predicate.
     * <p>
     * The contract of {@code processIndices} states that it should return {@code true} if all
     * indices were processed successfully. For an empty filter, this condition is
     * vacuously true, and the provided predicate should never be called.
     * </p>
     */
    @Test
    public void testProcessIndicesOnEmptyFilterReturnsTrueAndDoesNotCallPredicate() {
        // Arrange: Create an empty Bloom filter and a mock predicate.
        Shape shape = Shape.fromNMK(10, 1, 1); // A simple shape for the filter
        SimpleBloomFilter emptyFilter = new SimpleBloomFilter(shape);
        IntPredicate mockPredicate = mock(IntPredicate.class);

        // Act: Execute the method under test.
        boolean result = emptyFilter.processIndices(mockPredicate);

        // Assert: Verify the expected behavior.
        assertTrue("processIndices on an empty filter should return true", result);
        verify(mockPredicate, never()).test(anyInt());
    }
}