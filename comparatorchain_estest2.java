package org.apache.commons.collections4.comparators;

import static org.junit.Assert.assertFalse;

import java.util.Comparator;
import org.junit.Test;

/**
 * Tests for {@link ComparatorChain}.
 */
public class ComparatorChainTest {

    /**
     * Tests that adding comparators to the chain does not lock it.
     * A ComparatorChain should only become locked after the compare() method is called for the first time.
     */
    @Test
    public void testIsLocked_ReturnsFalse_AfterAddingComparators() {
        // Arrange: Create an empty ComparatorChain
        final ComparatorChain<String> comparatorChain = new ComparatorChain<>();
        final Comparator<String> naturalOrderComparator = Comparator.naturalOrder();

        // Act: Add two comparators to the chain
        comparatorChain.addComparator(naturalOrderComparator, true); // descending
        comparatorChain.addComparator(naturalOrderComparator, false); // ascending

        // Assert: Verify that the chain is not yet locked
        assertFalse("The chain should not be locked just by adding comparators.", comparatorChain.isLocked());
    }
}