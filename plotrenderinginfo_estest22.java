package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.junit.Test;
import java.awt.Rectangle;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the equals() method in the {@link PlotRenderingInfo} class.
 */
public class PlotRenderingInfoTest {

    /**
     * Verifies that two PlotRenderingInfo instances are not considered equal
     * if they have different dataArea properties.
     */
    @Test
    public void equals_shouldReturnFalse_whenDataAreasAreDifferent() {
        // Arrange: Create two PlotRenderingInfo instances with different data areas.
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();

        // Create the first instance and assign it a custom data area.
        PlotRenderingInfo info1 = new PlotRenderingInfo(chartInfo);
        info1.setDataArea(new Rectangle(10, 20, 30, 40));

        // The second instance has the default data area (an empty rectangle at 0,0).
        PlotRenderingInfo info2 = new PlotRenderingInfo(chartInfo);

        // Act: Compare the two instances for equality.
        boolean areEqual = info1.equals(info2);

        // Assert: The instances should not be equal.
        assertFalse("PlotRenderingInfo objects with different data areas should not be equal.", areEqual);
    }
}