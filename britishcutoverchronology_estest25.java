package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.chrono.Era;
import java.time.chrono.MinguoEra;

/**
 * Tests exception handling for the {@link BritishCutoverChronology} class.
 */
public class BritishCutoverChronologyExceptionTest {

    /**
     * Verifies that the {@code date(Era, int, int, int)} method throws a
     * {@code ClassCastException} when provided with an era that is not a {@link JulianEra}.
     * <p>
     * The {@code BritishCutoverChronology} is designed to work exclusively with {@code JulianEra}.
     * This test ensures that the method correctly rejects other era types as per its contract,
     * preventing incorrect date calculations.
     */
    @Test(expected = ClassCastException.class)
    public void date_withInvalidEraType_throwsClassCastException() {
        // Arrange: Create the chronology and an era of an incompatible type.
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        Era invalidEra = MinguoEra.BEFORE_ROC;

        // Act: Attempt to create a date using the invalid era.
        // The year, month, and day values are arbitrary, as the type check on the era
        // is expected to fail before these values are processed.
        chronology.date(invalidEra, 2000, 1, 1);

        // Assert: The test is expected to throw a ClassCastException, which is
        // handled by the @Test(expected) annotation.
    }
}