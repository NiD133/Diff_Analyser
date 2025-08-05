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

import static org.junit.jupiter.api.Assertions.assertAll;
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
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Test TaiInstant.
 */
public class TaiInstantTest {

    private static final long NANOS_PER_SECOND = 1_000_000_000L;

    @Test
    void isSerializable() {
        assertTrue(Serializable.class.isAssignableFrom(TaiInstant.class));
    }

    @Test
    void isComparable() {
        assertTrue(Comparable.class.isAssignableFrom(TaiInstant.class));
    }

    @Test
    void serialization_deserialization_works() throws Exception {
        TaiInstant original = TaiInstant.ofTaiSeconds(2, 3);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(original);
        }
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
            assertEquals(original, ois.readObject());
        }
    }

    @Nested
    @DisplayName("Factory methods")
    class Factory {

        @Nested
        @DisplayName("ofTaiSeconds")
        class OfTaiSeconds {
            @Test
            void withPositiveNanos_createsInstance() {
                TaiInstant test = TaiInstant.ofTaiSeconds(5, 10);
                assertAll(
                    () -> assertEquals(5, test.getTaiSeconds()),
                    () -> assertEquals(10, test.getNano())
                );
            }

            @Test
            void withNegativeNanos_normalizesByBorrowingFromSeconds() {
                TaiInstant test = TaiInstant.ofTaiSeconds(2L, -1);
                assertAll(
                    () -> assertEquals(1, test.getTaiSeconds()),
                    () -> assertEquals(NANOS_PER_SECOND - 1, test.getNano())
                );
            }

            @Test
            void withNanosGreaterThanOneSecond_normalizesByCarryingToSeconds() {
                TaiInstant test = TaiInstant.ofTaiSeconds(2L, NANOS_PER_SECOND + 1);
                assertAll(
                    () -> assertEquals(3, test.getTaiSeconds()),
                    () -> assertEquals(1, test.getNano())
                );
            }

            @Test
            void withMaxSecondsAndPositiveNanos_throwsArithmeticException() {
                assertThrows(ArithmeticException.class, () -> TaiInstant.ofTaiSeconds(Long.MAX_VALUE, NANOS_PER_SECOND));
            }
        }

        @Nested
        @DisplayName("of(Instant)")
        class OfInstant {
            @Test
            void fromInstant_createsCorrespondingTaiInstant() {
                // TAI epoch is 1958-01-01. UTC epoch is 1970-01-01.
                // The difference is 4383 days. 4383 * 86400 = 378,691,200 seconds.
                // At 1970-01-01, TAI was ahead of UTC by 10 seconds.
                // So, Instant.EPOCH corresponds to 378,691,200 + 10 = 378,691,210 TAI seconds.
                long expectedTaiSeconds = 378_691_210L;
                Instant instant = Instant.ofEpochSecond(0, 2); // 2ns after UTC epoch
                
                TaiInstant taiInstant = TaiInstant.of(instant);

                assertAll(
                    () -> assertEquals(expectedTaiSeconds, taiInstant.getTaiSeconds()),
                    () -> assertEquals(2, taiInstant.getNano())
                );
            }

            @Test
            void fromNullInstant_throwsNullPointerException() {
                assertThrows(NullPointerException.class, () -> TaiInstant.of((Instant) null));
            }
        }

        @Nested
        @DisplayName("of(UtcInstant)")
        class OfUtcInstant {
            @Test
            void fromUtcInstant_createsCorrespondingTaiInstant() {
                // TAI epoch is 1958-01-01T00:00:00(TAI).
                // This corresponds to MJD 36204.
                // The initial TAI-UTC difference is 10 seconds.
                // A UTC instant on MJD 36204 + 1 day, with 2ns, should be 1 day + 10s in TAI seconds.
                long oneDayInSeconds = 24 * 60 * 60;
                UtcInstant utcInstant = UtcInstant.ofModifiedJulianDay(36204 + 1, 2L);
                
                TaiInstant taiInstant = TaiInstant.of(utcInstant);

                assertAll(
                    () -> assertEquals(oneDayInSeconds + 10, taiInstant.getTaiSeconds()),
                    () -> assertEquals(2, taiInstant.getNano())
                );
            }

            @Test
            void fromNullUtcInstant_throwsNullPointerException() {
                assertThrows(NullPointerException.class, () -> TaiInstant.of((UtcInstant) null));
            }
        }

        @Nested
        @DisplayName("parse")
        class Parse {
            @ParameterizedTest(name = "Parsing \"{0}\" -> seconds={1}, nanos={2}")
            @CsvSource({
                "123.123456789s(TAI), 123, 123456789",
                "-5.000000001s(TAI), -5, 1",
                "0.999999999s(TAI), 0, 999999999"
            })
            void validString_parsesCorrectly(String text, long expectedSeconds, int expectedNanos) {
                TaiInstant test = TaiInstant.parse(text);
                assertAll(
                    () -> assertEquals(expectedSeconds, test.getTaiSeconds()),
                    () -> assertEquals(expectedNanos, test.getNano())
                );
            }

            public static Object[][] data_badParse() {
                return new Object[][] {
                    {"A.123456789s(TAI)"},      // Non-numeric seconds
                    {"123.12345678As(TAI)"},    // Non-numeric nanos
                    {"123.123456789"},          // Missing suffix
                    {"123.123456789s"},         // Incomplete suffix
                    {"+123.123456789s(TAI)"},   // Explicit positive sign not allowed
                    {"123.123s(TAI)"},          // Nanos must be 9 digits
                };
            }

            @ParameterizedTest(name = "Invalid format: {0}")
            @MethodSource("data_badParse")
            @DisplayName("Invalid string format throws DateTimeParseException")
            void invalidString_throwsDateTimeParseException(String str) {
                assertThrows(DateTimeParseException.class, () -> TaiInstant.parse(str));
            }

            @Test
            void nullString_throwsNullPointerException() {
                assertThrows(NullPointerException.class, () -> TaiInstant.parse(null));
            }
        }
    }

    @Nested
    @DisplayName("Modification methods")
    class Modification {
        @Test
        void withTaiSeconds_returnsUpdatedInstance() {
            TaiInstant base = TaiInstant.ofTaiSeconds(100, 50);
            TaiInstant updated = base.withTaiSeconds(200);
            assertAll(
                () -> assertEquals(200, updated.getTaiSeconds()),
                () -> assertEquals(50, updated.getNano())
            );
        }

        @Test
        void withNano_withValidNano_returnsUpdatedInstance() {
            TaiInstant base = TaiInstant.ofTaiSeconds(100, 50);
            TaiInstant updated = base.withNano(99);
            assertAll(
                () -> assertEquals(100, updated.getTaiSeconds()),
                () -> assertEquals(99, updated.getNano())
            );
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, 1_000_000_000})
        void withNano_withInvalidNano_throwsIllegalArgumentException(int invalidNano) {
            TaiInstant base = TaiInstant.ofTaiSeconds(100, 50);
            assertThrows(IllegalArgumentException.class, () -> base.withNano(invalidNano));
        }
    }

    @Nested
    @DisplayName("Arithmetic methods")
    class Arithmetic {
        @Nested
        @DisplayName("plus(Duration)")
        class Plus {
            @Test
            void zeroDuration_returnsEqualInstance() {
                TaiInstant base = TaiInstant.ofTaiSeconds(10, 20);
                assertEquals(base, base.plus(Duration.ZERO));
            }

            @Test
            void positiveDuration_addsCorrectly() {
                TaiInstant base = TaiInstant.ofTaiSeconds(10, 20);
                TaiInstant result = base.plus(Duration.ofSeconds(5, 30));
                assertAll(
                    () -> assertEquals(15, result.getTaiSeconds()),
                    () -> assertEquals(50, result.getNano())
                );
            }

            @Test
            void negativeDuration_subtractsCorrectly() {
                TaiInstant base = TaiInstant.ofTaiSeconds(10, 20);
                TaiInstant result = base.plus(Duration.ofSeconds(-5, -10));
                assertAll(
                    () -> assertEquals(5, result.getTaiSeconds()),
                    () -> assertEquals(10, result.getNano())
                );
            }

            @Test
            void durationCausingNanoOverflow_addsWithCarry() {
                TaiInstant base = TaiInstant.ofTaiSeconds(10, 800_000_000);
                TaiInstant result = base.plus(Duration.ofNanos(300_000_000)); // 0.3 seconds
                assertAll(
                    () -> assertEquals(11, result.getTaiSeconds()),
                    () -> assertEquals(100_000_000, result.getNano())
                );
            }

            @Test
            void durationCausingNanoUnderflow_addsWithBorrow() {
                TaiInstant base = TaiInstant.ofTaiSeconds(10, 100_000_000);
                TaiInstant result = base.plus(Duration.ofNanos(-300_000_000)); // -0.3 seconds
                assertAll(
                    () -> assertEquals(9, result.getTaiSeconds()),
                    () -> assertEquals(800_000_000, result.getNano())
                );
            }

            @Test
            void durationToMaxInstant_throwsArithmeticException() {
                TaiInstant max = TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 999_999_999);
                assertThrows(ArithmeticException.class, () -> max.plus(Duration.ofNanos(1)));
            }

            @Test
            void negativeDurationToMinInstant_throwsArithmeticException() {
                TaiInstant min = TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0);
                assertThrows(ArithmeticException.class, () -> min.plus(Duration.ofSeconds(-1)));
            }
        }

        @Nested
        @DisplayName("minus(Duration)")
        class Minus {
            @Test
            void zeroDuration_returnsEqualInstance() {
                TaiInstant base = TaiInstant.ofTaiSeconds(10, 20);
                assertEquals(base, base.minus(Duration.ZERO));
            }

            @Test
            void positiveDuration_subtractsCorrectly() {
                TaiInstant base = TaiInstant.ofTaiSeconds(10, 20);
                TaiInstant result = base.minus(Duration.ofSeconds(5, 10));
                assertAll(
                    () -> assertEquals(5, result.getTaiSeconds()),
                    () -> assertEquals(10, result.getNano())
                );
            }

            @Test
            void negativeDuration_addsCorrectly() {
                TaiInstant base = TaiInstant.ofTaiSeconds(10, 20);
                TaiInstant result = base.minus(Duration.ofSeconds(-5, -30));
                assertAll(
                    () -> assertEquals(15, result.getTaiSeconds()),
                    () -> assertEquals(50, result.getNano())
                );
            }

            @Test
            void durationCausingNanoBorrow_subtractsWithBorrow() {
                TaiInstant base = TaiInstant.ofTaiSeconds(10, 100_000_000);
                TaiInstant result = base.minus(Duration.ofNanos(300_000_000)); // 0.3 seconds
                assertAll(
                    () -> assertEquals(9, result.getTaiSeconds()),
                    () -> assertEquals(800_000_000, result.getNano())
                );
            }

            @Test
            void durationFromMinInstant_throwsArithmeticException() {
                TaiInstant min = TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0);
                assertThrows(ArithmeticException.class, () -> min.minus(Duration.ofNanos(1)));
            }

            @Test
            void negativeDurationFromMaxInstant_throwsArithmeticException() {
                TaiInstant max = TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 999_999_999);
                assertThrows(ArithmeticException.class, () -> max.minus(Duration.ofSeconds(-1)));
            }
        }

        @Nested
        @DisplayName("durationUntil(TaiInstant)")
        class DurationUntil {
            @Test
            void toLaterInstant_returnsPositiveDuration() {
                TaiInstant start = TaiInstant.ofTaiSeconds(10, 500);
                TaiInstant end = TaiInstant.ofTaiSeconds(25, 700);
                Duration duration = start.durationUntil(end);
                assertEquals(Duration.ofSeconds(15, 200), duration);
            }

            @Test
            void toEarlierInstant_returnsNegativeDuration() {
                TaiInstant start = TaiInstant.ofTaiSeconds(25, 700);
                TaiInstant end = TaiInstant.ofTaiSeconds(10, 500);
                Duration duration = start.durationUntil(end);
                assertEquals(Duration.ofSeconds(-15, -200), duration);
            }

            @Test
            void toLaterInstantWithNanoBorrow_returnsCorrectDuration() {
                TaiInstant start = TaiInstant.ofTaiSeconds(10, 700);
                TaiInstant end = TaiInstant.ofTaiSeconds(25, 500);
                Duration duration = start.durationUntil(end);
                assertEquals(Duration.ofSeconds(14, 800_000_000), duration);
            }
        }
    }

    @Nested
    @DisplayName("Conversion methods")
    class Conversion {
        @Test
        void toUtcInstant_convertsCorrectly() {
            long oneDayInSeconds = 24 * 60 * 60;
            TaiInstant taiInstant = TaiInstant.ofTaiSeconds(oneDayInSeconds + 10, 2);
            
            UtcInstant utcInstant = taiInstant.toUtcInstant();
            
            // MJD 36204 is TAI epoch. Expect MJD to be one day after.
            assertAll(
                () -> assertEquals(36204 + 1, utcInstant.getModifiedJulianDay()),
                () -> assertEquals(2, utcInstant.getNanoOfSecond())
            );
        }

        @Test
        void toInstant_convertsCorrectly() {
            // TAI seconds for UTC epoch is 378,691,210.
            // A TAI instant 1 second after that should correspond to 1s after UTC epoch.
            long taiSecondsAtUtcEpoch = 378_691_210L;
            TaiInstant taiInstant = TaiInstant.ofTaiSeconds(taiSecondsAtUtcEpoch + 1, 2);

            Instant instant = taiInstant.toInstant();
            
            assertEquals(Instant.ofEpochSecond(1, 2), instant);
        }
    }

    @Nested
    @DisplayName("Comparison methods")
    class Comparison {
        private final TaiInstant T_NEG2_N0 = TaiInstant.ofTaiSeconds(-2L, 0);
        private final TaiInstant T_NEG2_N_MAX = TaiInstant.ofTaiSeconds(-2L, 999_999_999);
        private final TaiInstant T_NEG1_N0 = TaiInstant.ofTaiSeconds(-1L, 0);
        private final TaiInstant T_NEG1_N1 = TaiInstant.ofTaiSeconds(-1L, 1);
        private final TaiInstant T_0_N0 = TaiInstant.ofTaiSeconds(0L, 0);
        private final TaiInstant T_1_N0 = TaiInstant.ofTaiSeconds(1L, 0);

        @Test
        void compareTo_and_isBefore_isAfter() {
            TaiInstant[] instants = new TaiInstant[] {
                T_NEG2_N0, T_NEG2_N_MAX, T_NEG1_N0, T_NEG1_N1, T_0_N0, T_1_N0
            };

            for (int i = 0; i < instants.length; i++) {
                for (int j = 0; j < instants.length; j++) {
                    TaiInstant a = instants[i];
                    TaiInstant b = instants[j];
                    if (i < j) {
                        assertTrue(a.compareTo(b) < 0, a + " should be less than " + b);
                        assertTrue(a.isBefore(b), a + ".isBefore(" + b + ")");
                        assertFalse(a.isAfter(b), "!" + a + ".isAfter(" + b + ")");
                    } else if (i > j) {
                        assertTrue(a.compareTo(b) > 0, a + " should be greater than " + b);
                        assertFalse(a.isBefore(b), "!" + a + ".isBefore(" + b + ")");
                        assertTrue(a.isAfter(b), a + ".isAfter(" + b + ")");
                    } else {
                        assertEquals(0, a.compareTo(b), a + " should be equal to " + b);
                        assertFalse(a.isBefore(b), "!" + a + ".isBefore(" + b + ")");
                        assertFalse(a.isAfter(b), "!" + a + ".isAfter(" + b + ")");
                    }
                }
            }
        }

        @Test
        void compareTo_null_throwsNullPointerException() {
            assertThrows(NullPointerException.class, () -> T_0_N0.compareTo(null));
        }

        @Test
        @SuppressWarnings({"unchecked", "rawtypes"})
        void compareTo_nonTaiInstant_throwsClassCastException() {
            Comparable c = T_0_N0;
            assertThrows(ClassCastException.class, () -> c.compareTo(new Object()));
        }
    }

    @Nested
    @DisplayName("General methods")
    class GeneralMethods {
        @Test
        void equals_and_hashCode_contract() {
            new EqualsTester()
                .addEqualityGroup(TaiInstant.ofTaiSeconds(5L, 20), TaiInstant.ofTaiSeconds(5L, 20))
                .addEqualityGroup(TaiInstant.ofTaiSeconds(5L, 30))
                .addEqualityGroup(TaiInstant.ofTaiSeconds(6L, 20))
                .testEquals();
        }

        @Test
        void toString_formatsCorrectly() {
            assertEquals("123.123456789s(TAI)", TaiInstant.ofTaiSeconds(123L, 123456789).toString());
            assertEquals("-123.123456789s(TAI)", TaiInstant.ofTaiSeconds(-123L, 123456789).toString());
            assertEquals("0.000000567s(TAI)", TaiInstant.ofTaiSeconds(0L, 567).toString());
        }
    }
}