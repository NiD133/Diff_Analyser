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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.RetryingTest;

import static java.time.temporal.ChronoField.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test DayOfYear.
 */
public class TestDayOfYear {

    private static final Year STANDARD_YEAR = Year.of(2007);
    private static final Year LEAP_YEAR = Year.of(2008);
    private static final int DAYS_IN_STANDARD_YEAR = 365;
    private static final int DAYS_IN_LEAP_YEAR = 366;
    private static final DayOfYear TEST_DAY = DayOfYear.of(12);
    private static final ZoneId PARIS = ZoneId.of("Europe/Paris");

    // Helper class for testing custom temporal fields
    private static class TestingField implements TemporalField {
        public static final TestingField INSTANCE = new TestingField();

        @Override public TemporalUnit getBaseUnit() { return ChronoUnit.DAYS; }
        @Override public TemporalUnit getRangeUnit() { return ChronoUnit.YEARS; }
        @Override public ValueRange range() { return ValueRange.of(1, 365, 366); }
        @Override public boolean isDateBased() { return true; }
        @Override public boolean isTimeBased() { return false; }
        @Override public boolean isSupportedBy(TemporalAccessor temporal) { 
            return temporal.isSupported(DAY_OF_YEAR); 
        }
        @Override public ValueRange rangeRefinedBy(TemporalAccessor temporal) { return range(); }
        @Override public long getFrom(TemporalAccessor temporal) { 
            return temporal.getLong(DAY_OF_YEAR); 
        }
        @Override public <R extends Temporal> R adjustInto(R temporal, long newValue) {
            return (R) temporal.with(DAY_OF_YEAR, newValue);
        }
    }

    //-----------------------------------------------------------------------
    // Class Structure Verification
    //-----------------------------------------------------------------------
    
    @Test
    public void class_implements_expected_interfaces() {
        assertTrue(Serializable.class.isAssignableFrom(DayOfYear.class));
        assertTrue(Comparable.class.isAssignableFrom(DayOfYear.class));
        assertTrue(TemporalAdjuster.class.isAssignableFrom(DayOfYear.class));
        assertTrue(TemporalAccessor.class.isAssignableFrom(DayOfYear.class));
    }

