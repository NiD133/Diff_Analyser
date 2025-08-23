package com.google.gson.internal;

import org.junit.Test;
import java.util.Comparator;
import static org.junit.Assert.assertEquals;
import static org.evosuite.shaded.org.mockito.Mockito.mock;
import org.evosuite.runtime.ViolatedAssumptionAnswer;

public class LinkedTreeMap_ESTestTest5 extends LinkedTreeMap_ESTest_scaffolding {

    /**
     * Tests that the internal `removeInternal` method decrements the map's size
     * even when called with a node that is not part of the map.
     *
     * This test documents a quirk in the implementation: `removeInternal` does not
     * verify that the node belongs to the map, leading to an inconsistent state
     * (a negative size) if misused. This method is not part of the public API.
     */
    @Test(timeout = 4000)
    public void removeInternalWithDetachedNodeDecrementsSizeToNegative() {
        // Arrange: Create an empty map and a standalone node that is not in the map.
        @SuppressWarnings("unchecked")
        Comparator<Object> mockComparator = (Comparator<Object>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        LinkedTreeMap<Object, Object> map = new LinkedTreeMap<>(mockComparator, true);
        
        // This node is "detached"—it's not part of the map's internal tree structure.
        LinkedTreeMap.Node<Object, Object> detachedNode = new LinkedTreeMap.Node<>(true);

        assertEquals("Initial size of an empty map should be 0", 0, map.size());

        // Act: Call the internal remove method with the detached node.
        map.removeInternal(detachedNode, true);

        // Assert: The size is unconditionally decremented, resulting in -1.
        assertEquals("Size should be -1 after removing a detached node", -1, map.size());
    }
}