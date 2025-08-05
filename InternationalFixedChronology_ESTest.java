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
package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import java.time.temporal.ChronoField;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * A test suite for the {@link InternationalFixedChronology}.
 */
public class InternationalFixedChronologyTest {

    private static final InternationalFixedChronology IFC = InternationalFixedChronology.INSTANCE;
    private static final ZoneId UTC = ZoneOffset.UTC;

    //-----------------------------------------------------------------------
    // isLeapYear()
    //-----------------------------------------------------------------------

    @Test
    public void isLeapYear_whenDivisibleBy4ButNot100_isTrue() {
        assertTrue(IFC.isLeapYear(4));
        assertTrue(IFC.isLeapYear(2004));
        assertTrue(IFC.isLeapYear(-3708));
    }

    @Test
    public void isLeapYear_whenDivisibleBy400_isTrue() {
        assertTrue(IFC.isLeapYear(2000));
        assertTrue(IFC.isLeapYear(400));
    }

    @Test
    public void isLeapYear_whenDivisibleBy100ButNot400_isFalse() {
        assertFalse(IFC.isLeapYear(100));
        assertFalse(IFC.isLeapYear(1900));
    }

    @Test
    public void isLeapYear_whenNotDivisibleBy4_isFalse() {
        assertFalse(IFC.isLeapYear(1));
        assertFalse(IFC.isLeapYear(2001));
        assertFalse(IFC.isLeapYear(241L));
    }

    //-----------------------------------------------------------------------
    // date() factory methods
    //-----------------------------------------------------------------------

    @Test
    public void date_withYearMonthDay_createsCorrectDate() {
        InternationalFixedDate date = IFC.date(3309, 8, 10);
        assertEquals(3309, date.get(ChronoField.YEAR));
        assertEquals(8, date.get(ChronoField.MONTH_OF_YEAR));
        assertEquals(10, date.get(ChronoField.DAY_OF_MONTH));
        assertEquals(489265L, date.toEpochDay());
    }

    @Test
    public void date_withEraYearMonthDay_createsCorrectDate() {
        InternationalFixedDate date = IFC.date(InternationalFixedEra.CE, 7, 7, 7);
        assertEquals(7, date.get(ChronoField.YEAR));
        assertEquals(7, date.get(ChronoField.MONTH_OF_YEAR));
        assertEquals(7, date.get(ChronoField.DAY_OF_MONTH));
        assertEquals(-716797L, date.toEpochDay());
    }

    @Test
    public void dateYearDay_createsCorrectDate() {
        InternationalFixedDate date = IFC.dateYearDay(134, 134);
        assertEquals(134, date.get(ChronoField.YEAR));
        assertEquals(134, date.get(ChronoField.DAY_OF_YEAR));
        assertEquals(-670452L, date.toEpochDay());
        assertEquals(365, date.lengthOfYear());
    }

    @Test
    public void dateYearDay_withEra_createsCorrectDate() {
        InternationalFixedDate date = IFC.dateYearDay(InternationalFixedEra.CE, 157, 3);
        assertEquals(157, date.get(ChronoField.YEAR));
        assertEquals(3, date.get(ChronoField.DAY_OF_YEAR));
        assertEquals(-662182L, date.toEpochDay());
        assertEquals(365, date.lengthOfYear());
    }

    @Test
    public void dateEpochDay_createsCorrectDate() {
        InternationalFixedDate dateAtEpoch = IFC.dateEpochDay(0L);
        assertEquals(1970, dateAtEpoch.get(ChronoField.YEAR));
        assertEquals(1, dateAtEpoch.get(ChronoField.MONTH_OF_YEAR));
        assertEquals(1, dateAtEpoch.get(ChronoField.DAY_OF_MONTH));

        InternationalFixedDate dateBeforeEpoch = IFC.dateEpochDay(-2219L);
        assertEquals(1964, dateBeforeEpoch.get(ChronoField.YEAR)); // A leap year
        assertEquals(366, dateBeforeEpoch.lengthOfYear());

        InternationalFixedDate dateAfterEpoch = IFC.dateEpochDay(146096L);
        assertEquals(2369, dateAfterEpoch.get(ChronoField.YEAR));
        assertEquals(365, dateAfterEpoch.lengthOfYear());
    }

