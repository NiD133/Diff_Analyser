package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.junit.Test;
import java.awt.geom.Rectangle2D;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link PlotRenderingInfo} class.
 */
public class PlotRenderingInfoTest {

    /**
     * Verifies that getDataArea() correctly returns the rectangle
     * that was previously set by setDataArea().
     */
    @Test
    public void testSetAndGetDataArea() {
        // Arrange: Create a PlotRenderingInfo instance and a test rectangle.
        // The original test used a rectangle with zero height, which is an interesting
        // edge case to preserve.
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = chartInfo.getPlotInfo();
        Rectangle2D expectedDataArea = new Rectangle2D.Double(10.0, 20.0, 300.0, 0.0);

        // Act: Set the data area for the plot.
        plotInfo.setDataArea(expectedDataArea);
        Rectangle2D actualDataArea = plotInfo.getDataArea();

        // Assert: The retrieved data area should be equal to the one that was set.
        assertEquals(expectedDataArea, actualDataArea);
    }
}