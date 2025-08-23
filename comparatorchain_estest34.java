package org.apache.commons.collections4.comparators;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Contains a specific test case for the {@link ComparatorChain} class,
 * focusing on its behavior with an empty chain.
 */
public class ComparatorChainTest {

    /**
     * Tests that calling {@code setForwardSort()} on an empty ComparatorChain
     * does not throw an exception or modify the chain.
     *
     * This test verifies the graceful handling of an edge case. The method
     * operates on an internal BitSet, which can handle an index like 0 even
     * when the list of comparators is empty. This test ensures that such an
     * operation completes without error and has no unintended side effects,
     * like changing the chain's size.
     */
    @Test
    public void setForwardSort_onEmptyChain_shouldHaveNoEffect() {
        // Arrange: Create an empty ComparatorChain.
        final ComparatorChain<Object> emptyChain = new ComparatorChain<>();

        // Act: Attempt to set the sort order at index 0. This should not throw
        // an IndexOutOfBoundsException despite the chain being empty.
        emptyChain.setForwardSort(0);

        // Assert: Verify that the chain remains empty and its state is unchanged.
        assertEquals("The size of the chain should remain 0 after the operation.", 0, emptyChain.size());
    }
}