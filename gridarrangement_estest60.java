package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.Graphics2D;

/**
 * Tests for the {@link GridArrangement} class, focusing on its handling of
 * various layout constraints.
 */
public class GridArrangementTest {

    /**
     * Verifies that the arrange() method throws a RuntimeException when given a
     * fixed-width constraint that does not match the container's natural width.
     *
     * This scenario is handled by the internal `arrangeFN` method, which has a
     * code path that is explicitly not implemented for resizing blocks to fit
     * a fixed width. This test confirms that the unimplemented path is guarded
     * by an appropriate exception.
     */
    @Test
    public void arrangeWithMismatchedFixedWidthConstraintThrowsException() {
        // Arrange: Set up a grid arrangement and a container with a single block
        // of a known size. This gives the container a predictable "natural" size.
        GridArrangement arrangement = new GridArrangement(1, 1);
        BlockContainer container = new BlockContainer(arrangement);
        
        // The natural width of the container will be 20.0 due to this block.
        container.add(new EmptyBlock(20.0, 20.0));

        // Create a constraint with a fixed width (10.0) that differs from the
        // container's natural width (20.0). The height constraint is NONE.
        // This specific combination triggers the unimplemented logic.
        RectangleConstraint constraint = new RectangleConstraint(
            10.0, null, LengthConstraintType.FIXED,
            0.0, null, LengthConstraintType.NONE);
        
        // The Graphics2D object is not used in this specific code path.
        Graphics2D g2 = null;

        // Act & Assert: We expect a RuntimeException because the logic to handle
        // this resizing case is not implemented.
        try {
            arrangement.arrange(container, g2, constraint);
            fail("A RuntimeException was expected but not thrown.");
        } catch (RuntimeException e) {
            assertEquals("The exception message should indicate an unimplemented feature.",
                         "Not implemented.", e.getMessage());
        }
    }
}