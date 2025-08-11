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
 *    this list of conditions and the distribution in the documentation
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
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.testing.EqualsTester;

/**
 * Test UtcInstant.
 */
public class TestUtcInstant {

    // Historical leap second dates for testing
    private static final long MJD_1972_12_30 = 41681;           // Day before first leap second
    private static final long MJD_1972_12_31_LEAP = 41682;      // First leap second day (1972-12-31)
    private static final long MJD_1973_01_01 = 41683;           // Day after first leap second
    private static final long MJD_1973_12_31_LEAP = MJD_1972_12_31_LEAP + 365; // Second leap second day
    
    // Time constants for clarity
    private static final long SECONDS_PER_DAY = 24L * 60 * 60;
    private static final long NANOS_PER_SECOND = 1000000000L;
    private static final long NANOS_PER_DAY = SECONDS_PER_DAY * NANOS_PER_SECOND;
    private static final long NANOS_PER_LEAP_DAY = (SECONDS_PER_DAY + 1) * NANOS_PER_SECOND; // +1 for leap second

    //-----------------------------------------------------------------------
    // Basic interface compliance tests
    //-----------------------------------------------------------------------
    
    @Test
    public void shouldImplementRequiredInterfaces() {
        assertTrue(Serializable.class.isAssignableFrom(UtcInstant.class));
        assertTrue(Comparable.class.isAssignableFrom(UtcInstant.class));
    }

    @Test
    public void shouldSerializeAndDeserializeCorrectly() throws Exception {
        UtcInstant originalInstant = UtcInstant.ofModifiedJulianDay(2, 3);
        
        // Serialize
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        try (ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput)) {
            objectOutput.writeObject(originalInstant);
        }
        
