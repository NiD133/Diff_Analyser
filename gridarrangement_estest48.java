package org.jfree.chart.block;

import org.junit.Test;
import java.awt.Graphics2D;

/**
 * Unit tests for the {@link GridArrangement} class.
 */
public class GridArrangementTest {

    /**
     * Verifies that the arrangeNR method throws a NullPointerException if the
     * Graphics2D argument is null. The internal logic of the method requires a
     * non-null graphics context to perform calculations.
     */
    @Test(expected = NullPointerException.class)
    public void arrangeNRShouldThrowNullPointerExceptionForNullGraphics2D() {
        // Arrange: Create a GridArrangement and its required dependencies.
        // The negative row/column values are unusual but preserved from the original
        // test case. The NullPointerException is triggered by the null Graphics2D
        // object, not these values.
        GridArrangement arrangement = new GridArrangement(-2760, -2760);
        BlockContainer container = new BlockContainer();
        RectangleConstraint constraint = RectangleConstraint.NONE;
        Graphics2D g2 = null;

        // Act & Assert: Call the method under test with a null Graphics2D object.
        // The @Test(expected=...) annotation asserts that a NullPointerException is thrown.
        arrangement.arrangeNR(container, g2, constraint);
    }
}