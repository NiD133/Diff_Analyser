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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.testing.EqualsTester;

/**
 * Test TaiInstant - TAI (International Atomic Time) instant representation.
 * 
 * TAI is a continuous time scale without leap seconds, making it ideal for precise time calculations.
 * The epoch is 1958-01-01T00:00:00(TAI).
 */
public class TestTaiInstant {

    // Constants for better readability
    private static final long TAI_EPOCH_OFFSET_FROM_UNIX = (40587L - 36204) * 24 * 60 * 60 + 10; // TAI seconds from Unix epoch
    private static final long MODIFIED_JULIAN_DAY_TAI_EPOCH = 36204L; // MJD for TAI epoch (1958-01-01)
    private static final int NANOS_PER_SECOND = 1_000_000_000;
    private static final long SECONDS_PER_DAY = 24 * 60 * 60;

    //-----------------------------------------------------------------------
    // Basic interface compliance tests
    //-----------------------------------------------------------------------
    
    @Test
    public void shouldImplementRequiredInterfaces() {
        assertTrue(Serializable.class.isAssignableFrom(TaiInstant.class), 
                  "TaiInstant should be serializable");
        assertTrue(Comparable.class.isAssignableFrom(TaiInstant.class), 
                  "TaiInstant should be comparable");
    }

    //-----------------------------------------------------------------------
    // Serialization tests
    //-----------------------------------------------------------------------
    
    @Test
    public void shouldSerializeAndDeserializeCorrectly() throws Exception {
        // Given: A TaiInstant with specific seconds and nanoseconds
        TaiInstant originalInstant = TaiInstant.ofTaiSeconds(2, 3);
        
        // When: Serializing and deserializing
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(originalInstant);
        }
        
