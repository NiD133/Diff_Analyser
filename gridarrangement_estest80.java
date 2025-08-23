package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link GridArrangement} class.
 */
public class GridArrangementTest {

    /**
     * Verifies the reflexive property of the equals() method.
     * An object must always be equal to itself.
     */
    @Test
    public void equals_whenComparedToItself_shouldReturnTrue() {
        // Arrange
        GridArrangement arrangement = new GridArrangement(0, 0);

        // Act & Assert
        // According to the contract of equals(), an object must be equal to itself.
        assertEquals(arrangement, arrangement);
    }
}