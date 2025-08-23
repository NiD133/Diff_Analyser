package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;

import java.time.chrono.HijrahEra;

/**
 * Tests for the {@link DefaultFlowDataset} class, focusing on setting flow properties.
 */
public class DefaultFlowDataset_ESTestTest35 {

    /**
     * Verifies that multiple properties, including one with an empty key,
     * can be set and retrieved for the same flow.
     */
    @Test(timeout = 4000)
    public void setFlowProperty_shouldStoreAndRetrieveMultiplePropertiesForSameFlow() {
        // Arrange: Create a dataset, a flow key, and the properties to be set.
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        
        int stage = 0;
        Integer sourceNode = 0;
        Integer destNode = 0;
        FlowKey<Integer> flowKey = new FlowKey<>(stage, sourceNode, destNode);

        String descriptionKey = "description";
        String descriptionValue = "Flow from node 0 to 0";
        
        String typeKey = ""; // Test with an empty string as a property key
        HijrahEra typeValue = HijrahEra.AH;

        // Act: Set two different properties on the same flow.
        dataset.setFlowProperty(flowKey, descriptionKey, descriptionValue);
        dataset.setFlowProperty(flowKey, typeKey, typeValue);

        // Assert: Verify that both properties were stored correctly and can be retrieved.
        assertEquals("The 'description' property should be correctly retrieved.",
                descriptionValue, dataset.getFlowProperty(flowKey, descriptionKey));
        
        assertEquals("The property with an empty key should be correctly retrieved.",
                typeValue, dataset.getFlowProperty(flowKey, typeKey));
    }
}