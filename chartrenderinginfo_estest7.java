package org.jfree.chart;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.awt.geom.Rectangle2D;

/**
 * Contains tests for the {@link ChartRenderingInfo} class.
 */
public class ChartRenderingInfoTest {

    /**
     * Verifies that the chart area rectangle set via setChartArea()
     * is correctly returned by getChartArea().
     */
    @Test
    public void setAndGetChartArea_shouldPreserveRectangleProperties() {
        // Arrange: Create a ChartRenderingInfo instance and a test rectangle.
        ChartRenderingInfo renderingInfo = new ChartRenderingInfo();
        
        // The rectangle is defined using setFrameFromDiagonal with non-normalized
        // coordinates (where the first point's x-value is greater than the second's).
        // This setup, using simple values, mirrors the logic of the original,
        // more obscure test case. A rectangle from diagonal points (200, 100) and
        // (0, 100) should result in a rectangle at (x=0, y=100) with a width of 200
        // and a height of 0.
        Rectangle2D.Double expectedArea = new Rectangle2D.Double();
        expectedArea.setFrameFromDiagonal(200.0, 100.0, 0.0, 100.0);

        // Act: Set the chart area and then retrieve it.
        renderingInfo.setChartArea(expectedArea);
        Rectangle2D actualArea = renderingInfo.getChartArea();

        // Assert: The retrieved rectangle should be equal in value to the original.
        // For Rectangle2D, assertEquals compares the x, y, width, and height properties.
        assertEquals(expectedArea, actualArea);
    }
}