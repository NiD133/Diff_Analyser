package org.jfree.chart.title;

import org.jfree.chart.block.RectangleConstraint;
import org.jfree.data.Range;
import org.junit.Test;

import java.awt.Graphics2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link ShortTextTitle} class, focusing on exception handling.
 */
public class ShortTextTitleTest {

    /**
     * Verifies that the arrange() method throws a RuntimeException for a code path
     * that is marked as "Not yet implemented".
     *
     * This specific scenario is triggered by passing a null Graphics2D object and
     * a constraint with a fixed width and unconstrained height.
     */
    @Test
    public void arrange_WhenPathIsUnimplemented_ShouldThrowRuntimeException() {
        // Arrange
        ShortTextTitle title = new ShortTextTitle("Test Title");
        // A constraint with a fixed width and no height constraint.
        RectangleConstraint constraint = new RectangleConstraint(-1.0, (Range) null);
        Graphics2D g2 = null;

        // Act & Assert
        try {
            title.arrange(g2, constraint);
            fail("A RuntimeException was expected but not thrown.");
        } catch (RuntimeException e) {
            // Verify that the exception is the one we expect from an unimplemented method.
            assertEquals("Not yet implemented.", e.getMessage());
        }
    }
}