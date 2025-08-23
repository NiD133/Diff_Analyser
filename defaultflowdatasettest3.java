package org.jfree.data.flow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Tests for the equals() method in the {@link DefaultFlowDataset} class.
 */
@DisplayName("DefaultFlowDataset equals()")
class DefaultFlowDatasetTest {

    /**
     * Verifies that the equals() method correctly compares datasets based on their
     * flow data. The test follows a sequence of state changes to ensure that
     * equality is maintained or broken as expected.
     */
    @Test
    @DisplayName("Should correctly compare datasets based on their flows")
    void equals_shouldCorrectlyCompareDatasetsBasedOnFlows() {
        // Arrange: Create two identical, empty datasets.
        DefaultFlowDataset<String> dataset1 = new DefaultFlowDataset<>();
        DefaultFlowDataset<String> dataset2 = new DefaultFlowDataset<>();

        // Assert: Initially, the two empty datasets should be equal.
        assertEquals(dataset1, dataset2, "Two new, empty datasets should be equal.");

        // Act: Add a flow to the first dataset, making it different from the second.
        dataset1.setFlow(0, "A", "Z", 1.0);

        // Assert: The datasets should no longer be equal.
        assertNotEquals(dataset1, dataset2, "A dataset should not be equal to another after a flow is added to one.");

        // Act: Add the same flow to the second dataset, making them identical again.
        d2.setFlow(0, "A", "Z", 1.0);

        // Assert: The datasets should now be equal again.
        assertEquals(dataset1, dataset2, "Two datasets with identical flows should be equal.");
    }
}