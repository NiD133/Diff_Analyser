package org.apache.commons.collections4.set;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

// The original test class name and scaffolding are kept to match the request context.
public class CompositeSet_ESTestTest35 extends CompositeSet_ESTest_scaffolding {

    /**
     * Tests that calling removeAll on a CompositeSet correctly removes elements
     * from the underlying component set.
     */
    @Test
    public void testRemoveAllModifiesUnderlyingSet() {
        // --- Arrange ---
        // Create a component set and add an element to it.
        Set<Object> componentSet = new HashSet<>();
        Object element = new Object();
        componentSet.add(element);

        // Create the CompositeSet using the component set.
        CompositeSet<Object> compositeSet = new CompositeSet<>(componentSet);

        // Verify the initial state: the composite set contains the element.
        assertEquals("CompositeSet should have one element before removal", 1, compositeSet.size());
        assertTrue("CompositeSet should contain the element before removal", compositeSet.contains(element));

        // --- Act ---
        // Call removeAll with a collection containing the element to be removed.
        // In this case, we use the component set itself as the argument.
        boolean wasModified = compositeSet.removeAll(componentSet);

        // --- Assert ---
        // Verify that the removeAll operation reported a change.
        assertTrue("removeAll should return true as the set was modified", wasModified);

        // Verify that the element was removed from both the composite set
        // and, crucially, the underlying component set.
        assertTrue("CompositeSet should be empty after removeAll", compositeSet.isEmpty());
        assertTrue("The underlying component set should also be empty", componentSet.isEmpty());
        assertFalse("The element should no longer be in the component set", componentSet.contains(element));
    }
}