package org.jfree.chart.block;

import org.junit.Test;
import java.awt.Graphics2D;

/**
 * A test suite for the {@link GridArrangement} class, focusing on its behavior
 * under exceptional or edge-case conditions.
 */
public class GridArrangementTest {

    /**
     * Verifies that attempting to arrange a grid with excessively large dimensions
     * throws an {@link OutOfMemoryError}.
     *
     * This test ensures that the arrangement logic fails predictably when faced
     * with resource constraints that are impossible to meet, rather than hanging
     * or causing other undefined behavior.
     */
    @Test(expected = OutOfMemoryError.class, timeout = 4000)
    public void arrangeFFWithExcessivelyLargeGridShouldThrowOutOfMemoryError() {
        // Arrange: Create a grid arrangement with dimensions so large that they
        // are certain to exhaust available heap memory during allocation.
        final int excessivelyLargeDimension = 3_000_000;
        GridArrangement gridArrangement = new GridArrangement(
                excessivelyLargeDimension, excessivelyLargeDimension);

        BlockContainer container = new BlockContainer();
        RectangleConstraint constraint = RectangleConstraint.NONE;

        // Act & Assert: Attempt to arrange the container. This call is expected
        // to throw an OutOfMemoryError when the method tries to allocate internal
        // data structures (like arrays) for the massive grid.
        // The Graphics2D context is passed as null, as the memory error should
        // occur before it is ever used.
        gridArrangement.arrangeFF(container, (Graphics2D) null, constraint);
    }
}