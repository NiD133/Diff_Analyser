package org.jfree.chart.block;

import org.jfree.data.Range;
import org.junit.Test;

import java.awt.Graphics2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link GridArrangement} class.
 */
public class GridArrangementTest {

    /**
     * Verifies that the arrange method throws an IllegalArgumentException when
     * provided with a RectangleConstraint that has a negative width.
     * A negative width causes an invalid internal calculation for cell sizes,
     * leading to an attempt to create a Range with a lower bound greater than
     * its upper bound.
     */
    @Test
    public void arrangeWithNegativeWidthConstraintShouldThrowException() {
        // Arrange: Create a grid arrangement, a container with one block, and a
        // constraint with a negative width.
        GridArrangement arrangement = new GridArrangement(10, 10);
        BlockContainer container = new BlockContainer();
        container.add(new EmptyBlock(0, 0)); // The container must not be empty.

        // A constraint with a negative width and no height restriction.
        RectangleConstraint negativeWidthConstraint = new RectangleConstraint(-100.0, null);

        // Act & Assert
        try {
            // The Graphics2D context can be null for this layout calculation.
            arrangement.arrange(container, null, negativeWidthConstraint);
            fail("Expected an IllegalArgumentException to be thrown due to the negative width constraint.");
        } catch (IllegalArgumentException e) {
            // The negative width (-100.0) divided by the number of columns (10)
            // results in a negative cell width (-10.0). This is then used to
            // construct an invalid Range, triggering this specific exception.
            String expectedMessage = "Range(double, double): require lower (0.0) <= upper (-10.0).";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}