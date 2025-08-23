package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class contains tests for the IslamicChronology class.
 * Note: The original class name and scaffolding are preserved from the auto-generated test suite.
 */
public class IslamicChronology_ESTestTest42 extends IslamicChronology_ESTest_scaffolding {

    /**
     * Tests that getDaysInMonthMax() returns 29 for any even-numbered month.
     *
     * This test specifically uses an invalid month number (100) to verify that
     * the method does not perform input validation. The result depends solely on
     * whether the month number is even or odd, not on its validity. In the
     * Islamic calendar's alternating month-length pattern, even months are
     * the shorter ones.
     */
    @Test
    public void getDaysInMonthMax_returns29ForEvenMonthNumber() {
        // Arrange
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        
        // An invalid, but even, month number is used to test the calculation logic.
        int evenMonthNumber = 100;
        final int DAYS_IN_SHORT_ISLAMIC_MONTH = 29;

        // Act
        int actualMaxDays = islamicChronology.getDaysInMonthMax(evenMonthNumber);

        // Assert
        assertEquals(DAYS_IN_SHORT_ISLAMIC_MONTH, actualMaxDays);
    }
}