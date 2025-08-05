/*
 * Copyright (c) 2007-present, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.threeten.extra;

import static java.time.temporal.ChronoField.DAY_OF_YEAR;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQueries;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Test DayOfYear.
 *
 * This revised test suite improves understandability by using descriptive names,
 * grouping related tests in nested classes, and employing parameterized tests
 * to reduce boilerplate code.
 */
@DisplayName("DayOfYear")
public class TestDayOfYear {

    private static final Year YEAR_2007 = Year.of(2007);
    private static final Year YEAR_2008_LEAP = Year.of(2008);
    private static final int STANDARD_YEAR_LENGTH = 365;
    private static final int LEAP_YEAR_LENGTH = 366;
    private static final DayOfYear TEST_DOY_12 = DayOfYear.of(12);
    private static final ZoneId PARIS = ZoneId.of("Europe/Paris");

    /**
     * Tests for the factory methods, such as of(), from(), and now().
     */
    @Nested
    @DisplayName("Factory methods")
    class Factory {

        @Test
        void of_whenDayOfYearIsValid_returnsCachedInstance() {
            for (int i = 1; i <= LEAP_YEAR_LENGTH; i++) {
                DayOfYear test = DayOfYear.of(i);
                assertEquals(i, test.getValue());
                assertSame(DayOfYear.of(i), test);
            }
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 367})
        void of_whenDayOfYearIsInvalid_throwsException(int dayOfYear) {
            assertThrows(DateTimeException.class, () -> DayOfYear.of(dayOfYear));
        }

        @Test
        void from_withValidTemporalAccessor_returnsDayOfYear() {
            LocalDate date = LocalDate.of(2008, 1, 1);
            for (int i = 1; i <= LEAP_YEAR_LENGTH; i++) {
                DayOfYear test = DayOfYear.from(date);
                assertEquals(i, test.getValue());
                date = date.plusDays(1);
            }
        }

        @Test
        void from_withDayOfYearInstance_returnsSelf() {
            DayOfYear dayOfYear = DayOfYear.of(6);
            assertEquals(dayOfYear, DayOfYear.from(dayOfYear));
        }

        @Test
        void from_withNonIsoChronology_returnsDayOfYear() {
            LocalDate date = LocalDate.now();
            JapaneseDate japaneseDate = JapaneseDate.from(date);
            DayOfYear dayOfYear = DayOfYear.from(japaneseDate);
            assertEquals(date.getDayOfYear(), dayOfYear.getValue());
        }

        @Test
        void from_withNull_throwsException() {
            assertThrows(NullPointerException.class, () -> DayOfYear.from(null));
        }

        @Test
        void from_withUnsupportedType_throwsException() {
            assertThrows(DateTimeException.class, () -> DayOfYear.from(LocalTime.NOON));
        }

