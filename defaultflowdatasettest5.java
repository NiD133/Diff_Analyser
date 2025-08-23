package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

/**
 * Contains tests for the {@link DefaultFlowDataset} class, focusing on the
 * understandability and correctness of its behavior.
 */
public class DefaultFlowDatasetUnderstandabilityTest {

    /**
     * Verifies that getInFlows() returns an empty list when queried for a node
     * at a stage that has no defined incoming flows. This holds true even if a
     * flow involving that same node exists at a different stage.
     */
    @Test
    public void whenGetInFlowsForNodeInDifferentStage_thenReturnsEmptyList() {
        // Arrange: Create a dataset with a single flow at stage 0.
        DefaultFlowDataset<Integer> flowDataset = new DefaultFlowDataset<>();
        final int stageWithFlow = 0;
        final int nodeId = 0;
        final double flowValue = 100.0;

        // Define a flow from 'nodeId' to itself at 'stageWithFlow'.
        flowDataset.setFlow(stageWithFlow, nodeId, nodeId, flowValue);

        // Act: Query for incoming flows to the same node, but at a different stage
        // for which no flows have been defined.
        final int queriedStage = 1;
        NodeKey<Integer> nodeKeyForDifferentStage = new NodeKey<>(queriedStage, nodeId);
        List<FlowKey<Integer>> incomingFlows = flowDataset.getInFlows(nodeKeyForDifferentStage);

        // Assert: The list of incoming flows should be empty, and the dataset's
        // stage count should correctly reflect the single stage that was added.
        assertTrue("Expected no incoming flows for a different stage.", incomingFlows.isEmpty());
        assertEquals("The dataset should only contain one stage.", 1, flowDataset.getStageCount());
    }
}