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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.Era;
import java.time.chrono.JapaneseEra;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * A more understandable test suite for {@link JulianChronology}.
 */
public class JulianChronology_ESTest {

    private static final JulianChronology JULIAN_CHRONO = JulianChronology.INSTANCE;
    private static final ZoneId UTC = ZoneOffset.UTC;
    private static final ZoneId CET = ZoneId.of("Europe/Paris");

    // -----------------------------------------------------------------------
    // date() factory methods
    // -----------------------------------------------------------------------

    @Test
    public void date_fromYearMonthDay() {
        JulianDate date = JULIAN_CHRONO.date(2014, 2, 14);
        assertEquals(JulianEra.AD, date.getEra());
        assertEquals(2014, date.get(ChronoField.YEAR));
        assertEquals(2, date.get(ChronoField.MONTH_OF_YEAR));
        assertEquals(14, date.get(ChronoField.DAY_OF_MONTH));
    }

    @Test
    public void date_fromEraYearMonthDay_AD() {
        JulianDate date = JULIAN_CHRONO.date(JulianEra.AD, 2014, 2, 14);
        assertEquals(JULIAN_CHRONO.date(2014, 2, 14), date);
    }

    @Test
    public void date_fromEraYearMonthDay_BC() {
        JulianDate date = JULIAN_CHRONO.date(JulianEra.BC, 1, 1, 1);
        assertEquals(0, date.get(ChronoField.PROLEPTIC_YEAR)); // Year 1 BC is proleptic year 0
        assertEquals(JulianEra.BC, date.getEra());
    }

    @Test
    public void date_fromTemporalAccessor_convertsFromIsoDate() {
        LocalDate isoDate = LocalDate.of(2014, 2, 14);
        JulianDate julianDate = JULIAN_CHRONO.date(isoDate);
        // ISO 2014-02-14 is Julian 2014-02-01
        assertEquals(JULIAN_CHRONO.date(2014, 2, 1), julianDate);
    }

    @Test
    public void date_fromEraAndNegativeYear_createsBCDate() {
        // A negative year-of-era for AD should be interpreted as a BC date
        JulianDate date = JULIAN_CHRONO.date(JulianEra.AD, -44, 3, 15);
        assertEquals(JulianEra.BC, date.getEra());
        assertEquals(45, date.get(ChronoField.YEAR_OF_ERA)); // Proleptic year -44 is 45 BC
    }

    // -----------------------------------------------------------------------
    // dateYearDay() factory methods
    // -----------------------------------------------------------------------

    @Test
    public void dateYearDay_fromProlepticYear() {
        JulianDate date = JULIAN_CHRONO.dateYearDay(2012, 366); // 2012 is a leap year
        assertEquals(2012, date.get(ChronoField.YEAR));
        assertEquals(12, date.get(ChronoField.MONTH_OF_YEAR));
        assertEquals(31, date.get(ChronoField.DAY_OF_MONTH));
    }

    @Test
    public void dateYearDay_fromEraAndYear_AD() {
        JulianDate date = JULIAN_CHRONO.dateYearDay(JulianEra.AD, 2012, 100);
        assertEquals(2012, date.get(ChronoField.YEAR));
        assertEquals(100, date.get(ChronoField.DAY_OF_YEAR));
    }

    // -----------------------------------------------------------------------
    // dateEpochDay() factory method
    // -----------------------------------------------------------------------

    @Test
    public void dateEpochDay_withPositiveEpochDay() {
        JulianDate date = JULIAN_CHRONO.dateEpochDay(0);
        // Julian epoch day 0 corresponds to ISO 1969-12-19
        assertEquals(1969, date.get(ChronoField.YEAR));
        assertEquals(12, date.get(ChronoField.MONTH_OF_YEAR));
        assertEquals(19, date.get(ChronoField.DAY_OF_MONTH));
    }

    @Test
    public void dateEpochDay_withNegativeEpochDay_returnsBCDate() {
        // Epoch day for Julian date 1 BC, Jan 1 (proleptic year 0)
        long epochDayFor1BC = JULIAN_CHRONO.date(0, 1, 1).toEpochDay();
        JulianDate date = JULIAN_CHRONO.dateEpochDay(epochDayFor1BC);
        assertEquals(JulianEra.BC, date.getEra());
        assertEquals(1, date.get(ChronoField.YEAR_OF_ERA));
    }

    // -----------------------------------------------------------------------
    // dateNow() factory methods
    // -----------------------------------------------------------------------

