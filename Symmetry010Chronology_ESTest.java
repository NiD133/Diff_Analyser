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
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import java.time.chrono.JapaneseEra;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test cases for the {@link Symmetry010Chronology} class.
 */
public class Symmetry010ChronologyTest {

    private final Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;

    // --- Constants and Singleton ---

    @Test
    public void singleton_andConstants_areCorrect() {
        assertEquals("Sym010", chronology.getId());
        assertNull(chronology.getCalendarType());
    }

    // --- isLeapYear ---

    @Test
    public void isLeapYear_returnsTrueForLeapYear() {
        // Year -1547 is a leap year according to the formula
        assertTrue(chronology.isLeapYear(-1547L));
    }

    @Test
    public void isLeapYear_returnsFalseForNonLeapYear() {
        assertFalse(chronology.isLeapYear(32L));
    }

    // --- getLeapYearsBefore (static helper) ---

    @Test
    public void getLeapYearsBefore_calculatesCorrectly() {
        assertEquals(0L, Symmetry010Chronology.getLeapYearsBefore(0L));
        assertEquals(99L, Symmetry010Chronology.getLeapYearsBefore(560L));
        assertEquals(-453L, Symmetry010Chronology.getLeapYearsBefore(-2552));
    }

    // --- prolepticYear ---

    @Test
    public void prolepticYear_withCEEra_returnsYearOfEra() {
        assertEquals(2024, chronology.prolepticYear(IsoEra.CE, 2024));
        assertEquals(-537, chronology.prolepticYear(IsoEra.CE, -537));
    }

    @Test
    public void prolepticYear_withBCEEra_returnsOneMinusYearOfEra() {
        assertEquals(0, chronology.prolepticYear(IsoEra.BCE, 1));
        assertEquals(-1, chronology.prolepticYear(IsoEra.BCE, 2));
    }

    @Test(expected = DateTimeException.class)
    public void prolepticYear_throwsForYearOutOfRange() {
        chronology.prolepticYear(IsoEra.CE, 2_000_000); // Max is 1,000,000
    }

    @Test(expected = ClassCastException.class)
    public void prolepticYear_throwsForInvalidEraType() {
        chronology.prolepticYear(JapaneseEra.MEIJI, 1);
    }

    // --- date(...) factories ---

    @Test
    public void date_fromYearMonthDay_createsCorrectDate() {
        Symmetry010Date date = chronology.date(2024, 1, 1);
        assertEquals(2024, date.get(ChronoField.YEAR));
        assertEquals(1, date.get(ChronoField.MONTH_OF_YEAR));
        assertEquals(1, date.get(ChronoField.DAY_OF_MONTH));
    }

    @Test(expected = DateTimeException.class)
    public void date_fromYearMonthDay_throwsForInvalidMonth() {
        chronology.date(2024, 13, 1);
    }

    @Test(expected = DateTimeException.class)
    public void date_fromYearMonthDay_throwsForInvalidDay() {
        // May (month 5) is a 31-day month, so 35 is invalid
        chronology.date(5, 5, 35);
    }

    @Test
    public void date_fromEraYearMonthDay_createsCorrectDate() {
        Symmetry010Date date = chronology.date(IsoEra.CE, 2024, 10, 10);
        assertEquals(2024, date.getYear());
        assertEquals(10, date.getMonthValue());
        assertEquals(10, date.getDayOfMonth());
        assertEquals(IsoEra.CE, date.getEra());
    }

    @Test(expected = DateTimeException.class)
    public void date_fromEraYearMonthDay_throwsForInvalidMonth() {
        chronology.date(IsoEra.CE, 2024, -1, 1);
    }

    @Test(expected = ClassCastException.class)
    public void date_fromEraYearMonthDay_throwsForInvalidEraType() {
        chronology.date(JulianEra.AD, 2024, 1, 1);
    }

    // --- dateYearDay(...) factories ---

    @Test
    public void dateYearDay_fromYearDay_createsCorrectDate() {
        Symmetry010Date date = chronology.dateYearDay(2023, 364); // 2023 is a leap year
        assertEquals(2023, date.getYear());
        assertEquals(364, date.getDayOfYear());
    }

