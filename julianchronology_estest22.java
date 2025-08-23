package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.Clock;

/**
 * Unit tests for the {@link JulianChronology} class.
 */
public class JulianChronologyTest {

    @Test(expected = NullPointerException.class)
    public void dateNow_withNullClock_shouldThrowNullPointerException() {
        // Arrange: Obtain the singleton instance of the chronology, as recommended by its documentation.
        JulianChronology chronology = JulianChronology.INSTANCE;

        // Act & Assert: Calling dateNow() with a null clock should throw a NullPointerException.
        // The cast to (Clock) is necessary to resolve ambiguity with other overloaded dateNow() methods.
        chronology.dateNow((Clock) null);
    }
}