package org.jfree.data.xy;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link XYInterval} class.
 */
public class XYIntervalTest {

    /**
     * Verifies that the equals() method returns false for two XYInterval objects
     * that differ only by their y-interval boundaries (yLow and yHigh).
     */
    @Test
    public void equals_shouldReturnFalse_whenYIntervalsDiffer() {
        // Arrange: Create two intervals with identical x-values and y-values,
        // but different y-interval boundaries.
        XYInterval intervalA = new XYInterval(0.0, 0.0, 0.0, 0.0, 0.0);
        XYInterval intervalB = new XYInterval(0.0, 0.0, 0.0, -10.0, 10.0);

        // Act & Assert: The two intervals should not be considered equal.
        assertNotEquals(intervalA, intervalB);
    }
}