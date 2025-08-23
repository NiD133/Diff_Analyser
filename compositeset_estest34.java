package org.apache.commons.collections4.set;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains improved tests for the {@link CompositeSet} class.
 */
public class CompositeSetImprovedTest {

    /**
     * Tests that calling addComposited() with two sets that have common elements
     * throws an UnsupportedOperationException when no SetMutator is configured.
     * A SetMutator is required to resolve such collisions.
     */
    @Test
    public void addCompositedWithOverlappingSetsAndNoMutatorShouldThrowException() {
        // Arrange: Create two sets with a common element ("B").
        final Set<String> set1 = new HashSet<>(Arrays.asList("A", "B"));
        final Set<String> set2 = new HashSet<>(Arrays.asList("B", "C"));
        final CompositeSet<String> compositeSet = new CompositeSet<>();

        // Act & Assert: Attempting to add both sets should cause a collision
        // and throw an exception because no collision resolution strategy (SetMutator) is defined.
        try {
            compositeSet.addComposited(set1, set2);
            fail("Expected an UnsupportedOperationException due to set collision without a mutator.");
        } catch (final UnsupportedOperationException e) {
            // Verify the exception message is as expected.
            final String expectedMessage = "Collision adding composited set with no SetMutator set";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}