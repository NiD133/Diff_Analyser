package org.jfree.data.general;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Tests for the equals() method in the DefaultKeyedValueDataset class.
 */
@DisplayName("DefaultKeyedValueDataset.equals()")
class DefaultKeyedValueDatasetTest {

    @Test
    @DisplayName("should return true for datasets with the same key and value")
    void equals_whenObjectsHaveSameState_shouldReturnTrue() {
        // Arrange
        var dataset1 = new DefaultKeyedValueDataset("Test Key", 45.5);
        var dataset2 = new DefaultKeyedValueDataset("Test Key", 45.5);

        // Assert
        assertEquals(dataset1, dataset2, "Datasets with identical state should be equal.");
        assertEquals(dataset2, dataset1, "Equality should be symmetric.");
    }

    @Test
    @DisplayName("should return false for datasets with different keys")
    void equals_whenKeysDiffer_shouldReturnFalse() {
        // Arrange
        var datasetWithKey1 = new DefaultKeyedValueDataset("Test Key 1", 45.5);
        var datasetWithKey2 = new DefaultKeyedValueDataset("Test Key 2", 45.5);

        // Assert
        assertNotEquals(datasetWithKey1, datasetWithKey2, "Datasets with different keys should not be equal.");
    }

    @Test
    @DisplayName("should return false for datasets with different values")
    void equals_whenValuesDiffer_shouldReturnFalse() {
        // Arrange
        var datasetWithValue1 = new DefaultKeyedValueDataset("Test Key", 45.5);
        var datasetWithValue2 = new DefaultKeyedValueDataset("Test Key", 45.6);

        // Assert
        assertNotEquals(datasetWithValue1, datasetWithValue2, "Datasets with different values should not be equal.");
    }
}