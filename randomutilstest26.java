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
public class RandomUtilsTest extends AbstractLangTest {

    /**
     * For comparing doubles and floats.
     */
    private static final double DELTA = 1e-5;

    /**
     * Provides instances of secure, strong secure, and insecure RandomUtils.
     */
    static Stream<RandomUtils> randomProvider() {
        return Stream.of(RandomUtils.secure(), RandomUtils.secureStrong(), RandomUtils.insecure());
    }

    @Nested
    @DisplayName("randomBoolean()")
    class RandomBooleanTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a boolean without throwing an exception")
        void shouldReturnBoolean(final RandomUtils ru) {
            // This test just ensures the method can be called without error.
            // A true/false result is a tautology and doesn't test for randomness.
            final boolean result = ru.randomBoolean();
            assertTrue(result || !result, "A boolean value must be either true or false.");
        }
    }

    @Nested
    @DisplayName("randomBytes(count)")
    class RandomBytesTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a byte array of the specified size")
        void shouldReturnArrayOfCorrectSize(final RandomUtils ru) {
            final byte[] result = ru.randomBytes(20);
            assertEquals(20, result.length);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return an empty array when count is zero")
        void shouldReturnEmptyArrayForZeroCount(final RandomUtils ru) {
            assertArrayEquals(new byte[0], ru.randomBytes(0));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw IllegalArgumentException when count is negative")
        void shouldThrowExceptionForNegativeCount(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomBytes(-1));
        }
    }

    @Nested
    @DisplayName("randomDouble()")
    class RandomDoubleTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a double within the default range [0, Double.MAX_VALUE)")
        void shouldReturnDoubleInDefaultRange(final RandomUtils ru) {
            final double result = ru.randomDouble();
            assertTrue(result >= 0d && result < Double.MAX_VALUE);
        }
    }

    @Nested
    @DisplayName("randomDouble(startInclusive, endExclusive)")
    class RandomDoubleRangeTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a double within the specified range")
        void shouldReturnDoubleInSpecifiedRange(final RandomUtils ru) {
            final double result = ru.randomDouble(33d, 42d);
            assertTrue(result >= 33d && result < 42d);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should handle a minimal range where start equals end")
        void shouldHandleMinimalRange(final RandomUtils ru) {
            assertEquals(42.1, ru.randomDouble(42.1, 42.1), DELTA);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should handle the extreme range [0, Double.MAX_VALUE)")
        void shouldHandleExtremeRange(final RandomUtils ru) {
            final double result = ru.randomDouble(0, Double.MAX_VALUE);
            assertTrue(result >= 0 && result < Double.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw IllegalArgumentException when start > end")
        void shouldThrowExceptionForInvalidRange(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomDouble(2, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw IllegalArgumentException when start is negative")
        void shouldThrowExceptionForNegativeStart(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomDouble(-1, 1));
        }
    }

    @Nested
    @DisplayName("randomFloat()")
    class RandomFloatTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a float within the default range [0, Float.MAX_VALUE)")
        void shouldReturnFloatInDefaultRange(final RandomUtils ru) {
            final float result = ru.randomFloat();
            assertTrue(result >= 0f && result < Float.MAX_VALUE);
        }
    }

    @Nested
    @DisplayName("randomFloat(startInclusive, endExclusive)")
    class RandomFloatRangeTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a float within the specified range")
        void shouldReturnFloatInSpecifiedRange(final RandomUtils ru) {
            final float result = ru.randomFloat(33f, 42f);
            assertTrue(result >= 33f && result < 42f);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should handle a minimal range where start equals end")
        void shouldHandleMinimalRange(final RandomUtils ru) {
            assertEquals(42.1f, ru.randomFloat(42.1f, 42.1f), DELTA);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should handle the extreme range [0, Float.MAX_VALUE)")
        void shouldHandleExtremeRange(final RandomUtils ru) {
            final float result = ru.randomFloat(0, Float.MAX_VALUE);
            assertTrue(result >= 0f && result < Float.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw IllegalArgumentException when start > end")
        void shouldThrowExceptionForInvalidRange(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomFloat(2, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw IllegalArgumentException when start is negative")
        void shouldThrowExceptionForNegativeStart(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomFloat(-1, 1));
        }
    }

    @Nested
    @DisplayName("randomInt()")
    class RandomIntTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return an int within the default range [0, Integer.MAX_VALUE)")
        void shouldReturnIntInDefaultRange(final RandomUtils ru) {
            final int result = ru.randomInt();
            assertTrue(result >= 0 && result < Integer.MAX_VALUE);
        }
    }

    @Nested
    @DisplayName("randomInt(startInclusive, endExclusive)")
    class RandomIntRangeTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return an int within the specified range")
        void shouldReturnIntInSpecifiedRange(final RandomUtils ru) {
            final int result = ru.randomInt(33, 42);
            assertTrue(result >= 33 && result < 42);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should handle a minimal range where start equals end")
        void shouldHandleMinimalRange(final RandomUtils ru) {
            assertEquals(42, ru.randomInt(42, 42));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should handle the extreme range [0, Integer.MAX_VALUE)")
        void shouldHandleExtremeRange(final RandomUtils ru) {
            final int result = ru.randomInt(0, Integer.MAX_VALUE);
            assertTrue(result >= 0 && result < Integer.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw IllegalArgumentException when start > end")
        void shouldThrowExceptionForInvalidRange(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomInt(2, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw IllegalArgumentException when start is negative")
        void shouldThrowExceptionForNegativeStart(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomInt(-1, 1));
        }
    }

    @Nested
    @DisplayName("randomLong()")
    class RandomLongTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a long within the default range [0, Long.MAX_VALUE)")
        void shouldReturnLongInDefaultRange(final RandomUtils ru) {
            final long result = ru.randomLong();
            assertTrue(result >= 0L && result < Long.MAX_VALUE);
        }
    }

    @Nested
    @DisplayName("randomLong(startInclusive, endExclusive)")
    class RandomLongRangeTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a long within the specified range")
        void shouldReturnLongInSpecifiedRange(final RandomUtils ru) {
            final long result = ru.randomLong(33L, 42L);
            assertTrue(result >= 33L && result < 42L);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should handle a minimal range where start equals end")
        void shouldHandleMinimalRange(final RandomUtils ru) {
            assertEquals(42L, ru.randomLong(42L, 42L));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should handle the extreme range [0, Long.MAX_VALUE)")
        void shouldHandleExtremeRange(final RandomUtils ru) {
            final long result = ru.randomLong(0, Long.MAX_VALUE);
            assertTrue(result >= 0 && result < Long.MAX_VALUE);
        }

        /**
         * Regression test for LANG-1592, where a previous implementation using
         * {@code (long) nextDouble(startInclusive, endExclusive)} could incorrectly
         * generate a value equal to the exclusive upper bound.
         */
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should not return the exclusive end for a large value range (LANG-1592)")
        void shouldNotReturnEndExclusiveForLargeValueRange(final RandomUtils ru) {
            final long startInclusive = 12900000000001L;
            final long endExclusive = 12900000000016L;
            // The previous faulty method failed frequently with this many iterations.
            final int n = (int) (endExclusive - startInclusive) * 1000;
            for (int i = 0; i < n; i++) {
                assertNotEquals(endExclusive, ru.randomLong(startInclusive, endExclusive));
            }
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw IllegalArgumentException when start > end")
        void shouldThrowExceptionForInvalidRange(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomLong(2, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw IllegalArgumentException when start is negative")
        void shouldThrowExceptionForNegativeStart(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomLong(-1, 1));
        }
    }

    @Nested
    @DisplayName("Deprecated Static API")
    class DeprecatedStaticApiTest {
        @Test
        @DisplayName("nextLong(start, end) should throw IllegalArgumentException when start > end")
        void staticNextLong_shouldThrowExceptionForInvalidRange() {
            assertIllegalArgumentException(() -> RandomUtils.nextLong(2, 1));
        }
    }
}