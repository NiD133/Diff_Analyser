package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.temporal.TemporalField;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link DayOfMonth#get(TemporalField)} method.
 */
class DayOfMonthGetTest {

    private static final DayOfMonth SAMPLE_DAY_OF_MONTH = DayOfMonth.of(12);

    @Test
    @DisplayName("get(TemporalField) throws NullPointerException when the field is null")
    void get_whenFieldIsNull_throwsNullPointerException() {
        // The cast to TemporalField is necessary to resolve method ambiguity for the compiler
        assertThrows(NullPointerException.class, () -> SAMPLE_DAY_OF_MONTH.get((TemporalField) null));
    }
}