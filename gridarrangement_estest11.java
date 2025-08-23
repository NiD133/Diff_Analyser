package org.jfree.chart.block;

import org.jfree.data.Range;
import org.junit.Test;

import java.awt.Graphics2D;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link GridArrangement} class.
 */
public class GridArrangementTest {

    private static final double DELTA = 0.01;

    /**
     * Tests that arranging an empty container with a range-based constraint
     * results in a size corresponding to the lower bound of the range.
     *
     * This tests the specific arrangeRR() protected method, which is called when
     * both width and height constraints are of type Range.
     */
    @Test
    public void arrangeWithRangeConstraintOnEmptyContainerShouldReturnSizeOfLowerBound() {
        // Arrange
        final double lowerBound = 527.2633130861806;
        final double upperBound = 1317.5620888757064;

        GridArrangement arrangement = new GridArrangement(15, 15);
        BlockContainer emptyContainer = new BlockContainer(arrangement);
        
        Range constraintRange = new Range(lowerBound, upperBound);
        RectangleConstraint constraint = new RectangleConstraint(constraintRange, constraintRange);

        // Act
        // The Graphics2D object is not used when arranging an empty container, so null is acceptable.
        Size2D resultSize = arrangement.arrange(emptyContainer, (Graphics2D) null, constraint);

        // Assert
        assertEquals("The resulting width should be the lower bound of the constraint range.",
                lowerBound, resultSize.getWidth(), DELTA);
        assertEquals("The resulting height should be the lower bound of the constraint range.",
                lowerBound, resultSize.getHeight(), DELTA);
    }
}