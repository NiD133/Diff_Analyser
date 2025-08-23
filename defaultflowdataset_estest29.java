package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

/**
 * Unit tests for the {@link DefaultFlowDataset} class.
 */
public class DefaultFlowDatasetTest {

    /**
     * Verifies that getOutFlows() correctly returns the list of flows
     * originating from a specified source node.
     */
    @Test
    public void getOutFlows_shouldReturnFlowsOriginatingFromNode() {
        // Arrange: Create a dataset and add a single, well-defined flow.
        DefaultFlowDataset<String> dataset = new DefaultFlowDataset<>();
        final int stage = 0;
        final String sourceNode = "Source A";
        final String destinationNode = "Destination B";
        final double flowValue = 100.0;

        dataset.setFlow(stage, sourceNode, destinationNode, flowValue);

        // Act: Retrieve the outgoing flows for the source node.
        NodeKey<String> sourceNodeKey = new NodeKey<>(stage, sourceNode);
        List<FlowKey> outgoingFlows = dataset.getOutFlows(sourceNodeKey);

        // Assert: The list should contain exactly one flow, matching the one we added.
        assertNotNull("The list of outgoing flows should not be null.", outgoingFlows);
        assertEquals("There should be exactly one outgoing flow from the source node.", 1, outgoingFlows.size());

        FlowKey<String> expectedFlowKey = new FlowKey<>(stage, sourceNode, destinationNode);
        assertEquals("The flow key in the list should match the one that was added.", expectedFlowKey, outgoingFlows.get(0));
        
        // Also, verify that adding a flow to stage 0 correctly sets the stage count.
        assertEquals("The stage count should be 1.", 1, dataset.getStageCount());
    }
}