    @Test
    public void dateNow_withFixedClock() {
        Instant fixedInstant = LocalDateTime.of(2014, 2, 14, 10, 30).toInstant(UTC);
        Clock clock = Clock.fixed(fixedInstant, UTC);
        JulianDate date = JULIAN_CHRONO.dateNow(clock);

        // ISO 2014-02-14 is Julian 2014-02-01
        assertEquals(2014, date.get(ChronoField.YEAR));
        assertEquals(2, date.get(ChronoField.MONTH_OF_YEAR));
        assertEquals(1, date.get(ChronoField.DAY_OF_MONTH));
    }

    @Test
    public void dateNow_withFixedClockAndZone() {
        Instant fixedInstant = LocalDateTime.of(2014, 2, 14, 1, 0).toInstant(CET); // 2014-02-14 00:00 UTC
        Clock clock = Clock.fixed(fixedInstant, CET);
        JulianDate date = JULIAN_CHRONO.dateNow(clock);

        // ISO 2014-02-14 is Julian 2014-02-01
        assertEquals(JULIAN_CHRONO.date(2014, 2, 1), date);
    }

    // -----------------------------------------------------------------------
    // localDateTime() and zonedDateTime()
    // -----------------------------------------------------------------------

    @Test
    public void localDateTime_fromTemporalAccessor() {
        LocalDateTime isoLdt = LocalDateTime.of(2014, 2, 14, 12, 0);
        ChronoLocalDateTime<JulianDate> julianLdt = JULIAN_CHRONO.localDateTime(isoLdt);
        assertNotNull(julianLdt);
        assertEquals(JULIAN_CHRONO.date(2014, 2, 1), julianLdt.toLocalDate());
    }

    @Test
    public void zonedDateTime_fromInstant() {
        Instant instant = Instant.ofEpochSecond(0); // 1970-01-01T00:00:00Z
        ChronoZonedDateTime<JulianDate> julianZdt = JULIAN_CHRONO.zonedDateTime(instant, UTC);
        assertNotNull(julianZdt);
        // ISO 1970-01-01 is Julian 1969-12-19
        assertEquals(JULIAN_CHRONO.date(1969, 12, 19), julianZdt.toLocalDate());
    }

    @Test
    public void zonedDateTime_fromTemporalAccessor() {
        OffsetDateTime odt = OffsetDateTime.of(2014, 2, 14, 12, 0, 0, 0, ZoneOffset.ofHours(1));
        ChronoZonedDateTime<JulianDate> julianZdt = JULIAN_CHRONO.zonedDateTime(odt);
        assertNotNull(julianZdt);
        assertEquals(odt.toInstant(), julianZdt.toInstant());
    }

    // -----------------------------------------------------------------------
    // Core API: isLeapYear, prolepticYear, eraOf, eras, range
    // -----------------------------------------------------------------------

    @Test
    public void isLeapYear_forLeapYear_returnsTrue() {
        assertTrue(JULIAN_CHRONO.isLeapYear(2012)); // Divisible by 4
        assertTrue(JULIAN_CHRONO.isLeapYear(-4));   // BC leap year
    }

    @Test
    public void isLeapYear_forNonLeapYear_returnsFalse() {
        assertFalse(JULIAN_CHRONO.isLeapYear(2013));
    }

    @Test
    public void prolepticYear_forADEra_returnsYearOfEra() {
        assertEquals(1850, JULIAN_CHRONO.prolepticYear(JulianEra.AD, 1850));
        assertEquals(1, JULIAN_CHRONO.prolepticYear(JulianEra.AD, 1));
    }

    @Test
    public void prolepticYear_forBCEra_returnsCorrectNegativeYear() {
        assertEquals(0, JULIAN_CHRONO.prolepticYear(JulianEra.BC, 1));   // 1 BC is proleptic year 0
        assertEquals(-980, JULIAN_CHRONO.prolepticYear(JulianEra.BC, 981)); // 981 BC is proleptic year -980
    }

    @Test
    public void eraOf_returnsCorrectEra() {
        assertEquals(JulianEra.BC, JULIAN_CHRONO.eraOf(0));
        assertEquals(JulianEra.AD, JULIAN_CHRONO.eraOf(1));
    }

    @Test
    public void eras_returnsListOfEras() {
        List<Era> eras = JULIAN_CHRONO.eras();
        assertEquals(2, eras.size());
        assertTrue(eras.contains(JulianEra.BC));
        assertTrue(eras.contains(JulianEra.AD));
    }

