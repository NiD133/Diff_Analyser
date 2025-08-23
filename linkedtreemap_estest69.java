package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the internal Node class of LinkedTreeMap.
 * This focuses on behavior not easily accessible through the public Map API.
 */
public class LinkedTreeMapNodeTest {

    /**
     * Verifies that a new Node created via `find(key, create=true)`
     * has a `toString()` representation of "key=null".
     */
    @Test
    public void toStringOnNewNodeReturnsKeyEqualsNull() {
        // Arrange
        LinkedTreeMap<Integer, String> map = new LinkedTreeMap<>();
        Integer key = -1960;
        String expectedNodeString = "-1960=null";

        // Act
        // The find(key, true) method creates and returns a new node if the key is not present.
        // The new node's value should be null by default.
        LinkedTreeMap.Node<Integer, String> newNode = map.find(key, true);
        String actualNodeString = newNode.toString();

        // Assert
        // 1. Check that a new node was indeed added to the map.
        assertEquals("A new node should have been added to the map.", 1, map.size());

        // 2. Check that the new node's string representation is correct.
        assertEquals("The new node's toString() should format as 'key=null'.",
                expectedNodeString, actualNodeString);
    }
}