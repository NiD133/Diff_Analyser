package org.apache.commons.collections4.comparators;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.Comparator;

/**
 * This test class contains an improved version of a test for the ComparatorChain.
 * The original test was functionally correct but difficult to understand.
 * This version focuses on clarity and maintainability.
 */
public class ComparatorChain_ESTestTest12 extends ComparatorChain_ESTest_scaffolding {

    /**
     * Tests that attempting to modify a ComparatorChain after it has been used
     * for a comparison (and is thus "locked") results in an UnsupportedOperationException.
     */
    @Test
    public void setComparator_onLockedChain_throwsUnsupportedOperationException() {
        // Arrange: Create a ComparatorChain with a single, simple comparator.
        final Comparator<String> initialComparator = String.CASE_INSENSITIVE_ORDER;
        final ComparatorChain<String> chain = new ComparatorChain<>(initialComparator);

        // Act: The first call to compare() locks the chain, preventing further modifications.
        // The actual result of the comparison is not important for this test.
        chain.compare("a", "b");

        // Sanity check to confirm the chain is now locked.
        assertTrue("The chain should be locked after the first comparison.", chain.isLocked());

        // Assert: Attempting to replace a comparator in the locked chain should throw an exception.
        try {
            chain.setComparator(0, Comparator.naturalOrder());
            fail("Expected an UnsupportedOperationException because the chain is locked.");
        } catch (final UnsupportedOperationException e) {
            // This is the expected outcome.
            // We also verify the exception message to ensure it's failing for the right reason.
            final String expectedMessage =
                "Comparator ordering cannot be changed after the first comparison is performed";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}