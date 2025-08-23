package org.joda.time.convert;

import static org.junit.Assert.assertEquals;

import java.util.Locale;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for StringConverter.
 * This test focuses on creating a DateTime object from a String.
 */
public class StringConverterTest {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");

    private DateTimeZone originalDefaultZone;
    private Locale originalDefaultLocale;

    @Before
    public void setUp() {
        // To ensure tests are isolated from the system's default settings,
        // we save the original defaults and set predictable ones for the test run.
        originalDefaultZone = DateTimeZone.getDefault();
        originalDefaultLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        Locale.setDefault(Locale.UK);
    }

    @After
    public void tearDown() {
        // Restore the original default timezone and locale to avoid side effects on other tests.
        DateTimeZone.setDefault(originalDefaultZone);
        Locale.setDefault(originalDefaultLocale);
    }

    /**
     * Tests that a DateTime object can be correctly constructed from an ISO 8601 formatted string
     * when an explicit time zone is provided.
     * The constructor {@code new DateTime(String, DateTimeZone)} implicitly uses the StringConverter.
     */
    @Test
    public void shouldCreateDateTimeFromISOStringWithExplicitTimeZone() {
        // Arrange
        String isoDateTimeString = "2004-06-09T12:24:48.501";
        DateTime expectedDateTime = new DateTime(2004, 6, 9, 12, 24, 48, 501, PARIS);

        // Act
        // This constructor call implicitly uses StringConverter to parse the string.
        DateTime actualDateTime = new DateTime(isoDateTimeString, PARIS);

        // Assert
        assertEquals(expectedDateTime, actualDateTime);
    }
}