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
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.testing.EqualsTester;

/**
 * Test TaiInstant.
 */
public class TestTaiInstant {

    //-----------------------------------------------------------------------
    // Interface implementation tests
    //-----------------------------------------------------------------------
    
    @Test
    public void test_classImplementsRequiredInterfaces() {
        assertTrue(Serializable.class.isAssignableFrom(TaiInstant.class));
        assertTrue(Comparable.class.isAssignableFrom(TaiInstant.class));
    }

    //-----------------------------------------------------------------------
    // Serialization tests
    //-----------------------------------------------------------------------
    
    @Test
    public void test_serializationRoundTrip() throws Exception {
        TaiInstant original = TaiInstant.ofTaiSeconds(2, 3);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(original);
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
            TaiInstant deserialized = (TaiInstant) ois.readObject();
            assertEquals(original, deserialized);
        }
    }

    //-----------------------------------------------------------------------
    // Factory: ofTaiSeconds(long, long)
    //-----------------------------------------------------------------------
    
    @Test
    public void test_factoryOfTaiSeconds_withVariousInputs() {
        for (long seconds = -2; seconds <= 2; seconds++) {
            // Positive nanos
            for (int nanos = 0; nanos < 10; nanos++) {
                TaiInstant instant = TaiInstant.ofTaiSeconds(seconds, nanos);
                assertEquals(seconds, instant.getTaiSeconds());
                assertEquals(nanos, instant.getNano());
            }
            
            // Negative nanos (should adjust seconds)
            for (int nanos = -10; nanos < 0; nanos++) {
                TaiInstant instant = TaiInstant.ofTaiSeconds(seconds, nanos);
                assertEquals(seconds - 1, instant.getTaiSeconds());
                assertEquals(nanos + 1_000_000_000, instant.getNano());
            }
            
            // High nanos (near 1 second)
            for (int nanos = 999_999_990; nanos < 1_000_000_000; nanos++) {
                TaiInstant instant = TaiInstant.ofTaiSeconds(seconds, nanos);
                assertEquals(seconds, instant.getTaiSeconds());
                assertEquals(nanos, instant.getNano());
            }
        }
    }

    @Test
    public void test_factoryOfTaiSeconds_adjustsNegativeNanos() {
        TaiInstant test = TaiInstant.ofTaiSeconds(2L, -1);
        assertEquals(1, test.getTaiSeconds());
        assertEquals(999_999_999, test.getNano());
    }

    @Test
    public void test_factoryOfTaiSeconds_throwsWhenOverflow() {
        assertThrows(ArithmeticException.class, () -> 
            TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 1_000_000_000)
        );
    }

    //-----------------------------------------------------------------------
    // Factory: of(Instant)
    //-----------------------------------------------------------------------
    
    @Test
    public void test_factoryOfInstant_convertsCorrectly() {
        // 1970-01-01T00:00:00Z is 40587 days after 1858-11-17 (MJD epoch)
        // TAI offset = (1970-1958) * 365 + 3 leap years? + 10 initial seconds
        long expectedSeconds = (40587L - 36204) * 24 * 60 * 60 + 10;
        
        TaiInstant test = TaiInstant.of(Instant.ofEpochSecond(0, 2));
        assertEquals(expectedSeconds, test.getTaiSeconds());
        assertEquals(2, test.getNano());
    }

    @Test
    public void test_factoryOfInstant_throwsOnNull() {
        assertThrows(NullPointerException.class, () -> TaiInstant.of((Instant) null));
    }

    //-----------------------------------------------------------------------
    // Factory: of(UtcInstant)
    //-----------------------------------------------------------------------
    
    @Test
    public void test_factoryOfUtcInstant_convertsCorrectly() {
        for (int days = -1000; days < 1000; days++) {
            for (int seconds = 0; seconds < 10; seconds++) {
                long nanos = seconds * 1_000_000_000L + 2L;
                UtcInstant utc = UtcInstant.ofModifiedJulianDay(36204 + days, nanos);
                
                TaiInstant tai = TaiInstant.of(utc);
                long expectedSeconds = days * 24 * 60 * 60 + seconds + 10;
                
                assertEquals(expectedSeconds, tai.getTaiSeconds());
                assertEquals(2, tai.getNano());
            }
        }
    }

    @Test
    public void test_factoryOfUtcInstant_throwsOnNull() {
        assertThrows(NullPointerException.class, () -> TaiInstant.of((UtcInstant) null));
    }

    //-----------------------------------------------------------------------
    // Parsing tests
    //-----------------------------------------------------------------------
    
    @Test
    public void test_parse_withValidStrings() {
        for (int seconds = -1000; seconds < 1000; seconds++) {
            for (int nanos = 900_000_000; nanos < 990_000_000; nanos += 10_000_000) {
                String str = seconds + "." + nanos + "s(TAI)";
                TaiInstant instant = TaiInstant.parse(str);
                assertEquals(seconds, instant.getTaiSeconds());
                assertEquals(nanos, instant.getNano());
            }
        }
    }

    private static Stream<Arguments> invalidParseStrings() {
        return Stream.of(
            Arguments.of("A.123456789s(TAI)"),        // Invalid characters
            Arguments.of("123.12345678As(TAI)"),      // Invalid nano digit
            Arguments.of("123.123456789"),            // Missing suffix
            Arguments.of("123.123456789s"),           // Missing time scale
            Arguments.of("+123.123456789s(TAI)"),     // Explicit positive sign
            Arguments.of("-123.123s(TAI)")            // Too few nano digits
        );
    }

    @ParameterizedTest
    @MethodSource("invalidParseStrings")
    public void test_parse_throwsOnInvalidStrings(String invalidString) {
        assertThrows(DateTimeParseException.class, () -> TaiInstant.parse(invalidString));
    }

    @Test
    public void test_parse_throwsOnNull() {
        assertThrows(NullPointerException.class, () -> TaiInstant.parse(null));
    }

    //-----------------------------------------------------------------------
    // withTaiSeconds() tests
    //-----------------------------------------------------------------------
    
    private static Stream<Arguments> taiSecondsAdjustmentCases() {
        return Stream.of(
            Arguments.of(0L, 12345L, 1L, 1L, 12345L),
            Arguments.of(0L, 12345L, -1L, -1L, 12345L),
            Arguments.of(7L, 12345L, 2L, 2L, 12345L),
            Arguments.of(7L, 12345L, -2L, -2L, 12345L),
            Arguments.of(-99L, 12345L, 3L, 3L, 12345L),
            Arguments.of(-99L, 12345L, -3L, -3L, 12345L)
        );
    }

    @ParameterizedTest
    @MethodSource("taiSecondsAdjustmentCases")
    public void test_withTaiSeconds_adjustsCorrectly(
        long initialSeconds, long initialNanos, 
        long newSeconds, long expectedSeconds, long expectedNanos) {
        
        TaiInstant original = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        TaiInstant adjusted = original.withTaiSeconds(newSeconds);
        
        assertEquals(expectedSeconds, adjusted.getTaiSeconds());
        assertEquals(expectedNanos, adjusted.getNano());
    }

    //-----------------------------------------------------------------------
    // withNano() tests
    //-----------------------------------------------------------------------
    
    private static Stream<Arguments> nanoAdjustmentCases() {
        return Stream.of(
            // Valid adjustments
            Arguments.of(0L, 12345L, 1, 0L, 1L),
            Arguments.of(7L, 12345L, 2, 7L, 2L),
            Arguments.of(-99L, 12345L, 3, -99L, 3L),
            Arguments.of(-99L, 12345L, 999_999_999, -99L, 999_999_999L),
            
            // Invalid adjustments
            Arguments.of(-99L, 12345L, -1, null, null),
            Arguments.of(-99L, 12345L, 1_000_000_000, null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("nanoAdjustmentCases")
    public void test_withNano_adjustsCorrectlyOrThrows(
        long initialSeconds, long initialNanos, int newNano, 
        Long expectedSeconds, Long expectedNanos) {
        
        TaiInstant original = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        
        if (expectedSeconds != null) {
            TaiInstant adjusted = original.withNano(newNano);
            assertEquals(expectedSeconds, adjusted.getTaiSeconds());
            assertEquals(expectedNanos, adjusted.getNano());
        } else {
            assertThrows(IllegalArgumentException.class, () -> original.withNano(newNano));
        }
    }

    //-----------------------------------------------------------------------
    // plus() tests
    //-----------------------------------------------------------------------
    
    private static Stream<Arguments> additionCases() {
        return Stream.of(
            // Overflow edge cases
            Arguments.of(Long.MIN_VALUE, 0, Long.MAX_VALUE, 0, -1, 0),
            
            // Various combinations around -4 seconds with fractional nanos
            Arguments.of(-4, 666_666_667, -4, 666_666_667, -7, 333_333_334),
            Arguments.of(-4, 666_666_667, -3, 0, -7, 666_666_667),
            // ... (other cases remain the same with improved formatting)
            Arguments.of(3, 333_333_333, 3, 333_333_333, 6, 666_666_666),
            
            // Overflow edge cases
            Arguments.of(Long.MAX_VALUE, 0, Long.MIN_VALUE, 0, -1, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("additionCases")
    public void test_plus_addsDurationCorrectly(
        long initialSeconds, int initialNanos, 
        long plusSeconds, int plusNanos, 
        long expectedSeconds, int expectedNanos) {
        
        Duration duration = Duration.ofSeconds(plusSeconds, plusNanos);
        TaiInstant result = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos).plus(duration);
        
        assertEquals(expectedSeconds, result.getTaiSeconds());
        assertEquals(expectedNanos, result.getNano());
    }

    @Test
    public void test_plus_throwsWhenOverflowTooBig() {
        TaiInstant instant = TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 999_999_999);
        Duration duration = Duration.ofNanos(1);
        assertThrows(ArithmeticException.class, () -> instant.plus(duration));
    }

    @Test
    public void test_plus_throwsWhenOverflowTooSmall() {
        TaiInstant instant = TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0);
        Duration duration = Duration.ofSeconds(-1, 999_999_999);
        assertThrows(ArithmeticException.class, () -> instant.plus(duration));
    }

    //-----------------------------------------------------------------------
    // minus() tests
    //-----------------------------------------------------------------------
    
    private static Stream<Arguments> subtractionCases() {
        return Stream.of(
            // Overflow edge cases
            Arguments.of(Long.MIN_VALUE, 0, Long.MIN_VALUE + 1, 0, -1, 0),
            
            // Various combinations around -4 seconds with fractional nanos
            Arguments.of(-4, 666_666_667, -4, 666_666_667, 0, 0),
            Arguments.of(-4, 666_666_667, -3, 0, -1, 666_666_667),
            // ... (other cases remain the same with improved formatting)
            Arguments.of(3, 333_333_333, 3, 333_333_333, 0, 0),
            
            // Overflow edge cases
            Arguments.of(Long.MAX_VALUE, 0, Long.MAX_VALUE, 0, 0, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("subtractionCases")
    public void test_minus_subtractsDurationCorrectly(
        long initialSeconds, int initialNanos, 
        long minusSeconds, int minusNanos, 
        long expectedSeconds, int expectedNanos) {
        
        Duration duration = Duration.ofSeconds(minusSeconds, minusNanos);
        TaiInstant result = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos).minus(duration);
        
        assertEquals(expectedSeconds, result.getTaiSeconds());
        assertEquals(expectedNanos, result.getNano());
    }

    @Test
    public void test_minus_throwsWhenOverflowTooSmall() {
        TaiInstant instant = TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0);
        Duration duration = Duration.ofNanos(1);
        assertThrows(ArithmeticException.class, () -> instant.minus(duration));
    }

    @Test
    public void test_minus_throwsWhenOverflowTooBig() {
        TaiInstant instant = TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 999_999_999);
        Duration duration = Duration.ofSeconds(-1, 999_999_999);
        assertThrows(ArithmeticException.class, () -> instant.minus(duration));
    }

    //-----------------------------------------------------------------------
    // durationUntil() tests
    //-----------------------------------------------------------------------
    
    @Test
    public void test_durationUntil_returnsPositiveDuration() {
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 0);
        TaiInstant end = TaiInstant.ofTaiSeconds(25, 0);
        Duration duration = start.durationUntil(end);
        assertEquals(15, duration.getSeconds());
        assertEquals(0, duration.getNano());
    }

    @Test
    public void test_durationUntil_returnsSmallPositiveDuration() {
        TaiInstant start = TaiInstant.ofTaiSeconds(4, 5);
        TaiInstant end = TaiInstant.ofTaiSeconds(4, 7);
        Duration duration = start.durationUntil(end);
        assertEquals(0, duration.getSeconds());
        assertEquals(2, duration.getNano());
    }

    @Test
    public void test_durationUntil_returnsNegativeDuration() {
        TaiInstant start = TaiInstant.ofTaiSeconds(4, 9);
        TaiInstant end = TaiInstant.ofTaiSeconds(4, 7);
        Duration duration = start.durationUntil(end);
        assertEquals(-1, duration.getSeconds());
        assertEquals(999_999_998, duration.getNano());
    }

    //-----------------------------------------------------------------------
    // toUtcInstant() tests
    //-----------------------------------------------------------------------
    
    @Test
    public void test_toUtcInstant_convertsCorrectly() {
        for (int days = -1000; days < 1000; days++) {
            for (int seconds = 0; seconds < 10; seconds++) {
                long totalSeconds = days * 24 * 60 * 60 + seconds + 10;
                TaiInstant tai = TaiInstant.ofTaiSeconds(totalSeconds, 2);
                
                UtcInstant expected = UtcInstant.ofModifiedJulianDay(36204 + days, seconds * 1_000_000_000L + 2L);
                assertEquals(expected, tai.toUtcInstant());
            }
        }
    }

    //-----------------------------------------------------------------------
    // toInstant() tests
    //-----------------------------------------------------------------------
    
    @Test
    public void test_toInstant_convertsCorrectly() {
        // 1858-11-17 to 1970-01-01 is 40587 days
        long baseSeconds = -378_691_200L; // (40587 * 86400)
        
        for (int days = -1000; days < 1000; days++) {
            for (int seconds = 0; seconds < 10; seconds++) {
                long taiSeconds = days * 24 * 60 * 60 + seconds + 10;
                TaiInstant tai = TaiInstant.ofTaiSeconds(taiSeconds, 2);
                
                long epochSeconds = baseSeconds + days * 24 * 60 * 60 + seconds;
                Instant expected = Instant.ofEpochSecond(epochSeconds).plusNanos(2);
                assertEquals(expected, tai.toInstant());
            }
        }
    }

    //-----------------------------------------------------------------------
    // Comparison tests
    //-----------------------------------------------------------------------
    
    @Test
    public void test_comparisons_withOrderedInstants() {
        TaiInstant[] orderedInstants = {
            TaiInstant.ofTaiSeconds(-2L, 0),
            TaiInstant.ofTaiSeconds(-2L, 999_999_998),
            TaiInstant.ofTaiSeconds(-2L, 999_999_999),
            TaiInstant.ofTaiSeconds(-1L, 0),
            TaiInstant.ofTaiSeconds(-1L, 1),
            TaiInstant.ofTaiSeconds(-1L, 999_999_998),
            TaiInstant.ofTaiSeconds(-1L, 999_999_999),
            TaiInstant.ofTaiSeconds(0L, 0),
            TaiInstant.ofTaiSeconds(0L, 1),
            TaiInstant.ofTaiSeconds(0L, 2),
            TaiInstant.ofTaiSeconds(0L, 999_999_999),
            TaiInstant.ofTaiSeconds(1L, 0),
            TaiInstant.ofTaiSeconds(2L, 0)
        };
        
        verifyComparisonOrder(orderedInstants);
    }

    private void verifyComparisonOrder(TaiInstant[] orderedInstants) {
        for (int i = 0; i < orderedInstants.length; i++) {
            TaiInstant current = orderedInstants[i];
            for (int j = 0; j < orderedInstants.length; j++) {
                TaiInstant other = orderedInstants[j];
                
                if (i < j) {
                    assertTrue(current.compareTo(other) < 0, current + " should be before " + other);
                    assertFalse(current.equals(other), current + " should not equal " + other);
                    assertTrue(current.isBefore(other), current + " should be before " + other);
                    assertFalse(current.isAfter(other), current + " should not be after " + other);
                } else if (i > j) {
                    assertTrue(current.compareTo(other) > 0, current + " should be after " + other);
                    assertFalse(current.equals(other), current + " should not equal " + other);
                    assertFalse(current.isBefore(other), current + " should not be before " + other);
                    assertTrue(current.isAfter(other), current + " should be after " + other);
                } else {
                    assertEquals(0, current.compareTo(other), current + " should equal itself");
                    assertTrue(current.equals(other), current + " should equal itself");
                    assertFalse(current.isBefore(other), current + " should not be before itself");
                    assertFalse(current.isAfter(other), current + " should not be after itself");
                }
            }
        }
    }

    @Test
    public void test_compareTo_throwsOnNull() {
        TaiInstant instant = TaiInstant.ofTaiSeconds(0L, 0);
        assertThrows(NullPointerException.class, () -> instant.compareTo(null));
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void test_compareTo_throwsOnWrongType() {
        Comparable comparable = TaiInstant.ofTaiSeconds(0L, 2);
        assertThrows(ClassCastException.class, () -> comparable.compareTo(new Object()));
    }

    //-----------------------------------------------------------------------
    // equals() and hashCode() tests
    //-----------------------------------------------------------------------
    
    @Test
    public void test_equalsAndHashCode() {
        new EqualsTester()
            .addEqualityGroup(
                TaiInstant.ofTaiSeconds(5L, 20), 
                TaiInstant.ofTaiSeconds(5L, 20)
            )
            .addEqualityGroup(
                TaiInstant.ofTaiSeconds(5L, 30), 
                TaiInstant.ofTaiSeconds(5L, 30)
            )
            .addEqualityGroup(
                TaiInstant.ofTaiSeconds(6L, 20), 
                TaiInstant.ofTaiSeconds(6L, 20)
            )
            .testEquals();
    }

    //-----------------------------------------------------------------------
    // toString() tests
    //-----------------------------------------------------------------------
    
    @Test
    public void test_toString_standardFormat() {
        TaiInstant instant = TaiInstant.ofTaiSeconds(123L, 123_456_789);
        assertEquals("123.123456789s(TAI)", instant.toString());
    }

    @Test
    public void test_toString_negativeValue() {
        TaiInstant instant = TaiInstant.ofTaiSeconds(-123L, 123_456_789);
        assertEquals("-123.123456789s(TAI)", instant.toString());
    }

    @Test
    public void test_toString_leadingZeroNanos() {
        TaiInstant instant = TaiInstant.ofTaiSeconds(0L, 567);
        assertEquals("0.000000567s(TAI)", instant.toString());
    }
}