package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.ISOChronology;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link StringConverter#getChronology(Object, DateTimeZone)}.
 *
 * This test suite focuses on how the StringConverter determines the correct Chronology
 * based on the input string and an optional DateTimeZone parameter.
 */
@DisplayName("StringConverter.getChronology(Object, DateTimeZone)")
class StringConverterGetChronologyTest {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");

    private DateTimeZone originalDefaultZone;

    @BeforeEach
    void setUp() {
        // To ensure tests are predictable, we save the original default time zone
        // and set a specific one (LONDON) for the duration of each test.
        originalDefaultZone = DateTimeZone.getDefault();
        DateTimeZone.setDefault(LONDON);
    }

    @AfterEach
    void tearDown() {
        // Restore the original default time zone to prevent side effects on other tests.
        DateTimeZone.setDefault(originalDefaultZone);
    }

    @Test
    @DisplayName("should use the explicit time zone, ignoring any offset in the string")
    void getChronology_whenZoneIsProvided_usesProvidedZone() {
        // Arrange
        String dateTimeWithOffset = "2004-06-09T12:24:48.501+01:00";
        Chronology expected = ISOChronology.getInstance(PARIS);

        // Act: Call the method with an explicit zone (PARIS).
        Chronology actual = StringConverter.INSTANCE.getChronology(dateTimeWithOffset, PARIS);

        // Assert: The chronology should use the explicitly provided PARIS zone.
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("should use the explicit time zone when the string has no time zone info")
    void getChronology_whenZoneIsProvidedAndStringHasNoOffset_usesProvidedZone() {
        // Arrange
        String dateTimeWithoutOffset = "2004-06-09T12:24:48.501";
        Chronology expected = ISOChronology.getInstance(PARIS);

        // Act: Call the method with an explicit zone (PARIS).
        Chronology actual = StringConverter.INSTANCE.getChronology(dateTimeWithoutOffset, PARIS);

        // Assert: The chronology should use the provided PARIS zone as the string has none.
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("should use the time zone from the string's offset when the zone parameter is null")
    void getChronology_whenZoneIsNull_usesZoneFromString() {
        // Arrange
        // The test asserts that parsing a string with an offset, without an explicit
        // zone parameter, results in a chronology based on the default zone (LONDON).
        // This is because the string's offset (+01:00) matches London's summer time (BST)
        // offset for that date, and the parser resolves it accordingly.
        String dateTimeWithOffset = "2004-06-09T12:24:48.501+01:00";
        Chronology expected = ISOChronology.getInstance(LONDON);

        // Act: Call the method with a null zone.
        Chronology actual = StringConverter.INSTANCE.getChronology(dateTimeWithOffset, (DateTimeZone) null);

        // Assert: The chronology should be inferred from the string, resolving to the default zone.
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("should use the default time zone when the zone parameter is null and the string has no offset")
    void getChronology_whenZoneIsNullAndStringHasNoOffset_usesDefaultZone() {
        // Arrange
        String dateTimeWithoutOffset = "2004-06-09T12:24:48.501";
        // The default zone is LONDON (set in setUp).
        Chronology expected = ISOChronology.getInstance(LONDON);

        // Act: Call the method with a null zone.
        Chronology actual = StringConverter.INSTANCE.getChronology(dateTimeWithoutOffset, (DateTimeZone) null);

        // Assert: The chronology should fall back to the default system zone.
        assertEquals(expected, actual);
    }
}