    @Test
    public void date_fromTemporalAccessor_createsCorrectDate() {
        InternationalFixedDate originalDate = IFC.date(2024, 6, 1);
        InternationalFixedDate fromTemporal = IFC.date(originalDate);
        assertEquals(originalDate, fromTemporal);
    }

    //-----------------------------------------------------------------------
    // dateNow() factory methods
    //-----------------------------------------------------------------------

    @Test
    public void dateNow_withClock_returnsDateFromClock() {
        Instant instant = Instant.parse("2025-07-21T19:49:23Z"); // Corresponds to epoch day 20289
        Clock clock = Clock.fixed(instant, UTC);

        InternationalFixedDate date = IFC.dateNow(clock);
        InternationalFixedDate expected = IFC.dateEpochDay(20289);

        assertEquals(expected, date);
        assertEquals(2025, date.get(ChronoField.YEAR));
    }

    @Test
    public void dateNow_withZone_usesSystemClockInThatZone() {
        // This test is dependent on the system clock, but verifies the method call.
        ZoneId zone = ZoneId.of("America/New_York");
        InternationalFixedDate dateFromZone = IFC.dateNow(zone);
        InternationalFixedDate dateFromClock = IFC.dateNow(Clock.system(zone));
        assertEquals(dateFromClock, dateFromZone);
    }

    //-----------------------------------------------------------------------
    // zonedDateTime() and localDateTime()
    //-----------------------------------------------------------------------

    @Test
    public void zonedDateTime_fromInstant_createsCorrectZonedDateTime() {
        Instant instant = Instant.ofEpochSecond(-1512L, 107016L);
        ZoneOffset zone = ZoneOffset.MAX;

        ChronoZonedDateTime<InternationalFixedDate> zdt = IFC.zonedDateTime(instant, zone);

        assertNotNull(zdt);
        assertEquals(IFC, zdt.getChronology());
        assertEquals(instant, zdt.toInstant());
        assertEquals(zone, zdt.getZone());
    }

    @Test
    public void zonedDateTime_fromTemporalAccessor_createsCorrectZonedDateTime() {
        ZonedDateTime isoZdt = ZonedDateTime.now();
        ChronoZonedDateTime<InternationalFixedDate> ifcZdt = IFC.zonedDateTime(isoZdt);

        assertEquals(IFC, ifcZdt.getChronology());
        assertEquals(isoZdt.toInstant(), ifcZdt.toInstant());
        assertEquals(isoZdt.getZone(), ifcZdt.getZone());
    }

    @Test
    public void localDateTime_fromTemporalAccessor_createsCorrectLocalDateTime() {
        LocalDateTime isoLdt = LocalDateTime.now();
        ChronoLocalDateTime<InternationalFixedDate> ifcLdt = IFC.localDateTime(isoLdt);

        assertEquals(IFC, ifcLdt.getChronology());
        assertEquals(isoLdt.toLocalTime(), ifcLdt.toLocalTime());
        assertEquals(IFC.date(isoLdt.toLocalDate()), ifcLdt.toLocalDate());
    }

    //-----------------------------------------------------------------------
    // prolepticYear(), eraOf(), eras()
    //-----------------------------------------------------------------------

    @Test
    public void prolepticYear_forCEEra_returnsYearOfEra() {
        assertEquals(734, IFC.prolepticYear(InternationalFixedEra.CE, 734));
        assertEquals(1, IFC.prolepticYear(InternationalFixedEra.CE, 1));
    }

    @Test
    public void eraOf_forValue1_returnsCE() {
        assertEquals(InternationalFixedEra.CE, IFC.eraOf(1));
    }

