package org.jfree.data.flow;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link DefaultFlowDataset} class.
 */
public class DefaultFlowDatasetTest {

    /**
     * Verifies that getSources() for stage 0 on a newly created dataset
     * returns an empty list, as no data has been added yet.
     */
    @Test
    public void getSourcesForStageZeroOnNewDatasetShouldReturnEmptyList() {
        // Arrange: Create a new, empty dataset.
        DefaultFlowDataset<Integer> dataset = new DefaultFlowDataset<>();

        // Act: Retrieve the list of source nodes for the first stage.
        List<Integer> sources = dataset.getSources(0);

        // Assert: The returned list should be non-null and empty.
        assertNotNull("The sources list should never be null.", sources);
        assertTrue("A new dataset should have no sources for stage 0.", sources.isEmpty());
    }
}