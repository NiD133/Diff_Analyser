package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

// Note: Unused imports from the original test have been removed to reduce clutter.

/**
 * Tests for the {@link LongNeedle} class, a concrete implementation of {@link MeterNeedle}.
 */
public class MeterNeedle_ESTestTest4 { // In a real project, this file would be renamed to LongNeedleTest.java

    /**
     * Verifies that a newly created LongNeedle instance is initialized with the
     * correct default property values.
     */
    @Test
    public void constructor_shouldSetDefaultProperties() {
        // Arrange: Create a new instance of LongNeedle.
        // The LongNeedle class is a specific type of MeterNeedle.
        LongNeedle needle = new LongNeedle();

        // Act: No action is performed because we are testing the initial state
        // of the object immediately after construction.

        // Assert: Verify that the default properties are set as expected.
        // These values appear to be the specified defaults for the LongNeedle class.
        assertEquals("Default size should be 5", 5, needle.getSize());
        assertEquals("Default X rotation point should be 0.5", 0.5, needle.getRotateX(), 0.01);
        assertEquals("Default Y rotation point should be 0.8", 0.8, needle.getRotateY(), 0.01);
    }
}