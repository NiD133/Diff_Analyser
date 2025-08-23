package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.junit.Test;
import java.awt.geom.Rectangle2D;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link PlotRenderingInfo} class, focusing on the data area property.
 */
public class PlotRenderingInfoTest {

    /**
     * Verifies that the getDataArea() method correctly returns the Rectangle2D
     * object that was previously set by the setDataArea() method.
     */
    @Test
    public void getDataArea_ShouldReturnPreviouslySetDataArea() {
        // Arrange: Create a PlotRenderingInfo instance and a sample data area.
        ChartRenderingInfo chartRenderingInfo = new ChartRenderingInfo(null);
        PlotRenderingInfo plotInfo = new PlotRenderingInfo(chartRenderingInfo);
        Rectangle2D expectedDataArea = new Rectangle2D.Double(10.0, 20.0, 100.0, 200.0);

        // Act: Set the data area and then retrieve it.
        plotInfo.setDataArea(expectedDataArea);
        Rectangle2D actualDataArea = plotInfo.getDataArea();

        // Assert: The retrieved data area should be equal to the one that was set.
        assertEquals("The retrieved data area should match the one that was set.",
                expectedDataArea, actualDataArea);
    }
}