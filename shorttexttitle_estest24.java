package org.jfree.chart.title;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.RectangleConstraint;
import org.jfree.data.Range;
import org.junit.Test;

import java.awt.Graphics2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This is a test suite for the ShortTextTitle class.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class ShortTextTitle_ESTestTest24 extends ShortTextTitle_ESTest_scaffolding {

    /**
     * Verifies that the arrange() method throws a RuntimeException when called with a null Graphics2D context.
     *
     * The original test expected a "Not yet implemented." message, which suggests that
     * in the version of the library under test, this method's implementation was a placeholder.
     */
    @Test
    public void arrange_withNullGraphics2D_shouldThrowRuntimeException() {
        // Arrange: Create a title and a layout constraint. The specific values are not
        // critical for this test, as the null Graphics2D object is the trigger for the exception.
        ShortTextTitle title = new ShortTextTitle("Test Title");
        Range widthRange = ValueAxis.DEFAULT_RANGE;
        RectangleConstraint constraint = new RectangleConstraint(widthRange, 100.0);

        // Act & Assert: Call arrange() with a null Graphics2D and verify the expected exception.
        try {
            title.arrange(null, constraint);
            fail("Expected a RuntimeException to be thrown due to the null Graphics2D context.");
        } catch (RuntimeException e) {
            // Assert that the exception message matches the expected placeholder text.
            assertEquals("Not yet implemented.", e.getMessage());
        }
    }
}