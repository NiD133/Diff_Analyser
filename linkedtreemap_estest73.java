package com.google.gson.internal;

import org.junit.Test;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

// The original test class structure is preserved.
public class LinkedTreeMap_ESTestTest73 extends LinkedTreeMap_ESTest_scaffolding {

    /**
     * Verifies that calling `contains()` on a key set with an object of an
     * incompatible type returns `false` instead of throwing a ClassCastException.
     * This behavior is part of the contract for java.util.Set.
     */
    @Test
    public void keySetContainsShouldReturnFalseForIncompatibleType() {
        // Arrange
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        Set<Integer> keySet = map.keySet();

        // Act
        // Check for containment of the map itself. Since the key set expects Integers,
        // the map is an object of an incompatible type. The `contains` method
        // should handle this gracefully.
        boolean contains = keySet.contains(map);

        // Assert
        assertFalse("keySet.contains() should return false for an incompatible type.", contains);
        assertEquals("The key set of an empty map should have a size of 0.", 0, keySet.size());
    }
}