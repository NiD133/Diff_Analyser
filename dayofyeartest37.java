package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.temporal.Temporal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link DayOfYear#adjustInto(Temporal)} method.
 */
class DayOfYearAdjustIntoTest {

    private static final DayOfYear SAMPLE_DAY_OF_YEAR = DayOfYear.of(12);

    @Test
    @DisplayName("adjustInto(null) should throw NullPointerException")
    void adjustInto_withNullTemporal_throwsNullPointerException() {
        // The contract of TemporalAdjuster#adjustInto requires throwing a NullPointerException
        // if the temporal object to be adjusted is null.
        assertThrows(NullPointerException.class, () ->
            SAMPLE_DAY_OF_YEAR.adjustInto((Temporal) null)
        );
    }
}