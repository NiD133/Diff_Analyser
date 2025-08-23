package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link NodeKey} class.
 */
public class NodeKeyTest {

    /**
     * Verifies that the toString() method returns a correctly formatted string
     * representation of the NodeKey, including its stage and node value.
     */
    @Test
    public void toString_shouldReturnFormattedStringRepresentation() {
        // Arrange: Create a NodeKey with a specific stage and node value.
        int stage = 2281;
        Integer nodeValue = 3;
        NodeKey<Integer> nodeKey = new NodeKey<>(stage, nodeValue);
        
        String expectedString = "[NodeKey: 2281, 3]";

        // Act: Call the toString() method.
        String actualString = nodeKey.toString();

        // Assert: Verify that the output matches the expected format.
        assertEquals(expectedString, actualString);
    }
}