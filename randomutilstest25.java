package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("RandomUtils Test Suite")
class RandomUtilsTest extends AbstractLangTest {

    /**
     * For comparing doubles and floats.
     */
    private static final double DELTA = 1e-5;

    // This provider is used by all parameterized tests in the nested classes.
    static Stream<RandomUtils> randomProvider() {
        return Stream.of(RandomUtils.secure(), RandomUtils.secureStrong(), RandomUtils.insecure());
    }

    @Nested
    @DisplayName("randomBoolean()")
    class RandomBooleanTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a boolean without error")
        void shouldReturnBooleanWithoutError(final RandomUtils randomUtils) {
            // The primary goal is to ensure this method executes without throwing an exception.
            // Asserting the result is either true or false is a tautology.
            randomUtils.randomBoolean();
        }
    }

    @Nested
    @DisplayName("randomBytes(int)")
    class RandomBytesTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return an array of the correct size for a positive count")
        void shouldReturnArrayOfCorrectSize(final RandomUtils randomUtils) {
            final byte[] result = randomUtils.randomBytes(20);
            assertEquals(20, result.length);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return an empty array for a zero count")
        void shouldReturnEmptyArrayForZeroCount(final RandomUtils randomUtils) {
            assertArrayEquals(new byte[0], randomUtils.randomBytes(0));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw an exception for a negative count")
        void shouldThrowExceptionForNegativeCount(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomBytes(-1));
        }
    }

    @Nested
    @DisplayName("randomDouble(...)")
    class RandomDoubleTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a value within the default range [0, Double.MAX_VALUE)")
        void shouldReturnDoubleInDefaultRange(final RandomUtils randomUtils) {
            final double result = randomUtils.randomDouble();
            assertTrue(result >= 0d && result < Double.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a value within the specified range")
        void shouldReturnDoubleInSpecifiedRange(final RandomUtils randomUtils) {
            final double result = randomUtils.randomDouble(33d, 42d);
            assertTrue(result >= 33d && result < 42d);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return the start value when the range is zero")
        void shouldReturnStartValueForMinimalRange(final RandomUtils randomUtils) {
            assertEquals(42.1, randomUtils.randomDouble(42.1, 42.1), DELTA);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should handle the extreme range [0, Double.MAX_VALUE)")
        void shouldHandleExtremeRange(final RandomUtils randomUtils) {
            final double result = randomUtils.randomDouble(0, Double.MAX_VALUE);
            assertTrue(result >= 0 && result < Double.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw an exception when start is negative")
        void shouldThrowExceptionForNegativeStart(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomDouble(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw an exception when start > end")
        void shouldThrowExceptionForInvalidRange(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomDouble(2, 1));
        }
    }

    @Nested
    @DisplayName("randomFloat(...)")
    class RandomFloatTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a value within the default range [0, Float.MAX_VALUE)")
        void shouldReturnFloatInDefaultRange(final RandomUtils randomUtils) {
            final float result = randomUtils.randomFloat();
            assertTrue(result >= 0f && result < Float.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a value within the specified range")
        void shouldReturnFloatInSpecifiedRange(final RandomUtils randomUtils) {
            final float result = randomUtils.randomFloat(33f, 42f);
            assertTrue(result >= 33f && result < 42f);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return the start value when the range is zero")
        void shouldReturnStartValueForMinimalRange(final RandomUtils randomUtils) {
            assertEquals(42.1f, randomUtils.randomFloat(42.1f, 42.1f), DELTA);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should handle the extreme range [0, Float.MAX_VALUE)")
        void shouldHandleExtremeRange(final RandomUtils randomUtils) {
            final float result = randomUtils.randomFloat(0, Float.MAX_VALUE);
            assertTrue(result >= 0f && result < Float.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw an exception when start is negative")
        void shouldThrowExceptionForNegativeStart(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomFloat(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw an exception when start > end")
        void shouldThrowExceptionForInvalidRange(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomFloat(2, 1));
        }
    }

    @Nested
    @DisplayName("randomInt(...)")
    class RandomIntTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a value within the default range [0, Integer.MAX_VALUE)")
        void shouldReturnIntInDefaultRange(final RandomUtils randomUtils) {
            final int result = randomUtils.randomInt();
            assertTrue(result >= 0 && result < Integer.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a value within the specified range")
        void shouldReturnIntInSpecifiedRange(final RandomUtils randomUtils) {
            final int result = randomUtils.randomInt(33, 42);
            assertTrue(result >= 33 && result < 42);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return the start value when the range is zero")
        void shouldReturnStartValueForMinimalRange(final RandomUtils randomUtils) {
            assertEquals(42, randomUtils.randomInt(42, 42));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should handle the extreme range [0, Integer.MAX_VALUE)")
        void shouldHandleExtremeRange(final RandomUtils randomUtils) {
            final int result = randomUtils.randomInt(0, Integer.MAX_VALUE);
            assertTrue(result >= 0 && result < Integer.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw an exception when start is negative")
        void shouldThrowExceptionForNegativeStart(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomInt(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw an exception when start > end")
        void shouldThrowExceptionForInvalidRange(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomInt(2, 1));
        }
    }

    @Nested
    @DisplayName("randomLong(...)")
    class RandomLongTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a value within the default range [0, Long.MAX_VALUE)")
        void shouldReturnLongInDefaultRange(final RandomUtils randomUtils) {
            final long result = randomUtils.randomLong();
            assertTrue(result >= 0L && result < Long.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a value within the specified range")
        void shouldReturnLongInSpecifiedRange(final RandomUtils randomUtils) {
            final long result = randomUtils.randomLong(33L, 42L);
            assertTrue(result >= 33L && result < 42L);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return the start value when the range is zero")
        void shouldReturnStartValueForMinimalRange(final RandomUtils randomUtils) {
            assertEquals(42L, randomUtils.randomLong(42L, 42L));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should handle the extreme range [0, Long.MAX_VALUE)")
        void shouldHandleExtremeRange(final RandomUtils randomUtils) {
            final long result = randomUtils.randomLong(0, Long.MAX_VALUE);
            assertTrue(result >= 0 && result < Long.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw an exception when start is negative")
        void shouldThrowExceptionForNegativeStart(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomLong(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw an exception when start > end")
        void shouldThrowExceptionForInvalidRange(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomLong(2, 1));
        }

        /**
         * Test for LANG-1592, where a previous implementation using
         * {@code (long) nextDouble(startInclusive, endExclusive)} could incorrectly
         * generate a value equal to the exclusive upper bound.
         */
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should not generate the exclusive upper bound on large ranges (LANG-1592)")
        void shouldAdhereToExclusiveUpperBoundForLargeValues(final RandomUtils randomUtils) {
            final long startInclusive = 12900000000001L;
            final long endExclusive = 12900000000016L;
            // The previous buggy implementation failed frequently with this many iterations.
            final int iterations = (int) (endExclusive - startInclusive) * 1000;
            for (int i = 0; i < iterations; i++) {
                final long result = randomUtils.randomLong(startInclusive, endExclusive);
                assertNotEquals(endExclusive, result, "Generated value should not equal the exclusive upper bound");
            }
        }
    }
}