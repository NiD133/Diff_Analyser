package org.jfree.chart.plot.dial;

import org.junit.Test;
import java.awt.geom.Rectangle2D;

/**
 * Unit tests for the {@link DialBackground} class.
 */
public class DialBackgroundTest {

    /**
     * Verifies that the draw() method throws a NullPointerException if the
     * Graphics2D context is null. A valid graphics context is essential for
     * any drawing operation, and the method should fail fast in its absence.
     */
    @Test(expected = NullPointerException.class)
    public void drawWithNullGraphics2DShouldThrowNullPointerException() {
        // Arrange: Create instances needed for the test
        DialBackground dialBackground = new DialBackground();
        DialPlot plot = new DialPlot();
        Rectangle2D.Double frame = new Rectangle2D.Double(0, 0, 100, 100);
        Rectangle2D.Double view = new Rectangle2D.Double(0, 0, 100, 100);

        // Act: Call the draw method with a null Graphics2D context.
        // The @Test(expected=...) annotation asserts that a NullPointerException is thrown.
        dialBackground.draw(null, plot, frame, view);
    }
}