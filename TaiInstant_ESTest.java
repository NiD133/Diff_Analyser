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

import org.junit.Test;
import static org.junit.Assert.*;

import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;

/**
 * A more understandable test suite for {@link TaiInstant}.
 */
public class TaiInstantUnderstandableTest {

    private static final TaiInstant INSTANT_1S_1N = TaiInstant.ofTaiSeconds(1, 1);
    private static final TaiInstant INSTANT_1S_2N = TaiInstant.ofTaiSeconds(1, 2);
    private static final TaiInstant INSTANT_2S_1N = TaiInstant.ofTaiSeconds(2, 1);

    //-----------------------------------------------------------------------
    // Factory methods: ofTaiSeconds(), of(), parse()
    //-----------------------------------------------------------------------

    @Test
    public void ofTaiSeconds_withPositiveNanoAdjustment_normalizesCorrectly() {
        // Arrange
        long seconds = 10L;
        long nanoAdjustment = 1_000_000_001L; // 1 second and 1 nanosecond

        // Act
        TaiInstant actual = TaiInstant.ofTaiSeconds(seconds, nanoAdjustment);

        // Assert
        assertEquals(11L, actual.getTaiSeconds());
        assertEquals(1, actual.getNano());
    }

    @Test
    public void ofTaiSeconds_withNegativeNanoAdjustment_normalizesCorrectly() {
        // Arrange
        long seconds = 10L;
        long nanoAdjustment = -1L; // -1 nanosecond

        // Act
        TaiInstant actual = TaiInstant.ofTaiSeconds(seconds, nanoAdjustment);

        // Assert
        assertEquals(9L, actual.getTaiSeconds());
        assertEquals(999_999_999, actual.getNano());
    }

