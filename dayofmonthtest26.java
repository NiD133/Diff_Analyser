package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.temporal.TemporalField;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link DayOfMonth}.
 */
class DayOfMonthTest {

    // A sample DayOfMonth instance for testing. The specific value (12) is arbitrary
    // for this test case, as the behavior with a null field is independent of the value.
    private static final DayOfMonth DAY_OF_MONTH_12 = DayOfMonth.of(12);

    /**
     * Tests that calling getLong() with a null TemporalField throws a NullPointerException.
     *
     * The method contract requires a non-null field. This test verifies that the
     * implementation correctly enforces this precondition.
     */
    @Test
    void getLong_whenFieldIsNull_throwsNullPointerException() {
        // The cast to (TemporalField) is necessary to resolve method signature ambiguity for the compiler.
        assertThrows(NullPointerException.class, () -> {
            DAY_OF_MONTH_12.getLong((TemporalField) null);
        });
    }
}