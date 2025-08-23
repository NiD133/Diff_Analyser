package org.jfree.chart;

import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

/**
 * Tests for the {@link ChartRenderingInfo} class.
 */
public class ChartRenderingInfoTest {

    /**
     * Verifies that the hashCode() method's result is dependent on the chartArea property.
     * When the chartArea is changed, the hash code should also change.
     */
    @Test
    public void hashCode_shouldChangeWhenChartAreaIsSet() {
        // Arrange: Create a ChartRenderingInfo instance and get its initial hash code.
        ChartRenderingInfo info = new ChartRenderingInfo();
        int initialHashCode = info.hashCode();

        // Act: Set a new chart area on the instance.
        Rectangle2D newChartArea = new Rectangle(10, 20, 30, 40);
        info.setChartArea(newChartArea);
        int updatedHashCode = info.hashCode();

        // Assert: The new hash code should be different from the initial one.
        assertNotEquals("Setting the chart area should change the hash code.", initialHashCode, updatedHashCode);
    }
}