    @Test
    public void eras_returnsSingletonListWithCE() {
        List<Era> eras = IFC.eras();
        assertEquals(1, eras.size());
        assertEquals(InternationalFixedEra.CE, eras.get(0));
    }

    //-----------------------------------------------------------------------
    // getId(), getCalendarType()
    //-----------------------------------------------------------------------

    @Test
    public void getId_returnsIfc() {
        assertEquals("Ifc", IFC.getId());
    }

    @Test
    public void getCalendarType_returnsNull() {
        assertNull(IFC.getCalendarType());
    }

    //-----------------------------------------------------------------------
    // range()
    //-----------------------------------------------------------------------

    @Test
    public void range_forSupportedFields_returnsCorrectRanges() {
        assertEquals(ValueRange.of(1, 1), IFC.range(ChronoField.ERA));
        assertEquals(ValueRange.of(1, 1_000_000), IFC.range(ChronoField.YEAR_OF_ERA));
        assertEquals(ValueRange.of(1, 1_000_000), IFC.range(ChronoField.YEAR));
        assertEquals(ValueRange.of(1, 13), IFC.range(ChronoField.MONTH_OF_YEAR));
        assertEquals(ValueRange.of(1, 28, 29), IFC.range(ChronoField.DAY_OF_MONTH));
        assertEquals(ValueRange.of(1, 365, 366), IFC.range(ChronoField.DAY_OF_YEAR));
        assertEquals(ValueRange.of(1, 7), IFC.range(ChronoField.DAY_OF_WEEK));
        assertEquals(ValueRange.of(1, 52, 53), IFC.range(ChronoField.ALIGNED_WEEK_OF_YEAR));
    }

    //-----------------------------------------------------------------------
    // Exception handling
    //-----------------------------------------------------------------------

    @Test(expected = DateTimeException.class)
    public void date_withInvalidMonth_throwsException() {
        IFC.date(2024, 14, 1);
    }

    @Test(expected = DateTimeException.class)
    public void date_withInvalidDayForMonth_throwsException() {
        // Month 4 has 28 days.
        IFC.date(2024, 4, 29);
    }

    @Test(expected = DateTimeException.class)
    public void dateYearDay_forNonLeapYearWithDay366_throwsException() {
        IFC.dateYearDay(2025, 366);
    }

    @Test(expected = DateTimeException.class)
    public void dateYearDay_withInvalidDay_throwsException() {
        IFC.dateYearDay(2024, 367);
    }

    @Test(expected = DateTimeException.class)
    public void dateEpochDay_withOutOfRangeValue_throwsException() {
        IFC.dateEpochDay(Long.MIN_VALUE);
    }

    @Test(expected = DateTimeException.class)
    public void eraOf_withInvalidValue_throwsException() {
        IFC.eraOf(0);
    }

    @Test(expected = DateTimeException.class)
    public void prolepticYear_withInvalidYearOfEra_throwsException() {
        IFC.prolepticYear(InternationalFixedEra.CE, 0);
    }

    @Test(expected = ClassCastException.class)
    public void prolepticYear_withInvalidEraType_throwsException() {
        IFC.prolepticYear(IsoEra.BCE, 1);
    }

    @Test(expected = UnsupportedTemporalTypeException.class)
    public void date_fromTemporalWithoutEpochDay_throwsException() {
        // Month does not support EPOCH_DAY, so conversion should fail.
        IFC.date(java.time.Month.JANUARY);
    }

    @Test(expected = NullPointerException.class)
    public void date_fromNullTemporal_throwsNPE() {
        IFC.date(null);
    }

    @Test(expected = NullPointerException.class)
    public void localDateTime_fromNullTemporal_throwsNPE() {
        IFC.localDateTime(null);
    }

    @Test(expected = NullPointerException.class)
    public void zonedDateTime_fromNullTemporal_throwsNPE() {
        IFC.zonedDateTime(null);
    }
}