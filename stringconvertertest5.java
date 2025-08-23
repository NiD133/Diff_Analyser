package org.joda.time.convert;

import static org.junit.Assert.assertEquals;

import java.util.Locale;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.JulianChronology;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * A test suite for the {@link StringConverter} class.
 * This test focuses on the getInstantMillis method.
 */
public class StringConverterTest {

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");

    // Store original default time zone and locale to restore them after tests
    private DateTimeZone originalDefaultZone;
    private Locale originalDefaultLocale;

    @Before
    public void setUp() {
        // Save the original defaults to ensure tests are isolated
        originalDefaultZone = DateTimeZone.getDefault();
        originalDefaultLocale = Locale.getDefault();

        // Set predictable defaults for the test environment
        DateTimeZone.setDefault(LONDON);
        Locale.setDefault(Locale.UK);
    }

    @After
    public void tearDown() {
        // Restore the original defaults to avoid side effects on other tests
        DateTimeZone.setDefault(originalDefaultZone);
        Locale.setDefault(originalDefaultLocale);
    }

    @Test
    public void getInstantMillis_shouldParseIsoStringUsingSpecifiedChronology() {
        // Arrange
        // An ISO-8601 formatted string. The +01:00 offset represents British Summer Time (BST),
        // which is the correct offset for the London timezone in June.
        final String dateTimeString = "2004-06-09T12:24:48.501+01:00";

        // The test will parse the string using the Julian calendar system. The StringConverter
        // should parse the ISO string format but interpret the date and time values
        // using this specified chronology.
        final Chronology julianChronologyInLondon = JulianChronology.getInstance(LONDON);

        // Create the expected DateTime object to get the correct millisecond value for comparison.
        // This ensures our reference point is built with the same parameters as the test input.
        final DateTime expectedDateTime = new DateTime(2004, 6, 9, 12, 24, 48, 501, julianChronologyInLondon);
        final long expectedMillis = expectedDateTime.getMillis();

        // Act
        final long actualMillis = StringConverter.INSTANCE.getInstantMillis(dateTimeString, julianChronologyInLondon);

        // Assert
        assertEquals(expectedMillis, actualMillis);
    }
}