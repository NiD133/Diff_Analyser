package org.apache.commons.collections4.set;

import org.junit.Test;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Contains tests for {@link CompositeSet}.
 * This class focuses on specific scenarios and edge cases.
 */
public class CompositeSetTest {

    /**
     * Tests that adding a composited set that causes a collision (i.e., contains
     * overlapping elements) throws an UnsupportedOperationException when no
     * SetMutator is configured to resolve the conflict.
     */
    @Test
    public void addComposited_withCollidingSetAndNoMutator_throwsUnsupportedOperationException() {
        // Arrange: Create a composite set with one component set containing an element.
        Set<Integer> initialSet = new LinkedHashSet<>();
        initialSet.add(-468);

        CompositeSet<Integer> compositeSet = new CompositeSet<>(initialSet);

        // Act & Assert: Attempting to add the same set again causes a collision.
        // Without a SetMutator to resolve it, an exception is expected.
        UnsupportedOperationException thrown = assertThrows(
            UnsupportedOperationException.class,
            () -> compositeSet.addComposited(initialSet)
        );

        // Verify the exception message to ensure it's the correct error.
        assertEquals("Collision adding composited set with no SetMutator set", thrown.getMessage());
    }
}