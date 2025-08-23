package com.google.gson.internal;

import org.junit.Test;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// Note: The original test class name and inheritance are preserved.
public class LinkedTreeMap_ESTestTest74 extends LinkedTreeMap_ESTest_scaffolding {

    @Test
    public void keySetClear_shouldAlsoClearTheUnderlyingMap() {
        // Arrange: Create a map and add some elements.
        LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        Set<String> keySet = map.keySet();
        assertFalse("Precondition failed: Map should not be empty before clear", map.isEmpty());
        assertEquals("Precondition failed: KeySet size should be 3 before clear", 3, keySet.size());

        // Act: Clear the key set.
        keySet.clear();

        // Assert: Verify that both the key set and the map are now empty.
        assertTrue("KeySet should be empty after clear", keySet.isEmpty());
        assertTrue("Map should be empty after its key set is cleared", map.isEmpty());
        assertEquals("Map size should be 0 after its key set is cleared", 0, map.size());
    }
}