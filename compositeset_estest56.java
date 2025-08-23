package org.apache.commons.collections4.set;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class contains improved versions of test cases for CompositeSet.
 * The original test was auto-generated and has been rewritten for clarity.
 */
public class CompositeSet_ESTestTest56 extends CompositeSet_ESTest_scaffolding {

    /**
     * Tests that calling remove() on an empty CompositeSet always returns false,
     * even when the object to be removed is another, equal CompositeSet.
     */
    @Test
    public void removeNonContainedObjectFromEmptySetShouldReturnFalse() {
        // Arrange
        // Create an empty composite set.
        final CompositeSet<Object> emptySet = new CompositeSet<>();

        // Create another empty composite set to use as the object for removal.
        // Note: This object is .equals() to the first set, but it is not an
        // element *within* the first set.
        final CompositeSet<Object> objectToRemove = new CompositeSet<>();
        assertEquals("Precondition: The two empty sets must be equal.", emptySet, objectToRemove);

        // Act
        // Attempt to remove the object from the empty set.
        final boolean wasRemoved = emptySet.remove(objectToRemove);

        // Assert
        // The remove operation should return false as the set is empty and does not contain the object.
        assertFalse("remove() should return false when called on an empty set.", wasRemoved);
        
        // The set's state should not have changed.
        assertTrue("The set should remain empty after the failed remove operation.", emptySet.isEmpty());
    }
}