package org.apache.commons.collections4.comparators;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.Comparator;

/**
 * Contains tests for the {@link ComparatorChain} class, focusing on its size management.
 * The test class name and inheritance are preserved from the original auto-generated code.
 */
public class ComparatorChain_ESTestTest35 extends ComparatorChain_ESTest_scaffolding {

    /**
     * Tests that calling addComparator() on a chain increases its size.
     */
    @Test
    public void addComparatorIncreasesChainSize() {
        // Arrange: Create a ComparatorChain initialized with a single comparator.
        final Comparator<Integer> initialComparator = Comparator.naturalOrder();
        final ComparatorChain<Integer> comparatorChain = new ComparatorChain<>(initialComparator, true);

        // Sanity check the initial state.
        assertEquals("The chain should initially contain one comparator.", 1, comparatorChain.size());

        // Act: Add a second comparator to the chain.
        comparatorChain.addComparator(initialComparator);

        // Assert: The size of the chain should now be 2.
        assertEquals("The size should be 2 after adding a second comparator.", 2, comparatorChain.size());
    }
}