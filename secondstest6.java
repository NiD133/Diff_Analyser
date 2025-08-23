package org.joda.time;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Seconds} class, focusing on the
 * {@code standardSecondsIn(ReadablePeriod)} factory method.
 */
@DisplayName("Seconds.standardSecondsIn(ReadablePeriod)")
class SecondsTest {

    @Test
    @DisplayName("should return zero seconds when the period is null")
    void standardSecondsIn_givenNullPeriod_returnsZero() {
        assertEquals(Seconds.ZERO, Seconds.standardSecondsIn(null));
    }

    @Test
    @DisplayName("should return zero seconds for a zero-length period")
    void standardSecondsIn_givenZeroPeriod_returnsZero() {
        assertEquals(Seconds.ZERO, Seconds.standardSecondsIn(Period.ZERO));
    }

    @Test
    @DisplayName("should create an equivalent Seconds object for a period containing only seconds")
    void standardSecondsIn_givenPeriodOfSeconds_createsEquivalentSeconds() {
        assertEquals(Seconds.seconds(123), Seconds.standardSecondsIn(Period.seconds(123)));
        assertEquals(Seconds.seconds(-987), Seconds.standardSecondsIn(Period.seconds(-987)));
        assertEquals(Seconds.ONE, Seconds.standardSecondsIn(Period.seconds(1)));
    }

    @Test
    @DisplayName("should correctly convert standard period units (like days) into seconds")
    void standardSecondsIn_givenPeriodOfDays_convertsToSeconds() {
        // Given a period of 2 days
        Period twoDays = Period.days(2);
        
        // There are 2 * 24 * 60 * 60 = 172,800 seconds in 2 standard days
        Seconds expected = Seconds.seconds(172800);

        // When converting the period to seconds
        Seconds actual = Seconds.standardSecondsIn(twoDays);

        // Then the result should be correct
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("should throw IllegalArgumentException for periods with imprecise units like months")
    void standardSecondsIn_givenPeriodWithImpreciseUnits_throwsException() {
        // Given a period containing months, which have a variable number of days
        Period periodWithMonths = Period.months(1);

        // Then an IllegalArgumentException is expected
        assertThrows(IllegalArgumentException.class, () -> {
            Seconds.standardSecondsIn(periodWithMonths);
        });
    }
}