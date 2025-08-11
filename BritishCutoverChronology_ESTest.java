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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.chrono.Era;
import java.time.chrono.MinguoEra;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.ValueRange;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test for {@link BritishCutoverChronology}.
 */
class BritishCutoverChronologyTest {

    private final BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;

    //-----------------------------------------------------------------------
    // General properties
    //-----------------------------------------------------------------------

    @Test
    void getId_returnsCorrectId() {
        assertEquals("BritishCutover", chronology.getId());
    }

    @Test
    void getCalendarType_returnsNull() {
        assertNull(chronology.getCalendarType());
    }

    @Test
    void getCutover_returnsCorrectDate() {
        assertEquals(LocalDate.of(1752, 9, 14), chronology.getCutover());
    }

    //-----------------------------------------------------------------------
    // Era methods
    //-----------------------------------------------------------------------

    @Test
    void eras_returnsListOfTwoEras() {
        List<Era> eras = chronology.eras();
        assertEquals(2, eras.size());
        assertEquals(JulianEra.BC, eras.get(0));
        assertEquals(JulianEra.AD, eras.get(1));
    }

    @Test
    void eraOf_returnsCorrectEra() {
        assertEquals(JulianEra.BC, chronology.eraOf(0));
        assertEquals(JulianEra.AD, chronology.eraOf(1));
    }

    @Test
    void eraOf_withInvalidValue_throwsException() {
        assertThrows(DateTimeException.class, () -> chronology.eraOf(4));
    }

    //-----------------------------------------------------------------------
    // Factory methods for BritishCutoverDate
    //-----------------------------------------------------------------------

    @Nested
    class DateFactoryTests {

        @Test
        void fromProlepticYearMonthDay_forJulianDate() {
            BritishCutoverDate date = chronology.date(12, 6, 12);
            assertEquals(12, date.get(ChronoField.YEAR));
            assertEquals(6, date.get(ChronoField.MONTH_OF_YEAR));
            assertEquals(12, date.get(ChronoField.DAY_OF_MONTH));
        }

        @Test
        void fromProlepticYearMonthDay_withInvalidMonth_throwsException() {
            assertThrows(DateTimeException.class, () -> chronology.date(1737, 1737, 1));
        }

        @Test
        void fromProlepticYearMonthDay_withInvalidDayForJulianDate_throwsException() {
            // Year 9 is Julian, September has 30 days
            assertThrows(DateTimeException.class, () -> chronology.date(9, 9, 31));
        }

        @Test
        void fromEraYearMonthDay_forJulianDate() {
            BritishCutoverDate date = chronology.date(JulianEra.AD, 5, 5, 5);
            assertEquals(5, date.get(ChronoField.YEAR));
            assertEquals(5, date.get(ChronoField.MONTH_OF_YEAR));
            assertEquals(5, date.get(ChronoField.DAY_OF_MONTH));
            assertEquals(JulianEra.AD, date.getEra());
        }

        @Test
        void fromEraYearMonthDay_withInvalidMonth_throwsException() {
            assertThrows(DateTimeException.class, () -> chronology.date(JulianEra.AD, 6, 1000, 1));
        }

        @Test
        void fromEraYearMonthDay_withWrongEraType_throwsException() {
            assertThrows(ClassCastException.class, () -> chronology.date(MinguoEra.ROC, 1, 1, 1));
        }

        @Test
        void fromYearAndDayOfYear_forJulianDate() {
            BritishCutoverDate date = chronology.dateYearDay(103, 103);
            assertEquals(103, date.get(ChronoField.YEAR));
            assertEquals(103, date.get(ChronoField.DAY_OF_YEAR));
        }

        @Test
        void fromYearAndDayOfYear_withDayOfYearTooLarge_throwsException() {
            assertThrows(DateTimeException.class, () -> chronology.dateYearDay(874, 874));
        }

        @Test
        void fromYearAndDayOfYear_forNonLeapYearWithDay366_throwsException() {
            // Year 366 is Julian and not a leap year
            assertThrows(DateTimeException.class, () -> chronology.dateYearDay(366, 366));
        }

        @Test
        void fromEraYearAndDayOfYear_forBcEra() {
            BritishCutoverDate date = chronology.dateYearDay(JulianEra.BC, 5, 5);
            assertEquals(-4, date.get(ChronoField.YEAR)); // 5 BC is proleptic year -4
            assertEquals(5, date.get(ChronoField.DAY_OF_YEAR));
            assertEquals(JulianEra.BC, date.getEra());
        }

        @Test
        void fromEraYearAndDayOfYear_withInvalidDayOfYear_throwsException() {
            assertThrows(DateTimeException.class, () -> chronology.dateYearDay(JulianEra.BC, 1778, -5738));
        }

