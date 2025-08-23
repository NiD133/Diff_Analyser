package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * A collection of tests for the {@link MeterNeedle} class.
 * Since {@code MeterNeedle} is abstract, the concrete subclass {@link WindNeedle}
 * is used for instantiation to test the default constructor behavior.
 */
public class MeterNeedleTest {

    /**
     * Verifies that a MeterNeedle instance created with the default constructor
     * is initialized with the expected default property values.
     */
    @Test
    public void defaultConstructorShouldInitializePropertiesCorrectly() {
        // Arrange: Create a concrete instance of MeterNeedle.
        MeterNeedle needle = new WindNeedle();

        // Act: No action is needed as we are testing the state after construction.

        // Assert: Check that the properties have their expected default values.
        assertNull("The default fill paint should be null.", needle.getFillPaint());
        assertEquals("The default size should be 5.", 5, needle.getSize());
        assertEquals("The default X rotation point should be 0.5.", 0.5, needle.getRotateX(), 0.01);
        assertEquals("The default Y rotation point should be 0.5.", 0.5, needle.getRotateY(), 0.01);
    }
}