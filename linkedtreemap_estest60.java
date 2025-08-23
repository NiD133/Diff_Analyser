package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class contains the refactored test case.
 * The original test was part of a larger, generated test suite (LinkedTreeMap_ESTestTest60).
 * This version is functionally identical but has been improved for human readability.
 */
public class LinkedTreeMap_ESTestTest60 extends LinkedTreeMap_ESTest_scaffolding {

    /**
     * Tests that a node created by {@link LinkedTreeMap#find(Object, boolean)}
     * with create=true can be successfully removed by {@link LinkedTreeMap#removeInternal(Node, boolean)}.
     */
    @Test
    public void removeInternal_removesNodeCreatedByFindWithCreateFlag() {
        // Arrange
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        map.put(-2139, -2139);
        map.put(-36, null);
        assertEquals("Map should contain 2 elements initially", 2, map.size());

        Integer keyToCreateAndRemove = -1109;

        // Act
        // 1. Call find() with create=true, which adds a new node for a non-existent key.
        LinkedTreeMap.Node<Integer, Integer> nodeCreatedByFind = map.find(keyToCreateAndRemove, true);
        assertEquals("Size should be 3 after find() creates a new node", 3, map.size());

        // 2. Remove the newly created node using the internal remove method.
        map.removeInternal(nodeCreatedByFind, true);

        // Assert
        assertEquals("Size should return to 2 after removing the node", 2, map.size());
    }
}