package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * A test suite for the {@link DefaultFlowDataset} class.
 * This specific test focuses on the behavior of the getFlowProperty() method.
 */
public class DefaultFlowDatasetTest {

    /**
     * Verifies that getFlowProperty() returns null for a property key that has not been set,
     * even when other properties for the same flow exist.
     */
    @Test
    public void getFlowPropertyShouldReturnNullForNonExistentKey() {
        // Arrange: Create a dataset and define a flow with one existing property.
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        
        int stage = 0;
        Integer sourceNode = 0;
        Integer destNode = 0;
        FlowKey<Integer> flowKey = new FlowKey<>(stage, sourceNode, destNode);
        
        // Set one property on the flow.
        String existingPropertyKey = "color";
        String propertyValue = "blue";
        dataset.setFlowProperty(flowKey, existingPropertyKey, propertyValue);
        
        // Define the key for a property that does not exist.
        String nonExistentPropertyKey = "weight";

        // Act: Attempt to retrieve the property that was never set.
        Object result = dataset.getFlowProperty(flowKey, nonExistentPropertyKey);

        // Assert: The result should be null, as the property was not found.
        assertNull("Expected getFlowProperty to return null for a non-existent property key.", result);
    }
}