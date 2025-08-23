package org.jfree.chart.title;

import org.junit.Test;
import java.awt.Graphics2D;
import org.jfree.chart.block.RectangleConstraint;

/**
 * Unit tests for the {@link ShortTextTitle} class.
 */
public class ShortTextTitleTest {

    /**
     * Verifies that the arrange() method throws a NullPointerException
     * when called with a null Graphics2D context. The underlying implementation
     * requires a valid graphics context to measure font metrics.
     */
    @Test(expected = NullPointerException.class)
    public void arrange_withNullGraphics2D_shouldThrowNullPointerException() {
        // Arrange: Create a title instance and a dummy constraint.
        ShortTextTitle title = new ShortTextTitle("Test Title");
        RectangleConstraint constraint = new RectangleConstraint(100.0, 50.0);

        // Act & Assert: Call the method with a null Graphics2D object.
        // The @Test(expected=...) annotation handles the assertion,
        // ensuring a NullPointerException is thrown.
        title.arrange((Graphics2D) null, constraint);
    }
}