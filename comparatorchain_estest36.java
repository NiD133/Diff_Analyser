package org.apache.commons.collections4.comparators;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

// Note: The original class name and extension are retained for context.
// A more conventional name would be "ComparatorChainTest".
public class ComparatorChain_ESTestTest36 extends ComparatorChain_ESTest_scaffolding {

    /**
     * Tests that the size() method returns 0 for a newly created, empty ComparatorChain.
     */
    @Test
    public void sizeOfNewEmptyChainShouldBeZero() {
        // Arrange: Create a new ComparatorChain using the default constructor.
        // This chain is expected to be empty.
        final ComparatorChain<Object> emptyChain = new ComparatorChain<>();

        // Act: Get the size of the newly created chain.
        final int size = emptyChain.size();

        // Assert: Verify that the size is 0.
        assertEquals(0, size);
    }
}