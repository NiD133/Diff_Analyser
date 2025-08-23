package org.threeten.extra.chrono;

import org.junit.Test;

/**
 * Tests for {@link BritishCutoverChronology} focusing on argument validation.
 */
public class BritishCutoverChronologyTest {

    /**
     * Verifies that {@code localDateTime(TemporalAccessor)} throws a {@code NullPointerException}
     * when the temporal accessor argument is null. This ensures the method adheres to its contract
     * of rejecting null inputs.
     */
    @Test(expected = NullPointerException.class)
    public void localDateTime_withNullTemporal_throwsNullPointerException() {
        // The INSTANCE singleton is used for the test.
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        
        // The method under test should throw NullPointerException for null input.
        chronology.localDateTime(null);
    }
}