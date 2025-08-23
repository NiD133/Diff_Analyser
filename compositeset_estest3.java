package org.apache.commons.collections4.set;

import org.junit.Test;
import java.util.LinkedHashSet;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link CompositeSet#remove(Object)} method.
 */
// The original test class name and inheritance are preserved for context.
public class CompositeSet_ESTestTest3 extends CompositeSet_ESTest_scaffolding {

    /**
     * Tests that calling remove() on a CompositeSet successfully removes the element
     * from the underlying component set and returns true.
     */
    @Test
    public void remove_whenElementExists_shouldRemoveFromUnderlyingSetAndReturnTrue() {
        // Arrange
        final String element = "test-element";
        final Set<Object> componentSet = new LinkedHashSet<>();
        componentSet.add(element);

        // Create the CompositeSet with the component set containing one element.
        final CompositeSet<Object> compositeSet = new CompositeSet<>(componentSet);
        
        // Sanity check to ensure the initial state is correct.
        assertFalse("Precondition failed: The component set should not be empty", componentSet.isEmpty());
        assertEquals("Precondition failed: The composite set should have one element", 1, compositeSet.size());

        // Act
        final boolean wasRemoved = compositeSet.remove(element);

        // Assert
        assertTrue("remove() should return true when an element is successfully removed.", wasRemoved);
        assertTrue("The composite set should be empty after removing its only element.", compositeSet.isEmpty());
        assertTrue("The underlying component set should also be empty after the removal.", componentSet.isEmpty());
    }
}