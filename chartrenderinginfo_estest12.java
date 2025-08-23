package org.jfree.chart;

import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.CombinedRangeCategoryPlot;
import org.jfree.chart.plot.Plot;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Contains tests for the {@link ChartRenderingInfo} class, focusing on its equality logic.
 */
public class ImprovedChartRenderingInfoTest {

    /**
     * Verifies that the equals() method returns false after one of two
     * identical ChartRenderingInfo instances is modified by a chart rendering operation.
     * The rendering process populates the instance with data (like the chart area),
     * which should cause the equality check to fail.
     */
    @Test(timeout = 4000)
    public void equals_shouldReturnFalse_whenStateIsModifiedByChartRendering() {
        // Arrange: Create two identical ChartRenderingInfo objects.
        ChartRenderingInfo info1 = new ChartRenderingInfo();
        ChartRenderingInfo info2 = new ChartRenderingInfo();

        // Sanity check: two newly created instances should be equal.
        assertEquals("Two new ChartRenderingInfo instances should be equal.", info1, info2);

        // Arrange: Create a simple chart. The specific chart type is not important;
        // we only need it to trigger the rendering process that modifies the info object.
        Plot plot = new CombinedRangeCategoryPlot(new DateAxis("Time"));
        JFreeChart chart = new JFreeChart("Test Chart", plot);

        // Act: Render the chart. This action populates 'info1' with details about
        // the chart's rendered area, thus changing its internal state. The 'info2'
        // object remains in its original, pristine state.
        chart.createBufferedImage(100, 100, info1);

        // Assert: After modification, 'info1' should no longer be equal to 'info2'.
        assertNotEquals("An instance modified by rendering should not be equal to a pristine instance.", info1, info2);
    }
}