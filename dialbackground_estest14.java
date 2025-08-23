package org.jfree.chart.plot.dial;

import org.junit.Test;
import static org.junit.Assert.assertThrows;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * Tests for the {@link DialBackground} class, focusing on its drawing behavior.
 */
public class DialBackgroundTest {

    /**
     * Verifies that the draw() method throws a NullPointerException when the Graphics2D context is null.
     * A valid graphics context is essential for any drawing operation, so the method should fail fast
     * by throwing an exception.
     */
    @Test
    public void draw_WithNullGraphics2D_ShouldThrowNullPointerException() {
        // Arrange: Create a DialBackground instance and the necessary (but minimal) arguments for the draw method.
        DialBackground dialBackground = new DialBackground();
        DialPlot plot = new DialPlot(); // Required by method signature, but its state is not relevant here.
        Rectangle2D view = new Rectangle2D.Float(); // A non-null view rectangle is required by the method.

        // Act & Assert: Call the draw method with a null Graphics2D context and assert that it throws the expected exception.
        assertThrows(NullPointerException.class, () -> {
            dialBackground.draw(null, plot, null, view);
        });
    }
}