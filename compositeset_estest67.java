package org.apache.commons.collections4.set;

import org.junit.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This class contains tests for the CompositeSet class.
 * The original test class name is kept for context, but in a real-world scenario,
 * this test would be part of a larger, more descriptively named test suite like 'CompositeSetTest'.
 */
public class CompositeSet_ESTestTest67 extends CompositeSet_ESTest_scaffolding {

    /**
     * Tests that adding a composited set which causes a collision (i.e., has
     * common elements with an existing set) throws an UnsupportedOperationException
     * when no SetMutator is configured to resolve the collision.
     */
    @Test
    public void addCompositedShouldThrowExceptionOnCollisionWhenNoMutatorIsSet() {
        // Arrange: Create a set with an element and a CompositeSet containing it.
        final Set<String> initialSet = new HashSet<>();
        initialSet.add("common-element");

        final CompositeSet<String> compositeSet = new CompositeSet<>(initialSet);

        // Act & Assert: Attempting to add the same set again will cause a collision
        // because "common-element" exists in both the existing set and the one being added.
        // Without a SetMutator to handle this, an exception is expected.
        try {
            compositeSet.addComposited(initialSet);
            fail("Expected an UnsupportedOperationException due to collision without a SetMutator.");
        } catch (final UnsupportedOperationException e) {
            // Verify that the correct exception was thrown with the expected message.
            final String expectedMessage = "Collision adding composited set with no SetMutator set";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}