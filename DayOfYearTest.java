package org.threeten.extra;

import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_YEAR;
import static java.time.temporal.ChronoField.AMPM_OF_DAY;
import static java.time.temporal.ChronoField.CLOCK_HOUR_OF_AMPM;
import static java.time.temporal.ChronoField.CLOCK_HOUR_OF_DAY;
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.DAY_OF_YEAR;
import static java.time.temporal.ChronoField.EPOCH_DAY;
import static java.time.temporal.ChronoField.ERA;
import static java.time.temporal.ChronoField.HOUR_OF_AMPM;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.INSTANT_SECONDS;
import static java.time.temporal.ChronoField.MICRO_OF_DAY;
import static java.time.temporal.ChronoField.MICRO_OF_SECOND;
import static java.time.temporal.ChronoField.MILLI_OF_DAY;
import static java.time.temporal.ChronoField.MILLI_OF_SECOND;
import static java.time.temporal.ChronoField.MINUTE_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.NANO_OF_DAY;
import static java.time.temporal.ChronoField.NANO_OF_SECOND;
import static java.time.temporal.ChronoField.OFFSET_SECONDS;
import static java.time.temporal.ChronoField.PROLEPTIC_MONTH;
import static java.time.temporal.ChronoField.SECOND_OF_DAY;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import static java.time.temporal.ChronoField.YEAR;
import static java.time.temporal.ChronoField.YEAR_OF_ERA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.testing.EqualsTester;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.chrono.IsoChronology;
import java.time.chrono.JapaneseDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.RetryingTest;

/**
 * Tests for DayOfYear.
 *
 * This suite uses helper methods to avoid repetition and clarify intent.
 * The field- and range-related tests explicitly verify supported vs unsupported fields.
 */
public class TestDayOfYear {

    // Common constants
    private static final Year YEAR_STANDARD = Year.of(2007); // non-leap
    private static final Year YEAR_LEAP = Year.of(2008);     // leap
    private static final int STANDARD_YEAR_LENGTH = 365;
    private static final int LEAP_YEAR_LENGTH = 366;
    private static final DayOfYear DOY_12 = DayOfYear.of(12);
    private static final ZoneId PARIS = ZoneId.of("Europe/Paris");

    /**
     * A TemporalField that delegates to DAY_OF_YEAR but advertises its own identity,
     * used to prove non-ChronoField fields are supported via isSupportedBy/rangeRefinedBy/getFrom/adjustInto.
     */
    private static class DayOfYearLikeField implements TemporalField {
        static final DayOfYearLikeField INSTANCE = new DayOfYearLikeField();

        @Override
        public TemporalUnit getBaseUnit() {
            return ChronoUnit.DAYS;
        }

        @Override
        public TemporalUnit getRangeUnit() {
            return ChronoUnit.YEARS;
        }

        @Override
        public ValueRange range() {
            return ValueRange.of(1, 365, 366);
        }

        @Override
        public boolean isDateBased() {
            return true;
        }

        @Override
        public boolean isTimeBased() {
            return false;
        }

        @Override
        public boolean isSupportedBy(TemporalAccessor temporal) {
            return temporal.isSupported(DAY_OF_YEAR);
        }

        @Override
        public ValueRange rangeRefinedBy(TemporalAccessor temporal) {
            return range();
        }

        @Override
        public long getFrom(TemporalAccessor temporal) {
            return temporal.getLong(DAY_OF_YEAR);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <R extends Temporal> R adjustInto(R temporal, long newValue) {
            return (R) temporal.with(DAY_OF_YEAR, newValue);
        }
    }

    // ---------------------------------- Type and serialization ----------------------------------

    @Test
    public void test_interfaces() {
        assertTrue(Serializable.class.isAssignableFrom(DayOfYear.class));
        assertTrue(Comparable.class.isAssignableFrom(DayOfYear.class));
        assertTrue(TemporalAdjuster.class.isAssignableFrom(DayOfYear.class));
        assertTrue(TemporalAccessor.class.isAssignableFrom(DayOfYear.class));
    }

