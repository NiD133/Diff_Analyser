package org.jfree.chart.block;

import org.junit.Test;
import java.awt.Graphics2D;

/**
 * This test suite focuses on the GridArrangement class.
 * The original test was improved for clarity and maintainability.
 */
public class GridArrangement_ESTestTest65 extends GridArrangement_ESTest_scaffolding {

    /**
     * Verifies that calling arrangeFF with a null BlockContainer throws a NullPointerException.
     * The method is expected to fail early if the container is null, as it cannot
     * retrieve blocks to arrange from a null reference.
     */
    @Test(expected = NullPointerException.class)
    public void arrangeFFWithNullContainerShouldThrowNullPointerException() {
        // Arrange: Create a GridArrangement instance and a standard constraint.
        GridArrangement arrangement = new GridArrangement(2, 2);
        RectangleConstraint constraint = RectangleConstraint.NONE;

        // Act & Assert: Call the method with a null container.
        // The test will pass only if a NullPointerException is thrown,
        // as specified by the @Test annotation.
        arrangement.arrangeFF(null, (Graphics2D) null, constraint);
    }
}