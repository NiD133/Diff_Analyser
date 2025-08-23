package org.threeten.extra.chrono;

import java.time.chrono.Era;
import java.time.chrono.JapaneseEra;
import org.junit.Test;

/**
 * Tests for the {@link InternationalFixedChronology} class.
 */
public class InternationalFixedChronologyTest {

    /**
     * Tests that the date() method throws a ClassCastException when provided with an Era
     * from a different calendar system.
     */
    @Test(expected = ClassCastException.class)
    public void date_whenEraIsFromDifferentChronology_throwsClassCastException() {
        // Arrange
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        Era invalidEra = JapaneseEra.TAISHO; // An era from a non-IFC chronology.
        
        // The specific date values are irrelevant, as the type check on the Era happens first.
        int year = 2012;
        int month = 6;
        int day = 15;

        // Act
        // The date() method expects an InternationalFixedEra. This call should fail
        // because it's being passed a JapaneseEra.
        chronology.date(invalidEra, year, month, day);
    }
}