package org.jfree.chart.block;

import org.junit.Test;

import java.awt.Graphics2D;

import static org.evosuite.runtime.EvoAssertions.verifyException;
import static org.junit.Assert.fail;

// The test class name is retained from the original, but a more descriptive name
// like "GridArrangementTest" would be preferable in a real-world scenario.
public class GridArrangement_ESTestTest36 extends GridArrangement_ESTest_scaffolding {

    /**
     * Verifies that the arrangeRR method throws a NullPointerException when
     * the Graphics2D context is null. The arrangeRR method is responsible for
     * arranging blocks within a container when both width and height are
     * constrained to a range.
     */
    @Test
    public void arrangeRR_withNullGraphics2D_shouldThrowNullPointerException() {
        // Arrange: Set up the objects required for the test.
        GridArrangement arrangement = new GridArrangement(0, 0);
        BlockContainer container = new BlockContainer(arrangement);
        RectangleConstraint constraint = RectangleConstraint.NONE;

        // Act & Assert: Call the method and verify that it throws the expected exception.
        try {
            arrangement.arrangeRR(container, (Graphics2D) null, constraint);
            fail("A NullPointerException was expected but not thrown.");
        } catch (NullPointerException e) {
            // This EvoSuite-specific assertion verifies that the exception
            // originates from the GridArrangement class, which is more precise
            // than only checking the exception type.
            verifyException("org.jfree.chart.block.GridArrangement", e);
        }
    }
}