        TaiInstant deserializedInstant;
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
            deserializedInstant = (TaiInstant) ois.readObject();
        }
        
        // Then: The deserialized object should equal the original
        assertEquals(originalInstant, deserializedInstant);
    }

    //-----------------------------------------------------------------------
    // Factory method: ofTaiSeconds(long, long)
    //-----------------------------------------------------------------------
    
    @Test
    public void shouldCreateTaiInstantWithPositiveNanoseconds() {
        // Test with small range of seconds and positive nanoseconds
        for (long seconds = -2; seconds <= 2; seconds++) {
            for (int nanos = 0; nanos < 10; nanos++) {
                TaiInstant instant = TaiInstant.ofTaiSeconds(seconds, nanos);
                
                assertEquals(seconds, instant.getTaiSeconds(), 
                           "Seconds should match input for seconds=" + seconds + ", nanos=" + nanos);
                assertEquals(nanos, instant.getNano(), 
                           "Nanoseconds should match input for seconds=" + seconds + ", nanos=" + nanos);
            }
        }
    }

    @Test
    public void shouldNormalizeNegativeNanoseconds() {
        // Test that negative nanoseconds are properly normalized
        for (long seconds = -2; seconds <= 2; seconds++) {
            for (int negativeNanos = -10; negativeNanos < 0; negativeNanos++) {
                TaiInstant instant = TaiInstant.ofTaiSeconds(seconds, negativeNanos);
                
                // Negative nanos should cause seconds to be decremented and nanos to be positive
                assertEquals(seconds - 1, instant.getTaiSeconds(), 
                           "Negative nanos should decrement seconds for seconds=" + seconds + ", nanos=" + negativeNanos);
                assertEquals(negativeNanos + NANOS_PER_SECOND, instant.getNano(), 
                           "Negative nanos should be normalized to positive for seconds=" + seconds + ", nanos=" + negativeNanos);
            }
        }
    }

    @Test
    public void shouldHandleLargePositiveNanoseconds() {
        // Test with nanoseconds near the upper boundary
        for (long seconds = -2; seconds <= 2; seconds++) {
            for (int nanos = 999_999_990; nanos < NANOS_PER_SECOND; nanos++) {
                TaiInstant instant = TaiInstant.ofTaiSeconds(seconds, nanos);
                
                assertEquals(seconds, instant.getTaiSeconds(), 
                           "Large positive nanos should not affect seconds for seconds=" + seconds + ", nanos=" + nanos);
                assertEquals(nanos, instant.getNano(), 
                           "Large positive nanos should be preserved for seconds=" + seconds + ", nanos=" + nanos);
            }
        }
    }

    @Test
    public void shouldNormalizeSpecificNegativeNanosecondCase() {
        // Given: 2 seconds with -1 nanosecond
        TaiInstant instant = TaiInstant.ofTaiSeconds(2L, -1);
        
        // Then: Should normalize to 1 second and 999,999,999 nanoseconds
        assertEquals(1, instant.getTaiSeconds(), "Negative nano should decrement seconds");
        assertEquals(999_999_999, instant.getNano(), "Negative nano should be normalized");
    }

    @Test
    public void shouldThrowArithmeticExceptionForOverflow() {
        // When/Then: Creating instant with values that would cause overflow should throw exception
        assertThrows(ArithmeticException.class, 
                    () -> TaiInstant.ofTaiSeconds(Long.MAX_VALUE, NANOS_PER_SECOND),
                    "Should throw ArithmeticException when nanoseconds would cause overflow");
    }

    //-----------------------------------------------------------------------
    // Factory method: of(Instant)
    //-----------------------------------------------------------------------
    
    @Test
    public void shouldCreateTaiInstantFromJavaInstant() {
        // Given: A Java Instant at Unix epoch with 2 nanoseconds
        Instant javaInstant = Instant.ofEpochSecond(0, 2);
        
        // When: Converting to TAI
        TaiInstant taiInstant = TaiInstant.of(javaInstant);
        
        // Then: Should account for the offset between Unix epoch and TAI epoch
        assertEquals(TAI_EPOCH_OFFSET_FROM_UNIX, taiInstant.getTaiSeconds(), 
                    "TAI seconds should account for epoch difference");
        assertEquals(2, taiInstant.getNano(), 
                    "Nanoseconds should be preserved");
    }

    @Test
    public void shouldThrowNullPointerExceptionForNullInstant() {
        assertThrows(NullPointerException.class, 
                    () -> TaiInstant.of((Instant) null),
                    "Should throw NPE for null Instant");
    }

    //-----------------------------------------------------------------------
    // Factory method: of(UtcInstant)
    //-----------------------------------------------------------------------
    
    @Test
    public void shouldCreateTaiInstantFromUtcInstant() {
        // Test conversion from UTC to TAI across a range of days and times
        for (int dayOffset = -1000; dayOffset < 1000; dayOffset++) {
            for (int hourOffset = 0; hourOffset < 10; hourOffset++) {
                // Given: A UTC instant at a specific modified Julian day
                long mjd = MODIFIED_JULIAN_DAY_TAI_EPOCH + dayOffset;
                long nanoOfDay = hourOffset * NANOS_PER_SECOND + 2L;
                UtcInstant utcInstant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
                
                // When: Converting to TAI
                TaiInstant taiInstant = TaiInstant.of(utcInstant);
                
                // Then: Should correctly calculate TAI seconds (including 10-second offset)
                long expectedTaiSeconds = dayOffset * SECONDS_PER_DAY + hourOffset + 10;
                assertEquals(expectedTaiSeconds, taiInstant.getTaiSeconds(), 
                           "TAI seconds calculation incorrect for day offset " + dayOffset + ", hour " + hourOffset);
                assertEquals(2, taiInstant.getNano(), 
                           "Nanoseconds should be preserved");
            }
        }
    }

    @Test
    public void shouldThrowNullPointerExceptionForNullUtcInstant() {
        assertThrows(NullPointerException.class, 
                    () -> TaiInstant.of((UtcInstant) null),
                    "Should throw NPE for null UtcInstant");
    }

    //-----------------------------------------------------------------------
    // Factory method: parse(CharSequence)
    //-----------------------------------------------------------------------
    
    @Test
    public void shouldParseValidTaiInstantStrings() {
        // Test parsing across a range of seconds and nanosecond values
        for (int seconds = -1000; seconds < 1000; seconds++) {
            for (int nanos = 900_000_000; nanos < 990_000_000; nanos += 10_000_000) {
                // Given: A properly formatted TAI string
                String taiString = seconds + "." + nanos + "s(TAI)";
                
                // When: Parsing the string
                TaiInstant parsed = TaiInstant.parse(taiString);
                
                // Then: Should correctly extract seconds and nanoseconds
                assertEquals(seconds, parsed.getTaiSeconds(), 
                           "Parsed seconds incorrect for: " + taiString);
                assertEquals(nanos, parsed.getNano(), 
                           "Parsed nanoseconds incorrect for: " + taiString);
            }
        }
    }

    public static Object[][] invalidTaiStringFormats() {
        return new Object[][] {
            {"A.123456789s(TAI)", "Non-numeric seconds"},
            {"123.12345678As(TAI)", "Non-numeric nanoseconds"},
            {"123.123456789", "Missing s(TAI) suffix"},
            {"123.123456789s", "Missing (TAI) suffix"},
            {"+123.123456789s(TAI)", "Explicit positive sign not allowed"},
            {"-123.123s(TAI)", "Insufficient nanosecond digits"},
        };
    }

    @ParameterizedTest(name = "Should reject invalid format: {1}")
    @MethodSource("invalidTaiStringFormats")
    public void shouldRejectInvalidTaiStringFormats(String invalidString, String description) {
        assertThrows(DateTimeParseException.class, 
                    () -> TaiInstant.parse(invalidString),
                    "Should reject invalid format: " + description);
    }

    @Test
    public void shouldThrowNullPointerExceptionForNullString() {
        assertThrows(NullPointerException.class, 
                    () -> TaiInstant.parse((String) null),
                    "Should throw NPE for null string");
    }

    //-----------------------------------------------------------------------
    // Method: withTaiSeconds()
    //-----------------------------------------------------------------------
    
    public static Object[][] taiSecondsReplacementCases() {
        return new Object[][] {
            // originalSeconds, originalNanos, newSeconds, expectedSeconds, expectedNanos, description
            {0L, 12345L, 1L, 1L, 12345L, "Replace zero seconds with positive"},
            {0L, 12345L, -1L, -1L, 12345L, "Replace zero seconds with negative"},
            {7L, 12345L, 2L, 2L, 12345L, "Replace positive seconds with smaller positive"},
            {7L, 12345L, -2L, -2L, 12345L, "Replace positive seconds with negative"},
            {-99L, 12345L, 3L, 3L, 12345L, "Replace negative seconds with positive"},
            {-99L, 12345L, -3L, -3L, 12345L, "Replace negative seconds with different negative"},
        };
    }

    @ParameterizedTest(name = "{5}")
    @MethodSource("taiSecondsReplacementCases")
    public void shouldReplaceSecondsWhilePreservingNanoseconds(
            long originalSeconds, long originalNanos, long newSeconds, 
            long expectedSeconds, long expectedNanos, String description) {
        
        // Given: A TAI instant with specific seconds and nanoseconds
        TaiInstant original = TaiInstant.ofTaiSeconds(originalSeconds, originalNanos);
        
        // When: Replacing the seconds
        TaiInstant modified = original.withTaiSeconds(newSeconds);
        
        // Then: Seconds should be updated, nanoseconds preserved
        assertEquals(expectedSeconds, modified.getTaiSeconds(), "Seconds should be updated");
        assertEquals(expectedNanos, modified.getNano(), "Nanoseconds should be preserved");
    }

    //-----------------------------------------------------------------------
    // Method: withNano()
    //-----------------------------------------------------------------------
    
    public static Object[][] nanoReplacementCases() {
        return new Object[][] {
            // originalSeconds, originalNanos, newNanos, expectedSeconds, expectedNanos, shouldSucceed, description
            {0L, 12345L, 1, 0L, 1L, true, "Replace nanoseconds with small value"},
            {7L, 12345L, 2, 7L, 2L, true, "Replace nanoseconds with different small value"},
            {-99L, 12345L, 3, -99L, 3L, true, "Replace nanoseconds on negative seconds"},
            {-99L, 12345L, 999_999_999, -99L, 999_999_999L, true, "Replace with maximum valid nanoseconds"},
            {-99L, 12345L, -1, null, null, false, "Negative nanoseconds should be rejected"},
            {-99L, 12345L, 1_000_000_000, null, null, false, "Nanoseconds >= 1 billion should be rejected"},
        };
    }

    @ParameterizedTest(name = "{6}")
    @MethodSource("nanoReplacementCases")
    public void shouldReplaceNanosecondsWithValidation(
            long originalSeconds, long originalNanos, int newNanos, 
            Long expectedSeconds, Long expectedNanos, boolean shouldSucceed, String description) {
        
        // Given: A TAI instant with specific seconds and nanoseconds
        TaiInstant original = TaiInstant.ofTaiSeconds(originalSeconds, originalNanos);
        
        if (shouldSucceed) {
            // When: Replacing with valid nanoseconds
            TaiInstant modified = original.withNano(newNanos);
            
            // Then: Nanoseconds should be updated, seconds preserved
            assertEquals(expectedSeconds.longValue(), modified.getTaiSeconds(), "Seconds should be preserved");
            assertEquals(expectedNanos.longValue(), modified.getNano(), "Nanoseconds should be updated");
        } else {
            // When/Then: Invalid nanoseconds should throw exception
            assertThrows(IllegalArgumentException.class, 
                        () -> original.withNano(newNanos),
                        "Should reject invalid nanoseconds: " + newNanos);
        }
    }

    //-----------------------------------------------------------------------
    // Method: plus(Duration) - Key test cases
    //-----------------------------------------------------------------------
    
    @Test
    public void shouldAddSimpleDurations() {
        // Given: A TAI instant at a specific time
        TaiInstant baseInstant = TaiInstant.ofTaiSeconds(10, 500_000_000);
        
        // When: Adding 5 seconds
        TaiInstant result = baseInstant.plus(Duration.ofSeconds(5));
        
        // Then: Should correctly add the duration
        assertEquals(15, result.getTaiSeconds(), "Should add seconds correctly");
        assertEquals(500_000_000, result.getNano(), "Should preserve nanoseconds");
    }

    @Test
    public void shouldAddDurationWithNanosecondCarry() {
        // Given: A TAI instant with high nanoseconds
        TaiInstant baseInstant = TaiInstant.ofTaiSeconds(10, 800_000_000);
        
        // When: Adding nanoseconds that cause carry to next second
        TaiInstant result = baseInstant.plus(Duration.ofNanos(300_000_000));
        
        // Then: Should handle carry correctly
        assertEquals(11, result.getTaiSeconds(), "Should carry to next second");
        assertEquals(100_000_000, result.getNano(), "Should have correct remaining nanoseconds");
    }

    @Test
    public void shouldHandleNegativeDurations() {
        // Given: A TAI instant
        TaiInstant baseInstant = TaiInstant.ofTaiSeconds(10, 200_000_000);
        
        // When: Adding negative duration (subtraction)
        TaiInstant result = baseInstant.plus(Duration.ofSeconds(-3, -100_000_000));
        
        // Then: Should correctly subtract
        assertEquals(7, result.getTaiSeconds(), "Should subtract seconds");
        assertEquals(100_000_000, result.getNano(), "Should handle nanosecond borrow");
    }

    @Test
    public void shouldThrowArithmeticExceptionOnPositiveOverflow() {
        // Given: TAI instant at maximum value
        TaiInstant maxInstant = TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 999_999_999);
        
        // When/Then: Adding any positive duration should overflow
        assertThrows(ArithmeticException.class, 
                    () -> maxInstant.plus(Duration.ofNanos(1)),
                    "Should throw ArithmeticException on positive overflow");
    }

    @Test
    public void shouldThrowArithmeticExceptionOnNegativeOverflow() {
        // Given: TAI instant at minimum value
        TaiInstant minInstant = TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0);
        
        // When/Then: Subtracting should cause underflow
        assertThrows(ArithmeticException.class, 
                    () -> minInstant.plus(Duration.ofSeconds(-1, 999_999_999)),
                    "Should throw ArithmeticException on negative overflow");
    }

    //-----------------------------------------------------------------------
    // Method: minus(Duration) - Key test cases
    //-----------------------------------------------------------------------
    
    @Test
    public void shouldSubtractSimpleDurations() {
        // Given: A TAI instant
        TaiInstant baseInstant = TaiInstant.ofTaiSeconds(20, 600_000_000);
        
        // When: Subtracting 5 seconds
        TaiInstant result = baseInstant.minus(Duration.ofSeconds(5));
        
        // Then: Should correctly subtract
        assertEquals(15, result.getTaiSeconds(), "Should subtract seconds correctly");
        assertEquals(600_000_000, result.getNano(), "Should preserve nanoseconds");
    }

    @Test
    public void shouldSubtractWithNanosecondBorrow() {
        // Given: A TAI instant with low nanoseconds
        TaiInstant baseInstant = TaiInstant.ofTaiSeconds(10, 200_000_000);
        
        // When: Subtracting more nanoseconds than available
        TaiInstant result = baseInstant.minus(Duration.ofNanos(300_000_000));
        
        // Then: Should borrow from seconds
        assertEquals(9, result.getTaiSeconds(), "Should borrow from seconds");
        assertEquals(900_000_000, result.getNano(), "Should have correct remaining nanoseconds");
    }

    @Test
    public void shouldThrowArithmeticExceptionOnMinusOverflow() {
        // Given: TAI instant at minimum value
        TaiInstant minInstant = TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0);
        
        // When/Then: Subtracting positive duration should underflow
        assertThrows(ArithmeticException.class, 
                    () -> minInstant.minus(Duration.ofNanos(1)),
                    "Should throw ArithmeticException on underflow");
    }

    //-----------------------------------------------------------------------
    // Method: durationUntil()
    //-----------------------------------------------------------------------
    
    @Test
    public void shouldCalculateDurationBetweenInstants() {
        // Given: Two TAI instants 15 seconds apart
        TaiInstant earlier = TaiInstant.ofTaiSeconds(10, 0);
        TaiInstant later = TaiInstant.ofTaiSeconds(25, 0);
        
        // When: Calculating duration between them
        Duration duration = earlier.durationUntil(later);
        
        // Then: Should return correct duration
        assertEquals(15, duration.getSeconds(), "Duration should be 15 seconds");
        assertEquals(0, duration.getNano(), "Duration should have no nanoseconds");
    }

    @Test
    public void shouldCalculateNanosecondDuration() {
        // Given: Two TAI instants with small nanosecond difference
        TaiInstant earlier = TaiInstant.ofTaiSeconds(4, 5);
        TaiInstant later = TaiInstant.ofTaiSeconds(4, 7);
        
        // When: Calculating duration
        Duration duration = earlier.durationUntil(later);
        
        // Then: Should return nanosecond precision
        assertEquals(0, duration.getSeconds(), "Should have no full seconds");
        assertEquals(2, duration.getNano(), "Should have 2 nanoseconds difference");
    }

    @Test
    public void shouldCalculateNegativeDuration() {
        // Given: Two TAI instants where first is later than second
        TaiInstant later = TaiInstant.ofTaiSeconds(4, 9);
        TaiInstant earlier = TaiInstant.ofTaiSeconds(4, 7);
        
        // When: Calculating duration from later to earlier
        Duration duration = later.durationUntil(earlier);
        
        // Then: Should return negative duration
        assertEquals(-1, duration.getSeconds(), "Should have negative seconds");
        assertEquals(999_999_998, duration.getNano(), "Should have correct nanosecond adjustment");
    }

    //-----------------------------------------------------------------------
    // Conversion methods
    //-----------------------------------------------------------------------
    
    @Test
    public void shouldConvertToUtcInstant() {
        // Test conversion across multiple days and times
        for (int dayOffset = -1000; dayOffset < 1000; dayOffset++) {
            for (int hourOffset = 0; hourOffset < 10; hourOffset++) {
                // Given: A TAI instant
                long taiSeconds = dayOffset * SECONDS_PER_DAY + hourOffset + 10;
                TaiInstant taiInstant = TaiInstant.ofTaiSeconds(taiSeconds, 2);
                
                // When: Converting to UTC
                UtcInstant utcInstant = taiInstant.toUtcInstant();
                
                // Then: Should produce expected UTC instant
                UtcInstant expectedUtc = UtcInstant.ofModifiedJulianDay(
                    MODIFIED_JULIAN_DAY_TAI_EPOCH + dayOffset, 
                    hourOffset * NANOS_PER_SECOND + 2L);
                assertEquals(expectedUtc, utcInstant, 
                           "UTC conversion incorrect for day offset " + dayOffset + ", hour " + hourOffset);
            }
        }
    }

    @Test
    public void shouldConvertToJavaInstant() {
        // Test conversion to standard Java Instant
        for (int dayOffset = -1000; dayOffset < 1000; dayOffset++) {
            for (int hourOffset = 0; hourOffset < 10; hourOffset++) {
                // Given: A TAI instant
                long taiSeconds = dayOffset * SECONDS_PER_DAY + hourOffset + 10;
                TaiInstant taiInstant = TaiInstant.ofTaiSeconds(taiSeconds, 2);
                
                // When: Converting to Java Instant
                Instant javaInstant = taiInstant.toInstant();
                
                // Then: Should account for epoch differences
                long expectedEpochSeconds = -378_691_200L + dayOffset * SECONDS_PER_DAY + hourOffset;
                Instant expectedInstant = Instant.ofEpochSecond(expectedEpochSeconds).plusNanos(2);
                assertEquals(expectedInstant, javaInstant, 
                           "Java Instant conversion incorrect for day offset " + dayOffset + ", hour " + hourOffset);
            }
        }
    }

    //-----------------------------------------------------------------------
    // Comparison methods
    //-----------------------------------------------------------------------
    
    @Test
    public void shouldCompareInstantsCorrectly() {
        // Given: A series of TAI instants in chronological order
        TaiInstant[] chronologicalInstants = {
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
        
        // When/Then: Each instant should compare correctly with all others
        verifyChronologicalOrdering(chronologicalInstants);
    }

    private void verifyChronologicalOrdering(TaiInstant[] instants) {
        for (int i = 0; i < instants.length; i++) {
            TaiInstant current = instants[i];
            for (int j = 0; j < instants.length; j++) {
                TaiInstant other = instants[j];
                
                if (i < j) {
                    // Current should be before other
                    assertTrue(current.compareTo(other) < 0, 
                              current + " should be less than " + other);
                    assertFalse(current.equals(other), 
                               current + " should not equal " + other);
                    assertTrue(current.isBefore(other), 
                              current + " should be before " + other);
                    assertFalse(current.isAfter(other), 
                               current + " should not be after " + other);
                } else if (i > j) {
                    // Current should be after other
                    assertTrue(current.compareTo(other) > 0, 
                              current + " should be greater than " + other);
                    assertFalse(current.equals(other), 
                               current + " should not equal " + other);
                    assertFalse(current.isBefore(other), 
                               current + " should not be before " + other);
                    assertTrue(current.isAfter(other), 
                              current + " should be after " + other);
                } else {
                    // Current should equal other
                    assertEquals(0, current.compareTo(other), 
                                current + " should equal " + other);
                    assertTrue(current.equals(other), 
                              current + " should equal " + other);
                    assertFalse(current.isBefore(other), 
                               current + " should not be before itself");
                    assertFalse(current.isAfter(other), 
                               current + " should not be after itself");
                }
            }
        }
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenComparingToNull() {
        TaiInstant instant = TaiInstant.ofTaiSeconds(0L, 0);
        assertThrows(NullPointerException.class, 
                    () -> instant.compareTo(null),
                    "Should throw NPE when comparing to null");
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void shouldThrowClassCastExceptionWhenComparingToWrongType() {
        Comparable instant = TaiInstant.ofTaiSeconds(0L, 2);
        assertThrows(ClassCastException.class, 
                    () -> instant.compareTo(new Object()),
                    "Should throw ClassCastException when comparing to wrong type");
    }

    //-----------------------------------------------------------------------
    // equals() and hashCode()
    //-----------------------------------------------------------------------
    
    @Test
    public void shouldImplementEqualsAndHashCodeCorrectly() {
        new EqualsTester()
            .addEqualityGroup(
                TaiInstant.ofTaiSeconds(5L, 20), 
                TaiInstant.ofTaiSeconds(5L, 20))
            .addEqualityGroup(
                TaiInstant.ofTaiSeconds(5L, 30), 
                TaiInstant.ofTaiSeconds(5L, 30))
            .addEqualityGroup(
                TaiInstant.ofTaiSeconds(6L, 20), 
                TaiInstant.ofTaiSeconds(6L, 20))
            .testEquals();
    }

    //-----------------------------------------------------------------------
    // toString()
    //-----------------------------------------------------------------------
    
    @Test
    public void shouldFormatPositiveInstantCorrectly() {
        TaiInstant instant = TaiInstant.ofTaiSeconds(123L, 123_456_789);
        assertEquals("123.123456789s(TAI)", instant.toString(), 
                    "Should format positive instant correctly");
    }

    @Test
    public void shouldFormatNegativeInstantCorrectly() {
        TaiInstant instant = TaiInstant.ofTaiSeconds(-123L, 123_456_789);
        assertEquals("-123.123456789s(TAI)", instant.toString(), 
                    "Should format negative instant correctly");
    }

    @Test
    public void shouldFormatZeroSecondsWithLeadingZeros() {
        TaiInstant instant = TaiInstant.ofTaiSeconds(0L, 567);
        assertEquals("0.000000567s(TAI)", instant.toString(), 
                    "Should format with leading zeros for nanoseconds");
    }
}