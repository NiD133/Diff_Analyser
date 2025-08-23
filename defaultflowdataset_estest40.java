package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link DefaultFlowDataset} class.
 */
public class DefaultFlowDatasetTest {

    /**
     * Verifies that getNodeProperty() returns null when queried for a property
     * on a node that does not exist in the dataset.
     */
    @Test
    public void getNodePropertyShouldReturnNullForNonexistentNode() {
        // Arrange: Create an empty dataset and a key for a node that has not been added.
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        NodeKey<Integer> nonexistentNodeKey = new NodeKey<>(0, 123);
        String propertyKey = "selected";

        // Act: Attempt to retrieve a property for the non-existent node.
        Object propertyValue = dataset.getNodeProperty(nonexistentNodeKey, propertyKey);

        // Assert: The returned property value should be null.
        assertNull("Getting a property for a non-existent node should return null.", propertyValue);
    }
}