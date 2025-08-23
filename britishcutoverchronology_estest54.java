package org.threeten.extra.chrono;

import org.junit.jupiter.api.Test;
import java.time.DateTimeException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link BritishCutoverChronology}.
 */
class BritishCutoverChronologyTest {

    @Test
    void date_whenDayOfMonthIsInvalidForJulianDate_throwsException() {
        // Arrange: The BritishCutoverChronology uses Julian calendar rules for dates
        // before the 1752 cutover. The year 9 is well before this cutover.
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;

        // Act & Assert: Attempt to create an invalid date (September 31st)
        // and verify that a DateTimeException is thrown.
        DateTimeException exception = assertThrows(
                DateTimeException.class,
                () -> chronology.date(9, 9, 31)
        );

        // Assert: Verify the exception message for correctness.
        assertEquals("Invalid date 'SEPTEMBER 31'", exception.getMessage());
    }
}