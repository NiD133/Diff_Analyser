package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link IslamicChronology.LeapYearPatternType} inner class.
 */
public class IslamicChronologyLeapYearPatternTypeTest {

    /**
     * Verifies the reflexive property of the equals() method.
     * According to the contract of Object.equals(), an object must be equal to itself.
     */
    @Test
    public void testEqualsIsReflexive() {
        // Arrange
        final IslamicChronology.LeapYearPatternType pattern = IslamicChronology.LEAP_YEAR_15_BASED;

        // Assert: An object must be equal to itself.
        assertEquals(pattern, pattern);
    }
}