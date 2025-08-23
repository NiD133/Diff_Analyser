package org.jfree.data.flow;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link DefaultFlowDataset} class, focusing on how nodes
 * are handled across different stages.
 */
public class DefaultFlowDataset_ESTestTest9 {

    /**
     * Verifies that a destination node from a flow in one stage is correctly
     * identified as a source node for the subsequent stage.
     */
    @Test
    public void getSourcesForNextStageShouldContainDestinationOfPreviousStage() {
        // Arrange: Create a dataset and add a flow in stage 0.
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        Integer sourceNodeStage0 = 10;
        Integer destinationNodeStage0 = 20;
        double flowValue = 100.0;

        // This flow from node 10 to 20 in stage 0 implies that node 20
        // is now a potential source for flows in stage 1.
        dataset.setFlow(0, sourceNodeStage0, destinationNodeStage0, flowValue);

        // Act: Get the list of source nodes for the next stage (stage 1).
        List<Integer> sourcesForStage1 = dataset.getSources(1);

        // Assert: Check that the destination from stage 0 is now a source for stage 1.
        // A dataset with flows only in stage 0 has a total of one stage.
        assertEquals("Adding a flow to stage 0 should result in one stage.", 1, dataset.getStageCount());

        // The destination node from the previous stage should be the only source for the next stage.
        assertEquals("There should be exactly one source node for stage 1.", 1, sourcesForStage1.size());
        assertTrue("The destination from stage 0 should be a source for stage 1.",
                sourcesForStage1.contains(destinationNodeStage0));
    }
}