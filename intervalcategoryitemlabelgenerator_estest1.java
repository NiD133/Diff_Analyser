package org.jfree.chart.labels;

import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link IntervalCategoryItemLabelGenerator} class.
 * This refactored test focuses on clarity and maintainability.
 */
public class IntervalCategoryItemLabelGenerator_ESTestTest1 extends IntervalCategoryItemLabelGenerator_ESTest_scaffolding {

    /**
     * Verifies that createItemArray throws an IllegalArgumentException when the
     * dataset contains a null value for the requested item. This occurs because
     * the generator's default NumberFormat cannot format a null object.
     */
    @Test(timeout = 4000)
    public void createItemArray_forCellWithNullValue_throwsIllegalArgumentException() {
        // Arrange
        // The generator is created with a default NumberFormat that throws an
        // exception when attempting to format a null value.
        IntervalCategoryItemLabelGenerator generator = new IntervalCategoryItemLabelGenerator();

        // Create a simple dataset with a single data point. This means that
        // any other cell in the dataset will contain a null value.
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(100.0, "Series 1", "Category 1");

        // Define coordinates for a cell that is known to be empty (null).
        // The dataset has a value at (row=0, col=0), but not at (row=0, col=1).
        // Therefore, dataset.getValue(0, 1) will return null.
        int rowWithData = 0;
        int columnWithoutData = 1;

        // Act & Assert
        try {
            generator.createItemArray(dataset, rowWithData, columnWithoutData);
            fail("Expected an IllegalArgumentException because the formatter cannot handle a null value.");
        } catch (IllegalArgumentException e) {
            // The exception is thrown by the internal NumberFormat when it receives the null value.
            // We verify the message to ensure the failure is for the expected reason.
            assertEquals("Cannot format given Object as a Number", e.getMessage());
        }
    }
}