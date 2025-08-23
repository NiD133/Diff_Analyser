package org.apache.commons.collections4.set;

import org.junit.Test;
import java.util.LinkedHashSet;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link CompositeSet} class.
 */
public class CompositeSetTest {

    /**
     * Tests that calling addComposited() with a set that is already part of the composite
     * adds the set instance again to the internal list of sets.
     */
    @Test
    public void addComposited_withExistingEmptySet_addsSetAgain() {
        // Arrange
        // Create an empty set that will be a component of the composite set.
        final Set<Integer> componentSet = new LinkedHashSet<>();

        // Create a composite set, initially containing the single empty component set.
        final CompositeSet<Integer> compositeSet = new CompositeSet<>(componentSet);

        // Verify initial state: one component set, but zero total elements.
        assertEquals("CompositeSet should initially contain one component set.", 1, compositeSet.getSets().size());
        assertTrue("CompositeSet should initially be empty.", compositeSet.isEmpty());

        // Act
        // Add the *same* empty set instance to the composite set again.
        compositeSet.addComposited(componentSet);

        // Assert
        // The composite set should now hold two references to the same empty set.
        assertEquals("CompositeSet should now contain two component sets.", 2, compositeSet.getSets().size());
        
        // The overall contents and size of the composite set should remain unchanged (still empty).
        assertTrue("CompositeSet should still be empty after the operation.", compositeSet.isEmpty());
        assertEquals("The size of the composite set should still be 0.", 0, compositeSet.size());
    }
}