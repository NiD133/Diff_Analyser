package org.jfree.chart.labels;

import org.jfree.data.xy.DefaultXYDataset;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link SymbolicXYItemLabelGenerator} class.
 */
public class SymbolicXYItemLabelGeneratorTest {

    /**
     * Verifies that the generateToolTip method produces the default "X: {x}, Y: {y}"
     * format for a standard XYDataset.
     */
    @Test
    public void generateToolTip_shouldReturnDefaultFormattedString() {
        // Arrange
        SymbolicXYItemLabelGenerator generator = new SymbolicXYItemLabelGenerator();

        // Create a simple dataset with a single data point (102.0, 0.0)
        DefaultXYDataset dataset = new DefaultXYDataset();
        double[][] data = {{102.0}, {0.0}};
        dataset.addSeries("Series 1", data);

        int seriesIndex = 0;
        int itemIndex = 0;

        String expectedToolTip = "X: 102.0, Y: 0.0";

        // Act
        String actualToolTip = generator.generateToolTip(dataset, seriesIndex, itemIndex);

        // Assert
        assertEquals(expectedToolTip, actualToolTip);
    }
}