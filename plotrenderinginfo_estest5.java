package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.junit.Test;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link PlotRenderingInfo} class.
 */
public class PlotRenderingInfoTest {

    /**
     * Verifies that getPlotArea() correctly returns the Rectangle2D object
     * that was previously set by setPlotArea().
     */
    @Test
    public void getPlotArea_shouldReturnTheAreaPreviouslySet() {
        // Arrange: Create a PlotRenderingInfo and a test rectangle.
        ChartRenderingInfo chartRenderingInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotRenderingInfo = new PlotRenderingInfo(chartRenderingInfo);
        Rectangle2D expectedPlotArea = new Rectangle(10, 20, 300, 200);

        // Act: Set the plot area and then retrieve it.
        plotRenderingInfo.setPlotArea(expectedPlotArea);
        Rectangle2D actualPlotArea = plotRenderingInfo.getPlotArea();

        // Assert: The retrieved area should be identical to the one that was set.
        assertEquals(expectedPlotArea, actualPlotArea);
    }
}