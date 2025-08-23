package org.joda.time.convert;

import static org.junit.Assert.assertEquals;

import java.util.Locale;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.ISOChronology;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link StringConverter}, focusing on the getInstantMillis(Object, Chronology) method.
 *
 * <p>These tests verify that the converter correctly parses ISO 8601 formatted strings
 * into milliseconds, considering both the timezone offset within the string and the
 * timezone provided by the Chronology.
 */
public class StringConverterTest {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");

    private static final Chronology ISO_PARIS = ISOChronology.getInstance(PARIS);
    private static final Chronology ISO_LONDON = ISOChronology.getInstance(LONDON);

    private DateTimeZone originalDefaultZone;
    private Locale originalDefaultLocale;

    /**
     * Stores the original default timezone and locale, and sets them to a fixed value
     * for test consistency. This ensures tests are not affected by the environment they run in.
     */
    @Before
    public void setUp() {
        originalDefaultZone = DateTimeZone.getDefault();
        originalDefaultLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        Locale.setDefault(Locale.UK);
    }

    /**
     * Restores the original default timezone and locale after each test.
     */
    @After
    public void tearDown() {
        DateTimeZone.setDefault(originalDefaultZone);
        Locale.setDefault(originalDefaultLocale);
    }

    @Test
    public void getInstantMillis_shouldParseStringWithOffset_usingParisChronology() {
        // The input string has a timezone offset (+02:00) which corresponds to the Paris timezone.
        // The converter should correctly parse this into the corresponding instant.
        String inputWithOffset = "2004-06-09T12:24:48.501+02:00";
        DateTime expected = new DateTime(2004, 6, 9, 12, 24, 48, 501, PARIS);

        long actualMillis = StringConverter.INSTANCE.getInstantMillis(inputWithOffset, ISO_PARIS);

        assertEquals(expected.getMillis(), actualMillis);
    }

    @Test
    public void getInstantMillis_shouldParseStringWithoutOffset_usingParisChronologyZone() {
        // The input string has no timezone. The converter should use the timezone
        // from the provided chronology (ISO_PARIS) to interpret the local time.
        String inputWithoutOffset = "2004-06-09T12:24:48.501";
        DateTime expected = new DateTime(2004, 6, 9, 12, 24, 48, 501, PARIS);

        long actualMillis = StringConverter.INSTANCE.getInstantMillis(inputWithoutOffset, ISO_PARIS);

        assertEquals(expected.getMillis(), actualMillis);
    }

    @Test
    public void getInstantMillis_shouldParseStringWithOffset_usingLondonChronology() {
        // The input string has a timezone offset (+01:00) which corresponds to the London timezone.
        // The converter should correctly parse this into the corresponding instant.
        String inputWithOffset = "2004-06-09T12:24:48.501+01:00";
        DateTime expected = new DateTime(2004, 6, 9, 12, 24, 48, 501, LONDON);

        long actualMillis = StringConverter.INSTANCE.getInstantMillis(inputWithOffset, ISO_LONDON);

        assertEquals(expected.getMillis(), actualMillis);
    }

    @Test
    public void getInstantMillis_shouldParseStringWithoutOffset_usingLondonChronologyZone() {
        // The input string has no timezone. The converter should use the timezone
        // from the provided chronology (ISO_LONDON) to interpret the local time.
        String inputWithoutOffset = "2004-06-09T12:24:48.501";
        DateTime expected = new DateTime(2004, 6, 9, 12, 24, 48, 501, LONDON);

        long actualMillis = StringConverter.INSTANCE.getInstantMillis(inputWithoutOffset, ISO_LONDON);

        assertEquals(expected.getMillis(), actualMillis);
    }
}