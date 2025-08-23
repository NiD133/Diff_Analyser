package org.jfree.chart.block;

import org.junit.Test;

import java.awt.Graphics2D;
import org.jfree.data.Range;

import static org.junit.Assert.assertThrows;

/**
 * Contains focused tests for the {@link GridArrangement} class.
 */
public class GridArrangementTest {

    /**
     * Verifies that the arrangeRN method throws a NullPointerException when
     * the Graphics2D context is null. The method requires a valid graphics
     * context to perform measurements and calculations, so passing null is
     * an illegal argument.
     */
    @Test
    public void arrangeRN_shouldThrowNullPointerException_whenGraphics2DIsNull() {
        // Arrange: Create a simple grid arrangement and an empty container.
        GridArrangement gridArrangement = new GridArrangement(5, 5);
        BlockContainer emptyContainer = new BlockContainer();
        
        // The constraint is not critical, but we create one that matches the
        // 'RN' (Range-None) method signature for correctness.
        RectangleConstraint constraint = new RectangleConstraint(new Range(0, 100), null);

        // Act & Assert: Expect a NullPointerException when calling arrangeRN with a null Graphics2D object.
        assertThrows(NullPointerException.class, () -> {
            gridArrangement.arrangeRN(emptyContainer, (Graphics2D) null, constraint);
        });
    }
}