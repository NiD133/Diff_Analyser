package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for the {@link DefaultFlowDataset} class.
 */
public class DefaultFlowDatasetTest {

    /**
     * Verifies that getFlow() returns null for a flow that does not exist
     * in an empty dataset.
     */
    @Test
    public void getFlow_forNonExistentFlow_shouldReturnNull() {
        // Arrange: Create an empty dataset and define a flow to query.
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        int stage = 0;
        Integer sourceNode = 100;
        Integer destinationNode = 200;

        // Act: Attempt to retrieve the flow from the empty dataset.
        Number flowValue = dataset.getFlow(stage, sourceNode, destinationNode);

        // Assert: The result should be null, as the flow was never added.
        assertNull("Expected null for a flow that does not exist.", flowValue);
    }
}