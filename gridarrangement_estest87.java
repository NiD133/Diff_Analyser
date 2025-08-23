package org.jfree.chart.block;

import org.jfree.data.Range;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

/**
 * A test case for the arrangeFR() method in the GridArrangement class.
 */
public class GridArrangementArrangeFRTest {

    /**
     * Verifies that calling arrangeFR() with a null Graphics2D object
     * results in a NullPointerException. The Graphics2D object is essential
     * for layout calculations, and the method should not proceed without it.
     */
    @Test
    public void arrangeFRShouldThrowNullPointerExceptionForNullGraphics2D() {
        // Arrange: Create a grid arrangement and a container to be arranged.
        GridArrangement arrangement = new GridArrangement(2, 2);
        BlockContainer container = new BlockContainer();

        // Arrange: Define a constraint with a fixed width and a ranged height,
        // as expected by the arrangeFR method.
        final double fixedWidth = 100.0;
        final Range heightRange = new Range(50.0, 150.0);
        RectangleConstraint constraint = new RectangleConstraint(fixedWidth, heightRange);

        // Act & Assert: Expect a NullPointerException when arrangeFR is called
        // with a null Graphics2D context.
        assertThrows(NullPointerException.class, () -> {
            arrangement.arrangeFR(container, null, constraint);
        });
    }
}