package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import org.jfree.data.flow.DefaultFlowDataset;
import org.jfree.data.flow.FlowKey;
import org.jfree.data.flow.NodeKey;

/**
 * Contains tests for the {@link DefaultFlowDataset} class, focusing on flow retrieval logic.
 */
public class DefaultFlowDatasetTest {

    /**
     * Tests that getOutFlows() returns an empty list for a node at a stage
     * that has no outgoing flows, even when a node with the same key exists
     * with flows at a different stage. This confirms that the method correctly
     * distinguishes nodes based on both their key and their stage.
     */
    @Test
    public void getOutFlows_whenNodeStageHasNoFlows_shouldReturnEmptyList() {
        // Arrange: Set up the dataset and the test conditions.
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        final Integer NODE_KEY = 100;
        final int flowStage = 1;
        final int queriedStage = 0; // A different stage from where the flow is defined.
        final double flowValue = 500.0;

        // Define a flow originating from NODE_KEY at a specific stage.
        dataset.setFlow(flowStage, NODE_KEY, NODE_KEY, flowValue);

        // Create a key for the same node but at a different stage, where no flows are defined.
        NodeKey<Integer> nodeAtQueriedStage = new NodeKey<>(queriedStage, NODE_KEY);

        // Act: Call the method under test.
        List<FlowKey> outgoingFlows = dataset.getOutFlows(nodeAtQueriedStage);

        // Assert: Verify the outcome is as expected.
        // The list must be empty because no flows originate from the node at the queried stage.
        assertTrue("Expected no outgoing flows for the node at the queried stage.", outgoingFlows.isEmpty());
    }
}