        // Deserialize
        try (ObjectInputStream objectInput = new ObjectInputStream(new ByteArrayInputStream(byteOutput.toByteArray()))) {
            UtcInstant deserializedInstant = (UtcInstant) objectInput.readObject();
            assertEquals(originalInstant, deserializedInstant);
        }
    }

    //-----------------------------------------------------------------------
    // Factory method: ofModifiedJulianDay(long, long)
    //-----------------------------------------------------------------------
    
    @Test
    public void ofModifiedJulianDay_shouldCreateValidInstants() {
        // Test various combinations of days and nanoseconds
        for (long dayOffset = -2; dayOffset <= 2; dayOffset++) {
            for (int nanoOffset = 0; nanoOffset < 10; nanoOffset++) {
                UtcInstant instant = UtcInstant.ofModifiedJulianDay(dayOffset, nanoOffset);
                
                assertEquals(dayOffset, instant.getModifiedJulianDay());
                assertEquals(nanoOffset, instant.getNanoOfDay());
                assertFalse(instant.isLeapSecond(), "Regular time should not be leap second");
            }
        }
    }

    @Test
    public void ofModifiedJulianDay_shouldHandleEndOfNormalDay() {
        // Test the last nanosecond of a normal day (23:59:59.999999999)
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1);
        
        assertEquals(MJD_1972_12_31_LEAP, instant.getModifiedJulianDay());
        assertEquals(NANOS_PER_DAY - 1, instant.getNanoOfDay());
        assertFalse(instant.isLeapSecond());
        assertEquals("1972-12-31T23:59:59.999999999Z", instant.toString());
    }

    @Test
    public void ofModifiedJulianDay_shouldHandleStartOfLeapSecond() {
        // Test the start of leap second (23:59:60.000000000)
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_DAY);
        
        assertEquals(MJD_1972_12_31_LEAP, instant.getModifiedJulianDay());
        assertEquals(NANOS_PER_DAY, instant.getNanoOfDay());
        assertTrue(instant.isLeapSecond());
        assertEquals("1972-12-31T23:59:60Z", instant.toString());
    }

    @Test
    public void ofModifiedJulianDay_shouldHandleEndOfLeapSecond() {
        // Test the last nanosecond of leap second (23:59:60.999999999)
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1);
        
        assertEquals(MJD_1972_12_31_LEAP, instant.getModifiedJulianDay());
        assertEquals(NANOS_PER_LEAP_DAY - 1, instant.getNanoOfDay());
        assertTrue(instant.isLeapSecond());
        assertEquals("1972-12-31T23:59:60.999999999Z", instant.toString());
    }

    @Test
    public void ofModifiedJulianDay_shouldRejectNegativeNanos() {
        assertThrows(DateTimeException.class, 
            () -> UtcInstant.ofModifiedJulianDay(MJD_1973_01_01, -1),
            "Negative nanoseconds should be rejected");
    }

    @Test
    public void ofModifiedJulianDay_shouldRejectTooLargeNanosForNormalDay() {
        assertThrows(DateTimeException.class, 
            () -> UtcInstant.ofModifiedJulianDay(MJD_1973_01_01, NANOS_PER_DAY),
            "Nanoseconds beyond normal day length should be rejected for non-leap days");
    }

    @Test
    public void ofModifiedJulianDay_shouldRejectTooLargeNanosForLeapDay() {
        assertThrows(DateTimeException.class, 
            () -> UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY),
            "Nanoseconds beyond leap day length should be rejected");
    }

    //-----------------------------------------------------------------------
    // Factory method: of(Instant)
    //-----------------------------------------------------------------------
    
    @Test
    public void of_shouldConvertInstantCorrectly() {
        // Unix epoch (1970-01-01) corresponds to MJD 40587
        UtcInstant utcInstant = UtcInstant.of(Instant.ofEpochSecond(0, 2));
        
        assertEquals(40587, utcInstant.getModifiedJulianDay());
        assertEquals(2, utcInstant.getNanoOfDay());
    }

    @Test
    public void of_shouldRejectNullInstant() {
        assertThrows(NullPointerException.class, 
            () -> UtcInstant.of((Instant) null),
            "Null Instant should be rejected");
    }

    //-----------------------------------------------------------------------
    // Factory method: of(TaiInstant)
    //-----------------------------------------------------------------------
    
    @Test
    public void of_shouldConvertTaiInstantCorrectly() {
        // Test conversion from TAI to UTC for various dates
        for (int dayOffset = -1000; dayOffset < 1000; dayOffset++) {
            for (int secondOffset = 0; secondOffset < 10; secondOffset++) {
                UtcInstant expectedUtc = UtcInstant.ofModifiedJulianDay(
                    36204 + dayOffset, 
                    secondOffset * NANOS_PER_SECOND + 2L
                );
                TaiInstant taiInstant = TaiInstant.ofTaiSeconds(
                    dayOffset * SECONDS_PER_DAY + secondOffset + 10, 
                    2
                );
                
                assertEquals(expectedUtc, UtcInstant.of(taiInstant));
            }
        }
    }

    @Test
    public void of_shouldRejectNullTaiInstant() {
        assertThrows(NullPointerException.class, 
            () -> UtcInstant.of((TaiInstant) null),
            "Null TaiInstant should be rejected");
    }

    //-----------------------------------------------------------------------
    // Factory method: parse(CharSequence)
    //-----------------------------------------------------------------------
    
    @Test
    public void parse_shouldHandleValidTimeStrings() {
        // Normal second before leap second
        assertEquals(
            UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_DAY - NANOS_PER_SECOND), 
            UtcInstant.parse("1972-12-31T23:59:59Z")
        );
        
        // Leap second
        assertEquals(
            UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_DAY), 
            UtcInstant.parse("1972-12-31T23:59:60Z")
        );
    }

    public static Object[][] invalidParseStrings() {
        return new Object[][] {
            {""},                           // Empty string
            {"A"},                          // Invalid format
            {"2012-13-01T00:00:00Z"},      // Invalid month (13)
        };
    }

    @ParameterizedTest
    @MethodSource("invalidParseStrings")
    public void parse_shouldRejectInvalidStrings(String invalidString) {
        assertThrows(DateTimeException.class, 
            () -> UtcInstant.parse(invalidString),
            "Invalid string should be rejected: " + invalidString);
    }

    @Test
    public void parse_shouldRejectInvalidLeapSecond() {
        // Leap second on a day that doesn't have one
        assertThrows(DateTimeException.class, 
            () -> UtcInstant.parse("1972-11-11T23:59:60Z"),
            "Leap second should only be valid on actual leap second days");
    }

    @Test
    public void parse_shouldRejectNullString() {
        assertThrows(NullPointerException.class, 
            () -> UtcInstant.parse((String) null),
            "Null string should be rejected");
    }

    //-----------------------------------------------------------------------
    // Method: withModifiedJulianDay()
    //-----------------------------------------------------------------------
    
    public static Object[][] modifiedJulianDayTestData() {
        return new Object[][] {
            // originalMjd, originalNanos, newMjd, expectedMjd, expectedNanos
            {0L, 12345L, 1L, 1L, 12345L},                                    // Normal day change
            {0L, 12345L, -1L, -1L, 12345L},                                  // Negative day change
            {7L, 12345L, 2L, 2L, 12345L},                                    // Positive day change
            {7L, 12345L, -2L, -2L, 12345L},                                  // Another negative change
            {-99L, 12345L, 3L, 3L, 12345L},                                  // From negative to positive
            {-99L, 12345L, -3L, -3L, 12345L},                                // Between negatives
            
            // Leap second scenarios
            {MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_30, null, null},      // Leap second to non-leap day (invalid)
            {MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_31_LEAP, MJD_1972_12_31_LEAP, NANOS_PER_DAY}, // Same leap day
            {MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_01_01, null, null},      // Leap second to non-leap day (invalid)
            {MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_12_31_LEAP, MJD_1973_12_31_LEAP, NANOS_PER_DAY}, // To another leap day
        };
    }

    @ParameterizedTest
    @MethodSource("modifiedJulianDayTestData")
    public void withModifiedJulianDay_shouldHandleValidAndInvalidChanges(
            long originalMjd, long originalNanos, long newMjd, 
            Long expectedMjd, Long expectedNanos) {
        
        UtcInstant originalInstant = UtcInstant.ofModifiedJulianDay(originalMjd, originalNanos);
        
        if (expectedMjd != null) {
            // Valid change expected
            UtcInstant modifiedInstant = originalInstant.withModifiedJulianDay(newMjd);
            assertEquals(expectedMjd.longValue(), modifiedInstant.getModifiedJulianDay());
            assertEquals(expectedNanos.longValue(), modifiedInstant.getNanoOfDay());
        } else {
            // Invalid change expected
            assertThrows(DateTimeException.class, 
                () -> originalInstant.withModifiedJulianDay(newMjd),
                "Should reject invalid day change that makes nanoseconds invalid");
        }
    }

    //-----------------------------------------------------------------------
    // Method: withNanoOfDay()
    //-----------------------------------------------------------------------
    
    public static Object[][] nanoOfDayTestData() {
        return new Object[][] {
            // originalMjd, originalNanos, newNanos, expectedMjd, expectedNanos
            {0L, 12345L, 1L, 0L, 1L},                                        // Normal change
            {0L, 12345L, -1L, null, null},                                   // Invalid negative
            {7L, 12345L, 2L, 7L, 2L},                                        // Another normal change
            {-99L, 12345L, 3L, -99L, 3L},                                    // Negative day, positive change
            
            // End of normal day scenarios
            {MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1, MJD_1972_12_30, NANOS_PER_DAY - 1},
            {MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1},
            {MJD_1973_01_01, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1, MJD_1973_01_01, NANOS_PER_DAY - 1},
            
            // Leap second scenarios
            {MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_DAY, null, null},                    // Non-leap day can't have leap second
            {MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_DAY, MJD_1972_12_31_LEAP, NANOS_PER_DAY}, // Leap day can have leap second
            {MJD_1973_01_01, NANOS_PER_DAY - 1, NANOS_PER_DAY, null, null},                    // Non-leap day can't have leap second
            
            // End of leap second scenarios
            {MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY - 1, null, null},           // Non-leap day can't have leap second end
            {MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1}, // Leap day can
            {MJD_1973_01_01, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY - 1, null, null},           // Non-leap day can't have leap second end
            
            // Beyond leap day scenarios (all invalid)
            {MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY, null, null},
            {MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY, null, null},
            {MJD_1973_01_01, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY, null, null},
        };
    }

    @ParameterizedTest
    @MethodSource("nanoOfDayTestData")
    public void withNanoOfDay_shouldHandleValidAndInvalidChanges(
            long originalMjd, long originalNanos, long newNanoOfDay, 
            Long expectedMjd, Long expectedNanos) {
        
        UtcInstant originalInstant = UtcInstant.ofModifiedJulianDay(originalMjd, originalNanos);
        
        if (expectedMjd != null) {
            // Valid change expected
            UtcInstant modifiedInstant = originalInstant.withNanoOfDay(newNanoOfDay);
            assertEquals(expectedMjd.longValue(), modifiedInstant.getModifiedJulianDay());
            assertEquals(expectedNanos.longValue(), modifiedInstant.getNanoOfDay());
        } else {
            // Invalid change expected
            assertThrows(DateTimeException.class, 
                () -> originalInstant.withNanoOfDay(newNanoOfDay),
                "Should reject invalid nano-of-day value");
        }
    }

    //-----------------------------------------------------------------------
    // Method: plus(Duration)
    //-----------------------------------------------------------------------
    
    public static Object[][] plusDurationTestData() {
        return new Object[][] {
            // originalMjd, originalNanos, plusSeconds, plusNanos, expectedMjd, expectedNanos
            {0, 0,  -2 * SECONDS_PER_DAY, 5, -2, 5},                        // Subtract days
            {0, 0,  -1 * SECONDS_PER_DAY, 1, -1, 1},                        // Subtract one day
            {0, 0,  -1 * SECONDS_PER_DAY, 0, -1, 0},                        // Subtract exactly one day
            {0, 0,  0, -2, -1,  NANOS_PER_DAY - 2},                         // Subtract nanoseconds (underflow)
            {0, 0,  0, -1, -1,  NANOS_PER_DAY - 1},                         // Subtract one nanosecond (underflow)
            {0, 0,  0, 0,  0,  0},                                           // Add nothing
            {0, 0,  0, 1,  0,  1},                                           // Add one nanosecond
            {0, 0,  0, 2,  0,  2},                                           // Add two nanoseconds
            {0, 0,  1, 0,  0,  1 * NANOS_PER_SECOND},                       // Add one second
            {0, 0,  2, 0,  0,  2 * NANOS_PER_SECOND},                       // Add two seconds
            {0, 0,  3, 333333333,  0,  3 * NANOS_PER_SECOND + 333333333},   // Add seconds and nanos
            {0, 0,  1 * SECONDS_PER_DAY, 0,  1, 0},                         // Add one day
            {0, 0,  1 * SECONDS_PER_DAY, 1,  1, 1},                         // Add one day and one nano
            {0, 0,  2 * SECONDS_PER_DAY, 5,  2, 5},                         // Add two days and nanos
            
            // Tests starting from day 1
            {1, 0,  -2 * SECONDS_PER_DAY, 5, -1, 5},
            {1, 0,  -1 * SECONDS_PER_DAY, 1, 0, 1},
            {1, 0,  -1 * SECONDS_PER_DAY, 0, 0, 0},
            {1, 0,  0, -2,  0,  NANOS_PER_DAY - 2},
            {1, 0,  0, -1,  0,  NANOS_PER_DAY - 1},
            {1, 0,  0, 0,  1,  0},
            {1, 0,  0, 1,  1,  1},
            {1, 0,  0, 2,  1,  2},
            {1, 0,  1, 0,  1,  1 * NANOS_PER_SECOND},
            {1, 0,  2, 0,  1,  2 * NANOS_PER_SECOND},
            {1, 0,  3, 333333333,  1,  3 * NANOS_PER_SECOND + 333333333},
            {1, 0,  1 * SECONDS_PER_DAY, 0,  2, 0},
            {1, 0,  1 * SECONDS_PER_DAY, 1,  2, 1},
            {1, 0,  2 * SECONDS_PER_DAY, 5,  3, 5},
        };
    }

    @ParameterizedTest
    @MethodSource("plusDurationTestData")
    public void plus_shouldAddDurationCorrectly(
            long originalMjd, long originalNanos, long plusSeconds, int plusNanos, 
            long expectedMjd, long expectedNanos) {
        
        UtcInstant originalInstant = UtcInstant.ofModifiedJulianDay(originalMjd, originalNanos);
        UtcInstant resultInstant = originalInstant.plus(Duration.ofSeconds(plusSeconds, plusNanos));
        
        assertEquals(expectedMjd, resultInstant.getModifiedJulianDay());
        assertEquals(expectedNanos, resultInstant.getNanoOfDay());
    }

    @Test
    public void plus_shouldThrowOnOverflowTooBig() {
        UtcInstant maxInstant = UtcInstant.ofModifiedJulianDay(Long.MAX_VALUE, NANOS_PER_DAY - 1);
        assertThrows(ArithmeticException.class, 
            () -> maxInstant.plus(Duration.ofNanos(1)),
            "Should throw on overflow when adding to maximum instant");
    }

    @Test
    public void plus_shouldThrowOnOverflowTooSmall() {
        UtcInstant minInstant = UtcInstant.ofModifiedJulianDay(Long.MIN_VALUE, 0);
        assertThrows(ArithmeticException.class, 
            () -> minInstant.plus(Duration.ofNanos(-1)),
            "Should throw on underflow when subtracting from minimum instant");
    }

    //-----------------------------------------------------------------------
    // Method: minus(Duration)
    //-----------------------------------------------------------------------
    
    public static Object[][] minusDurationTestData() {
        return new Object[][] {
            // originalMjd, originalNanos, minusSeconds, minusNanos, expectedMjd, expectedNanos
            {0, 0,  2 * SECONDS_PER_DAY, -5, -2, 5},                        // Subtract negative nanos (add)
            {0, 0,  1 * SECONDS_PER_DAY, -1, -1, 1},                        // Subtract day, add nano
            {0, 0,  1 * SECONDS_PER_DAY, 0, -1, 0},                         // Subtract exactly one day
            {0, 0,  0, 2, -1,  NANOS_PER_DAY - 2},                          // Subtract nanos (underflow)
            {0, 0,  0, 1, -1,  NANOS_PER_DAY - 1},                          // Subtract one nano (underflow)
            {0, 0,  0, 0,  0,  0},                                           // Subtract nothing
            {0, 0,  0, -1,  0,  1},                                          // Subtract negative nano (add)
            {0, 0,  0, -2,  0,  2},                                          // Subtract negative nanos (add)
            {0, 0,  -1, 0,  0,  1 * NANOS_PER_SECOND},                      // Subtract negative second (add)
            {0, 0,  -2, 0,  0,  2 * NANOS_PER_SECOND},                      // Subtract negative seconds (add)
            {0, 0,  -3, -333333333,  0,  3 * NANOS_PER_SECOND + 333333333}, // Subtract negative duration (add)
            {0, 0,  -1 * SECONDS_PER_DAY, 0,  1, 0},                        // Subtract negative day (add)
            {0, 0,  -1 * SECONDS_PER_DAY, -1,  1, 1},                       // Subtract negative day and nano (add)
            {0, 0,  -2 * SECONDS_PER_DAY, -5,  2, 5},                       // Subtract negative duration (add)
            
            // Tests starting from day 1
            {1, 0,  2 * SECONDS_PER_DAY, -5, -1, 5},
            {1, 0,  1 * SECONDS_PER_DAY, -1, 0, 1},
            {1, 0,  1 * SECONDS_PER_DAY, 0, 0, 0},
            {1, 0,  0, 2,  0,  NANOS_PER_DAY - 2},
            {1, 0,  0, 1,  0,  NANOS_PER_DAY - 1},
            {1, 0,  0, 0,  1,  0},
            {1, 0,  0, -1,  1,  1},
            {1, 0,  0, -2,  1,  2},
            {1, 0,  -1, 0,  1,  1 * NANOS_PER_SECOND},
            {1, 0,  -2, 0,  1,  2 * NANOS_PER_SECOND},
            {1, 0,  -3, -333333333,  1,  3 * NANOS_PER_SECOND + 333333333},
            {1, 0,  -1 * SECONDS_PER_DAY, 0,  2, 0},
            {1, 0,  -1 * SECONDS_PER_DAY, -1,  2, 1},
            {1, 0,  -2 * SECONDS_PER_DAY, -5,  3, 5},
        };
    }

    @ParameterizedTest
    @MethodSource("minusDurationTestData")
    public void minus_shouldSubtractDurationCorrectly(
            long originalMjd, long originalNanos, long minusSeconds, int minusNanos, 
            long expectedMjd, long expectedNanos) {
        
        UtcInstant originalInstant = UtcInstant.ofModifiedJulianDay(originalMjd, originalNanos);
        UtcInstant resultInstant = originalInstant.minus(Duration.ofSeconds(minusSeconds, minusNanos));
        
        assertEquals(expectedMjd, resultInstant.getModifiedJulianDay());
        assertEquals(expectedNanos, resultInstant.getNanoOfDay());
    }

    @Test
    public void minus_shouldThrowOnUnderflow() {
        UtcInstant minInstant = UtcInstant.ofModifiedJulianDay(Long.MIN_VALUE, 0);
        assertThrows(ArithmeticException.class, 
            () -> minInstant.minus(Duration.ofNanos(1)),
            "Should throw on underflow when subtracting from minimum instant");
    }

    @Test
    public void minus_shouldThrowOnOverflow() {
        UtcInstant maxInstant = UtcInstant.ofModifiedJulianDay(Long.MAX_VALUE, NANOS_PER_DAY - 1);
        assertThrows(ArithmeticException.class, 
            () -> maxInstant.minus(Duration.ofNanos(-1)),
            "Should throw on overflow when subtracting negative from maximum instant");
    }

    //-----------------------------------------------------------------------
    // Method: durationUntil()
    //-----------------------------------------------------------------------
    
    @Test
    public void durationUntil_shouldCalculateNormalDayDuration() {
        // Duration from one normal day to the next
        UtcInstant startDay = UtcInstant.ofModifiedJulianDay(MJD_1972_12_30, 0);
        UtcInstant endDay = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, 0);
        
        Duration duration = startDay.durationUntil(endDay);
        
        assertEquals(86400, duration.getSeconds(), "Normal day should have 86400 seconds");
        assertEquals(0, duration.getNano());
    }

    @Test
    public void durationUntil_shouldCalculateLeapDayDuration() {
        // Duration across a leap second day (has 86401 seconds)
        UtcInstant startLeapDay = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, 0);
        UtcInstant endLeapDay = UtcInstant.ofModifiedJulianDay(MJD_1973_01_01, 0);
        
        Duration duration = startLeapDay.durationUntil(endLeapDay);
        
        assertEquals(86401, duration.getSeconds(), "Leap day should have 86401 seconds");
        assertEquals(0, duration.getNano());
    }

    @Test
    public void durationUntil_shouldCalculateNegativeLeapDayDuration() {
        // Reverse direction across leap second day
        UtcInstant endLeapDay = UtcInstant.ofModifiedJulianDay(MJD_1973_01_01, 0);
        UtcInstant startLeapDay = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, 0);
        
        Duration duration = endLeapDay.durationUntil(startLeapDay);
        
        assertEquals(-86401, duration.getSeconds(), "Reverse leap day should have -86401 seconds");
        assertEquals(0, duration.getNano());
    }

    //-----------------------------------------------------------------------
    // Conversion methods
    //-----------------------------------------------------------------------
    
    @Test
    public void toTaiInstant_shouldConvertCorrectly() {
        // Test conversion from UTC to TAI for various dates
        for (int dayOffset = -1000; dayOffset < 1000; dayOffset++) {
            for (int secondOffset = 0; secondOffset < 10; secondOffset++) {
                UtcInstant utcInstant = UtcInstant.ofModifiedJulianDay(
                    36204 + dayOffset, 
                    secondOffset * NANOS_PER_SECOND + 2L
                );
                TaiInstant taiInstant = utcInstant.toTaiInstant();
                
                assertEquals(dayOffset * SECONDS_PER_DAY + secondOffset + 10, taiInstant.getTaiSeconds());
                assertEquals(2, taiInstant.getNano());
            }
        }
    }

    @Test
    public void toTaiInstant_shouldThrowOnMaxValueOverflow() {
        UtcInstant maxUtcInstant = UtcInstant.ofModifiedJulianDay(Long.MAX_VALUE, 0);
        assertThrows(ArithmeticException.class, 
            () -> maxUtcInstant.toTaiInstant(),
            "Should throw on overflow when converting maximum UTC instant to TAI");
    }

    @Test
    public void toInstant_shouldConvertCorrectly() {
        // Test conversion from UTC to Java Instant for various dates
        for (int dayOffset = -1000; dayOffset < 1000; dayOffset++) {
            for (int secondOffset = 0; secondOffset < 10; secondOffset++) {
                // Expected Java Instant (epoch 1970-01-01 = 315532800 seconds from 1980-01-06)
                Instant expectedInstant = Instant.ofEpochSecond(315532800 + dayOffset * SECONDS_PER_DAY + secondOffset)
                                                .plusNanos(2);
                
                UtcInstant utcInstant = UtcInstant.ofModifiedJulianDay(
                    44239 + dayOffset, 
                    secondOffset * NANOS_PER_SECOND + 2
                );
                
                assertEquals(expectedInstant, utcInstant.toInstant());
            }
        }
    }

    //-----------------------------------------------------------------------
    // Comparison methods
    //-----------------------------------------------------------------------
    
    @Test
    public void compareTo_shouldOrderInstancesCorrectly() {
        UtcInstant[] orderedInstants = {
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
        
        verifyComparisons(orderedInstants);
    }

    private void verifyComparisons(UtcInstant[] orderedInstants) {
        for (int i = 0; i < orderedInstants.length; i++) {
            UtcInstant current = orderedInstants[i];
            for (int j = 0; j < orderedInstants.length; j++) {
                UtcInstant other = orderedInstants[j];
                
                if (i < j) {
                    // Current should be less than other
                    assertEquals(-1, current.compareTo(other), 
                        String.format("Expected %s < %s", current, other));
                    assertFalse(current.equals(other));
                    assertTrue(current.isBefore(other));
                    assertFalse(current.isAfter(other));
                } else if (i > j) {
                    // Current should be greater than other
                    assertEquals(1, current.compareTo(other), 
                        String.format("Expected %s > %s", current, other));
                    assertFalse(current.equals(other));
                    assertFalse(current.isBefore(other));
                    assertTrue(current.isAfter(other));
                } else {
                    // Current should equal other
                    assertEquals(0, current.compareTo(other), 
                        String.format("Expected %s == %s", current, other));
                    assertTrue(current.equals(other));
                    assertFalse(current.isBefore(other));
                    assertFalse(current.isAfter(other));
                }
            }
        }
    }

    @Test
    public void compareTo_shouldRejectNullComparison() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(0L, 0);
        assertThrows(NullPointerException.class, 
            () -> instant.compareTo(null),
            "Should reject null comparison");
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void compareTo_shouldRejectNonUtcInstantComparison() {
        Comparable comparableInstant = UtcInstant.ofModifiedJulianDay(0L, 2);
        assertThrows(ClassCastException.class, 
            () -> comparableInstant.compareTo(new Object()),
            "Should reject comparison with non-UtcInstant object");
    }

    //-----------------------------------------------------------------------
    // equals() and hashCode()
    //-----------------------------------------------------------------------
    
    @Test
    public void equals_shouldFollowContract() {
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

    //-----------------------------------------------------------------------
    // toString() and parsing
    //-----------------------------------------------------------------------
    
    public static Object[][] toStringTestData() {
        return new Object[][] {
            // mjd, nanoOfDay, expectedString
            {40587, 0, "1970-01-01T00:00:00Z"},                             // Unix epoch
            {40588, 1, "1970-01-02T00:00:00.000000001Z"},                   // One nanosecond
            {40588, 999, "1970-01-02T00:00:00.000000999Z"},                 // 999 nanoseconds
            {40588, 1000, "1970-01-02T00:00:00.000001Z"},                   // 1 microsecond
            {40588, 999000, "1970-01-02T00:00:00.000999Z"},                 // 999 microseconds
            {40588, 1000000, "1970-01-02T00:00:00.001Z"},                   // 1 millisecond
            {40618, 999999999, "1970-02-01T00:00:00.999999999Z"},           // 999,999,999 nanoseconds
            {40619, 1000000000, "1970-02-02T00:00:01Z"},                    // 1 second
            {40620, 60L * 1000000000L, "1970-02-03T00:01:00Z"},             // 1 minute
            {40621, 60L * 60L * 1000000000L, "1970-02-04T01:00:00Z"},       // 1 hour
            {MJD_1972_12_31_LEAP, 24L * 60L * 60L * 1000000000L - 1000000000L, "1972-12-31T23:59:59Z"}, // Before leap second
            {MJD_1972_12_31_LEAP, NANOS_PER_DAY, "1972-12-31T23:59:60Z"},   // Leap second
            {MJD_1973_01_01, 0, "1973-01-01T00:00:00Z"},                    // After leap second
        };
    }

    @ParameterizedTest
    @MethodSource("toStringTestData")
    public void toString_shouldFormatCorrectly(long mjd, long nanoOfDay, String expectedString) {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
        assertEquals(expectedString, instant.toString());
    }

    @ParameterizedTest
    @MethodSource("toStringTestData")
    public void parse_shouldRoundTripWithToString(long mjd, long nanoOfDay, String stringRepresentation) {
        UtcInstant originalInstant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
        UtcInstant parsedInstant = UtcInstant.parse(stringRepresentation);
        assertEquals(originalInstant, parsedInstant, 
            "Parsing toString() output should recreate original instant");
    }
}