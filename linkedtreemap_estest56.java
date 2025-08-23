package com.google.gson.internal;

import org.junit.Test;
import java.util.AbstractMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LinkedTreeMap_ESTestTest56 extends LinkedTreeMap_ESTest_scaffolding {

    /**
     * Tests that findByEntry() returns null when an entry with a matching key
     * exists, but its value is different from the value of the entry being searched for.
     * This also verifies that the find operation does not modify the map.
     */
    @Test
    public void findByEntry_whenKeyMatchesButValueDoesNot_returnsNullAndMapIsUnchanged() {
        // Arrange
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        Integer key = -1960;
        Integer valueInMap = -1960;
        map.put(key, valueInMap);

        // Create a search entry with the same key but a different value.
        // The original test created a new Node whose value defaulted to null, so we
        // replicate that behavior here for an equivalent test.
        Map.Entry<Integer, Integer> entryToFind = new AbstractMap.SimpleEntry<>(key, null);

        // Act
        LinkedTreeMap.Node<Integer, Integer> result = map.findByEntry(entryToFind);

        // Assert
        assertNull("findByEntry should return null if the entry's value does not match.", result);
        assertEquals("The map's size should not be affected by the findByEntry call.", 1, map.size());
    }
}