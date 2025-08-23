package com.google.gson.internal;

import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class LinkedTreeMapEntrySetTest {

    @Test
    public void entrySetContains_shouldReturnFalse_forIncompatibleObjectType() {
        // Arrange
        LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();
        Set<Map.Entry<String, Integer>> entrySet = map.entrySet();

        // Act
        // The Set.contains() contract specifies that if the object's type is
        // incompatible with the set's element type, the method should return false
        // rather than throwing a ClassCastException. We test this by passing the
        // map itself to its entrySet's contains() method.
        boolean result = entrySet.contains(map);

        // Assert
        assertFalse("contains() should return false when called with an incompatible object type.", result);
        assertEquals("The size of the entry set of an empty map should be 0.", 0, entrySet.size());
    }
}