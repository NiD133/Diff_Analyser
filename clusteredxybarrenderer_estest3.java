package org.jfree.chart.renderer.xy;

import org.jfree.data.Range;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the {@link ClusteredXYBarRenderer#findDomainBoundsWithOffset(org.jfree.data.xy.IntervalXYDataset)} method.
 */
public class ClusteredXYBarRenderer_ESTestTest3 {

    /**
     * Verifies that findDomainBoundsWithOffset correctly calculates the domain range
     * for a dataset containing a single data point at x=0.
     *
     * The method is expected to calculate a range for a bar of default width 1.0.
     * With default renderer settings, this range appears to be anchored such that
     * it *ends* at the data point's x-value, resulting in a range of [-1.0, 0.0].
     */
    @Test
    public void findDomainBoundsWithOffset_forSinglePointAtZero_returnsRangeEndingAtZero() {
        // Arrange
        // Create a renderer with default settings (margin=0.0, centerBarAtStartValue=false)
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();

        // Create a dataset with a single data point at x=0.0.
        // The y-value is not relevant for calculating domain (x-axis) bounds.
        XYSeries series = new XYSeries("Test Series");
        series.add(0.0, 30.0);
        XYSeriesCollection dataset = new XYSeriesCollection(series);

        // Act
        // Calculate the domain bounds, which should account for the bar's width.
        Range domainRange = renderer.findDomainBoundsWithOffset(dataset);

        // Assert
        assertNotNull("The calculated range should not be null.", domainRange);

        // For a point at x=0, the renderer creates a bar of width 1.0. The resulting
        // domain range is expected to be [-1.0, 0.0].
        assertEquals("Lower bound should be -1.0", -1.0, domainRange.getLowerBound(), 0.01);
        assertEquals("Upper bound should be 0.0", 0.0, domainRange.getUpperBound(), 0.01);
        assertEquals("Central value should be -0.5", -0.5, domainRange.getCentralValue(), 0.01);
    }
}