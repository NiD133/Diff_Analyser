package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Tests for the subplot management features of the {@link PlotRenderingInfo} class.
 */
public class PlotRenderingInfoTest {

    /**
     * Verifies that adding a subplot correctly increases the subplot count and
     * that the same subplot instance can be retrieved by its index.
     */
    @Test
    public void addAndGetSubplotInfo_shouldStoreAndRetrieveSubplotCorrectly() {
        // Arrange: Create a parent plot info and a separate subplot info.
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo parentPlotInfo = new PlotRenderingInfo(chartInfo);
        PlotRenderingInfo subplotInfoToAdd = new PlotRenderingInfo(chartInfo);

        // Act: Add the subplot info to the parent.
        parentPlotInfo.addSubplotInfo(subplotInfoToAdd);

        // Assert: Verify the state of the parent plot info.
        assertEquals("After adding one subplot, the count should be 1.", 1, parentPlotInfo.getSubplotCount());

        PlotRenderingInfo retrievedSubplotInfo = parentPlotInfo.getSubplotInfo(0);
        assertSame("The retrieved subplot should be the same instance that was added.",
                subplotInfoToAdd, retrievedSubplotInfo);
    }
}