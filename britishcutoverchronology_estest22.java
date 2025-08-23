package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.Clock;

/**
 * Tests for {@link BritishCutoverChronology}.
 */
public class BritishCutoverChronologyTest {

    /**
     * Tests that dateNow(Clock) throws a NullPointerException when the clock is null.
     * The method contract requires a non-null clock.
     */
    @Test(expected = NullPointerException.class)
    public void dateNow_withNullClock_throwsNullPointerException() {
        // Arrange: Get the singleton instance of the chronology.
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;

        // Act & Assert: Call dateNow with a null clock.
        // The cast to (Clock) is necessary to resolve method overload ambiguity.
        chronology.dateNow((Clock) null);
    }
}