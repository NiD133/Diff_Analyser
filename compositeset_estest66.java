package org.apache.commons.collections4.set;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link CompositeSet} class, focusing on its construction
 * and handling of set collisions.
 */
public class CompositeSetTest {

    /**
     * Tests that the CompositeSet constructor throws an UnsupportedOperationException
     * when initialized with sets that contain common elements (a collision),
     * and no SetMutator has been provided to resolve it.
     */
    @Test
    public void constructorShouldThrowExceptionOnSetCollisionWhenNoMutatorIsDefined() {
        // Arrange: Create two distinct sets that share a common element.
        final Integer collidingElement = 42;

        final Set<Integer> set1 = new HashSet<>();
        set1.add(collidingElement);
        set1.add(1);

        final Set<Integer> set2 = new HashSet<>();
        set2.add(collidingElement);
        set2.add(2);

        @SuppressWarnings("unchecked") // Creating an array of generic types is safe here
        final Set<Integer>[] collidingSets = new Set[]{set1, set2};
        final String expectedMessage = "Collision adding composited set with no SetMutator set";

        // Act & Assert
        try {
            new CompositeSet<>(collidingSets);
            fail("Expected an UnsupportedOperationException due to set collision without a mutator.");
        } catch (final UnsupportedOperationException e) {
            // Verify that the correct exception with the expected message was thrown.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}