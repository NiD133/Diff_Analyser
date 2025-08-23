package com.google.gson.internal;

import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link LinkedTreeMap.EntrySet} inner class.
 */
public class LinkedTreeMapEntrySetTest {

    /**
     * Verifies that calling remove() on an EntrySet with an object that is not a
     * Map.Entry returns false and does not modify the underlying map. The Set contract
     * for remove(Object) requires this behavior.
     */
    @Test
    public void remove_objectThatIsNotAMapEntry_shouldReturnFalseAndNotModifySet() {
        // Arrange
        LinkedTreeMap<Integer, String> map = new LinkedTreeMap<>();
        Set<Map.Entry<Integer, String>> entrySet = map.entrySet();

        // An object that is not of type Map.Entry.
        Object objectToRemove = -54;

        // Act
        boolean wasRemoved = entrySet.remove(objectToRemove);

        // Assert
        assertFalse("remove() should return false when called with an object that is not a Map.Entry.", wasRemoved);
        assertEquals("The entry set's size should remain unchanged.", 0, entrySet.size());
    }
}