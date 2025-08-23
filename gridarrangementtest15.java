package org.jfree.chart.block;

import org.jfree.chart.util.Size2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link GridArrangement} class.
 */
class GridArrangementTest {

    private static final double DELTA = 1e-9;

    /**
     * Verifies that when arranging with a fixed-size constraint, the arrangement
     * reports the full size of the constraint, even if the grid is only
     * partially populated with blocks. This is the expected behavior for a
     * fixed-size layout.
     */
    @Test
    void arrangeWithFixedConstraintOnPartiallyFilledGridShouldReturnFullConstraintSize() {
        // Arrange
        final int gridRows = 2;
        final int gridCols = 3; // A 2x3 grid has 6 cells
        final double constraintWidth = 200.0;
        final double constraintHeight = 100.0;

        // Create a container with a 2x3 grid, but add only one small block.
        BlockContainer container = new BlockContainer(new GridArrangement(gridRows, gridCols));
        container.add(new EmptyBlock(5.0, 5.0));

        RectangleConstraint fixedSizeConstraint = new RectangleConstraint(constraintWidth, constraintHeight);

        // Act
        // Arrange the container within the fixed-size constraint.
        Size2D arrangedSize = container.arrange(null, fixedSizeConstraint);

        // Assert
        // The resulting size should match the constraint's dimensions, not the size
        // of the single block within the container.
        assertEquals(constraintWidth, arrangedSize.getWidth(), DELTA);
        assertEquals(constraintHeight, arrangedSize.getHeight(), DELTA);
    }
}