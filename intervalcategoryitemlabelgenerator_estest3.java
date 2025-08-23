package org.jfree.chart.labels;

import org.jfree.data.category.CategoryDataset;
import org.junit.Test;

/**
 * Unit tests for the {@link IntervalCategoryItemLabelGenerator} class.
 */
public class IntervalCategoryItemLabelGeneratorTest {

    /**
     * Verifies that the createItemArray method throws a NullPointerException
     * when the provided dataset is null. The method's contract requires a
     * non-null dataset.
     */
    @Test(expected = NullPointerException.class)
    public void createItemArray_withNullDataset_shouldThrowNullPointerException() {
        // Arrange: Create an instance of the label generator.
        IntervalCategoryItemLabelGenerator generator = new IntervalCategoryItemLabelGenerator();
        CategoryDataset nullDataset = null;
        int anyRow = 0;
        int anyColumn = 0;

        // Act: Call the method under test with a null dataset.
        // Assert: The test expects a NullPointerException, which is handled by the
        // @Test(expected=...) annotation.
        generator.createItemArray(nullDataset, anyRow, anyColumn);
    }
}