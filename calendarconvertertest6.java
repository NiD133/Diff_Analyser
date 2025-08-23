package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.chrono.JulianChronology;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link CalendarConverter}.
 * This test focuses on the getChronology(Object, Chronology) method.
 */
class CalendarConverterTest {

    private CalendarConverter converter;
    private Chronology julianChronology;

    @BeforeEach
    void setUp() {
        converter = CalendarConverter.INSTANCE;
        julianChronology = JulianChronology.getInstance();
    }

    @Test
    @DisplayName("getChronology() should return the provided chronology if it is not null")
    void getChronology_whenChronologyIsProvided_returnsProvidedChronology() {
        // Arrange
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Paris"));
        Chronology expectedChronology = julianChronology;

        // Act
        Chronology actualChronology = converter.getChronology(calendar, expectedChronology);

        // Assert
        assertEquals(expectedChronology, actualChronology,
                "The converter should always return the explicitly provided chronology.");
    }
}