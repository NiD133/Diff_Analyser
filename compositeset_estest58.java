package org.apache.commons.collections4.set;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link CompositeSet} class.
 */
public class CompositeSetTest {

    /**
     * Verifies that calling add() on a CompositeSet throws an UnsupportedOperationException
     * when no SetMutator strategy has been configured. The add operation is intentionally
     * unsupported by default and requires explicit configuration.
     */
    @Test
    public void addShouldThrowUnsupportedOperationExceptionWhenNoMutatorIsConfigured() {
        // Arrange: Create a CompositeSet without a mutator.
        CompositeSet<String> compositeSet = new CompositeSet<>();

        // Act & Assert
        try {
            compositeSet.add("any-element");
            fail("Expected an UnsupportedOperationException to be thrown.");
        } catch (UnsupportedOperationException e) {
            // Verify that the exception has the expected message.
            String expectedMessage = "add() is not supported on CompositeSet without a SetMutator strategy";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}