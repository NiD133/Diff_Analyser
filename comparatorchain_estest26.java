package org.apache.commons.collections4.comparators;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

/**
 * This test class focuses on the behavior of the ComparatorChain.equals() method.
 * The original class name and inheritance are kept to match the input structure.
 */
public class ComparatorChain_ESTestTest26 extends ComparatorChain_ESTest_scaffolding {

    /**
     * Verifies that two ComparatorChains are not equal if their underlying
     * lists of comparators are different.
     */
    @Test
    public void testEqualsReturnsFalseForChainsWithDifferentComparatorLists() {
        // Arrange
        // Create an empty comparator chain, which has no comparators in its internal list.
        final ComparatorChain<Object> emptyChain = new ComparatorChain<>();

        // Create a second chain that contains the first (empty) chain as its single comparator.
        // Its internal list of comparators contains one element.
        final ComparatorChain<Object> chainWithOneComparator = new ComparatorChain<>(emptyChain, true);

        // Act & Assert
        // The two chains should not be equal because their internal comparator lists
        // differ in size and content, which is what the equals() method checks.
        assertNotEquals("Chains with different comparator lists should not be equal",
                        emptyChain, chainWithOneComparator);
    }
}