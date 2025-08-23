package org.jfree.chart.block;

import org.junit.Test;
import java.awt.Graphics2D;
import org.jfree.data.Range;

/**
 * This test class contains tests for the GridArrangement class.
 * This particular test was improved for clarity and maintainability.
 */
public class GridArrangement_ESTestTest5 extends GridArrangement_ESTest_scaffolding {

    /**
     * Verifies that the arrange() method throws a NullPointerException if the
     * Graphics2D context is null. The method requires a valid graphics context
     * to perform its layout calculations, so a null value is an invalid argument.
     */
    @Test(expected = NullPointerException.class)
    public void arrange_withNullGraphics2D_shouldThrowNullPointerException() {
        // Arrange: Set up the necessary objects for the test.
        // The specific row/column counts are not critical for this test,
        // as the exception is expected before they are used.
        GridArrangement arrangement = new GridArrangement(2, 3);
        BlockContainer container = new BlockContainer();
        RectangleConstraint constraint = new RectangleConstraint(100.0, (Range) null);

        // Act: Call the method under test with a null Graphics2D object.
        // This is the action expected to trigger the exception.
        arrangement.arrange(container, null, constraint);

        // Assert: The test will pass only if a NullPointerException is thrown,
        // as specified by the @Test(expected=...) annotation.
    }
}