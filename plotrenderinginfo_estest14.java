package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.junit.Test;
import java.awt.geom.Point2D;

/**
 * Unit tests for the {@link PlotRenderingInfo} class.
 */
public class PlotRenderingInfoTest {

    /**
     * Verifies that getSubplotIndex() throws a NullPointerException if the list
     * of subplots contains a null entry. The method iterates through the subplots
     * and attempts to access their data area, which will fail for a null element.
     */
    @Test(expected = NullPointerException.class)
    public void getSubplotIndexShouldThrowNPEForNullSubplotInfo() {
        // Arrange: Create a PlotRenderingInfo and add a null subplot to it.
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = new PlotRenderingInfo(chartInfo);
        plotInfo.addSubplotInfo(null);

        Point2D.Double point = new Point2D.Double(10.0, 20.0);

        // Act & Assert: Calling getSubplotIndex should trigger a NullPointerException
        // when it tries to access the data area of the null subplot.
        plotInfo.getSubplotIndex(point);
    }
}