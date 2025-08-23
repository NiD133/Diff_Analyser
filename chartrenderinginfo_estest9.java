package org.jfree.chart;

import org.junit.Test;
import java.awt.geom.Rectangle2D;

/**
 * Unit tests for the {@link ChartRenderingInfo} class, focusing on method argument validation.
 */
public class ChartRenderingInfoTest {

    /**
     * Verifies that the setChartArea() method rejects a null argument by throwing a
     * NullPointerException. This test documents the expected behavior that the chart
     * area cannot be set to null.
     */
    @Test(expected = NullPointerException.class)
    public void setChartArea_withNullArgument_shouldThrowNullPointerException() {
        // Arrange: Create a new ChartRenderingInfo instance.
        ChartRenderingInfo info = new ChartRenderingInfo();

        // Act: Attempt to set the chart area to null.
        info.setChartArea(null);

        // Assert: The test expects a NullPointerException, which is declared
        // in the @Test annotation. If no exception is thrown, the test will fail.
    }
}