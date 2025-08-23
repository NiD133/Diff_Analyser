package com.google.gson.internal;

import org.junit.Test;
import java.util.Comparator;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// The test class name is kept for context, but in a real scenario,
// it would be renamed to something like LinkedTreeMapTest.
public class LinkedTreeMap_ESTestTest9 extends LinkedTreeMap_ESTest_scaffolding {

    /**
     * Tests that when a custom comparator considers two different key objects to be equal,
     * a `put` operation for the second key overwrites the value of the first,
     * rather than creating a new entry.
     */
    @Test
    public void put_withKeysConsideredEqualByComparator_replacesExistingValue() {
        // Arrange: Create a comparator that treats all keys as equal.
        @SuppressWarnings("unchecked")
        Comparator<Object> alwaysEqualComparator = mock(Comparator.class);
        when(alwaysEqualComparator.compare(any(), any())).thenReturn(0);

        // Arrange: Initialize the map with the custom comparator.
        LinkedTreeMap<String, String> map = new LinkedTreeMap<>(alwaysEqualComparator, true);

        String key1 = "first_key";
        String value1 = "first_value";
        String key2 = "second_key"; // A different object, but "equal" per the comparator.
        String value2 = "second_value";

        // Act: Insert the first entry, then insert a second entry whose key is
        // considered a duplicate by the comparator.
        map.put(key1, value1);
        map.put(key2, value2);

        // Assert: The map size should be 1 because the second put replaced the first.
        assertEquals(1, map.size());

        // Assert: The value in the map should be the one from the second put operation.
        // We can use either key to retrieve it, as the comparator drives lookups.
        assertEquals(value2, map.get(key1));
        assertEquals(value2, map.get(key2));
    }
}