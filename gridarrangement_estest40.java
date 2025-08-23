package org.jfree.chart.block;

import org.junit.Test;
import java.awt.Graphics2D;

/**
 * Contains unit tests for the {@link GridArrangement} class.
 */
public class GridArrangementTest {

    /**
     * Verifies that the arrangeRN method throws a NullPointerException
     * when the Graphics2D context is null. This is expected behavior, as the
     * method requires a valid graphics context to perform calculations.
     */
    @Test(expected = NullPointerException.class)
    public void arrangeRNShouldThrowNullPointerExceptionForNullGraphics2D() {
        // Arrange: Set up the necessary objects for the test.
        GridArrangement arrangement = new GridArrangement(1, 1);
        BlockContainer container = new BlockContainer(arrangement);
        RectangleConstraint constraint = RectangleConstraint.NONE;

        // Act: Call the method under test with a null Graphics2D object.
        // The @Test(expected) annotation will assert that the correct exception is thrown.
        arrangement.arrangeRN(container, null, constraint);
    }
}