package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for {@link Symmetry010Chronology}.
 */
public class Symmetry010ChronologyTest {

    /**
     * Tests that isLeapYear() correctly identifies a non-leap year.
     *
     * <p>The Symmetry010 calendar determines leap years based on the formula:
     * {@code (52 > ((52 * year + 146) % 293))}.
     *
     * <p>For the year 32, the calculation is:
     * {@code (52 * 32 + 146) % 293 = 1810 % 293 = 52}.
     * The condition {@code 52 > 52} evaluates to false, meaning year 32 is not a leap year.
     * This test verifies this specific case.
     */
    @Test
    public void isLeapYear_whenYearIsNotLeap_returnsFalse() {
        // Arrange: Set up the test subject and input data.
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        long nonLeapYear = 32L;

        // Act: Call the method under test.
        boolean isLeap = chronology.isLeapYear(nonLeapYear);

        // Assert: Verify the result.
        assertFalse("Year 32 should not be a leap year according to the Symmetry010 rules.", isLeap);
    }
}