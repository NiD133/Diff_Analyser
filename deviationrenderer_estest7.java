package org.jfree.chart.renderer.xy;

import org.jfree.data.Range;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the {@link DeviationRenderer} class, focusing on the findRangeBounds method.
 */
public class DeviationRendererTest {

    /**
     * Verifies that findRangeBounds correctly calculates the data range
     * when the dataset includes negative values. The expected range should
     * encompass both the lowest and highest values in the dataset.
     */
    @Test
    public void findRangeBounds_withNegativeValues_returnsCorrectRange() {
        // Arrange
        DeviationRenderer renderer = new DeviationRenderer();

        // Create a dataset with a mix of zero and negative values.
        // The y-values (low, high) for the three data points are:
        // (0.0, 0.0), (-1.0, -1.0), (0.0, 0.0)
        // The overall range should therefore be from -1.0 to 0.0.
        Date[] dates = {new Date(100L), new Date(200L), new Date(300L)};
        double[] highValues = {0.0, -1.0, 0.0};
        double[] lowValues = {0.0, -1.0, 0.0};
        // Note: open, close, and volume are not used by DeviationRenderer for range calculation.
        DefaultHighLowDataset dataset = new DefaultHighLowDataset(
                "Series 1", dates, highValues, lowValues, lowValues, lowValues, lowValues);

        // Act
        Range actualRange = renderer.findRangeBounds(dataset);

        // Assert
        assertNotNull("The calculated range should not be null.", actualRange);
        assertEquals("The lower bound of the range is incorrect.", -1.0, actualRange.getLowerBound(), 0.001);
        assertEquals("The upper bound of the range is incorrect.", 0.0, actualRange.getUpperBound(), 0.001);
    }
}