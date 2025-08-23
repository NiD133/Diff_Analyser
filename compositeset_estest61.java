package org.apache.commons.collections4.set;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class contains tests for the {@link CompositeSet} class.
 * This particular test focuses on the behavior of modification methods
 * when no SetMutator is configured.
 */
// The original test class name is kept for context.
public class CompositeSet_ESTestTest61 {

    /**
     * Verifies that calling addAll() on a CompositeSet without a configured
     * SetMutator throws an UnsupportedOperationException.
     */
    @Test
    public void addAllShouldThrowUnsupportedOperationExceptionWhenNoMutatorIsSet() {
        // --- Arrange ---
        // Create a composite set with an initial member set.
        final Set<String> initialSet = new HashSet<>(Arrays.asList("a", "b"));
        final CompositeSet<String> compositeSet = new CompositeSet<>(initialSet);

        // Create a collection of elements to add.
        final Collection<String> collectionToAdd = Arrays.asList("c", "d");

        // --- Act & Assert ---
        try {
            compositeSet.addAll(collectionToAdd);
            fail("Expected an UnsupportedOperationException to be thrown because no SetMutator was configured.");
        } catch (final UnsupportedOperationException e) {
            // Verify that the exception has the expected message, confirming the cause of the failure.
            final String expectedMessage = "addAll() is not supported on CompositeSet without a SetMutator strategy";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}