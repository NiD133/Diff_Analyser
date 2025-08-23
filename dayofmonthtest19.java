package org.threeten.extra;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DayOfMonth}.
 */
class DayOfMonthTest {

    private static final DayOfMonth DAY_OF_MONTH_12 = DayOfMonth.of(12);

    //-----------------------------------------------------------------------
    // get(TemporalField)
    //-----------------------------------------------------------------------

    @Test
    void get_withDayOfMonthField_returnsTheDayValue() {
        // Asserts that get() with the DAY_OF_MONTH field returns the correct integer value.
        assertEquals(12, DAY_OF_MONTH_12.get(DAY_OF_MONTH));
    }
}