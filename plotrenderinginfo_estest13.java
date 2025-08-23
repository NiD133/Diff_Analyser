package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.junit.Test;
import java.awt.geom.Rectangle2D;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link PlotRenderingInfo} class.
 */
public class PlotRenderingInfoTest {

    /**
     * Verifies that getDataArea() correctly returns the Rectangle2D object
     * that was previously set via setDataArea().
     */
    @Test
    public void getDataArea_shouldReturnRectangleSetBySetDataArea() {
        // Arrange: Create a PlotRenderingInfo instance and a test rectangle.
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = chartInfo.getPlotInfo();
        Rectangle2D.Double expectedDataArea = new Rectangle2D.Double(10.0, 20.0, 100.0, 80.0);

        // Act: Set the data area and then retrieve it.
        plotInfo.setDataArea(expectedDataArea);
        Rectangle2D actualDataArea = plotInfo.getDataArea();

        // Assert: The retrieved data area should be identical to the one that was set.
        assertEquals(expectedDataArea, actualDataArea);
    }
}