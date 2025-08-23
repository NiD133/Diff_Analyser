package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.junit.Test;
import java.awt.geom.Rectangle2D;
import static org.junit.Assert.assertNull;

/**
 * Tests for the {@link PlotRenderingInfo} class.
 */
public class PlotRenderingInfoTest {

    /**
     * Verifies that getDataArea() returns null after the data area has been explicitly set to null.
     * This confirms the setter and getter work correctly with null values.
     */
    @Test
    public void getDataArea_shouldReturnNull_whenDataAreaIsSetToNull() {
        // Arrange: Create a PlotRenderingInfo instance.
        ChartRenderingInfo owner = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = new PlotRenderingInfo(owner);

        // Act: Set the data area to null and then retrieve it.
        plotInfo.setDataArea(null);
        Rectangle2D result = plotInfo.getDataArea();

        // Assert: The retrieved data area should be null.
        assertNull("The data area should be null after being set to null.", result);
    }
}