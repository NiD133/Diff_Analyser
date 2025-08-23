package org.jfree.chart.block;

import org.junit.Test;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * Unit tests for the {@link LabelBlock} class.
 */
public class LabelBlockTest {

    /**
     * Verifies that the draw() method throws a NullPointerException when the 
     * Graphics2D context is null. Drawing operations cannot proceed without a 
     * valid graphics context.
     */
    @Test(expected = NullPointerException.class)
    public void draw_withNullGraphics2D_shouldThrowNullPointerException() {
        // Arrange: Create a LabelBlock instance and a drawing area.
        LabelBlock labelBlock = new LabelBlock("Test Label");
        Rectangle2D drawArea = new Rectangle2D.Float();
        
        // Act: Attempt to draw the block with a null Graphics2D object.
        // The 'params' argument is ignored by this method and can be null.
        labelBlock.draw(null, drawArea, null);

        // Assert: The test passes if a NullPointerException is thrown, as
        // specified by the @Test(expected=...) annotation.
    }
}