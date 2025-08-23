package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.Graphics2D;
import org.jfree.chart.util.Size2D;
import org.jfree.data.Range;

/**
 * Contains tests for the {@link GridArrangement} class, focusing on its arrangement logic.
 */
public class GridArrangementTest {

    /**
     * Tests that the arrangeFR method, when called with an empty container,
     * returns a size that matches the given constraints. 'FR' stands for
     * Fixed-width and Ranged-height.
     */
    @Test
    public void arrangeFR_withEmptyContainer_shouldReturnSizeMatchingConstraints() {
        // Arrange
        // Create a grid arrangement. The row/column count is irrelevant for an empty container.
        GridArrangement gridArrangement = new GridArrangement(65, 65);
        BlockContainer emptyContainer = new BlockContainer();

        // Define a constraint with a fixed width and a ranged height.
        // The original test used a negative width; we preserve this to maintain the test's
        // original intent, which is to verify that the constraint's width is returned.
        final double expectedWidth = -555.02928076;
        final double expectedHeight = 65.0;
        Range heightRange = new Range(expectedHeight, expectedHeight);
        RectangleConstraint constraint = new RectangleConstraint(expectedWidth, heightRange);

        // Act
        // Arrange the empty container. The Graphics2D context is not needed for this calculation.
        Size2D resultSize = gridArrangement.arrangeFR(emptyContainer, (Graphics2D) null, constraint);

        // Assert
        // The resulting size should directly reflect the provided constraints,
        // as there are no blocks in the container to arrange.
        assertNotNull("The resulting size should not be null.", resultSize);
        assertEquals("Width should match the fixed width from the constraint.",
                expectedWidth, resultSize.getWidth(), 0.01);
        assertEquals("Height should match the upper bound of the height range from the constraint.",
                expectedHeight, resultSize.getHeight(), 0.01);
    }
}