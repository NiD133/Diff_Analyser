package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

/**
 * Contains tests for the {@link DefaultFlowDataset} class, focusing on flow retrieval logic.
 */
public class DefaultFlowDataset_ESTestTest5 {

    /**
     * Verifies that getInFlows() returns an empty list when queried for a node
     * in a stage that does not exist in the dataset.
     */
    @Test
    public void getInFlowsForNodeInNonExistentStageShouldReturnEmptyList() {
        // Arrange: Create a dataset with a single flow in stage 0.
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        Integer node = 0;
        int stage = 0;
        double flowValue = 10.0;
        dataset.setFlow(stage, node, node, flowValue);

        // Define a NodeKey for the same node but in a stage that is known not to exist.
        int nonExistentStage = 99;
        NodeKey<Integer> nodeInNonExistentStage = new NodeKey<>(nonExistentStage, node);

        // Act: Request the incoming flows for the node in the non-existent stage.
        List<FlowKey<Integer>> inFlows = dataset.getInFlows(nodeInNonExistentStage);

        // Assert: The result should be an empty list, and the dataset's state should be unchanged.
        assertTrue("Expected an empty list of flows for a node in a non-existent stage.", inFlows.isEmpty());
        assertEquals("The stage count should not change after the query.", 1, dataset.getStageCount());
    }
}