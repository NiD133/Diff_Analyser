package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * A test suite for the equals() method in the {@link DefaultKeyedValueDataset} class.
 */
public class DefaultKeyedValueDatasetEqualsTest {

    /**
     * Tests that an empty dataset is not considered equal to a dataset containing data.
     * The equals() method should correctly handle comparisons between datasets with
     * different content.
     */
    @Test
    public void equals_whenComparingEmptyToNonEmptyDataset_shouldReturnFalse() {
        // Arrange: Create two datasets, one empty and one with a key-value pair.
        DefaultKeyedValueDataset emptyDataset = new DefaultKeyedValueDataset();
        DefaultKeyedValueDataset datasetWithData = new DefaultKeyedValueDataset("Test Key", 123.45);

        // Act: Compare the two datasets for equality.
        boolean areEqual = emptyDataset.equals(datasetWithData);

        // Assert: The datasets should not be equal.
        assertFalse("An empty dataset should not be equal to a non-empty one.", areEqual);
    }

    /**
     * A more idiomatic version of the same test using assertNotEquals for better
     * readability and intent.
     */
    @Test
    public void equals_comparingEmptyAndNonEmptyDatasets_shouldNotBeEqual() {
        // Arrange
        DefaultKeyedValueDataset emptyDataset = new DefaultKeyedValueDataset();
        DefaultKeyedValueDataset datasetWithData = new DefaultKeyedValueDataset("Test Key", 123.45);

        // Act & Assert: The two datasets should not be equal.
        // The comparison should also be symmetric.
        assertNotEquals(emptyDataset, datasetWithData);
        assertNotEquals(datasetWithData, emptyDataset);
    }
}