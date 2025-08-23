package org.jfree.data.xy;

import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the {@link XYInterval} class, focusing on its equality logic.
 */
public class XYIntervalTest {

    /**
     * Verifies that two XYInterval objects are not considered equal if their
     * yLow values differ, even when all other properties are identical.
     */
    @Test
    public void equals_shouldReturnFalse_whenYLowValuesDiffer() {
        // Arrange: Create two intervals that are identical except for the yLow value.
        XYInterval interval1 = new XYInterval(1.0, 2.0, 1.5, 1.4, 1.6);
        XYInterval interval2 = new XYInterval(1.0, 2.0, 1.5, 9.9, 1.6); // Different yLow

        // Act & Assert: The two intervals should not be equal.
        // The assertNotEquals method is a clear and concise way to test for inequality.
        assertNotEquals(interval1, interval2);
    }
}