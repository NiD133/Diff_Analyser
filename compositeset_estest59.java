package org.apache.commons.collections4.set;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link CompositeSet} class, focusing on mutation operations.
 * This class is an improved version of an auto-generated test.
 */
public class CompositeSetMutationTest {

    /**
     * Verifies that calling the add() method on a CompositeSet throws an
     * UnsupportedOperationException if no SetMutator strategy has been configured.
     * This is the expected behavior as per the class documentation.
     */
    @Test
    public void add_withoutMutator_shouldThrowUnsupportedOperationException() {
        // Arrange: Create a CompositeSet without configuring a SetMutator strategy.
        final CompositeSet<Integer> compositeSet = new CompositeSet<>();
        final Integer elementToAdd = 42;

        // Act & Assert: Verify that attempting to add an element throws the expected exception.
        try {
            compositeSet.add(elementToAdd);
            fail("Expected an UnsupportedOperationException because no SetMutator was configured.");
        } catch (final UnsupportedOperationException e) {
            // The exception is expected. Now, verify its message for correctness.
            final String expectedMessage = "add() is not supported on CompositeSet without a SetMutator strategy";
            assertEquals("The exception message should match the expected text.", expectedMessage, e.getMessage());
        }
    }
}