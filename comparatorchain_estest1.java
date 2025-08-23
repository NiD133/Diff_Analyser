package org.apache.commons.collections4.comparators;

import org.junit.Test;
import java.util.Comparator;

/**
 * Contains tests for the {@link ComparatorChain} class, focusing on specific edge cases.
 */
public class ComparatorChainTest {

    /**
     * Tests that calling hashCode() on a ComparatorChain that contains a circular
     * reference to itself results in a StackOverflowError.
     *
     * This scenario can occur if a comparator that wraps the chain is added
     * back into the chain itself. The test ensures this edge case behaves as expected
     * (infinite recursion) rather than causing other unexpected errors.
     */
    @Test(timeout = 4000, expected = StackOverflowError.class)
    public void hashCodeShouldThrowStackOverflowErrorOnCircularReference() {
        // Arrange: Create a circular dependency.
        // 1. A ComparatorChain is created.
        // 2. A wrapper comparator is created that delegates to the chain.
        // 3. The wrapper is added to the chain itself.
        final ComparatorChain<Object> chainWithCircularReference = new ComparatorChain<>();
        final Comparator<Object> wrapperComparator = Comparator.nullsLast(chainWithCircularReference);
        chainWithCircularReference.addComparator(wrapperComparator);

        // Act & Assert:
        // Calling hashCode() will lead to infinite recursion:
        // chain.hashCode() -> wrapper.hashCode() -> chain.hashCode() -> ...
        // This is expected to throw a StackOverflowError.
        chainWithCircularReference.hashCode();
    }
}