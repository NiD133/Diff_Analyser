package org.jfree.chart.labels;

import org.jfree.data.category.DefaultIntervalCategoryDataset;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the {@link IntervalCategoryItemLabelGenerator} class.
 */
public class IntervalCategoryItemLabelGeneratorTest {

    /**
     * Verifies that createItemArray returns an array of size 5 when used with
     * an IntervalCategoryDataset. This array is structured to hold the series key,
     * category key, primary value, interval start value, and interval end value,
     * which are used for formatting the item label.
     */
    @Test
    public void createItemArray_forIntervalDataset_returnsArrayOfSizeFive() {
        // Arrange: Create a generator and a sample interval dataset.
        IntervalCategoryItemLabelGenerator generator = new IntervalCategoryItemLabelGenerator();

        // The dataset requires start and end values for the intervals. The actual
        // numbers are not important for this test, only the data structure.
        double[][] startValues = new double[][]{{1.0, 2.0}, {3.0, 4.0}};
        double[][] endValues = new double[][]{{1.5, 2.5}, {3.5, 4.5}};
        DefaultIntervalCategoryDataset dataset = new DefaultIntervalCategoryDataset(startValues, endValues);

        int testRow = 1;
        int testColumn = 0;

        // Act: Generate the array of items for a specific data point.
        Object[] itemArray = generator.createItemArray(dataset, testRow, testColumn);

        // Assert: The returned array should have exactly 5 elements.
        assertNotNull("The created item array should not be null.", itemArray);
        assertEquals("The item array should have 5 elements for an interval dataset.", 5, itemArray.length);
    }
}