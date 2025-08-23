package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the {@link GridArrangement} class.
 */
public class GridArrangementTest {

    /**
     * Verifies that the equals() method correctly returns false when comparing
     * two GridArrangement instances with different row counts.
     */
    @Test
    public void equals_shouldReturnFalse_whenRowCountsDiffer() {
        // Arrange: Create two arrangements with the same column count but different row counts.
        GridArrangement arrangementA = new GridArrangement(15, 15);
        GridArrangement arrangementB = new GridArrangement(20, 15);

        // Act & Assert: The two arrangements should not be considered equal.
        assertNotEquals(arrangementA, arrangementB);
    }
}