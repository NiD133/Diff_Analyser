package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.temporal.TemporalAccessor;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DayOfYear}.
 */
class DayOfYearTest {

    //-----------------------------------------------------------------------
    // from(TemporalAccessor)
    //-----------------------------------------------------------------------

    /**
     * Tests that from() throws a NullPointerException when the temporal accessor is null.
     */
    @Test
    void from_whenTemporalAccessorIsNull_throwsNullPointerException() {
        // The cast to TemporalAccessor is necessary to resolve method ambiguity for the `from(null)` call.
        assertThrows(NullPointerException.class, () -> DayOfYear.from((TemporalAccessor) null));
    }
}