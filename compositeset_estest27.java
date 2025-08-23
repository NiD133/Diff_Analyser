package org.apache.commons.collections4.set;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Contains tests for the {@link CompositeSet} class.
 */
public class CompositeSetTest {

    /**
     * Tests that calling remove() on an empty CompositeSet returns false.
     *
     * This test verifies that attempting to remove any object from a CompositeSet
     * that contains no elements will result in a 'false' return value, and the
     * set's state remains unchanged.
     */
    @Test
    public void removeShouldReturnFalseWhenElementIsNotPresentInEmptySet() {
        // Arrange
        // Create a CompositeSet containing a single, empty component set.
        // The CompositeSet itself is therefore empty.
        Set<Object> componentSet = new LinkedHashSet<>();
        CompositeSet<Object> compositeSet = new CompositeSet<>(componentSet);

        // Verify the initial state: the set is empty.
        assertTrue("Precondition: The composite set should be empty.", compositeSet.isEmpty());

        // The object to be removed is the component set itself. Since the composite set
        // is empty, this object is guaranteed not to be an element.
        Object objectToRemove = componentSet;

        // Act
        boolean wasRemoved = compositeSet.remove(objectToRemove);

        // Assert
        assertFalse("remove() should return false as the object was not in the set.", wasRemoved);
        assertTrue("The composite set should remain empty after the remove operation.", compositeSet.isEmpty());
    }
}