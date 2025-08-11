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

import com.google.common.testing.EqualsTester;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Test {@link UtcInstant}.
 */
@DisplayName("UtcInstant")
public class TestUtcInstant {

    // A known leap day: 1972-12-31
    private static final long MJD_1972_12_31_LEAP = 41682;
    // The day before the known leap day
    private static final long MJD_1972_12_30 = MJD_1972_12_31_LEAP - 1;
    // The day after the known leap day
    private static final long MJD_1973_01_01 = MJD_1972_12_31_LEAP + 1;
    // A later leap day: 1973-12-31
    private static final long MJD_1973_12_31_LEAP = MJD_1972_12_31_LEAP + 365;
    // MJD for 1970-01-01
    private static final long MJD_EPOCH_DAY = 40587L;

    private static final long SECS_PER_DAY = 86400L;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    @Nested
    @DisplayName("API and Serialization")
    class ApiAndSerializationTests {

        @Test
        @DisplayName("implements required interfaces")
        void test_interfaces() {
            assertTrue(Serializable.class.isAssignableFrom(UtcInstant.class));
            assertTrue(Comparable.class.isAssignableFrom(UtcInstant.class));
        }

        @Test
        @DisplayName("is serializable")
        void test_serialization() throws Exception {
            // Arrange
            UtcInstant original = UtcInstant.ofModifiedJulianDay(2, 3);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(original);
            }

            // Act
            Object deserialized;
            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
                deserialized = ois.readObject();
            }

