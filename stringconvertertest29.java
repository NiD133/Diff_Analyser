package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableInterval;
import org.joda.time.chrono.ISOChronology;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link StringConverter}.
 */
public class StringConverterTest {

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");

    private DateTimeZone originalDefaultZone;
    private Locale originalDefaultLocale;

    @Before
    public void setUp() {
        // Save the original default zone and locale to restore them after the test.
        originalDefaultZone = DateTimeZone.getDefault();
        originalDefaultLocale = Locale.getDefault();

        // Set a predictable default zone and locale for the test environment.
        // The StringConverter may rely on these defaults when no explicit chronology is provided.
        DateTimeZone.setDefault(LONDON);
        Locale.setDefault(Locale.UK);
    }

    @After
    public void tearDown() {
        // Restore the original default zone and locale.
        DateTimeZone.setDefault(originalDefaultZone);
        Locale.setDefault(originalDefaultLocale);
    }

    @Test
    public void setInto_shouldParseIntervalStringWithStartDateAndPeriod() {
        // GIVEN: An ISO 8601 string for an interval defined by a start date and a period.
        String intervalString = "2004-06-09/P1Y2M";
        MutableInterval intervalToUpdate = new MutableInterval();

        // The test runs in the "Europe/London" timezone, as set in setUp().
        // The converter should use the default ISO chronology for that zone.
        Chronology expectedChronology = ISOChronology.getInstance(LONDON);

        // Define the expected start and end DateTimes based on the input string.
        DateTime expectedStart = new DateTime(2004, 6, 9, 0, 0, 0, 0, expectedChronology);
        // The end is calculated by adding the period (P1Y2M: 1 year, 2 months) to the start.
        DateTime expectedEnd = new DateTime(2005, 8, 9, 0, 0, 0, 0, expectedChronology);

        // WHEN: The string is converted and set into the mutable interval.
        // A null chronology is passed, so the converter should use the default.
        StringConverter.INSTANCE.setInto(intervalToUpdate, intervalString, null);

        // THEN: The interval's properties are correctly set.
        assertEquals("Start of interval should be parsed correctly", expectedStart, intervalToUpdate.getStart());
        assertEquals("End of interval should be calculated correctly", expectedEnd, intervalToUpdate.getEnd());
        assertEquals("Chronology should be the default ISO chronology", expectedChronology, intervalToUpdate.getChronology());
    }
}