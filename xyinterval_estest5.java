package org.jfree.data.xy;

import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the {@link XYInterval} class, focusing on the equals() method.
 */
public class XYIntervalTest {

    /**
     * Verifies that two XYInterval instances are not equal if their xLow values differ,
     * while all other properties remain the same.
     */
    @Test
    public void equals_shouldReturnFalse_whenXLowValuesDiffer() {
        // Arrange: Create two intervals that differ only by their xLow value.
        XYInterval interval1 = new XYInterval(0.0, 0.0, 0.0, 0.0, 0.0);
        XYInterval interval2 = new XYInterval(1.0, 0.0, 0.0, 0.0, 0.0);

        // Act & Assert: The intervals should not be considered equal.
        assertNotEquals("Intervals with different xLow values should not be equal.", interval1, interval2);
    }
}