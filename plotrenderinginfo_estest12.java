package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.junit.Test;
import java.awt.geom.Rectangle2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Contains unit tests for the {@link PlotRenderingInfo} class.
 */
public class PlotRenderingInfoTest {

    /**
     * Verifies that the getDataArea() method correctly returns the same Rectangle2D
     * instance that was previously set via the setDataArea() method.
     */
    @Test
    public void getDataArea_shouldReturnTheSameInstanceSetBySetDataArea() {
        // Arrange: Create a PlotRenderingInfo and a specific data area rectangle.
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = chartInfo.getPlotInfo();
        Rectangle2D expectedDataArea = new Rectangle2D.Double(10.0, 20.0, 300.0, 200.0);

        // Act: Set the data area on the PlotRenderingInfo object.
        plotInfo.setDataArea(expectedDataArea);
        Rectangle2D actualDataArea = plotInfo.getDataArea();

        // Assert: The retrieved data area should be the exact same object that was set.
        assertSame("The object returned by getDataArea() should be the same instance " +
                   "that was passed to setDataArea().", expectedDataArea, actualDataArea);
        
        // A check for equality is also good practice for completeness.
        assertEquals("The returned data area should be equal to the one that was set.",
                     expectedDataArea, actualDataArea);
    }
}