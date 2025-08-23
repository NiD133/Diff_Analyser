package org.jfree.data.xy;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link XYInterval} class.
 */
public class XYIntervalTest {

    /**
     * Verifies that the equals() method returns false when comparing two
     * XYInterval objects that have different property values.
     */
    @Test
    public void equals_shouldReturnFalse_forIntervalsWithDifferentProperties() {
        // Arrange: Create a base interval and another interval with some different values.
        XYInterval interval1 = new XYInterval(10.0, 10.0, 10.0, 10.0, 10.0);
        XYInterval interval2 = new XYInterval(10.0, -1.0, -1.0, 10.0, -1.0);

        // Act: Compare the two different intervals.
        boolean result = interval1.equals(interval2);

        // Assert: The result should be false, as the intervals are not identical.
        assertFalse("equals() should return false for XYInterval objects with different properties.", result);
    }
}