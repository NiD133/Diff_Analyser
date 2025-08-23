package com.google.gson.internal;

import org.junit.Test;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// The original test class name 'LinkedTreeMap_ESTestTest29' and scaffolding are kept
// to match the provided context.
public class LinkedTreeMap_ESTestTest29 extends LinkedTreeMap_ESTest_scaffolding {

    /**
     * Tests that removing an existing key from the KeySet view correctly
     * removes the entry from the underlying map and returns true.
     */
    @Test
    public void keySetRemove_whenKeyExists_removesElementAndReturnsTrue() {
        // Arrange: Create a map and add a single element.
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        Integer key = -2139;
        map.put(key, key);
        assertEquals("Precondition failed: Map should contain one element", 1, map.size());

        // Act: Get the key set and remove the element from it.
        Set<Integer> keySet = map.keySet();
        boolean wasRemoved = keySet.remove(key);

        // Assert: Verify the removal was successful and the map is now empty.
        assertTrue("remove() should return true for an existing element.", wasRemoved);
        assertTrue("The key set should be empty after removal.", keySet.isEmpty());
        assertEquals("The map's size should be 0 after removing the element via its key set.", 0, map.size());
        assertFalse("The map should no longer contain the removed key.", map.containsKey(key));
    }
}