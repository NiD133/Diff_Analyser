package org.jfree.chart;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

/**
 * Unit tests for the {@link ChartRenderingInfo} class.
 */
public class ChartRenderingInfoTest {

    /**
     * Verifies that getChartArea() returns a rectangle with the correct geometric
     * properties after a chart area has been set. This test uses a rectangle
     * with a negative height to ensure correct handling of such edge cases.
     */
    @Test
    public void getChartArea_ShouldReturnRectangleWithCorrectCenterY() {
        // Arrange
        final int y = -782;
        final int height = -2457;
        final Rectangle chartAreaToSet = new Rectangle(-521, y, 0, height);

        // The center Y coordinate is calculated as y + (height / 2.0)
        final double expectedCenterY = y + (height / 2.0); // -782 - 1228.5 = -2010.5

        ChartRenderingInfo renderingInfo = new ChartRenderingInfo();

        // Act
        renderingInfo.setChartArea(chartAreaToSet);
        Rectangle2D retrievedChartArea = renderingInfo.getChartArea();

        // Assert
        assertEquals("The Y-coordinate of the center of the retrieved chart area should be calculated correctly.",
                expectedCenterY, retrievedChartArea.getCenterY(), 0.01);
    }
}