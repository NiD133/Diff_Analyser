package org.apache.commons.collections4.set;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Improved, understandable tests for the {@link CompositeSet} class.
 */
public class CompositeSetTest {

    /**
     * Tests that calling remove() with an object that is not an element of the set
     * returns false and does not modify the set. This is a specific test case where
     * the object being removed is one of the underlying component sets, not an element within them.
     */
    @Test
    public void removeShouldReturnFalseWhenObjectIsNotAnElement() {
        // Arrange
        final Object element = "test-element";
        final Set<Object> componentSet = new HashSet<>();
        componentSet.add(element);

        final CompositeSet<Object> compositeSet = new CompositeSet<>(componentSet);
        
        // Pre-assertion to ensure the initial state is correct
        assertTrue("CompositeSet should contain the added element", compositeSet.contains(element));
        assertEquals("CompositeSet should have one element after initialization", 1, compositeSet.size());

        // Act: Attempt to remove the component set itself. The component set is not an *element*
        // of the composite set, so this operation should fail.
        final boolean wasRemoved = compositeSet.remove(componentSet);

        // Assert
        assertFalse("remove() should return false as the component set is not an element", wasRemoved);
        assertEquals("CompositeSet size should not change after a failed remove operation", 1, compositeSet.size());
        assertTrue("CompositeSet should still contain the original element", compositeSet.contains(element));
    }

    /**
     * Tests that the equals() method returns false when comparing a CompositeSet
     * to another Set that has different contents and size.
     */
    @Test
    public void equalsShouldReturnFalseForSetWithDifferentContents() {
        // Arrange
        final Object element = "test-element";
        final Set<Object> componentSet = new HashSet<>();
        componentSet.add(element);

        // A composite set containing one element.
        final CompositeSet<Object> compositeSet = new CompositeSet<>(componentSet);

        // An empty set to compare against.
        final Set<Object> emptySet = new HashSet<>();

        // Act
        final boolean areEqual = compositeSet.equals(emptySet);

        // Assert
        assertFalse("A non-empty CompositeSet should not be equal to an empty Set", areEqual);
    }
}