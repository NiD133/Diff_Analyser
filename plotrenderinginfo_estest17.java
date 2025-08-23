package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

// Note: The original test class name and extension are kept for context.
public class PlotRenderingInfo_ESTestTest17 extends PlotRenderingInfo_ESTest_scaffolding {

    /**
     * Verifies that cloning a PlotRenderingInfo object does not alter the state
     * of the original object, specifically its subplot count.
     */
    @Test(timeout = 4000)
    public void cloneShouldNotChangeSubplotCountOfOriginal() throws CloneNotSupportedException {
        // Arrange: Create a PlotRenderingInfo instance and add a subplot to it.
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo originalPlotInfo = new PlotRenderingInfo(chartInfo);
        
        // Use the default plot info from the chart info as our subplot.
        PlotRenderingInfo subplotInfo = chartInfo.getPlotInfo();
        originalPlotInfo.addSubplotInfo(subplotInfo);

        // Act: Clone the original object. The purpose of this test is to ensure
        // this action has no side effects on the original object.
        originalPlotInfo.clone();

        // Assert: The subplot count of the original object should remain unchanged.
        final int expectedSubplotCount = 1;
        assertEquals("The subplot count of the original object should not change after cloning.",
                expectedSubplotCount, originalPlotInfo.getSubplotCount());
    }
}