        @Test
        void fromEraYearAndDayOfYear_withWrongEraType_throwsException() {
            assertThrows(ClassCastException.class, () -> chronology.dateYearDay(MinguoEra.ROC, 1, 1));
        }

        @Test
        void fromEpochDay_forGregorianDate() {
            // -1813 epoch days is 1965-01-15, which is after the cutover
            BritishCutoverDate date = chronology.dateEpochDay(-1813L);
            assertEquals(LocalDate.of(1965, 1, 15), date.toLocalDate());
        }

        @Test
        void fromEpochDay_withLargeNegativeValue_throwsException() {
            assertThrows(DateTimeException.class, () -> chronology.dateEpochDay(-2135812540L));
        }

        @Test
        void fromTemporalAccessor_withBritishCutoverDate_returnsSameInstance() {
            BritishCutoverDate originalDate = chronology.date(2000, 1, 1);
            BritishCutoverDate fromTemporal = chronology.date(originalDate);
            assertSame(originalDate, fromTemporal);
        }

        @Test
        void fromTemporalAccessor_withLocalDate_returnsBritishCutoverDate() {
            LocalDate localDate = LocalDate.of(2024, 5, 20);
            BritishCutoverDate date = chronology.date(localDate);
            assertEquals(localDate, date.toLocalDate());
        }

        @Test
        void fromTemporalAccessor_withUnsupportedType_throwsException() {
            // An Era cannot be converted to a date
            assertThrows(DateTimeException.class, () -> chronology.date(JulianEra.AD));
        }

        @Test
        void fromTemporalAccessor_withNull_throwsNullPointerException() {
            assertThrows(NullPointerException.class, () -> chronology.date((TemporalAccessor) null));
        }
    }

    //-----------------------------------------------------------------------
    // dateNow()
    //-----------------------------------------------------------------------

    @Nested
    class DateNowTests {
        private final ZoneId zone = ZoneId.of("Europe/London");
        private final Instant fixedInstant = LocalDate.of(2024, 1, 1).atStartOfDay(zone).toInstant();
        private final Clock fixedClock = Clock.fixed(fixedInstant, zone);

        @Test
        void withClock_returnsCurrentDate() {
            BritishCutoverDate date = chronology.dateNow(fixedClock);
            assertEquals(chronology.date(2024, 1, 1), date);
        }

        @Test
        void withNullClock_throwsNullPointerException() {
            assertThrows(NullPointerException.class, () -> chronology.dateNow((Clock) null));
        }

        @Test
        void withNullZoneId_throwsNullPointerException() {
            assertThrows(NullPointerException.class, () -> chronology.dateNow((ZoneId) null));
        }
    }

    //-----------------------------------------------------------------------
    // isLeapYear(long)
    //-----------------------------------------------------------------------

    @ParameterizedTest
    @CsvSource({
            "371, false",   // Julian, not divisible by 4
            "1700, true",   // Julian, divisible by 4 (Gregorian would be false)
            "1752, true",   // Cutover year, Julian rules apply, divisible by 4
            "1800, false",  // Gregorian, divisible by 100 but not 400
            "2000, true",   // Gregorian, divisible by 400
            "2023, false",  // Gregorian, not divisible by 4
            "2024, true"    // Gregorian, divisible by 4
    })
    void isLeapYear_forVariousYears(long prolepticYear, boolean expected) {
        assertEquals(expected, chronology.isLeapYear(prolepticYear));
    }

    //-----------------------------------------------------------------------
    // prolepticYear(Era, int)
    //-----------------------------------------------------------------------

    @Test
    void prolepticYear_forAdEra_returnsYearOfEra() {
        assertEquals(13, chronology.prolepticYear(JulianEra.AD, 13));
        assertEquals(0, chronology.prolepticYear(JulianEra.AD, 0)); // The method allows this input
    }

    @Test
    void prolepticYear_forBcEra_returnsCorrectNegativeYear() {
        // Year 1 BC is proleptic year 0. Year 2 BC is -1, etc.
        // So, proleptic year = 1 - yearOfEra
        assertEquals(-1777, chronology.prolepticYear(JulianEra.BC, 1778));
    }

    @Test
    void prolepticYear_withWrongEraType_throwsException() {
        assertThrows(ClassCastException.class, () -> chronology.prolepticYear(MinguoEra.ROC, 1));
    }

    //-----------------------------------------------------------------------
    // range(ChronoField)
    //-----------------------------------------------------------------------

