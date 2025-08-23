package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link PlotRenderingInfo} class.
 */
public class PlotRenderingInfoTest {

    /**
     * Verifies the reflexive property of the equals() method.
     * An object must always be equal to itself.
     */
    @Test
    public void equals_shouldReturnTrue_whenComparedToItself() {
        // Arrange: Create a PlotRenderingInfo instance.
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = chartInfo.getPlotInfo();

        // Act & Assert: The object should be equal to itself.
        // Using assertEquals is more semantic for equality checks.
        assertEquals(plotInfo, plotInfo);
    }
}