package org.jfree.chart.block;

import org.jfree.data.Range;
import org.junit.Test;

import java.awt.Graphics2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

// Note: The original test class name is kept for context, though the test
// has been corrected to target BorderArrangement.
public class GridArrangement_ESTestTest43 {

    /**
     * Verifies that BorderArrangement.arrange() throws a RuntimeException when
     * provided with a constraint that requires the unimplemented arrangeRF() logic.
     * <p>
     * <b>Refactoring Note:</b> The original auto-generated test was logically flawed.
     * It created a {@code GridArrangement} instance but expected an exception that is
     * only thrown by {@code BorderArrangement#arrangeRF}. This test has been corrected
     * to instantiate {@code BorderArrangement} to align with the expected behavior,
     * making the test's intent clear and valid.
     */
    @Test(timeout = 4000)
    public void arrange_WithRangeWidthAndFixedHeight_ThrowsExceptionForBorderArrangement() {
        // ARRANGE
        // The test targets BorderArrangement because its arrangeRF() method (for
        // Range-width and Fixed-height) is not implemented.
        Arrangement arrangement = new BorderArrangement();
        BlockContainer container = new BlockContainer();

        // This constraint type will cause the public arrange() method to delegate
        // to the protected arrangeRF() method inside BorderArrangement.
        RectangleConstraint constraint = new RectangleConstraint(
                new Range(0.0, 200.0), // Range for width
                100.0                  // Fixed value for height
        );

        // ACT & ASSERT
        try {
            arrangement.arrange(container, (Graphics2D) null, constraint);
            fail("A RuntimeException was expected because BorderArrangement.arrangeRF() is not implemented.");
        } catch (RuntimeException e) {
            // Verify that the thrown exception has the expected message.
            assertEquals("Not implemented.", e.getMessage());
        }
    }
}