package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test suite contains tests for the equals() method of the DefaultPieDataset class.
 */
public class DefaultPieDatasetTest {

    /**
     * Tests that the equals() method returns false when a DefaultPieDataset
     * is compared with an object of a different, albeit related, type.
     */
    @Test
    public void equals_returnsFalse_whenComparedWithDifferentDatasetType() {
        // Arrange: Create a populated DefaultPieDataset.
        DefaultPieDataset<String> pieDataset = new DefaultPieDataset<>();
        pieDataset.setValue("Sample Key", 100.0);

        // Arrange: Create an empty dataset of a different type.
        DefaultKeyedValuesDataset<String> otherDataset = new DefaultKeyedValuesDataset<>();

        // Act: Compare the two datasets for equality.
        boolean areEqual = pieDataset.equals(otherDataset);

        // Assert: The datasets should not be equal, as they are of different types
        // and have different content.
        assertFalse(areEqual);
    }
}