    @Test
    public void test_serialization() throws IOException, ClassNotFoundException {
        DayOfYear original = DayOfYear.of(1);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(original);
        }

        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
            assertSame(original, ois.readObject()); // relies on singletons in cache
        }
    }

    // ---------------------------------- now(...) factories ----------------------------------

    @RetryingTest(100) // minimize flakiness around midnight rollover
    public void test_now_systemDefaultZone_matches_LocalDate_now() {
        assertEquals(LocalDate.now().getDayOfYear(), DayOfYear.now().getValue());
    }

    @RetryingTest(100)
    public void test_now_zone_matches_LocalDate_now_in_zone() {
        ZoneId tokyo = ZoneId.of("Asia/Tokyo");
        assertEquals(LocalDate.now(tokyo).getDayOfYear(), DayOfYear.now(tokyo).getValue());
    }

    @Test
    public void test_now_clock_nonLeapYear_wholeYear() {
        assertNowAcrossYear(LocalDate.of(2007, 1, 1), STANDARD_YEAR_LENGTH);
    }

    @Test
    public void test_now_clock_leapYear_wholeYear() {
        assertNowAcrossYear(LocalDate.of(2008, 1, 1), LEAP_YEAR_LENGTH);
    }

    private static void assertNowAcrossYear(LocalDate startOfYear, int yearLength) {
        LocalDate date = startOfYear;
        for (int i = 1; i <= yearLength; i++) {
            Clock clock = fixedClockAtStartOfDay(date, PARIS);
            assertEquals(i, DayOfYear.now(clock).getValue(), "Mismatch at " + date);
            date = date.plusDays(1);
        }
    }

    private static Clock fixedClockAtStartOfDay(LocalDate date, ZoneId zone) {
        Instant instant = date.atStartOfDay(zone).toInstant();
        return Clock.fixed(instant, zone);
    }

    // ---------------------------------- of(...) ----------------------------------

    @Test
    public void test_of_allValidValues_areSingletons() {
        for (int i = 1; i <= LEAP_YEAR_LENGTH; i++) {
            DayOfYear doy = DayOfYear.of(i);
            assertEquals(i, doy.getValue());
            assertSame(doy, DayOfYear.of(i)); // singleton cache
        }
    }

    @Test
    public void test_of_tooLow() {
        assertThrows(DateTimeException.class, () -> DayOfYear.of(0));
    }

    @Test
    public void test_of_tooHigh() {
        assertThrows(DateTimeException.class, () -> DayOfYear.of(367));
    }

    // ---------------------------------- from(TemporalAccessor) ----------------------------------

    @Test
    public void test_from_nonLeapYear_fullCycle_then_wraps() {
        LocalDate date = LocalDate.of(2007, 1, 1);
        for (int i = 1; i <= STANDARD_YEAR_LENGTH; i++) {
            assertEquals(i, DayOfYear.from(date).getValue(), "Mismatch at " + date);
            date = date.plusDays(1);
        }
        // first day of next year
        assertEquals(1, DayOfYear.from(date).getValue());
    }

    @Test
    public void test_from_leapYear_fullCycle() {
        LocalDate date = LocalDate.of(2008, 1, 1);
        for (int i = 1; i <= LEAP_YEAR_LENGTH; i++) {
            assertEquals(i, DayOfYear.from(date).getValue(), "Mismatch at " + date);
            date = date.plusDays(1);
        }
    }

    @Test
    public void test_from_with_DayOfYear_returns_same() {
        DayOfYear doy = DayOfYear.of(6);
        assertEquals(doy, DayOfYear.from(doy));
    }

    @Test
    public void test_from_nonIso_convertible_via_JapaneseDate() {
        LocalDate date = LocalDate.now();
        assertEquals(date.getDayOfYear(), DayOfYear.from(JapaneseDate.from(date)).getValue());
    }

    @Test
    public void test_from_noDerive_from_timeOnly() {
        assertThrows(DateTimeException.class, () -> DayOfYear.from(LocalTime.NOON));
    }

    @Test
    public void test_from_null_temporal() {
        assertThrows(NullPointerException.class, () -> DayOfYear.from((TemporalAccessor) null));
    }

    @Test
    public void test_from_via_Formatter_parse_methodReference() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("D");
        assertEquals(DayOfYear.of(76), formatter.parse("76", DayOfYear::from));
    }

    // ---------------------------------- isSupported/range/get/getLong ----------------------------------

    @Test
    public void test_isSupported_supportedAndUnsupportedFields() {
        // Supported
        assertEquals(true, DOY_12.isSupported(DAY_OF_YEAR));
        assertEquals(true, DOY_12.isSupported(DayOfYearLikeField.INSTANCE));

        // Null
        assertEquals(false, DOY_12.isSupported((TemporalField) null));

        // Unsupported chrono fields
        TemporalField[] unsupported = new TemporalField[] {
            NANO_OF_SECOND, NANO_OF_DAY, MICRO_OF_SECOND, MICRO_OF_DAY, MILLI_OF_SECOND, MILLI_OF_DAY,
            SECOND_OF_MINUTE, SECOND_OF_DAY, MINUTE_OF_HOUR, MINUTE_OF_DAY, HOUR_OF_AMPM, CLOCK_HOUR_OF_AMPM,
            HOUR_OF_DAY, CLOCK_HOUR_OF_DAY, AMPM_OF_DAY, DAY_OF_WEEK, ALIGNED_DAY_OF_WEEK_IN_MONTH,
            ALIGNED_DAY_OF_WEEK_IN_YEAR, DAY_OF_MONTH, EPOCH_DAY, ALIGNED_WEEK_OF_MONTH, ALIGNED_WEEK_OF_YEAR,
            MONTH_OF_YEAR, PROLEPTIC_MONTH, YEAR_OF_ERA, YEAR, ERA, INSTANT_SECONDS, OFFSET_SECONDS
        };
        for (TemporalField f : unsupported) {
            assertEquals(false, DOY_12.isSupported(f), "Field should be unsupported: " + f);
        }
    }

    @Test
    public void test_range_supported() {
        assertEquals(DAY_OF_YEAR.range(), DOY_12.range(DAY_OF_YEAR));
    }

    @Test
    public void test_range_invalidField() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> DOY_12.range(MONTH_OF_YEAR));
    }

    @Test
    public void test_range_null() {
        assertThrows(NullPointerException.class, () -> DOY_12.range((TemporalField) null));
    }

    @Test
    public void test_get_supported() {
        assertEquals(12, DOY_12.get(DAY_OF_YEAR));
    }

    @Test
    public void test_get_invalidField() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> DOY_12.get(MONTH_OF_YEAR));
    }

    @Test
    public void test_get_null() {
        assertThrows(NullPointerException.class, () -> DOY_12.get((TemporalField) null));
    }

    @Test
    public void test_getLong_supported() {
        assertEquals(12L, DOY_12.getLong(DAY_OF_YEAR));
    }

    @Test
    public void test_getLong_derivedField() {
        assertEquals(12L, DOY_12.getLong(DayOfYearLikeField.INSTANCE));
    }

    @Test
    public void test_getLong_invalidField() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> DOY_12.getLong(MONTH_OF_YEAR));
    }

    @Test
    public void test_getLong_invalidField2() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> DOY_12.getLong(IsoFields.DAY_OF_QUARTER));
    }

    @Test
    public void test_getLong_null() {
        assertThrows(NullPointerException.class, () -> DOY_12.getLong((TemporalField) null));
    }

    // ---------------------------------- isValidYear ----------------------------------

    @Test
    public void test_isValidYear_day366_onlyLeapYears() {
        DayOfYear day366 = DayOfYear.of(366);
        assertEquals(false, day366.isValidYear(2011));
        assertEquals(true,  day366.isValidYear(2012));
        assertEquals(false, day366.isValidYear(2013));
    }

    @Test
    public void test_isValidYear_day365_allYears() {
        DayOfYear day365 = DayOfYear.of(365);
        assertEquals(true, day365.isValidYear(2011));
        assertEquals(true, day365.isValidYear(2012));
        assertEquals(true, day365.isValidYear(2013));
    }

    // ---------------------------------- query ----------------------------------

    @Test
    public void test_query_commonQueries() {
        assertEquals(IsoChronology.INSTANCE, DOY_12.query(TemporalQueries.chronology()));
        assertEquals(null, DOY_12.query(TemporalQueries.localDate()));
        assertEquals(null, DOY_12.query(TemporalQueries.localTime()));
        assertEquals(null, DOY_12.query(TemporalQueries.offset()));
        assertEquals(null, DOY_12.query(TemporalQueries.precision()));
        assertEquals(null, DOY_12.query(TemporalQueries.zone()));
        assertEquals(null, DOY_12.query(TemporalQueries.zoneId()));
    }

    // ---------------------------------- adjustInto ----------------------------------

    @Nested
    class AdjustIntoNonLeapYear {
        @Test
        public void fromStartOfYear() {
            assertAdjustIntoForYear(LocalDate.of(2007, 1, 1), STANDARD_YEAR_LENGTH);
        }

        @Test
        public void fromEndOfYear() {
            assertAdjustIntoForYear(LocalDate.of(2007, 12, 31), STANDARD_YEAR_LENGTH);
        }

        @Test
        public void fromStartOfYear_day366_throws() {
            DayOfYear day366 = DayOfYear.of(LEAP_YEAR_LENGTH);
            assertThrows(DateTimeException.class, () -> day366.adjustInto(LocalDate.of(2007, 1, 1)));
        }

        @Test
        public void fromEndOfYear_day366_throws() {
            DayOfYear day366 = DayOfYear.of(LEAP_YEAR_LENGTH);
            assertThrows(DateTimeException.class, () -> day366.adjustInto(LocalDate.of(2007, 12, 31)));
        }
    }

    @Nested
    class AdjustIntoLeapYear {
        @Test
        public void fromStartOfYear() {
            assertAdjustIntoForYear(LocalDate.of(2008, 1, 1), LEAP_YEAR_LENGTH);
        }

        @Test
        public void fromEndOfYear() {
            assertAdjustIntoForYear(LocalDate.of(2008, 12, 31), LEAP_YEAR_LENGTH);
        }
    }

    @Test
    public void test_adjustInto_nonIso_throws() {
        assertThrows(DateTimeException.class, () -> DOY_12.adjustInto(JapaneseDate.now()));
    }

    @Test
    public void test_adjustInto_null_temporal() {
        assertThrows(NullPointerException.class, () -> DOY_12.adjustInto((Temporal) null));
    }

    private static void assertAdjustIntoForYear(LocalDate baseDate, int yearLength) {
        // Regardless of baseDate being Jan 1 or Dec 31, the adjusted date is based on the base's year
        LocalDate expected = LocalDate.of(baseDate.getYear(), 1, 1);
        for (int i = 1; i <= yearLength; i++) {
            DayOfYear doy = DayOfYear.of(i);
            assertEquals(expected, doy.adjustInto(baseDate), "Mismatch at day " + i + " from base " + baseDate);
            expected = expected.plusDays(1);
        }
    }

    // ---------------------------------- atYear(Year) and atYear(int) ----------------------------------

    @Nested
    class AtYearYear {
        @Test
        public void nonLeapYear_allDays() {
            assertAtYear(YEAR_STANDARD, STANDARD_YEAR_LENGTH);
        }

        @Test
        public void nonLeapYear_day366_throws() {
            assertThrows(DateTimeException.class, () -> DayOfYear.of(LEAP_YEAR_LENGTH).atYear(YEAR_STANDARD));
        }

        @Test
        public void leapYear_allDays() {
            assertAtYear(YEAR_LEAP, LEAP_YEAR_LENGTH);
        }

        @Test
        public void nullYear_throws() {
            assertThrows(NullPointerException.class, () -> DOY_12.atYear((Year) null));
        }
    }

    @Nested
    class AtYearInt {
        @Test
        public void nonLeapYear_allDays() {
            assertAtYear(2007, STANDARD_YEAR_LENGTH);
        }

        @Test
        public void nonLeapYear_day366_throws() {
            assertThrows(DateTimeException.class, () -> DayOfYear.of(LEAP_YEAR_LENGTH).atYear(2007));
        }

        @Test
        public void leapYear_allDays() {
            assertAtYear(2008, LEAP_YEAR_LENGTH);
        }

        @Test
        public void invalidYear_throws() {
            assertThrows(DateTimeException.class, () -> DOY_12.atYear(Year.MIN_VALUE - 1));
        }
    }

    private static void assertAtYear(Year year, int length) {
        LocalDate expected = LocalDate.of(year.getValue(), 1, 1);
        for (int i = 1; i <= length; i++) {
            assertEquals(expected, DayOfYear.of(i).atYear(year), "Mismatch at day " + i + " for year " + year);
            expected = expected.plusDays(1);
        }
    }

    private static void assertAtYear(int year, int length) {
        LocalDate expected = LocalDate.of(year, 1, 1);
        for (int i = 1; i <= length; i++) {
            assertEquals(expected, DayOfYear.of(i).atYear(year), "Mismatch at day " + i + " for year " + year);
            expected = expected.plusDays(1);
        }
    }

    // ---------------------------------- compareTo / equals / hashCode / toString ----------------------------------

    @Test
    public void test_compareTo_allPairs() {
        for (int i = 1; i <= LEAP_YEAR_LENGTH; i++) {
            DayOfYear a = DayOfYear.of(i);
            for (int j = 1; j <= LEAP_YEAR_LENGTH; j++) {
                DayOfYear b = DayOfYear.of(j);
                int cmp = Integer.compare(i, j);
                if (cmp < 0) {
                    assertTrue(a.compareTo(b) < 0);
                    assertTrue(b.compareTo(a) > 0);
                } else if (cmp > 0) {
                    assertTrue(a.compareTo(b) > 0);
                    assertTrue(b.compareTo(a) < 0);
                } else {
                    assertEquals(0, a.compareTo(b));
                    assertEquals(0, b.compareTo(a));
                }
            }
        }
    }

    @Test
    public void test_compareTo_null_throws() {
        DayOfYear doy = null;
        DayOfYear test = DayOfYear.of(1);
        assertThrows(NullPointerException.class, () -> test.compareTo(doy));
    }

    @Test
    public void test_equals_and_hashCode_allValues() {
        EqualsTester equalsTester = new EqualsTester();
        for (int i = 1; i <= LEAP_YEAR_LENGTH; i++) {
            equalsTester.addEqualityGroup(DayOfYear.of(i), DayOfYear.of(i));
        }
        equalsTester.testEquals();
    }

    @Test
    public void test_toString_allValues() {
        for (int i = 1; i <= LEAP_YEAR_LENGTH; i++) {
            assertEquals("DayOfYear:" + i, DayOfYear.of(i).toString());
        }
    }
}