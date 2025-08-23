package org.jfree.chart;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.awt.geom.Rectangle2D;

/**
 * Unit tests for the {@link ChartRenderingInfo} class.
 */
public class ChartRenderingInfoTest {

    /**
     * Verifies that the chart area set on a ChartRenderingInfo instance
     * can be retrieved correctly.
     */
    @Test
    public void getChartArea_shouldReturnAreaSetBySetter() {
        // Arrange: Create a ChartRenderingInfo instance and define the expected chart area.
        ChartRenderingInfo renderingInfo = new ChartRenderingInfo();
        Rectangle2D expectedArea = new Rectangle2D.Double(-1.0, -1.0, 1.0, 7.0);

        // Act: Set the chart area and then retrieve it.
        renderingInfo.setChartArea(expectedArea);
        Rectangle2D actualArea = renderingInfo.getChartArea();

        // Assert: Verify that the retrieved area is identical to the one that was set.
        assertEquals("The retrieved chart area should be equal to the one that was set.",
                expectedArea, actualArea);
    }
}