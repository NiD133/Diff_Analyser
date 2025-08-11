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

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import java.time.Month;
import java.time.MonthDay;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.chrono.IsoChronology;
import java.time.chrono.JapaneseDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQueries;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junitpioneer.jupiter.RetryingTest;

/**
 * Test DayOfMonth.
 */
@DisplayName("DayOfMonth")
public class TestDayOfMonth {

    private static final DayOfMonth TEST_DAY_12 = DayOfMonth.of(12);
    private static final ZoneId PARIS = ZoneId.of("Europe/Paris");

    //-----------------------------------------------------------------------
    @Test
    @DisplayName("should implement required interfaces")
    void should_implementRequiredInterfaces() {
        assertTrue(Serializable.class.isAssignableFrom(DayOfMonth.class));
        assertTrue(Comparable.class.isAssignableFrom(DayOfMonth.class));
        assertTrue(TemporalAdjuster.class.isAssignableFrom(DayOfMonth.class));
        assertTrue(TemporalAccessor.class.isAssignableFrom(DayOfMonth.class));
    }

    @Test
    @DisplayName("should serialize and deserialize correctly")
    void should_serializeAndDeserialize() throws IOException, ClassNotFoundException {
        DayOfMonth test = DayOfMonth.of(1);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(test);
        }
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
            assertEquals(test, ois.readObject());
        }
    }

    //-----------------------------------------------------------------------
    // now()
    //-----------------------------------------------------------------------
    @RetryingTest(100)
    @DisplayName("now() should return the current day of the month")
    void now_getsCurrentDayOfMonth() {
        assertEquals(LocalDate.now().getDayOfMonth(), DayOfMonth.now().getValue());
    }

    @RetryingTest(100)
    @DisplayName("now(ZoneId) should return the current day of the month in the specified zone")
    void now_withZoneId_getsCurrentDayOfMonthForZone() {
        ZoneId zone = ZoneId.of("Asia/Tokyo");
        assertEquals(LocalDate.now(zone).getDayOfMonth(), DayOfMonth.now(zone).getValue());
    }

    @Test
    @DisplayName("now(Clock) should return the day of the month from the clock")
    void now_withClock_returnsDayOfMonthFromClock() {
        Instant instant = LocalDate.of(2008, 1, 23).atStartOfDay(PARIS).toInstant();
        Clock clock = Clock.fixed(instant, PARIS);
        assertEquals(23, DayOfMonth.now(clock).getValue());
    }

    //-----------------------------------------------------------------------
    // of(int)
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("of(int) should return a cached instance for valid days")
    void of_withValidDay_returnsCachedInstance() {
        for (int i = 1; i <= 31; i++) {
            DayOfMonth test = DayOfMonth.of(i);
            assertEquals(i, test.getValue());
            assertSame(test, DayOfMonth.of(i));
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 32, -1})
    @DisplayName("of(int) should throw DateTimeException for invalid days")
    void of_withInvalidDay_throwsException(int day) {
        assertThrows(DateTimeException.class, () -> DayOfMonth.of(day));
    }

    //-----------------------------------------------------------------------
    // from(TemporalAccessor)
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("from(TemporalAccessor) should extract the day from a temporal")
    void from_withTemporalAccessor_extractsDay() {
        assertEquals(DayOfMonth.of(15), DayOfMonth.from(LocalDate.of(2024, 5, 15)));
        assertEquals(DayOfMonth.of(29), DayOfMonth.from(LocalDate.of(2024, 2, 29)));
    }

    @Test
    @DisplayName("from(TemporalAccessor) should return the same instance if a DayOfMonth is passed")
    void from_withDayOfMonth_returnsSameInstance() {
        DayOfMonth dom = DayOfMonth.of(6);
        assertEquals(dom, DayOfMonth.from(dom));
    }

    @Test
    @DisplayName("from(TemporalAccessor) should extract the day from a non-ISO date")
    void from_withNonIsoDate_returnsCorrectDayOfMonth() {
        LocalDate date = LocalDate.now();
        assertEquals(date.getDayOfMonth(), DayOfMonth.from(JapaneseDate.from(date)).getValue());
    }

    @Test
    @DisplayName("from(TemporalAccessor) should throw DateTimeException for unsupported types")
    void from_withUnsupportedTemporal_throwsException() {
        assertThrows(DateTimeException.class, () -> DayOfMonth.from(LocalTime.NOON));
    }

    @Test
    @DisplayName("from(TemporalAccessor) should throw NullPointerException for null input")
    void from_withNull_throwsException() {
        assertThrows(NullPointerException.class, () -> DayOfMonth.from(null));
    }

    @Test
    @DisplayName("from(TemporalAccessor) should be usable as a query after parsing")
    void from_afterParsingString_returnsCorrectDayOfMonth() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d");
        assertEquals(DayOfMonth.of(3), formatter.parse("3", DayOfMonth::from));
    }

    //-----------------------------------------------------------------------
    // isSupported(TemporalField)
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("isSupported(TemporalField) should return true for DAY_OF_MONTH")
    void isSupported_withDayOfMonthField_returnsTrue() {
        assertTrue(TEST_DAY_12.isSupported(DAY_OF_MONTH));
    }

    @Test
    @DisplayName("isSupported(TemporalField) should return false for other ChronoFields")
    void isSupported_withOtherFields_returnsFalse() {
        assertFalse(TEST_DAY_12.isSupported(null));
        assertFalse(TEST_DAY_12.isSupported(ChronoField.DAY_OF_WEEK));
        assertFalse(TEST_DAY_12.isSupported(ChronoField.DAY_OF_YEAR));
        assertFalse(TEST_DAY_12.isSupported(ChronoField.MONTH_OF_YEAR));
        assertFalse(TEST_DAY_12.isSupported(ChronoField.YEAR));
        assertFalse(TEST_DAY_12.isSupported(ChronoField.HOUR_OF_DAY));
    }

    //-----------------------------------------------------------------------
    // range(TemporalField)
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("range(TemporalField) should return the correct range for DAY_OF_MONTH")
    void range_withDayOfMonthField_returnsCorrectRange() {
        assertEquals(DAY_OF_MONTH.range(), TEST_DAY_12.range(DAY_OF_MONTH));
    }

    @Test
    @DisplayName("range(TemporalField) should throw UnsupportedTemporalTypeException for unsupported fields")
    void range_withUnsupportedField_throwsException() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> TEST_DAY_12.range(MONTH_OF_YEAR));
    }

    @Test
    @DisplayName("range(TemporalField) should throw NullPointerException for null field")
    void range_withNullField_throwsException() {
        assertThrows(NullPointerException.class, () -> TEST_DAY_12.range(null));
    }

    //-----------------------------------------------------------------------
    // get(TemporalField) / getLong(TemporalField)
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("get(TemporalField) should return the day for DAY_OF_MONTH")
    void get_withDayOfMonthField_returnsValue() {
        assertEquals(12, TEST_DAY_12.get(DAY_OF_MONTH));
    }

    @Test
    @DisplayName("getLong(TemporalField) should return the day for DAY_OF_MONTH")
    void getLong_withDayOfMonthField_returnsValue() {
        assertEquals(12L, TEST_DAY_12.getLong(DAY_OF_MONTH));
    }

    @Test
    @DisplayName("getLong(TemporalField) should throw UnsupportedTemporalTypeException for unsupported fields")
    void getLong_withUnsupportedField_throwsException() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> TEST_DAY_12.getLong(MONTH_OF_YEAR));
    }

    //-----------------------------------------------------------------------
    // isValidYearMonth(YearMonth)
    //-----------------------------------------------------------------------
    @ParameterizedTest
    @CsvSource({
            "31, 2012, 1, true",   // Jan
            "31, 2012, 2, false",  // Feb non-leap
            "30, 2012, 2, false",  // Feb
            "29, 2012, 2, true",   // Feb leap
            "29, 2011, 2, false",  // Feb non-leap
            "31, 2012, 3, true",   // Mar
            "31, 2012, 4, false",  // Apr
            "30, 2012, 4, true",   // Apr
    })
    @DisplayName("isValidYearMonth(YearMonth) should correctly validate the day for a given year-month")
    void isValidYearMonth_forVariousMonths_returnsCorrectValidity(int day, int year, int month, boolean expected) {
        DayOfMonth dom = DayOfMonth.of(day);
        assertEquals(expected, dom.isValidYearMonth(YearMonth.of(year, month)));
    }

    @Test
    @DisplayName("isValidYearMonth(YearMonth) should return false for null input")
    void isValidYearMonth_withNull_returnsFalse() {
        assertFalse(TEST_DAY_12.isValidYearMonth(null));
    }

    //-----------------------------------------------------------------------
    // query(TemporalQuery)
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("query(TemporalQuery) should return expected results for standard queries")
    void query_withStandardQueries_returnsExpectedResults() {
        assertEquals(IsoChronology.INSTANCE, TEST_DAY_12.query(TemporalQueries.chronology()));
        assertEquals(null, TEST_DAY_12.query(TemporalQueries.localDate()));
        assertEquals(null, TEST_DAY_12.query(TemporalQueries.zoneId()));
        assertEquals(null, TEST_DAY_12.query(TemporalQueries.precision()));
    }

    //-----------------------------------------------------------------------
    // adjustInto(Temporal)
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("adjustInto(Temporal) should adjust a date to the new day")
    void adjustInto_withValidDate_returnsAdjustedDate() {
        LocalDate base = LocalDate.of(2007, 1, 1);
        LocalDate adjusted = (LocalDate) DayOfMonth.of(23).adjustInto(base);
        assertEquals(LocalDate.of(2007, 1, 23), adjusted);
    }

    @Test
    @DisplayName("adjustInto(Temporal) should throw DateTimeException for an invalid resulting date")
    void adjustInto_toInvalidDate_throwsException() {
        LocalDate base = LocalDate.of(2007, 4, 1); // April
        DayOfMonth day31 = DayOfMonth.of(31);
        assertThrows(DateTimeException.class, () -> day31.adjustInto(base));
    }

    @Test
    @DisplayName("adjustInto(Temporal) should throw NullPointerException for null input")
    void adjustInto_withNull_throwsException() {
        assertThrows(NullPointerException.class, () -> TEST_DAY_12.adjustInto(null));
    }

    //-----------------------------------------------------------------------
    // atMonth(Month) / atMonth(int)
    //-----------------------------------------------------------------------
    private static Stream<Arguments> atMonthSource() {
        return Stream.of(
                Arguments.of(31, Month.JANUARY, MonthDay.of(1, 31)),
                Arguments.of(31, Month.FEBRUARY, MonthDay.of(2, 29)), // resolves to last valid day
                Arguments.of(31, Month.APRIL, MonthDay.of(4, 30)),    // resolves to last valid day
                Arguments.of(28, Month.FEBRUARY, MonthDay.of(2, 28))
        );
    }

    @ParameterizedTest
    @MethodSource("atMonthSource")
    @DisplayName("atMonth(Month) should combine to a MonthDay, adjusting to the last valid day")
    void atMonth_withMonthEnum_returnsCorrectMonthDay(int day, Month month, MonthDay expected) {
        assertEquals(expected, DayOfMonth.of(day).atMonth(month));
    }

    @ParameterizedTest
    @MethodSource("atMonthSource")
    @DisplayName("atMonth(int) should combine to a MonthDay, adjusting to the last valid day")
    void atMonth_withMonthValue_returnsCorrectMonthDay(int day, Month month, MonthDay expected) {
        assertEquals(expected, DayOfMonth.of(day).atMonth(month.getValue()));
    }

    @Test
    @DisplayName("atMonth(int) should throw DateTimeException for invalid month value")
    void atMonth_withInvalidMonthValue_throwsException() {
        assertThrows(DateTimeException.class, () -> TEST_DAY_12.atMonth(13));
    }

    //-----------------------------------------------------------------------
    // atYearMonth(YearMonth)
    //-----------------------------------------------------------------------
    private static Stream<Arguments> atYearMonthSource() {
        return Stream.of(
                Arguments.of(31, YearMonth.of(2012, 1), LocalDate.of(2012, 1, 31)),
                Arguments.of(31, YearMonth.of(2012, 2), LocalDate.of(2012, 2, 29)), // leap year
                Arguments.of(31, YearMonth.of(2011, 2), LocalDate.of(2011, 2, 28)), // non-leap year
                Arguments.of(31, YearMonth.of(2012, 4), LocalDate.of(2012, 4, 30)), // 30-day month
                Arguments.of(28, YearMonth.of(2012, 2), LocalDate.of(2012, 2, 28))
        );
    }

    @ParameterizedTest
    @MethodSource("atYearMonthSource")
    @DisplayName("atYearMonth(YearMonth) should combine to a LocalDate, adjusting to the last valid day")
    void atYearMonth_forVariousYearMonths_returnsCorrectLocalDate(int day, YearMonth ym, LocalDate expected) {
        assertEquals(expected, DayOfMonth.of(day).atYearMonth(ym));
    }

    @Test
    @DisplayName("atYearMonth(YearMonth) should throw NullPointerException for null input")
    void atYearMonth_withNull_throwsException() {
        assertThrows(NullPointerException.class, () -> TEST_DAY_12.atYearMonth(null));
    }

    //-----------------------------------------------------------------------
    // compareTo()
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("compareTo() should correctly compare two DayOfMonth instances")
    void compareTo_withOtherDayOfMonth_returnsCorrectComparison() {
        DayOfMonth dom1 = DayOfMonth.of(1);
        DayOfMonth dom15 = DayOfMonth.of(15);
        DayOfMonth dom31 = DayOfMonth.of(31);

        assertTrue(dom1.compareTo(dom15) < 0);
        assertTrue(dom15.compareTo(dom1) > 0);
        assertTrue(dom15.compareTo(dom31) < 0);
        assertEquals(0, dom15.compareTo(DayOfMonth.of(15)));
    }

    //-----------------------------------------------------------------------
    // equals() / hashCode()
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("equals() and hashCode() should follow the contract")
    void equalsAndHashCode_shouldFollowContract() {
        new EqualsTester()
                .addEqualityGroup(DayOfMonth.of(1), DayOfMonth.of(1))
                .addEqualityGroup(DayOfMonth.of(2), DayOfMonth.of(2))
                .testEquals();
    }

    //-----------------------------------------------------------------------
    // toString()
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("toString() should return the correct string representation")
    void toString_returnsCorrectFormat() {
        assertEquals("DayOfMonth:1", DayOfMonth.of(1).toString());
        assertEquals("DayOfMonth:31", DayOfMonth.of(31).toString());
    }
}