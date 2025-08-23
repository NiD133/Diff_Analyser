package org.jfree.data.xy;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the {@link XYInterval} class.
 */
public class XYIntervalTest {

    /**
     * Tests that the equals() method returns false when comparing two XYInterval
     * objects that have different property values.
     */
    @Test
    public void equals_shouldReturnFalseForDifferentIntervals() {
        // Arrange: Create two distinct XYInterval objects.
        // The constructor is XYInterval(xLow, xHigh, y, yLow, yHigh).
        XYInterval interval1 = new XYInterval(0.0, -1.0, 0.0, 0.0, -1.0);
        XYInterval interval2 = new XYInterval(0.0, 0.0, 1.0, 0.0, 0.0);

        // Act & Assert: The objects should not be considered equal.
        // Note: The original test confirmed they were different, so we do the same.
        assertNotEquals(interval1, interval2);
        
        // For completeness, we can also check the symmetry of the equals method.
        assertNotEquals(interval2, interval1);
    }
}