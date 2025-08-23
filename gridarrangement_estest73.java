package org.jfree.chart.block;

import org.junit.Test;
import java.awt.Graphics2D;
import org.jfree.data.Range;

/**
 * Tests for the {@link GridArrangement} class, focusing on exception handling.
 */
public class GridArrangement_ESTestTest73 extends GridArrangement_ESTest_scaffolding {

    /**
     * Verifies that the arrange() method throws a NullPointerException when
     * the Graphics2D context is null. The arrangement process requires a valid
     * graphics context to measure elements, so a null value is an illegal argument.
     */
    @Test(expected = NullPointerException.class)
    public void arrangeWithNullGraphicsShouldThrowException() {
        // Arrange: Set up a grid arrangement and a container for it.
        GridArrangement arrangement = new GridArrangement(0, 0);
        BlockContainer container = new BlockContainer(arrangement);
        RectangleConstraint constraint = new RectangleConstraint(0.0, (Range) null,
                LengthConstraintType.NONE, 0.0, (Range) null, LengthConstraintType.RANGE);

        // Act: Attempt to arrange the container with a null Graphics2D object.
        // Assert: The @Test(expected) annotation asserts that a NullPointerException is thrown.
        arrangement.arrange(container, (Graphics2D) null, constraint);
    }
}