    @Test(expected = DateTimeException.class)
    public void dateYearDay_fromYearDay_throwsForDayOfYearTooLarge() {
        chronology.dateYearDay(2024, 999);
    }

    @Test(expected = DateTimeException.class)
    public void dateYearDay_fromYearDay_throwsForDayOfYearInvalidForNonLeapYear() {
        int nonLeapYear = 2024;
        chronology.dateYearDay(nonLeapYear, 371); // 371 is only valid in a leap year
    }

    @Test
    public void dateYearDay_fromEraYearDay_createsCorrectDate() {
        Symmetry010Date date = chronology.dateYearDay(IsoEra.CE, 2023, 30); // 2023 is a leap year
        assertEquals(2023, date.getYear());
        assertEquals(30, date.getDayOfYear());
        assertEquals(IsoEra.CE, date.getEra());
    }

    @Test(expected = DateTimeException.class)
    public void dateYearDay_fromEraYearDay_throwsForDayOfYearTooLarge() {
        chronology.dateYearDay(IsoEra.CE, 2024, 1223);
    }

    @Test(expected = DateTimeException.class)
    public void dateYearDay_fromEraYearDay_throwsForDayOfYearInvalidForNonLeapYear() {
        int nonLeapYear = 369;
        chronology.dateYearDay(IsoEra.CE, nonLeapYear, 369);
    }

    @Test(expected = ClassCastException.class)
    public void dateYearDay_fromEraYearDay_throwsForInvalidEraType() {
        chronology.dateYearDay(ThaiBuddhistEra.BE, 2567, 1);
    }

    // --- dateEpochDay ---

    @Test
    public void dateEpochDay_createsCorrectDate() {
        Symmetry010Date date = chronology.dateEpochDay(719162L); // Corresponds to 1970-01-01 in Symmetry010
        assertEquals(1970, date.getYear());
        assertEquals(1, date.getMonthValue());
        assertEquals(1, date.getDayOfMonth());
    }

    @Test(expected = DateTimeException.class)
    public void dateEpochDay_throwsForEpochDayOutOfRange() {
        long maxEpochDay = chronology.range(ChronoField.EPOCH_DAY).getMaximum();
        chronology.dateEpochDay(maxEpochDay + 1);
    }

    // --- date(TemporalAccessor) ---

    @Test
    public void date_fromTemporal_createsCorrectDate() {
        // CopticDate is a valid TemporalAccessor with epoch day
        TemporalAccessor temporal = CopticDate.of(1686, 4, 26); // Corresponds to 2020-01-01 ISO
        Symmetry010Date date = chronology.date(temporal);
        assertEquals(2020, date.getYear());
        assertEquals(1, date.getMonthValue());
        assertEquals(1, date.getDayOfMonth());
    }

    @Test(expected = DateTimeException.class)
    public void date_fromTemporal_throwsForUnsupportedType() {
        // LocalDate does not have a direct conversion path defined in the chronology
        chronology.date(LocalDate.of(2024, 1, 1));
    }

    @Test(expected = UnsupportedTemporalTypeException.class)
    public void date_fromTemporal_throwsForMissingEpochDayField() {
        // java.time.Year does not support EPOCH_DAY
        chronology.date(java.time.Year.of(2024));
    }

    @Test(expected = NullPointerException.class)
    public void date_fromTemporal_throwsForNullInput() {
        chronology.date(null);
    }

    // --- dateNow ---

    @Test
    public void dateNow_default_returnsCurrentDate() {
        assertNotNull(chronology.dateNow());
    }

    @Test
    public void dateNow_withZone_returnsCurrentDate() {
        assertNotNull(chronology.dateNow(ZoneOffset.UTC));
    }

    @Test(expected = NullPointerException.class)
    public void dateNow_withZone_throwsForNullZone() {
        chronology.dateNow((ZoneId) null);
    }

    @Test
    public void dateNow_withClock_returnsDateFromClock() {
        Instant instant = Instant.parse("2024-07-21T10:15:30.00Z");
        Clock clock = Clock.fixed(instant, ZoneOffset.UTC);
        Symmetry010Date date = chronology.dateNow(clock);
        assertEquals(chronology.date(2024, 7, 21), date);
    }

    @Test(expected = NullPointerException.class)
    public void dateNow_withClock_throwsForNullClock() {
        chronology.dateNow((Clock) null);
    }

