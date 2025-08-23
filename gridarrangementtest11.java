package org.jfree.chart.block;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link GridArrangement} class.
 */
public class GridArrangementTest {

    private static final double EPSILON = 0.000000001;

    /**
     * Verifies that arranging a container with a null block and a fixed-size
     * constraint results in a size that matches the constraint. This ensures the
     * arrangement gracefully handles empty or null cells in the grid.
     */
    @Test
    public void arrangeWithNullBlockAndFixedConstraintShouldReturnConstraintSize() {
        // Arrange: Create a 1x1 grid container and add a null block.
        GridArrangement arrangement = new GridArrangement(1, 1);
        BlockContainer container = new BlockContainer(arrangement);
        container.add(null); // The key element being tested.

        // Define a fixed-size constraint for the arrangement.
        RectangleConstraint fixedConstraint = new RectangleConstraint(20.0, 10.0);

        // Act: Arrange the container. The Graphics2D context is null as it's not
        // needed for this size calculation.
        Size2D arrangedSize = container.arrange(null, fixedConstraint);

        // Assert: The resulting size should be equal to the fixed constraint dimensions.
        assertAll("Arranged size with null block and fixed constraint",
            () -> assertEquals(20.0, arrangedSize.getWidth(), EPSILON,
                    "The width should match the fixed constraint width."),
            () -> assertEquals(10.0, arrangedSize.getHeight(), EPSILON,
                    "The height should match the fixed constraint height.")
        );
    }
}