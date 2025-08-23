package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link NodeKey} class.
 */
public class NodeKeyTest {

    /**
     * Verifies that the getStage() method returns the value that was provided
     * to the constructor.
     */
    @Test
    public void getStageShouldReturnStageSetInConstructor() {
        // Arrange: Define the input values for the NodeKey.
        int expectedStage = 9;
        Integer nodeIdentifier = 9; // This value is required by the constructor but not under test here.

        // Act: Create an instance of the NodeKey.
        NodeKey<Integer> nodeKey = new NodeKey<>(expectedStage, nodeIdentifier);

        // Assert: Check if the getStage() method returns the expected value.
        assertEquals(expectedStage, nodeKey.getStage());
    }
}