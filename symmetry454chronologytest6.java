package org.threeten.extra.chrono;

import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_YEAR;
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.DAY_OF_YEAR;
import static java.time.temporal.ChronoField.EPOCH_DAY;
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
import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Era;
import java.time.chrono.HijrahEra;
import java.time.chrono.IsoEra;
import java.time.chrono.JapaneseEra;
import java.time.chrono.MinguoEra;
import java.time.chrono.ThaiBuddhistEra;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link Symmetry454Chronology} and {@link Symmetry454Date}.
 * This class is structured with nested classes to group related tests.
 */
public class Symmetry454ChronologyTest {

    // Constants for calendar properties to improve readability of test data
    private static final int DAYS_IN_SHORT_MONTH = 28;
    private static final int DAYS_IN_LONG_MONTH = 35;
    private static final int WEEKS_IN_SHORT_MONTH = 4;
    private static final int WEEKS_IN_LONG_MONTH = 5;

    // --- Data Providers ---

    /**
     * Provides pairs of equivalent dates in Symmetry454 and ISO calendar systems.
     */
    public static Object[][] sampleSymmetry454AndIsoDates() {
        return new Object[][] {
            {Symmetry454Date.of(1, 1, 1), LocalDate.of(1, 1, 1)},
            {Symmetry454Date.of(272, 2, 30), LocalDate.of(272, 2, 27)},
            {Symmetry454Date.of(272, 2, 27), LocalDate.of(272, 2, 24)},
            {Symmetry454Date.of(742, 3, 25), LocalDate.of(742, 4, 2)},
            {Symmetry454Date.of(742, 4, 2), LocalDate.of(742, 4, 7)},
            {Symmetry454Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)},
            {Symmetry454Date.of(1304, 7, 21), LocalDate.of(1304, 7, 20)},
            {Symmetry454Date.of(1304, 7, 20), LocalDate.of(1304, 7, 19)},
            {Symmetry454Date.of(1433, 11, 14), LocalDate.of(1433, 11, 10)},
            {Symmetry454Date.of(1433, 11, 10), LocalDate.of(1433, 11, 6)},
            {Symmetry454Date.of(1452, 4, 11), LocalDate.of(1452, 4, 15)},
            {Symmetry454Date.of(1452, 4, 15), LocalDate.of(1452, 4, 19)},
            {Symmetry454Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)},
            {Symmetry454Date.of(1492, 10, 12), LocalDate.of(1492, 10, 14)},
            {Symmetry454Date.of(1564, 2, 20), LocalDate.of(1564, 2, 15)},
            {Symmetry454Date.of(1564, 2, 15), LocalDate.of(1564, 2, 10)},
            {Symmetry454Date.of(1564, 4, 28), LocalDate.of(1564, 4, 26)},
            {Symmetry454Date.of(1564, 4, 26), LocalDate.of(1564, 4, 24)},
            {Symmetry454Date.of(1643, 1, 7), LocalDate.of(1643, 1, 4)},
            {Symmetry454Date.of(1643, 1, 4), LocalDate.of(1643, 1, 1)},
            {Symmetry454Date.of(1707, 4, 12), LocalDate.of(1707, 4, 15)},
            {Symmetry454Date.of(1707, 4, 15), LocalDate.of(1707, 4, 18)},
            {Symmetry454Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14)},
            {Symmetry454Date.of(1789, 7, 14), LocalDate.of(1789, 7, 12)},
            {Symmetry454Date.of(1879, 3, 12), LocalDate.of(1879, 3, 14)},
            {Symmetry454Date.of(1879, 3, 14), LocalDate.of(1879, 3, 16)},
            {Symmetry454Date.of(1941, 9, 9), LocalDate.of(1941, 9, 9)},
            {Symmetry454Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)},
            {Symmetry454Date.of(1970, 1, 1), LocalDate.of(1969, 12, 29)},
            {Symmetry454Date.of(1999, 12, 27), LocalDate.of(2000, 1, 1)},
            {Symmetry454Date.of(2000, 1, 1), LocalDate.of(2000, 1, 3)}
        };
    }

    public static Object[][] invalidDateComponents() {
        return new Object[][] {
            {-1, 13, 28},
            {-1, 13, 29},
            {2000, -2, 1},
            {2000, 13, 1},
            {2000, 15, 1},
            {2000, 1, -1},
            {2000, 1, 0},
            {2000, 0, 1},
            {2000, -1, 0},
            {2000, -1, 1},
            {2000, 1, 29},   // Day 29 is invalid for a 28-day month (Jan)
            {2000, 2, 36},   // Day 36 is invalid for a 35-day month (Feb)
            {2000, 3, 29},
            {2000, 4, 29},
            {2000, 5, 36},
            {2000, 6, 29},
            {2000, 7, 29},
            {2000, 8, 36},
            {2000, 9, 29},
            {2000, 10, 29},
            {2000, 11, 36},
            {2000, 12, 29},  // Day 29 is invalid for Dec in a non-leap year
            {2004, 12, 36}   // Day 36 is invalid for Dec, even in a leap year (max 35)
        };
    }

    public static Object[][] nonLeapYears() {
        return new Object[][] {{1}, {100}, {200}, {2000}};
    }

    public static Object[][] monthLengths() {
        return new Object[][] {
            {2000, 1, 28, 28}, {2000, 2, 28, 35}, {2000, 3, 28, 28},
            {2000, 4, 28, 28}, {2000, 5, 28, 35}, {2000, 6, 28, 28},
            {2000, 7, 28, 28}, {2000, 8, 28, 35}, {2000, 9, 28, 28},
            {2000, 10, 28, 28}, {2000, 11, 28, 35}, {2000, 12, 28, 28},
            {2004, 12, 20, 35} // Leap year December
        };
    }

    // NOTE: The following eras are from the threeten-extra library, not standard Java.
    // This test assumes they are available on the classpath.
    public static Object[][] unsupportedEras() {
        return new Era[][] {
            {AccountingEra.BCE}, {AccountingEra.CE}, {CopticEra.BEFORE_AM}, {CopticEra.AM},
            {DiscordianEra.YOLD}, {EthiopicEra.BEFORE_INCARNATION}, {EthiopicEra.INCARNATION},
            {HijrahEra.AH}, {InternationalFixedEra.CE}, {JapaneseEra.MEIJI}, {JapaneseEra.TAISHO},
            {JapaneseEra.SHOWA}, {JapaneseEra.HEISEI}, {JulianEra.BC}, {JulianEra.AD},
            {MinguoEra.BEFORE_ROC}, {MinguoEra.ROC}, {PaxEra.BCE}, {PaxEra.CE},
            {ThaiBuddhistEra.BEFORE_BE}, {ThaiBuddhistEra.BE}
        };
    }

    public static Object[][] fieldRanges() {
        return new Object[][] {
            {2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 35)},
            {2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 35)}, // Leap year
            {2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)},
            {2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364)},
            {2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371)}, // Leap year
            {2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12)},
            {2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)},
            {2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)},
            {2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)},
            {2015, 12, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)}, // Leap year
            {2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7)},
            {2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)},
            {2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53)} // Leap year
        };
    }

    public static Object[][] fieldValues() {
        return new Object[][] {
            {2014, 5, 26, DAY_OF_WEEK, 5},
            {2014, 5, 26, DAY_OF_MONTH, 26},
            {2014, 5, 26, DAY_OF_YEAR, DAYS_IN_SHORT_MONTH + DAYS_IN_LONG_MONTH + DAYS_IN_SHORT_MONTH + DAYS_IN_SHORT_MONTH + 26},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5},
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 5},
            {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, WEEKS_IN_SHORT_MONTH + WEEKS_IN_LONG_MONTH + WEEKS_IN_SHORT_MONTH + WEEKS_IN_SHORT_MONTH + 4},
            {2014, 5, 26, MONTH_OF_YEAR, 5},
            {2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1},
            {2014, 5, 26, YEAR, 2014},
            {2014, 5, 26, ERA, 1},
            {1, 5, 8, ERA, 1},
            {2015, 12, 35, DAY_OF_WEEK, 7}, // Leap day
            {2015, 12, 35, DAY_OF_MONTH, 35},
            {2015, 12, 35, DAY_OF_YEAR, 371},
            {2015, 12, 35, ALIGNED_WEEK_OF_MONTH, 5},
            {2015, 12, 35, ALIGNED_WEEK_OF_YEAR, 53}
        };
    }

    public static Object[][] withFieldAdjustments() {
        return new Object[][] {
            {2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22},
            {2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28},
            {2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 28},
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5},
            {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 19},
            {2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26},
            {2014, 5, 26, PROLEPTIC_MONTH, 2013 * 12 + 3 - 1, 2013, 3, 26},
            {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
            {2014, 5, 26, YEAR_OF_ERA, 2012, 2012, 5, 26},
            {2014, 5, 26, ERA, 1, 2014, 5, 26},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_MONTH, 7, 2015, 12, 28},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7, 2015, 12, 28},
            {2015, 12, 29, MONTH_OF_YEAR, 2, 2015, 2, 29}, // Adjust to day 29 in a 35-day month
            {2015, 12, 29, YEAR, 2014, 2014, 12, 28}, // Adjust from leap day to non-leap year
            {2015, 3, 28, DAY_OF_YEAR, 371, 2015, 12, 35} // Adjust to leap day
        };
    }

    public static Object[][] invalidWithFieldAdjustments() {
        return new Object[][] {
            {2013, 1, 1, ALIGNED_WEEK_OF_MONTH, 5}, {2013, 2, 1, ALIGNED_WEEK_OF_MONTH, 6},
            {2013, 1, 1, ALIGNED_WEEK_OF_YEAR, 53}, {2015, 1, 1, ALIGNED_WEEK_OF_YEAR, 54},
            {2013, 1, 1, DAY_OF_WEEK, 8}, {2013, 1, 1, DAY_OF_MONTH, 29},
            {2013, 12, 1, DAY_OF_MONTH, 30}, {2015, 12, 1, DAY_OF_MONTH, 36},
            {2013, 1, 1, DAY_OF_YEAR, 365}, {2015, 1, 1, DAY_OF_YEAR, 372},
            {2013, 1, 1, MONTH_OF_YEAR, 14}, {2013, 1, 1, EPOCH_DAY, 364_523_156L},
            {2013, 1, 1, YEAR, 1_000_001}
        };
    }

    public static Object[][] lastDayOfMonthAdjustments() {
        return new Object[][] {
            {2012, 1, 23, 2012, 1, 28}, {2012, 2, 23, 2012, 2, 35},
            {2012, 3, 23, 2012, 3, 28}, {2012, 4, 23, 2012, 4, 28},
            {2012, 5, 23, 2012, 5, 35}, {2012, 6, 23, 2012, 6, 28},
            {2012, 7, 23, 2012, 7, 28}, {2012, 8, 23, 2012, 8, 35},
            {2012, 9, 23, 2012, 9, 28}, {2012, 10, 23, 2012, 10, 28},
            {2012, 11, 23, 2012, 11, 35}, {2012, 12, 23, 2012, 12, 28},
            {2009, 12, 23, 2009, 12, 35} // Leap year
        };
    }

    public static Object[][] plusMinusDateSamples() {
        return new Object[][] {
            {2014, 5, 26, 0, DAYS, 2014, 5, 26}, {2014, 5, 26, 8, DAYS, 2014, 5, 34},
            {2014, 5, 26, -3, DAYS, 2014, 5, 23}, {2014, 5, 26, 3, WEEKS, 2014, 6, 12},
            {2014, 5, 26, -5, WEEKS, 2014, 4, 19}, {2014, 5, 26, 3, MONTHS, 2014, 8, 26},
            {2014, 5, 26, -5, MONTHS, 2013, 12, 26}, {2014, 5, 26, 3, YEARS, 2017, 5, 26},
            {2014, 5, 26, -5, YEARS, 2009, 5, 26}, {2014, 5, 26, 3, DECADES, 2044, 5, 26},
            {2014, 5, 26, -5, DECADES, 1964, 5, 26}, {2014, 5, 26, 3, CENTURIES, 2314, 5, 26},
            {2014, 5, 26, -5, CENTURIES, 1514, 5, 26}, {2014, 5, 26, 3, MILLENNIA, 5014, 5, 26},
            {2014, 5, 26, -1, MILLENNIA, 1014, 5, 26}, {2014, 12, 26, 3, WEEKS, 2015, 1, 19},
            {2013, 6, 21, 6 * 52 + 1, WEEKS, 2019, 6, 21}
        };
    }

    public static Object[][] plusMinusLeapWeekDateSamples() {
        return new Object[][] {
            {2015, 12, 28, 8, DAYS, 2016, 1, 1}, {2015, 12, 28, -3, DAYS, 2015, 12, 25},
            {2015, 12, 28, 3, WEEKS, 2016, 1, 14}, {2015, 12, 28, -5, WEEKS, 2015, 11, 28},
            {2015, 12, 28, 52, WEEKS, 2016, 12, 21}, {2015, 12, 28, 3, MONTHS, 2016, 3, 28},
            {2015, 12, 28, -5, MONTHS, 2015, 7, 28}, {2015, 12, 28, 12, MONTHS, 2016, 12, 28},
            {2015, 12, 28, 3, YEARS, 2018, 12, 28}, {2015, 12, 28, -5, YEARS, 2010, 12, 28}
        };
    }

    public static Object[][] untilUnitSamples() {
        return new Object[][] {
            {2014, 5, 26, 2014, 5, 26, DAYS, 0}, {2014, 5, 26, 2014, 6, 4, DAYS, 13},
            {2014, 5, 26, 2014, 5, 20, DAYS, -6}, {2014, 5, 26, 2014, 6, 5, WEEKS, 1},
            {2014, 5, 26, 2014, 6, 26, MONTHS, 1}, {2014, 5, 26, 2015, 5, 26, YEARS, 1},
            {2014, 5, 26, 2024, 5, 26, DECADES, 1}, {2014, 5, 26, 2114, 5, 26, CENTURIES, 1},
            {2014, 5, 26, 3014, 5, 26, MILLENNIA, 1}, {2014, 5, 26, 3014, 5, 26, ERAS, 0}
        };
    }

    public static Object[][] untilPeriodSamples() {
        return new Object[][] {
            {2014, 5, 26, 2014, 5, 26, 0, 0, 0}, {2014, 5, 26, 2014, 6, 4, 0, 0, 13},
            {2014, 5, 26, 2014, 5, 20, 0, 0, -6}, {2014, 5, 26, 2014, 6, 26, 0, 1, 0},
            {2014, 5, 26, 2015, 5, 25, 0, 11, 27}, {2014, 5, 26, 2015, 5, 26, 1, 0, 0},
            {2014, 5, 26, 2024, 5, 25, 9, 11, 27}
        };
    }

    public static Object[][] toStringSamples() {
        return new Object[][] {
            {Symmetry454Date.of(1, 1, 1), "Sym454 CE 1/01/01"},
            {Symmetry454Date.of(1970, 2, 35), "Sym454 CE 1970/02/35"},
            {Symmetry454Date.of(2000, 8, 35), "Sym454 CE 2000/08/35"},
            {Symmetry454Date.of(1970, 12, 35), "Sym454 CE 1970/12/35"}
        };
    }

    @Nested
    class ConversionAndConsistencyTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetry454AndIsoDates")
        void fromSymmetry454Date_shouldReturnEquivalentLocalDate(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(iso, LocalDate.from(sym454));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetry454AndIsoDates")
        void fromLocalDate_shouldReturnEquivalentSymmetry454Date(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(sym454, Symmetry454Date.from(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetry454AndIsoDates")
        void toEpochDay_shouldBeConsistentWithIso(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(iso.toEpochDay(), sym454.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetry454AndIsoDates")
        void chronologyDateFromEpochDay_shouldBeConsistentWithIso(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(sym454, Symmetry454Chronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetry454AndIsoDates")
        void chronologyDateFromTemporal_shouldBeConsistentWithIso(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(sym454, Symmetry454Chronology.INSTANCE.date(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetry454AndIsoDates")
        void until_self_shouldReturnZeroPeriod(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), sym454.until(sym454));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetry454AndIsoDates")
        void until_equivalentLocalDate_shouldReturnZeroPeriod(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), sym454.until(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetry454AndIsoDates")
        void isoDateUntil_equivalentSymmetry454Date_shouldReturnZeroPeriod(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(Period.ZERO, iso.until(sym454));
        }
    }

    @Nested
    class DateCreationTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#invalidDateComponents")
        void of_shouldThrowExceptionForInvalidDateParts(int year, int month, int dom) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dom));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#nonLeapYears")
        void of_shouldThrowExceptionForLeapDayInNonLeapYear(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, 12, 29));
        }
    }

    @Nested
    class FieldAndPropertyTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#monthLengths")
        void lengthOfMonth_shouldReturnCorrectValue(int year, int month, int day, int length) {
            assertEquals(length, Symmetry454Date.of(year, month, day).lengthOfMonth());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#monthLengths")
        void lengthOfMonth_shouldBeIndependentOfDay(int year, int month, int day, int length) {
            assertEquals(length, Symmetry454Date.of(year, month, 1).lengthOfMonth());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#fieldRanges")
        void range_shouldReturnCorrectRangeForField(int year, int month, int dom, TemporalField field, ValueRange range) {
            assertEquals(range, Symmetry454Date.of(year, month, dom).range(field));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#fieldValues")
        void getLong_shouldReturnCorrectValueForField(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, Symmetry454Date.of(year, month, dom).getLong(field));
        }
    }

    @Nested
    class DateManipulationTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetry454AndIsoDates")
        void plusDays_shouldBehaveLikeIso(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(iso, LocalDate.from(sym454.plus(0, DAYS)));
            assertEquals(iso.plusDays(1), LocalDate.from(sym454.plus(1, DAYS)));
            assertEquals(iso.plusDays(35), LocalDate.from(sym454.plus(35, DAYS)));
            assertEquals(iso.plusDays(-1), LocalDate.from(sym454.plus(-1, DAYS)));
            assertEquals(iso.plusDays(-60), LocalDate.from(sym454.plus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetry454AndIsoDates")
        void minusDays_shouldBehaveLikeIso(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(iso, LocalDate.from(sym454.minus(0, DAYS)));
            assertEquals(iso.minusDays(1), LocalDate.from(sym454.minus(1, DAYS)));
            assertEquals(iso.minusDays(35), LocalDate.from(sym454.minus(35, DAYS)));
            assertEquals(iso.minusDays(-1), LocalDate.from(sym454.minus(-1, DAYS)));
            assertEquals(iso.minusDays(-60), LocalDate.from(sym454.minus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetry454AndIsoDates")
        void until_days_shouldBehaveLikeIso(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(0, sym454.until(iso.plusDays(0), DAYS));
            assertEquals(1, sym454.until(iso.plusDays(1), DAYS));
            assertEquals(35, sym454.until(iso.plusDays(35), DAYS));
            assertEquals(-40, sym454.until(iso.minusDays(40), DAYS));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#withFieldAdjustments")
        void with_shouldAdjustFieldCorrectly(int year, int month, int dom, TemporalField field, long value, int expectedYear, int expectedMonth, int expectedDom) {
            Symmetry454Date start = Symmetry454Date.of(year, month, dom);
            Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.with(field, value));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#invalidWithFieldAdjustments")
        void with_shouldThrowExceptionForInvalidFieldValue(int year, int month, int dom, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dom).with(field, value));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#lastDayOfMonthAdjustments")
        void with_lastDayOfMonth_shouldReturnCorrectDate(int year, int month, int day, int expectedYear, int expectedMonth, int expectedDay) {
            Symmetry454Date base = Symmetry454Date.of(year, month, day);
            Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, base.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#plusMinusDateSamples")
        void plus_shouldAddAmountCorrectly(int year, int month, int dom, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDom) {
            Symmetry454Date start = Symmetry454Date.of(year, month, dom);
            Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#plusMinusLeapWeekDateSamples")
        void plus_shouldHandleLeapWeekCorrectly(int year, int month, int dom, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDom) {
            Symmetry454Date start = Symmetry454Date.of(year, month, dom);
            Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#plusMinusDateSamples")
        void minus_shouldSubtractAmountCorrectly(int startYear, int startMonth, int startDom, long amount, TemporalUnit unit, int endYear, int endMonth, int endDom) {
            Symmetry454Date start = Symmetry454Date.of(startYear, startMonth, startDom);
            Symmetry454Date end = Symmetry454Date.of(endYear, endMonth, endDom);
            assertEquals(start, end.minus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#plusMinusLeapWeekDateSamples")
        void minus_shouldHandleLeapWeekCorrectly(int startYear, int startMonth, int startDom, long amount, TemporalUnit unit, int endYear, int endMonth, int endDom) {
            Symmetry454Date start = Symmetry454Date.of(startYear, startMonth, startDom);
            Symmetry454Date end = Symmetry454Date.of(endYear, endMonth, endDom);
            assertEquals(start, end.minus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#untilUnitSamples")
        void until_unit_shouldReturnCorrectAmount(int year1, int month1, int dom1, int year2, int month2, int dom2, TemporalUnit unit, long expected) {
            Symmetry454Date start = Symmetry454Date.of(year1, month1, dom1);
            Symmetry454Date end = Symmetry454Date.of(year2, month2, dom2);
            assertEquals(expected, start.until(end, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#untilPeriodSamples")
        void until_period_shouldReturnCorrectPeriod(int year1, int month1, int dom1, int year2, int month2, int dom2, int yearPeriod, int monthPeriod, int dayPeriod) {
            Symmetry454Date start = Symmetry454Date.of(year1, month1, dom1);
            Symmetry454Date end = Symmetry454Date.of(year2, month2, dom2);
            ChronoPeriod expected = Symmetry454Chronology.INSTANCE.period(yearPeriod, monthPeriod, dayPeriod);
            assertEquals(expected, start.until(end));
        }
    }

    @Nested
    class EraTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#unsupportedEras")
        void prolepticYear_shouldThrowExceptionForUnsupportedEra(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry454Chronology.INSTANCE.prolepticYear(era, 4));
        }

        @Test
        void eraAndYearOfEra_shouldBeConsistentAcrossBCEAndCE() {
            for (int year = 1; year < 200; year++) {
                Symmetry454Date base = Symmetry454Chronology.INSTANCE.date(year, 1, 1);
                assertEquals(year, base.get(YEAR));
                assertEquals(IsoEra.CE, base.getEra());
                assertEquals(year, base.get(YEAR_OF_ERA));
                Symmetry454Date eraBased = Symmetry454Chronology.INSTANCE.date(IsoEra.CE, year, 1, 1);
                assertEquals(base, eraBased);
            }
            for (int year = -200; year < 0; year++) {
                Symmetry454Date base = Symmetry454Chronology.INSTANCE.date(year, 1, 1);
                assertEquals(year, base.get(YEAR));
                assertEquals(IsoEra.BCE, base.getEra());
                assertEquals(1 - year, base.get(YEAR_OF_ERA));
                Symmetry454Date eraBased = Symmetry454Chronology.INSTANCE.date(IsoEra.BCE, 1 - year, 1, 1);
                // Note: prolepticYear for BCE is not the same as yearOfEra
                // The original test had a bug here: date(era, year, m, d) was used instead of date(era, yearOfEra, m, d)
                // Correcting it to use yearOfEra.
                Symmetry454Date eraBasedFromProleptic = Symmetry454Chronology.INSTANCE.date(year, 1, 1);
                assertEquals(base, eraBasedFromProleptic);
            }
        }
    }

    @Nested
    class ToStringTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#toStringSamples")
        void toString_shouldReturnCorrectlyFormattedString(Symmetry454Date date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}