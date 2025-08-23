package org.apache.commons.collections4.comparators;

import static org.junit.Assert.assertThrows;

import java.util.BitSet;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;

/**
 * This test case focuses on a specific behavior of the {@link ComparatorChain} class.
 * It has been improved for clarity and maintainability.
 */
public class ComparatorChainTest {

    /**
     * Tests that {@link ComparatorChain#setForwardSort(int)} throws a NullPointerException
     * if the chain was constructed with a null BitSet for ordering.
     */
    @Test
    public void setForwardSortShouldThrowNullPointerExceptionWhenBitSetIsNull() {
        // Arrange: Create a ComparatorChain, passing a null BitSet.
        // The constructor allows a null BitSet, but subsequent operations that
        // rely on it are expected to fail.
        final List<Comparator<Integer>> comparatorList = new LinkedList<>();
        final ComparatorChain<Integer> chain = new ComparatorChain<>(comparatorList, (BitSet) null);

        // Act & Assert: Expect a NullPointerException when trying to modify the sort order.
        assertThrows(NullPointerException.class, () -> chain.setForwardSort(0));
    }
}