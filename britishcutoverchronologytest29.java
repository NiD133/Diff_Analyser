package org.threeten.extra.chrono;

import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_YEAR;
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.DAY_OF_YEAR;
import static java.time.temporal.ChronoField.ERA;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.PROLEPTIC_MONTH;
import static java.time.temporal.ChronoField.YEAR;
import static java.time.temporal.ChronoField.YEAR_OF_ERA;
import static java.time.temporal.ChronoUnit.CENTURIES;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.DECADES;
import static java.time.temporal.ChronoUnit.ERAS;
import static java.time.temporal.ChronoUnit.MILLENNIA;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.WEEKS;
import static java.time.temporal.ChronoUnit.YEARS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoPeriod;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Comprehensive tests for {@link BritishCutoverDate}.
 */
@DisplayName("BritishCutoverDate")
class BritishCutoverDateTest {

    //-----------------------------------------------------------------------
    // Test Data Providers
    //-----------------------------------------------------------------------

    static Stream<Arguments> data_samples() {
        return Stream.of(
            // { BritishCutoverDate, Corresponding ISO LocalDate }
            // Arguments.of(cutoverDate, isoDate)
            
            // Dates around the beginning of the era
            Arguments.of(BritishCutoverDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
            Arguments.of(BritishCutoverDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),

            // Dates around a Julian leap year (4)
            Arguments.of(BritishCutoverDate.of(4, 2, 29), LocalDate.of(4, 2, 27)),
            Arguments.of(BritishCutoverDate.of(4, 3, 1), LocalDate.of(4, 2, 28)),

            // Dates around a Julian leap year (100) that is not a Gregorian leap year
            Arguments.of(BritishCutoverDate.of(100, 2, 29), LocalDate.of(100, 2, 27)),
            Arguments.of(BritishCutoverDate.of(100, 3, 1), LocalDate.of(100, 2, 28)),

            // Dates in year 0
            Arguments.of(BritishCutoverDate.of(0, 12, 31), LocalDate.of(0, 12, 29)),

            // Dates around the original Gregorian cutover (not used in Britain, but tests Julian logic)
            Arguments.of(BritishCutoverDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),
            Arguments.of(BritishCutoverDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)),

            // Dates around the change of year from March 25th to Jan 1st (1751/1752)
            Arguments.of(BritishCutoverDate.of(1751, 12, 31), LocalDate.of(1752, 1, 11)),
            Arguments.of(BritishCutoverDate.of(1752, 1, 1), LocalDate.of(1752, 1, 12)),

            // Dates around the British cutover gap (Sept 3-13 1752 are skipped)
            Arguments.of(BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 13)), // Day before the gap
            Arguments.of(BritishCutoverDate.of(1752, 9, 3), LocalDate.of(1752, 9, 14)), // First day of the gap (leniently accepted)
            Arguments.of(BritishCutoverDate.of(1752, 9, 13), LocalDate.of(1752, 9, 24)),// Last day of the gap (leniently accepted)
            Arguments.of(BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 14)),// First day after the gap (now Gregorian)

