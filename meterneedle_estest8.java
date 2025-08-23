package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link MeterNeedle} class, focusing on property accessors.
 */
public class MeterNeedleTest {

    /**
     * Verifies that the rotateY property can be set and retrieved correctly.
     */
    @Test
    public void shouldSetAndGetRotateY() {
        // Arrange: Create a PointerNeedle, a concrete implementation of MeterNeedle.
        // The default rotateY value is expected to be 0.5.
        PointerNeedle needle = new PointerNeedle();
        assertEquals("Default rotateY value should be 0.5", 0.5, needle.getRotateY(), 0.01);

        // Act: Set a new value for the rotateY property.
        double newRotateYValue = 0.0;
        needle.setRotateY(newRotateYValue);

        // Assert: The getter should now return the newly set value.
        assertEquals("After setting, getRotateY should return the new value", newRotateYValue, needle.getRotateY(), 0.01);
    }
}