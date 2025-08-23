package org.apache.commons.collections4.comparators;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.BitSet;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Tests for {@link ComparatorChain}.
 * This test focuses on adding a comparator with ascending order.
 */
public class ComparatorChain_ESTestTest21 { // Original class name kept for context

    /**
     * Tests that adding a comparator with ascending order (reverse=false) to an
     * empty chain correctly increases its size and leaves the ordering bit unset.
     */
    @Test
    public void addComparatorWithAscendingOrderToEmptyChain() {
        // Arrange: Create an empty ComparatorChain.
        // The chain is constructed with direct references to the list and bitset,
        // so modifications to the chain will affect these external objects.
        final List<Comparator<Integer>> comparatorList = new LinkedList<>();
        final BitSet orderingBits = new BitSet();
        final ComparatorChain<Integer> chain = new ComparatorChain<>(comparatorList, orderingBits);

        assertEquals("Chain should be empty initially", 0, chain.size());

        // Act: Add a comparator to the chain with ascending order.
        // Note: The chain itself is used as the comparator, which is valid as it implements Comparator.
        chain.addComparator(chain, false);

        // Assert: Verify the chain's state is updated as expected.
        assertEquals("Size should be 1 after adding one comparator", 1, chain.size());

        // For ascending order, the corresponding bit in orderingBits should be 'false'.
        // Therefore, the cardinality (number of 'true' bits) should remain 0.
        assertEquals("Cardinality of ordering bits should be 0 for ascending order", 0, orderingBits.cardinality());
    }
}