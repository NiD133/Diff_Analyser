package org.apache.commons.collections4.set;

import org.junit.Test;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link CompositeSet}.
 * This class focuses on the behavior of the constructor when handling colliding sets.
 */
public class CompositeSetRefactoredTest {

    /**
     * Verifies that the CompositeSet constructor throws an UnsupportedOperationException
     * when initialized with sets that have overlapping elements, and no SetMutator
     * has been configured to resolve such collisions.
     */
    @Test
    public void constructorShouldThrowExceptionOnElementCollisionWhenNoMutatorIsSet() {
        // Arrange: Create two distinct sets that share a common element.
        // A CompositeSet should not allow overlapping member sets by default.
        final LinkedHashSet<Integer> commonElement = new LinkedHashSet<>();

        final Set<Set<Integer>> set1 = new LinkedHashSet<>();
        set1.add(commonElement);

        final Set<Set<Integer>> set2 = new LinkedHashSet<>();
        set2.add(commonElement);

        // Act & Assert: Attempt to create a CompositeSet with these colliding sets.
        // This should fail because no strategy (SetMutator) for resolving the
        // collision has been provided.
        try {
            new CompositeSet<>(set1, set2);
            fail("Expected an UnsupportedOperationException due to set collision");
        } catch (final UnsupportedOperationException e) {
            // Verify the exception message to ensure it's the expected error.
            final String expectedMessage = "Collision adding composited set with no SetMutator set";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}