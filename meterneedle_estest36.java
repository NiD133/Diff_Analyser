package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the default state of the {@link MeterNeedle} class, using a
 * concrete {@link ShipNeedle} implementation.
 */
public class MeterNeedleDefaultsTest {

    /**
     * Verifies that the default constructor of a MeterNeedle subclass
     * (ShipNeedle) correctly initializes its properties to the expected
     * default values.
     */
    @Test
    public void testDefaultConstructorInitializesPropertiesCorrectly() {
        // Arrange: Create a new ShipNeedle instance. Since MeterNeedle is abstract,
        // we test its default state through a concrete subclass.
        ShipNeedle needle = new ShipNeedle();

        // Act: No action is required as we are testing the state immediately
        // after construction.

        // Assert: Verify that the properties have their expected default values.
        final double delta = 0.0; // Using 0.0 delta as values should be exact.
        
        assertEquals("Default size should be 5.", 5, needle.getSize());
        assertEquals("Default rotateX should be 0.5.", 0.5, needle.getRotateX(), delta);
        assertEquals("Default rotateY should be 0.5.", 0.5, needle.getRotateY(), delta);
    }
}