package org.threeten.extra.chrono;

import java.time.format.ResolverStyle;
import java.time.temporal.TemporalField;
import java.util.Map;
import org.junit.Test;

/**
 * Test suite for the BritishCutoverChronology.
 */
public class BritishCutoverChronologyTest {

    /**
     * Tests that resolveDate() throws a NullPointerException if the field map is null.
     * The underlying AbstractChronology class mandates this behavior, so this test
     * confirms compliance with the contract.
     */
    @Test(expected = NullPointerException.class)
    public void resolveDate_whenFieldMapIsNull_throwsNullPointerException() {
        // Obtain an instance of the chronology using the recommended singleton
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        
        // The specific resolver style is not relevant for a null map, so any can be used
        ResolverStyle style = ResolverStyle.SMART;

        // This call is expected to throw a NullPointerException
        chronology.resolveDate((Map<TemporalField, Long>) null, style);
    }
}