    @Test(expected = ArithmeticException.class)
    public void ofTaiSeconds_withOverflow_throwsArithmeticException() {
        TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 1_000_000_000L);
    }

    @Test
    public void ofInstant_and_toInstant_areConsistent() {
        // Arrange: A standard java.time.Instant
        Instant instant = Instant.ofEpochSecond(1_000_000_000L, 123_456_789L);

        // Act: Convert to TaiInstant and back
        TaiInstant taiInstant = TaiInstant.of(instant);
        Instant roundTripInstant = taiInstant.toInstant();

        // Assert: The conversion should be reversible.
        // The exact number of leap seconds is managed by UtcRules.
        // As of 2017, TAI is 37 seconds ahead of UTC, so we expect this difference.
        assertEquals(1_000_000_037L, taiInstant.getTaiSeconds());
        assertEquals(123_456_789, taiInstant.getNano());
        assertEquals(instant, roundTripInstant);
    }

    @Test(expected = NullPointerException.class)
    public void of_nullInstant_throwsNullPointerException() {
        TaiInstant.of((Instant) null);
    }

    @Test
    public void ofUtcInstant_and_toUtcInstant_areConsistent() {
        // Arrange
        UtcInstant utcInstant = UtcInstant.ofModifiedJulianDay(50000, 12345L);

        // Act
        TaiInstant taiInstant = TaiInstant.of(utcInstant);
        UtcInstant roundTripUtcInstant = taiInstant.toUtcInstant();

        // Assert
        assertEquals(utcInstant, roundTripUtcInstant);
    }

    @Test(expected = NullPointerException.class)
    public void of_nullUtcInstant_throwsNullPointerException() {
        TaiInstant.of((UtcInstant) null);
    }

    @Test
    public void parse_validString_returnsCorrectInstant() {
        // Arrange
        String text = "123.123456789s(TAI)";

        // Act
        TaiInstant parsed = TaiInstant.parse(text);

        // Assert
        assertEquals(123L, parsed.getTaiSeconds());
        assertEquals(123456789, parsed.getNano());
    }

    @Test(expected = DateTimeParseException.class)
    public void parse_stringWithIncorrectNanoDigits_throwsDateTimeParseException() {
        TaiInstant.parse("123.123s(TAI)");
    }

    @Test(expected = NullPointerException.class)
    public void parse_null_throwsNullPointerException() {
        TaiInstant.parse(null);
    }

    //-----------------------------------------------------------------------
    // with... methods
    //-----------------------------------------------------------------------

    @Test
    public void withTaiSeconds_returnsNewInstanceWithUpdatedSeconds() {
        // Arrange
        TaiInstant base = TaiInstant.ofTaiSeconds(100L, 50);

        // Act
        TaiInstant updated = base.withTaiSeconds(200L);

        // Assert
        assertEquals(200L, updated.getTaiSeconds());
        assertEquals(50, updated.getNano());
        assertNotSame(base, updated);
    }

    @Test
    public void withNano_returnsNewInstanceWithUpdatedNano() {
        // Arrange
        TaiInstant base = TaiInstant.ofTaiSeconds(100L, 50);

        // Act
        TaiInstant updated = base.withNano(75);

        // Assert
        assertEquals(100L, updated.getTaiSeconds());
        assertEquals(75, updated.getNano());
        assertNotSame(base, updated);
    }

    @Test(expected = IllegalArgumentException.class)
    public void withNano_valueTooLarge_throwsIllegalArgumentException() {
        TaiInstant.ofTaiSeconds(0, 0).withNano(1_000_000_000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void withNano_valueTooSmall_throwsIllegalArgumentException() {
        TaiInstant.ofTaiSeconds(0, 0).withNano(-1);
    }

    //-----------------------------------------------------------------------
    // plus() and minus()
    //-----------------------------------------------------------------------

    @Test
    public void plus_positiveDuration_returnsLaterInstant() {
        // Arrange
        TaiInstant start = TaiInstant.ofTaiSeconds(100L, 500_000_000);
        Duration duration = Duration.ofSeconds(10, 200_000_000);

        // Act
        TaiInstant result = start.plus(duration);

        // Assert
        assertEquals(110L, result.getTaiSeconds());
        assertEquals(700_000_000, result.getNano());
    }

    @Test
    public void plus_zeroDuration_returnsSameInstance() {
        // Arrange
        TaiInstant start = TaiInstant.ofTaiSeconds(100L, 50);

        // Act
        TaiInstant result = start.plus(Duration.ZERO);

        // Assert
        assertSame(start, result);
    }

    @Test(expected = NullPointerException.class)
    public void plus_nullDuration_throwsNullPointerException() {
        INSTANT_1S_1N.plus(null);
    }

    @Test
    public void minus_positiveDuration_returnsEarlierInstant() {
        // Arrange
        TaiInstant start = TaiInstant.ofTaiSeconds(100L, 500_000_000);
        Duration duration = Duration.ofSeconds(10, 200_000_000);

        // Act
        TaiInstant result = start.minus(duration);

        // Assert
        assertEquals(90L, result.getTaiSeconds());
        assertEquals(300_000_000, result.getNano());
    }

    @Test
    public void minus_zeroDuration_returnsSameInstance() {
        // Arrange
        TaiInstant start = TaiInstant.ofTaiSeconds(100L, 50);

        // Act
        TaiInstant result = start.minus(Duration.ZERO);

        // Assert
        assertSame(start, result);
    }

    @Test(expected = NullPointerException.class)
    public void minus_nullDuration_throwsNullPointerException() {
        INSTANT_1S_1N.minus(null);
    }

    //-----------------------------------------------------------------------
    // durationUntil()
    //-----------------------------------------------------------------------

    @Test
    public void durationUntil_toLaterInstant_returnsPositiveDuration() {
        // Arrange
        TaiInstant start = TaiInstant.ofTaiSeconds(100L, 100);
        TaiInstant end = TaiInstant.ofTaiSeconds(105L, 500);

        // Act
        Duration duration = start.durationUntil(end);

        // Assert
        assertEquals(Duration.ofSeconds(5, 400), duration);
    }

    @Test
    public void durationUntil_toEarlierInstant_returnsNegativeDuration() {
        // Arrange
        TaiInstant start = TaiInstant.ofTaiSeconds(105L, 500);
        TaiInstant end = TaiInstant.ofTaiSeconds(100L, 100);

        // Act
        Duration duration = start.durationUntil(end);

        // Assert
        assertEquals(Duration.ofSeconds(-5, -400), duration);
    }

    @Test
    public void durationUntil_toSameInstant_returnsZero() {
        // Arrange
        TaiInstant instant = TaiInstant.ofTaiSeconds(100L, 100);

        // Act
        Duration duration = instant.durationUntil(instant);

        // Assert
        assertEquals(Duration.ZERO, duration);
    }

    //-----------------------------------------------------------------------
    // Comparison: compareTo(), isAfter(), isBefore()
    //-----------------------------------------------------------------------

    @Test
    public void compareTo_withLaterInstant_returnsNegative() {
        assertTrue(INSTANT_1S_1N.compareTo(INSTANT_1S_2N) < 0);
        assertTrue(INSTANT_1S_1N.compareTo(INSTANT_2S_1N) < 0);
    }

    @Test
    public void compareTo_withEarlierInstant_returnsPositive() {
        assertTrue(INSTANT_1S_2N.compareTo(INSTANT_1S_1N) > 0);
        assertTrue(INSTANT_2S_1N.compareTo(INSTANT_1S_1N) > 0);
    }

    @Test
    public void compareTo_withEqualInstant_returnsZero() {
        TaiInstant sameAsConstant = TaiInstant.ofTaiSeconds(1, 1);
        assertEquals(0, INSTANT_1S_1N.compareTo(sameAsConstant));
    }

    @Test
    public void isAfter_whenLater_returnsTrue() {
        assertTrue(INSTANT_1S_2N.isAfter(INSTANT_1S_1N));
    }

    @Test
    public void isAfter_whenEarlierOrEqual_returnsFalse() {
        assertFalse(INSTANT_1S_1N.isAfter(INSTANT_1S_2N));
        assertFalse(INSTANT_1S_1N.isAfter(INSTANT_1S_1N));
    }

    @Test
    public void isBefore_whenEarlier_returnsTrue() {
        assertTrue(INSTANT_1S_1N.isBefore(INSTANT_1S_2N));
    }

    @Test
    public void isBefore_whenLaterOrEqual_returnsFalse() {
        assertFalse(INSTANT_1S_2N.isBefore(INSTANT_1S_1N));
        assertFalse(INSTANT_1S_1N.isBefore(INSTANT_1S_1N));
    }

    //-----------------------------------------------------------------------
    // equals() and hashCode()
    //-----------------------------------------------------------------------

    @Test
    public void equals_withSameInstance_returnsTrue() {
        assertTrue(INSTANT_1S_1N.equals(INSTANT_1S_1N));
    }

    @Test
    public void equals_withEqualValues_returnsTrue() {
        TaiInstant sameAsConstant = TaiInstant.ofTaiSeconds(1, 1);
        assertTrue(INSTANT_1S_1N.equals(sameAsConstant));
    }

    @Test
    public void equals_withDifferentSeconds_returnsFalse() {
        assertFalse(INSTANT_1S_1N.equals(INSTANT_2S_1N));
    }

    @Test
    public void equals_withDifferentNanos_returnsFalse() {
        assertFalse(INSTANT_1S_1N.equals(INSTANT_1S_2N));
    }

    @Test
    public void equals_withDifferentTypeOrNull_returnsFalse() {
        assertFalse(INSTANT_1S_1N.equals("not a TaiInstant"));
        assertFalse(INSTANT_1S_1N.equals(null));
    }

    @Test
    public void hashCode_isConsistentForEqualObjects() {
        // Arrange
        TaiInstant instant1 = TaiInstant.ofTaiSeconds(123L, 456);
        TaiInstant instant2 = TaiInstant.ofTaiSeconds(123L, 456);

        // Assert
        assertEquals(instant1.hashCode(), instant2.hashCode());
    }

    //-----------------------------------------------------------------------
    // toString()
    //-----------------------------------------------------------------------

    @Test
    public void toString_returnsCorrectFormat() {
        // Arrange
        TaiInstant instant = TaiInstant.ofTaiSeconds(1L, 123456789);

        // Act
        String str = instant.toString();

        // Assert
        assertEquals("1.123456789s(TAI)", str);
    }

    @Test
    public void toString_withZeroNanos_padsCorrectly() {
        // Arrange
        TaiInstant instant = TaiInstant.ofTaiSeconds(1L, 0);

        // Act
        String str = instant.toString();

        // Assert
        assertEquals("1.000000000s(TAI)", str);
    }
}