package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the equals() method of the {@link GridArrangement} class.
 */
public class GridArrangementTest {

    /**
     * Verifies that two GridArrangement objects are not equal if they have the
     * same number of rows but a different number of columns.
     */
    @Test
    public void equals_shouldReturnFalse_whenColumnsDiffer() {
        // Arrange: Create two arrangements with the same row count but different column counts.
        GridArrangement arrangement1 = new GridArrangement(5, 10);
        GridArrangement arrangement2 = new GridArrangement(5, 20);

        // Act & Assert: The two objects should not be considered equal.
        // We also check for symmetry, ensuring a.equals(b) is consistent with b.equals(a).
        assertNotEquals(arrangement1, arrangement2);
        assertNotEquals(arrangement2, arrangement1);
    }
}