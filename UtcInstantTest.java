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
package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.testing.EqualsTester;

/**
 * Test UtcInstant.
 */
public class TestUtcInstant {

    // Constants for Modified Julian Days (MJD)
    // MJD epoch: 1858-11-17, JD=2400000.5
    private static final long MJD_1972_12_30 = 41681;           // Normal day
    private static final long MJD_1972_12_31_LEAP = 41682;      // Day with leap second
    private static final long MJD_1973_01_01 = 41683;           // Next day after leap second
    private static final long MJD_1973_12_31_LEAP = MJD_1972_12_31_LEAP + 365; // Non-leap day for testing

    // Time conversion constants
    private static final long SECS_PER_DAY = 24L * 60 * 60;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    //-------------------------------------------------------------------------
    // Interface implementation tests
    //-------------------------------------------------------------------------
    @Test
    public void test_implementsSerializable() {
        assertTrue(Serializable.class.isAssignableFrom(UtcInstant.class));
    }

    @Test
    public void test_implementsComparable() {
        assertTrue(Comparable.class.isAssignableFrom(UtcInstant.class));
    }

    //-------------------------------------------------------------------------
    // Serialization tests
    //-------------------------------------------------------------------------
    @Test
    public void test_serializationRoundTrip() throws Exception {
        UtcInstant original = UtcInstant.ofModifiedJulianDay(2, 3);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(original);
        }
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
            assertEquals(original, ois.readObject());
        }
    }

    //-------------------------------------------------------------------------
    // ofModifiedJulianDay() factory tests
    //-------------------------------------------------------------------------
    @Test
    public void factory_ofModifiedJulianDay_validInputs() {
        for (long day = -2; day <= 2; day++) {
            for (int nanos = 0; nanos < 10; nanos++) {
                UtcInstant instant = UtcInstant.ofModifiedJulianDay(day, nanos);
                assertEquals(day, instant.getModifiedJulianDay());
                assertEquals(nanos, instant.getNanoOfDay());
                assertFalse(instant.isLeapSecond());
            }
        }
    }

    @Test
    public void factory_ofModifiedJulianDay_lastNanoBeforeLeapSecond() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1);
        assertEquals(MJD_1972_12_31_LEAP, instant.getModifiedJulianDay());
        assertEquals(NANOS_PER_DAY - 1, instant.getNanoOfDay());
        assertFalse(instant.isLeapSecond());
        assertEquals("1972-12-31T23:59:59.999999999Z", instant.toString());
    }

    @Test
    public void factory_ofModifiedJulianDay_leapSecondStart() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_DAY);
        assertEquals(MJD_1972_12_31_LEAP, instant.getModifiedJulianDay());
        assertEquals(NANOS_PER_DAY, instant.getNanoOfDay());
        assertTrue(instant.isLeapSecond());
        assertEquals("1972-12-31T23:59:60Z", instant.toString());
    }

    @Test
    public void factory_ofModifiedJulianDay_lastNanoOfLeapSecond() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1);
        assertEquals(MJD_1972_12_31_LEAP, instant.getModifiedJulianDay());
        assertEquals(NANOS_PER_LEAP_DAY - 1, instant.getNanoOfDay());
        assertTrue(instant.isLeapSecond());
        assertEquals("1972-12-31T23:59:60.999999999Z", instant.toString());
    }

    @Test
    public void factory_ofModifiedJulianDay_negativeNanos() {
        assertThrows(DateTimeException.class, 
            () -> UtcInstant.ofModifiedJulianDay(MJD_1973_01_01, -1));
    }

    @Test
    public void factory_ofModifiedJulianDay_nanosExceedNormalDay() {
        assertThrows(DateTimeException.class, 
            () -> UtcInstant.ofModifiedJulianDay(MJD_1973_01_01, NANOS_PER_DAY));
    }

    @Test
    public void factory_ofModifiedJulianDay_nanosExceedLeapDay() {
        assertThrows(DateTimeException.class, 
            () -> UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY));
    }

    //-------------------------------------------------------------------------
    // of(Instant) factory tests
    //-------------------------------------------------------------------------
    @Test
    public void factory_fromInstant_epoch() {
        UtcInstant test = UtcInstant.of(Instant.ofEpochSecond(0, 2));  // 1970-01-01
        assertEquals(40587, test.getModifiedJulianDay());
        assertEquals(2, test.getNanoOfDay());
    }

    @Test
    public void factory_fromInstant_null() {
        assertThrows(NullPointerException.class, () -> UtcInstant.of((Instant) null));
    }

    //-------------------------------------------------------------------------
    // of(TaiInstant) factory tests
    //-------------------------------------------------------------------------
    @Test
    public void factory_fromTaiInstant_conversion() {
        for (int dayOffset = -1000; dayOffset < 1000; dayOffset++) {
            for (int second = 0; second < 10; second++) {
                UtcInstant expected = UtcInstant.ofModifiedJulianDay(36204 + dayOffset, second * NANOS_PER_SEC + 2L);
                TaiInstant tai = TaiInstant.ofTaiSeconds(dayOffset * SECS_PER_DAY + second + 10, 2);
                assertEquals(expected, UtcInstant.of(tai));
            }
        }
    }

    @Test
    public void factory_fromTaiInstant_null() {
        assertThrows(NullPointerException.class, () -> UtcInstant.of((TaiInstant) null));
    }

    //-------------------------------------------------------------------------
    // parse() factory tests
    //-------------------------------------------------------------------------
    @Test
    public void factory_parse_validLeapSecond() {
        assertEquals(
            UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_DAY - NANOS_PER_SEC),
            UtcInstant.parse("1972-12-31T23:59:59Z")
        );
        assertEquals(
            UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_DAY),
            UtcInstant.parse("1972-12-31T23:59:60Z")
        );
    }

    public static Arguments[] invalidParseInputs() {
        return new Arguments[] {
            Arguments.of("", "Empty string"),
            Arguments.of("A", "Invalid format"),
            Arguments.of("2012-13-01T00:00:00Z", "Invalid month"),
        };
    }

    @ParameterizedTest(name = "{1}")
    @MethodSource("invalidParseInputs")
    public void factory_parse_invalidFormats(String input, String description) {
        assertThrows(DateTimeException.class, () -> UtcInstant.parse(input));
    }

    @Test
    public void factory_parse_invalidLeapSecond() {
        assertThrows(DateTimeException.class, 
            () -> UtcInstant.parse("1972-11-11T23:59:60Z"));
    }

    @Test
    public void factory_parse_null() {
        assertThrows(NullPointerException.class, () -> UtcInstant.parse(null));
    }

    //-------------------------------------------------------------------------
    // withModifiedJulianDay() tests
    //-------------------------------------------------------------------------
    public static Arguments[] withModifiedJulianDayCases() {
        return new Arguments[] {
            // Original day/nanos, new day, expected day/nanos (null for invalid)
            Arguments.of(0L, 12345L, 1L, 1L, 12345L, "Same nanos, different day"),
            Arguments.of(0L, 12345L, -1L, -1L, 12345L, "Same nanos, negative day"),
            Arguments.of(7L, 12345L, 2L, 2L, 12345L, "Positive day change"),
            Arguments.of(7L, 12345L, -2L, -2L, 12345L, "Negative day change"),
            Arguments.of(-99L, 12345L, 3L, 3L, 12345L, "From negative to positive"),
            Arguments.of(-99L, 12345L, -3L, -3L, 12345L, "Negative to more negative"),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_30, null, null, "Leap second to non-leap day"),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_31_LEAP, MJD_1972_12_31_LEAP, NANOS_PER_DAY, "Same leap day"),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_01_01, null, null, "Leap second to next day"),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_12_31_LEAP, MJD_1973_12_31_LEAP, NANOS_PER_DAY, "Leap second to future non-leap day")
        };
    }

    @ParameterizedTest(name = "[{index}] {5}")
    @MethodSource("withModifiedJulianDayCases")
    public void test_withModifiedJulianDay(long originalDay, long originalNanos, long newDay, 
                                          Long expectedDay, Long expectedNanos, String description) {
        UtcInstant original = UtcInstant.ofModifiedJulianDay(originalDay, originalNanos);
        
        if (expectedDay != null) {
            UtcInstant result = original.withModifiedJulianDay(newDay);
            assertEquals(expectedDay, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        } else {
            assertThrows(DateTimeException.class, () -> original.withModifiedJulianDay(newDay));
        }
    }

    //-------------------------------------------------------------------------
    // withNanoOfDay() tests
    //-------------------------------------------------------------------------
    public static Arguments[] withNanoOfDayCases() {
        return new Arguments[] {
            // Original day/nanos, new nanos, expected day/nanos (null for invalid)
            Arguments.of(0L, 12345L, 1L, 0L, 1L, "Small nano adjustment"),
            Arguments.of(0L, 12345L, -1L, null, null, "Negative nanos invalid"),
            Arguments.of(7L, 12345L, 2L, 7L, 2L, "Different day, small nanos"),
            Arguments.of(-99L, 12345L, 3L, -99L, 3L, "Negative day, valid nanos"),
            
            // Normal day boundaries
            Arguments.of(MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1, 
                MJD_1972_12_30, NANOS_PER_DAY - 1, "Last nano on normal day"),
            Arguments.of(MJD_1973_01_01, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1, 
                MJD_1973_01_01, NANOS_PER_DAY - 1, "Last nano on next day"),
                
            // Leap day boundaries
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1, 
                MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, "Last nano before leap second"),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_DAY, 
                MJD_1972_12_31_LEAP, NANOS_PER_DAY, "Start of leap second"),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY - 1, 
                MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1, "Last nano of leap second"),
                
            // Invalid cases
            Arguments.of(MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_DAY, null, null, "Add leap second to non-leap day"),
            Arguments.of(MJD_1973_01_01, NANOS_PER_DAY - 1, NANOS_PER_DAY, null, null, "Add leap second to next day"),
            Arguments.of(MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY - 1, null, null, "Leap second nanos on normal day"),
            Arguments.of(MJD_1973_01_01, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY - 1, null, null, "Leap second nanos on next day"),
            Arguments.of(MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY, null, null, "Exceed leap day nanos on normal day"),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY, null, null, "Exceed leap day nanos"),
            Arguments.of(MJD_1973_01_01, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY, null, null, "Exceed leap day nanos on next day")
        };
    }

    @ParameterizedTest(name = "[{index}] {5}")
    @MethodSource("withNanoOfDayCases")
    public void test_withNanoOfDay(long originalDay, long originalNanos, long newNanos, 
                                  Long expectedDay, Long expectedNanos, String description) {
        UtcInstant original = UtcInstant.ofModifiedJulianDay(originalDay, originalNanos);
        
        if (expectedDay != null) {
            UtcInstant result = original.withNanoOfDay(newNanos);
            assertEquals(expectedDay, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        } else {
            assertThrows(DateTimeException.class, () -> original.withNanoOfDay(newNanos));
        }
    }

    //-------------------------------------------------------------------------
    // plus() operation tests
    //-------------------------------------------------------------------------
    public static Arguments[] plusOperationCases() {
        return new Arguments[] {
            // mjd, nanos, plusSeconds, plusNanos, expectedMjd, expectedNanos
            Arguments.of(0, 0,  -2 * SECS_PER_DAY, 5, -2, 5, "Large negative seconds"),
            Arguments.of(0, 0,  0,         0,  0,  0, "Zero duration"),
            Arguments.of(0, 0,  0,         1,  0,  1, "Add one nano"),
            Arguments.of(0, 0,  1,         0,  0,  1 * NANOS_PER_SEC, "Add one second"),
            Arguments.of(0, 0,  3, 333_333_333,  0,  3 * NANOS_PER_SEC + 333_333_333, "Fractional seconds"),
            Arguments.of(0, 0,  1 * SECS_PER_DAY, 0,  1, 0, "Add one day"),
            Arguments.of(1, 0,  -1 * SECS_PER_DAY, 0, 0, 0, "Remove one day from day 1"),
            Arguments.of(1, 0,  0,        -1,  0,  NANOS_PER_DAY - 1, "Cross day boundary backward")
        };
    }

    @ParameterizedTest(name = "[{index}] {6}")
    @MethodSource("plusOperationCases")
    public void test_plus(long mjd, long nanos, long plusSeconds, int plusNanos, 
                         long expectedMjd, long expectedNanos, String description) {
        UtcInstant result = UtcInstant.ofModifiedJulianDay(mjd, nanos)
            .plus(Duration.ofSeconds(plusSeconds, plusNanos));
        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    @Test
    public void test_plus_overflowUpperBound() {
        UtcInstant max = UtcInstant.ofModifiedJulianDay(Long.MAX_VALUE, NANOS_PER_DAY - 1);
        assertThrows(ArithmeticException.class, () -> max.plus(Duration.ofNanos(1)));
    }

    @Test
    public void test_plus_overflowLowerBound() {
        UtcInstant min = UtcInstant.ofModifiedJulianDay(Long.MIN_VALUE, 0);
        assertThrows(ArithmeticException.class, () -> min.plus(Duration.ofNanos(-1)));
    }

    //-------------------------------------------------------------------------
    // minus() operation tests
    //-------------------------------------------------------------------------
    public static Arguments[] minusOperationCases() {
        return new Arguments[] {
            // mjd, nanos, minusSeconds, minusNanos, expectedMjd, expectedNanos
            Arguments.of(0, 0,  2 * SECS_PER_DAY, -5, -2, 5, "Large positive seconds subtraction"),
            Arguments.of(0, 0,  0,          0,  0,  0, "Zero duration subtraction"),
            Arguments.of(0, 0,  0,         -1,  0,  1, "Subtract negative nano (add)"),
            Arguments.of(0, 0,  -1,         0,  0,  1 * NANOS_PER_SEC, "Subtract negative second (add)"),
            Arguments.of(0, 0,  -3, -333_333_333,  0,  3 * NANOS_PER_SEC + 333_333_333, "Subtract negative fractional seconds"),
            Arguments.of(0, 0,  -1 * SECS_PER_DAY, 0,  1, 0, "Subtract negative day (add)"),
            Arguments.of(1, 0,  1 * SECS_PER_DAY, 0, 0, 0, "Subtract one day from day 1"),
            Arguments.of(1, 0,  0,          1,  0,  NANOS_PER_DAY - 1, "Cross day boundary backward")
        };
    }

    @ParameterizedTest(name = "[{index}] {6}")
    @MethodSource("minusOperationCases")
    public void test_minus(long mjd, long nanos, long minusSeconds, int minusNanos, 
                          long expectedMjd, long expectedNanos, String description) {
        UtcInstant result = UtcInstant.ofModifiedJulianDay(mjd, nanos)
            .minus(Duration.ofSeconds(minusSeconds, minusNanos));
        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    @Test
    public void test_minus_overflowLowerBound() {
        UtcInstant min = UtcInstant.ofModifiedJulianDay(Long.MIN_VALUE, 0);
        assertThrows(ArithmeticException.class, () -> min.minus(Duration.ofNanos(1)));
    }

    @Test
    public void test_minus_overflowUpperBound() {
        UtcInstant max = UtcInstant.ofModifiedJulianDay(Long.MAX_VALUE, NANOS_PER_DAY - 1);
        assertThrows(ArithmeticException.class, () -> max.minus(Duration.ofNanos(-1)));
    }

    //-------------------------------------------------------------------------
    // durationUntil() tests
    //-------------------------------------------------------------------------
    @Test
    public void test_durationUntil_normalDay() {
        UtcInstant start = UtcInstant.ofModifiedJulianDay(MJD_1972_12_30, 0);
        UtcInstant end = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, 0);
        Duration duration = start.durationUntil(end);
        assertEquals(86400, duration.getSeconds());
        assertEquals(0, duration.getNano());
    }

    @Test
    public void test_durationUntil_leapDay() {
        UtcInstant start = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, 0);
        UtcInstant end = UtcInstant.ofModifiedJulianDay(MJD_1973_01_01, 0);
        Duration duration = start.durationUntil(end);
        assertEquals(86401, duration.getSeconds());
        assertEquals(0, duration.getNano());
    }

    @Test
    public void test_durationUntil_leapDayNegative() {
        UtcInstant start = UtcInstant.ofModifiedJulianDay(MJD_1973_01_01, 0);
        UtcInstant end = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, 0);
        Duration duration = start.durationUntil(end);
        assertEquals(-86401, duration.getSeconds());
        assertEquals(0, duration.getNano());
    }

    //-------------------------------------------------------------------------
    // toTaiInstant() tests
    //-------------------------------------------------------------------------
    @Test
    public void test_toTaiInstant_conversion() {
        for (int dayOffset = -1000; dayOffset < 1000; dayOffset++) {
            for (int second = 0; second < 10; second++) {
                UtcInstant utc = UtcInstant.ofModifiedJulianDay(36204 + dayOffset, second * NANOS_PER_SEC + 2L);
                TaiInstant tai = utc.toTaiInstant();
                assertEquals(dayOffset * SECS_PER_DAY + second + 10, tai.getTaiSeconds());
                assertEquals(2, tai.getNano());
            }
        }
    }

    @Test
    public void test_toTaiInstant_overflowMax() {
        UtcInstant max = UtcInstant.ofModifiedJulianDay(Long.MAX_VALUE, 0);
        assertThrows(ArithmeticException.class, max::toTaiInstant);
    }

    //-------------------------------------------------------------------------
    // toInstant() tests
    //-------------------------------------------------------------------------
    @Test
    public void test_toInstant_conversion() {
        for (int dayOffset = -1000; dayOffset < 1000; dayOffset++) {
            for (int second = 0; second < 10; second++) {
                Instant expected = Instant.ofEpochSecond(315532800 + dayOffset * SECS_PER_DAY + second).plusNanos(2);
                UtcInstant test = UtcInstant.ofModifiedJulianDay(44239 + dayOffset, second * NANOS_PER_SEC + 2);
                assertEquals(expected, test.toInstant());
            }
        }
    }

    //-------------------------------------------------------------------------
    // Comparison and equality tests
    //-------------------------------------------------------------------------
    @Test
    public void test_comparisonOrder() {
        UtcInstant[] instants = {
            UtcInstant.ofModifiedJulianDay(-2L, 0),
            UtcInstant.ofModifiedJulianDay(-2L, NANOS_PER_DAY - 2),
            UtcInstant.ofModifiedJulianDay(-2L, NANOS_PER_DAY - 1),
            UtcInstant.ofModifiedJulianDay(-1L, 0),
            UtcInstant.ofModifiedJulianDay(-1L, 1),
            UtcInstant.ofModifiedJulianDay(-1L, NANOS_PER_DAY - 2),
            UtcInstant.ofModifiedJulianDay(-1L, NANOS_PER_DAY - 1),
            UtcInstant.ofModifiedJulianDay(0L, 0),
            UtcInstant.ofModifiedJulianDay(0L, 1),
            UtcInstant.ofModifiedJulianDay(0L, 2),
            UtcInstant.ofModifiedJulianDay(0L, NANOS_PER_DAY - 1),
            UtcInstant.ofModifiedJulianDay(1L, 0),
            UtcInstant.ofModifiedJulianDay(2L, 0)
        };

        for (int i = 0; i < instants.length; i++) {
            UtcInstant a = instants[i];
            for (int j = 0; j < instants.length; j++) {
                UtcInstant b = instants[j];
                if (i < j) {
                    assertEquals(-1, a.compareTo(b), a + " vs " + b);
                    assertFalse(a.equals(b), a + " vs " + b);
                    assertTrue(a.isBefore(b), a + " before " + b);
                    assertFalse(a.isAfter(b), a + " after " + b);
                } else if (i > j) {
                    assertEquals(1, a.compareTo(b), a + " vs " + b);
                    assertFalse(a.equals(b), a + " vs " + b);
                    assertFalse(a.isBefore(b), a + " before " + b);
                    assertTrue(a.isAfter(b), a + " after " + b);
                } else {
                    assertEquals(0, a.compareTo(b), a + " vs " + b);
                    assertTrue(a.equals(b), a + " vs " + b);
                    assertFalse(a.isBefore(b), a + " before " + b);
                    assertFalse(a.isAfter(b), a + " after " + b);
                }
            }
        }
    }

    @Test
    public void test_compareTo_null() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(0, 0);
        assertThrows(NullPointerException.class, () -> instant.compareTo(null));
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void test_compareTo_differentType() {
        Comparable comparable = UtcInstant.ofModifiedJulianDay(0, 0);
        assertThrows(ClassCastException.class, () -> comparable.compareTo(new Object()));
    }

    @Test
    public void test_equalsAndHashCode() {
        new EqualsTester()
            .addEqualityGroup(
                UtcInstant.ofModifiedJulianDay(5L, 20), 
                UtcInstant.ofModifiedJulianDay(5L, 20)
            )
            .addEqualityGroup(
                UtcInstant.ofModifiedJulianDay(5L, 30), 
                UtcInstant.ofModifiedJulianDay(5L, 30)
            )
            .addEqualityGroup(
                UtcInstant.ofModifiedJulianDay(6L, 20), 
                UtcInstant.ofModifiedJulianDay(6L, 20)
            )
            .testEquals();
    }

    //-------------------------------------------------------------------------
    // toString() and parsing tests
    //-------------------------------------------------------------------------
    public static Arguments[] toStringCases() {
        return new Arguments[] {
            Arguments.of(40587, 0, "1970-01-01T00:00:00Z", "Epoch instant"),
            Arguments.of(40588, 1, "1970-01-02T00:00:00.000000001Z", "One nano after epoch"),
            Arguments.of(40588, 1_000, "1970-01-02T00:00:00.000001Z", "One microsecond"),
            Arguments.of(40619, 1_000_000_000, "1970-02-02T00:00:01Z", "One second"),
            Arguments.of(40620, 60L * 1_000_000_000L, "1970-02-03T00:01:00Z", "One minute"),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - NANOS_PER_SEC, "1972-12-31T23:59:59Z", "Second before leap"),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, "1972-12-31T23:59:60Z", "Leap second"),
            Arguments.of(MJD_1973_01_01, 0, "1973-01-01T00:00:00Z", "After leap second")
        };
    }

    @ParameterizedTest(name = "[{index}] {3}")
    @MethodSource("toStringCases")
    public void test_toString(long mjd, long nanoOfDay, String expected, String description) {
        assertEquals(expected, UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay).toString());
    }

    @ParameterizedTest(name = "[{index}] {3}")
    @MethodSource("toStringCases")
    public void test_parse(long mjd, long nanoOfDay, String text, String description) {
        assertEquals(
            UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay),
            UtcInstant.parse(text)
        );
    }
}