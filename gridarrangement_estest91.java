package org.jfree.chart.block;

import org.jfree.chart.ui.Size2D;
import org.jfree.data.Range;
import org.junit.Test;

import java.awt.Color;
import java.awt.Graphics2D;

import static org.junit.Assert.assertEquals;

/**
 * This test suite contains tests for the {@link GridArrangement} class.
 * This particular test was improved for understandability.
 */
public class GridArrangement_ESTestTest91 extends GridArrangement_ESTest_scaffolding {

    /**
     * Tests that the arrangeRF method returns a size that strictly adheres to the
     * provided constraint when that constraint specifies a fixed width and height.
     *
     * The method under test, `arrangeRF`, is designed for a width range and a
     * fixed height. This test verifies its behavior at the edge case where the
     * width "range" is a single fixed value.
     */
    @Test
    public void arrangeRFWithFixedSizeConstraintShouldReturnConstrainedSize() {
        // Arrange
        // Create a simple 1x1 grid arrangement. The number of rows/cols is not
        // critical for this test's assertion on the final container size.
        GridArrangement gridArrangement = new GridArrangement(1, 1);

        // Create a container and add a single block to it. The block's preferred
        // size is irrelevant as the fixed constraint should override it.
        BlockContainer container = new BlockContainer();
        container.add(new ColorBlock(Color.BLUE, 10.0, 10.0));

        // Define a constraint that fixes both width and height to a specific value.
        final double fixedDimension = 105.0;
        Range fixedSizeRange = new Range(fixedDimension, fixedDimension);
        RectangleConstraint constraint = new RectangleConstraint(fixedSizeRange, fixedSizeRange);

        // Act
        // Arrange the container. A null Graphics2D object is acceptable as it's
        // not used in this particular arrangement calculation.
        Size2D arrangedSize = gridArrangement.arrangeRF(container, (Graphics2D) null, constraint);

        // Assert
        // The resulting size should exactly match the dimensions from the constraint.
        assertEquals("Width should match the fixed constraint",
                fixedDimension, arrangedSize.getWidth(), 0.0);
        assertEquals("Height should match the fixed constraint",
                fixedDimension, arrangedSize.getHeight(), 0.0);
    }
}