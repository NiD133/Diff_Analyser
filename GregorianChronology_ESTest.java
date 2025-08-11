package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Readable tests for GregorianChronology behavior.
 * 
 * Notes:
 * - Tests avoid environment-dependent behavior (e.g., default time zone),
 *   unless explicitly intended.
 * - Exception assertions use assertThrows for clarity.
 * - Numeric expectations include brief context in comments where helpful.
 */
public class GregorianChronologyTest {

    // --- calculateFirstDayOfYearMillis ---

    @Test
    public void calculateFirstDayOfYearMillis_year20() {
        GregorianChronology c = GregorianChronology.getInstanceUTC();
        long millis = c.calculateFirstDayOfYearMillis(20);
        // Known value from Joda-Time implementation
        assertEquals(-61536067200000L, millis);
    }

    @Test
    public void calculateFirstDayOfYearMillis_epoch1970() {
        GregorianChronology c = GregorianChronology.getInstanceUTC();
        long millis = c.calculateFirstDayOfYearMillis(1970);
        assertEquals(0L, millis);
    }

    @Test
    public void calculateFirstDayOfYearMillis_yearZero() {
        GregorianChronology c = GregorianChronology.getInstanceUTC();
        long millis = c.calculateFirstDayOfYearMillis(0);
        // 0000-01-01T00:00:00.000Z
        assertEquals(-62167219200000L, millis);
    }

    @Test
    public void calculateFirstDayOfYearMillis_farFutureYear3000() {
        GregorianChronology c = GregorianChronology.getInstanceUTC();
        long millis = c.calculateFirstDayOfYearMillis(3000);
        assertEquals(32503680000000L, millis);
    }

    @Test
    public void calculateFirstDayOfYearMillis_nearMinSupportedYear() {
        GregorianChronology c = GregorianChronology.getInstanceUTC();
        long millis = c.calculateFirstDayOfYearMillis(-292275054);
        assertEquals(-9223372017043200000L, millis);
    }

    // --- leap year rules ---

    @Test
    public void isLeapYear_yearZeroIsLeap() {
        GregorianChronology c = GregorianChronology.getInstance(DateTimeZone.UTC);
        assertTrue("Year 0 is divisible by 400 in proleptic Gregorian", c.isLeapYear(0));
    }

    @Test
    public void isLeapYear_negativeYearNotLeap() {
        GregorianChronology c = GregorianChronology.getInstanceUTC();
        assertFalse(c.isLeapYear(-900));
    }

    // --- averages and constants ---

    @Test
    public void averageMillisPerYearDividedByTwo() {
        GregorianChronology c = GregorianChronology.getInstanceUTC();
        assertEquals(15778476000L, c.getAverageMillisPerYearDividedByTwo());
    }

    @Test
    public void approxMillisAtEpochDividedByTwo() {
        GregorianChronology c = GregorianChronology.getInstanceUTC();
        assertEquals(31083597720000L, c.getApproxMillisAtEpochDividedByTwo());
    }

    @Test
    public void averageMillisPerYear() {
        GregorianChronology c = GregorianChronology.getInstanceUTC();
        assertEquals(31556952000L, c.getAverageMillisPerYear());
    }

    @Test
    public void averageMillisPerMonth() {
        GregorianChronology c = GregorianChronology.getInstanceUTC();
        assertEquals(2629746000L, c.getAverageMillisPerMonth());
    }

    @Test
    public void getMaxYear_constant() {
        GregorianChronology c = GregorianChronology.getInstanceUTC();
        assertEquals(292278993, c.getMaxYear());
    }

    @Test
    public void getMinYear_constant() {
        GregorianChronology c = GregorianChronology.getInstance(DateTimeZone.forOffsetHours(1), 1);
        assertEquals(-292275054, c.getMinYear());
    }

    // --- time zone behavior ---

    @Test
    public void withUTC_returnsDifferentInstanceForNonUTC() {
        GregorianChronology nonUtc = GregorianChronology.getInstance(DateTimeZone.forOffsetHours(2), 7);
        Chronology utc = nonUtc.withUTC();
        assertNotSame("withUTC() should return a different instance for non-UTC chronology", nonUtc, utc);
    }

    @Test
    public void withZone_sameInstanceWhenAlreadyUTC() {
        GregorianChronology utc = GregorianChronology.getInstanceUTC();
        Chronology stillUtc = utc.withZone(DateTimeZone.UTC);
        assertSame("withZone(UTC) should return the same UTC instance", utc, stillUtc);
    }

    @Test
    public void withZone_nullUsesDefaultZoneAndReturnsSameInstanceIfAlreadyInDefaultZone() {
        DateTimeZone defaultZone = DateTimeZone.getDefault();
        GregorianChronology inDefault = GregorianChronology.getInstance(defaultZone);
        Chronology againDefault = inDefault.withZone(null);
        assertSame("withZone(null) should use default zone and return same instance if already in default zone",
                inDefault, againDefault);
    }

    // --- assemble ---

    @Test
    public void assemble_acceptsFieldsInstance() {
        GregorianChronology c = GregorianChronology.getInstanceUTC();
        AssembledChronology.Fields fields = new AssembledChronology.Fields();
        // Should not throw
        c.assemble(fields);
    }

    @Test
    public void assemble_nullFields_throwsNPE() {
        GregorianChronology c = GregorianChronology.getInstanceUTC();
        NullPointerException ex = assertThrows(NullPointerException.class, () -> c.assemble(null));
        assertNull("Joda typically throws NPE without a message here", ex.getMessage());
    }

    // --- argument validation on factory methods ---

    @Test
    public void getInstance_invalidMinDays_throwsIAE() {
        DateTimeZone zone = DateTimeZone.forOffsetMillis(531);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> GregorianChronology.getInstance(zone, 531));
        assertTrue(ex.getMessage().contains("Invalid min days in first week: 531"));
    }

    @Test
    public void getInstance_nullZoneInvalidMinDays_throwsIAE() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> GregorianChronology.getInstance(null, -3203));
        assertTrue(ex.getMessage().contains("Invalid min days in first week: -3203"));
    }

    // --- behavior with specific conversions ---

    @Test
    public void localDateTime_toDateTimeWithOffset() {
        GregorianChronology utc = GregorianChronology.getInstanceUTC();
        LocalDateTime ldt = new LocalDateTime(199L, utc);
        DateTimeZone offset = DateTimeZone.forOffsetMillis(-3631);
        DateTime dt = ldt.toDateTime(offset);
        assertEquals(3830L, dt.getMillis());
    }

    // --- boundary/invalid inputs on internal methods ---

    @Test
    public void getDaysInYearMonth_invalidMonth_throwsArrayIndexOutOfBounds() {
        GregorianChronology utc = GregorianChronology.getInstanceUTC();
        ArrayIndexOutOfBoundsException ex = assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> utc.getDaysInYearMonth(-2764, -2764));
        // Joda's BasicGJChronology uses a month array lookup; message shows the index used
        assertTrue("Expected message to contain invalid array index",
                ex.getMessage() != null && ex.getMessage().contains("-2765"));
    }
}