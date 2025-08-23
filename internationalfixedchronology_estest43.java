package org.threeten.extra.chrono;

import java.time.temporal.TemporalAccessor;
import org.junit.Test;

/**
 * Unit tests for the {@link InternationalFixedChronology} class.
 */
public class InternationalFixedChronologyTest {

    /**
     * Verifies that calling date() with a null TemporalAccessor throws a NullPointerException.
     * This ensures the method correctly handles invalid null input as per its contract.
     */
    @Test(expected = NullPointerException.class)
    public void date_whenTemporalAccessorIsNull_throwsNullPointerException() {
        // The system under test is the singleton instance of the chronology.
        // The constructor is deprecated, so we use the provided INSTANCE.
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;

        // This call is expected to throw the NullPointerException.
        // The cast to (TemporalAccessor) is needed to resolve method ambiguity for the null argument.
        chronology.date((TemporalAccessor) null);
    }
}