    static Stream<Object[]> range_data() {
        return Stream.of(
                new Object[]{ChronoField.YEAR_OF_ERA, ValueRange.of(1, 999_999)},
                new Object[]{ChronoField.YEAR, ValueRange.of(-999_998, 999_999)},
                new Object[]{ChronoField.PROLEPTIC_MONTH, ValueRange.of(-999_998 * 12L, 999_999 * 12L + 11)},
                new Object[]{ChronoField.DAY_OF_YEAR, ValueRange.of(1, 355, 366)},
                new Object[]{ChronoField.ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 51, 53)},
                new Object[]{ChronoField.ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 3, 5)}
        );
    }

    @ParameterizedTest
    @MethodSource("range_data")
    void range_forVariousFields_returnsCorrectRange(ChronoField field, ValueRange expectedRange) {
        assertEquals(expectedRange, chronology.range(field));
    }

    @Test
    void range_withNullField_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> chronology.range(null));
    }

    //-----------------------------------------------------------------------
    // localDateTime(TemporalAccessor)
    //-----------------------------------------------------------------------

    @Test
    void localDateTime_fromZonedDateTime_returnsLocalDateTime() {
        ZonedDateTime zdt = ZonedDateTime.of(2024, 5, 20, 10, 30, 0, 0, ZoneOffset.UTC);
        ChronoLocalDateTime<BritishCutoverDate> result = chronology.localDateTime(zdt);

        assertNotNull(result);
        assertEquals(chronology.date(2024, 5, 20), result.toLocalDate());
        assertEquals(zdt.toLocalTime(), result.toLocalTime());
    }

    @Test
    void localDateTime_fromLocalDate_throwsException() {
        // A LocalDate does not have time information
        assertThrows(DateTimeException.class, () -> chronology.localDateTime(chronology.getCutover()));
    }

    @Test
    void localDateTime_fromNullTemporal_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> chronology.localDateTime(null));
    }

    //-----------------------------------------------------------------------
    // zonedDateTime(TemporalAccessor) / zonedDateTime(Instant, ZoneId)
    //-----------------------------------------------------------------------

    @Test
    void zonedDateTime_fromZonedDateTime_returnsZonedDateTime() {
        ZonedDateTime zdt = ZonedDateTime.of(2024, 5, 20, 10, 30, 0, 0, ZoneOffset.UTC);
        ChronoZonedDateTime<BritishCutoverDate> result = chronology.zonedDateTime(zdt);

        assertNotNull(result);
        assertEquals(chronology.date(2024, 5, 20), result.toLocalDate());
        assertEquals(zdt.toLocalTime(), result.toLocalTime());
        assertEquals(zdt.getZone(), result.getZone());
    }

    @Test
    void zonedDateTime_fromInstantAndZone_returnsZonedDateTime() {
        Instant instant = Instant.ofEpochSecond(1004L, 0); // 1970-01-01T00:16:44Z
        ZoneId zone = ZoneOffset.UTC;
        ChronoZonedDateTime<BritishCutoverDate> result = chronology.zonedDateTime(instant, zone);

        assertNotNull(result);
        assertEquals(chronology.date(1970, 1, 1), result.toLocalDate());
        assertEquals(16, result.get(ChronoField.MINUTE_OF_HOUR));
        assertEquals(44, result.get(ChronoField.SECOND_OF_MINUTE));
    }

    @Test
    void zonedDateTime_fromLocalDate_throwsException() {
        // A LocalDate does not have zone information
        assertThrows(DateTimeException.class, () -> chronology.zonedDateTime(chronology.getCutover()));
    }

    @Test
    void zonedDateTime_fromNullTemporal_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> chronology.zonedDateTime((TemporalAccessor) null));
    }

    @Test
    void zonedDateTime_fromNullInstant_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> chronology.zonedDateTime(null, ZoneOffset.UTC));
    }

    //-----------------------------------------------------------------------
    // resolveDate(Map, ResolverStyle)
    //-----------------------------------------------------------------------

    @Nested
    class ResolveDateTests {
        @Test
        void fromEpochDay_returnsCorrectDate() {
            Map<ChronoField, Long> fields = new HashMap<>();
            fields.put(ChronoField.EPOCH_DAY, 763L); // 1972-02-06
            BritishCutoverDate resolvedDate = chronology.resolveDate(fields, ResolverStyle.STRICT);
            assertEquals(chronology.date(1972, 2, 6), resolvedDate);
        }

        @Test
        void withInvalidYearOfEra_throwsException() {
            Map<ChronoField, Long> fields = new HashMap<>();
            fields.put(ChronoField.YEAR_OF_ERA, -4135L);
            fields.put(ChronoField.MONTH_OF_YEAR, 1L);
            fields.put(ChronoField.DAY_OF_MONTH, 1L);
            assertThrows(DateTimeException.class, () -> chronology.resolveDate(fields, ResolverStyle.STRICT));
        }

        @Test
        void withEmptyMap_returnsNull() {
            assertNull(chronology.resolveDate(Collections.emptyMap(), ResolverStyle.STRICT));
        }

        @Test
        void withNullMap_throwsNullPointerException() {
            assertThrows(NullPointerException.class, () -> chronology.resolveDate(null, ResolverStyle.STRICT));
        }
    }
}