package org.jfree.chart.labels;

import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link SymbolicXYItemLabelGenerator} class.
 */
public class SymbolicXYItemLabelGeneratorTest {

    /**
     * Verifies that generateLabel() returns null when the series and item indices
     * are out of bounds for the given dataset. Negative indices are always considered
     * out of bounds.
     */
    @Test
    public void generateLabel_withOutOfBoundsIndices_shouldReturnNull() {
        // Arrange
        SymbolicXYItemLabelGenerator generator = new SymbolicXYItemLabelGenerator();
        // Use an empty dataset, for which any index is out of bounds.
        XYDataset emptyDataset = new TimeSeriesCollection();
        int invalidSeriesIndex = -1;
        int invalidItemIndex = -1;

        // Act
        String label = generator.generateLabel(emptyDataset, invalidSeriesIndex, invalidItemIndex);

        // Assert
        assertNull("The generated label should be null for out-of-bounds indices.", label);
    }
}