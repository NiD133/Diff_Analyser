package org.jfree.chart.labels;

import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.junit.Test;

/**
 * Unit tests for the {@link SymbolicXYItemLabelGenerator} class, focusing on its interaction
 * with datasets that have out-of-bounds indices.
 */
// The original class name from the test suite is retained.
// A more conventional name would be SymbolicXYItemLabelGeneratorTest.
public class SymbolicXYItemLabelGenerator_ESTestTest5 {

    /**
     * Verifies that generateToolTip propagates an ArrayIndexOutOfBoundsException when the
     * provided series index is out of bounds for the dataset.
     *
     * This test ensures that the generator does not suppress exceptions from the underlying
     * dataset, which is the expected behavior.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void generateToolTip_whenSeriesIndexIsOutOfBounds_shouldThrowException() {
        // Arrange: Create a generator and a dataset with a known size.
        SymbolicXYItemLabelGenerator generator = new SymbolicXYItemLabelGenerator();

        int seriesCount = 10;
        int itemCountPerSeries = 50;
        XYDataset dataset = new DynamicTimeSeriesCollection(seriesCount, itemCountPerSeries);

        // An index equal to the series count is always out of bounds (valid indices are 0 to 9).
        int invalidSeriesIndex = seriesCount;
        int validItemIndex = 0;

        // Act & Assert: Call the method with the invalid index and expect an exception.
        generator.generateToolTip(dataset, invalidSeriesIndex, validItemIndex);
    }
}