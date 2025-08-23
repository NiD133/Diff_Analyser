package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.format.ResolverStyle;
import java.time.temporal.TemporalField;
import java.util.Map;

/**
 * Tests for the {@link JulianChronology} class.
 */
public class JulianChronologyTest {

    /**
     * Tests that resolveDate throws a NullPointerException when the field map is null.
     * This behavior is inherited from the base AbstractChronology class.
     */
    @Test(expected = NullPointerException.class)
    public void resolveDate_withNullFieldMap_throwsNullPointerException() {
        // Arrange
        JulianChronology chronology = JulianChronology.INSTANCE;
        ResolverStyle style = ResolverStyle.STRICT;

        // Act: Call the method under test with a null map, which is expected to throw.
        chronology.resolveDate((Map<TemporalField, Long>) null, style);

        // Assert: The exception is verified by the @Test(expected=...) annotation.
    }
}