    @Test
    public void serialization_preserves_singleton_instances() throws Exception {
        DayOfYear original = DayOfYear.of(1);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(original);
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
             new ByteArrayInputStream(baos.toByteArray()))) {
            DayOfYear deserialized = (DayOfYear) ois.readObject();
            assertSame(original, deserialized);
        }
    }

    //-----------------------------------------------------------------------
    // now() Factory Methods
    //-----------------------------------------------------------------------
    
    @RetryingTest(100)
    public void now_uses_system_default_zone() {
        assertEquals(LocalDate.now().getDayOfYear(), 
                     DayOfYear.now().getValue());
    }

    @RetryingTest(100)
    public void now_with_zoneId_uses_specified_zone() {
        ZoneId tokyo = ZoneId.of("Asia/Tokyo");
        assertEquals(LocalDate.now(tokyo).getDayOfYear(), 
                     DayOfYear.now(tokyo).getValue());
    }

    @Test
    public void now_with_clock_in_standard_year() {
        LocalDate date = LocalDate.of(2007, 1, 1);
        for (int i = 1; i <= DAYS_IN_STANDARD_YEAR; i++) {
            Instant instant = date.atStartOfDay(PARIS).toInstant();
            Clock clock = Clock.fixed(instant, PARIS);
            DayOfYear test = DayOfYear.now(clock);
            assertEquals(i, test.getValue());
            date = date.plusDays(1);
        }
    }

    @Test
    public void now_with_clock_in_leap_year() {
        LocalDate date = LocalDate.of(2008, 1, 1);
        for (int i = 1; i <= DAYS_IN_LEAP_YEAR; i++) {
            Instant instant = date.atStartOfDay(PARIS).toInstant();
            Clock clock = Clock.fixed(instant, PARIS);
            DayOfYear test = DayOfYear.now(clock);
            assertEquals(i, test.getValue());
            date = date.plusDays(1);
        }
    }

    //-----------------------------------------------------------------------
    // of() Factory Method
    //-----------------------------------------------------------------------
    
    @Test
    public void of_valid_days_returns_cached_instances() {
        for (int i = 1; i <= DAYS_IN_LEAP_YEAR; i++) {
            DayOfYear instance = DayOfYear.of(i);
            assertEquals(i, instance.getValue());
            assertSame(instance, DayOfYear.of(i)); // Verify instance caching
        }
    }

    @Test
    public void of_dayZero_throws_exception() {
        assertThrows(DateTimeException.class, () -> DayOfYear.of(0));
    }

    @Test
    public void of_day367_throws_exception() {
        assertThrows(DateTimeException.class, () -> DayOfYear.of(367));
    }

    //-----------------------------------------------------------------------
    // from() Factory Method
    //-----------------------------------------------------------------------
    
    @Test
    public void from_LocalDate_standard_year() {
        LocalDate date = LocalDate.of(2007, 1, 1);
        for (int i = 1; i <= DAYS_IN_STANDARD_YEAR; i++) {
            assertEquals(i, DayOfYear.from(date).getValue());
            date = date.plusDays(1);
        }
        // Verify rollover to next year
        assertEquals(1, DayOfYear.from(date).getValue());
    }

    @Test
    public void from_LocalDate_leap_year() {
        LocalDate date = LocalDate.of(2008, 1, 1);
        for (int i = 1; i <= DAYS_IN_LEAP_YEAR; i++) {
            assertEquals(i, DayOfYear.from(date).getValue());
            date = date.plusDays(1);
        }
    }

    @Test
    public void from_DayOfYear_returns_same_instance() {
        DayOfYear instance = DayOfYear.of(6);
        assertSame(instance, DayOfYear.from(instance));
    }

    @Test
    public void from_nonIsoTemporal_returns_correct_value() {
        LocalDate date = LocalDate.now();
        JapaneseDate japaneseDate = JapaneseDate.from(date);
        assertEquals(date.getDayOfYear(), 
                     DayOfYear.from(japaneseDate).getValue());
    }

    @Test
    public void from_unsupported_temporal_throws_exception() {
        assertThrows(DateTimeException.class, 
                     () -> DayOfYear.from(LocalTime.NOON));
    }

    @Test
    public void from_null_throws_exception() {
        assertThrows(NullPointerException.class, 
                     () -> DayOfYear.from((TemporalAccessor) null));
    }

    @Test
    public void from_parsed_string() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("D");
        assertEquals(DayOfYear.of(76), 
                     formatter.parse("76", DayOfYear::from));
    }

    //-----------------------------------------------------------------------
    // getValue()
    //-----------------------------------------------------------------------
    
    @Test
    public void getValue_returns_day_of_year() {
        DayOfYear instance = DayOfYear.of(12);
        assertEquals(12, instance.getValue());
    }

    //-----------------------------------------------------------------------
    // isSupported()
    //-----------------------------------------------------------------------
    
    @Test
    public void isSupported_for_various_temporal_fields() {
        // Supported fields
        assertTrue(TEST_DAY.isSupported(DAY_OF_YEAR));
        assertTrue(TEST_DAY.isSupported(TestingField.INSTANCE));

        // Unsupported fields
        assertFalse(TEST_DAY.isSupported(null));
        assertFalse(TEST_DAY.isSupported(NANO_OF_SECOND));
        assertFalse(TEST_DAY.isSupported(NANO_OF_DAY));
        assertFalse(TEST_DAY.isSupported(MICRO_OF_SECOND));
        assertFalse(TEST_DAY.isSupported(MICRO_OF_DAY));
        assertFalse(TEST_DAY.isSupported(MILLI_OF_SECOND));
        assertFalse(TEST_DAY.isSupported(MILLI_OF_DAY));
        assertFalse(TEST_DAY.isSupported(SECOND_OF_MINUTE));
        assertFalse(TEST_DAY.isSupported(SECOND_OF_DAY));
        assertFalse(TEST_DAY.isSupported(MINUTE_OF_HOUR));
        assertFalse(TEST_DAY.isSupported(MINUTE_OF_DAY));
        assertFalse(TEST_DAY.isSupported(HOUR_OF_AMPM));
        assertFalse(TEST_DAY.isSupported(CLOCK_HOUR_OF_AMPM));
        assertFalse(TEST_DAY.isSupported(HOUR_OF_DAY));
        assertFalse(TEST_DAY.isSupported(CLOCK_HOUR_OF_DAY));
        assertFalse(TEST_DAY.isSupported(AMPM_OF_DAY));
        assertFalse(TEST_DAY.isSupported(DAY_OF_WEEK));
        assertFalse(TEST_DAY.isSupported(ALIGNED_DAY_OF_WEEK_IN_MONTH));
        assertFalse(TEST_DAY.isSupported(ALIGNED_DAY_OF_WEEK_IN_YEAR));
        assertFalse(TEST_DAY.isSupported(DAY_OF_MONTH));
        assertFalse(TEST_DAY.isSupported(EPOCH_DAY));
        assertFalse(TEST_DAY.isSupported(ALIGNED_WEEK_OF_MONTH));
        assertFalse(TEST_DAY.isSupported(ALIGNED_WEEK_OF_YEAR));
        assertFalse(TEST_DAY.isSupported(MONTH_OF_YEAR));
        assertFalse(TEST_DAY.isSupported(PROLEPTIC_MONTH));
        assertFalse(TEST_DAY.isSupported(YEAR_OF_ERA));
        assertFalse(TEST_DAY.isSupported(YEAR));
        assertFalse(TEST_DAY.isSupported(ERA));
        assertFalse(TEST_DAY.isSupported(INSTANT_SECONDS));
        assertFalse(TEST_DAY.isSupported(OFFSET_SECONDS));
    }

    //-----------------------------------------------------------------------
    // range()
    //-----------------------------------------------------------------------
    
    @Test
    public void range_for_dayOfYear_returns_valid_range() {
        assertEquals(DAY_OF_YEAR.range(), TEST_DAY.range(DAY_OF_YEAR));
    }

    @Test
    public void range_unsupported_field_throws_exception() {
        assertThrows(UnsupportedTemporalTypeException.class, 
                     () -> TEST_DAY.range(MONTH_OF_YEAR));
    }

    @Test
    public void range_null_throws_exception() {
        assertThrows(NullPointerException.class, 
                     () -> TEST_DAY.range(null));
    }

    //-----------------------------------------------------------------------
    // get()
    //-----------------------------------------------------------------------
    
    @Test
    public void get_for_dayOfYear_returns_value() {
        assertEquals(12, TEST_DAY.get(DAY_OF_YEAR));
    }

    @Test
    public void get_unsupported_field_throws_exception() {
        assertThrows(UnsupportedTemporalTypeException.class, 
                     () -> TEST_DAY.get(MONTH_OF_YEAR));
    }

    @Test
    public void get_null_throws_exception() {
        assertThrows(NullPointerException.class, 
                     () -> TEST_DAY.get(null));
    }

    //-----------------------------------------------------------------------
    // getLong()
    //-----------------------------------------------------------------------
    
    @Test
    public void getLong_for_dayOfYear_returns_value() {
        assertEquals(12L, TEST_DAY.getLong(DAY_OF_YEAR));
    }

    @Test
    public void getLong_for_custom_field_returns_value() {
        assertEquals(12L, TEST_DAY.getLong(TestingField.INSTANCE));
    }

    @Test
    public void getLong_unsupported_field_throws_exception() {
        assertThrows(UnsupportedTemporalTypeException.class, 
                     () -> TEST_DAY.getLong(MONTH_OF_YEAR));
    }

    @Test
    public void getLong_unsupported_derived_field_throws_exception() {
        assertThrows(UnsupportedTemporalTypeException.class, 
                     () -> TEST_DAY.getLong(IsoFields.DAY_OF_QUARTER));
    }

    @Test
    public void getLong_null_throws_exception() {
        assertThrows(NullPointerException.class, 
                     () -> TEST_DAY.getLong(null));
    }

    //-----------------------------------------------------------------------
    // isValidYear()
    //-----------------------------------------------------------------------
    
    @Test
    public void isValidYear_for_day366() {
        DayOfYear day366 = DayOfYear.of(366);
        assertFalse(day366.isValidYear(2011)); // Non-leap year
        assertTrue(day366.isValidYear(2012));  // Leap year
        assertFalse(day366.isValidYear(2013)); // Non-leap year
    }

    @Test
    public void isValidYear_for_day365() {
        DayOfYear day365 = DayOfYear.of(365);
        assertTrue(day365.isValidYear(2011));
        assertTrue(day365.isValidYear(2012));
        assertTrue(day365.isValidYear(2013));
    }

    //-----------------------------------------------------------------------
    // query()
    //-----------------------------------------------------------------------
    
    @Test
    public void query_returns_expected_values() {
        assertEquals(IsoChronology.INSTANCE, 
                     TEST_DAY.query(TemporalQueries.chronology()));
        assertNull(TEST_DAY.query(TemporalQueries.localDate()));
        assertNull(TEST_DAY.query(TemporalQueries.localTime()));
        assertNull(TEST_DAY.query(TemporalQueries.offset()));
        assertNull(TEST_DAY.query(TemporalQueries.precision()));
        assertNull(TEST_DAY.query(TemporalQueries.zone()));
        assertNull(TEST_DAY.query(TemporalQueries.zoneId()));
    }

    //-----------------------------------------------------------------------
    // adjustInto()
    //-----------------------------------------------------------------------
    
    @Test
    public void adjustInto_standard_year_start() {
        LocalDate startDate = LocalDate.of(2007, 1, 1);
        LocalDate currentDate = startDate;
        
        for (int day = 1; day <= DAYS_IN_STANDARD_YEAR; day++) {
            DayOfYear dayOfYear = DayOfYear.of(day);
            assertEquals(currentDate, dayOfYear.adjustInto(startDate));
            currentDate = currentDate.plusDays(1);
        }
    }

    @Test
    public void adjustInto_standard_year_end() {
        LocalDate endDate = LocalDate.of(2007, 12, 31);
        LocalDate expectedDate = LocalDate.of(2007, 1, 1);
        
        for (int day = 1; day <= DAYS_IN_STANDARD_YEAR; day++) {
            DayOfYear dayOfYear = DayOfYear.of(day);
            assertEquals(expectedDate, dayOfYear.adjustInto(endDate));
            expectedDate = expectedDate.plusDays(1);
        }
    }

    @Test
    public void adjustInto_standard_year_invalid_day366_throws() {
        LocalDate baseDate = LocalDate.of(2007, 1, 1);
        DayOfYear day366 = DayOfYear.of(366);
        assertThrows(DateTimeException.class, () -> day366.adjustInto(baseDate));
    }

    @Test
    public void adjustInto_leap_year_start() {
        LocalDate startDate = LocalDate.of(2008, 1, 1);
        LocalDate currentDate = startDate;
        
        for (int day = 1; day <= DAYS_IN_LEAP_YEAR; day++) {
            DayOfYear dayOfYear = DayOfYear.of(day);
            assertEquals(currentDate, dayOfYear.adjustInto(startDate));
            currentDate = currentDate.plusDays(1);
        }
    }

    @Test
    public void adjustInto_leap_year_end() {
        LocalDate endDate = LocalDate.of(2008, 12, 31);
        LocalDate expectedDate = LocalDate.of(2008, 1, 1);
        
        for (int day = 1; day <= DAYS_IN_LEAP_YEAR; day++) {
            DayOfYear dayOfYear = DayOfYear.of(day);
            assertEquals(expectedDate, dayOfYear.adjustInto(endDate));
            expectedDate = expectedDate.plusDays(1);
        }
    }

    @Test
    public void adjustInto_nonIso_throws_exception() {
        assertThrows(DateTimeException.class, 
                     () -> TEST_DAY.adjustInto(JapaneseDate.now()));
    }

    @Test
    public void adjustInto_null_throws_exception() {
        assertThrows(NullPointerException.class, 
                     () -> TEST_DAY.adjustInto(null));
    }

    //-----------------------------------------------------------------------
    // atYear()
    //-----------------------------------------------------------------------
    
    @Test
    public void atYear_withYearObject_standard_year() {
        LocalDate expectedDate = LocalDate.of(2007, 1, 1);
        
        for (int day = 1; day <= DAYS_IN_STANDARD_YEAR; day++) {
            DayOfYear dayOfYear = DayOfYear.of(day);
            assertEquals(expectedDate, dayOfYear.atYear(STANDARD_YEAR));
            expectedDate = expectedDate.plusDays(1);
        }
    }

    @Test
    public void atYear_withYearObject_day366_standard_year_throws() {
        DayOfYear day366 = DayOfYear.of(366);
        assertThrows(DateTimeException.class, 
                     () -> day366.atYear(STANDARD_YEAR));
    }

    @Test
    public void atYear_withYearObject_leap_year() {
        LocalDate expectedDate = LocalDate.of(2008, 1, 1);
        
        for (int day = 1; day <= DAYS_IN_LEAP_YEAR; day++) {
            DayOfYear dayOfYear = DayOfYear.of(day);
            assertEquals(expectedDate, dayOfYear.atYear(LEAP_YEAR));
            expectedDate = expectedDate.plusDays(1);
        }
    }

    @Test
    public void atYear_withYearObject_null_throws_exception() {
        assertThrows(NullPointerException.class, 
                     () -> TEST_DAY.atYear((Year) null));
    }

    @Test
    public void atYear_withInt_standard_year() {
        LocalDate expectedDate = LocalDate.of(2007, 1, 1);
        
        for (int day = 1; day <= DAYS_IN_STANDARD_YEAR; day++) {
            DayOfYear dayOfYear = DayOfYear.of(day);
            assertEquals(expectedDate, dayOfYear.atYear(2007));
            expectedDate = expectedDate.plusDays(1);
        }
    }

    @Test
    public void atYear_withInt_day366_standard_year_throws() {
        DayOfYear day366 = DayOfYear.of(366);
        assertThrows(DateTimeException.class, 
                     () -> day366.atYear(2007));
    }

    @Test
    public void atYear_withInt_leap_year() {
        LocalDate expectedDate = LocalDate.of(2008, 1, 1);
        
        for (int day = 1; day <= DAYS_IN_LEAP_YEAR; day++) {
            DayOfYear dayOfYear = DayOfYear.of(day);
            assertEquals(expectedDate, dayOfYear.atYear(2008));
            expectedDate = expectedDate.plusDays(1);
        }
    }

    @Test
    public void atYear_withInt_invalid_year_throws() {
        assertThrows(DateTimeException.class, 
                     () -> TEST_DAY.atYear(Year.MIN_VALUE - 1));
    }

    //-----------------------------------------------------------------------
    // compareTo()
    //-----------------------------------------------------------------------
    
    @Test
    public void compareTo_orders_by_day_of_year() {
        for (int i = 1; i <= DAYS_IN_LEAP_YEAR; i++) {
            DayOfYear a = DayOfYear.of(i);
            
            for (int j = 1; j <= DAYS_IN_LEAP_YEAR; j++) {
                DayOfYear b = DayOfYear.of(j);
                int expected = Integer.compare(i, j);
                assertEquals(expected, a.compareTo(b));
            }
        }
    }

    @Test
    public void compareTo_null_throws_exception() {
        DayOfYear instance = DayOfYear.of(1);
        assertThrows(NullPointerException.class, 
                     () -> instance.compareTo(null));
    }

    //-----------------------------------------------------------------------
    // equals() / hashCode()
    //-----------------------------------------------------------------------
    
    @Test
    public void equals_and_hashCode_consistent() {
        EqualsTester tester = new EqualsTester();
        
        // Add equality groups (each group contains objects that should be equal)
        for (int i = 1; i <= DAYS_IN_LEAP_YEAR; i++) {
            tester.addEqualityGroup(DayOfYear.of(i), DayOfYear.of(i));
        }
        
        tester.testEquals();
    }

    //-----------------------------------------------------------------------
    // toString()
    //-----------------------------------------------------------------------
    
    @Test
    public void toString_contains_day_value() {
        for (int i = 1; i <= DAYS_IN_LEAP_YEAR; i++) {
            DayOfYear instance = DayOfYear.of(i);
            assertEquals("DayOfYear:" + i, instance.toString());
        }
    }
}