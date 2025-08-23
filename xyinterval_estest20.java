package org.jfree.data.xy;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the {@link XYInterval} class.
 */
public class XYIntervalTest {

    /**
     * Verifies that the equals() method returns false when two XYInterval objects
     * differ only by their xLow value.
     */
    @Test
    public void equals_shouldReturnFalse_whenXLowValuesDiffer() {
        // Arrange: Create two intervals that are identical except for the xLow value.
        XYInterval interval1 = new XYInterval(0.0, 0.0, 0.0, 0.0, 0.0);
        XYInterval interval2 = new XYInterval(1.0, 0.0, 0.0, 0.0, 0.0);

        // Act & Assert: The two intervals should not be considered equal.
        // We also check for symmetry in the equals implementation.
        assertNotEquals(interval1, interval2);
        assertNotEquals(interval2, interval1);
    }
}