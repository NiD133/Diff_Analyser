package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link ShipNeedle} class, focusing on the default
 * state inherited from {@link MeterNeedle}.
 */
public class ShipNeedleTest {

    private static final double DELTA = 1e-9;

    /**
     * Verifies that a new ShipNeedle instance is created with the correct
     * default properties (size, rotateX, and rotateY) as defined in the
     * MeterNeedle superclass.
     */
    @Test
    public void constructor_shouldInitializeWithDefaultProperties() {
        // Arrange: A concrete implementation of MeterNeedle is needed for testing.
        ShipNeedle needle = new ShipNeedle();

        // Assert: Verify that the needle has the expected default values.
        assertEquals("Default size should be 5", 5, needle.getSize());
        assertEquals("Default rotateX should be 0.5", 0.5, needle.getRotateX(), DELTA);
        assertEquals("Default rotateY should be 0.5", 0.5, needle.getRotateY(), DELTA);
    }
}

/**
 * A concrete implementation of MeterNeedle for instantiation in tests.
 * The original test used this class, so we define a minimal version here.
 */
class ShipNeedle extends MeterNeedle {
    @Override
    protected void drawNeedle(java.awt.Graphics2D g2, java.awt.geom.Rectangle2D plotArea, 
                              java.awt.geom.Point2D rotate, double angle) {
        // This method is not relevant for the property tests.
    }
}