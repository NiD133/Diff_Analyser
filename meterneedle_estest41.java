package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Contains tests for the {@link MeterNeedle} class and its subclasses.
 * This specific test focuses on the behavior of the PinNeedle subclass.
 */
public class MeterNeedle_ESTestTest41 extends MeterNeedle_ESTest_scaffolding {

    /**
     * Verifies that calling the draw() method on a default PinNeedle instance
     * does not alter its properties. This ensures the draw method is free of
     * side effects on the needle's configuration.
     */
    @Test
    public void drawOnDefaultNeedleShouldNotAlterProperties() {
        // Arrange: Create a PinNeedle with default properties and a mock drawing environment.
        PinNeedle pinNeedle = new PinNeedle();
        Rectangle2D plotArea = new Rectangle2D.Double(0, 0, 100, 100);
        
        // A BufferedImage is used to create a valid Graphics2D object for the draw method.
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Act: Call the draw method.
        pinNeedle.draw(g2d, plotArea);

        // Assert: Verify that the needle's properties remain unchanged from their defaults.
        final double delta = 0.0; // Using 0.0 for exact comparison where appropriate.
        assertEquals("Default rotateX should be 0.5", 0.5, pinNeedle.getRotateX(), delta);
        assertEquals("Default rotateY should be 0.5", 0.5, pinNeedle.getRotateY(), delta);
        assertEquals("Default size should be 5", 5, pinNeedle.getSize());
        
        // Clean up graphics resources.
        g2d.dispose();
    }
}