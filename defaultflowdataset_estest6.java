package org.jfree.data.flow;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertTrue;

/**
 * This test suite contains tests for the {@link DefaultFlowDataset} class.
 * This specific test focuses on the behavior of the getInFlows method.
 */
public class DefaultFlowDatasetTest {

    /**
     * Verifies that getInFlows() returns an empty list for a node when the only
     * existing flow is at a different stage.
     */
    @Test
    public void getInFlowsShouldReturnEmptyListWhenNodeStageDoesNotMatchFlowStage() {
        // Arrange
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        
        // Define a flow from node 100 to node 200 at stage 1.
        final int flowStage = 1;
        final Integer sourceNode = 100;
        final Integer destNode = 200;
        final double flowValue = 50.0;
        dataset.setFlow(flowStage, sourceNode, destNode, flowValue);

        // Define the target node we want to query. It has the same key as our
        // destination node (200) but is at a different stage (-5).
        final int queryStage = -5;
        NodeKey<Integer> targetNodeAtDifferentStage = new NodeKey<>(queryStage, destNode);

        // Act
        // Request all incoming flows for the target node.
        List<FlowKey<Integer>> inFlows = dataset.getInFlows(targetNodeAtDifferentStage);

        // Assert
        // The list should be empty because the only flow to node 200 exists at
        // stage 1, not at the queried stage of -5.
        assertTrue("Expected no incoming flows for a node at a different stage.", inFlows.isEmpty());
    }
}