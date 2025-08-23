package org.jfree.chart.title;

import org.jfree.chart.block.RectangleConstraint;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

/**
 * Unit tests for the {@link ShortTextTitle} class.
 */
public class ShortTextTitleTest {

    /**
     * Verifies that the arrange() method throws a NullPointerException when
     * the Graphics2D argument is null. The Graphics2D context is essential
     * for measuring text dimensions, so this is expected behavior.
     */
    @Test
    public void arrange_withNullGraphics2D_shouldThrowNullPointerException() {
        // Arrange: Create a title instance and a simple constraint.
        ShortTextTitle title = new ShortTextTitle("Test Title");
        RectangleConstraint constraint = RectangleConstraint.NONE;

        // Act & Assert: Expect a NullPointerException when calling arrange() with a null Graphics2D.
        assertThrows(NullPointerException.class, () -> {
            title.arrange(null, constraint);
        });
    }
}