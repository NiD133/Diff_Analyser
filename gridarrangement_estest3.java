package org.jfree.chart.block;

import org.jfree.chart.util.Size2D;
import org.jfree.data.Range;
import org.junit.Test;

import java.awt.Color;
import java.awt.Graphics2D;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link GridArrangement} class, focusing on specific arrangement scenarios.
 */
public class GridArrangementTest {

    /**
     * This test verifies the behavior of the protected method {@code arrangeFN}
     * (arrange with Fixed width, No height constraint).
     *
     * It specifically checks a scenario where the method is passed a
     * {@link RectangleConstraint} that has a 'RANGE' type for its width,
     * which is contrary to the method's 'FIXED' width assumption.
     *
     * The test expects {@code arrangeFN} to adapt by using the upper bound of the
     * width range as the fixed width. It also ensures the total height is
     * calculated as 0.0 when the container holds blocks with non-positive heights.
     */
    @Test
    public void arrangeFN_withRangeConstraint_shouldUseRangeUpperBoundAsWidth() {
        // Arrange
        final int gridRows = 105;
        final int gridCols = 105;
        final double expectedWidth = 105.0;

        GridArrangement gridArrangement = new GridArrangement(gridRows, gridCols);

        // A container with a single block that has a negative height.
        BlockContainer container = new BlockContainer();
        Block blockWithNegativeHeight = new ColorBlock(Color.BLACK, 0.0, Double.NEGATIVE_INFINITY);
        container.add(blockWithNegativeHeight);

        // Create a constraint where the width is defined by a Range. The arrangeFN
        // method is expected to treat the range's upper bound as the fixed width.
        // The height constraint is irrelevant for arrangeFN but is included to
        // match the original test's input.
        Range widthAndHeightRange = new Range(expectedWidth, expectedWidth);
        RectangleConstraint constraint = new RectangleConstraint(widthAndHeightRange, widthAndHeightRange);

        // Act
        // We are directly testing the protected method arrangeFN.
        Size2D arrangedSize = gridArrangement.arrangeFN(container, (Graphics2D) null, constraint);

        // Assert
        assertEquals("Width should be constrained by the upper bound of the range",
                expectedWidth, arrangedSize.width, 0.01);
        assertEquals("Height should be 0 for blocks with negative height",
                0.0, arrangedSize.height, 0.01);
    }
}