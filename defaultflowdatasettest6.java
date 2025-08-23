package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

/**
 * A test suite for the {@link DefaultFlowDataset} class, focusing on the 
 * {@link DefaultFlowDataset#getInFlows(NodeKey)} method.
 */
public class DefaultFlowDatasetTest {

    /**
     * This test verifies that getInFlows() returns an empty list when a flow
     * exists for the target node's key but at a non-matching stage.
     *
     * The getInFlows() method is expected to return a flow only when the queried
     * node's stage is exactly one greater than the flow's stage. This test
     * specifically checks the scenario where this condition is not met.
     */
    @Test
    public void getInFlowsShouldReturnEmptyListWhenNodeStageDoesNotMatchFlowStage() {
        // Arrange: Create a dataset and add a single flow from a source node to
        // a destination node at a specific stage. [1, 2, 3]
        DefaultFlowDataset<Integer> flowDataset = new DefaultFlowDataset<>();
        Integer nodeIdentifier = 100;
        int flowStage = 1;
        double flowValue = -478.3883577094;
        flowDataset.setFlow(flowStage, nodeIdentifier, nodeIdentifier, flowValue);

        // An incoming flow's destination node is at stage `flowStage + 1`.
        // We will query for a node at a stage that is deliberately incorrect.
        int mismatchedQueryStage = -352;
        NodeKey<Integer> queryNodeKey = new NodeKey<>(mismatchedQueryStage, nodeIdentifier);

        // Act: Call the method under test to get all incoming flows for the
        // node key with the mismatched stage.
        List<FlowKey<Integer>> incomingFlows = flowDataset.getInFlows(queryNodeKey);

        // Assert: Verify that the returned list is empty, as the queried node's
        // stage does not align with the expected stage for an incoming flow.
        assertTrue("Expected no incoming flows for a node at a mismatched stage.", incomingFlows.isEmpty());
    }
}