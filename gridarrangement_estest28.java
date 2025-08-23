package org.jfree.chart.block;

import org.jfree.data.Range;
import org.junit.Test;
import java.awt.Graphics2D;
import static org.junit.Assert.assertEquals;

/**
 * Provides tests for the {@link GridArrangement} class, focusing on specific arrangement scenarios.
 */
public class GridArrangementTest {

    /**
     * Verifies that arrangeFN() correctly handles an empty container when given a constraint
     * with a negative fixed width.
     *
     * The expected behavior is that the method returns a Size2D object reflecting the
     * specified negative width and a calculated height of zero, as there are no blocks to arrange.
     */
    @Test
    public void arrangeFN_withEmptyContainerAndNegativeFixedWidth_returnsSizeWithNegativeWidthAndZeroHeight() {
        // Arrange
        // The row and column counts are irrelevant for an empty container but are required by the constructor.
        GridArrangement gridArrangement = new GridArrangement(-1, 1301);
        BlockContainer emptyContainer = new BlockContainer();

        // Define a constraint with a fixed negative width. The arrangeFN method is expected
        // to use this fixed width and calculate the height independently.
        final double fixedNegativeWidth = -1.0;
        RectangleConstraint constraint = new RectangleConstraint(fixedNegativeWidth, (Range) null);

        // Act
        // Arrange the empty container. The Graphics2D context is null as it's not
        // required for size calculation when the container has no content.
        Size2D arrangedSize = gridArrangement.arrangeFN(emptyContainer, (Graphics2D) null, constraint);

        // Assert
        // The resulting width should match the fixed negative width from the constraint.
        assertEquals("Width should match the fixed negative width in the constraint",
                fixedNegativeWidth, arrangedSize.getWidth(), 0.01);

        // Since the container is empty, the calculated height should be zero.
        assertEquals("Height should be zero for an empty container",
                0.0, arrangedSize.getHeight(), 0.01);
    }
}