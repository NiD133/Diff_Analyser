package org.jfree.chart.block;

import org.jfree.chart.api.Size2D;
import org.jfree.data.Range;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * A test suite for the GridArrangement class, focusing on specific arrangement scenarios.
 */
public class GridArrangementTest {

    /**
     * This test verifies the behavior of the protected method {@code arrangeFR} when
     * handling an empty container. Specifically, it checks that even when the
     * provided height constraint's *type* is {@code FIXED}, the method still uses
     * the upper bound of the constraint's height *range* to determine the resulting size.
     */
    @Test
    public void arrangeFR_withEmptyContainerAndInconsistentConstraint_usesHeightRangeUpperBound() {
        // Arrange
        final int rows = 4;
        final int columns = 3136;
        final double fixedWidth = 3136.0;
        final double expectedHeight = -59.797977; // The upper bound of the height range

        GridArrangement arrangement = new GridArrangement(rows, columns);
        BlockContainer emptyContainer = new BlockContainer(arrangement);

        Range heightRange = new Range(-1833.62112, expectedHeight);

        // Create a constraint where the height constraint *type* is FIXED,
        // but a height *range* is also supplied. This inconsistent state is used
        // to test the specific implementation of arrangeFR.
        RectangleConstraint inconsistentConstraint = new RectangleConstraint(
                fixedWidth, null, LengthConstraintType.FIXED,
                0.0, heightRange, LengthConstraintType.FIXED // Note: Type is FIXED
        );

        // Act
        // Directly call the protected method arrangeFR, which is designed for
        // Fixed-width, Ranged-height constraints.
        Size2D resultSize = arrangement.arrangeFR(emptyContainer, null, inconsistentConstraint);

        // Assert
        // Verify that the method returned the fixed width and the upper bound of the
        // height *range*, ignoring the FIXED height constraint type.
        assertEquals("Width should match the fixed width from the constraint",
                fixedWidth, resultSize.getWidth(), 0.01);
        assertEquals("Height should match the upper bound of the range, despite the constraint type being FIXED",
                expectedHeight, resultSize.getHeight(), 0.01);
    }
}