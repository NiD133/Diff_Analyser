package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.junit.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the {@link PlotRenderingInfo} class, focusing on the hashCode method.
 */
public class PlotRenderingInfoTest {

    /**
     * Verifies that the hashCode() method returns a consistent value
     * when called multiple times on the same object after its state has been set.
     * According to the Object.hashCode() contract, this is a required behavior.
     */
    @Test
    public void hashCode_shouldBeConsistent_whenDataAreaIsSet() {
        // Arrange: Create a PlotRenderingInfo instance and set its data area.
        ChartRenderingInfo chartRenderingInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = chartRenderingInfo.getPlotInfo();
        assertNotNull("The plot info should not be null after initialization.", plotInfo);

        Rectangle2D dataArea = new Rectangle2D.Double(10.0, 20.0, 30.0, 40.0);
        plotInfo.setDataArea(dataArea);

        // Act: Calculate the hash code multiple times without changing the object's state.
        int firstHashCode = plotInfo.hashCode();
        int secondHashCode = plotInfo.hashCode();

        // Assert: The hash code must be the same for each call.
        assertEquals("hashCode() must be consistent for an unchanged object.", firstHashCode, secondHashCode);
    }
}