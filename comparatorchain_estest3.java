package org.apache.commons.collections4.comparators;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link ComparatorChain} class.
 */
public class ComparatorChainTest {

    /**
     * Tests that calling setReverseSort() with an index on an empty chain
     * does not throw an exception and does not change the size of the chain.
     *
     * <p>This behavior is valid because the underlying BitSet that tracks sort
     * order simply expands to accommodate the index, even if no comparator
     * exists at that index yet.</p>
     */
    @Test
    public void setReverseSortOnEmptyChainShouldNotAffectSize() {
        // Arrange: Create an empty ComparatorChain.
        final ComparatorChain<String> emptyChain = new ComparatorChain<>();
        assertEquals("The chain should be initially empty", 0, emptyChain.size());

        // Act: Set the sort order for an index. Since the chain is empty, this
        // index is effectively out of bounds for the comparator list. This action
        // should not throw an exception.
        final int anIndex = 5;
        emptyChain.setReverseSort(anIndex);

        // Assert: The size of the chain should remain unchanged because
        // setReverseSort() only modifies the sort order, not the list of comparators.
        assertEquals("The chain size should still be 0", 0, emptyChain.size());
    }
}