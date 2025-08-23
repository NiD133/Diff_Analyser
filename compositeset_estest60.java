package org.apache.commons.collections4.set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.Set;
import org.junit.Test;

/**
 * Contains tests for the {@link CompositeSet} class.
 * This improved test focuses on verifying the behavior of mutation methods
 * when no SetMutator is configured.
 */
public class CompositeSetTest {

    /**
     * Tests that calling addAll() on a CompositeSet without a configured
     * SetMutator throws an UnsupportedOperationException.
     *
     * As per the class documentation, mutation operations like addAll() are
     * disabled by default and require a SetMutator strategy to be enabled.
     */
    @Test
    public void addAllShouldThrowUnsupportedOperationExceptionWhenNoMutatorIsSet() {
        // Arrange: Create a CompositeSet without a mutator.
        final CompositeSet<String> compositeSet = new CompositeSet<>();
        final Set<String> collectionToAdd = Collections.emptySet();

        // Act & Assert: Expect an UnsupportedOperationException with a specific message.
        try {
            compositeSet.addAll(collectionToAdd);
            fail("Expected an UnsupportedOperationException to be thrown because no SetMutator was provided.");
        } catch (final UnsupportedOperationException e) {
            // This is the expected behavior.
            final String expectedMessage = "addAll() is not supported on CompositeSet without a SetMutator strategy";
            assertEquals("The exception message should clearly state why the operation is not supported.",
                         expectedMessage, e.getMessage());
        }
    }
}