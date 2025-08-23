package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link MeterNeedle} class.
 */
public class MeterNeedleTest {

    /**
     * Verifies that the setRotateX() method correctly updates the rotateX value,
     * which is then returned by getRotateX().
     */
    @Test
    public void getRotateX_shouldReturnTheValuePreviouslySet() {
        // Arrange
        // ArrowNeedle is a concrete implementation of the abstract MeterNeedle class.
        ArrowNeedle needle = new ArrowNeedle(false);
        final double expectedRotateX = -9.0;

        // Act
        needle.setRotateX(expectedRotateX);
        final double actualRotateX = needle.getRotateX();

        // Assert
        assertEquals("The value returned by getRotateX() should match the value set.",
                expectedRotateX, actualRotateX, 0.01);
    }
}