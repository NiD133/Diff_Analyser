package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the {@link GridArrangement} class, focusing on its equality logic.
 */
public class GridArrangementTest {

    /**
     * Verifies that the equals() method returns false when comparing two
     * GridArrangement objects that have a different number of rows.
     */
    @Test
    public void equals_shouldReturnFalse_whenRowCountsDiffer() {
        // Arrange: Create two arrangements with the same column count but different row counts.
        GridArrangement arrangement1 = new GridArrangement(29, 698);
        GridArrangement arrangement2 = new GridArrangement(81, 698);

        // Act & Assert: The two objects should not be considered equal.
        assertNotEquals(arrangement1, arrangement2);
    }
}