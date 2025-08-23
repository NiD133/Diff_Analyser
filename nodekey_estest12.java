package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * A test suite for the {@link NodeKey} class.
 */
public class NodeKeyTest {

    /**
     * Verifies that the constructor correctly initializes the stage and node,
     * and that the corresponding getter methods return these values.
     */
    @Test
    public void constructor_shouldSetStageAndNodeCorrectly() {
        // Arrange: Define the input values for the NodeKey.
        // Using distinct values for stage and node helps ensure they are not mixed up.
        int expectedStage = 1;
        String expectedNode = "NodeA";

        // Act: Create an instance of the NodeKey.
        NodeKey<String> nodeKey = new NodeKey<>(expectedStage, expectedNode);

        // Assert: Verify that the getters return the values set by the constructor.
        assertEquals("The stage should match the value provided in the constructor.",
                expectedStage, nodeKey.getStage());
        assertEquals("The node should match the value provided in the constructor.",
                expectedNode, nodeKey.getNode());
    }
}