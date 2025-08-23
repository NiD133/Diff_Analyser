package org.jfree.chart.labels;

import org.jfree.data.category.DefaultIntervalCategoryDataset;
import org.junit.Test;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link IntervalCategoryItemLabelGenerator} class, focusing on
 * the createItemArray method.
 */
public class IntervalCategoryItemLabelGeneratorTest {

    /**
     * Verifies that createItemArray produces a correctly structured and populated
     * object array for an IntervalCategoryDataset. The array should contain the
     * series key, category key, and the formatted value, start value, and end value.
     */
    @Test
    public void createItemArray_withIntervalCategoryDataset_returnsCorrectlyPopulatedArray() {
        // Arrange
        // 1. Define the data for our test dataset.
        String[] seriesKeys = {"Series A"};
        String[] categoryKeys = {"Category 1"};
        Number[][] startValues = {{10.5}};
        Number[][] endValues = {{25.5}};
        DefaultIntervalCategoryDataset dataset = new DefaultIntervalCategoryDataset(
                seriesKeys, categoryKeys, startValues, endValues
        );

        // 2. Create a formatter and the label generator under test.
        // The label format string doesn't affect createItemArray, but we use a realistic one.
        NumberFormat valueFormatter = new DecimalFormat("0.0");
        IntervalCategoryItemLabelGenerator generator = new IntervalCategoryItemLabelGenerator(
                "{0} - {1}: {3} to {4}", valueFormatter
        );

        // Act
        // 3. Call the method under test for the first data point (row 0, column 0).
        Object[] itemArray = generator.createItemArray(dataset, 0, 0);

        // Assert
        // 4. Verify the contents of the returned array.
        // The array should have 5 elements:
        // [0]: Series Key
        // [1]: Category Key
        // [2]: Formatted Value (which is the end value)
        // [3]: Formatted Start Value
        // [4]: Formatted End Value
        Object[] expectedArray = {
                "Series A",      // Series Key
                "Category 1",    // Category Key
                "25.5",          // Formatted end value (used as the primary value)
                "10.5",          // Formatted start value
                "25.5"           // Formatted end value (again)
        };

        assertArrayEquals(expectedArray, itemArray);
        assertEquals("The item array should have 5 elements for an interval dataset.", 5, itemArray.length);
    }
}