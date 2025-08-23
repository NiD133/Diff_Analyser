package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link DefaultFlowDataset} class, focusing on node properties.
 */
public class DefaultFlowDatasetTest {

    /**
     * Verifies that a property set on a node can be correctly retrieved.
     */
    @Test
    public void setNodeProperty_shouldStoreAndRetrievePropertyValue() {
        // Arrange
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        NodeKey<Integer> nodeKey = new NodeKey<>(0, 100); // A node at stage 0 with ID 100
        String propertyKey = "status";
        String expectedPropertyValue = "active";

        // Act
        dataset.setNodeProperty(nodeKey, propertyKey, expectedPropertyValue);
        Object actualPropertyValue = dataset.getNodeProperty(nodeKey, propertyKey);

        // Assert
        assertSame("The retrieved property value should be the same instance that was set.",
                expectedPropertyValue, actualPropertyValue);
    }
}