package org.jfree.chart.labels;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.junit.Test;

/**
 * This test suite contains tests for the {@link IntervalCategoryToolTipGenerator} class.
 * This specific test was improved for clarity and maintainability.
 */
// The original test class name and inheritance from a scaffolding class are preserved.
public class IntervalCategoryToolTipGenerator_ESTestTest2 extends IntervalCategoryToolTipGenerator_ESTest_scaffolding {

    /**
     * Verifies that calling createItemArray with negative row and column indices
     * correctly throws an IndexOutOfBoundsException.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void createItemArray_withNegativeIndices_shouldThrowIndexOutOfBoundsException() {
        // Arrange: Create a generator instance and an empty dataset.
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator();
        CategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
        int invalidRow = -1;
        int invalidColumn = -1;

        // Act & Assert: Call the method with invalid indices, which is expected
        // to throw an IndexOutOfBoundsException.
        generator.createItemArray(dataset, invalidRow, invalidColumn);
    }
}