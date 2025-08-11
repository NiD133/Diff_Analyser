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

import static java.time.Month.*;
import static java.time.temporal.ChronoField.*;
import static org.junit.jupiter.api.Assertions.*;

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

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.RetryingTest;

import com.google.common.testing.EqualsTester;

/**
 * Comprehensive test suite for DayOfMonth class.
 * Tests all public methods including factory methods, validation, temporal operations,
 * and edge cases for month-end dates.
 */
public class TestDayOfMonth {

    // Test constants
    private static final int MAX_DAY_OF_MONTH = 31;
    private static final DayOfMonth SAMPLE_DAY = DayOfMonth.of(12);
    private static final ZoneId PARIS_ZONE = ZoneId.of("Europe/Paris");
    
    // Test years for leap year testing
    private static final int NON_LEAP_YEAR = 2007;
    private static final int LEAP_YEAR = 2008;

    /**
     * Custom temporal field for testing field support and delegation.
     */
    private static class TestTemporalField implements TemporalField {
        public static final TestTemporalField INSTANCE = new TestTemporalField();

        @Override
        public TemporalUnit getBaseUnit() {
            return ChronoUnit.DAYS;
        }

        @Override
        public TemporalUnit getRangeUnit() {
            return ChronoUnit.MONTHS;
        }

        @Override
        public ValueRange range() {
            return ValueRange.of(1, 28, 31);
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
            return temporal.isSupported(DAY_OF_MONTH);
        }

        @Override
        public ValueRange rangeRefinedBy(TemporalAccessor temporal) {
            return range();
        }

        @Override
        public long getFrom(TemporalAccessor temporal) {
            return temporal.getLong(DAY_OF_MONTH);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <R extends Temporal> R adjustInto(R temporal, long newValue) {
            return (R) temporal.with(DAY_OF_MONTH, newValue);
        }
    }

    // ========== Basic Class Properties Tests ==========

    @Test
    public void shouldImplementRequiredInterfaces() {
        assertTrue(Serializable.class.isAssignableFrom(DayOfMonth.class));
        assertTrue(Comparable.class.isAssignableFrom(DayOfMonth.class));
        assertTrue(TemporalAdjuster.class.isAssignableFrom(DayOfMonth.class));
        assertTrue(TemporalAccessor.class.isAssignableFrom(DayOfMonth.class));
    }

    @Test
    public void shouldSerializeAndDeserializeCorrectly() throws IOException, ClassNotFoundException {
        DayOfMonth originalDay = DayOfMonth.of(1);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(originalDay);
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
            DayOfMonth deserializedDay = (DayOfMonth) ois.readObject();
            assertEquals(originalDay, deserializedDay);
        }
    }

    // ========== Factory Method Tests - now() ==========

    @RetryingTest(100)
    public void nowShouldReturnCurrentDayOfMonth() {
        int expectedDay = LocalDate.now().getDayOfMonth();
        int actualDay = DayOfMonth.now().getValue();
        assertEquals(expectedDay, actualDay);
    }

    @RetryingTest(100)
    public void nowWithZoneShouldReturnCurrentDayInSpecifiedZone() {
        ZoneId tokyoZone = ZoneId.of("Asia/Tokyo");
        int expectedDay = LocalDate.now(tokyoZone).getDayOfMonth();
        int actualDay = DayOfMonth.now(tokyoZone).getValue();
        assertEquals(expectedDay, actualDay);
    }

    @Test
    public void nowWithClockShouldReturnDayFromClock() {
        // Test each day of January 2008
        for (int day = 1; day <= 31; day++) {
            Instant fixedInstant = LocalDate.of(2008, 1, day).atStartOfDay(PARIS_ZONE).toInstant();
            Clock fixedClock = Clock.fixed(fixedInstant, PARIS_ZONE);
            
            assertEquals(day, DayOfMonth.now(fixedClock).getValue());
        }
    }

    // ========== Factory Method Tests - of(int) ==========

    @Test
    public void ofShouldCreateValidDayOfMonthAndUseSingletons() {
        for (int day = 1; day <= MAX_DAY_OF_MONTH; day++) {
            DayOfMonth dayOfMonth = DayOfMonth.of(day);
            assertEquals(day, dayOfMonth.getValue());
            // Verify singleton pattern
            assertSame(dayOfMonth, DayOfMonth.of(day));
        }
    }

