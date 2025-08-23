package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the property management methods of the {@link DefaultFlowDataset} class.
 */
public class DefaultFlowDatasetTest {

    /**
     * Verifies that a property set for a specific flow can be correctly retrieved.
     * This test uses another FlowKey object as the property value to ensure
     * complex objects are handled correctly.
     */
    @Test
    public void setAndGetFlowProperty_shouldReturnCorrectObjectValue() {
        // Arrange: Set up the dataset, a key to identify a specific flow,
        // and a property (name and value) to associate with that flow.
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();

        // 1. Define the flow that will have a property attached to it.
        FlowKey<Integer> flowIdentifier = new FlowKey<>(0, 100, 200);

        // 2. Define the property's name and a complex object for its value.
        String propertyName = "relatedFlow";
        FlowKey<Integer> propertyValue = new FlowKey<>(1, 300, 400);

        // Act: Set the property for the specified flow, then retrieve it.
        dataset.setFlowProperty(flowIdentifier, propertyName, propertyValue);
        Object retrievedObject = dataset.getFlowProperty(flowIdentifier, propertyName);

        // Assert: The retrieved object should be identical to the one that was set.
        assertNotNull("The retrieved property should not be null.", retrievedObject);
        assertTrue("The retrieved property should be an instance of FlowKey.",
                retrievedObject instanceof FlowKey);

        // A full equality check is more robust than checking a single attribute.
        assertEquals("The retrieved property object should be equal to the original set value.",
                propertyValue, retrievedObject);
    }
}