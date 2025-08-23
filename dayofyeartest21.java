package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link DayOfYear#get(java.time.temporal.TemporalField)} method.
 */
class DayOfYearGetTest {

    @Test
    void get_whenFieldIsNull_throwsNullPointerException() {
        // A sample DayOfYear instance to call the method on.
        // The specific value of the day does not affect this test's outcome.
        DayOfYear testDayOfYear = DayOfYear.of(12);

        // The contract for get(TemporalField) requires a non-null field.
        // We verify that passing null correctly throws a NullPointerException.
        assertThrows(NullPointerException.class, () -> testDayOfYear.get(null));
    }
}