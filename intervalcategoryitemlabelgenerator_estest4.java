package org.jfree.chart.labels;

import org.jfree.data.category.DefaultIntervalCategoryDataset;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link IntervalCategoryItemLabelGenerator} class, focusing on its interaction
 * with datasets.
 */
public class IntervalCategoryItemLabelGeneratorTest {

    /**
     * Verifies that createItemArray throws an IllegalArgumentException when provided with an
     * out-of-bounds row index for an empty dataset. The exception is expected to originate from the
     * dataset itself.
     */
    @Test
    public void createItemArray_withInvalidRowForEmptyDataset_throwsIllegalArgumentException() {
        // Arrange: Create a generator and an empty dataset.
        IntervalCategoryItemLabelGenerator generator = new IntervalCategoryItemLabelGenerator();
        
        // An empty dataset has zero rows and zero columns.
        double[][] startValues = new double[0][0];
        double[][] endValues = new double[0][0];
        DefaultIntervalCategoryDataset emptyDataset = new DefaultIntervalCategoryDataset(startValues, endValues);

        int invalidRow = 2;
        int anyColumn = 2;
        String expectedMessage = "The 'row' argument is out of bounds.";

        // Act & Assert: Attempt to create an item array with invalid indices and verify the exception.
        try {
            generator.createItemArray(emptyDataset, invalidRow, anyColumn);
            fail("Expected an IllegalArgumentException because the row index is out of bounds.");
        } catch (IllegalArgumentException e) {
            assertEquals("The exception message should match the expected message from the dataset.",
                    expectedMessage, e.getMessage());
        }
    }
}