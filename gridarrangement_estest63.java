package org.jfree.chart.block;

import org.jfree.data.Range;
import org.junit.Test;

import java.awt.Graphics2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link GridArrangement} class, focusing on edge cases
 * and invalid inputs for arrangement logic.
 */
public class GridArrangementTest {

    /**
     * Verifies that the {@code arrangeFN} method throws an {@link IllegalArgumentException}
     * when provided with a constraint that has a negative fixed width.
     * <p>
     * The method is expected to fail because it attempts to calculate the width
     * for each grid cell by dividing the total width by the number of columns. A negative
     * total width results in a negative cell width. This value is then used to create a
     * {@link Range} object, which throws an exception because its lower bound (0.0)
     * cannot be greater than its upper bound.
     */
    @Test
    public void arrangeFN_withNegativeWidthConstraint_shouldThrowIllegalArgumentException() {
        // Arrange: Set up a grid, a container with one block, and a constraint with a negative width.
        GridArrangement gridArrangement = new GridArrangement(68, 68);
        BlockContainer container = new BlockContainer();
        
        // The container must not be empty for the arrangement logic to be triggered.
        // Adding the container to itself is a simple way to add a block.
        container.add(container);

        // Create a constraint with a fixed negative width and an unconstrained height.
        // The 'FN' in arrangeFN stands for Fixed-width and No-height-constraint.
        double negativeWidth = -374.0;
        RectangleConstraint negativeWidthConstraint = new RectangleConstraint(negativeWidth, null);

        // Act & Assert: Call the method and verify that the expected exception is thrown.
        try {
            // The Graphics2D context can be null as it's not used before the exception occurs.
            gridArrangement.arrangeFN(container, (Graphics2D) null, negativeWidthConstraint);
            fail("Expected an IllegalArgumentException to be thrown due to negative width.");
        } catch (IllegalArgumentException e) {
            // The exception originates from the Range class constructor.
            // The internal calculation is: width per column = -374.0 / 68 columns = -5.5.
            // The code then fails when trying to create new Range(0.0, -5.5).
            String expectedMessage = "Range(double, double): require lower (0.0) <= upper (-5.5).";
            assertEquals("The exception message should match the expected Range error.",
                    expectedMessage, e.getMessage());
        }
    }
}