    @Test
    public void range_forSupportedFields_returnsCorrectRanges() {
        assertEquals(ValueRange.of(1, 999_999), JULIAN_CHRONO.range(ChronoField.YEAR_OF_ERA));
        assertEquals(ValueRange.of(-999_998, 999_999), JULIAN_CHRONO.range(ChronoField.YEAR));
        assertEquals(ValueRange.of(1, 12), JULIAN_CHRONO.range(ChronoField.CLOCK_HOUR_OF_AMPM));
    }

    // -----------------------------------------------------------------------
    // resolveDate
    // -----------------------------------------------------------------------

    @Test
    public void resolveDate_withEpochDay_returnsCorrectDate() {
        Map<TemporalField, Long> fields = new HashMap<>();
        fields.put(ChronoField.EPOCH_DAY, 3L);
        JulianDate resolvedDate = JULIAN_CHRONO.resolveDate(fields, ResolverStyle.LENIENT);
        assertEquals(JULIAN_CHRONO.dateEpochDay(3L), resolvedDate);
    }

    @Test
    public void resolveDate_withEmptyMap_returnsNull() {
        assertNull(JULIAN_CHRONO.resolveDate(Collections.emptyMap(), ResolverStyle.STRICT));
    }

    // -----------------------------------------------------------------------
    // getId() and getCalendarType()
    // -----------------------------------------------------------------------

    @Test
    public void getId_returnsJulian() {
        assertEquals("Julian", JULIAN_CHRONO.getId());
    }

    @Test
    public void getCalendarType_returnsJulian() {
        assertEquals("julian", JULIAN_CHRONO.getCalendarType());
    }

    // -----------------------------------------------------------------------
    // Exception Handling
    // -----------------------------------------------------------------------

    @Test(expected = DateTimeException.class)
    public void date_withInvalidMonth_throwsException() {
        JULIAN_CHRONO.date(2014, 13, 1);
    }

    @Test(expected = DateTimeException.class)
    public void dateYearDay_withInvalidDayOfYear_throwsException() {
        JULIAN_CHRONO.dateYearDay(2013, 367); // 2013 is not a leap year
    }

    @Test(expected = DateTimeException.class)
    public void dateYearDay_withDay366InNonLeapYear_throwsException() {
        JULIAN_CHRONO.dateYearDay(2013, 366);
    }

    @Test(expected = DateTimeException.class)
    public void dateEpochDay_withOutOfRangeEpochDay_throwsException() {
        JULIAN_CHRONO.dateEpochDay(Long.MAX_VALUE);
    }

    @Test(expected = DateTimeException.class)
    public void date_fromTemporalWithOutOfRangeYear_throwsException() {
        // LocalDate.MAX has a year that is far beyond the JulianChronology's supported range.
        JULIAN_CHRONO.date(LocalDate.MAX);
    }

    @Test(expected = UnsupportedTemporalTypeException.class)
    public void date_fromUnsupportedTemporal_throwsException() {
        JULIAN_CHRONO.date(Month.APRIL);
    }

    @Test(expected = DateTimeException.class)
    public void zonedDateTime_fromUnsupportedTemporal_throwsException() {
        JULIAN_CHRONO.zonedDateTime(ZoneOffset.MAX);
    }

    @Test(expected = DateTimeException.class)
    public void eraOf_withInvalidValue_throwsException() {
        JULIAN_CHRONO.eraOf(3);
    }

    @Test(expected = ClassCastException.class)
    public void prolepticYear_withWrongEraType_throwsException() {
        JULIAN_CHRONO.prolepticYear(JapaneseEra.HEISEI, 1);
    }

    @Test(expected = ClassCastException.class)
    public void date_withWrongEraType_throwsException() {
        JULIAN_CHRONO.date(JapaneseEra.HEISEI, 1, 1, 1);
    }

    @Test(expected = NullPointerException.class)
    public void dateNow_withNullClock_throwsException() {
        JULIAN_CHRONO.dateNow((Clock) null);
    }

    @Test(expected = NullPointerException.class)
    public void dateNow_withNullZoneId_throwsException() {
        JULIAN_CHRONO.dateNow((ZoneId) null);
    }

    @Test(expected = ArithmeticException.class)
    public void dateNow_withForeverOffsetClock_throwsArithmeticException() {
        Clock baseClock = Clock.systemDefaultZone();
        Clock offsetClock = Clock.offset(baseClock, ChronoUnit.FOREVER.getDuration());
        JULIAN_CHRONO.dateNow(offsetClock);
    }
}