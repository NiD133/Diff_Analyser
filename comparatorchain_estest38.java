package org.apache.commons.collections4.comparators;

import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.collections4.comparators.ComparatorChain}.
 */
public class ComparatorChainTest {

    @Test
    public void newComparatorChainShouldNotBeLocked() {
        // Arrange: Create a new, empty ComparatorChain.
        // A new chain should be modifiable, and therefore not "locked".
        final ComparatorChain<Object> chain = new ComparatorChain<>();

        // Act: Check if the chain is locked.
        final boolean isLocked = chain.isLocked();

        // Assert: The chain should not be locked, allowing for further configuration.
        assertFalse("A newly created ComparatorChain should not be locked", isLocked);
    }
}