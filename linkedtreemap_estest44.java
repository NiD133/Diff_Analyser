package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * This test focuses on the behavior of the {@link LinkedTreeMap.Node#equals(Object)} method.
 */
public class LinkedTreeMapNodeTest {

    /**
     * Tests that a regular data node is not considered equal to a special header node.
     * The Node's equals method should correctly differentiate between these two distinct types of nodes.
     */
    @Test
    public void nodeEquals_returnsFalse_whenComparingDataNodeWithHeaderNode() {
        // Arrange
        // 1. Create a map and add an element. The find(key, true) method creates and returns
        //    a regular data node within the map's structure.
        LinkedTreeMap<Integer, String> map = new LinkedTreeMap<>();
        LinkedTreeMap.Node<Integer, String> dataNode = map.find(1, true); // key=1, value=null

        // 2. The Node(boolean) constructor is used internally to create the special "header" node,
        //    which acts as a sentinel for the circular doubly-linked list of map entries.
        LinkedTreeMap.Node<Object, Object> headerNode = new LinkedTreeMap.Node<>(true);

        // Act
        // 3. Compare the header node with the data node.
        boolean areEqual = headerNode.equals(dataNode);

        // Assert
        // 4. A header node should never be equal to a data node.
        assertFalse("A header node should not be equal to a data node", areEqual);
        
        // 5. Also, verify that the data node was indeed added to the map.
        assertEquals("Map size should be 1 after adding the node", 1, map.size());
    }
}