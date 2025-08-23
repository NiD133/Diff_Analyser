package org.jfree.chart.block;

import org.jfree.chart.TestUtils;
import org.jfree.data.Range;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link GridArrangement} class, focusing on specific constraint scenarios.
 */
public class GridArrangementTest {

    private static final double EPSILON = 0.000000001;

    // The expected height is determined by the tallest block in the single-row grid.
    private static final double MAX_BLOCK_HEIGHT = 33.0;

    private BlockContainer container;

    /**
     * Creates a container with a 1x3 grid arrangement. The blocks in the grid
     * have varying heights, with the tallest being MAX_BLOCK_HEIGHT.
     */
    @BeforeEach
    public void setUp() {
        Block block1 = new EmptyBlock(10.0, 11.0);
        Block block2 = new EmptyBlock(20.0, 22.0);
        Block blockWithMaxHeight = new EmptyBlock(30.0, MAX_BLOCK_HEIGHT);

        // The container is arranged as a single row with three columns.
        GridArrangement arrangement = new GridArrangement(1, 3);
        this.container = new BlockContainer(arrangement);
        this.container.add(block1);
        this.container.add(block2);
        this.container.add(blockWithMaxHeight);
    }

    /**
     * Verifies that when arranging with a fixed width and no height constraint,
     * the resulting width matches the fixed value, and the height is determined
     * by the tallest block in the grid.
     */
    @Test
    public void testArrangeWithFixedWidthAndNoHeightConstraint() {
        // Arrange
        final double fixedWidth = 100.0;
        RectangleConstraint constraint = new RectangleConstraint(
            fixedWidth, null, LengthConstraintType.FIXED,
            0.0, null, LengthConstraintType.NONE
        );

        // Act
        Size2D arrangedSize = this.container.arrange(null, constraint);

        // Assert
        assertEquals(fixedWidth, arrangedSize.width, EPSILON,
                "Width should be constrained to the fixed value provided.");
        assertEquals(MAX_BLOCK_HEIGHT, arrangedSize.height, EPSILON,
                "With no height constraint, height should be the maximum height of all blocks in the row.");
    }
}