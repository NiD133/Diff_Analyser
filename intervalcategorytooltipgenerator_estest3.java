package org.jfree.chart.labels;

import org.jfree.data.category.DefaultIntervalCategoryDataset;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * This test class contains tests for the IntervalCategoryToolTipGenerator.
 * This specific test case focuses on verifying the behavior of the createItemArray method.
 */
public class IntervalCategoryToolTipGenerator_ESTestTest3 extends IntervalCategoryToolTipGenerator_ESTest_scaffolding {

    /**
     * Verifies that createItemArray throws an IllegalArgumentException when the
     * requested row index is out of bounds for the given dataset.
     */
    @Test
    public void createItemArray_withOutOfBoundsRow_throwsIllegalArgumentException() {
        // Arrange: Create a generator and an empty dataset with no rows or columns.
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator();
        double[][] emptyData = new double[0][0];
        DefaultIntervalCategoryDataset emptyDataset = new DefaultIntervalCategoryDataset(emptyData, emptyData);

        // Any non-negative row index is out of bounds for an empty dataset.
        int outOfBoundsRow = 0;
        int column = 0;

        // Act & Assert: Expect an exception when accessing the out-of-bounds row.
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> generator.createItemArray(emptyDataset, outOfBoundsRow, column)
        );

        // Verify that the exception message is correct, confirming the error's cause.
        String expectedMessage = "The 'row' argument is out of bounds.";
        assertEquals(expectedMessage, exception.getMessage());
    }
}