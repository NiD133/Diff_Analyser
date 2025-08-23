package org.jfree.chart.labels;

import org.jfree.data.category.CategoryDataset;
import org.junit.Test;

/**
 * Tests for the {@link IntervalCategoryToolTipGenerator} class.
 */
public class IntervalCategoryToolTipGeneratorTest {

    /**
     * Verifies that the createItemArray method throws a NullPointerException
     * when the provided dataset is null. This is the expected behavior to prevent
     * further processing with invalid data.
     */
    @Test(expected = NullPointerException.class)
    public void createItemArray_withNullDataset_shouldThrowNullPointerException() {
        // Arrange: Create an instance of the generator.
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator();
        CategoryDataset nullDataset = null;
        int anyRow = 0;
        int anyColumn = 0;

        // Act & Assert: Call the method with a null dataset, which is expected to throw.
        generator.createItemArray(nullDataset, anyRow, anyColumn);
    }
}