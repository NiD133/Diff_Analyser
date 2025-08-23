package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.*;
import static java.time.Duration.ofSeconds;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.testing.EqualsTester;

/**
 * Tests for TaiInstant.
 * 
 * This suite aims to be explicit about:
 * - how seconds and nanoseconds are normalized
 * - conversion boundaries and overflow behavior
 * - conversions to/from Instant and UtcInstant using clear constants
 */
@DisplayName("TaiInstant")
public class TestTaiInstant {

    // Common constants used across tests for readability
    private static final long SECONDS_PER_DAY = 86_400L;
    private static final int NANOS_PER_SECOND = 1_000_000_000;

    // MJD constants commonly used in TAI/UTC conversions
    private static final int MJD_TAI_EPOCH = 36_204;  // 1958-01-01 (TAI epoch)
    private static final int MJD_UNIX_EPOCH = 40_587; // 1970-01-01 (Unix epoch)

    // Unix epoch relative to TAI epoch is 4383 days later: 4383 * 86400 = 378,691,200
    private static final long SECONDS_BETWEEN_TAI_AND_UNIX_EPOCHS = 378_691_200L;

    // Historical offset used by these tests:
    // When converting between Instant (UTC-SLS) and TAI here we account for +10s at 1970-01-01.
    private static final int TAI_MINUS_UTC_AT_UNIX_EPOCH_SECONDS = 10;

    // Small helpers to avoid duplication and magic constants
    private static TaiInstant tai(long seconds, long nanos) {
        return TaiInstant.ofTaiSeconds(seconds, nanos);
    }

    private static void assertTai(TaiInstant actual, long expectedSeconds, long expectedNanos) {
        assertEquals(expectedSeconds, actual.getTaiSeconds(), "TAI seconds");
        assertEquals(expectedNanos, actual.getNano(), "nanos-of-second");
    }

    @Test
    public void test_interfaces() {
        assertTrue(Serializable.class.isAssignableFrom(TaiInstant.class));
        assertTrue(Comparable.class.isAssignableFrom(TaiInstant.class));
    }

