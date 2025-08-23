package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link PlotRenderingInfo} class.
 */
public class PlotRenderingInfoTest {

    /**
     * Verifies that a newly instantiated PlotRenderingInfo object
     * correctly reports having zero subplots.
     */
    @Test
    public void newPlotRenderingInfoShouldHaveZeroSubplots() {
        // Arrange: Create a new PlotRenderingInfo instance, which requires a
        // ChartRenderingInfo owner.
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = new PlotRenderingInfo(chartInfo);

        // Act: Retrieve the subplot count from the new instance.
        int subplotCount = plotInfo.getSubplotCount();

        // Assert: The initial subplot count should be zero.
        assertEquals("A new PlotRenderingInfo should have no subplots.", 0, subplotCount);
    }
}