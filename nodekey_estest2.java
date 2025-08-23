package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link NodeKey} class.
 */
public class NodeKeyTest {

    /**
     * Verifies that the getStage() method correctly returns the stage value
     * that was provided in the constructor.
     */
    @Test
    public void getStage_shouldReturnStageProvidedInConstructor() {
        // Arrange: Create a NodeKey with a specific stage and node identifier.
        int expectedStage = 0;
        Integer nodeId = 0;
        NodeKey<Integer> nodeKey = new NodeKey<>(expectedStage, nodeId);

        // Act: Call the method under test.
        int actualStage = nodeKey.getStage();

        // Assert: Check if the returned stage matches the expected value.
        assertEquals(expectedStage, actualStage);
    }
}