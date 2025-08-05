/*
 * Copyright (c) 2007-present, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * The original license header has been removed for brevity in this improved version.
 * The improved code is based on the original test under the same license terms.
 */
package org.threeten.extra.chrono;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Chronology;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import static java.time.temporal.ChronoField.*;
import static java.time.temporal.ChronoUnit.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A comprehensive test suite for the BritishCutoverChronology.
 *
 * This suite is organized into nested classes to group tests by functionality,
 * improving readability and maintainability.
 */
@DisplayName("BritishCutoverChronology Tests")
public class TestBritishCutoverChronology {

    private static final BritishCutoverChronology CHRONO = BritishCutoverChronology.INSTANCE;

    //-----------------------------------------------------------------------
    // Test data providers
    //-----------------------------------------------------------------------

    static Object[][] provideBritishAndIsoDatePairs() {
        return new Object[][] {
                // Standard Julian to ISO conversions
                {BritishCutoverDate.of(1, 1, 1), LocalDate.of(0, 12, 30)},
                {BritishCutoverDate.of(1, 1, 3), LocalDate.of(1, 1, 1)},
                {BritishCutoverDate.of(4, 3, 2), LocalDate.of(4, 2, 29)}, // Julian leap year

                // Pre-cutover dates close to the transition
                {BritishCutoverDate.of(1752, 9, 1), LocalDate.of(1752, 9, 12)},
                {BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 13)},

                // Dates during the cutover gap (leniently handled)
                {BritishCutoverDate.of(1752, 9, 3), LocalDate.of(1752, 9, 14)},
                {BritishCutoverDate.of(1752, 9, 13), LocalDate.of(1752, 9, 24)},

                // Post-cutover dates (equivalent to ISO)
                {BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 14)},
                {BritishCutoverDate.of(1945, 11, 12), LocalDate.of(1945, 11, 12)},
                {BritishCutoverDate.of(2012, 7, 6), LocalDate.of(2012, 7, 6)},
        };
    }

    static Object[][] provideInvalidDateComponents() {
        return new Object[][] {
                {1900, 0, 1},   // Invalid month
                {1900, 13, 1},  // Invalid month
                {1900, 1, 0},   // Invalid day
                {1900, 1, 32},  // Invalid day
                {1900, 2, 30},  // Invalid day for non-leap February
                {1899, 2, 29},  // Invalid day for Julian non-leap February
                {1900, 4, 31},  // Invalid day for April
        };
    }

    @Nested
    @DisplayName("Factory and Conversion Tests")
    class FactoryAndConversionTests {

        @Test
        @DisplayName("Chronology.of('BritishCutover') should return the singleton instance")
        void test_chronology_of_name() {
            Chronology chrono = Chronology.of("BritishCutover");
            assertNotNull(chrono);
            assertEquals(CHRONO, chrono);
            assertEquals("BritishCutover", chrono.getId());
            assertEquals(null, chrono.getCalendarType());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestBritishCutoverChronology#provideBritishAndIsoDatePairs")
        @DisplayName("LocalDate.from(cutoverDate) should convert correctly")
        void test_toLocalDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(cutoverDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestBritishCutoverChronology#provideBritishAndIsoDatePairs")
        @DisplayName("BritishCutoverDate.from(isoDate) should convert correctly")
        void test_fromLocalDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestBritishCutoverChronology#provideBritishAndIsoDatePairs")
        @DisplayName("chronology.dateEpochDay() should create the correct date")
        void test_dateFromEpochDay(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, CHRONO.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestBritishCutoverChronology#provideBritishAndIsoDatePairs")
        @DisplayName("cutoverDate.toEpochDay() should match ISO epoch day")
        void test_toEpochDay(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), cutoverDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestBritishCutoverChronology#provideBritishAndIsoDatePairs")
        @DisplayName("chronology.date(temporal) should create the correct date from an ISO date")
        void test_dateFromTemporal(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, CHRONO.date(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestBritishCutoverChronology#provideInvalidDateComponents")
        @DisplayName("of(year, month, day) should throw DateTimeException for invalid dates")
        void test_of_throwsForInvalidDate(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(year, month, day));
        }

        @Test
        @DisplayName("dateYearDay() should throw DateTimeException for invalid day-of-year")
        void test_dateYearDay_throwsForInvalidDay() {
            assertThrows(DateTimeException.class, () -> CHRONO.dateYearDay(2001, 366));
        }
    }

    @Nested
    @DisplayName("Date Property Tests")
    class DatePropertyTests {

        @Test
        @DisplayName("isLeapYear should follow Julian rules before 1752 and Gregorian after")
        void test_isLeapYear() {
            // Julian leap years, Gregorian non-leap
            assertTrue(CHRONO.isLeapYear(1700));
            // Gregorian leap year
            assertTrue(CHRONO.isLeapYear(2000));
            // Gregorian non-leap year
            assertEquals(false, CHRONO.isLeapYear(1900));
            // Standard leap year
            assertTrue(CHRONO.isLeapYear(2004));
            // Standard non-leap year
            assertEquals(false, CHRONO.isLeapYear(2003));
        }

        @Test
        @DisplayName("lengthOfMonth should be correct for Julian, Gregorian, and cutover months")
        void test_lengthOfMonth() {
            // Julian leap February
            assertEquals(29, BritishCutoverDate.of(1700, 2, 1).lengthOfMonth());
            // Gregorian non-leap February
            assertEquals(28, BritishCutoverDate.of(1800, 2, 1).lengthOfMonth());
            // Cutover month of September 1752
            assertEquals(19, BritishCutoverDate.of(1752, 9, 1).lengthOfMonth());
            // Standard month
            assertEquals(31, BritishCutoverDate.of(2012, 1, 1).lengthOfMonth());
        }

        @Test
        @DisplayName("lengthOfYear should be correct for Julian, Gregorian, and the cutover year")
        void test_lengthOfYear() {
            // Julian leap year
            assertEquals(366, BritishCutoverDate.of(1700, 1, 1).lengthOfYear());
            // Cutover year (366 - 11 days)
            assertEquals(355, BritishCutoverDate.of(1752, 1, 1).lengthOfYear());
            // Gregorian non-leap year
            assertEquals(365, BritishCutoverDate.of(1900, 1, 1).lengthOfYear());
            // Standard leap year
            assertEquals(366, BritishCutoverDate.of(2000, 1, 1).lengthOfYear());
        }

        @Test
        @DisplayName("range(field) should return correct value ranges")
        void test_range() {
            assertEquals(ValueRange.of(1, 7), CHRONO.range(DAY_OF_WEEK));
            assertEquals(ValueRange.of(1, 28, 31), CHRONO.range(DAY_OF_MONTH));
            assertEquals(ValueRange.of(1, 355, 366), CHRONO.range(DAY_OF_YEAR));
            assertEquals(ValueRange.of(1, 12), CHRONO.range(MONTH_OF_YEAR));
        }

        @Test
        @DisplayName("getLong(field) should return correct values for various fields")
        void test_getLong() {
            BritishCutoverDate preCutover = BritishCutoverDate.of(1752, 9, 2);
            assertEquals(3, preCutover.getLong(DAY_OF_WEEK)); // Wednesday
            assertEquals(246, preCutover.getLong(DAY_OF_YEAR));

            BritishCutoverDate postCutover = BritishCutoverDate.of(1752, 9, 14);
            assertEquals(4, postCutover.getLong(DAY_OF_WEEK)); // Thursday
            assertEquals(247, postCutover.getLong(DAY_OF_YEAR));

            BritishCutoverDate modernDate = BritishCutoverDate.of(2014, 5, 26);
            assertEquals(1, modernDate.getLong(DAY_OF_WEEK)); // Monday
            assertEquals(2014, modernDate.getLong(YEAR));
            assertEquals(1, modernDate.getLong(ERA));
        }
    }

    @Nested
    @DisplayName("Date Manipulation Tests")
    class DateManipulationTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.TestBritishCutoverChronology#provideBritishAndIsoDatePairs")
        @DisplayName("plus(days) should correctly add days across the cutover")
        void test_plusDays(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate.plusDays(1), LocalDate.from(cutoverDate.plus(1, DAYS)));
            assertEquals(isoDate.plusDays(-1), LocalDate.from(cutoverDate.plus(-1, DAYS)));
            assertEquals(isoDate.plusDays(35), LocalDate.from(cutoverDate.plus(35, DAYS)));
        }

        @Test
        @DisplayName("plus(months) should correctly add months across the cutover")
        void test_plusMonths() {
            // Crosses the gap
            BritishCutoverDate start = BritishCutoverDate.of(1752, 8, 12);
            BritishCutoverDate expected = BritishCutoverDate.of(1752, 9, 23);
            assertEquals(expected, start.plus(1, MONTHS));
        }

        @Test
        @DisplayName("with(field, value) should set fields correctly, handling the cutover")
        void test_with() {
            BritishCutoverDate date = BritishCutoverDate.of(1752, 9, 2);
            // Setting day of week to Thursday lands on the 14th, after the gap
            assertEquals(BritishCutoverDate.of(1752, 9, 14), date.with(DAY_OF_WEEK, 4));
            // Setting day of month into the gap is handled leniently
            assertEquals(BritishCutoverDate.of(1752, 9, 14), date.with(DAY_OF_MONTH, 3));
        }

        @Test
        @DisplayName("with(TemporalAdjuster) should work correctly")
        void test_with_adjuster() {
            BritishCutoverDate date = BritishCutoverDate.of(1752, 9, 2);
            // The last day of Sep 1752 is the 30th.
            assertEquals(BritishCutoverDate.of(1752, 9, 30), date.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    @Nested
    @DisplayName("Period and Duration (until) Tests")
    class UntilTests {

        @Test
        @DisplayName("until(endDate, DAYS) should correctly count days across the cutover")
        void test_until_daysAcrossCutover() {
            BritishCutoverDate start = BritishCutoverDate.of(1752, 9, 2);
            BritishCutoverDate end = BritishCutoverDate.of(1752, 9, 14);
            // There is only 1 day between Sep 2 and Sep 14 in this calendar
            assertEquals(1, start.until(end, DAYS));
            assertEquals(-1, end.until(start, DAYS));
        }

        @Test
        @DisplayName("until(endDate, MONTHS) should correctly count months")
        void test_until_months() {
            BritishCutoverDate start = BritishCutoverDate.of(2014, 5, 26);
            BritishCutoverDate end = BritishCutoverDate.of(2014, 8, 26);
            assertEquals(3, start.until(end, MONTHS));
        }

        @Test
        @DisplayName("until(endDate) should produce the correct ChronoPeriod")
        void test_until_period() {
            // This test case demonstrates the complex period calculation across the cutover.
            // Start: July 3, 1752. End: Sep 14, 1752.
            // Expected: 2 months and 0 days.
            // Logic: July 3 + 2 months = Sep 3. In this calendar, Sep 3 is the same as Sep 14.
            BritishCutoverDate start = BritishCutoverDate.of(1752, 7, 3);
            BritishCutoverDate end = BritishCutoverDate.of(1752, 9, 14);
            ChronoPeriod period = start.until(end);

            assertEquals(CHRONO.period(0, 2, 0), period);
            // Verify that adding the period back gives the end date
            assertEquals(end, start.plus(period));
        }
    }

    @Nested
    @DisplayName("Era-related Tests")
    class EraTests {

        @Test
        @DisplayName("eraOf() and eras() should return correct Julian eras")
        void test_eraOf_and_eras() {
            assertEquals(JulianEra.AD, CHRONO.eraOf(1));
            assertEquals(JulianEra.BC, CHRONO.eraOf(0));
            assertThrows(DateTimeException.class, () -> CHRONO.eraOf(2));

            List<Era> eras = CHRONO.eras();
            assertEquals(2, eras.size());
            assertTrue(eras.contains(JulianEra.BC));
            assertTrue(eras.contains(JulianEra.AD));
        }

        @Test
        @DisplayName("prolepticYear should correctly handle AD and BC eras")
        void test_prolepticYear() {
            assertEquals(1, CHRONO.prolepticYear(JulianEra.AD, 1));
            assertEquals(0, CHRONO.prolepticYear(JulianEra.BC, 1));
            assertEquals(-1, CHRONO.prolepticYear(JulianEra.BC, 2));
        }

        @Test
        @DisplayName("date(era, year, month, day) should create correct dates")
        void test_date_fromEra() {
            BritishCutoverDate adDate = CHRONO.date(JulianEra.AD, 2012, 6, 23);
            assertEquals(BritishCutoverDate.of(2012, 6, 23), adDate);

            BritishCutoverDate bcDate = CHRONO.date(JulianEra.BC, 1, 1, 1);
            assertEquals(BritishCutoverDate.of(0, 1, 1), bcDate);
        }
    }

    @Nested
    @DisplayName("Interoperability Tests")
    class InteroperabilityTests {

        @Test
        @DisplayName("atTime(localTime) should create a valid ChronoLocalDateTime")
        void test_atTime() {
            BritishCutoverDate date = BritishCutoverDate.of(2014, 10, 12);
            LocalTime time = LocalTime.of(12, 30);
            ChronoLocalDateTime<BritishCutoverDate> dateTime = date.atTime(time);

            assertEquals(date, dateTime.toLocalDate());
            assertEquals(time, dateTime.toLocalTime());
        }

        /**
         * This is a vital integration test that verifies the chronology's behavior against
         * Java's own GregorianCalendar, which is configured with the same cutover date.
         * It iterates day-by-day through a period that includes the cutover.
         */
        @Test
        @DisplayName("should align with Java's GregorianCalendar across the cutover")
        void test_crossCheckWithGregorianCalendar() {
            BritishCutoverDate testDate = BritishCutoverDate.of(1752, 8, 1);
            BritishCutoverDate endDate = BritishCutoverDate.of(1752, 10, 1);

            // Configure Java's calendar to match the British cutover
            Instant cutoverInstant = ZonedDateTime.of(1752, 9, 14, 0, 0, 0, 0, ZoneOffset.UTC).toInstant();
            GregorianCalendar gcal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
            gcal.setGregorianChange(Date.from(cutoverInstant));
            gcal.clear();
            gcal.set(1752, Calendar.AUGUST, 1);

            while (testDate.isBefore(endDate)) {
                String message = "Mismatch on date: " + testDate;
                assertEquals(gcal.get(Calendar.YEAR), testDate.get(YEAR_OF_ERA), message);
                assertEquals(gcal.get(Calendar.MONTH) + 1, testDate.get(MONTH_OF_YEAR), message);
                assertEquals(gcal.get(Calendar.DAY_OF_MONTH), testDate.get(DAY_OF_MONTH), message);
                assertEquals(gcal.toZonedDateTime().toLocalDate(), LocalDate.from(testDate), message);

                // Advance both calendars by one day
                gcal.add(Calendar.DAY_OF_MONTH, 1);
                testDate = testDate.plus(1, DAYS);
            }
        }
    }

    @Nested
    @DisplayName("Object Method Tests (equals, hashCode, toString)")
    class ObjectMethodTests {

        @Test
        @DisplayName("equals() and hashCode() should adhere to their contracts")
        void test_equalsAndHashCode() {
            new EqualsTester()
                    .addEqualityGroup(BritishCutoverDate.of(2000, 1, 3), BritishCutoverDate.of(2000, 1, 3))
                    .addEqualityGroup(BritishCutoverDate.of(2000, 1, 4))
                    .addEqualityGroup(BritishCutoverDate.of(2000, 2, 3))
                    .addEqualityGroup(BritishCutoverDate.of(2001, 1, 3))
                    .testEquals();
        }

        @Test
        @DisplayName("toString() should return a descriptive string")
        void test_toString() {
            assertEquals("BritishCutover AD 2012-06-23", BritishCutoverDate.of(2012, 6, 23).toString());
            assertEquals("BritishCutover BC 1-01-01", BritishCutoverDate.of(0, 1, 1).toString());
        }
    }
}