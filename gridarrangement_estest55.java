package org.jfree.chart.block;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.awt.Graphics2D;

import static org.hamcrest.CoreMatchers.is;

/**
 * Tests for the {@link GridArrangement} class, focusing on exception handling for unimplemented methods.
 */
public class GridArrangementTest {

    // A JUnit Rule for testing exceptions in a declarative way.
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Verifies that the arrangeNF() method throws a RuntimeException because it is not implemented.
     * The "NF" in the method name signifies arranging with No constraint on width and a Fixed height.
     */
    @Test
    public void arrangeNF_shouldThrowRuntimeException_asItIsNotImplemented() {
        // Arrange
        GridArrangement arrangement = new GridArrangement(1, 1);
        BlockContainer container = new BlockContainer();
        RectangleConstraint constraint = RectangleConstraint.NONE;
        Graphics2D g2 = null; // The Graphics2D object is not used in this path

        // Assert - Define expectations for the exception
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(is("Not implemented."));

        // Act - Call the method that is expected to throw the exception
        arrangement.arrangeNF(container, g2, constraint);
    }
}