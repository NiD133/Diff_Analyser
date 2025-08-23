package org.jfree.chart.title;

import org.junit.Test;
import java.awt.Graphics2D;
import org.jfree.chart.block.RectangleConstraint;

/**
 * Unit tests for the {@link ShortTextTitle} class, focusing on argument validation.
 */
public class ShortTextTitleTest {

    /**
     * Verifies that the arrange() method throws an IllegalArgumentException
     * when called with a null RectangleConstraint. The public contract of the
     * method requires a non-null constraint for layout calculations.
     */
    @Test(expected = IllegalArgumentException.class)
    public void arrangeShouldThrowIllegalArgumentExceptionForNullConstraint() {
        // Arrange: Create an instance of the class under test.
        ShortTextTitle title = new ShortTextTitle("Test Title");

        // Act: Call the method with a null constraint, which is expected to fail.
        // The Graphics2D argument is also null, matching the original test's scenario.
        title.arrange((Graphics2D) null, (RectangleConstraint) null);

        // Assert: The test passes if an IllegalArgumentException is thrown,
        // which is handled declaratively by the @Test(expected=...) annotation.
    }
}