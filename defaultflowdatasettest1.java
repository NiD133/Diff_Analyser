package org.jfree.data.flow;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link DefaultFlowDataset} class, focusing on flow value management.
 */
public class DefaultFlowDatasetTest {

    private static final double DELTA = 1e-9;

    /**
     * Verifies that getFlow() correctly retrieves a value that was previously 
     * set using setFlow().
     */
    @Test
    public void getFlow_shouldReturnPreviouslySetValue() {
        // Arrange: Create a dataset and set a specific flow value.
        DefaultFlowDataset<String> dataset = new DefaultFlowDataset<>();
        int stage = 0;
        String sourceNode = "A";
        String destinationNode = "Z";
        double expectedFlow = 1.5;

        dataset.setFlow(stage, sourceNode, destinationNode, expectedFlow);

        // Act: Retrieve the flow value from the dataset.
        Number actualFlow = dataset.getFlow(stage, sourceNode, destinationNode);

        // Assert: Verify that the retrieved value matches the set value.
        assertNotNull(actualFlow, "getFlow() should not return null for a flow that has been set.");
        assertEquals(expectedFlow, actualFlow.doubleValue(), DELTA,
                "The retrieved flow value should match the original value that was set.");
    }
}