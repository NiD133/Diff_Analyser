package org.jfree.chart.block;

import org.jfree.data.Range;
import org.jfree.ui.Size2D;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link GridArrangement} class.
 */
class GridArrangementTest {

    private static final double DELTA = 1e-9;

    /**
     * Verifies that the arrangement logic correctly handles a null block.
     * When a container with a null block is arranged with a fixed-width and
     * ranged-height constraint, the resulting size should respect the constraint.
     * Specifically, the height should be the lower bound of the range, as there
     * is no content to expand it.
     */
    @Test
    @DisplayName("Arrange should handle a null block with a fixed-width, ranged-height constraint")
    void arrangeWithNullBlockShouldReturnSizeBasedOnConstraint() {
        // Arrange: Create a 1x1 grid arrangement and a container with one null block.
        GridArrangement arrangement = new GridArrangement(1, 1);
        BlockContainer container = new BlockContainer(arrangement);
        container.add(null);

        // Define a constraint with a fixed width and a ranged height.
        double fixedWidth = 30.0;
        Range heightRange = new Range(5.0, 10.0);
        RectangleConstraint constraint = new RectangleConstraint(fixedWidth, heightRange);

        // Act: Arrange the container. The Graphics2D context is not needed for this calculation.
        Size2D actualSize = container.arrange(null, constraint);

        // Assert: The resulting size should match the fixed width and the minimum height from the range.
        double expectedWidth = fixedWidth;
        double expectedHeight = heightRange.getLowerBound();

        assertEquals(expectedWidth, actualSize.getWidth(), DELTA,
                "Width should match the fixed width from the constraint.");
        assertEquals(expectedHeight, actualSize.getHeight(), DELTA,
                "Height should be the lower bound of the range when the block is null.");
    }
}