package org.apache.commons.collections4.set;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class CompositeSet_ESTestTest7 extends CompositeSet_ESTest_scaffolding {

    /**
     * Tests that adding a composited set with an element that already exists
     * in the CompositeSet throws an UnsupportedOperationException if no
     * SetMutator is configured to handle the collision.
     */
    @Test
    public void addCompositedWithOverlappingSetThrowsExceptionWhenNoMutatorIsConfigured() {
        // Arrange
        // Create a set with an element that will cause a collision.
        Set<String> initialSet = new HashSet<>();
        initialSet.add("COLLISION_ELEMENT");

        // Create the CompositeSet containing the initial set.
        CompositeSet<String> compositeSet = new CompositeSet<>(initialSet);

        // Create another set that also contains the collision-causing element.
        Set<String> overlappingSet = new HashSet<>();
        overlappingSet.add("COLLISION_ELEMENT");

        // Act & Assert
        // An exception is expected because we are adding a set with an overlapping
        // element, and no SetMutator has been provided to resolve the collision.
        UnsupportedOperationException exception = assertThrows(
            UnsupportedOperationException.class,
            () -> compositeSet.addComposited(overlappingSet)
        );

        // Verify the exception message to ensure it's the expected error.
        assertEquals("Collision adding composited set with no SetMutator set", exception.getMessage());
    }
}