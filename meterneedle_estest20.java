package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link MeterNeedle} class, using {@link PlumNeedle} as a concrete implementation.
 */
public class MeterNeedleTest {

    /**
     * Verifies that the default constructor of a MeterNeedle (via PlumNeedle)
     * initializes its properties to the expected default values.
     */
    @Test
    public void testDefaultConstructorInitializesProperties() {
        // Arrange: Create a new PlumNeedle instance. PlumNeedle is a concrete
        // subclass of the abstract MeterNeedle.
        PlumNeedle needle = new PlumNeedle();

        // Act: No action is needed as we are testing the state immediately
        // after construction.

        // Assert: Check that the needle's properties match the documented defaults.
        assertNull("Default highlight paint should be null.", needle.getHighlightPaint());
        assertEquals("Default size should be 5.", 5, needle.getSize());
        assertEquals("Default rotation X-coordinate should be 0.5.", 0.5, needle.getRotateX(), 0.01);
        assertEquals("Default rotation Y-coordinate should be 0.5.", 0.5, needle.getRotateY(), 0.01);
    }
}