package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableInterval;
import org.joda.time.chrono.BuddhistChronology;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link StringConverter#setInto(ReadWritableInterval, Object, Chronology)}.
 */
public class StringConverterSetIntoIntervalTest {

    // Time zones used to construct the test data
    private static final DateTimeZone ZONE_PLUS_6 = DateTimeZone.forOffsetHours(6);
    private static final DateTimeZone ZONE_PLUS_7 = DateTimeZone.forOffsetHours(7);
    private static final DateTimeZone ZONE_PLUS_8 = DateTimeZone.forOffsetHours(8);

    // Keep track of original default zone and locale to restore them after tests
    private DateTimeZone originalDefaultZone;
    private Locale originalDefaultLocale;

    @Before
    public void setUp() {
        originalDefaultZone = DateTimeZone.getDefault();
        originalDefaultLocale = Locale.getDefault();
        DateTimeZone.setDefault(DateTimeZone.forID("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @After
    public void tearDown() {
        DateTimeZone.setDefault(originalDefaultZone);
        Locale.setDefault(originalDefaultLocale);
    }

    @Test
    public void setIntoInterval_whenStringHasOffsets_respectsStringOffsetsAndAssignsChronology() {
        // Arrange
        // The interval string contains start/end date-times with specific UTC offsets (+06:00 and +07:00).
        final String intervalString = "2003-08-09T+06:00/2004-06-09T+07:00";

        // The target chronology for the interval has a different time zone (+08:00).
        final Chronology targetChronology = BuddhistChronology.getInstance(ZONE_PLUS_8);

        // An existing interval to be modified. Its initial state is irrelevant.
        MutableInterval interval = new MutableInterval(0L, 0L);

        // Act
        StringConverter.INSTANCE.setInto(interval, intervalString, targetChronology);

        // Assert
        // The converter should parse the start and end times using the offsets from the string,
        // creating instants that are independent of the target chronology's time zone.
        DateTime expectedStartInstant = new DateTime(2003, 8, 9, 0, 0, 0, 0, BuddhistChronology.getInstance(ZONE_PLUS_6));
        DateTime expectedEndInstant = new DateTime(2004, 6, 9, 0, 0, 0, 0, BuddhistChronology.getInstance(ZONE_PLUS_7));

        // Verify that the interval's start and end represent the correct absolute moments in time.
        assertEquals("Interval start millis should match parsed instant",
                expectedStartInstant.getMillis(), interval.getStartMillis());
        assertEquals("Interval end millis should match parsed instant",
                expectedEndInstant.getMillis(), interval.getEndMillis());

        // Verify that the interval's chronology was set to the one provided.
        assertEquals("Interval chronology should be the one provided",
                targetChronology, interval.getChronology());
    }
}