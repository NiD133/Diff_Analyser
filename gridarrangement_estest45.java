package org.jfree.chart.block;

import org.junit.Test;
import java.awt.Graphics2D;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.block.GridArrangement;
import org.jfree.chart.block.RectangleConstraint;
import org.jfree.data.Range;

/**
 * This class contains tests for the GridArrangement class.
 * This particular test was improved for clarity and maintainability.
 */
public class GridArrangement_ESTestTest45 { // Retaining original class name

    /**
     * Verifies that the arrangeNR() method throws a NullPointerException when
     * called with a null Graphics2D object. The arrangeNR method is protected,
     * so this test must reside in the same package to be valid.
     */
    @Test(expected = NullPointerException.class)
    public void arrangeNRShouldThrowNullPointerExceptionForNullGraphics() {
        // Arrange: Create a grid arrangement, a container, and a constraint.
        // The specific dimensions and ranges are not critical for this test.
        GridArrangement gridArrangement = new GridArrangement(2, 2);
        BlockContainer container = new BlockContainer();

        // The arrangeNR method expects a constraint with no width limit (N)
        // and a ranged height limit (R).
        RectangleConstraint constraint = RectangleConstraint.NONE.toRangeHeight(new Range(50.0, 100.0));

        // Act: Call the method under test with a null Graphics2D context.
        // This is expected to cause a NullPointerException.
        gridArrangement.arrangeNR(container, null, constraint);

        // Assert: The test will pass if a NullPointerException is thrown,
        // as specified by the @Test(expected=...) annotation.
    }
}