package org.apache.commons.collections4.set;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashSet;
import java.util.Set;

public class CompositeSet_ESTestTest54 extends CompositeSet_ESTest_scaffolding {

    /**
     * Tests that an element can be removed from a CompositeSet even when that element
     * is itself a Set. The removal should be based on object equality (`.equals()`),
     * not instance identity.
     */
    @Test
    public void testRemoveSucceedsWhenElementIsASet() {
        // --- Arrange ---

        // 1. Create a component set that will be part of the CompositeSet.
        Set<Object> componentSet = new LinkedHashSet<>();

        // 2. Create an empty set and add it as an *element* to the component set.
        Set<Object> elementSet = new LinkedHashSet<>();
        componentSet.add(elementSet);

        // 3. Create the CompositeSet. It now contains one element: the empty elementSet.
        CompositeSet<Object> compositeSet = new CompositeSet<>(componentSet);
        assertEquals("Pre-condition: CompositeSet should contain one element", 1, compositeSet.size());

        // 4. Create another empty set. It is a different instance but is `equals()`
        //    to the elementSet already in the compositeSet.
        Set<Object> equivalentElementToRemove = new LinkedHashSet<>();

        // --- Act ---

        // Attempt to remove the element using the equivalent (but not identical) set.
        boolean wasRemoved = compositeSet.remove(equivalentElementToRemove);

        // --- Assert ---

        // The remove operation should be successful.
        assertTrue("remove() should return true when an element is successfully removed", wasRemoved);

        // The CompositeSet and its underlying component set should now be empty.
        assertTrue("CompositeSet should be empty after removal", compositeSet.isEmpty());
        assertTrue("The underlying component set should also be empty after removal", componentSet.isEmpty());
    }
}