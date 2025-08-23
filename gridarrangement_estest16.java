package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.Graphics2D;
import org.jfree.data.Range;

/**
 * Tests for the {@link GridArrangement} class, focusing on specific arrangement scenarios.
 */
public class GridArrangement_ESTestTest16 extends GridArrangement_ESTest_scaffolding {

    /**
     * Verifies that arranging an empty container results in a size with zero height.
     * The width should match the width specified in the constraint, even when
     * directly calling the `arrangeNR` method, which is typically used for
     * unconstrained widths.
     */
    @Test
    public void arrangeNR_withEmptyContainer_returnsSizeWithZeroHeightAndConstraintWidth() {
        // Arrange
        final double fixedWidth = 3506.73;
        final double maxHeight = 45.54;
        
        GridArrangement gridArrangement = new GridArrangement(30, 1328);
        BlockContainer emptyContainer = new BlockContainer();

        // Create a constraint with a fixed width and a ranged height.
        // Note: We are directly calling arrangeNR, which is the protected helper method
        // for arranging with NO width constraint and a RANGED height constraint.
        // This test verifies its behavior when passed a constraint with a FIXED width.
        Range heightRange = new Range(0.0, maxHeight);
        RectangleConstraint constraint = new RectangleConstraint(fixedWidth, heightRange);

        // Act
        // The Graphics2D object is not used when the container is empty, so null is acceptable.
        Size2D resultingSize = gridArrangement.arrangeNR(emptyContainer, null, constraint);

        // Assert
        assertEquals("Width should match the fixed width from the constraint",
                fixedWidth, resultingSize.getWidth(), 0.01);
        assertEquals("Height should be zero for an empty container",
                0.0, resultingSize.getHeight(), 0.01);
    }
}