package org.jfree.chart.block;

import org.jfree.chart.api.Size2D;
import org.junit.Test;

import java.awt.Color;
import java.awt.Graphics2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

// Note: The original test class name "GridArrangement_ESTestTest8" is kept,
// but in a real-world scenario, it would be renamed to "GridArrangementTest".
public class GridArrangement_ESTestTest8 {

    /**
     * Verifies that the arrange method correctly calculates the total size for a grid
     * with no constraints. The total size should be based on the grid dimensions
     * and the maximum dimensions of the blocks within the container. This test also
     * covers the edge case of a block with a negative width.
     */
    @Test(timeout = 4000)
    public void arrangeWithNoConstraintShouldCalculateSizeFromGridAndBlockDimensions() {
        // Arrange
        int rows = 59;
        int columns = 59;
        GridArrangement arrangement = new GridArrangement(rows, columns);

        // Create a container and add two blocks to it.
        // The arrangement logic will be based on the largest block dimensions.
        BlockContainer container = new BlockContainer();
        
        // Add a block with a negative width and a positive height.
        double blockWidth = -1.0;
        double blockHeight = 306.1;
        ColorBlock colorBlock = new ColorBlock(Color.RED, blockWidth, blockHeight);
        container.add(colorBlock);

        // Add the container to itself, which acts as a second block of size (0,0).
        // This ensures the arrangement handles multiple blocks.
        container.add(container);

        // Act
        // Arrange the container's blocks in the grid without any size constraints.
        // The Graphics2D object is not used in this arrangement scenario, so it can be null.
        Size2D resultingSize = arrangement.arrange(container, null, RectangleConstraint.NONE);

        // Assert
        assertNotNull(resultingSize);

        // The expected width is based on the maximum width of any block in the container.
        // The blocks have widths of -1.0 and 0.0. The arrangement logic treats
        // negative widths as 0, so the maximum width is 0.
        // Total width = columns * max_block_width = 59 * 0.0 = 0.0
        double expectedWidth = 0.0;

        // The expected height is based on the maximum height of any block.
        // The blocks have heights of 306.1 and 0.0, so the maximum is 306.1.
        // Total height = rows * max_block_height = 59 * 306.1 = 18059.9
        double expectedHeight = rows * blockHeight;

        assertEquals("The total width should be zero when the widest block has a non-positive width.",
                expectedWidth, resultingSize.getWidth(), 0.01);
        assertEquals("The total height should be the number of rows multiplied by the tallest block's height.",
                expectedHeight, resultingSize.getHeight(), 0.01);
    }
}