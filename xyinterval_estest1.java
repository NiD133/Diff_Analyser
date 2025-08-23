package org.jfree.data.xy;

import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the {@link XYInterval} class.
 */
public class XYIntervalTest {

    /**
     * Verifies that the equals() method returns false when two XYInterval objects
     * differ only by their yHigh value.
     */
    @Test
    public void equals_shouldReturnFalse_whenOnlyYHighIsDifferent() {
        // Arrange: Create two intervals that are identical except for the yHigh value.
        XYInterval interval1 = new XYInterval(1.0, 2.0, 10.0, 9.0, 11.0);
        XYInterval interval2 = new XYInterval(1.0, 2.0, 10.0, 9.0, 99.0); // Different yHigh

        // Act & Assert: The two intervals should not be considered equal.
        assertNotEquals(interval1, interval2);
    }
}