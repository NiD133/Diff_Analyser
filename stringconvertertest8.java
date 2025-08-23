package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.JulianChronology;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link StringConverter#getChronology(Object, Chronology)}.
 * This test focuses on how the converter determines the correct Chronology
 * based on the input string and a potentially provided Chronology.
 */
class StringConverterGetChronologyTest {

    private static final String DATE_TIME_WITH_ZONE = "2004-06-09T12:24:48.501+01:00";
    private static final String DATE_TIME_WITHOUT_ZONE = "2004-06-09T12:24:48.501";
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");

    private DateTimeZone originalDefaultZone;

    @BeforeEach
    void setUp() {
        // Save and set a specific default time zone to ensure tests are predictable.
        originalDefaultZone = DateTimeZone.getDefault();
        DateTimeZone.setDefault(LONDON);
    }

    @AfterEach
    void tearDown() {
        // Restore the original default time zone to avoid side effects on other tests.
        DateTimeZone.setDefault(originalDefaultZone);
    }

    @Test
    void getChronology_whenChronologyIsProvided_returnsSameChronology() {
        // Arrange
        // The converter should return the provided chronology, ignoring the string's content.
        Chronology expectedChronology = JulianChronology.getInstance(LONDON);

        // Act & Assert
        assertEquals(expectedChronology, StringConverter.INSTANCE.getChronology(DATE_TIME_WITH_ZONE, expectedChronology));
        assertEquals(expectedChronology, StringConverter.INSTANCE.getChronology(DATE_TIME_WITHOUT_ZONE, expectedChronology));
    }

    @Test
    void getChronology_whenChronologyIsNullAndStringHasZone_returnsISOChronologyWithParsedZone() {
        // Arrange
        // The string contains a zone offset "+01:00". For the given date in June,
        // this corresponds to the LONDON zone (which is in British Summer Time, BST).
        Chronology expectedChronology = ISOChronology.getInstance(LONDON);

        // Act
        Chronology actualChronology = StringConverter.INSTANCE.getChronology(DATE_TIME_WITH_ZONE, null);

        // Assert
        assertEquals(expectedChronology, actualChronology);
    }

    @Test
    void getChronology_whenChronologyIsNullAndStringHasNoZone_returnsISOChronologyWithDefaultZone() {
        // Arrange
        // The default zone is set to LONDON in setUp().
        Chronology expectedChronology = ISOChronology.getInstance(LONDON);

        // Act
        Chronology actualChronology = StringConverter.INSTANCE.getChronology(DATE_TIME_WITHOUT_ZONE, null);

        // Assert
        assertEquals(expectedChronology, actualChronology);
    }
}