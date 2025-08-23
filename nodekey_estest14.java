package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * A collection of tests for the {@link NodeKey} class.
 */
public class NodeKeyTest {

    /**
     * Verifies that the getStage() method returns the stage value
     * that was provided to the constructor.
     */
    @Test
    public void getStage_shouldReturnStageValueFromConstructor() {
        // Arrange: Create a NodeKey with a specific stage and node identifier.
        int expectedStage = 2281;
        Integer nodeIdentifier = 3;
        NodeKey<Integer> nodeKey = new NodeKey<>(expectedStage, nodeIdentifier);

        // Act: Call the getStage() method.
        int actualStage = nodeKey.getStage();

        // Assert: Check that the returned stage matches the expected value.
        assertEquals(expectedStage, actualStage);
    }
}