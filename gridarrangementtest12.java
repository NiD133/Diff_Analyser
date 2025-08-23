package org.jfree.chart.block;

import org.jfree.chart.util.Size2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link GridArrangement} class.
 */
class GridArrangementTest {

    private static final double EPSILON = 0.000000001;

    /**
     * Verifies that the arrangement can handle a null block in the layout.
     * When a container with only a null block is arranged with a fixed width,
     * the resulting height should be zero.
     */
    @Test
    void arrangeShouldHandleNullBlockAndReturnZeroHeight() {
        // Arrange: Create a 1x1 grid arrangement and a container with one null block.
        GridArrangement arrangement = new GridArrangement(1, 1);
        BlockContainer container = new BlockContainer(arrangement);
        container.add(null);
        
        // Define a constraint with a fixed width.
        final double fixedWidth = 10.0;
        RectangleConstraint constraint = RectangleConstraint.NONE.toFixedWidth(fixedWidth);

        // Act: Arrange the container. The Graphics2D context is null as it's not needed for this calculation.
        Size2D arrangedSize = container.arrange(null, constraint);

        // Assert: The resulting size should have the fixed width and zero height.
        assertAll("Arranged size for a container with a single null block",
            () -> assertEquals(fixedWidth, arrangedSize.getWidth(), EPSILON,
                    "Width should match the fixed constraint width"),
            () -> assertEquals(0.0, arrangedSize.getHeight(), EPSILON,
                    "Height should be zero because the null block has no dimensions")
        );
    }
}