package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Tests for RandomUtils")
class RandomUtilsTest extends AbstractLangTest {

    /**
     * A delta for comparing double and float values.
     */
    private static final double DELTA = 1e-5;

    /**
     * Provides instances of secure, strong secure, and insecure RandomUtils.
     */
    static Stream<RandomUtils> randomProvider() {
        return Stream.of(RandomUtils.secure(), RandomUtils.secureStrong(), RandomUtils.insecure());
    }

    @Nested
    @DisplayName("Boolean Generation")
    class BooleanTests {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void randomBoolean_shouldNotFail(final RandomUtils ru) {
            // This test simply ensures the method executes without throwing an exception.
            // A deterministic assertion for a random result is not practical.
            ru.randomBoolean();
        }
    }

    @Nested
    @DisplayName("Byte Array Generation")
    class BytesTests {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void randomBytes_shouldReturnArrayOfGivenSize(final RandomUtils ru) {
            final byte[] result = ru.randomBytes(20);
            assertEquals(20, result.length);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void randomBytes_withNegativeCount_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomBytes(-1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void randomBytes_withZeroCount_shouldReturnEmptyArray(final RandomUtils ru) {
            assertArrayEquals(new byte[0], ru.randomBytes(0));
        }
    }

    @Nested
    @DisplayName("Double Generation")
    class DoubleTests {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void randomDouble_withoutArgs_shouldReturnNumberWithinDefaultRange(final RandomUtils ru) {
            final double result = ru.randomDouble();
            assertTrue(result >= 0d);
            assertTrue(result < Double.MAX_VALUE, "Result should be less than Double.MAX_VALUE");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void randomDouble_inRange_shouldReturnNumberWithinRange(final RandomUtils ru) {
            final double result = ru.randomDouble(33d, 42d);
            assertTrue(result >= 33d);
            assertTrue(result < 42d);
        }
        
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void randomDouble_withMaximumRange_shouldReturnFiniteValueWithinRange(final RandomUtils ru) {
            final double result = ru.randomDouble(0, Double.MAX_VALUE);
            assertTrue(result >= 0 && result < Double.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void randomDouble_withStartEqualToEnd_shouldReturnStart(final RandomUtils ru) {
            assertEquals(42.1, ru.randomDouble(42.1, 42.1), DELTA);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void randomDouble_withNegativeStart_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomDouble(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void randomDouble_withStartGreaterThanEnd_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomDouble(2, 1));
        }
    }

    @Nested
    @DisplayName("Float Generation")
    class FloatTests {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void randomFloat_withoutArgs_shouldReturnNumberWithinDefaultRange(final RandomUtils ru) {
            final float result = ru.randomFloat();
            assertTrue(result >= 0f);
            assertTrue(result < Float.MAX_VALUE, "Result should be less than Float.MAX_VALUE");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void randomFloat_inRange_shouldReturnNumberWithinRange(final RandomUtils ru) {
            final float result = ru.randomFloat(33f, 42f);
            assertTrue(result >= 33f);
            assertTrue(result < 42f);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void randomFloat_withMaximumRange_shouldReturnFiniteValueWithinRange(final RandomUtils ru) {
            final float result = ru.randomFloat(0, Float.MAX_VALUE);
            assertTrue(result >= 0f && result < Float.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void randomFloat_withStartEqualToEnd_shouldReturnStart(final RandomUtils ru) {
            assertEquals(42.1f, ru.randomFloat(42.1f, 42.1f), DELTA);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void randomFloat_withNegativeStart_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomFloat(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void randomFloat_withStartGreaterThanEnd_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomFloat(2, 1));
        }
    }

    @Nested
    @DisplayName("Integer Generation")
    class IntTests {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void randomInt_withoutArgs_shouldReturnPositiveNumber(final RandomUtils ru) {
            final int result = ru.randomInt();
            assertTrue(result >= 0);
            assertTrue(result < Integer.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void randomInt_inRange_shouldReturnNumberWithinRange(final RandomUtils ru) {
            final int result = ru.randomInt(33, 42);
            assertTrue(result >= 33);
            assertTrue(result < 42);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void randomInt_withMaximumRange_shouldReturnNumberWithinRange(final RandomUtils ru) {
            final int result = ru.randomInt(0, Integer.MAX_VALUE);
            assertTrue(result >= 0);
            assertTrue(result < Integer.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void randomInt_withStartEqualToEnd_shouldReturnStart(final RandomUtils ru) {
            assertEquals(42, ru.randomInt(42, 42));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void randomInt_withNegativeStart_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomInt(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void randomInt_withStartGreaterThanEnd_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomInt(2, 1));
        }
    }

    @Nested
    @DisplayName("Long Generation")
    class LongTests {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void randomLong_withoutArgs_shouldReturnNumberWithinDefaultRange(final RandomUtils ru) {
            final long result = ru.randomLong();
            assertTrue(result >= 0L);
            assertTrue(result < Long.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void randomLong_inRange_shouldReturnNumberWithinRange(final RandomUtils ru) {
            final long result = ru.randomLong(33L, 42L);
            assertTrue(result >= 33L);
            assertTrue(result < 42L);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void randomLong_withMaximumRange_shouldReturnNumberWithinRange(final RandomUtils ru) {
            final long result = ru.randomLong(0, Long.MAX_VALUE);
            assertTrue(result >= 0);
            assertTrue(result < Long.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void randomLong_withStartEqualToEnd_shouldReturnStart(final RandomUtils ru) {
            assertEquals(42L, ru.randomLong(42L, 42L));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void randomLong_withNegativeStart_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomLong(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void randomLong_withStartGreaterThanEnd_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomLong(2, 1));
        }

        /**
         * <p>Tests a bug (LANG-1592) where a previous implementation using
         * {@code (long) nextDouble(startInclusive, endExclusive)} could incorrectly
         * generate a value equal to the exclusive upper bound.</p>
         *
         * <p>This test calls the method many times over a small range of large numbers
         * to increase the probability of hitting the edge case.</p>
         */
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void randomLong_forLargeValueRange_shouldNotReturnEndExclusiveValue(final RandomUtils ru) {
            final long startInclusive = 12_900_000_000_001L;
            final long endExclusive = 12_900_000_000_016L;
            final int loopCount = (int) (endExclusive - startInclusive) * 1000;

            for (int i = 0; i < loopCount; i++) {
                assertNotEquals(endExclusive, ru.randomLong(startInclusive, endExclusive));
            }
        }
    }
}