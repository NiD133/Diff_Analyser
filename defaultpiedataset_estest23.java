package org.jfree.data.general;

import org.jfree.chart.api.TableOrder;
import org.jfree.data.category.CategoryToPieDataset;
import org.jfree.data.category.DefaultIntervalCategoryDataset;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link DefaultPieDataset} class, focusing on its constructor.
 */
public class DefaultPieDatasetConstructorTest {

    /**
     * Tests that the constructor correctly propagates an ArrayIndexOutOfBoundsException
     * when its source data is a wrapper around a malformed dataset.
     *
     * This scenario is created by:
     * 1. Creating a DefaultIntervalCategoryDataset with an inconsistent data array
     *    (one row is empty).
     * 2. Wrapping it in a CategoryToPieDataset configured to extract a column
     *    that does not exist in the empty row.
     * 3. Passing this wrapper to the DefaultPieDataset constructor, which should
     *    trigger and propagate the underlying exception.
     */
    @Test
    public void constructorWithFaultySourceShouldThrowArrayIndexOutOfBoundsException() {
        // Arrange: Create a source dataset that is intentionally malformed.
        // It has three series (rows), but the last one is an empty array.
        double[][] seriesData = new double[3][9];
        double[] emptySeries = new double[0];
        seriesData[2] = emptySeries;

        DefaultIntervalCategoryDataset sourceCategoryDataset =
                new DefaultIntervalCategoryDataset(seriesData, seriesData);

        // This adapter is configured to extract data from column index 1. When it
        // iterates to the third series (which is empty), it will request an
        // element at index 1, causing an ArrayIndexOutOfBoundsException.
        int columnToExtract = 1;
        CategoryToPieDataset pieDataSource = new CategoryToPieDataset(
                sourceCategoryDataset, TableOrder.BY_COLUMN, columnToExtract);

        // Act & Assert
        try {
            // The constructor attempts to read all values from the faulty pieDataSource.
            new DefaultPieDataset<>(pieDataSource);
            fail("Expected an ArrayIndexOutOfBoundsException to be thrown.");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Verify that the expected exception was caught and has the correct message.
            // This confirms the exception originated from the access attempt on the empty series.
            String expectedMessage = "Index 1 out of bounds for length 0";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}