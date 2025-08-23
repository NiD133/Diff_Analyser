package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link PlotRenderingInfo} class.
 */
public class PlotRenderingInfoTest {

    /**
     * Verifies that a subplot's rendering info can be added to a parent plot's
     * info and then retrieved correctly. The retrieved object should be the
     * same instance as the one that was added.
     */
    @Test
    public void getSubplotInfo_shouldReturnTheCorrectSubplotInstance() {
        // Arrange: Create a main plot rendering info and a separate one for a subplot.
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo parentPlotInfo = chartInfo.getPlotInfo();
        PlotRenderingInfo subplotInfoToAdd = new PlotRenderingInfo(chartInfo);

        // Act: Add the subplot info to the parent and then retrieve it by index.
        parentPlotInfo.addSubplotInfo(subplotInfoToAdd);
        PlotRenderingInfo retrievedSubplotInfo = parentPlotInfo.getSubplotInfo(0);

        // Assert: The retrieved object should be the same instance as the one added,
        // and it should not be considered equal to its parent.
        assertSame("The retrieved subplot info should be the same instance that was added.",
                subplotInfoToAdd, retrievedSubplotInfo);
        assertNotEquals("The retrieved subplot info should not be equal to the parent plot info.",
                parentPlotInfo, retrievedSubplotInfo);
    }
}