package com.google.gson.internal;

import org.junit.Test;
import java.util.Comparator;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class LinkedTreeMap_ESTestTest7 extends LinkedTreeMap_ESTest_scaffolding {

    /**
     * Tests that `removeInternalByKey` successfully removes an entry if the map's
     * comparator considers the given key to be equal to an existing key, even if
     * they are different objects.
     */
    @Test
    public void removeInternalByKey_removesEntry_whenComparatorFindsMatch() {
        // Arrange

        // Create a custom comparator that considers any two objects to be equal.
        // This is the core of the test: forcing a "match" for different key objects.
        @SuppressWarnings("unchecked") // Necessary for mocking a generic type
        Comparator<Object> alwaysEqualComparator = (Comparator<Object>) mock(Comparator.class);
        doReturn(0).when(alwaysEqualComparator).compare(any(), any());

        // Create the map instance with our custom comparator.
        // Using simple <Object, String> types to keep the test focused and clear.
        LinkedTreeMap<Object, String> map = new LinkedTreeMap<>(alwaysEqualComparator, true);

        // Add an entry to the map.
        Object keyToInsert = new Object();
        map.put(keyToInsert, "value");
        assertEquals("Setup check: map should contain one entry", 1, map.size());

        // Create a completely different object to use for the removal operation.
        Object keyForRemoval = new Object();

        // Act
        // Attempt to remove the entry using the different key object.
        // Because the comparator returns 0, the map should find and remove the entry.
        map.removeInternalByKey(keyForRemoval);

        // Assert
        assertEquals("Map should be empty after removal", 0, map.size());
    }
}