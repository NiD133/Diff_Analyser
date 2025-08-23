package org.jfree.chart.block;

import org.junit.Test;

/**
 * Tests for the {@link GridArrangement} class, focusing on exception handling.
 */
public class GridArrangementTest {

    /**
     * Verifies that the arrangeNN() method throws a NullPointerException when the
     * container argument is null. This is expected behavior as the method
     * requires a valid container to perform its calculations.
     */
    @Test(expected = NullPointerException.class)
    public void arrangeNNShouldThrowNullPointerExceptionForNullContainer() {
        // Arrange: Create a standard 1x1 grid arrangement. The dimensions are
        // not relevant for this test, as the null check should happen before
        // they are used.
        GridArrangement arrangement = new GridArrangement(1, 1);

        // Act & Assert: Call arrangeNN with a null container.
        // This is expected to throw a NullPointerException.
        // The Graphics2D context can also be null for this test case.
        arrangement.arrangeNN(null, null);
    }
}