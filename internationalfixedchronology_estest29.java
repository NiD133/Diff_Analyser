package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.chrono.Era;
import java.time.chrono.JapaneseEra;

/**
 * Tests for the {@link InternationalFixedChronology} class, focusing on handling invalid inputs.
 */
public class InternationalFixedChronologyTest {

    /**
     * Tests that dateYearDay() throws a ClassCastException when the provided Era
     * is not from the InternationalFixedChronology.
     *
     * The Javadoc for the method explicitly states that a ClassCastException will be thrown
     * if the era is not an InternationalFixedEra.
     */
    @Test(expected = ClassCastException.class)
    public void dateYearDay_whenEraIsFromDifferentChronology_thenThrowsClassCastException() {
        // Arrange: Get the chronology instance and an Era from a different calendar system.
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        Era invalidEra = JapaneseEra.HEISEI; // An era that is not an InternationalFixedEra
        int yearOfEra = 2010;
        int dayOfYear = 150;

        // Act: Attempt to create a date with the invalid era.
        // This call is expected to throw a ClassCastException.
        chronology.dateYearDay(invalidEra, yearOfEra, dayOfYear);

        // Assert: The exception is caught and validated by the @Test(expected=...) annotation.
    }
}