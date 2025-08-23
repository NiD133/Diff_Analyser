package org.jfree.chart;

import org.junit.Test;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the {@link ChartRenderingInfo} class, focusing on chart area management.
 */
public class ChartRenderingInfoTest {

    /**
     * Verifies that setting the chart area with a Rectangle correctly updates
     * the internal state, and the getter returns an equivalent area.
     */
    @Test
    public void setChartArea_shouldStoreCorrectDimensions() {
        // Arrange: Create a ChartRenderingInfo instance and define an area to be set.
        // Using a null EntityCollection is a valid setup scenario.
        ChartRenderingInfo info = new ChartRenderingInfo(null);
        Rectangle areaToSet = new Rectangle(10, 20, 300, 200);

        // Act: Set the chart area using the defined Rectangle.
        info.setChartArea(areaToSet);

        // Assert: The retrieved area should have the same dimensions as the one that was set.
        Rectangle2D retrievedArea = info.getChartArea();

        assertNotNull("The retrieved chart area should not be null.", retrievedArea);
        assertEquals("X-coordinate should be correctly set.", 10.0, retrievedArea.getX(), 0.0);
        assertEquals("Y-coordinate should be correctly set.", 20.0, retrievedArea.getY(), 0.0);
        assertEquals("Width should be correctly set.", 300.0, retrievedArea.getWidth(), 0.0);
        assertEquals("Height should be correctly set.", 200.0, retrievedArea.getHeight(), 0.0);
    }
}