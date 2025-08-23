package com.google.gson.internal;

import org.junit.Test;
import java.util.Set;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * This class contains tests for the LinkedTreeMap class.
 * The original test class name 'LinkedTreeMap_ESTestTest47' suggests it was auto-generated.
 */
public class LinkedTreeMap_ESTestTest47 extends LinkedTreeMap_ESTest_scaffolding {

    /**
     * Verifies that calling keySet() on an empty map returns a non-null, empty set.
     */
    @Test
    public void keySet_onEmptyMap_returnsEmptySet() {
        // Arrange: Create an empty LinkedTreeMap.
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();

        // Act: Retrieve the key set from the map.
        Set<Integer> keySet = map.keySet();

        // Assert: Verify the key set is not null and is empty.
        assertNotNull("The key set should never be null.", keySet);
        assertTrue("The key set of a new map should be empty.", keySet.isEmpty());
    }
}