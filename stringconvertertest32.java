package org.joda.time.convert;

import static org.junit.Assert.assertEquals;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableInterval;
import org.joda.time.Period;
import org.joda.time.chrono.ISOChronology;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link StringConverter#setInto(ReadWritableInterval, Object, Chronology)}.
 */
public class StringConverterSetIntoIntervalTest {

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private DateTimeZone originalDefaultZone;

    /**
     * The method under test uses the default time zone when a null chronology is provided.
     * We set it to a known value (LONDON) to ensure the test is stable and predictable,
     * and restore it afterward to avoid side effects on other tests.
     */
    @Before
    public void setUp() {
        originalDefaultZone = DateTimeZone.getDefault();
        DateTimeZone.setDefault(LONDON);
    }

    @After
    public void tearDown() {
        DateTimeZone.setDefault(originalDefaultZone);
    }

    /**
     * Tests that setInto correctly parses an interval string defined by a start instant
     * (with a specific time zone) and a period. The resulting interval should use the
     * default ISO chronology.
     */
    @Test
    public void setInto_parsesStringWithStartInstantAndPeriod_usingDefaultChronology() {
        // Arrange
        // The input string defines an interval starting at 2004-06-09 in the +06:00 timezone,
        // and lasting for a period of 1 year and 2 months.
        String intervalString = "2004-06-09T+06:00/P1Y2M";

        // When the converter is called with a null chronology, it is expected to use the
        // default ISO chronology, which is based on the default time zone (LONDON).
        Chronology expectedChronology = ISOChronology.getInstance(LONDON);

        // Define the expected start instant and period from the input string.
        DateTimeZone startZone = DateTimeZone.forOffsetHours(6);
        DateTime startInstant = new DateTime(2004, 6, 9, 0, 0, 0, 0, startZone);
        Period period = new Period(1, 2, 0, 0, 0, 0, 0, 0); // P1Y2M

        // Construct the expected interval. The start and end DateTimes will have the same
        // millisecond instant as the parsed values, but their chronology will be the
        // interval's chronology (ISO in the LONDON zone).
        DateTime expectedStart = new DateTime(startInstant.getMillis(), expectedChronology);
        DateTime expectedEnd = expectedStart.plus(period);
        MutableInterval expectedInterval = new MutableInterval(expectedStart, expectedEnd);

        MutableInterval actualInterval = new MutableInterval(); // The interval to be populated.

        // Act
        // Pass null for the chronology to trigger the default behavior.
        StringConverter.INSTANCE.setInto(actualInterval, intervalString, null);

        // Assert
        // Verify that the populated interval matches the expected one.
        // This single assertion checks the start millis, end millis, and chronology.
        assertEquals(expectedInterval, actualInterval);
    }
}