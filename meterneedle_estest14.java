package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link MeterNeedle} class, using the concrete 
 * {@link WindNeedle} subclass for instantiation.
 */
public class MeterNeedleTest {

    /**
     * Verifies that the default constructor initializes the needle with the
     * correct default property values.
     */
    @Test
    public void testDefaultConstructor_shouldSetDefaultValues() {
        // Arrange: Create a new WindNeedle instance. Since MeterNeedle is abstract,
        // we use a concrete subclass to test the base functionality.
        WindNeedle needle = new WindNeedle();

        // Act: No action is needed as we are testing the state after construction.

        // Assert: Check that the properties are set to their expected default values.
        assertEquals("Default size should be 5", 5, needle.getSize());
        assertEquals("Default rotateX should be 0.5", 0.5, needle.getRotateX(), 0.01);
        assertEquals("Default rotateY should be 0.5", 0.5, needle.getRotateY(), 0.01);
    }

    /**
     * Tests the reflexive property of the equals() method, ensuring that an
     * object is always equal to itself.
     */
    @Test
    public void testEquals_withSameInstance_shouldReturnTrue() {
        // Arrange
        WindNeedle needle = new WindNeedle();

        // Act & Assert: An object must be equal to itself.
        assertTrue("An object should be equal to itself.", needle.equals(needle));
    }
}