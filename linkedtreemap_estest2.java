package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Map;
import java.util.Set;

// Note: The original class name and inheritance are preserved as per the problem context.
public class LinkedTreeMap_ESTestTest2 extends LinkedTreeMap_ESTest_scaffolding {

    /**
     * Verifies that the entry set of a newly created, empty LinkedTreeMap is empty.
     */
    @Test(timeout = 4000)
    public void entrySetOfEmptyMapShouldBeEmpty() {
        // Arrange
        LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();

        // Act
        Set<Map.Entry<String, Integer>> entrySet = map.entrySet();

        // Assert
        assertTrue("The entry set of an empty map should be empty.", entrySet.isEmpty());
        assertEquals("The size of the entry set from an empty map should be 0.", 0, entrySet.size());
    }
}