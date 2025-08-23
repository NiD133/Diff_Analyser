package org.jfree.data.flow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the getStageCount() method in the {@link DefaultFlowDataset} class.
 */
@DisplayName("DefaultFlowDataset.getStageCount")
class DefaultFlowDatasetTest {

    private DefaultFlowDataset<String> dataset;

    @BeforeEach
    void setUp() {
        dataset = new DefaultFlowDataset<>();
    }

    @Test
    @DisplayName("should return 1 for a new, empty dataset")
    void getStageCount_onNewDataset_returnsOne() {
        // Arrange: A new dataset is created in setUp().
        int expected = 1;

        // Act
        int actual = dataset.getStageCount();

        // Assert
        assertEquals(expected, actual, "A new dataset should always have one initial stage.");
    }

    @Test
    @DisplayName("should remain 1 after adding a flow to the first stage")
    void getStageCount_afterAddingFlowToFirstStage_remainsUnchanged() {
        // Arrange
        dataset.setFlow(0, "A", "Z", 11.1);
        int expected = 1;

        // Act
        int actual = dataset.getStageCount();

        // Assert
        assertEquals(expected, actual, "Adding a flow to an existing stage should not increase the stage count.");
    }

    @Test
    @DisplayName("should increase to 2 after adding a flow to a new stage")
    void getStageCount_afterAddingFlowToNewStage_isIncremented() {
        // Arrange
        dataset.setFlow(1, "Z", "P", 5.0);
        int expected = 2;

        // Act
        int actual = dataset.getStageCount();

        // Assert
        assertEquals(expected, actual, "Adding a flow to a new stage index should increase the stage count.");
    }
}