package org.apache.commons.collections4.comparators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * This test case verifies the behavior of the ComparatorChain when it is empty.
 *
 * The original test class name "ComparatorChain_ESTestTest31" and its extension
 * "ComparatorChain_ESTest_scaffolding" are kept to match the input structure.
 * In a real-world scenario, they would be renamed to "ComparatorChainTest"
 * and the need for the scaffolding class would be reviewed.
 */
public class ComparatorChain_ESTestTest31 extends ComparatorChain_ESTest_scaffolding {

    /**
     * Tests that calling the compare() method on a ComparatorChain that contains
     * no comparators throws an UnsupportedOperationException, as specified by
     * the class documentation.
     */
    @Test
    public void compare_onEmptyChain_shouldThrowUnsupportedOperationException() {
        // Arrange: Create a ComparatorChain with no comparators.
        final ComparatorChain<Object> emptyChain = new ComparatorChain<>();
        final String expectedErrorMessage = "ComparatorChains must contain at least one Comparator";

        // Act & Assert: Verify that calling compare() throws the expected exception.
        try {
            // The objects being compared are irrelevant as the exception should be
            // thrown before they are ever used.
            emptyChain.compare(new Object(), new Object());
            fail("Expected an UnsupportedOperationException to be thrown because the chain is empty.");
        } catch (final UnsupportedOperationException e) {
            // Assert that the exception has the expected message.
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}