package org.jfree.data.general;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * A test suite for the {@link DefaultPieDataset} class.
 */
public class DefaultPieDatasetTest {

    /**
     * Verifies that the equals() method returns false when a DefaultPieDataset
     * is compared with an object of a different class, even if it's another
     * type of dataset.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparedWithDifferentClass() {
        // Arrange: Create a DefaultPieDataset with one data entry.
        DefaultPieDataset<String> pieDataset = new DefaultPieDataset<>();
        pieDataset.setValue("Apples", 50.0);

        // Arrange: Create an object of a different dataset class to compare against.
        DefaultKeyedValuesDataset<String> otherDataset = new DefaultKeyedValuesDataset<>();

        // Act: Compare the two objects using the equals method.
        boolean areEqual = pieDataset.equals(otherDataset);

        // Assert: The objects should not be considered equal.
        assertFalse("A DefaultPieDataset should not be equal to an object of a different class.", areEqual);
    }
}