    @Nested
    @DisplayName("Serialization")
    class SerializationTests {
        @Test
        public void writesAndReadsBack() throws Exception {
            TaiInstant original = tai(2, 3);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            try (ObjectOutputStream out = new ObjectOutputStream(baos)) {
                out.writeObject(original);
            }
            try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
                Object read = in.readObject();
                assertEquals(original, read);
            }
        }
    }

    @Nested
    @DisplayName("Factory: ofTaiSeconds(seconds, nanos)")
    class OfTaiSecondsTests {
        @Test
        public void normalizesNegativeAndPositiveNanos() {
            for (long sec = -2; sec <= 2; sec++) {
                // nanos in [0, 9] should stay in the same second
                for (int nano = 0; nano < 10; nano++) {
                    TaiInstant t = tai(sec, nano);
                    assertTai(t, sec, nano);
                }
                // negative nanos shift one second back
                for (int nano = -10; nano < 0; nano++) {
                    TaiInstant t = tai(sec, nano);
                    assertTai(t, sec - 1, nano + NANOS_PER_SECOND);
                }
                // high valid nanos still in the same second
                for (int nano = 999_999_990; nano < NANOS_PER_SECOND; nano++) {
                    TaiInstant t = tai(sec, nano);
                    assertTai(t, sec, nano);
                }
            }
        }

        @Test
        public void adjustsWhenNanosIsNegative() {
            TaiInstant t = tai(2L, -1);
            assertTai(t, 1, 999_999_999);
        }

        @Test
        public void throwsWhenNanoAdjustmentWouldOverflow() {
            assertThrows(ArithmeticException.class, () -> tai(Long.MAX_VALUE, NANOS_PER_SECOND));
        }
    }

    @Nested
    @DisplayName("Factory: of(Instant)")
    class OfInstantTests {
        @Test
        public void convertsInstantToTAI() {
            // Instant at unix epoch + 2ns
            TaiInstant tai = TaiInstant.of(Instant.ofEpochSecond(0, 2));
            long expectedTai =
                (MJD_UNIX_EPOCH - MJD_TAI_EPOCH) * SECONDS_PER_DAY + TAI_MINUS_UTC_AT_UNIX_EPOCH_SECONDS;
            assertTai(tai, expectedTai, 2);
        }

        @Test
        public void nullInstantIsRejected() {
            assertThrows(NullPointerException.class, () -> TaiInstant.of((Instant) null));
        }
    }

    @Nested
    @DisplayName("Factory: of(UtcInstant)")
    class OfUtcInstantTests {
        @Test
        public void convertsUtcInstantToTAI() {
            for (int dayOffset = -1000; dayOffset < 1000; dayOffset++) {
                for (int secondOfDay = 0; secondOfDay < 10; secondOfDay++) {
                    long mjd = MJD_TAI_EPOCH + dayOffset; // 1958-01-01 base
                    long nanos = secondOfDay * (long) NANOS_PER_SECOND + 2L;
                    TaiInstant taiInstant = TaiInstant.of(UtcInstant.ofModifiedJulianDay(mjd, nanos));

                    long expectedSeconds = dayOffset * SECONDS_PER_DAY + secondOfDay + TAI_MINUS_UTC_AT_UNIX_EPOCH_SECONDS;
                    assertTai(taiInstant, expectedSeconds, 2);
                }
            }
        }

        @Test
        public void nullUtcInstantIsRejected() {
            assertThrows(NullPointerException.class, () -> TaiInstant.of((UtcInstant) null));
        }
    }

    @Nested
    @DisplayName("Parsing")
    class ParsingTests {

        @Test
        public void parsesCanonicalForm() {
            for (int sec = -1000; sec < 1000; sec++) {
                for (int nano = 900_000_000; nano < 990_000_000; nano += 10_000_000) {
                    TaiInstant parsed = TaiInstant.parse(sec + "." + nano + "s(TAI)");
                    assertTai(parsed, sec, nano);
                }
            }
        }

        static Stream<String> data_badParse() {
            return Stream.of(
                "A.123456789s(TAI)",   // non-numeric seconds
                "123.12345678As(TAI)", // non-numeric nanos
                "123.123456789",       // missing unit suffix
                "123.123456789s",      // missing (TAI)
                "+123.123456789s(TAI)",// leading + not allowed
                "-123.123s(TAI)"       // nanos must have 9 digits
            );
        }

        @ParameterizedTest
        @MethodSource("data_badParse")
        public void rejectsInvalidFormats(String text) {
            assertThrows(DateTimeParseException.class, () -> TaiInstant.parse(text));
        }

        @Test
        public void rejectsNullText() {
            assertThrows(NullPointerException.class, () -> TaiInstant.parse((String) null));
        }
    }

    @Nested
    @DisplayName("withTaiSeconds and withNano")
    class WithersTests {

        static Stream<org.junit.jupiter.params.provider.Arguments> data_withTAISeconds() {
            return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of(0L,  12_345L,  1L,  1L, 12_345L),
                org.junit.jupiter.params.provider.Arguments.of(0L,  12_345L, -1L, -1L, 12_345L),
                org.junit.jupiter.params.provider.Arguments.of(7L,  12_345L,  2L,  2L, 12_345L),
                org.junit.jupiter.params.provider.Arguments.of(7L,  12_345L, -2L, -2L, 12_345L),
                org.junit.jupiter.params.provider.Arguments.of(-99L,12_345L,  3L,  3L, 12_345L),
                org.junit.jupiter.params.provider.Arguments.of(-99L,12_345L, -3L, -3L, 12_345L)
            );
        }

        @ParameterizedTest
        @MethodSource("data_withTAISeconds")
        public void withTAISeconds(long seconds, long nanos, long newSeconds, Long expectedSeconds, Long expectedNanos) {
            TaiInstant adjusted = tai(seconds, nanos).withTaiSeconds(newSeconds);
            assertTai(adjusted, expectedSeconds, expectedNanos);
        }

        static Stream<org.junit.jupiter.params.provider.Arguments> data_withNano() {
            return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of(0L,   12_345L,      1,  0L,               1L),
                org.junit.jupiter.params.provider.Arguments.of(7L,   12_345L,      2,  7L,               2L),
                org.junit.jupiter.params.provider.Arguments.of(-99L, 12_345L,      3, -99L,              3L),
                org.junit.jupiter.params.provider.Arguments.of(-99L, 12_345L, 999_999_999, -99L, 999_999_999L),
                // invalid nanos
                org.junit.jupiter.params.provider.Arguments.of(-99L, 12_345L,     -1,  null,            null),
                org.junit.jupiter.params.provider.Arguments.of(-99L, 12_345L, NANOS_PER_SECOND, null,   null)
            );
        }

        @ParameterizedTest
        @MethodSource("data_withNano")
        public void withNano(long seconds, long nanos, int newNano, Long expectedSeconds, Long expectedNanos) {
            TaiInstant base = tai(seconds, nanos);
            if (expectedSeconds != null) {
                TaiInstant adjusted = base.withNano(newNano);
                assertTai(adjusted, expectedSeconds, expectedNanos);
            } else {
                assertThrows(IllegalArgumentException.class, () -> base.withNano(newNano));
            }
        }
    }

    @Nested
    @DisplayName("Arithmetic: plus(Duration)")
    class PlusTests {

        static Object[][] data_plus() {
            return new Object[][]{
                {Long.MIN_VALUE, 0, Long.MAX_VALUE, 0, -1, 0},

                {-4, 666_666_667, -4, 666_666_667, -7, 333_333_334},
                {-4, 666_666_667, -3,         0, -7, 666_666_667},
                {-4, 666_666_667, -2,         0, -6, 666_666_667},
                {-4, 666_666_667, -1,         0, -5, 666_666_667},
                {-4, 666_666_667, -1, 333_333_334, -4,         1},
                {-4, 666_666_667, -1, 666_666_667, -4, 333_333_334},
                {-4, 666_666_667, -1, 999_999_999, -4, 666_666_666},
                {-4, 666_666_667,  0,         0, -4, 666_666_667},
                {-4, 666_666_667,  0,         1, -4, 666_666_668},
                {-4, 666_666_667,  0, 333_333_333, -3,         0},
                {-4, 666_666_667,  0, 666_666_666, -3, 333_333_333},
                {-4, 666_666_667,  1,         0, -3, 666_666_667},
                {-4, 666_666_667,  2,         0, -2, 666_666_667},
                {-4, 666_666_667,  3,         0, -1, 666_666_667},
                {-4, 666_666_667,  3, 333_333_333,  0,         0},

                {-3, 0, -4, 666_666_667, -7, 666_666_667},
                {-3, 0, -3,         0, -6,         0},
                {-3, 0, -2,         0, -5,         0},
                {-3, 0, -1,         0, -4,         0},
                {-3, 0, -1, 333_333_334, -4, 333_333_334},
                {-3, 0, -1, 666_666_667, -4, 666_666_667},
                {-3, 0, -1, 999_999_999, -4, 999_999_999},
                {-3, 0,  0,         0, -3,         0},
                {-3, 0,  0,         1, -3,         1},
                {-3, 0,  0, 333_333_333, -3, 333_333_333},
                {-3, 0,  0, 666_666_666, -3, 666_666_666},
                {-3, 0,  1,         0, -2,         0},
                {-3, 0,  2,         0, -1,         0},
                {-3, 0,  3,         0,  0,         0},
                {-3, 0,  3, 333_333_333,  0, 333_333_333},

                {-2, 0, -4, 666_666_667, -6, 666_666_667},
                {-2, 0, -3,         0, -5,         0},
                {-2, 0, -2,         0, -4,         0},
                {-2, 0, -1,         0, -3,         0},
                {-2, 0, -1, 333_333_334, -3, 333_333_334},
                {-2, 0, -1, 666_666_667, -3, 666_666_667},
                {-2, 0, -1, 999_999_999, -3, 999_999_999},
                {-2, 0,  0,         0, -2,         0},
                {-2, 0,  0,         1, -2,         1},
                {-2, 0,  0, 333_333_333, -2, 333_333_333},
                {-2, 0,  0, 666_666_666, -2, 666_666_666},
                {-2, 0,  1,         0, -1,         0},
                {-2, 0,  2,         0,  0,         0},
                {-2, 0,  3,         0,  1,         0},
                {-2, 0,  3, 333_333_333,  1, 333_333_333},

                {-1, 0, -4, 666_666_667, -5, 666_666_667},
                {-1, 0, -3,         0, -4,         0},
                {-1, 0, -2,         0, -3,         0},
                {-1, 0, -1,         0, -2,         0},
                {-1, 0, -1, 333_333_334, -2, 333_333_334},
                {-1, 0, -1, 666_666_667, -2, 666_666_667},
                {-1, 0, -1, 999_999_999, -2, 999_999_999},
                {-1, 0,  0,         0, -1,         0},
                {-1, 0,  0,         1, -1,         1},
                {-1, 0,  0, 333_333_333, -1, 333_333_333},
                {-1, 0,  0, 666_666_666, -1, 666_666_666},
                {-1, 0,  1,         0,  0,         0},
                {-1, 0,  2,         0,  1,         0},
                {-1, 0,  3,         0,  2,         0},
                {-1, 0,  3, 333_333_333,  2, 333_333_333},

                {-1, 666_666_667, -4, 666_666_667, -4, 333_333_334},
                {-1, 666_666_667, -3,         0, -4, 666_666_667},
                {-1, 666_666_667, -2,         0, -3, 666_666_667},
                {-1, 666_666_667, -1,         0, -2, 666_666_667},
                {-1, 666_666_667, -1, 333_333_334, -1,         1},
                {-1, 666_666_667, -1, 666_666_667, -1, 333_333_334},
                {-1, 666_666_667, -1, 999_999_999, -1, 666_666_666},
                {-1, 666_666_667,  0,         0, -1, 666_666_667},
                {-1, 666_666_667,  0,         1, -1, 666_666_668},
                {-1, 666_666_667,  0, 333_333_333,  0,         0},
                {-1, 666_666_667,  0, 666_666_666,  0, 333_333_333},
                {-1, 666_666_667,  1,         0,  0, 666_666_667},
                {-1, 666_666_667,  2,         0,  1, 666_666_667},
                {-1, 666_666_667,  3,         0,  2, 666_666_667},
                {-1, 666_666_667,  3, 333_333_333,  3,         0},

                {0, 0, -4, 666_666_667, -4, 666_666_667},
                {0, 0, -3,         0, -3,         0},
                {0, 0, -2,         0, -2,         0},
                {0, 0, -1,         0, -1,         0},
                {0, 0, -1, 333_333_334, -1, 333_333_334},
                {0, 0, -1, 666_666_667, -1, 666_666_667},
                {0, 0, -1, 999_999_999, -1, 999_999_999},
                {0, 0,  0,         0,  0,         0},
                {0, 0,  0,         1,  0,         1},
                {0, 0,  0, 333_333_333,  0, 333_333_333},
                {0, 0,  0, 666_666_666,  0, 666_666_666},
                {0, 0,  1,         0,  1,         0},
                {0, 0,  2,         0,  2,         0},
                {0, 0,  3,         0,  3,         0},
                {0, 0,  3, 333_333_333,  3, 333_333_333},

                {0, 333_333_333, -4, 666_666_667, -3,         0},
                {0, 333_333_333, -3,         0, -3, 333_333_333},
                {0, 333_333_333, -2,         0, -2, 333_333_333},
                {0, 333_333_333, -1,         0, -1, 333_333_333},
                {0, 333_333_333, -1, 333_333_334, -1, 666_666_667},
                {0, 333_333_333, -1, 666_666_667,  0,         0},
                {0, 333_333_333, -1, 999_999_999,  0, 333_333_332},
                {0, 333_333_333,  0,         0,  0, 333_333_333},
                {0, 333_333_333,  0,         1,  0, 333_333_334},
                {0, 333_333_333,  0, 333_333_333,  0, 666_666_666},
                {0, 333_333_333,  0, 666_666_666,  0, 999_999_999},
                {0, 333_333_333,  1,         0,  1, 333_333_333},
                {0, 333_333_333,  2,         0,  2, 333_333_333},
                {0, 333_333_333,  3,         0,  3, 333_333_333},
                {0, 333_333_333,  3, 333_333_333,  3, 666_666_666},

                {1, 0, -4, 666_666_667, -3, 666_666_667},
                {1, 0, -3,         0, -2,         0},
                {1, 0, -2,         0, -1,         0},
                {1, 0, -1,         0,  0,         0},
                {1, 0, -1, 333_333_334,  0, 333_333_334},
                {1, 0, -1, 666_666_667,  0, 666_666_667},
                {1, 0, -1, 999_999_999,  0, 999_999_999},
                {1, 0,  0,         0,  1,         0},
                {1, 0,  0,         1,  1,         1},
                {1, 0,  0, 333_333_333,  1, 333_333_333},
                {1, 0,  0, 666_666_666,  1, 666_666_666},
                {1, 0,  1,         0,  2,         0},
                {1, 0,  2,         0,  3,         0},
                {1, 0,  3,         0,  4,         0},
                {1, 0,  3, 333_333_333,  4, 333_333_333},

                {2, 0, -4, 666_666_667, -2, 666_666_667},
                {2, 0, -3,         0, -1,         0},
                {2, 0, -2,         0,  0,         0},
                {2, 0, -1,         0,  1,         0},
                {2, 0, -1, 333_333_334,  1, 333_333_334},
                {2, 0, -1, 666_666_667,  1, 666_666_667},
                {2, 0, -1, 999_999_999,  1, 999_999_999},
                {2, 0,  0,         0,  2,         0},
                {2, 0,  0,         1,  2,         1},
                {2, 0,  0, 333_333_333,  2, 333_333_333},
                {2, 0,  0, 666_666_666,  2, 666_666_666},
                {2, 0,  1,         0,  3,         0},
                {2, 0,  2,         0,  4,         0},
                {2, 0,  3,         0,  5,         0},
                {2, 0,  3, 333_333_333,  5, 333_333_333},

                {3, 0, -4, 666_666_667, -1, 666_666_667},
                {3, 0, -3,         0,  0,         0},
                {3, 0, -2,         0,  1,         0},
                {3, 0, -1,         0,  2,         0},
                {3, 0, -1, 333_333_334,  2, 333_333_334},
                {3, 0, -1, 666_666_667,  2, 666_666_667},
                {3, 0, -1, 999_999_999,  2, 999_999_999},
                {3, 0,  0,         0,  3,         0},
                {3, 0,  0,         1,  3,         1},
                {3, 0,  0, 333_333_333,  3, 333_333_333},
                {3, 0,  0, 666_666_666,  3, 666_666_666},
                {3, 0,  1,         0,  4,         0},
                {3, 0,  2,         0,  5,         0},
                {3, 0,  3,         0,  6,         0},
                {3, 0,  3, 333_333_333,  6, 333_333_333},

                {3, 333_333_333, -4, 666_666_667,  0,         0},
                {3, 333_333_333, -3,         0,  0, 333_333_333},
                {3, 333_333_333, -2,         0,  1, 333_333_333},
                {3, 333_333_333, -1,         0,  2, 333_333_333},
                {3, 333_333_333, -1, 333_333_334,  2, 666_666_667},
                {3, 333_333_333, -1, 666_666_667,  3,         0},
                {3, 333_333_333, -1, 999_999_999,  3, 333_333_332},
                {3, 333_333_333,  0,         0,  3, 333_333_333},
                {3, 333_333_333,  0,         1,  3, 333_333_334},
                {3, 333_333_333,  0, 333_333_333,  3, 666_666_666},
                {3, 333_333_333,  0, 666_666_666,  3, 999_999_999},
                {3, 333_333_333,  1,         0,  4, 333_333_333},
                {3, 333_333_333,  2,         0,  5, 333_333_333},
                {3, 333_333_333,  3,         0,  6, 333_333_333},
                {3, 333_333_333,  3, 333_333_333,  6, 666_666_666},

                {Long.MAX_VALUE, 0, Long.MIN_VALUE, 0, -1, 0},
            };
        }

        @ParameterizedTest
        @MethodSource("data_plus")
        public void plusDuration(long seconds, int nanos, long plusSeconds, int plusNanos, long expectedSeconds, int expectedNanoOfSecond) {
            TaiInstant result = tai(seconds, nanos).plus(Duration.ofSeconds(plusSeconds, plusNanos));
            assertTai(result, expectedSeconds, expectedNanoOfSecond);
        }

        @Test
        public void overflowWhenAddingNanosPastMax() {
            TaiInstant i = tai(Long.MAX_VALUE, 999_999_999);
            assertThrows(ArithmeticException.class, () -> i.plus(ofSeconds(0, 1)));
        }

        @Test
        public void overflowWhenAddingNegativeDurationPastMin() {
            TaiInstant i = tai(Long.MIN_VALUE, 0);
            assertThrows(ArithmeticException.class, () -> i.plus(ofSeconds(-1, 999_999_999)));
        }
    }

    @Nested
    @DisplayName("Arithmetic: minus(Duration)")
    class MinusTests {

        static Object[][] data_minus() {
            return new Object[][]{
                {Long.MIN_VALUE, 0, Long.MIN_VALUE + 1, 0, -1, 0},

                {-4, 666_666_667, -4, 666_666_667,  0,         0},
                {-4, 666_666_667, -3,         0, -1, 666_666_667},
                {-4, 666_666_667, -2,         0, -2, 666_666_667},
                {-4, 666_666_667, -1,         0, -3, 666_666_667},
                {-4, 666_666_667, -1, 333_333_334, -3, 333_333_333},
                {-4, 666_666_667, -1, 666_666_667, -3,         0},
                {-4, 666_666_667, -1, 999_999_999, -4, 666_666_668},
                {-4, 666_666_667,  0,         0, -4, 666_666_667},
                {-4, 666_666_667,  0,         1, -4, 666_666_666},
                {-4, 666_666_667,  0, 333_333_333, -4, 333_333_334},
                {-4, 666_666_667,  0, 666_666_666, -4,         1},
                {-4, 666_666_667,  1,         0, -5, 666_666_667},
                {-4, 666_666_667,  2,         0, -6, 666_666_667},
                {-4, 666_666_667,  3,         0, -7, 666_666_667},
                {-4, 666_666_667,  3, 333_333_333, -7, 333_333_334},

                {-3, 0, -4, 666_666_667,  0, 333_333_333},
                {-3, 0, -3,         0,  0,         0},
                {-3, 0, -2,         0, -1,         0},
                {-3, 0, -1,         0, -2,         0},
                {-3, 0, -1, 333_333_334, -3, 666_666_666},
                {-3, 0, -1, 666_666_667, -3, 333_333_333},
                {-3, 0, -1, 999_999_999, -3,         1},
                {-3, 0,  0,         0, -3,         0},
                {-3, 0,  0,         1, -4, 999_999_999},
                {-3, 0,  0, 333_333_333, -4, 666_666_667},
                {-3, 0,  0, 666_666_666, -4, 333_333_334},
                {-3, 0,  1,         0, -4,         0},
                {-3, 0,  2,         0, -5,         0},
                {-3, 0,  3,         0, -6,         0},
                {-3, 0,  3, 333_333_333, -7, 666_666_667},

                {-2, 0, -4, 666_666_667,  1, 333_333_333},
                {-2, 0, -3,         0,  1,         0},
                {-2, 0, -2,         0,  0,         0},
                {-2, 0, -1,         0, -1,         0},
                {-2, 0, -1, 333_333_334, -2, 666_666_666},
                {-2, 0, -1, 666_666_667, -2, 333_333_333},
                {-2, 0, -1, 999_999_999, -2,         1},
                {-2, 0,  0,         0, -2,         0},
                {-2, 0,  0,         1, -3, 999_999_999},
                {-2, 0,  0, 333_333_333, -3, 666_666_667},
                {-2, 0,  0, 666_666_666, -3, 333_333_334},
                {-2, 0,  1,         0, -3,         0},
                {-2, 0,  2,         0, -4,         0},
                {-2, 0,  3,         0, -5,         0},
                {-2, 0,  3, 333_333_333, -6, 666_666_667},

                {-1, 0, -4, 666_666_667,  2, 333_333_333},
                {-1, 0, -3,         0,  2,         0},
                {-1, 0, -2,         0,  1,         0},
                {-1, 0, -1,         0,  0,         0},
                {-1, 0, -1, 333_333_334, -1, 666_666_666},
                {-1, 0, -1, 666_666_667, -1, 333_333_333},
                {-1, 0, -1, 999_999_999, -1,         1},
                {-1, 0,  0,         0, -1,         0},
                {-1, 0,  0,         1, -2, 999_999_999},
                {-1, 0,  0, 333_333_333, -2, 666_666_667},
                {-1, 0,  0, 666_666_666, -2, 333_333_334},
                {-1, 0,  1,         0, -2,         0},
                {-1, 0,  2,         0, -3,         0},
                {-1, 0,  3,         0, -4,         0},
                {-1, 0,  3, 333_333_333, -5, 666_666_667},

                {-1, 666_666_667, -4, 666_666_667,  3,         0},
                {-1, 666_666_667, -3,         0,  2, 666_666_667},
                {-1, 666_666_667, -2,         0,  1, 666_666_667},
                {-1, 666_666_667, -1,         0,  0, 666_666_667},
                {-1, 666_666_667, -1, 333_333_334,  0, 333_333_333},
                {-1, 666_666_667, -1, 666_666_667,  0,         0},
                {-1, 666_666_667, -1, 999_999_999, -1, 666_666_668},
                {-1, 666_666_667,  0,         0, -1, 666_666_667},
                {-1, 666_666_667,  0,         1, -1, 666_666_666},
                {-1, 666_666_667,  0, 333_333_333, -1, 333_333_334},
                {-1, 666_666_667,  0, 666_666_666, -1,         1},
                {-1, 666_666_667,  1,         0, -2, 666_666_667},
                {-1, 666_666_667,  2,         0, -3, 666_666_667},
                {-1, 666_666_667,  3,         0, -4, 666_666_667},
                {-1, 666_666_667,  3, 333_333_333, -4, 333_333_334},

                {0, 0, -4, 666_666_667,  3, 333_333_333},
                {0, 0, -3,         0,  3,         0},
                {0, 0, -2,         0,  2,         0},
                {0, 0, -1,         0,  1,         0},
                {0, 0, -1, 333_333_334,  0, 666_666_666},
                {0, 0, -1, 666_666_667,  0, 333_333_333},
                {0, 0, -1, 999_999_999,  0,         1},
                {0, 0,  0,         0,  0,         0},
                {0, 0,  0,         1, -1, 999_999_999},
                {0, 0,  0, 333_333_333, -1, 666_666_667},
                {0, 0,  0, 666_666_666, -1, 333_333_334},
                {0, 0,  1,         0, -1,         0},
                {0, 0,  2,         0, -2,         0},
                {0, 0,  3,         0, -3,         0},
                {0, 0,  3, 333_333_333, -4, 666_666_667},

                {0, 333_333_333, -4, 666_666_667,  3, 666_666_666},
                {0, 333_333_333, -3,         0,  3, 333_333_333},
                {0, 333_333_333, -2,         0,  2, 333_333_333},
                {0, 333_333_333, -1,         0,  1, 333_333_333},
                {0, 333_333_333, -1, 333_333_334,  0, 999_999_999},
                {0, 333_333_333, -1, 666_666_667,  0, 666_666_666},
                {0, 333_333_333, -1, 999_999_999,  0, 333_333_334},
                {0, 333_333_333,  0,         0,  0, 333_333_333},
                {0, 333_333_333,  0,         1,  0, 333_333_332},
                {0, 333_333_333,  0, 333_333_333,  0,         0},
                {0, 333_333_333,  0, 666_666_666, -1, 666_666_667},
                {0, 333_333_333,  1,         0, -1, 333_333_333},
                {0, 333_333_333,  2,         0, -2, 333_333_333},
                {0, 333_333_333,  3,         0, -3, 333_333_333},
                {0, 333_333_333,  3, 333_333_333, -3,         0},

                {1, 0, -4, 666_666_667,  4, 333_333_333},
                {1, 0, -3,         0,  4,         0},
                {1, 0, -2,         0,  3,         0},
                {1, 0, -1,         0,  2,         0},
                {1, 0, -1, 333_333_334,  1, 666_666_666},
                {1, 0, -1, 666_666_667,  1, 333_333_333},
                {1, 0, -1, 999_999_999,  1,         1},
                {1, 0,  0,         0,  1,         0},
                {1, 0,  0,         1,  0, 999_999_999},
                {1, 0,  0, 333_333_333,  0, 666_666_667},
                {1, 0,  0, 666_666_666,  0, 333_333_334},
                {1, 0,  1,         0,  0,         0},
                {1, 0,  2,         0, -1,         0},
                {1, 0,  3,         0, -2,         0},
                {1, 0,  3, 333_333_333, -3, 666_666_667},

                {2, 0, -4, 666_666_667,  5, 333_333_333},
                {2, 0, -3,         0,  5,         0},
                {2, 0, -2,         0,  4,         0},
                {2, 0, -1,         0,  3,         0},
                {2, 0, -1, 333_333_334,  2, 666_666_666},
                {2, 0, -1, 666_666_667,  2, 333_333_333},
                {2, 0, -1, 999_999_999,  2,         1},
                {2, 0,  0,         0,  2,         0},
                {2, 0,  0,         1,  1, 999_999_999},
                {2, 0,  0, 333_333_333,  1, 666_666_667},
                {2, 0,  0, 666_666_666,  1, 333_333_334},
                {2, 0,  1,         0,  1,         0},
                {2, 0,  2,         0,  0,         0},
                {2, 0,  3,         0, -1,         0},
                {2, 0,  3, 333_333_333, -2, 666_666_667},

                {3, 0, -4, 666_666_667,  6, 333_333_333},
                {3, 0, -3,         0,  6,         0},
                {3, 0, -2,         0,  5,         0},
                {3, 0, -1,         0,  4,         0},
                {3, 0, -1, 333_333_334,  3, 666_666_666},
                {3, 0, -1, 666_666_667,  3, 333_333_333},
                {3, 0, -1, 999_999_999,  3,         1},
                {3, 0,  0,         0,  3,         0},
                {3, 0,  0,         1,  2, 999_999_999},
                {3, 0,  0, 333_333_333,  2, 666_666_667},
                {3, 0,  0, 666_666_666,  2, 333_333_334},
                {3, 0,  1,         0,  2,         0},
                {3, 0,  2,         0,  1,         0},
                {3, 0,  3,         0,  0,         0},
                {3, 0,  3, 333_333_333, -1, 666_666_667},

                {3, 333_333_333, -4, 666_666_667,  6, 666_666_666},
                {3, 333_333_333, -3,         0,  6, 333_333_333},
                {3, 333_333_333, -2,         0,  5, 333_333_333},
                {3, 333_333_333, -1,         0,  4, 333_333_333},
                {3, 333_333_333, -1, 333_333_334,  3, 999_999_999},
                {3, 333_333_333, -1, 666_666_667,  3, 666_666_666},
                {3, 333_333_333, -1, 999_999_999,  3, 333_333_334},
                {3, 333_333_333,  0,         0,  3, 333_333_333},
                {3, 333_333_333,  0,         1,  3, 333_333_332},
                {3, 333_333_333,  0, 333_333_333,  3,         0},
                {3, 333_333_333,  0, 666_666_666,  2, 666_666_667},
                {3, 333_333_333,  1,         0,  2, 333_333_333},
                {3, 333_333_333,  2,         0,  1, 333_333_333},
                {3, 333_333_333,  3,         0,  0, 333_333_333},
                {3, 333_333_333,  3, 333_333_333,  0,         0},

                {Long.MAX_VALUE, 0, Long.MAX_VALUE, 0, 0, 0},
            };
        }

        @ParameterizedTest
        @MethodSource("data_minus")
        public void minusDuration(long seconds, int nanos, long minusSeconds, int minusNanos, long expectedSeconds, int expectedNanoOfSecond) {
            TaiInstant result = tai(seconds, nanos).minus(Duration.ofSeconds(minusSeconds, minusNanos));
            assertTai(result, expectedSeconds, expectedNanoOfSecond);
        }

        @Test
        public void overflowWhenSubtractingNanosPastMin() {
            TaiInstant i = tai(Long.MIN_VALUE, 0);
            assertThrows(ArithmeticException.class, () -> i.minus(ofSeconds(0, 1)));
        }

        @Test
        public void overflowWhenSubtractingNegativeDurationPastMax() {
            TaiInstant i = tai(Long.MAX_VALUE, 999_999_999);
            assertThrows(ArithmeticException.class, () -> i.minus(ofSeconds(-1, 999_999_999)));
        }
    }

    @Nested
    @DisplayName("Duration between instants")
    class DurationUntilTests {
        @Test
        public void fifteenSeconds() {
            Duration d = tai(10, 0).durationUntil(tai(25, 0));
            assertEquals(15, d.getSeconds());
            assertEquals(0, d.getNano());
        }

        @Test
        public void twoNanosPositive() {
            Duration d = tai(4, 5).durationUntil(tai(4, 7));
            assertEquals(0, d.getSeconds());
            assertEquals(2, d.getNano());
        }

        @Test
        public void twoNanosNegativeNormalized() {
            Duration d = tai(4, 9).durationUntil(tai(4, 7));
            assertEquals(-1, d.getSeconds());
            assertEquals(999_999_998, d.getNano());
        }
    }

    @Nested
    @DisplayName("Conversions")
    class ConversionTests {

        @Test
        public void toUtcInstantRoundTripShape() {
            for (int dayOffset = -1000; dayOffset < 1000; dayOffset++) {
                for (int secondOfDay = 0; secondOfDay < 10; secondOfDay++) {
                    UtcInstant expected = UtcInstant.ofModifiedJulianDay(
                        MJD_TAI_EPOCH + dayOffset,
                        secondOfDay * (long) NANOS_PER_SECOND + 2L
                    );
                    TaiInstant tai = tai(dayOffset * SECONDS_PER_DAY + secondOfDay + TAI_MINUS_UTC_AT_UNIX_EPOCH_SECONDS, 2);
                    assertEquals(expected, tai.toUtcInstant());
                }
            }
        }

        @Test
        public void toInstantUsesExpectedEpochOffset() {
            for (int dayOffset = -1000; dayOffset < 1000; dayOffset++) {
                for (int secondOfDay = 0; secondOfDay < 10; secondOfDay++) {
                    // Expected Instant epoch seconds: Unix epoch is 378,691,200 seconds after TAI epoch (negative when going from TAI to Instant)
                    long baseEpochSecond = -SECONDS_BETWEEN_TAI_AND_UNIX_EPOCHS + dayOffset * SECONDS_PER_DAY + secondOfDay;
                    Instant expected = Instant.ofEpochSecond(baseEpochSecond).plusNanos(2);

                    TaiInstant tai = tai(dayOffset * SECONDS_PER_DAY + secondOfDay + TAI_MINUS_UTC_AT_UNIX_EPOCH_SECONDS, 2);
                    assertEquals(expected, tai.toInstant());
                }
            }
        }
    }

    @Nested
    @DisplayName("Comparison")
    class ComparisonTests {

        @Test
        public void orderingAndEquality() {
            assertOrdering(
                tai(-2, 0),
                tai(-2, 999_999_998),
                tai(-2, 999_999_999),
                tai(-1, 0),
                tai(-1, 1),
                tai(-1, 999_999_998),
                tai(-1, 999_999_999),
                tai(0, 0),
                tai(0, 1),
                tai(0, 2),
                tai(0, 999_999_999),
                tai(1, 0),
                tai(2, 0)
            );
        }

        private void assertOrdering(TaiInstant... instants) {
            for (int i = 0; i < instants.length; i++) {
                TaiInstant a = instants[i];
                for (int j = 0; j < instants.length; j++) {
                    TaiInstant b = instants[j];
                    if (i < j) {
                        assertTrue(a.compareTo(b) < 0);
                        assertFalse(a.equals(b));
                        assertTrue(a.isBefore(b));
                        assertFalse(a.isAfter(b));
                    } else if (i > j) {
                        assertTrue(a.compareTo(b) > 0);
                        assertFalse(a.equals(b));
                        assertFalse(a.isBefore(b));
                        assertTrue(a.isAfter(b));
                    } else {
                        assertEquals(0, a.compareTo(b));
                        assertTrue(a.equals(b));
                        assertFalse(a.isBefore(b));
                        assertFalse(a.isAfter(b));
                    }
                }
            }
        }

        @Test
        public void compareToNullIsRejected() {
            TaiInstant a = tai(0, 0);
            assertThrows(NullPointerException.class, () -> a.compareTo(null));
        }

        @Test
        @SuppressWarnings({"unchecked", "rawtypes"})
        public void compareToNonTaiInstantIsRejected() {
            Comparable c = tai(0, 2);
            assertThrows(ClassCastException.class, () -> c.compareTo(new Object()));
        }
    }

    @Nested
    @DisplayName("equals and hashCode")
    class EqualityTests {
        @Test
        public void equalityGroups() {
            new EqualsTester()
                .addEqualityGroup(tai(5, 20), tai(5, 20))
                .addEqualityGroup(tai(5, 30), tai(5, 30))
                .addEqualityGroup(tai(6, 20), tai(6, 20))
                .testEquals();
        }
    }

    @Nested
    @DisplayName("toString")
    class ToStringTests {
        @Test
        public void standard() {
            assertEquals("123.123456789s(TAI)", tai(123L, 123_456_789).toString());
        }

        @Test
        public void negativeSeconds() {
            assertEquals("-123.123456789s(TAI)", tai(-123L, 123_456_789).toString());
        }

        @Test
        public void leftPadNanosToNineDigits() {
            assertEquals("0.000000567s(TAI)", tai(0L, 567).toString());
        }
    }
}