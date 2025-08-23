package org.jfree.chart.labels;

import org.jfree.data.category.DefaultIntervalCategoryDataset;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link IntervalCategoryToolTipGenerator} class.
 */
public class IntervalCategoryToolTipGeneratorTest {

    /**
     * Verifies that the createItemArray method returns an array with 5 elements
     * when used with an IntervalCategoryDataset. This size is required to populate
     * the default tooltip format string.
     */
    @Test
    public void createItemArray_withIntervalDataset_returnsArrayOfFiveElements() {
        // Arrange
        // Create a minimal 1x1 dataset with interval values.
        double[][] startValues = {{10.0}};
        double[][] endValues = {{20.0}};
        DefaultIntervalCategoryDataset dataset = new DefaultIntervalCategoryDataset(startValues, endValues);

        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator();
        int row = 0;
        int column = 0;

        // Act
        Object[] items = generator.createItemArray(dataset, row, column);

        // Assert
        // The generated array should contain 5 elements to match the format string placeholders:
        // {0}: Series Key
        // {1}: Category Key
        // {2}: Value (the interval end value)
        // {3}: Interval Start Value
        // {4}: Interval End Value
        final int expectedArraySize = 5;
        assertEquals("The item array should have 5 elements for an interval dataset.",
                expectedArraySize, items.length);
    }
}