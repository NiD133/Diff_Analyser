package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link MeterNeedle} class, focusing on its properties.
 */
public class MeterNeedleTest {

    /**
     * Verifies that the setRotateX() method correctly updates the rotateX property.
     */
    @Test
    public void setRotateX_shouldUpdateRotateXValue() {
        // Arrange
        // Using PointerNeedle as a concrete implementation of MeterNeedle
        MeterNeedle needle = new PointerNeedle();
        double expectedRotateX = -3074.422;

        // Act
        needle.setRotateX(expectedRotateX);
        double actualRotateX = needle.getRotateX();

        // Assert
        assertEquals("The rotateX value should be updated after calling the setter.",
                expectedRotateX, actualRotateX, 0.01);
    }
}