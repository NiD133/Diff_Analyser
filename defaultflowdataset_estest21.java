package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link DefaultFlowDataset} class, focusing on its initial state.
 */
public class DefaultFlowDatasetTest {

    /**
     * Verifies that a newly instantiated DefaultFlowDataset has a stage count of 1.
     * The class contract specifies that a dataset, even when empty, must have at
     * least one stage.
     */
    @Test
    public void newDatasetShouldHaveOneStage() {
        // Arrange: Create a new, empty dataset.
        DefaultFlowDataset<String> dataset = new DefaultFlowDataset<>();

        // Act: Retrieve the stage count from the new dataset.
        int stageCount = dataset.getStageCount();

        // Assert: The stage count should be 1 by default.
        assertEquals("A new, empty dataset must have one stage by default.", 1, stageCount);
    }
}