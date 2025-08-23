package org.jfree.chart.block;

import org.junit.Test;

/**
 * Tests for the {@link GridArrangement} class.
 */
public class GridArrangementTest {

    /**
     * Verifies that the clear() method executes without throwing an exception,
     * even when the GridArrangement is constructed with negative row and column counts.
     * The clear() method is specified to have no effect, so this test confirms
     * it can be safely called on any instance.
     */
    @Test
    public void clearShouldNotThrowExceptionWhenDimensionsAreNegative() {
        // Arrange: Create a GridArrangement with negative dimensions. The constructor
        // is expected to accept these values without throwing an exception.
        GridArrangement arrangement = new GridArrangement(-1, -1);

        // Act & Assert: Call the clear() method. The test succeeds if this call
        // completes without throwing any exceptions.
        arrangement.clear();
    }
}