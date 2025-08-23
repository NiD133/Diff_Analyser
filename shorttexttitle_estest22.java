package org.jfree.chart.title;

import org.jfree.chart.block.RectangleConstraint;
import org.junit.Test;

import java.awt.Graphics2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link ShortTextTitle} class.
 */
public class ShortTextTitleTest {

    /**
     * This test verifies that the arrange() method throws a RuntimeException when
     * passed a null Graphics2D object.
     *
     * The expected exception message "Not yet implemented" suggests this test was
     * originally generated against a stub or incomplete implementation of the method.
     * This test preserves that original behavior.
     */
    @Test
    public void arrange_withNullGraphics2D_shouldThrowRuntimeException() {
        // Arrange
        ShortTextTitle title = new ShortTextTitle("Test Title");
        RectangleConstraint constraint = new RectangleConstraint(0.0, 0.0);
        Graphics2D nullGraphics = null;

        // Act & Assert
        try {
            title.arrange(nullGraphics, constraint);
            fail("A RuntimeException was expected, but it was not thrown.");
        } catch (RuntimeException e) {
            // Verify that the exception has the expected message.
            assertEquals("Not yet implemented.", e.getMessage());
        }
    }
}