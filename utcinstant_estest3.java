package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the equals() and hashCode() contract of the {@link UtcInstant} class.
 */
public class UtcInstantEqualsTest {

    /**
     * Tests that two {@code UtcInstant} objects are not equal if they share the same
     * Modified Julian Day but have different nano-of-day values.
     */
    @Test
    public void equals_isFalse_whenInstantsHaveSameDayButDifferentNanos() {
        // Arrange: Create two UtcInstant objects representing different times on the same day.
        long sameModifiedJulianDay = 301L;
        UtcInstant instant1 = UtcInstant.ofModifiedJulianDay(sameModifiedJulianDay, 301L);
        UtcInstant instant2 = UtcInstant.ofModifiedJulianDay(sameModifiedJulianDay, 70L);

        // Act & Assert: The two instants should not be considered equal.
        // The assertNotEquals method provides a clear and direct check for inequality
        // by calling the .equals() method.
        assertNotEquals(instant1, instant2);
    }
}