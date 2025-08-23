package org.jfree.data.flow;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link DefaultFlowDataset} class, focusing on the getOutFlows() method.
 */
public class DefaultFlowDatasetTest {

    /**
     * Verifies that getOutFlows() returns an empty list for a node that exists
     * but has no outgoing flows defined.
     */
    @Test
    public void getOutFlows_forNodeWithNoOutgoingFlows_shouldReturnEmptyList() {
        // Arrange
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        
        // Define a flow from node 100 to node 100 at stage 0.
        // This ensures the stage exists and contains nodes.
        final int stage = 0;
        final Integer sourceNode = 100;
        final Integer destNode = 100;
        dataset.setFlow(stage, sourceNode, destNode, 50.0);

        // The node we want to query has no outgoing flows.
        final Integer nodeToQuery = 0;
        NodeKey<Integer> keyForNodeToQuery = new NodeKey<>(stage, nodeToQuery);

        // Act
        List<FlowKey> outgoingFlows = dataset.getOutFlows(keyForNodeToQuery);

        // Assert
        // The dataset should have one stage because we added a flow.
        assertEquals("The stage count should be 1.", 1, dataset.getStageCount());
        
        // The list of outgoing flows for 'nodeToQuery' should be empty.
        assertTrue("Expected an empty list for a node with no outgoing flows.", outgoingFlows.isEmpty());
    }
}