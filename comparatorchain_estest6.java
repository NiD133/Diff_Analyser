package org.apache.commons.collections4.comparators;

import org.junit.Test;

/**
 * Contains tests for the {@link ComparatorChain} class.
 */
public class ComparatorChainTest {

    /**
     * Tests that calling setReverseSort() with a negative index throws an
     * IndexOutOfBoundsException.
     *
     * The method should not accept negative indices, as it delegates the call
     * to an internal BitSet, which enforces this constraint.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void setReverseSort_withNegativeIndex_shouldThrowException() {
        // Arrange: Create an empty ComparatorChain.
        final ComparatorChain<Object> comparatorChain = new ComparatorChain<>();

        // Act: Attempt to set the sort order using a negative index.
        // This action is expected to throw an IndexOutOfBoundsException.
        comparatorChain.setReverseSort(-1);

        // Assert: The exception is caught and verified by the @Test(expected=...) annotation.
    }
}