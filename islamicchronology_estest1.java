package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.chrono.IslamicChronology.LeapYearPatternType;

/**
 * Unit tests for the inner classes and methods of {@link IslamicChronology}.
 */
public class IslamicChronologyTest {

    /**
     * Tests that the equals() method of the LeapYearPatternType inner class
     * correctly returns false when comparing two different pattern constants.
     */
    @Test
    public void leapYearPatternType_equals_shouldReturnFalseForDifferentPatterns() {
        // Arrange: Define two different leap year patterns to compare.
        LeapYearPatternType pattern15Based = IslamicChronology.LEAP_YEAR_15_BASED;
        LeapYearPatternType habashAlHasibPattern = IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB;

        // Act: Call the equals() method to compare the two patterns.
        boolean areEqual = habashAlHasibPattern.equals(pattern15Based);

        // Assert: Verify that the result is false, as the patterns are different.
        assertFalse("Different LeapYearPatternType instances should not be equal.", areEqual);
    }
}