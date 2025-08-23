package org.apache.commons.collections4.set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

/**
 * Tests for {@link CompositeSet}.
 */
public class CompositeSetTest {

    /**
     * Tests that the constructor throws an UnsupportedOperationException when attempting
     * to composite sets that contain common elements, if no SetMutator is provided
     * to resolve the collision.
     */
    @Test
    public void constructorShouldThrowExceptionOnElementCollisionWithoutMutator() {
        // Arrange: Create two references to the same set, which contains an element.
        // This guarantees an element collision when creating the CompositeSet.
        final Set<Integer> sharedSet = new HashSet<>();
        sharedSet.add(123);

        final Set<Integer>[] setsWithCollision = new Set[]{sharedSet, sharedSet};

        // Act & Assert
        try {
            new CompositeSet<>(setsWithCollision);
            fail("Expected an UnsupportedOperationException due to element collision without a mutator.");
        } catch (final UnsupportedOperationException e) {
            // Verify the exception message confirms the cause of the failure.
            final String expectedMessage = "Collision adding composited set with no SetMutator set";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}