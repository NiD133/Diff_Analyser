package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link DefaultFlowDataset} class.
 */
public class DefaultFlowDatasetTest {

    /**
     * Verifies that calling setFlow() for a stage equal to the current stage count
     * correctly adds a new stage, thereby increasing the total stage count.
     *
     * Per the documentation, a new DefaultFlowDataset starts with one stage (stage 0),
     * so getStageCount() initially returns 1. Setting a flow for stage 1 should
     * create that new stage and increase the count to 2.
     */
    @Test
    public void setFlow_forNewStage_increasesStageCount() {
        // Arrange: Create a new dataset.
        DefaultFlowDataset<String> dataset = new DefaultFlowDataset<>();
        // A new dataset is initialized with one stage (stage 0).
        assertEquals("A new dataset should have an initial stage count of 1.", 1, dataset.getStageCount());

        String sourceNode = "Source A";
        String destinationNode = "Destination B";
        double flowValue = 100.0;
        int newStageIndex = 1; // This index is equal to the initial stage count.

        // Act: Set a flow for a stage that does not yet exist.
        dataset.setFlow(newStageIndex, sourceNode, destinationNode, flowValue);

        // Assert: The dataset should have automatically added the new stage.
        assertEquals("The stage count should be 2 after adding a flow to a new stage.", 2, dataset.getStageCount());
    }
}