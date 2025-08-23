package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Tests for RandomUtils")
public class RandomUtilsTest extends AbstractLangTest {

    /**
     * A small delta for comparing floating-point numbers.
     */
    private static final double DELTA = 1e-5;

    /**
     * Provides streams of different RandomUtils instances (secure, secure-strong, insecure) for parameterized tests.
     */
    static Stream<RandomUtils> randomProvider() {
        return Stream.of(RandomUtils.secure(), RandomUtils.secureStrong(), RandomUtils.insecure());
    }

    @Nested
    @DisplayName("randomBoolean()")
    class RandomBooleanTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldReturnTrueOrFalse(final RandomUtils ru) {
            final boolean result = ru.randomBoolean();
            // This assertion is a tautology but confirms the method returns a boolean without throwing an exception.
            assertTrue(result || !result, "Result must be either true or false.");
        }
    }

    @Nested
    @DisplayName("randomBytes(int)")
    class RandomBytesTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withPositiveCount_shouldReturnArrayOfCorrectSize(final RandomUtils ru) {
            final byte[] result = ru.randomBytes(20);
            assertNotNull(result);
            assertEquals(20, result.length);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withZeroCount_shouldReturnEmptyArray(final RandomUtils ru) {
            final byte[] result = ru.randomBytes(0);
            assertNotNull(result);
            assertArrayEquals(new byte[0], result);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withNegativeCount_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomBytes(-1));
        }
    }

    @Nested
    @DisplayName("randomDouble()")
    class RandomDoubleTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withoutArgs_shouldReturnDoubleWithinDefaultRange(final RandomUtils ru) {
            final double result = ru.randomDouble();
            assertTrue(result >= 0d);
            assertTrue(result < Double.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withValidRange_shouldReturnDoubleWithinRange(final RandomUtils ru) {
            final double result = ru.randomDouble(33d, 42d);
            assertTrue(result >= 33d);
            assertTrue(result < 42d);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withMinimalRange_shouldReturnStartValue(final RandomUtils ru) {
            assertEquals(42.1, ru.randomDouble(42.1, 42.1), DELTA);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withExtremeRange_shouldReturnDoubleWithinRange(final RandomUtils ru) {
            final double result = ru.randomDouble(0, Double.MAX_VALUE);
            assertTrue(result >= 0);
            assertTrue(result < Double.MAX_VALUE, "Result should be less than Double.MAX_VALUE (exclusive bound)");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withNegativeStart_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomDouble(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withStartGreaterThanEnd_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomDouble(2, 1));
        }
    }

    @Nested
    @DisplayName("randomFloat()")
    class RandomFloatTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withoutArgs_shouldReturnFloatWithinDefaultRange(final RandomUtils ru) {
            final float result = ru.randomFloat();
            assertTrue(result >= 0f);
            assertTrue(result < Float.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withValidRange_shouldReturnFloatWithinRange(final RandomUtils ru) {
            final float result = ru.randomFloat(33f, 42f);
            assertTrue(result >= 33f);
            assertTrue(result < 42f);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withMinimalRange_shouldReturnStartValue(final RandomUtils ru) {
            assertEquals(42.1f, ru.randomFloat(42.1f, 42.1f), DELTA);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withExtremeRange_shouldReturnFloatWithinRange(final RandomUtils ru) {
            final float result = ru.randomFloat(0, Float.MAX_VALUE);
            assertTrue(result >= 0f);
            assertTrue(result < Float.MAX_VALUE, "Result should be less than Float.MAX_VALUE (exclusive bound)");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withNegativeStart_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomFloat(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withStartGreaterThanEnd_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomFloat(2, 1));
        }
    }

    @Nested
    @DisplayName("randomInt()")
    class RandomIntTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withoutArgs_shouldReturnIntWithinDefaultRange(final RandomUtils ru) {
            final int result = ru.randomInt();
            assertTrue(result >= 0, "Result should be non-negative.");
            assertTrue(result < Integer.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withValidRange_shouldReturnIntWithinRange(final RandomUtils ru) {
            final int result = ru.randomInt(33, 42);
            assertTrue(result >= 33);
            assertTrue(result < 42);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withMinimalRange_shouldReturnStartValue(final RandomUtils ru) {
            assertEquals(42, ru.randomInt(42, 42));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withExtremeRange_shouldReturnIntWithinRange(final RandomUtils ru) {
            final int result = ru.randomInt(0, Integer.MAX_VALUE);
            assertTrue(result >= 0);
            assertTrue(result < Integer.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withNegativeStart_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomInt(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withStartGreaterThanEnd_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomInt(2, 1));
        }
    }

    @Nested
    @DisplayName("randomLong()")
    class RandomLongTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withoutArgs_shouldReturnLongWithinDefaultRange(final RandomUtils ru) {
            final long result = ru.randomLong();
            assertTrue(result >= 0L);
            assertTrue(result < Long.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withValidRange_shouldReturnLongWithinRange(final RandomUtils ru) {
            final long result = ru.randomLong(33L, 42L);
            assertTrue(result >= 33L);
            assertTrue(result < 42L);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withMinimalRange_shouldReturnStartValue(final RandomUtils ru) {
            assertEquals(42L, ru.randomLong(42L, 42L));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withExtremeRange_shouldReturnLongWithinRange(final RandomUtils ru) {
            final long result = ru.randomLong(0, Long.MAX_VALUE);
            assertTrue(result >= 0);
            assertTrue(result < Long.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withNegativeStart_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomLong(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withStartGreaterThanEnd_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomLong(2, 1));
        }

        /**
         * Tests a bug (LANG-1592) where a previous implementation using
         * {@code (long) nextDouble(startInclusive, endExclusive)} could incorrectly
         * generate a value equal to the exclusive upper bound.
         */
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withLargeValueRange_shouldNotReturnUpperBound(final RandomUtils ru) {
            final long startInclusive = 12900000000001L;
            final long endExclusive = 12900000000016L;
            // The previous buggy method failed frequently with this many iterations.
            final int iterations = (int) (endExclusive - startInclusive) * 1000;
            for (int i = 0; i < iterations; i++) {
                assertNotEquals(endExclusive, ru.randomLong(startInclusive, endExclusive),
                    "Result should not equal the exclusive upper bound.");
            }
        }
    }

    @Nested
    @DisplayName("Tests for deprecated static methods")
    class DeprecatedStaticMethodsTest {
        /**
         * This test is for the deprecated static method {@link RandomUtils#nextFloat(float, float)}.
         * It is kept for backward compatibility testing.
         */
        @Test
        @SuppressWarnings("deprecation")
        void nextFloat_withExtremeRange_shouldReturnFloatWithinRange() {
            final float result = RandomUtils.nextFloat(0, Float.MAX_VALUE);
            assertTrue(result >= 0f);
            assertTrue(result < Float.MAX_VALUE, "Result should be less than Float.MAX_VALUE (exclusive bound)");
        }
    }
}