    @Test
    public void ofShouldRejectDayTooLow() {
        assertThrows(DateTimeException.class, () -> DayOfMonth.of(0));
    }

    @Test
    public void ofShouldRejectDayTooHigh() {
        assertThrows(DateTimeException.class, () -> DayOfMonth.of(32));
    }

    // ========== Factory Method Tests - from(TemporalAccessor) ==========

    @Test
    public void fromShouldExtractDayFromNonLeapYear() {
        verifyDayExtractionForYear(NON_LEAP_YEAR);
    }

    @Test
    public void fromShouldExtractDayFromLeapYear() {
        verifyDayExtractionForYear(LEAP_YEAR);
    }

    private void verifyDayExtractionForYear(int year) {
        LocalDate date = LocalDate.of(year, 1, 1);
        
        // Test each month of the year
        int[] daysInMonth = getDaysInMonthForYear(year);
        
        for (int month = 1; month <= 12; month++) {
            for (int day = 1; day <= daysInMonth[month - 1]; day++) {
                assertEquals(day, DayOfMonth.from(date).getValue());
                date = date.plusDays(1);
            }
        }
    }

    private int[] getDaysInMonthForYear(int year) {
        boolean isLeapYear = (year == LEAP_YEAR);
        return new int[]{31, isLeapYear ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    }

    @Test
    public void fromShouldReturnSameInstanceForDayOfMonth() {
        DayOfMonth originalDay = DayOfMonth.of(6);
        assertEquals(originalDay, DayOfMonth.from(originalDay));
    }

    @Test
    public void fromShouldWorkWithNonIsoChronology() {
        LocalDate isoDate = LocalDate.now();
        JapaneseDate japaneseDate = JapaneseDate.from(isoDate);
        assertEquals(isoDate.getDayOfMonth(), DayOfMonth.from(japaneseDate).getValue());
    }

    @Test
    public void fromShouldRejectTemporalWithoutDayOfMonth() {
        assertThrows(DateTimeException.class, () -> DayOfMonth.from(LocalTime.NOON));
    }

    @Test
    public void fromShouldRejectNullTemporalAccessor() {
        assertThrows(NullPointerException.class, () -> DayOfMonth.from((TemporalAccessor) null));
    }

    @Test
    public void fromShouldWorkWithDateTimeFormatter() {
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("d");
        assertEquals(DayOfMonth.of(3), dayFormatter.parse("3", DayOfMonth::from));
    }

    // ========== Temporal Field Support Tests ==========

    @Test
    public void isSupportedShouldReturnCorrectValuesForAllFields() {
        // Supported fields
        assertTrue(SAMPLE_DAY.isSupported(DAY_OF_MONTH));
        assertTrue(SAMPLE_DAY.isSupported(TestTemporalField.INSTANCE));
        
        // Unsupported fields - time-based
        assertFalse(SAMPLE_DAY.isSupported(NANO_OF_SECOND));
        assertFalse(SAMPLE_DAY.isSupported(NANO_OF_DAY));
        assertFalse(SAMPLE_DAY.isSupported(MICRO_OF_SECOND));
        assertFalse(SAMPLE_DAY.isSupported(MICRO_OF_DAY));
        assertFalse(SAMPLE_DAY.isSupported(MILLI_OF_SECOND));
        assertFalse(SAMPLE_DAY.isSupported(MILLI_OF_DAY));
        assertFalse(SAMPLE_DAY.isSupported(SECOND_OF_MINUTE));
        assertFalse(SAMPLE_DAY.isSupported(SECOND_OF_DAY));
        assertFalse(SAMPLE_DAY.isSupported(MINUTE_OF_HOUR));
        assertFalse(SAMPLE_DAY.isSupported(MINUTE_OF_DAY));
        assertFalse(SAMPLE_DAY.isSupported(HOUR_OF_AMPM));
        assertFalse(SAMPLE_DAY.isSupported(CLOCK_HOUR_OF_AMPM));
        assertFalse(SAMPLE_DAY.isSupported(HOUR_OF_DAY));
        assertFalse(SAMPLE_DAY.isSupported(CLOCK_HOUR_OF_DAY));
        assertFalse(SAMPLE_DAY.isSupported(AMPM_OF_DAY));
        
        // Unsupported fields - other date-based
        assertFalse(SAMPLE_DAY.isSupported(DAY_OF_WEEK));
        assertFalse(SAMPLE_DAY.isSupported(ALIGNED_DAY_OF_WEEK_IN_MONTH));
        assertFalse(SAMPLE_DAY.isSupported(ALIGNED_DAY_OF_WEEK_IN_YEAR));
        assertFalse(SAMPLE_DAY.isSupported(DAY_OF_YEAR));
        assertFalse(SAMPLE_DAY.isSupported(EPOCH_DAY));
        assertFalse(SAMPLE_DAY.isSupported(ALIGNED_WEEK_OF_MONTH));
        assertFalse(SAMPLE_DAY.isSupported(ALIGNED_WEEK_OF_YEAR));
        assertFalse(SAMPLE_DAY.isSupported(MONTH_OF_YEAR));
        assertFalse(SAMPLE_DAY.isSupported(PROLEPTIC_MONTH));
        assertFalse(SAMPLE_DAY.isSupported(YEAR_OF_ERA));
        assertFalse(SAMPLE_DAY.isSupported(YEAR));
        assertFalse(SAMPLE_DAY.isSupported(ERA));
        assertFalse(SAMPLE_DAY.isSupported(INSTANT_SECONDS));
        assertFalse(SAMPLE_DAY.isSupported(OFFSET_SECONDS));
        assertFalse(SAMPLE_DAY.isSupported(IsoFields.DAY_OF_QUARTER));
        
        // Null field
        assertFalse(SAMPLE_DAY.isSupported((TemporalField) null));
    }

    @Test
    public void rangeShouldReturnDayOfMonthRange() {
        assertEquals(DAY_OF_MONTH.range(), SAMPLE_DAY.range(DAY_OF_MONTH));
    }

    @Test
    public void rangeShouldRejectUnsupportedField() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> SAMPLE_DAY.range(MONTH_OF_YEAR));
    }

    @Test
    public void rangeShouldRejectNullField() {
        assertThrows(NullPointerException.class, () -> SAMPLE_DAY.range((TemporalField) null));
    }

    @Test
    public void getShouldReturnDayValue() {
        assertEquals(12, SAMPLE_DAY.get(DAY_OF_MONTH));
    }

    @Test
    public void getShouldRejectUnsupportedField() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> SAMPLE_DAY.get(MONTH_OF_YEAR));
    }

    @Test
    public void getShouldRejectNullField() {
        assertThrows(NullPointerException.class, () -> SAMPLE_DAY.get((TemporalField) null));
    }

    @Test
    public void getLongShouldReturnDayValue() {
        assertEquals(12L, SAMPLE_DAY.getLong(DAY_OF_MONTH));
    }

    @Test
    public void getLongShouldWorkWithCustomField() {
        assertEquals(12L, SAMPLE_DAY.getLong(TestTemporalField.INSTANCE));
    }

    @Test
    public void getLongShouldRejectUnsupportedFields() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> SAMPLE_DAY.getLong(MONTH_OF_YEAR));
        assertThrows(UnsupportedTemporalTypeException.class, () -> SAMPLE_DAY.getLong(IsoFields.DAY_OF_QUARTER));
    }

    @Test
    public void getLongShouldRejectNullField() {
        assertThrows(NullPointerException.class, () -> SAMPLE_DAY.getLong((TemporalField) null));
    }

    // ========== Year-Month Validation Tests ==========

    @Test
    public void isValidYearMonthShouldValidateDay31Correctly() {
        DayOfMonth day31 = DayOfMonth.of(31);
        
        // Months with 31 days
        assertTrue(day31.isValidYearMonth(YearMonth.of(2012, JANUARY)));
        assertTrue(day31.isValidYearMonth(YearMonth.of(2012, MARCH)));
        assertTrue(day31.isValidYearMonth(YearMonth.of(2012, MAY)));
        assertTrue(day31.isValidYearMonth(YearMonth.of(2012, JULY)));
        assertTrue(day31.isValidYearMonth(YearMonth.of(2012, AUGUST)));
        assertTrue(day31.isValidYearMonth(YearMonth.of(2012, OCTOBER)));
        assertTrue(day31.isValidYearMonth(YearMonth.of(2012, DECEMBER)));
        
        // Months with 30 days or less
        assertFalse(day31.isValidYearMonth(YearMonth.of(2012, FEBRUARY)));
        assertFalse(day31.isValidYearMonth(YearMonth.of(2012, APRIL)));
        assertFalse(day31.isValidYearMonth(YearMonth.of(2012, JUNE)));
        assertFalse(day31.isValidYearMonth(YearMonth.of(2012, SEPTEMBER)));
        assertFalse(day31.isValidYearMonth(YearMonth.of(2012, NOVEMBER)));
    }

    @Test
    public void isValidYearMonthShouldValidateDay30Correctly() {
        DayOfMonth day30 = DayOfMonth.of(30);
        
        // Valid for all months except February
        for (Month month : Month.values()) {
            boolean expected = (month != FEBRUARY);
            assertEquals(expected, day30.isValidYearMonth(YearMonth.of(2012, month)));
        }
    }

    @Test
    public void isValidYearMonthShouldValidateDay29ForLeapAndNonLeapYears() {
        DayOfMonth day29 = DayOfMonth.of(29);
        
        // Valid for all months in leap year
        for (Month month : Month.values()) {
            assertTrue(day29.isValidYearMonth(YearMonth.of(LEAP_YEAR, month)));
        }
        
        // Invalid only for February in non-leap year
        assertFalse(day29.isValidYearMonth(YearMonth.of(NON_LEAP_YEAR, FEBRUARY)));
        for (Month month : Month.values()) {
            if (month != FEBRUARY) {
                assertTrue(day29.isValidYearMonth(YearMonth.of(NON_LEAP_YEAR, month)));
            }
        }
    }

    @Test
    public void isValidYearMonthShouldValidateDay28ForAllMonths() {
        DayOfMonth day28 = DayOfMonth.of(28);
        
        // Valid for all months in any year
        for (Month month : Month.values()) {
            assertTrue(day28.isValidYearMonth(YearMonth.of(2012, month)));
            assertTrue(day28.isValidYearMonth(YearMonth.of(2011, month)));
        }
    }

    @Test
    public void isValidYearMonthShouldReturnFalseForNull() {
        assertFalse(SAMPLE_DAY.isValidYearMonth(null));
    }

    // ========== Temporal Query Tests ==========

    @Test
    public void queryShouldReturnExpectedValues() {
        assertEquals(IsoChronology.INSTANCE, SAMPLE_DAY.query(TemporalQueries.chronology()));
        assertNull(SAMPLE_DAY.query(TemporalQueries.localDate()));
        assertNull(SAMPLE_DAY.query(TemporalQueries.localTime()));
        assertNull(SAMPLE_DAY.query(TemporalQueries.offset()));
        assertNull(SAMPLE_DAY.query(TemporalQueries.precision()));
        assertNull(SAMPLE_DAY.query(TemporalQueries.zone()));
        assertNull(SAMPLE_DAY.query(TemporalQueries.zoneId()));
    }

    // ========== Temporal Adjuster Tests ==========

    @Test
    public void adjustIntoShouldSetDayOfMonth() {
        LocalDate baseDate = LocalDate.of(2007, 1, 1);
        
        for (int day = 1; day <= MAX_DAY_OF_MONTH; day++) {
            LocalDate expectedDate = LocalDate.of(2007, 1, day);
            Temporal adjustedDate = DayOfMonth.of(day).adjustInto(baseDate);
            assertEquals(expectedDate, adjustedDate);
        }
    }

    @Test
    public void adjustIntoShouldRejectInvalidDayForMonth() {
        LocalDate aprilFirst = LocalDate.of(2007, 4, 1);
        DayOfMonth day31 = DayOfMonth.of(31);
        assertThrows(DateTimeException.class, () -> day31.adjustInto(aprilFirst));
    }

    @Test
    public void adjustIntoShouldRejectFebruary29InNonLeapYear() {
        LocalDate februaryFirst = LocalDate.of(NON_LEAP_YEAR, 2, 1);
        DayOfMonth day29 = DayOfMonth.of(29);
        assertThrows(DateTimeException.class, () -> day29.adjustInto(februaryFirst));
    }

    @Test
    public void adjustIntoShouldRejectNonIsoChronology() {
        assertThrows(DateTimeException.class, () -> SAMPLE_DAY.adjustInto(JapaneseDate.now()));
    }

    @Test
    public void adjustIntoShouldRejectNullTemporal() {
        assertThrows(NullPointerException.class, () -> SAMPLE_DAY.adjustInto((Temporal) null));
    }

    // ========== Combination Method Tests - atMonth ==========

    @Test
    public void atMonthWithMonthEnumShouldHandleDay31Correctly() {
        DayOfMonth day31 = DayOfMonth.of(31);
        
        // Test month-end adjustments for day 31
        assertEquals(MonthDay.of(JANUARY, 31), day31.atMonth(JANUARY));
        assertEquals(MonthDay.of(FEBRUARY, 29), day31.atMonth(FEBRUARY)); // Adjusted to Feb 29
        assertEquals(MonthDay.of(MARCH, 31), day31.atMonth(MARCH));
        assertEquals(MonthDay.of(APRIL, 30), day31.atMonth(APRIL)); // Adjusted to Apr 30
        assertEquals(MonthDay.of(MAY, 31), day31.atMonth(MAY));
        assertEquals(MonthDay.of(JUNE, 30), day31.atMonth(JUNE)); // Adjusted to Jun 30
        assertEquals(MonthDay.of(JULY, 31), day31.atMonth(JULY));
        assertEquals(MonthDay.of(AUGUST, 31), day31.atMonth(AUGUST));
        assertEquals(MonthDay.of(SEPTEMBER, 30), day31.atMonth(SEPTEMBER)); // Adjusted to Sep 30
        assertEquals(MonthDay.of(OCTOBER, 31), day31.atMonth(OCTOBER));
        assertEquals(MonthDay.of(NOVEMBER, 30), day31.atMonth(NOVEMBER)); // Adjusted to Nov 30
        assertEquals(MonthDay.of(DECEMBER, 31), day31.atMonth(DECEMBER));
    }

    @Test
    public void atMonthWithMonthEnumShouldPreserveValidDays() {
        DayOfMonth day28 = DayOfMonth.of(28);
        
        // Day 28 should be preserved for all months
        for (Month month : Month.values()) {
            assertEquals(MonthDay.of(month, 28), day28.atMonth(month));
        }
    }

    @Test
    public void atMonthWithMonthEnumShouldRejectNull() {
        assertThrows(NullPointerException.class, () -> SAMPLE_DAY.atMonth((Month) null));
    }

    @Test
    public void atMonthWithIntShouldHandleDay31Correctly() {
        DayOfMonth day31 = DayOfMonth.of(31);
        
        assertEquals(MonthDay.of(1, 31), day31.atMonth(1));
        assertEquals(MonthDay.of(2, 29), day31.atMonth(2)); // Adjusted to Feb 29
        assertEquals(MonthDay.of(3, 31), day31.atMonth(3));
        assertEquals(MonthDay.of(4, 30), day31.atMonth(4)); // Adjusted to Apr 30
        assertEquals(MonthDay.of(5, 31), day31.atMonth(5));
        assertEquals(MonthDay.of(6, 30), day31.atMonth(6)); // Adjusted to Jun 30
        assertEquals(MonthDay.of(7, 31), day31.atMonth(7));
        assertEquals(MonthDay.of(8, 31), day31.atMonth(8));
        assertEquals(MonthDay.of(9, 30), day31.atMonth(9)); // Adjusted to Sep 30
        assertEquals(MonthDay.of(10, 31), day31.atMonth(10));
        assertEquals(MonthDay.of(11, 30), day31.atMonth(11)); // Adjusted to Nov 30
        assertEquals(MonthDay.of(12, 31), day31.atMonth(12));
    }

    @Test
    public void atMonthWithIntShouldPreserveValidDays() {
        DayOfMonth day28 = DayOfMonth.of(28);
        
        for (int month = 1; month <= 12; month++) {
            assertEquals(MonthDay.of(month, 28), day28.atMonth(month));
        }
    }

    @Test
    public void atMonthWithIntShouldRejectInvalidMonths() {
        assertThrows(DateTimeException.class, () -> SAMPLE_DAY.atMonth(0));
        assertThrows(DateTimeException.class, () -> SAMPLE_DAY.atMonth(13));
    }

    // ========== Combination Method Tests - atYearMonth ==========

    @Test
    public void atYearMonthShouldHandleDay31WithMonthEndAdjustments() {
        DayOfMonth day31 = DayOfMonth.of(31);
        
        assertEquals(LocalDate.of(2012, 1, 31), day31.atYearMonth(YearMonth.of(2012, 1)));
        assertEquals(LocalDate.of(2012, 2, 29), day31.atYearMonth(YearMonth.of(2012, 2))); // Leap year
        assertEquals(LocalDate.of(2012, 3, 31), day31.atYearMonth(YearMonth.of(2012, 3)));
        assertEquals(LocalDate.of(2012, 4, 30), day31.atYearMonth(YearMonth.of(2012, 4)));
        assertEquals(LocalDate.of(2012, 5, 31), day31.atYearMonth(YearMonth.of(2012, 5)));
        assertEquals(LocalDate.of(2012, 6, 30), day31.atYearMonth(YearMonth.of(2012, 6)));
        assertEquals(LocalDate.of(2012, 7, 31), day31.atYearMonth(YearMonth.of(2012, 7)));
        assertEquals(LocalDate.of(2012, 8, 31), day31.atYearMonth(YearMonth.of(2012, 8)));
        assertEquals(LocalDate.of(2012, 9, 30), day31.atYearMonth(YearMonth.of(2012, 9)));
        assertEquals(LocalDate.of(2012, 10, 31), day31.atYearMonth(YearMonth.of(2012, 10)));
        assertEquals(LocalDate.of(2012, 11, 30), day31.atYearMonth(YearMonth.of(2012, 11)));
        assertEquals(LocalDate.of(2012, 12, 31), day31.atYearMonth(YearMonth.of(2012, 12)));
        
        // Non-leap year February
        assertEquals(LocalDate.of(2011, 2, 28), day31.atYearMonth(YearMonth.of(2011, 2)));
    }

    @Test
    public void atYearMonthShouldPreserveValidDays() {
        DayOfMonth day28 = DayOfMonth.of(28);
        
        for (int month = 1; month <= 12; month++) {
            assertEquals(LocalDate.of(2012, month, 28), day28.atYearMonth(YearMonth.of(2012, month)));
        }
    }

    @Test
    public void atYearMonthShouldRejectNull() {
        assertThrows(NullPointerException.class, () -> SAMPLE_DAY.atYearMonth(null));
    }

    // ========== Comparison Tests ==========

    @Test
    public void compareToShouldOrderByDayValue() {
        for (int i = 1; i <= MAX_DAY_OF_MONTH; i++) {
            DayOfMonth dayA = DayOfMonth.of(i);
            
            for (int j = 1; j <= MAX_DAY_OF_MONTH; j++) {
                DayOfMonth dayB = DayOfMonth.of(j);
                
                if (i < j) {
                    assertTrue(dayA.compareTo(dayB) < 0);
                    assertTrue(dayB.compareTo(dayA) > 0);
                } else if (i > j) {
                    assertTrue(dayA.compareTo(dayB) > 0);
                    assertTrue(dayB.compareTo(dayA) < 0);
                } else {
                    assertEquals(0, dayA.compareTo(dayB));
                    assertEquals(0, dayB.compareTo(dayA));
                }
            }
        }
    }

    @Test
    public void compareToShouldRejectNull() {
        assertThrows(NullPointerException.class, () -> SAMPLE_DAY.compareTo(null));
    }

    // ========== Equals and HashCode Tests ==========

    @Test
    public void equalsAndHashCodeShouldFollowContract() {
        EqualsTester equalsTester = new EqualsTester();
        for (int day = 1; day <= MAX_DAY_OF_MONTH; day++) {
            equalsTester.addEqualityGroup(DayOfMonth.of(day), DayOfMonth.of(day));
        }
        equalsTester.testEquals();
    }

    // ========== String Representation Tests ==========

    @Test
    public void toStringShouldShowDayValue() {
        for (int day = 1; day <= MAX_DAY_OF_MONTH; day++) {
            DayOfMonth dayOfMonth = DayOfMonth.of(day);
            assertEquals("DayOfMonth:" + day, dayOfMonth.toString());
        }
    }
}