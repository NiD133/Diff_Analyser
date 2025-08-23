package org.jfree.data.xy;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link XYInterval} class.
 */
public class XYIntervalTest {

    /**
     * Verifies that the equals() method returns false for two XYInterval objects
     * that have the same x-interval but different y-values.
     */
    @Test
    public void equals_shouldReturnFalse_whenYValuesAreDifferent() {
        // Arrange: Create two intervals with the same x-values but different y-values.
        XYInterval intervalA = new XYInterval(10.0, 20.0, 15.0, 14.0, 16.0);
        XYInterval intervalB = new XYInterval(10.0, 20.0, 30.0, 28.0, 32.0); // Same x-range, different y-values

        // Act & Assert: The two intervals should not be considered equal.
        assertNotEquals(intervalA, intervalB);
    }
}