package org.apache.commons.collections4.comparators;

import static org.junit.Assert.assertFalse;

import java.util.Comparator;
import org.junit.Test;

/**
 * Contains tests for the {@link ComparatorChain} class.
 */
public class ComparatorChainTest {

    /**
     * Tests that modifying a comparator in the chain does not lock the chain.
     * A ComparatorChain should only become locked after the compare() method is called.
     */
    @Test
    public void setComparatorShouldNotLockChain() {
        // Arrange: Create a chain with a single comparator.
        // The chain is initially unlocked by default.
        final ComparatorChain<Object> chain = new ComparatorChain<>((Comparator<Object>) null);

        // Act: Replace the comparator at the first index.
        chain.setComparator(0, null, true);

        // Assert: The chain should remain unlocked after the modification.
        assertFalse("The chain should not be locked by a call to setComparator()", chain.isLocked());
    }
}