            // Assert
            assertEquals(original, deserialized);
        }
    }

    @Nested
    @DisplayName("Factory method: ofModifiedJulianDay()")
    class OfModifiedJulianDayTests {

        @Test
        @DisplayName("creates an instant with correct values")
        void ofModifiedJulianDay_createsInstant() {
            // Arrange
            long mjd = 12345L;
            long nanoOfDay = 67890L;

            // Act
            UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);

            // Assert
            assertEquals(mjd, instant.getModifiedJulianDay());
            assertEquals(nanoOfDay, instant.getNanoOfDay());
            assertFalse(instant.isLeapSecond());
        }

        @Test
        @DisplayName("creates an instant at the end of a normal day")
        void ofModifiedJulianDay_atEndOfNormalDay() {
            UtcInstant instant = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1);
            assertEquals(MJD_1972_12_31_LEAP, instant.getModifiedJulianDay());
            assertEquals(NANOS_PER_DAY - 1, instant.getNanoOfDay());
            assertFalse(instant.isLeapSecond());
            assertEquals("1972-12-31T23:59:59.999999999Z", instant.toString());
        }

        @Test
        @DisplayName("creates an instant at the start of a leap second")
        void ofModifiedJulianDay_atStartOfLeapSecond() {
            UtcInstant instant = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_DAY);
            assertEquals(MJD_1972_12_31_LEAP, instant.getModifiedJulianDay());
            assertEquals(NANOS_PER_DAY, instant.getNanoOfDay());
            assertTrue(instant.isLeapSecond());
            assertEquals("1972-12-31T23:59:60Z", instant.toString());
        }

        @Test
        @DisplayName("creates an instant at the end of a leap second")
        void ofModifiedJulianDay_atEndOfLeapSecond() {
            UtcInstant instant = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1);
            assertEquals(MJD_1972_12_31_LEAP, instant.getModifiedJulianDay());
            assertEquals(NANOS_PER_LEAP_DAY - 1, instant.getNanoOfDay());
            assertTrue(instant.isLeapSecond());
            assertEquals("1972-12-31T23:59:60.999999999Z", instant.toString());
        }

        @Test
        @DisplayName("throws exception for negative nano-of-day")
        void ofModifiedJulianDay_throwsException_whenNanoOfDayIsNegative() {
            assertThrows(DateTimeException.class, () -> UtcInstant.ofModifiedJulianDay(MJD_1973_01_01, -1));
        }

        @Test
        @DisplayName("throws exception when nano-of-day is too large for a normal day")
        void ofModifiedJulianDay_throwsException_whenNanosTooBigOnNormalDay() {
            assertThrows(DateTimeException.class, () -> UtcInstant.ofModifiedJulianDay(MJD_1973_01_01, NANOS_PER_DAY));
        }

        @Test
        @DisplayName("throws exception when nano-of-day is too large for a leap day")
        void ofModifiedJulianDay_throwsException_whenNanosTooBigOnLeapDay() {
            assertThrows(DateTimeException.class, () -> UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY));
        }
    }

    @Nested
    @DisplayName("Factory method: of(Instant)")
    class OfInstantTests {

        @Test
        @DisplayName("creates an instant from a standard Instant")
        void of_fromInstant_createsCorrectInstant() {
            // Arrange
            Instant instant = Instant.ofEpochSecond(0, 2); // 1970-01-01T00:00:00.000000002Z

            // Act
            UtcInstant utcInstant = UtcInstant.of(instant);

            // Assert
            assertEquals(MJD_EPOCH_DAY, utcInstant.getModifiedJulianDay());
            assertEquals(2, utcInstant.getNanoOfDay());
        }

        @Test
        @DisplayName("throws exception for null Instant")
        void of_fromInstant_throwsException_whenNull() {
            assertThrows(NullPointerException.class, () -> UtcInstant.of((Instant) null));
        }
    }

    @Nested
    @DisplayName("Factory method: of(TaiInstant)")
    class OfTaiInstantTests {

        @ParameterizedTest
        @CsvSource({
            // TAI seconds, TAI nanos, expected MJD, expected nano-of-day
            "10, 2, 36204, 2",                 // Near TAI epoch (1958-01-01)
            "457958411, 500, 41683, 500",      // 1973-01-01, after first leap second (offset 11s)
            "1136073635, 999, 53736, 999"      // 2006-01-01, after many leap seconds (offset 33s)
        })
        @DisplayName("creates an instant from a TAI instant")
        void of_fromTaiInstant_createsCorrectInstant(long taiSeconds, int nano, long expectedMjd, long expectedNod) {
            // Arrange
            TaiInstant taiInstant = TaiInstant.ofTaiSeconds(taiSeconds, nano);
            UtcInstant expected = UtcInstant.ofModifiedJulianDay(expectedMjd, expectedNod);

            // Act
            UtcInstant actual = UtcInstant.of(taiInstant);

            // Assert
            assertEquals(expected, actual);
        }

        @Test
        @DisplayName("throws exception for null TaiInstant")
        void of_fromTaiInstant_throwsException_whenNull() {
            assertThrows(NullPointerException.class, () -> UtcInstant.of((TaiInstant) null));
        }
    }

    @Nested
    @DisplayName("Factory method: parse()")
    class ParseTests {

        @Test
        @DisplayName("parses a valid standard time string")
        void parse_parsesValidStandardTimeString() {
            UtcInstant expected = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_DAY - NANOS_PER_SEC);
            assertEquals(expected, UtcInstant.parse("1972-12-31T23:59:59Z"));
        }

        @Test
        @DisplayName("parses a valid leap second time string")
        void parse_parsesValidLeapSecondString() {
            UtcInstant expected = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_DAY);
            assertEquals(expected, UtcInstant.parse("1972-12-31T23:59:60Z"));
        }

        @ParameterizedTest(name = "for input string: \"{0}\"")
        @MethodSource("provideInvalidParseStrings")
        @DisplayName("throws exception for invalid time strings")
        void parse_throwsException_forInvalidStrings(String invalidString) {
            assertThrows(DateTimeException.class, () -> UtcInstant.parse(invalidString));
        }

        static Stream<Arguments> provideInvalidParseStrings() {
            return Stream.of(
                Arguments.of(""),
                Arguments.of("A"),
                Arguments.of("2012-13-01T00:00:00Z"), // bad month
                Arguments.of("1972-11-11T23:59:60Z")  // leap second on non-leap day
            );
        }

        @Test
        @DisplayName("throws exception for null string")
        void parse_throwsException_whenNull() {
            assertThrows(NullPointerException.class, () -> UtcInstant.parse(null));
        }
    }

    @Nested
    @DisplayName("Modifier method: withModifiedJulianDay()")
    class WithModifiedJulianDayTests {

        static Stream<Arguments> provider_withModifiedJulianDay() {
            // mjd, nanos, newMjd, expectedMjd, expectedNanos
            return Stream.of(
                // Valid changes
                Arguments.of(0L, 12345L, 1L, 1L, 12345L),
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_12_31_LEAP, MJD_1973_12_31_LEAP, NANOS_PER_DAY),
                // Invalid changes (moving a leap second to a non-leap day)
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_30, null, null),
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_01_01, null, null)
            );
        }

        @ParameterizedTest
        @MethodSource("provider_withModifiedJulianDay")
        @DisplayName("modifies the MJD correctly or throws exception")
        void withModifiedJulianDay(long mjd, long nanos, long newMjd, Long expectedMjd, Long expectedNanos) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);

            if (expectedMjd != null) {
                UtcInstant result = initial.withModifiedJulianDay(newMjd);
                assertEquals(expectedMjd.longValue(), result.getModifiedJulianDay());
                assertEquals(expectedNanos.longValue(), result.getNanoOfDay());
            } else {
                assertThrows(DateTimeException.class, () -> initial.withModifiedJulianDay(newMjd));
            }
        }
    }

    @Nested
    @DisplayName("Modifier method: withNanoOfDay()")
    class WithNanoOfDayTests {

        static Stream<Arguments> provider_withNanoOfDay() {
            // mjd, nanos, newNanoOfDay, expectedMjd, expectedNanos
            return Stream.of(
                // Valid changes on normal day
                Arguments.of(MJD_1973_01_01, 12345L, 1L, MJD_1973_01_01, 1L),
                // Valid changes on leap day
                Arguments.of(MJD_1972_12_31_LEAP, 12345L, NANOS_PER_DAY, MJD_1972_12_31_LEAP, NANOS_PER_DAY),
                Arguments.of(MJD_1972_12_31_LEAP, 12345L, NANOS_PER_LEAP_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1),
                // Invalid: negative nano
                Arguments.of(MJD_1973_01_01, 12345L, -1L, null, null),
                // Invalid: leap nano on normal day
                Arguments.of(MJD_1973_01_01, 1L, NANOS_PER_DAY, null, null),
                Arguments.of(MJD_1973_01_01, 1L, NANOS_PER_LEAP_DAY - 1, null, null),
                // Invalid: nano too large for leap day
                Arguments.of(MJD_1972_12_31_LEAP, 1L, NANOS_PER_LEAP_DAY, null, null)
            );
        }

        @ParameterizedTest
        @MethodSource("provider_withNanoOfDay")
        @DisplayName("modifies the nano-of-day correctly or throws exception")
        void withNanoOfDay(long mjd, long nanos, long newNanoOfDay, Long expectedMjd, Long expectedNanos) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);

            if (expectedMjd != null) {
                UtcInstant result = initial.withNanoOfDay(newNanoOfDay);
                assertEquals(expectedMjd.longValue(), result.getModifiedJulianDay());
                assertEquals(expectedNanos.longValue(), result.getNanoOfDay());
            } else {
                assertThrows(DateTimeException.class, () -> initial.withNanoOfDay(newNanoOfDay));
            }
        }
    }

    @Nested
    @DisplayName("Math operations")
    class MathOperationTests {

        static Stream<Arguments> provideDurationsForPlus() {
            // mjd, nanos, plusSeconds, plusNanos, expectedMjd, expectedNanos
            return Stream.of(
                Arguments.of(0L, 0L, 0L, 1, 0L, 1L),
                Arguments.of(0L, 0L, 1L, 0, 0L, 1 * NANOS_PER_SEC),
                Arguments.of(0L, NANOS_PER_DAY - 1, 0L, 1, 1L, 0L),
                Arguments.of(0L, 0L, SECS_PER_DAY, 0, 1L, 0L),
                Arguments.of(0L, 0L, -1L, 0, -1L, (SECS_PER_DAY - 1) * NANOS_PER_SEC)
            );
        }

        @ParameterizedTest
        @MethodSource("provideDurationsForPlus")
        @DisplayName("plus(Duration) adds duration correctly")
        void plus_addsDuration(long mjd, long nanos, long plusSeconds, int plusNanos, long expectedMjd, long expectedNanos) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
            Duration duration = Duration.ofSeconds(plusSeconds, plusNanos);

            UtcInstant result = initial.plus(duration);

            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }

        @Test
        @DisplayName("plus(Duration) throws exception on overflow")
        void plus_throwsException_onOverflow() {
            UtcInstant max = UtcInstant.ofModifiedJulianDay(Long.MAX_VALUE, NANOS_PER_DAY - 1);
            assertThrows(ArithmeticException.class, () -> max.plus(Duration.ofNanos(1)));
        }

        @Test
        @DisplayName("minus(Duration) subtracts duration correctly")
        void minus_subtractsDuration() {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(1, 0);
            Duration duration = Duration.ofNanos(1);

            UtcInstant result = initial.minus(duration);

            assertEquals(0, result.getModifiedJulianDay());
            assertEquals(NANOS_PER_DAY - 1, result.getNanoOfDay());
        }

        @Test
        @DisplayName("minus(Duration) throws exception on underflow")
        void minus_throwsException_onUnderflow() {
            UtcInstant min = UtcInstant.ofModifiedJulianDay(Long.MIN_VALUE, 0);
            assertThrows(ArithmeticException.class, () -> min.minus(Duration.ofNanos(1)));
        }

        @Test
        @DisplayName("durationUntil() calculates correctly over a non-leap day")
        void durationUntil_acrossNormalDay() {
            UtcInstant start = UtcInstant.ofModifiedJulianDay(MJD_1972_12_30, 0);
            UtcInstant end = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, 0);

            Duration duration = start.durationUntil(end);

            assertEquals(Duration.ofSeconds(SECS_PER_DAY), duration);
        }

        @Test
        @DisplayName("durationUntil() calculates correctly over a leap day")
        void durationUntil_acrossLeapDay() {
            UtcInstant start = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, 0);
            UtcInstant end = UtcInstant.ofModifiedJulianDay(MJD_1973_01_01, 0);

            Duration duration = start.durationUntil(end);

            assertEquals(Duration.ofSeconds(SECS_PER_DAY + 1), duration);
        }

        @Test
        @DisplayName("durationUntil() calculates correctly for a negative duration")
        void durationUntil_forNegativeDuration() {
            UtcInstant start = UtcInstant.ofModifiedJulianDay(MJD_1973_01_01, 0);
            UtcInstant end = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, 0);

            Duration duration = start.durationUntil(end);

            assertEquals(Duration.ofSeconds(-(SECS_PER_DAY + 1)), duration);
        }
    }

    @Nested
    @DisplayName("Conversions")
    class ConversionTests {

        @Test
        @DisplayName("toTaiInstant() converts correctly")
        void toTaiInstant_convertsCorrectly() {
            // 1973-01-01 (UTC) is 1973-01-01T00:00:11 (TAI)
            // Leap seconds up to this point: 11s
            UtcInstant utc = UtcInstant.ofModifiedJulianDay(MJD_1973_01_01, 500);
            TaiInstant tai = utc.toTaiInstant();

            long expectedTaiSeconds = (MJD_1973_01_01 - 40587) * SECS_PER_DAY + 11;
            assertEquals(expectedTaiSeconds, tai.getTaiSeconds());
            assertEquals(500, tai.getNano());
        }

        @Test
        @DisplayName("toInstant() converts correctly")
        void toInstant_convertsCorrectly() {
            // 1973-01-01 (UTC) is 1973-01-01T00:00:00 (UTC-SLS)
            // Leap seconds up to this point: 11s. Instant epoch is 1972-01-01.
            // The conversion smears the leap second.
            UtcInstant utc = UtcInstant.ofModifiedJulianDay(MJD_1973_01_01, 500);
            Instant instant = utc.toInstant();

            long epochDay = MJD_1973_01_01 - MJD_EPOCH_DAY;
            Instant expected = Instant.ofEpochSecond(epochDay * SECS_PER_DAY, 500);
            assertEquals(expected, instant);
        }
    }

    @Nested
    @DisplayName("Comparisons")
    class ComparisonTests {
        private final UtcInstant base = UtcInstant.ofModifiedJulianDay(100, 1000);
        private final UtcInstant laterByNano = UtcInstant.ofModifiedJulianDay(100, 2000);
        private final UtcInstant laterByDay = UtcInstant.ofModifiedJulianDay(200, 1000);
        private final UtcInstant equalToBase = UtcInstant.ofModifiedJulianDay(100, 1000);

        @Test
        @DisplayName("compareTo() returns negative for earlier instant")
        void compareTo_returnsNegative_whenThisIsBefore() {
            assertTrue(base.compareTo(laterByNano) < 0);
            assertTrue(base.compareTo(laterByDay) < 0);
        }

        @Test
        @DisplayName("compareTo() returns positive for later instant")
        void compareTo_returnsPositive_whenThisIsAfter() {
            assertTrue(laterByNano.compareTo(base) > 0);
            assertTrue(laterByDay.compareTo(base) > 0);
        }

        @Test
        @DisplayName("compareTo() returns zero for equal instants")
        void compareTo_returnsZero_whenEqual() {
            assertEquals(0, base.compareTo(equalToBase));
        }

        @Test
        @DisplayName("isBefore() is consistent with compareTo()")
        void isBefore_isConsistentWithCompareTo() {
            assertTrue(base.isBefore(laterByNano));
            assertFalse(laterByNano.isBefore(base));
            assertFalse(base.isBefore(equalToBase));
        }

        @Test
        @DisplayName("isAfter() is consistent with compareTo()")
        void isAfter_isConsistentWithCompareTo() {
            assertTrue(laterByNano.isAfter(base));
            assertFalse(base.isAfter(laterByNano));
            assertFalse(base.isAfter(equalToBase));
        }

        @Test
        @DisplayName("compareTo() throws exception for null")
        void compareTo_throwsException_whenNull() {
            assertThrows(NullPointerException.class, () -> base.compareTo(null));
        }

        @Test
        @DisplayName("compareTo() throws exception for wrong type")
        @SuppressWarnings({"unchecked", "rawtypes"})
        void compareTo_throwsException_forWrongType() {
            Comparable c = UtcInstant.ofModifiedJulianDay(0L, 2);
            assertThrows(ClassCastException.class, () -> c.compareTo(new Object()));
        }
    }

    @Nested
    @DisplayName("Object methods")
    class ObjectMethodTests {

        @Test
        @DisplayName("equals() and hashCode() are consistent")
        void test_equals_and_hashCode() {
            new EqualsTester()
                .addEqualityGroup(UtcInstant.ofModifiedJulianDay(5L, 20), UtcInstant.ofModifiedJulianDay(5L, 20))
                .addEqualityGroup(UtcInstant.ofModifiedJulianDay(5L, 30))
                .addEqualityGroup(UtcInstant.ofModifiedJulianDay(6L, 20))
                .testEquals();
        }

        static Stream<Arguments> provider_toString() {
            return Stream.of(
                Arguments.of(MJD_EPOCH_DAY, 0, "1970-01-01T00:00:00Z"),
                Arguments.of(MJD_EPOCH_DAY + 1, 1, "1970-01-02T00:00:00.000000001Z"),
                Arguments.of(MJD_EPOCH_DAY + 1, 1000, "1970-01-02T00:00:00.000001Z"),
                Arguments.of(MJD_EPOCH_DAY + 1, 1000000, "1970-01-02T00:00:00.001Z"),
                Arguments.of(MJD_EPOCH_DAY + 1, NANOS_PER_SEC, "1970-01-02T00:00:01Z"),
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - NANOS_PER_SEC, "1972-12-31T23:59:59Z"),
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, "1972-12-31T23:59:60Z"),
                Arguments.of(MJD_1973_01_01, 0, "1973-01-01T00:00:00Z")
            );
        }

        @ParameterizedTest(name = "{2}")
        @MethodSource("provider_toString")
        @DisplayName("toString() returns correct ISO-8601 format")
        void toString_returnsCorrectFormat(long mjd, long nod, String expected) {
            UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nod);
            assertEquals(expected, instant.toString());
        }

        @ParameterizedTest(name = "{2}")
        @MethodSource("provider_toString")
        @DisplayName("parse() is inverse of toString()")
        void parse_isInverseOfToString(long mjd, long nod, String str) {
            UtcInstant expected = UtcInstant.ofModifiedJulianDay(mjd, nod);
            assertEquals(expected, UtcInstant.parse(str));
        }
    }
}