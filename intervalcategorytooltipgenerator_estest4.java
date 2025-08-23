package org.jfree.chart.labels;

import org.jfree.data.category.DefaultIntervalCategoryDataset;
import org.junit.Test;

/**
 * Tests for the {@link IntervalCategoryToolTipGenerator} class, focusing on exception handling.
 */
public class IntervalCategoryToolTipGenerator_ESTestTest4 {

    /**
     * Verifies that createItemArray throws an ArrayIndexOutOfBoundsException
     * when the provided column index is out of bounds for the dataset.
     * The exception is expected to originate from the dataset itself when the
     * generator attempts to access data.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void createItemArrayShouldThrowExceptionForInvalidColumnIndex() {
        // Arrange: Create a dataset with a known size and a tooltip generator.
        final int numberOfRows = 14;
        final int numberOfColumns = 4;
        double[][] data = new double[numberOfRows][numberOfColumns];
        DefaultIntervalCategoryDataset dataset = new DefaultIntervalCategoryDataset(data, data);
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator();

        int validRowIndex = 13;
        int invalidColumnIndex = 13; // This index is out of bounds (valid: 0-3)

        // Act: Attempt to create the item array with an invalid column index.
        // The @Test(expected) annotation will assert that the correct exception is thrown.
        generator.createItemArray(dataset, validRowIndex, invalidColumnIndex);
    }
}