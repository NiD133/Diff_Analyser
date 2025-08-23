package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

// The original test class name and inheritance are kept for context.
// In a real-world scenario, the 'ESTestTest6' suffix would also be improved.
public class LinkedTreeMap_ESTestTest6 extends LinkedTreeMap_ESTest_scaffolding {

    /**
     * Verifies that calling removeInternalByKey on an empty map with a key of an
     * incompatible type returns null and does not modify the map.
     *
     * The implementation is expected to gracefully handle the internal ClassCastException
     * that occurs when trying to compare the incompatible key.
     */
    @Test
    public void removeInternalByKey_onEmptyMapWithIncompatibleKeyType_returnsNull() {
        // Arrange: Create an empty map that expects Integer keys.
        LinkedTreeMap<Integer, Object> map = new LinkedTreeMap<>();
        // Use a key of an incompatible type (a String) to test graceful failure.
        Object incompatibleKey = "this key is a string, not an integer";

        // Act: Attempt to remove an entry using the incompatible key.
        LinkedTreeMap.Node<Integer, Object> removedNode = map.removeInternalByKey(incompatibleKey);

        // Assert: Verify that no node was removed and the map remains unchanged.
        assertNull("removeInternalByKey should return null when the key is not found.", removedNode);
        assertEquals("The map size should remain 0.", 0, map.size());
    }
}