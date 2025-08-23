package org.threeten.extra;

import org.junit.jupiter.api.Test;

import java.time.DateTimeException;
import java.time.chrono.JapaneseDate;
import java.time.temporal.Temporal;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the behavior of {@link DayOfYear#adjustInto(Temporal)}.
 */
class DayOfYearAdjustIntoTest {

    /**
     * Tests that calling adjustInto() with a temporal object that does not use the
     * ISO calendar system throws a DateTimeException.
     *
     * The contract of DayOfYear.adjustInto() specifies that it only operates on ISO-based temporals.
     * This test verifies that providing a non-ISO date, like a JapaneseDate,
     * correctly triggers this exception, upholding the method's contract.
     */
    @Test
    void adjustInto_withNonIsoChronology_shouldThrowException() {
        // Arrange
        DayOfYear dayOfYear = DayOfYear.of(12);
        Temporal nonIsoDate = JapaneseDate.of(2007, 1, 1);

        // Act & Assert
        assertThrows(DateTimeException.class, () -> dayOfYear.adjustInto(nonIsoDate));
    }
}