package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test focuses on the internal behavior of the LinkedTreeMap class.
 */
public class LinkedTreeMapInternalTest {

    /**
     * Tests that calling the internal method `removeInternal` with a node
     * that is not part of the map's structure incorrectly decrements the map's size.
     *
     * <p>This scenario is not a standard use case, as `removeInternal` is a private
     * helper method. The test reveals a lack of validation within this method,
     * which can lead to a corrupted map state (e.g., a negative size) if misused.
     */
    @Test
    public void removeInternal_withDetachedNode_corruptsMapSize() {
        // Arrange: Create an empty map and a standalone node that is not part of the map.
        LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();
        assertEquals("An empty map should have a size of 0", 0, map.size());

        // This creates a new node that is not linked to the map's root or any other element.
        // It is effectively a "detached" node.
        LinkedTreeMap.Node<String, Integer> detachedNode = new LinkedTreeMap.Node<>(true);

        // Act: Call the internal remove method with the detached node.
        // This is an improper use of the internal API, as the method assumes
        // the node is part of the map.
        map.removeInternal(detachedNode, true);

        // Assert: The size is incorrectly decremented without validation, resulting in a negative value.
        assertEquals("Size should become -1 after removing a detached node", -1, map.size());
    }
}