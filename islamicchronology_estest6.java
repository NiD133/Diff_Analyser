package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test class contains tests for the IslamicChronology class.
 * Note: The original class name 'IslamicChronology_ESTestTest6' is an artifact
 * of a test generation tool. A more conventional name would be 'IslamicChronologyTest'.
 */
public class IslamicChronology_ESTestTest6 {

    /**
     * Tests that getDaysInYearMonth returns 29 for any even-numbered month.
     *
     * <p>This test specifically uses a negative even number for the month parameter to
     * confirm that the method's logic relies solely on the parity (even/odd) of the
     * month number. It demonstrates that the implementation lacks input validation to
     * ensure the month is within the expected range of 1 to 12.</p>
     *
     * <p>The year is arbitrary and does not affect the outcome, as the month is not 12
     * (the only month where the year matters for leap year calculations).</p>
     */
    @Test
    public void getDaysInYearMonth_forAnyEvenMonth_shouldReturn29Days() {
        // Arrange
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        int anyNonLeapAffectingYear = 220;
        int anEvenMonthNumber = -4026; // An invalid but even month number.
        int expectedDaysInShortMonth = 29;

        // Act
        int actualDays = islamicChronology.getDaysInYearMonth(anyNonLeapAffectingYear, anEvenMonthNumber);

        // Assert
        assertEquals(expectedDaysInShortMonth, actualDays);
    }
}