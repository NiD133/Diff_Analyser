package org.jfree.data.flow;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link DefaultFlowDataset} class.
 */
public class DefaultFlowDatasetTest {

    /**
     * Verifies that getDestinations() returns an empty list for a newly
     * instantiated, empty dataset.
     */
    @Test
    public void getDestinations_shouldReturnEmptyList_forNewDataset() {
        // Arrange: Create a new, empty dataset.
        DefaultFlowDataset<String> dataset = new DefaultFlowDataset<>();

        // Act: Request the list of destination nodes for the first stage.
        List<String> destinations = dataset.getDestinations(0);

        // Assert: The returned list should be empty.
        assertTrue("For a new dataset, the list of destinations should be empty.",
                   destinations.isEmpty());
    }
}