        @Test
        void from_usingDateTimeFormatter_parsesCorrectly() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("D");
            DayOfYear expected = DayOfYear.of(76);
            assertEquals(expected, formatter.parse("76", DayOfYear::from));
        }

        @Test
        void now_withClock_returnsDayOfYearFromClock() {
            LocalDate date = LocalDate.of(2024, 6, 20); // Day 172
            Instant instant = date.atStartOfDay(PARIS).toInstant();
            Clock clock = Clock.fixed(instant, PARIS);

            DayOfYear actual = DayOfYear.now(clock);
            assertEquals(172, actual.getValue());
        }
    }

    /**
     * Tests for the core API methods like isSupported(), get(), range(), and isValidYear().
     */
    @Nested
    @DisplayName("Core API")
    class CoreApi {

        @Test
        void isSupported_withDayOfYear_returnsTrue() {
            assertTrue(TEST_DOY_12.isSupported(DAY_OF_YEAR));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.TestDayOfYear#unsupportedFields")
        void isSupported_withUnsupportedFields_returnsFalse(TemporalField field) {
            assertFalse(TEST_DOY_12.isSupported(field));
        }

        @Test
        void range_forDayOfYear_returnsValidRange() {
            assertEquals(DAY_OF_YEAR.range(), TEST_DOY_12.range(DAY_OF_YEAR));
        }

        @Test
        void range_forUnsupportedField_throwsException() {
            assertThrows(UnsupportedTemporalTypeException.class, () -> TEST_DOY_12.range(MONTH_OF_YEAR));
        }

        @Test
        void get_forDayOfYear_returnsValue() {
            assertEquals(12, TEST_DOY_12.get(DAY_OF_YEAR));
        }

        @Test
        void getLong_forDayOfYear_returnsValue() {
            assertEquals(12L, TEST_DOY_12.getLong(DAY_OF_YEAR));
        }

        @Test
        void get_forUnsupportedField_throwsException() {
            assertThrows(UnsupportedTemporalTypeException.class, () -> TEST_DOY_12.get(MONTH_OF_YEAR));
        }

        @Test
        void isValidYear_withDay366_isCorrectForLeapAndNonLeapYears() {
            DayOfYear day366 = DayOfYear.of(366);
            assertTrue(day366.isValidYear(2012)); // Leap year
            assertFalse(day366.isValidYear(2011)); // Non-leap year
        }

        @Test
        void isValidYear_withDay365_isAlwaysTrue() {
            DayOfYear day365 = DayOfYear.of(365);
            assertTrue(day365.isValidYear(2011));
            assertTrue(day365.isValidYear(2012));
        }
    }

    /**
     * Tests for the TemporalAdjuster and TemporalQuery functionality.
     */
    @Nested
    @DisplayName("Adjuster and Query API")
    class AdjusterQueryApi {

        @Test
        void adjustInto_setsDayOfYearCorrectly() {
            LocalDate baseDate = LocalDate.of(2023, 5, 10);
            DayOfYear dayOfYear = DayOfYear.of(45); // Feb 14
            LocalDate expected = LocalDate.of(2023, 2, 14);

            assertEquals(expected, dayOfYear.adjustInto(baseDate));
        }

        @Test
        void adjustInto_withDay366InNonLeapYear_throwsException() {
            LocalDate nonLeapYearDate = LocalDate.of(2023, 1, 1);
            DayOfYear day366 = DayOfYear.of(LEAP_YEAR_LENGTH);
            assertThrows(DateTimeException.class, () -> day366.adjustInto(nonLeapYearDate));
        }

        @Test
        void adjustInto_withNull_throwsException() {
            assertThrows(NullPointerException.class, () -> TEST_DOY_12.adjustInto(null));
        }

        @Test
        void query_returnsExpectedValues() {
            assertEquals(IsoChronology.INSTANCE, TEST_DOY_12.query(TemporalQueries.chronology()));
            assertNull(TEST_DOY_12.query(TemporalQueries.localDate()));
            assertNull(TEST_DOY_12.query(TemporalQueries.zoneId()));
            assertNull(TEST_DOY_12.query(TemporalQueries.precision()));
        }
    }

    /**
     * Tests for the atYear() methods.
     */
    @Nested
    @DisplayName("atYear() functionality")
    class AtYear {

        @Test
        void atYear_withInt_returnsCorrectDate() {
            LocalDate expected = LocalDate.of(2023, 1, 12);
            assertEquals(expected, TEST_DOY_12.atYear(2023));
        }

        @Test
        void atYear_withYearObject_returnsCorrectDate() {
            LocalDate expected = LocalDate.of(2007, 1, 12);
            assertEquals(expected, TEST_DOY_12.atYear(YEAR_2007));
        }

        @Test
        void atYear_withDay366InLeapYear_returnsCorrectDate() {
            DayOfYear day366 = DayOfYear.of(LEAP_YEAR_LENGTH);
            LocalDate expected = LocalDate.of(2008, 12, 31);
            assertEquals(expected, day366.atYear(YEAR_2008_LEAP));
        }

        @Test
        void atYear_withDay366InNonLeapYear_throwsException() {
            DayOfYear day366 = DayOfYear.of(LEAP_YEAR_LENGTH);
            assertThrows(DateTimeException.class, () -> day366.atYear(YEAR_2007));
        }

        @Test
        void atYear_withNullYear_throwsException() {
            assertThrows(NullPointerException.class, () -> TEST_DOY_12.atYear(null));
        }
    }

    /**
     * Tests for the standard object methods: equals(), hashCode(), compareTo(), toString(),
     * and serialization.
     */
    @Nested
    @DisplayName("Object contract and Serialization")
    class ObjectContract {

        @Test
        void equals_and_hashCode_adhereToContract() {
            new EqualsTester()
                    .addEqualityGroup(DayOfYear.of(1), DayOfYear.of(1))
                    .addEqualityGroup(DayOfYear.of(2), DayOfYear.of(2))
                    .testEquals();
        }

        @Test
        void compareTo_isCorrect() {
            DayOfYear doy100 = DayOfYear.of(100);
            DayOfYear doy200 = DayOfYear.of(200);

            assertTrue(doy100.compareTo(doy200) < 0);
            assertEquals(0, doy100.compareTo(doy100));
            assertTrue(doy200.compareTo(doy100) > 0);
        }

        @Test
        void compareTo_withNull_throwsException() {
            assertThrows(NullPointerException.class, () -> TEST_DOY_12.compareTo(null));
        }

        @Test
        void toString_returnsCorrectFormat() {
            assertEquals("DayOfYear:123", DayOfYear.of(123).toString());
        }

        @Test
        void serialization_preservesInstance() throws IOException, ClassNotFoundException {
            DayOfYear original = DayOfYear.of(150);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(original);
            }
            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
                DayOfYear deserialized = (DayOfYear) ois.readObject();
                // The implementation uses a cache and readResolve, so it should be the same instance.
                assertSame(original, deserialized);
            }
        }

        @Test
        void test_interfaces() {
            assertTrue(Serializable.class.isAssignableFrom(DayOfYear.class));
            assertTrue(Comparable.class.isAssignableFrom(DayOfYear.class));
            assertTrue(TemporalAdjuster.class.isAssignableFrom(DayOfYear.class));
            assertTrue(TemporalAccessor.class.isAssignableFrom(DayOfYear.class));
        }
    }

    // Used as a MethodSource for parameterized tests
    static Stream<TemporalField> unsupportedFields() {
        return Stream.of(
                ChronoField.NANO_OF_SECOND, ChronoField.NANO_OF_DAY, ChronoField.MICRO_OF_SECOND,
                ChronoField.MICRO_OF_DAY, ChronoField.MILLI_OF_SECOND, ChronoField.MILLI_OF_DAY,
                ChronoField.SECOND_OF_MINUTE, ChronoField.SECOND_OF_DAY, ChronoField.MINUTE_OF_HOUR,
                ChronoField.MINUTE_OF_DAY, ChronoField.HOUR_OF_AMPM, ChronoField.CLOCK_HOUR_OF_AMPM,
                ChronoField.HOUR_OF_DAY, ChronoField.CLOCK_HOUR_OF_DAY, ChronoField.AMPM_OF_DAY,
                ChronoField.DAY_OF_WEEK, ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH,
                ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR, ChronoField.DAY_OF_MONTH, ChronoField.EPOCH_DAY,
                ChronoField.ALIGNED_WEEK_OF_MONTH, ChronoField.ALIGNED_WEEK_OF_YEAR,
                ChronoField.MONTH_OF_YEAR, ChronoField.PROLEPTIC_MONTH, ChronoField.YEAR_OF_ERA,
                ChronoField.YEAR, ChronoField.ERA, ChronoField.INSTANT_SECONDS,
                ChronoField.OFFSET_SECONDS
        );
    }
}