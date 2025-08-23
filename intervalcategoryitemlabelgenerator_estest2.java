package org.jfree.chart.labels;

import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.Test;

/**
 * Tests for the {@link IntervalCategoryItemLabelGenerator} class, focusing on exception handling.
 */
public class IntervalCategoryItemLabelGeneratorTest {

    /**
     * Verifies that createItemArray throws an IndexOutOfBoundsException when an invalid column index is provided.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void createItemArray_withInvalidColumnIndex_shouldThrowException() {
        // Arrange: Set up the test objects and state.
        IntervalCategoryItemLabelGenerator generator = new IntervalCategoryItemLabelGenerator();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Add a single data point, which creates one row (index 0) and one column (index 0).
        dataset.addValue(100.0, "Row 1", "Column 1");

        int validRowIndex = 0;
        int invalidColumnIndex = 1; // This index is out of bounds for the dataset.

        // Act: Call the method under test with invalid arguments.
        // This call is expected to throw an IndexOutOfBoundsException.
        generator.createItemArray(dataset, validRowIndex, invalidColumnIndex);

        // Assert: The test passes if the expected exception is thrown.
        // This is handled by the @Test(expected = ...) annotation.
    }
}