package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the subplot management features of the {@link PlotRenderingInfo} class.
 */
public class PlotRenderingInfoTest {

    /**
     * Verifies that getSubplotCount() correctly reflects the number of subplots
     * after a new subplot info object has been added.
     */
    @Test
    public void getSubplotCount_shouldReturnOne_afterAddingOneSubplot() {
        // Arrange: Create a main PlotRenderingInfo object.
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = chartInfo.getPlotInfo();

        // Act: Add a subplot and retrieve the count.
        // Note: For simplicity, we add the plotInfo to its own list of subplots.
        plotInfo.addSubplotInfo(plotInfo);
        int subplotCount = plotInfo.getSubplotCount();

        // Assert: The count should be 1.
        assertEquals("The subplot count should be 1 after adding a single subplot.", 1, subplotCount);
    }
}