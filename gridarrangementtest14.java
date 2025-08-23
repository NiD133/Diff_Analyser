package org.jfree.chart.block;

import org.jfree.chart.util.Size2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link GridArrangement} class, focusing on edge cases.
 */
class GridArrangementTest {

    private static final double DELTA = 1e-9;

    /**
     * Verifies that the arrangement correctly handles a null block. When a
     * container with a null block is arranged, the resulting size should be zero.
     */
    @Test
    void arrangeShouldReturnZeroSizeWhenContainerHasNullBlock() {
        // Arrange: Create a container with a 1x1 grid and add a null block.
        BlockContainer container = new BlockContainer(new GridArrangement(1, 1));
        container.add(null);

        // Act: Arrange the container with no constraints.
        // The Graphics2D context is null as no actual drawing is performed.
        Size2D arrangedSize = container.arrange(null, RectangleConstraint.NONE);

        // Assert: The resulting size should have zero width and height.
        assertEquals(0.0, arrangedSize.getWidth(), DELTA, "Width should be zero for a null block");
        assertEquals(0.0, arrangedSize.getHeight(), DELTA, "Height should be zero for a null block");
    }
}