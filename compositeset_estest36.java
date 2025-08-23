package org.apache.commons.collections4.set;

import org.junit.Test;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This class contains tests for the {@link CompositeSet} class.
 * The original test class name and scaffolding suggest it was auto-generated.
 * This version has been refactored for improved clarity and maintainability.
 */
public class CompositeSet_ESTestTest36 {

    /**
     * Tests that calling addComposited() with a set that is already part of the
     * composite throws an UnsupportedOperationException if no SetMutator is configured.
     * This situation is considered a "collision," which requires a mutator to resolve.
     */
    @Test
    public void addCompositedShouldThrowExceptionForDuplicateSetWhenNoMutatorIsSet() {
        // Arrange: Create a CompositeSet with one underlying set.
        Set<Integer> initialSet = new LinkedHashSet<>();
        initialSet.add(100);

        CompositeSet<Integer> compositeSet = new CompositeSet<>(initialSet);

        // Sanity check to ensure the initial state is as expected.
        assertEquals("CompositeSet should have one element before the test action.", 1, compositeSet.size());
        assertTrue("CompositeSet should contain the element from the initial set.", compositeSet.contains(100));

        // Act & Assert: Attempt to add the same set again, which should cause a collision.
        try {
            compositeSet.addComposited(initialSet);
            fail("Expected an UnsupportedOperationException because a duplicate set was added without a SetMutator to resolve the collision.");
        } catch (final UnsupportedOperationException e) {
            // Verify that the correct exception was thrown with the expected message.
            final String expectedMessage = "Collision adding composited set with no SetMutator set";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}