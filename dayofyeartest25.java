package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.temporal.IsoFields;
import java.time.temporal.UnsupportedTemporalTypeException;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DayOfYear}.
 */
class DayOfYearTest {

    @Test
    void getLong_forUnsupportedField_throwsException() {
        // A DayOfYear instance primarily supports the DAY_OF_YEAR field.
        // We expect an exception when querying for an unsupported field like DAY_OF_QUARTER.
        DayOfYear dayOfYear = DayOfYear.of(12);

        assertThrows(
                UnsupportedTemporalTypeException.class,
                () -> dayOfYear.getLong(IsoFields.DAY_OF_QUARTER),
                "DayOfYear should not support DAY_OF_QUARTER");
    }
}