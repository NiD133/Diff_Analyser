package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.junit.Test;
import java.awt.geom.Rectangle2D;
import static org.junit.Assert.assertNull;

/**
 * A test case for the PlotRenderingInfo class, focusing on its initial state.
 * This is an improved version of an auto-generated test.
 */
public class PlotRenderingInfo_ESTestTest28 extends PlotRenderingInfo_ESTest_scaffolding {

    /**
     * Verifies that the plot area is null for a newly created PlotRenderingInfo instance.
     * The plot area is expected to be set later during the chart drawing process.
     */
    @Test
    public void getPlotArea_shouldReturnNull_whenNewlyConstructed() {
        // Arrange: Create a new PlotRenderingInfo instance, which requires a
        // ChartRenderingInfo owner.
        ChartRenderingInfo ownerInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = new PlotRenderingInfo(ownerInfo);

        // Act: Retrieve the plot area from the newly created instance.
        Rectangle2D plotArea = plotInfo.getPlotArea();

        // Assert: The plot area should be null by default, as it has not been set yet.
        assertNull("The plot area should be null upon initialization.", plotArea);
    }
}