package org.jfree.chart;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.geom.Rectangle2D;

/**
 * Unit tests for the {@link ChartRenderingInfo} class.
 */
public class ChartRenderingInfoTest {

    /**
     * Verifies that a newly created ChartRenderingInfo instance has an empty
     * chart area by default.
     */
    @Test
    public void newChartRenderingInfoShouldHaveEmptyChartArea() {
        // Arrange: Create a new ChartRenderingInfo instance using the default constructor.
        ChartRenderingInfo renderingInfo = new ChartRenderingInfo();

        // Act: Retrieve the chart area from the new instance.
        Rectangle2D chartArea = renderingInfo.getChartArea();

        // Assert: The retrieved chart area should be an empty rectangle at origin (0,0).
        assertNotNull("The chart area should not be null.", chartArea);
        assertEquals("Default chart area x-coordinate should be 0.0.", 0.0, chartArea.getX(), 0.0);
        assertEquals("Default chart area y-coordinate should be 0.0.", 0.0, chartArea.getY(), 0.0);
        assertEquals("Default chart area width should be 0.0.", 0.0, chartArea.getWidth(), 0.0);
        assertEquals("Default chart area height should be 0.0.", 0.0, chartArea.getHeight(), 0.0);
    }
}