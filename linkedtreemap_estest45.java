package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;

public class LinkedTreeMap_ESTestTest45 extends LinkedTreeMap_ESTest_scaffolding {

    /**
     * Tests that a LinkedTreeMap.Node's equals() method returns false
     * when compared with an object of a completely different type (e.g., a LinkedTreeMap).
     */
    @Test
    public void nodeEquals_shouldReturnFalse_whenComparedWithDifferentType() {
        // Arrange
        // 1. Create a map and add an entry to obtain a Node instance.
        LinkedTreeMap<Integer, String> mapWithNode = new LinkedTreeMap<>();
        Integer key = 1;
        mapWithNode.put(key, "value");

        // 2. Retrieve the internal Node object we want to test.
        LinkedTreeMap.Node<Integer, String> node = mapWithNode.findByObject(key);
        assertNotNull("Setup failed: Node should not be null", node);

        // 3. Create an object of a different type to compare against.
        LinkedTreeMap<Integer, Object> anotherMap = new LinkedTreeMap<>();

        // Act
        // 4. Call the equals() method on the Node, passing the other map instance.
        boolean areEqual = node.equals(anotherMap);

        // Assert
        // 5. The result must be false, as the Node.equals() implementation checks
        //    if the other object is an instance of Map.Entry.
        assertFalse("A Node should not be equal to a LinkedTreeMap instance.", areEqual);
    }
}