            // Modern dates (post-cutover)
            Arguments.of(BritishCutoverDate.of(1945, 11, 12), LocalDate.of(1945, 11, 12)),
            Arguments.of(BritishCutoverDate.of(2012, 7, 6), LocalDate.of(2012, 7, 6))
        );
    }

    static Stream<Arguments> data_badDates() {
        return Stream.of(
            Arguments.of(1900, 0, 0),
            Arguments.of(1900, 13, 1),
            Arguments.of(1900, 1, 32),
            Arguments.of(1900, 2, 30), // Not a leap year
            Arguments.of(1899, 2, 29), // Not a leap year
            Arguments.of(1900, 4, 31)  // April has 30 days
        );
    }

    static Stream<Arguments> data_plus() {
        return Stream.of(
            // start, amount, unit, expected, isBidirectional
            Arguments.of(BritishCutoverDate.of(1752, 9, 2), 1L, DAYS, BritishCutoverDate.of(1752, 9, 14), true), // Crosses the gap
            Arguments.of(BritishCutoverDate.of(1752, 9, 14), -1L, DAYS, BritishCutoverDate.of(1752, 9, 2), true), // Crosses the gap backwards
            Arguments.of(BritishCutoverDate.of(2014, 5, 26), 8L, DAYS, BritishCutoverDate.of(2014, 6, 3), true),
            Arguments.of(BritishCutoverDate.of(1752, 9, 2), 1L, WEEKS, BritishCutoverDate.of(1752, 9, 20), true),
            Arguments.of(BritishCutoverDate.of(1752, 8, 12), 1L, MONTHS, BritishCutoverDate.of(1752, 9, 23), false), // Day lands in gap
            Arguments.of(BritishCutoverDate.of(2014, 5, 26), 3L, YEARS, BritishCutoverDate.of(2017, 5, 26), true),
            Arguments.of(BritishCutoverDate.of(2014, 5, 26), 3L, DECADES, BritishCutoverDate.of(2044, 5, 26), true),
            Arguments.of(BritishCutoverDate.of(2014, 5, 26), 3L, CENTURIES, BritishCutoverDate.of(2314, 5, 26), true),
            Arguments.of(BritishCutoverDate.of(2014, 5, 26), 1L, MILLENNIA, BritishCutoverDate.of(3014, 5, 26), true),
            Arguments.of(BritishCutoverDate.of(2014, 5, 26), -1L, ERAS, BritishCutoverDate.of(-2013, 5, 26), true)
        );
    }

    static Stream<Arguments> data_until() {
        return Stream.of(
            // start, end, unit, expectedAmount
            Arguments.of(BritishCutoverDate.of(1752, 9, 1), BritishCutoverDate.of(1752, 9, 14), DAYS, 2L), // Across the gap
            Arguments.of(BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 9, 14), DAYS, 1L), // From day before gap
            Arguments.of(BritishCutoverDate.of(2014, 5, 26), BritishCutoverDate.of(2014, 6, 1), DAYS, 6L),
            Arguments.of(BritishCutoverDate.of(1752, 9, 1), BritishCutoverDate.of(1752, 9, 19), WEEKS, 1L), // until() counts whole units
            Arguments.of(BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 10, 2), MONTHS, 1L),
            Arguments.of(BritishCutoverDate.of(2014, 5, 26), BritishCutoverDate.of(2015, 5, 26), YEARS, 1L),
            Arguments.of(BritishCutoverDate.of(2014, 5, 26), BritishCutoverDate.of(2024, 5, 26), DECADES, 1L),
            Arguments.of(BritishCutoverDate.of(2014, 5, 26), BritishCutoverDate.of(2114, 5, 26), CENTURIES, 1L),
            Arguments.of(BritishCutoverDate.of(2014, 5, 26), BritishCutoverDate.of(3014, 5, 26), MILLENNIA, 1L),
            Arguments.of(BritishCutoverDate.of(-2013, 5, 26), BritishCutoverDate.of(2014, 5, 26), ERAS, 1L)
        );
    }

    static Stream<Arguments> data_untilChronoPeriod() {
        return Stream.of(
            // start, end, expectedYears, expectedMonths, expectedDays
            Arguments.of(BritishCutoverDate.of(1752, 7, 2), BritishCutoverDate.of(1752, 7, 1), 0, 0, -1),
            Arguments.of(BritishCutoverDate.of(1752, 7, 2), BritishCutoverDate.of(1752, 7, 2), 0, 0, 0),
            // Period calculation across the 1752 cutover gap
            Arguments.of(BritishCutoverDate.of(1752, 8, 2), BritishCutoverDate.of(1752, 10, 2), 0, 2, 0),
            Arguments.of(BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 9, 14), 0, 0, 1),
            Arguments.of(BritishCutoverDate.of(1752, 9, 14), BritishCutoverDate.of(1752, 9, 2), 0, 0, -1)
        );
    }

    //-----------------------------------------------------------------------
    // Test Cases
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Factory Methods and Validation")
    class FactoryAndValidationTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#data_samples")
        void from_isoDate_isCorrect(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#data_samples")
        void chronology_dateFromTemporal_isCorrect(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.date(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#data_badDates")
        void of_withInvalidDateParts_throwsException(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(year, month, day));
        }
    }

    @Nested
    @DisplayName("Conversions and Epoch Day")
    class ConversionAndEpochDayTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#data_samples")
        void toLocalDate_isCorrect(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(cutoverDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#data_samples")
        void toEpochDay_isCorrect(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), cutoverDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#data_samples")
        void fromEpochDay_isCorrect(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }
    }

    @Nested
    @DisplayName("Date Properties")
    class DatePropertiesTests {
        @Test
        void lengthOfMonth_forCutoverMonth_isCorrect() {
            assertEquals(19, BritishCutoverDate.of(1752, 9, 1).lengthOfMonth());
        }

        @Test
        void lengthOfMonth_forLeapAndNonLeapFeb_isCorrect() {
            assertEquals(29, BritishCutoverDate.of(1700, 2, 1).lengthOfMonth(), "Julian leap year 1700");
            assertEquals(28, BritishCutoverDate.of(1800, 2, 1).lengthOfMonth(), "Gregorian non-leap year 1800");
            assertEquals(29, BritishCutoverDate.of(2000, 2, 1).lengthOfMonth(), "Gregorian leap year 2000");
        }

        @Test
        void lengthOfYear_forCutoverYear_isCorrect() {
            assertEquals(355, BritishCutoverDate.of(1752, 1, 1).lengthOfYear());
        }

        @Test
        void lengthOfYear_forLeapAndNonLeapYears_isCorrect() {
            assertEquals(366, BritishCutoverDate.of(1700, 1, 1).lengthOfYear(), "Julian leap year 1700");
            assertEquals(365, BritishCutoverDate.of(1800, 1, 1).lengthOfYear(), "Gregorian non-leap year 1800");
            assertEquals(366, BritishCutoverDate.of(2000, 1, 1).lengthOfYear(), "Gregorian leap year 2000");
        }

        @Test
        void range_forDayOfMonthInCutoverMonth_isCorrect() {
            // The valid days are 1-2 and 14-30, but range is defined to cover all valid values.
            assertEquals(ValueRange.of(1, 30), BritishCutoverDate.of(1752, 9, 1).range(DAY_OF_MONTH));
        }

        @Test
        void getLong_forFieldsOnDateInCutover_isCorrect() {
            BritishCutoverDate date = BritishCutoverDate.of(1752, 9, 14); // First day after gap
            assertEquals(4, date.getLong(DAY_OF_WEEK)); // Thursday
            assertEquals(14, date.getLong(DAY_OF_MONTH));
            assertEquals(246, date.getLong(DAY_OF_YEAR)); // Day of year is continuous
        }
    }

    @Nested
    @DisplayName("Date Arithmetic")
    class DateArithmeticTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#data_plus")
        void plus_addsAmountCorrectly(BritishCutoverDate start, long amount, TemporalUnit unit, BritishCutoverDate expected, boolean isBidi) {
            assertEquals(expected, start.plus(amount, unit),
                "plus() failed for " + start + " + " + amount + " " + unit);
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#data_plus")
        void minus_subtractsAmountCorrectly(BritishCutoverDate start, long amount, TemporalUnit unit, BritishCutoverDate expected, boolean isBidi) {
            if (isBidi) {
                assertEquals(start, expected.minus(amount, unit),
                    "minus() failed for " + expected + " - " + amount + " " + unit);
            }
        }
    }

    @Nested
    @DisplayName("Adjusters")
    class AdjusterTests {
        @Test
        void with_fieldAndValue_adjustsCorrectly() {
            // Adjusting a day within the cutover month to a day that is in the gap
            BritishCutoverDate start = BritishCutoverDate.of(1752, 9, 2);
            BritishCutoverDate expected = BritishCutoverDate.of(1752, 9, 14);
            assertEquals(expected, start.with(DAY_OF_MONTH, 3), "Adjusting to day 3 (in gap) should resolve to day 14");
        }

        @Test
        void with_lastDayOfMonth_forCutoverMonth_isCorrect() {
            BritishCutoverDate date = BritishCutoverDate.of(1752, 9, 2);
            BritishCutoverDate expected = BritishCutoverDate.of(1752, 9, 30);
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @Test
        void with_localDate_isCorrect() {
            BritishCutoverDate start = BritishCutoverDate.of(2012, 2, 23);
            LocalDate target = LocalDate.of(1752, 9, 14); // The first day after the gap
            BritishCutoverDate expected = BritishCutoverDate.of(1752, 9, 14);
            assertEquals(expected, start.with(target));
        }
    }

    @Nested
    @DisplayName("Period and Duration")
    class PeriodAndDurationTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#data_samples")
        void until_selfAndIsoEquivalent_isZero(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutoverDate.until(cutoverDate), "Period until self should be zero");
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutoverDate.until(isoDate), "Period until ISO equivalent should be zero");
            assertEquals(Period.ZERO, isoDate.until(cutoverDate), "Period from ISO equivalent should be zero");
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#data_until")
        void until_withTemporalUnit_calculatesCorrectly(BritishCutoverDate start, BritishCutoverDate end, TemporalUnit unit, long expected) {
            assertEquals(expected, start.until(end, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#data_untilChronoPeriod")
        void until_asChronoPeriod_isCorrect(BritishCutoverDate start, BritishCutoverDate end, int y, int m, int d) {
            ChronoPeriod expected = BritishCutoverChronology.INSTANCE.period(y, m, d);
            assertEquals(expected, start.until(end));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#data_untilChronoPeriod")
        void until_asChronoPeriod_isReversibleWithPlus(BritishCutoverDate start, BritishCutoverDate end, int y, int m, int d) {
            ChronoPeriod period = start.until(end);
            assertEquals(end, start.plus(period), "Adding the period back should result in the end date");
        }
    }

    @Nested
    @DisplayName("Formatting")
    class FormattingTests {
        @Test
        void toString_returnsCorrectFormat() {
            assertEquals("BritishCutover AD 2012-06-23", BritishCutoverDate.of(2012, 6, 23).toString());
            assertEquals("BritishCutover AD 1-01-01", BritishCutoverDate.of(1, 1, 1).toString());
        }
    }

    @Nested
    @DisplayName("Cross-Validation")
    class CrossValidationTests {
        @Test
        void crossCheckAgainstGregorianCalendarWithCutover() {
            // Setup a standard Java GregorianCalendar with the British cutover date.
            // This validates our implementation against the JDK's for a range of dates.
            Instant cutoverInstant = ZonedDateTime.of(1752, 9, 14, 0, 0, 0, 0, ZoneOffset.UTC).toInstant();
            GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
            calendar.setGregorianChange(Date.from(cutoverInstant));
            calendar.clear();
            calendar.set(1700, Calendar.JANUARY, 1);

            BritishCutoverDate cutoverDate = BritishCutoverDate.of(1700, 1, 1);
            BritishCutoverDate endDate = BritishCutoverDate.of(1800, 1, 1);

            while (cutoverDate.isBefore(endDate)) {
                String message = "Mismatch for date: " + cutoverDate;
                assertEquals(calendar.get(Calendar.YEAR), cutoverDate.get(YEAR_OF_ERA), message);
                assertEquals(calendar.get(Calendar.MONTH) + 1, cutoverDate.get(MONTH_OF_YEAR), message);
                assertEquals(calendar.get(Calendar.DAY_OF_MONTH), cutoverDate.get(DAY_OF_MONTH), message);
                assertEquals(calendar.toZonedDateTime().toLocalDate(), LocalDate.from(cutoverDate), message);

                // Advance both calendars by one day
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                cutoverDate = cutoverDate.plus(1, DAYS);
            }
        }
    }
}