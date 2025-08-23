package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

/**
 * Contains unit tests for the {@link GridArrangement#equals(Object)} method.
 */
public class GridArrangement_ESTestTest2 {

    /**
     * Verifies that two GridArrangement instances are not considered equal if they
     * have the same row count but different column counts. This test also ensures
     * that the equals() implementation is symmetric.
     */
    @Test
    public void equals_shouldReturnFalse_whenColumnCountsDiffer() {
        // Arrange: Create two arrangements with the same number of rows but
        // different numbers of columns.
        GridArrangement arrangementA = new GridArrangement(0, 0);
        GridArrangement arrangementB = new GridArrangement(0, -1901);

        // Assert: The two arrangements should not be equal.
        // Using assertNotEquals is more expressive than assertFalse(a.equals(b)).
        // We test both directions to confirm the method is symmetric.
        assertNotEquals(arrangementA, arrangementB);
        assertNotEquals(arrangementB, arrangementA);
    }
}