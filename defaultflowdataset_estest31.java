package org.jfree.data.flow;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link DefaultFlowDataset} class.
 */
public class DefaultFlowDatasetTest {

    /**
     * Verifies that getOutFlows() returns an empty list for a node
     * when the dataset is newly created and has no data.
     */
    @Test
    public void getOutFlows_forNodeInEmptyDataset_shouldReturnEmptyList() {
        // Arrange: Create a new, empty dataset and a key for a node that doesn't exist yet.
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        NodeKey<Integer> nodeKey = new NodeKey<>(1, 100); // Stage 1, Node ID 100

        // Act: Request the list of outgoing flows from the specified node.
        List<FlowKey> outFlows = dataset.getOutFlows(nodeKey);

        // Assert: The returned list should be empty, as no flows have been added.
        assertTrue("Expected an empty list of outgoing flows for a node in an empty dataset.",
                outFlows.isEmpty());
    }
}