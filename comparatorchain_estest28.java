package org.apache.commons.collections4.comparators;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * This test class focuses on the equals() method of the ComparatorChain.
 * Note: This class name is preserved from the original for context, but in a real-world
 * scenario, this test case would be merged into a comprehensive ComparatorChainTest class.
 */
public class ComparatorChain_ESTestTest28 extends ComparatorChain_ESTest_scaffolding {

    /**
     * Tests that the equals() method correctly returns false when comparing a
     * ComparatorChain instance to null.
     */
    @Test
    public void equalsShouldReturnFalseForNullInput() {
        // Arrange: Create an empty ComparatorChain instance.
        final ComparatorChain<Object> chain = new ComparatorChain<>();

        // Act & Assert: Verify that comparing the instance to null returns false.
        assertFalse("A ComparatorChain instance should not be equal to null.", chain.equals(null));
    }
}