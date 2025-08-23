package org.jfree.data.xy;

import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the {@link XYInterval} class.
 */
public class XYIntervalTest {

    /**
     * Verifies that the equals() method returns false for two XYInterval objects
     * that have the same x-interval but different y-related values.
     */
    @Test
    public void equals_returnsFalse_whenYValuesAreDifferent() {
        // Arrange: Create two intervals with the same x-interval but different y values.
        XYInterval intervalA = new XYInterval(0.0, 0.0, 0.0, 0.0, 0.0);
        XYInterval intervalB = new XYInterval(0.0, 0.0, -2810.0, 2884.61, -2810.0);

        // Act & Assert: The two intervals should not be considered equal.
        // The assertNotEquals method relies on the equals() implementation.
        assertNotEquals(intervalA, intervalB);
    }
}