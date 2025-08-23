package org.apache.commons.collections4.set;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CompositeSetRefactoredTest {

    /**
     * Tests that addComposited() throws an UnsupportedOperationException when
     * adding a set that has elements in common with an existing set in the composite,
     * and no SetMutator has been configured to resolve the collision.
     */
    @Test
    public void addComposited_shouldThrowUnsupportedOperationException_forCollisionWithoutMutator() {
        // Arrange
        // Create a composite set containing a set with one element.
        Set<String> set1 = new HashSet<>();
        set1.add("collision_element");
        CompositeSet<String> compositeSet = new CompositeSet<>(set1);

        // Create a second set that also contains the same element. This will cause a collision.
        Set<String> set2 = new HashSet<>();
        set2.add("collision_element");

        // Act & Assert
        try {
            // Attempt to add the second set, which should trigger the collision.
            compositeSet.addComposited(set2);
            fail("Expected an UnsupportedOperationException because a collision occurred without a SetMutator.");
        } catch (final UnsupportedOperationException e) {
            // Verify that the exception was thrown for the expected reason.
            final String expectedMessage = "Collision adding composited set with no SetMutator set";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}