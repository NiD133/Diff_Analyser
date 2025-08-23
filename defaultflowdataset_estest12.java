package org.jfree.data.flow;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * A test suite for the {@link DefaultFlowDataset} class.
 */
public class DefaultFlowDatasetTest {

    /**
     * Verifies that calling setFlow() for a new stage correctly adds the
     * destination node to that stage and updates the total stage count.
     */
    @Test
    public void setFlow_forNewStage_updatesDestinationsAndStageCount() {
        // Arrange
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();
        Integer sourceNode = 100;
        Integer destinationNode = 200;
        int stage = 0;
        double flowValue = 50.0;

        // Act
        dataset.setFlow(stage, sourceNode, destinationNode, flowValue);

        // Assert
        // Verify that the destination node was added correctly
        List<Integer> destinations = dataset.getDestinations(stage);
        assertEquals("There should be exactly one destination node for the stage.", 1, destinations.size());
        assertTrue("The destination node should be present in the list of destinations.",
                destinations.contains(destinationNode));

        // Verify that the stage count was updated
        assertEquals("The stage count should be 1 after adding a flow to stage 0.",
                1, dataset.getStageCount());
    }
}