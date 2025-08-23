package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link MeterNeedle} class.
 * <p>
 * Since {@code MeterNeedle} is an abstract class, a concrete subclass,
 * {@link PointerNeedle}, is instantiated to test the non-abstract functionality.
 */
public class MeterNeedleTest {

    /**
     * Verifies that the default constructor correctly initializes the needle's
     * size and rotation point coordinates to their default values.
     */
    @Test
    public void testDefaultConstructorInitializesProperties() {
        // Arrange: MeterNeedle is abstract, so we instantiate a concrete subclass.
        MeterNeedle needle = new PointerNeedle();

        // Assert: Check that the properties have their expected default values.
        final double expectedRotationCenter = 0.5;
        final int expectedSize = 5;
        final double delta = 0.0; // Use a delta of 0.0 for exact floating-point comparisons.

        assertEquals("Default rotation X-coordinate should be 0.5.",
                expectedRotationCenter, needle.getRotateX(), delta);

        assertEquals("Default rotation Y-coordinate should be 0.5.",
                expectedRotationCenter, needle.getRotateY(), delta);

        assertEquals("Default size should be 5.",
                expectedSize, needle.getSize());
    }
}