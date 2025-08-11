package org.joda.time.convert;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Locale;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableInterval;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.TimeOfDay;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.JulianChronology;

/**
 * Clearer, less repetitive tests for StringConverter.
 * Keeps JUnit 3 style to match the project, but factors out helpers and
 * uses descriptive test names and assertions to aid readability.
 */
public class TestStringConverter extends TestCase {

    // Common zones and chronologies used across tests
    private static final DateTimeZone ONE_HOUR = DateTimeZone.forOffsetHours(1);
    private static final DateTimeZone SIX = DateTimeZone.forOffsetHours(6);
    private static final DateTimeZone SEVEN = DateTimeZone.forOffsetHours(7);
    private static final DateTimeZone EIGHT = DateTimeZone.forOffsetHours(8);
    private static final DateTimeZone UTC = DateTimeZone.UTC;
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");

    private static final Chronology ISO_EIGHT = ISOChronology.getInstance(EIGHT);
    private static final Chronology ISO_PARIS = ISOChronology.getInstance(PARIS);
    private static final Chronology ISO_LONDON = ISOChronology.getInstance(LONDON);

    private static Chronology ISO;
    private static Chronology JULIAN;

    // Test harness state
    private DateTimeZone originalZone;
    private Locale originalLocale;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestStringConverter.class);
    }

    public TestStringConverter(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        originalZone = DateTimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        Locale.setDefault(Locale.UK);

        ISO = ISOChronology.getInstance();
        JULIAN = JulianChronology.getInstance();
    }

    @Override
    protected void tearDown() throws Exception {
        DateTimeZone.setDefault(originalZone);
        Locale.setDefault(originalLocale);
        originalZone = null;
        originalLocale = null;
    }

    // ---------------------------------------------------------------------
    // Singleton and supported type
    // ---------------------------------------------------------------------
    public void testSingletonVisibilityAndInstanceField() throws Exception {
        Class cls = StringConverter.class;

        assertFalse(Modifier.isPublic(cls.getModifiers()));
        assertFalse(Modifier.isProtected(cls.getModifiers()));
        assertFalse(Modifier.isPrivate(cls.getModifiers()));

        Constructor con = cls.getDeclaredConstructor((Class[]) null);
        assertEquals(1, cls.getDeclaredConstructors().length);
        assertTrue(Modifier.isProtected(con.getModifiers()));

        Field fld = cls.getDeclaredField("INSTANCE");
        assertFalse(Modifier.isPublic(fld.getModifiers()));
        assertFalse(Modifier.isProtected(fld.getModifiers()));
        assertFalse(Modifier.isPrivate(fld.getModifiers()));
    }

    public void testSupportedTypeIsStringClass() {
        assertSame(String.class, StringConverter.INSTANCE.getSupportedType());
    }

    // ---------------------------------------------------------------------
    // getInstantMillis
    // ---------------------------------------------------------------------
    public void testGetInstantMillis_parsesIsoInstantVariants() {
        // Full date-time with offset
        assertSameInstant(dt(EIGHT, 2004, 6, 9, 12, 24, 48, 501),
                StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24:48.501+08:00", ISO_EIGHT));

        // Year only
        assertSameInstant(dt(EIGHT, 2004, 1, 1, 0, 0, 0, 0),
                StringConverter.INSTANCE.getInstantMillis("2004T+08:00", ISO_EIGHT));

        // Year-month
        assertSameInstant(dt(EIGHT, 2004, 6, 1, 0, 0, 0, 0),
                StringConverter.INSTANCE.getInstantMillis("2004-06T+08:00", ISO_EIGHT));

        // Calendar date (Y-M-D)
        assertSameInstant(dt(EIGHT, 2004, 6, 9, 0, 0, 0, 0),
                StringConverter.INSTANCE.getInstantMillis("2004-06-09T+08:00", ISO_EIGHT));

        // Ordinal date (Y-dayOfYear)
        assertSameInstant(dt(EIGHT, 2004, 6, 9, 0, 0, 0, 0),
                StringConverter.INSTANCE.getInstantMillis("2004-161T+08:00", ISO_EIGHT));

        // Week date (Y-Www-D)
        assertSameInstant(dt(EIGHT, 2004, 6, 9, 0, 0, 0, 0),
                StringConverter.INSTANCE.getInstantMillis("2004-W24-3T+08:00", ISO_EIGHT));

        // Week date (Y-Www)
        assertSameInstant(dt(EIGHT, 2004, 6, 7, 0, 0, 0, 0),
                StringConverter.INSTANCE.getInstantMillis("2004-W24T+08:00", ISO_EIGHT));

        // Truncated time (hour)
        assertSameInstant(dt(EIGHT, 2004, 6, 9, 12, 0, 0, 0),
                StringConverter.INSTANCE.getInstantMillis("2004-06-09T12+08:00", ISO_EIGHT));

        // Truncated time (hour:minute)
        assertSameInstant(dt(EIGHT, 2004, 6, 9, 12, 24, 0, 0),
                StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24+08:00", ISO_EIGHT));

        // Full time, no millis
        assertSameInstant(dt(EIGHT, 2004, 6, 9, 12, 24, 48, 0),
                StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24:48+08:00", ISO_EIGHT));

        // Fractional hour
        assertSameInstant(dt(EIGHT, 2004, 6, 9, 12, 30, 0, 0),
                StringConverter.INSTANCE.getInstantMillis("2004-06-09T12.5+08:00", ISO_EIGHT));

        // Fractional minute
        assertSameInstant(dt(EIGHT, 2004, 6, 9, 12, 24, 30, 0),
                StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24.5+08:00", ISO_EIGHT));

        // Fractional second
        assertSameInstant(dt(EIGHT, 2004, 6, 9, 12, 24, 48, 500),
                StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24:48.5+08:00", ISO_EIGHT));

        // Uses parser zone when none specified, default ISO chronology
        assertSameInstant(dt(DateTimeZone.getDefault(), 2004, 6, 9, 12, 24, 48, 501),
                StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24:48.501", ISO));
    }

    public void testGetInstantMillis_respectsZoneFromChronology() {
        assertSameInstant(dt(PARIS, 2004, 6, 9, 12, 24, 48, 501),
                StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24:48.501+02:00", ISO_PARIS));

        assertSameInstant(dt(PARIS, 2004, 6, 9, 12, 24, 48, 501),
                StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24:48.501", ISO_PARIS));

        assertSameInstant(dt(LONDON, 2004, 6, 9, 12, 24, 48, 501),
                StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24:48.501+01:00", ISO_LONDON));

        assertSameInstant(dt(LONDON, 2004, 6, 9, 12, 24, 48, 501),
                StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24:48.501", ISO_LONDON));
    }

    public void testGetInstantMillis_withNonIsoChronology() {
        DateTime expected = new DateTime(2004, 6, 9, 12, 24, 48, 501, JulianChronology.getInstance(LONDON));
        assertEquals(expected.getMillis(),
                StringConverter.INSTANCE.getInstantMillis("2004-06-09T12:24:48.501+01:00", JULIAN));
    }

    public void testGetInstantMillis_invalidInputsThrowIAE() {
        assertIllegalArgumentThrown(new ThrowingCallable() {
            public void call() {
                StringConverter.INSTANCE.getInstantMillis("", (Chronology) null);
            }
        });
        assertIllegalArgumentThrown(new ThrowingCallable() {
            public void call() {
                StringConverter.INSTANCE.getInstantMillis("X", (Chronology) null);
            }
        });
    }

    // ---------------------------------------------------------------------
    // getChronology
    // ---------------------------------------------------------------------
    public void testGetChronology_withZone() {
        assertEquals(ISOChronology.getInstance(PARIS),
                StringConverter.INSTANCE.getChronology("2004-06-09T12:24:48.501+01:00", PARIS));
        assertEquals(ISOChronology.getInstance(PARIS),
                StringConverter.INSTANCE.getChronology("2004-06-09T12:24:48.501", PARIS));

        assertEquals(ISOChronology.getInstance(LONDON),
                StringConverter.INSTANCE.getChronology("2004-06-09T12:24:48.501+01:00", (DateTimeZone) null));
        assertEquals(ISOChronology.getInstance(LONDON),
                StringConverter.INSTANCE.getChronology("2004-06-09T12:24:48.501", (DateTimeZone) null));
    }

    public void testGetChronology_withChronology() {
        assertEquals(JulianChronology.getInstance(LONDON),
                StringConverter.INSTANCE.getChronology("2004-06-09T12:24:48.501+01:00", JULIAN));
        assertEquals(JulianChronology.getInstance(LONDON),
                StringConverter.INSTANCE.getChronology("2004-06-09T12:24:48.501", JULIAN));

        assertEquals(ISOChronology.getInstance(LONDON),
                StringConverter.INSTANCE.getChronology("2004-06-09T12:24:48.501+01:00", (Chronology) null));
        assertEquals(ISOChronology.getInstance(LONDON),
                StringConverter.INSTANCE.getChronology("2004-06-09T12:24:48.501", (Chronology) null));
    }

    // ---------------------------------------------------------------------
    // getPartialValues
    // ---------------------------------------------------------------------
    public void testGetPartialValues_timeOfDay() {
        TimeOfDay tod = new TimeOfDay();
        int[] expected = new int[] {3, 4, 5, 6};
        int[] actual = StringConverter.INSTANCE.getPartialValues(tod, "T03:04:05.006", ISOChronology.getInstance());
        assertIntArrayEquals(expected, actual);
    }

    // ---------------------------------------------------------------------
    // DateTime(String) behavior (integration-level checks)
    // ---------------------------------------------------------------------
    public void testDateTime_fromString_defaultZoneAndChronology() {
        DateTime test = new DateTime("2004-06-09T12:24:48.501+01:00");
        assertDateTimeFields(test, 2004, 6, 9, 12, 24, 48, 501, LONDON);
    }

    public void testDateTime_fromString_noOffset_usesDefaultZone() {
        DateTime test = new DateTime("2004-06-09T12:24:48.501");
        assertDateTimeFields(test, 2004, 6, 9, 12, 24, 48, 501, LONDON);
    }

    public void testDateTime_fromString_withExplicitZoneParameter() {
        DateTime test = new DateTime("2004-06-09T12:24:48.501+02:00", PARIS);
        assertDateTimeFields(test, 2004, 6, 9, 12, 24, 48, 501, PARIS);
    }

    public void testDateTime_fromString_noOffset_withZoneParameter() {
        DateTime test = new DateTime("2004-06-09T12:24:48.501", PARIS);
        assertDateTimeFields(test, 2004, 6, 9, 12, 24, 48, 501, PARIS);
    }

    public void testDateTime_fromString_withNonIsoChronologyAndOffset() {
        DateTime test = new DateTime("2004-06-09T12:24:48.501+02:00", JulianChronology.getInstance(PARIS));
        assertDateTimeFields(test, 2004, 6, 9, 12, 24, 48, 501, PARIS);
    }

    public void testDateTime_fromString_withNonIsoChronologyNoOffset() {
        DateTime test = new DateTime("2004-06-09T12:24:48.501", JulianChronology.getInstance(PARIS));
        assertDateTimeFields(test, 2004, 6, 9, 12, 24, 48, 501, PARIS);
    }

    public void testDateTime_roundTrip_fromBaseToString() {
        DateTime base = new DateTime(2004, 6, 9, 12, 24, 48, 501, PARIS);
        DateTime parsed = new DateTime(base.toString(), PARIS);
        assertEquals(base, parsed);
    }

    // ---------------------------------------------------------------------
    // getDurationMillis
    // ---------------------------------------------------------------------
    public void testGetDurationMillis_validInputs() {
        assertEquals(12345, StringConverter.INSTANCE.getDurationMillis("PT12.345S"));
        assertEquals(12345, StringConverter.INSTANCE.getDurationMillis("pt12.345s"));
        assertEquals(12000, StringConverter.INSTANCE.getDurationMillis("pt12s"));
        assertEquals(12000, StringConverter.INSTANCE.getDurationMillis("pt12.s"));
        assertEquals(-12320, StringConverter.INSTANCE.getDurationMillis("pt-12.32s"));
        assertEquals(-320, StringConverter.INSTANCE.getDurationMillis("pt-0.32s"));
        assertEquals(0, StringConverter.INSTANCE.getDurationMillis("pt-0.0s"));
        assertEquals(0, StringConverter.INSTANCE.getDurationMillis("pt0.0s"));
        assertEquals(12345, StringConverter.INSTANCE.getDurationMillis("pt12.3456s")); // truncated millis
    }

    public void testGetDurationMillis_invalidInputsThrowIAE() {
        String[] invalid = new String[] {
            "P2Y6M9DXYZ", "PTS", "XT0S", "PX0S", "PT0X", "PTXS", "PT0.0.0S", "PT0-00S", "PT-.001S"
        };
        for (int i = 0; i < invalid.length; i++) {
            final String s = invalid[i];
            assertIllegalArgumentThrown(new ThrowingCallable() {
                public void call() {
                    StringConverter.INSTANCE.getDurationMillis(s);
                }
            });
        }
    }

    // ---------------------------------------------------------------------
    // Period parsing (setInto ReadWritablePeriod)
    // ---------------------------------------------------------------------
    public void testGetPeriodType_fromString() {
        assertEquals(PeriodType.standard(), StringConverter.INSTANCE.getPeriodType("P2Y6M9D"));
    }

    public void testSetIntoPeriod_yearMonthDayTime() {
        MutablePeriod p = new MutablePeriod(PeriodType.yearMonthDayTime());
        StringConverter.INSTANCE.setInto(p, "P2Y6M9DT12H24M48S", null);
        assertPeriod(p, 2, 6, 0, 9, 12, 24, 48, 0);
    }

    public void testSetIntoPeriod_yearWeekDayTime() {
        MutablePeriod p = new MutablePeriod(PeriodType.yearWeekDayTime());
        StringConverter.INSTANCE.setInto(p, "P2Y4W3DT12H24M48S", null);
        assertPeriod(p, 2, 0, 4, 3, 12, 24, 48, 0);
    }

    public void testSetIntoPeriod_withFractionalSeconds() {
        MutablePeriod p = new MutablePeriod(PeriodType.yearWeekDayTime());
        StringConverter.INSTANCE.setInto(p, "P2Y4W3DT12H24M48.034S", null);
        assertPeriod(p, 2, 0, 4, 3, 12, 24, 48, 34);
    }

    public void testSetIntoPeriod_onlyFractionalSeconds() {
        MutablePeriod p = new MutablePeriod(PeriodType.yearWeekDayTime());
        StringConverter.INSTANCE.setInto(p, "P2Y4W3DT12H24M.056S", null);
        assertPeriod(p, 2, 0, 4, 3, 12, 24, 0, 56);
    }

    public void testSetIntoPeriod_secondsFollowedByFractionSymbol() {
        MutablePeriod p = new MutablePeriod(PeriodType.yearWeekDayTime());
        StringConverter.INSTANCE.setInto(p, "P2Y4W3DT12H24M56.S", null);
        assertPeriod(p, 2, 0, 4, 3, 12, 24, 56, 0);
    }

    public void testSetIntoPeriod_fractionalSecondsTruncatedToMillis() {
        MutablePeriod p = new MutablePeriod(PeriodType.yearWeekDayTime());
        StringConverter.INSTANCE.setInto(p, "P2Y4W3DT12H24M56.1234567S", null);
        assertPeriod(p, 2, 0, 4, 3, 12, 24, 56, 123);
    }

    public void testSetIntoPeriod_zerosUnsetFields() {
        MutablePeriod p = new MutablePeriod(1, 0, 1, 1, 1, 1, 1, 1, PeriodType.yearWeekDayTime());
        StringConverter.INSTANCE.setInto(p, "P2Y4W3D", null);
        assertPeriod(p, 2, 0, 4, 3, 0, 0, 0, 0);
    }

    public void testSetIntoPeriod_invalidInputsThrowIAE() {
        final MutablePeriod p = new MutablePeriod();
        assertIllegalArgumentThrown(new ThrowingCallable() {
            public void call() {
                StringConverter.INSTANCE.setInto(p, "", null);
            }
        });
        assertIllegalArgumentThrown(new ThrowingCallable() {
            public void call() {
                StringConverter.INSTANCE.setInto(p, "PXY", null);
            }
        });
        assertIllegalArgumentThrown(new ThrowingCallable() {
            public void call() {
                StringConverter.INSTANCE.setInto(p, "PT0SXY", null);
            }
        });
        assertIllegalArgumentThrown(new ThrowingCallable() {
            public void call() {
                StringConverter.INSTANCE.setInto(p, "P2Y4W3DT12H24M48SX", null);
            }
        });
    }

    // ---------------------------------------------------------------------
    // Interval parsing (setInto ReadWritableInterval)
    // ---------------------------------------------------------------------
    public void testIsReadableInterval_returnsFalseForEmptyString() {
        assertFalse(StringConverter.INSTANCE.isReadableInterval("", null));
    }

    public void testSetIntoInterval_startInstantSlashPeriod() {
        MutableInterval i = new MutableInterval(-1000L, 1000L);
        StringConverter.INSTANCE.setInto(i, "2004-06-09/P1Y2M", null);
        assertInterval(i,
                dt(null, 2004, 6, 9, 0, 0, 0, 0),
                dt(null, 2005, 8, 9, 0, 0, 0, 0),
                ISOChronology.getInstance());
    }

    public void testSetIntoInterval_periodSlashEndInstant() {
        MutableInterval i = new MutableInterval(-1000L, 1000L);
        StringConverter.INSTANCE.setInto(i, "P1Y2M/2004-06-09", null);
        assertInterval(i,
                dt(null, 2003, 4, 9, 0, 0, 0, 0),
                dt(null, 2004, 6, 9, 0, 0, 0, 0),
                ISOChronology.getInstance());
    }

    public void testSetIntoInterval_startInstantSlashEndInstant() {
        MutableInterval i = new MutableInterval(-1000L, 1000L);
        StringConverter.INSTANCE.setInto(i, "2003-08-09/2004-06-09", null);
        assertInterval(i,
                dt(null, 2003, 8, 9, 0, 0, 0, 0),
                dt(null, 2004, 6, 9, 0, 0, 0, 0),
                ISOChronology.getInstance());
    }

    public void testSetIntoInterval_respectsExplicitOffsetOnInstants() {
        MutableInterval i = new MutableInterval(-1000L, 1000L);
        StringConverter.INSTANCE.setInto(i, "2004-06-09T+06:00/P1Y2M", null);
        assertInterval(i,
                new DateTime(2004, 6, 9, 0, 0, 0, 0, SIX).withChronology(null),
                new DateTime(2005, 8, 9, 0, 0, 0, 0, SIX).withChronology(null),
                ISOChronology.getInstance());
    }

    public void testSetIntoInterval_respectsExplicitOffsetOnInstants_endAtOffset() {
        MutableInterval i = new MutableInterval(-1000L, 1000L);
        StringConverter.INSTANCE.setInto(i, "P1Y2M/2004-06-09T+06:00", null);
        assertInterval(i,
                new DateTime(2003, 4, 9, 0, 0, 0, 0, SIX).withChronology(null),
                new DateTime(2004, 6, 9, 0, 0, 0, 0, SIX).withChronology(null),
                ISOChronology.getInstance());
    }

    public void testSetIntoInterval_differentOffsetsForStartAndEnd() {
        MutableInterval i = new MutableInterval(-1000L, 1000L);
        StringConverter.INSTANCE.setInto(i, "2003-08-09T+06:00/2004-06-09T+07:00", null);
        assertInterval(i,
                new DateTime(2003, 8, 9, 0, 0, 0, 0, SIX).withChronology(null),
                new DateTime(2004, 6, 9, 0, 0, 0, 0, SEVEN).withChronology(null),
                ISOChronology.getInstance());
    }

    public void testSetIntoInterval_withNonIsoChronology() {
        MutableInterval i = new MutableInterval(-1000L, 1000L);
        StringConverter.INSTANCE.setInto(i, "2003-08-09/2004-06-09", BuddhistChronology.getInstance());
        assertInterval(i,
                new DateTime(2003, 8, 9, 0, 0, 0, 0, BuddhistChronology.getInstance()),
                new DateTime(2004, 6, 9, 0, 0, 0, 0, BuddhistChronology.getInstance()),
                BuddhistChronology.getInstance());
    }

    public void testSetIntoInterval_withNonIsoChronologyAndExplicitOffsets() {
        MutableInterval i = new MutableInterval(-1000L, 1000L);
        StringConverter.INSTANCE.setInto(i, "2003-08-09T+06:00/2004-06-09T+07:00", BuddhistChronology.getInstance(EIGHT));
        assertInterval(i,
                new DateTime(2003, 8, 9, 0, 0, 0, 0, BuddhistChronology.getInstance(SIX)).withZone(EIGHT),
                new DateTime(2004, 6, 9, 0, 0, 0, 0, BuddhistChronology.getInstance(SEVEN)).withZone(EIGHT),
                BuddhistChronology.getInstance(EIGHT));
    }

    public void testSetIntoInterval_invalidInputsThrowIAE() {
        final MutableInterval i1 = new MutableInterval(-1000L, 1000L);
        assertIllegalArgumentThrown(new ThrowingCallable() {
            public void call() {
                StringConverter.INSTANCE.setInto(i1, "", null);
            }
        });

        final MutableInterval i2 = new MutableInterval(-1000L, 1000L);
        assertIllegalArgumentThrown(new ThrowingCallable() {
            public void call() {
                StringConverter.INSTANCE.setInto(i2, "/", null);
            }
        });

        final MutableInterval i3 = new MutableInterval(-1000L, 1000L);
        assertIllegalArgumentThrown(new ThrowingCallable() {
            public void call() {
                StringConverter.INSTANCE.setInto(i3, "P1Y/", null);
            }
        });

        final MutableInterval i4 = new MutableInterval(-1000L, 1000L);
        assertIllegalArgumentThrown(new ThrowingCallable() {
            public void call() {
                StringConverter.INSTANCE.setInto(i4, "/P1Y", null);
            }
        });

        final MutableInterval i5 = new MutableInterval(-1000L, 1000L);
        assertIllegalArgumentThrown(new ThrowingCallable() {
            public void call() {
                StringConverter.INSTANCE.setInto(i5, "P1Y/P2Y", null);
            }
        });
    }

    // ---------------------------------------------------------------------
    // Misc
    // ---------------------------------------------------------------------
    public void testToString() {
        assertEquals("Converter[java.lang.String]", StringConverter.INSTANCE.toString());
    }

    // ---------------------------------------------------------------------
    // Helpers to reduce duplication and improve readability
    // ---------------------------------------------------------------------

    private static DateTime dt(DateTimeZone zone, int y, int M, int d, int h, int m, int s, int ms) {
        // If zone is null, use default chronology & zone
        return (zone == null)
                ? new DateTime(y, M, d, h, m, s, ms)
                : new DateTime(y, M, d, h, m, s, ms, zone);
    }

    private static void assertSameInstant(DateTime expected, long actualMillis) {
        assertEquals("Instant millis differ", expected.getMillis(), actualMillis);
    }

    private static void assertIntArrayEquals(int[] expected, int[] actual) {
        assertTrue("Expected: " + Arrays.toString(expected) + " but was: " + Arrays.toString(actual),
                Arrays.equals(expected, actual));
    }

    private static void assertDateTimeFields(DateTime dt, int y, int M, int d, int h, int m, int s, int ms, DateTimeZone zone) {
        assertEquals(y, dt.getYear());
        assertEquals(M, dt.getMonthOfYear());
        assertEquals(d, dt.getDayOfMonth());
        assertEquals(h, dt.getHourOfDay());
        assertEquals(m, dt.getMinuteOfHour());
        assertEquals(s, dt.getSecondOfMinute());
        assertEquals(ms, dt.getMillisOfSecond());
        assertEquals(zone, dt.getZone());
    }

    private static void assertPeriod(MutablePeriod p,
                                     int years, int months, int weeks, int days,
                                     int hours, int minutes, int seconds, int millis) {
        assertEquals(years, p.getYears());
        assertEquals(months, p.getMonths());
        assertEquals(weeks, p.getWeeks());
        assertEquals(days, p.getDays());
        assertEquals(hours, p.getHours());
        assertEquals(minutes, p.getMinutes());
        assertEquals(seconds, p.getSeconds());
        assertEquals(millis, p.getMillis());
    }

    private static void assertInterval(MutableInterval i, DateTime expectedStart, DateTime expectedEnd, Chronology expectedChrono) {
        assertEquals(expectedStart, i.getStart());
        assertEquals(expectedEnd, i.getEnd());
        assertEquals(expectedChrono, i.getChronology());
    }

    private interface ThrowingCallable {
        void call() throws Exception;
    }

    private static void assertIllegalArgumentThrown(ThrowingCallable c) {
        try {
            c.call();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // expected
        } catch (Exception other) {
            fail("Expected IllegalArgumentException but caught: " + other);
        }
    }
}