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
     * that was previously set via setPlotArea().
     */
    @Test
    public void getPlotArea_shouldReturnRectangleSetBySetPlotArea() {
        // Arrange
        final int plotWidth = 1595;
        final int plotHeight = 2;
        
        ChartRenderingInfo chartInfo = new ChartRenderingInfo(null);
        PlotRenderingInfo plotInfo = chartInfo.getPlotInfo();
        Rectangle2D expectedPlotArea = new Rectangle(plotWidth, plotHeight);

        // Act
        plotInfo.setPlotArea(expectedPlotArea);
        Rectangle2D actualPlotArea = plotInfo.getPlotArea();

        // Assert
        assertEquals("The retrieved plot area should be identical to the one that was set.",
                expectedPlotArea, actualPlotArea);
    }
}