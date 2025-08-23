package org.jfree.data.flow;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link DefaultFlowDataset#getInFlows(NodeKey)} method.
 */
public class DefaultFlowDataset_ESTestTest33 extends DefaultFlowDataset_ESTest_scaffolding {

    /**
     * Verifies that getInFlows() returns the correct flow key for a given
     * destination node.
     */
    @Test
    public void getInFlows_shouldReturnFlowsPointingToSpecifiedNode() {
        // Arrange: Create a dataset and add a single flow.
        // The flow is from a source node (ID 100) at stage 0
        // to a destination node (ID 200) at stage 1.
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        int stage = 0;
        Integer sourceNodeId = 100;
        Integer destNodeId = 200;
        dataset.setFlow(stage, sourceNodeId, destNodeId, 50.0);

        // The node we are interested in is the destination node, which is at stage 1.
        NodeKey<Integer> destinationNode = new NodeKey<>(stage + 1, destNodeId);

        // Act: Retrieve all incoming flows for the destination node.
        List<FlowKey<Integer>> incomingFlows = dataset.getInFlows(destinationNode);

        // Assert: Verify that the correct flow is returned.
        // We expect to find exactly one incoming flow.
        assertEquals("Should find one incoming flow for the destination node.", 1, incomingFlows.size());

        // We also expect the details of that flow to match what we added.
        FlowKey<Integer> expectedFlowKey = new FlowKey<>(stage, sourceNodeId, destNodeId);
        assertEquals("The returned flow key should match the one that was added.",
                expectedFlowKey, incomingFlows.get(0));

        // As a side-effect of adding a flow at stage 0, the total stage count should be 1.
        assertEquals("Stage count should be updated correctly.", 1, dataset.getStageCount());
    }
}