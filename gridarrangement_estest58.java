package org.jfree.chart.block;

import org.jfree.data.Range;
import org.junit.Test;

import java.awt.Graphics2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for the {@link GridArrangement} class.
 */
public class GridArrangementTest {

    /**
     * The arrangeFR() method, which handles arrangement for a fixed-width and
     * range-height constraint, is not implemented in the current version.
     * This test verifies that calling it throws a RuntimeException as expected.
     */
    @Test
    public void arrangeFRShouldThrowExceptionAsItIsNotImplemented() {
        // Arrange: Set up a grid arrangement, a container with a block, and a
        // constraint that matches the method being tested (Fixed-width, Range-height).
        GridArrangement gridArrangement = new GridArrangement(2, 2);
        BlockContainer container = new BlockContainer();
        container.add(new EmptyBlock(0, 0)); // Add a simple block to the container

        // Create a constraint for a fixed width and a ranged height (FR).
        RectangleConstraint frConstraint = new RectangleConstraint(100.0, new Range(50.0, 150.0));
        Graphics2D g2 = null; // Graphics2D is not used by the unimplemented method.

        // Act & Assert: Verify that calling the method throws a RuntimeException
        // with the specific "Not implemented" message.
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            gridArrangement.arrangeFR(container, g2, frConstraint);
        });

        assertEquals("Not implemented.", exception.getMessage());
    }
}