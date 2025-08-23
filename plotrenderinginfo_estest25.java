package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link PlotRenderingInfo} class, focusing on its equality logic.
 */
public class PlotRenderingInfoTest {

    /**
     * Verifies that two PlotRenderingInfo instances are considered equal if they
     * share the same owner and are in their default, initial state.
     */
    @Test
    public void equals_withSameOwnerAndDefaultState_shouldReturnTrue() {
        // Arrange: Create two PlotRenderingInfo instances with the same owner.
        // One is retrieved from the owner, and the other is created separately.
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        
        PlotRenderingInfo infoFromOwner = chartInfo.getPlotInfo();
        PlotRenderingInfo newInfoWithSameOwner = new PlotRenderingInfo(chartInfo);

        // Act & Assert: The two instances should be equal.
        // The assertEquals method is used here as it provides a more descriptive
        // failure message compared to assertTrue(info1.equals(info2)).
        assertEquals(infoFromOwner, newInfoWithSameOwner);
    }
}