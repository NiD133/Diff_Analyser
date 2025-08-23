package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link DefaultFlowDataset} class.
 */
public class DefaultFlowDatasetTest {

    /**
     * Verifies that a newly created, empty dataset has a default stage count of 1.
     * The Javadoc for getStageCount() specifies this behavior.
     */
    @Test
    public void getStageCount_shouldReturnOne_forNewEmptyDataset() {
        // Arrange: Create a new, empty dataset.
        DefaultFlowDataset<String> dataset = new DefaultFlowDataset<>();

        // Act: Retrieve the stage count from the new dataset.
        int stageCount = dataset.getStageCount();

        // Assert: The stage count should be 1 by default.
        assertEquals("A new, empty dataset must have a default stage count of 1.", 1, stageCount);
    }
}