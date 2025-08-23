package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link ShipNeedle} class, focusing on its initial state.
 * Note: The original test class name "MeterNeedle_ESTestTest5" is kept for context,
 * but a more descriptive name like "ShipNeedleTest" would be preferable.
 */
public class MeterNeedle_ESTestTest5 {

    /**
     * Verifies that a newly created ShipNeedle instance is initialized with the
     * correct default property values.
     */
    @Test
    public void shipNeedleShouldHaveDefaultPropertiesOnConstruction() {
        // Arrange: Create a new instance of ShipNeedle.
        ShipNeedle needle = new ShipNeedle();

        // Act: No action is needed as we are testing the state after construction.

        // Assert: Check that the default properties are set to their expected values.
        final double delta = 0.001;
        assertEquals("Default size should be 5", 5, needle.getSize());
        assertEquals("Default rotateX should be 0.5", 0.5, needle.getRotateX(), delta);
        assertEquals("Default rotateY should be 0.5", 0.5, needle.getRotateY(), delta);
    }
}