    @Test(expected = DateTimeException.class)
    public void dateNow_withClock_throwsForDateBeyondMaxRange() {
        ValueRange epochDayRange = chronology.range(ChronoField.EPOCH_DAY);
        long maxEpochDay = epochDayRange.getMaximum();
        Instant outOfRangeInstant = Instant.ofEpochDay(maxEpochDay + 1);
        Clock clock = Clock.fixed(outOfRangeInstant, ZoneOffset.UTC);
        chronology.dateNow(clock);
    }

    // --- localDateTime(TemporalAccessor) ---

    @Test
    public void localDateTime_fromTemporal_createsCorrectDateTime() {
        OffsetDateTime odt = OffsetDateTime.now(ZoneOffset.UTC);
        assertNotNull(chronology.localDateTime(odt));
    }

    @Test(expected = DateTimeException.class)
    public void localDateTime_fromTemporal_throwsForUnsupportedType() {
        chronology.localDateTime(LocalDate.now());
    }

    // --- zonedDateTime(...) ---

    @Test
    public void zonedDateTime_fromTemporal_createsCorrectDateTime() {
        OffsetDateTime odt = OffsetDateTime.now(ZoneOffset.UTC);
        assertNotNull(chronology.zonedDateTime(odt));
    }

    @Test(expected = DateTimeException.class)
    public void zonedDateTime_fromTemporal_throwsForUnsupportedType() {
        chronology.zonedDateTime(EthiopicDate.now());
    }

    @Test
    public void zonedDateTime_fromInstant_createsCorrectDateTime() {
        Instant instant = Instant.now();
        assertNotNull(chronology.zonedDateTime(instant, ZoneOffset.UTC));
    }

    @Test(expected = NullPointerException.class)
    public void zonedDateTime_fromInstant_throwsForNullInstant() {
        chronology.zonedDateTime(null, ZoneOffset.UTC);
    }

    // --- eraOf and eras ---

    @Test
    public void eraOf_returnsCorrectEra() {
        assertEquals(IsoEra.BCE, chronology.eraOf(0));
        assertEquals(IsoEra.CE, chronology.eraOf(1));
    }

    @Test(expected = DateTimeException.class)
    public void eraOf_throwsForInvalidValue() {
        chronology.eraOf(2);
    }

    @Test
    public void eras_returnsListOfEras() {
        List<Era> eras = chronology.eras();
        assertEquals(2, eras.size());
        assertTrue(eras.contains(IsoEra.BCE));
        assertTrue(eras.contains(IsoEra.CE));
    }

    // --- range(ChronoField) ---

    @Test
    public void range_returnsCorrectRangesForFields() {
        assertEquals(ValueRange.of(1, 7), chronology.range(ChronoField.DAY_OF_WEEK));
        assertEquals(ValueRange.of(1, 30, 37), chronology.range(ChronoField.DAY_OF_MONTH));
        assertEquals(ValueRange.of(1, 364, 371), chronology.range(ChronoField.DAY_OF_YEAR));
        assertEquals(ValueRange.of(1, 12), chronology.range(ChronoField.MONTH_OF_YEAR));
        assertEquals(ValueRange.of(0, 1), chronology.range(ChronoField.ERA));
        assertEquals(ValueRange.of(1, 1_000_000), chronology.range(ChronoField.YEAR_OF_ERA));
        assertEquals(ValueRange.of(-1_000_000, 1_000_000), chronology.range(ChronoField.YEAR));
    }

    @Test
    public void range_returnsCorrectRangesForTimeFields() {
        assertEquals(ChronoField.NANO_OF_SECOND.range(), chronology.range(ChronoField.NANO_OF_SECOND));
        assertEquals(ChronoField.SECOND_OF_MINUTE.range(), chronology.range(ChronoField.SECOND_OF_MINUTE));
        assertEquals(ChronoField.MINUTE_OF_HOUR.range(), chronology.range(ChronoField.MINUTE_OF_HOUR));
        assertEquals(ChronoField.HOUR_OF_DAY.range(), chronology.range(ChronoField.HOUR_OF_DAY));
    }

    @Test(expected = NullPointerException.class)
    public void range_throwsForNullField() {
        chronology.range(null);
    }
}