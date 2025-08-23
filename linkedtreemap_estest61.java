package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class contains an improved version of the original test case for LinkedTreeMap.
 * The original was an auto-generated test from a larger suite.
 */
public class LinkedTreeMapImprovedTest {

    @Test
    public void putIfAbsent_shouldAddEntriesForNewKeys_andIncreaseSize() {
        // Arrange
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        Integer commonValue = -2139;

        // Act
        // Add three distinct key-value pairs. Since the keys are new,
        // `putIfAbsent` should add them to the map.
        map.putIfAbsent(-2139, commonValue);
        map.putIfAbsent(-36, null); // Also test with a null value
        map.putIfAbsent(1, commonValue);

        // Assert
        // 1. Verify that the map size is correct after adding three new entries.
        assertEquals("Map size should be 3 after adding three distinct keys.", 3, map.size());

        // 2. For completeness, verify that the values were correctly inserted.
        assertTrue("Map should contain the first key.", map.containsKey(-2139));
        assertEquals("Value for the first key is incorrect.", commonValue, map.get(-2139));

        assertTrue("Map should contain the second key.", map.containsKey(-36));
        assertNull("Value for the second key should be null.", map.get(-36));

        assertTrue("Map should contain the third key.", map.containsKey(1));
        assertEquals("Value for the third key is incorrect.", commonValue, map.get(1));
    }
}