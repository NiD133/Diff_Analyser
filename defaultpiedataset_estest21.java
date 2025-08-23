package org.jfree.data.general;

import org.jfree.chart.api.TableOrder;
import org.jfree.data.category.CategoryToPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.Test;

/**
 * This test class focuses on the DefaultPieDataset.
 * This specific test case verifies the behavior of the constructor when it receives
 * a PieDataset adapter (CategoryToPieDataset) that is configured with an invalid index.
 */
public class DefaultPieDataset_ESTestTest21 {

    /**
     * Tests that the DefaultPieDataset constructor throws an IndexOutOfBoundsException
     * when initialized with a CategoryToPieDataset that has an invalid 'extract' index.
     * The CategoryToPieDataset acts as an adapter, and when the DefaultPieDataset
     * constructor tries to read data from it, the invalid index causes the exception.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void constructorWithAdapterThrowsExceptionForInvalidExtractIndex() {
        // Arrange: Create a source CategoryDataset with one data point.
        // This serves as the underlying data for the adapter.
        DefaultCategoryDataset sourceCategoryDataset = new DefaultCategoryDataset();
        sourceCategoryDataset.addValue(150.0, "RowKey", "ColumnKey");

        // Arrange: Create a CategoryToPieDataset adapter. This adapter is configured
        // to extract data from a specific column of the source dataset. We intentionally
        // provide an invalid column index (-1) to trigger an error.
        final int invalidColumnIndex = -1;
        CategoryToPieDataset adapterDataset = new CategoryToPieDataset(
                sourceCategoryDataset, TableOrder.BY_COLUMN, invalidColumnIndex);

        // Act & Assert: Attempt to create a DefaultPieDataset from the misconfigured
        // adapter. The constructor will try to access data using the invalid index,
        // which is expected to throw an IndexOutOfBoundsException.
        new DefaultPieDataset<>(adapterDataset);
    }
}