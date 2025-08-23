package org.apache.commons.collections4.comparators;

import org.junit.Test;
import java.util.Comparator;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link ComparatorChain}.
 * This class focuses on improving the understandability of a specific test case.
 */
public class ComparatorChainTest {

    /**
     * Tests that setComparator() replaces an existing comparator at a given index
     * without changing the size of the chain.
     */
    @Test
    public void setComparatorShouldReplaceElementWithoutChangingChainSize() {
        // Arrange: Create a ComparatorChain with a single, initial comparator.
        final Comparator<String> initialComparator = Comparator.naturalOrder();
        final ComparatorChain<String> chain = new ComparatorChain<>(initialComparator);
        assertEquals("Pre-condition: chain should have one comparator", 1, chain.size());

        // Define a new comparator to replace the initial one.
        final Comparator<String> newComparator = Comparator.reverseOrder();

        // Act: Replace the comparator at index 0.
        chain.setComparator(0, newComparator);

        // Assert: The size of the chain should remain unchanged after the replacement.
        assertEquals("Post-condition: chain size should still be 1", 1, chain.size());
    }
}