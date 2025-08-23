package org.apache.commons.collections4.set;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link CompositeSet}.
 * This refactored test focuses on a specific failure scenario.
 */
public class CompositeSetTest {

    /**
     * Tests that the CompositeSet constructor throws an UnsupportedOperationException
     * when initialized with sets that contain common elements (a collision),
     * and no SetMutator has been provided to resolve it.
     */
    @Test
    public void constructorWithOverlappingSetsAndNoMutatorThrowsException() {
        // Arrange: Create two sets with a common element to cause a collision.
        Set<String> set1 = new HashSet<>();
        set1.add("A");
        set1.add("B"); // The overlapping element

        Set<String> set2 = new HashSet<>();
        set2.add("B"); // The overlapping element
        set2.add("C");

        // Act & Assert: Expect an exception when creating the CompositeSet.
        // The constructor attempts to add the sets, detects the collision, and
        // throws because no resolution strategy (SetMutator) is defined.
        UnsupportedOperationException exception = assertThrows(
            UnsupportedOperationException.class,
            () -> new CompositeSet<>(set1, set2)
        );

        // Assert: Verify the exception message clearly indicates the cause of the failure.
        assertEquals("Collision adding composited set with no SetMutator set", exception.getMessage());
    }
}