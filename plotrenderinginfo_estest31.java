package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.junit.Test;

/**
 * Unit tests for the {@link PlotRenderingInfo} class.
 */
public class PlotRenderingInfoTest {

    /**
     * Verifies that getSubplotInfo() throws an IndexOutOfBoundsException
     * when attempting to access a subplot index that does not exist.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void getSubplotInfo_withInvalidIndex_throwsIndexOutOfBoundsException() {
        // Arrange: Create a PlotRenderingInfo instance which, by default, has no subplots.
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = chartInfo.getPlotInfo();

        // Act: Attempt to retrieve a subplot using an index that is out of bounds
        // for an empty list.
        plotInfo.getSubplotInfo(1);

        // Assert: The test passes if an IndexOutOfBoundsException is thrown,
        // which is handled by the @Test(expected=...) annotation.
    }
}