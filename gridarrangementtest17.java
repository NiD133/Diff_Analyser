package org.jfree.chart.block;

import org.jfree.data.Range;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link GridArrangement} class, focusing on specific arrangement scenarios.
 */
public class GridArrangementTest {

    private static final double EPSILON = 0.000000001;

    /**
     * Verifies that when arranging with a fixed width and a ranged height,
     * the container expands to the maximum height of the range if the blocks
     * do not fill the entire grid. This tests the 'arrangeFR' (Fixed-Ranged)
     * behavior for a partially filled grid.
     */
    @Test
    public void arrangeWithFixedRangedHeightShouldUseUpperHeightBoundForPartiallyFilledGrid() {
        // Arrange: Set up a 2x3 grid but only add one small block, leaving it
        // mostly empty.
        GridArrangement arrangement = new GridArrangement(2, 3);
        BlockContainer container = new BlockContainer(arrangement);
        container.add(new EmptyBlock(5.0, 5.0));

        // Define a constraint with a fixed width and a height range.
        final double fixedWidth = 30.0;
        final double minHeight = 5.0;
        final double maxHeight = 10.0;
        RectangleConstraint constraint = new RectangleConstraint(
                fixedWidth, new Range(minHeight, maxHeight)
        );

        // Act: Arrange the container with the given constraint.
        // The Graphics2D parameter is null, as it's not needed for size calculation.
        Size2D arrangedSize = container.arrange(null, constraint);

        // Assert: The final size should respect the constraints.
        // The width should be the fixed width specified.
        assertEquals(fixedWidth, arrangedSize.getWidth(), EPSILON,
                "Width should match the fixed width constraint.");

        // The height should be the maximum height from the range, as the
        // arrangement logic expands to fill the available space.
        assertEquals(maxHeight, arrangedSize.getHeight(), EPSILON,
                "Height should expand to the maximum of the allowed range.");
    }
}