package org.jfree.chart.block;

import org.junit.Test;
import java.awt.Graphics2D;
import org.jfree.data.Range;

/**
 * Tests for the {@link GridArrangement} class, focusing on exception handling.
 */
public class GridArrangement_ESTestTest41 extends GridArrangement_ESTest_scaffolding {

    /**
     * Verifies that the arrange() method throws a NullPointerException when a null
     * Graphics2D object is provided. This is crucial because rendering operations
     * cannot proceed without a valid graphics context.
     */
    @Test(expected = NullPointerException.class)
    public void arrangeShouldThrowNPEForNullGraphics2D() {
        // Arrange: Create a grid arrangement, a container, and a constraint.
        // The specific dimensions are not critical for this test.
        GridArrangement arrangement = new GridArrangement(2, 2);
        BlockContainer container = new BlockContainer();

        // A constraint with a ranged width and a fixed height is used to ensure
        // the correct internal arrange method (arrangeRF) is dispatched.
        RectangleConstraint constraint = new RectangleConstraint(new Range(0, 100), 50.0);

        // Act: Call the arrange method with a null Graphics2D context.
        // The test expects a NullPointerException to be thrown here.
        arrangement.arrange(container, (Graphics2D) null, constraint);

        // Assert: The assertion is handled by the 'expected' attribute of the @Test annotation.
        // If a NullPointerException is not thrown, the test will fail.
    }
}