package org.apache.commons.collections4.set;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link CompositeSet} focusing on adding composited sets.
 */
public class CompositeSetTest {

    /**
     * Tests that addComposited() throws an UnsupportedOperationException when
     * attempting to add a set that has common elements with an existing set
     * (a "collision"), and no SetMutator has been configured to resolve it.
     */
    @Test
    public void addCompositedShouldThrowExceptionOnCollisionWhenNoMutatorIsSet() {
        // Arrange: Create a CompositeSet that already contains a set with an element.
        final Set<String> initialSet = new HashSet<>();
        initialSet.add("collision_element");

        final CompositeSet<String> compositeSet = new CompositeSet<>(initialSet);

        // Act & Assert: Attempting to add the same set again should cause a collision
        // and throw an exception because no SetMutator is defined to handle it.
        final UnsupportedOperationException exception = assertThrows(
            UnsupportedOperationException.class,
            () -> compositeSet.addComposited(initialSet)
        );

        // Verify the exception message is as expected.
        assertEquals("Collision adding composited set with no SetMutator set", exception.getMessage());
    }
}