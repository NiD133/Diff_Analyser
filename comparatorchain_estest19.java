package org.apache.commons.collections4.comparators;

import org.junit.Test;
import java.util.Comparator;
import static org.junit.Assert.assertFalse;

/**
 * This test case focuses on the "locked" state of the {@link ComparatorChain}.
 * A ComparatorChain can be modified until its compare() method is called,
 * at which point it becomes "locked" to ensure consistency.
 */
public class ComparatorChain_ESTestTest19 {

    /**
     * Tests that modifying a ComparatorChain with setComparator() before any
     * comparison has been performed does not lock the chain.
     */
    @Test
    public void setComparatorOnUnlockedChainShouldNotLockIt() {
        // Arrange: Create a chain with a single comparator.
        // Upon creation, the chain is not locked.
        final ComparatorChain<Object> comparatorChain = new ComparatorChain<>((Comparator<Object>) null);

        // Act: Replace the existing comparator. This is a modification that should
        // be allowed on an unlocked chain.
        comparatorChain.setComparator(0, (Comparator<Object>) null, false);

        // Assert: The chain should remain unlocked because the compare() method
        // has not yet been invoked.
        assertFalse("The chain should not be locked after modification, before any comparison is performed.",
                    comparatorChain.isLocked());
    }
}