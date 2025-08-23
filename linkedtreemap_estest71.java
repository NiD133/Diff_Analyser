package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.Set;

// The test class name and inheritance are kept to match the original structure.
public class LinkedTreeMap_ESTestTest71 extends LinkedTreeMap_ESTest_scaffolding {

    /**
     * Tests that the size of the key set for a newly created, empty LinkedTreeMap is 0.
     */
    @Test
    public void keySet_onEmptyMap_shouldHaveSizeZero() {
        // Arrange: Create a new, empty LinkedTreeMap.
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();

        // Act: Get the key set from the map.
        Set<Integer> keySet = map.keySet();

        // Assert: Verify that the size of the key set is 0.
        assertEquals(0, keySet.size());
    }
}