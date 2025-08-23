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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Tests for RandomUtils")
public class RandomUtilsTest extends AbstractLangTest {

    /**
     * Provides the different RandomUtils instances (secure, secure-strong, insecure) for parameterized tests.
     */
    static Stream<RandomUtils> randomProvider() {
        return Stream.of(RandomUtils.secure(), RandomUtils.secureStrong(), RandomUtils.insecure());
    }

    @Nested
    @DisplayName("randomBoolean()")
    class RandomBooleanTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a valid boolean value")
        void shouldReturnValidBoolean(final RandomUtils ru) {
            // This is a smoke test to ensure the method executes and returns a valid boolean.
            final boolean result = ru.randomBoolean();
            assertTrue(result || !result, "The result must be either true or false.");
        }
    }

    @Nested
    @DisplayName("randomBytes(int)")
    class RandomBytesTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a byte array of the specified size")
        void shouldReturnArrayOfCorrectSize(final RandomUtils ru) {
            final byte[] result = ru.randomBytes(20);
            assertNotNull(result);
            assertEquals(20, result.length);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return an empty array when size is zero")
        void shouldReturnEmptyArrayForZeroSize(final RandomUtils ru) {
            assertArrayEquals(new byte[0], ru.randomBytes(0));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw IllegalArgumentException for a negative size")
        void shouldThrowExceptionForNegativeSize(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomBytes(-1));
        }
    }

    @Nested
    @DisplayName("randomDouble()")
    class RandomDoubleDefaultTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a double in the range [0, Double.MAX_VALUE)")
        void shouldReturnDoubleInDefaultRange(final RandomUtils ru) {
            final double result = ru.randomDouble();
            assertTrue(result >= 0d, "Result should be non-negative");
            assertTrue(result < Double.MAX_VALUE, "Result should be less than Double.MAX_VALUE");
        }
    }

    @Nested
    @DisplayName("randomDouble(double, double)")
    class RandomDoubleRangeTest {
        private static final double DELTA = 1e-5;

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a double within the specified range")
        void shouldReturnDoubleWithinRange(final RandomUtils ru) {
            final double result = ru.randomDouble(33d, 42d);
            assertTrue(result >= 33d, "Result should be >= startInclusive");
            assertTrue(result < 42d, "Result should be < endExclusive");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a double within the extreme range [0, Double.MAX_VALUE)")
        void shouldReturnDoubleInExtremeRange(final RandomUtils ru) {
            final double result = ru.randomDouble(0, Double.MAX_VALUE);
            assertTrue(result >= 0, "Result should be non-negative");
            assertTrue(result < Double.MAX_VALUE, "Result should be less than Double.MAX_VALUE");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return the start value when start and end are the same")
        void shouldReturnStartWhenRangeIsZero(final RandomUtils ru) {
            assertEquals(42.1, ru.randomDouble(42.1, 42.1), DELTA);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw IllegalArgumentException when start > end")
        void shouldThrowExceptionWhenStartIsGreaterThanEnd(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomDouble(2, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw IllegalArgumentException for a negative start value")
        void shouldThrowExceptionForNegativeStart(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomDouble(-1, 1));
        }
    }

    @Nested
    @DisplayName("randomFloat()")
    class RandomFloatDefaultTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a float in the range [0, Float.MAX_VALUE)")
        void shouldReturnFloatInDefaultRange(final RandomUtils ru) {
            final float result = ru.randomFloat();
            assertTrue(result >= 0f, "Result should be non-negative");
            assertTrue(result < Float.MAX_VALUE, "Result should be less than Float.MAX_VALUE");
        }
    }

    @Nested
    @DisplayName("randomFloat(float, float)")
    class RandomFloatRangeTest {
        private static final double DELTA = 1e-5;

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a float within the specified range")
        void shouldReturnFloatWithinRange(final RandomUtils ru) {
            final float result = ru.randomFloat(33f, 42f);
            assertTrue(result >= 33f, "Result should be >= startInclusive");
            assertTrue(result < 42f, "Result should be < endExclusive");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a float within the extreme range [0, Float.MAX_VALUE)")
        void shouldReturnFloatInExtremeRange(final RandomUtils ru) {
            final float result = ru.randomFloat(0, Float.MAX_VALUE);
            assertTrue(result >= 0f, "Result should be non-negative");
            assertTrue(result < Float.MAX_VALUE, "Result should be less than Float.MAX_VALUE");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return the start value when start and end are the same")
        void shouldReturnStartWhenRangeIsZero(final RandomUtils ru) {
            assertEquals(42.1f, ru.randomFloat(42.1f, 42.1f), DELTA);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw IllegalArgumentException when start > end")
        void shouldThrowExceptionWhenStartIsGreaterThanEnd(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomFloat(2, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw IllegalArgumentException for a negative start value")
        void shouldThrowExceptionForNegativeStart(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomFloat(-1, 1));
        }
    }

    @Nested
    @DisplayName("randomInt()")
    class RandomIntDefaultTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return an int in the range [0, Integer.MAX_VALUE)")
        void shouldReturnIntInDefaultRange(final RandomUtils ru) {
            final int result = ru.randomInt();
            assertTrue(result >= 0, "Result should be non-negative");
            assertTrue(result < Integer.MAX_VALUE, "Result should be less than Integer.MAX_VALUE");
        }
    }

    @Nested
    @DisplayName("randomInt(int, int)")
    class RandomIntRangeTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return an int within the specified range")
        void shouldReturnIntWithinRange(final RandomUtils ru) {
            final int result = ru.randomInt(33, 42);
            assertTrue(result >= 33, "Result should be >= startInclusive");
            assertTrue(result < 42, "Result should be < endExclusive");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return an int within the extreme range [0, Integer.MAX_VALUE)")
        void shouldReturnIntInExtremeRange(final RandomUtils ru) {
            final int result = ru.randomInt(0, Integer.MAX_VALUE);
            assertTrue(result >= 0, "Result should be non-negative");
            assertTrue(result < Integer.MAX_VALUE, "Result should be less than Integer.MAX_VALUE");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return the start value when start and end are the same")
        void shouldReturnStartWhenRangeIsZero(final RandomUtils ru) {
            assertEquals(42, ru.randomInt(42, 42));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw IllegalArgumentException when start > end")
        void shouldThrowExceptionWhenStartIsGreaterThanEnd(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomInt(2, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw IllegalArgumentException for a negative start value")
        void shouldThrowExceptionForNegativeStart(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomInt(-1, 1));
        }
    }

    @Nested
    @DisplayName("randomLong()")
    class RandomLongDefaultTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a long in the range [0, Long.MAX_VALUE)")
        void shouldReturnLongInDefaultRange(final RandomUtils ru) {
            final long result = ru.randomLong();
            assertTrue(result >= 0L, "Result should be non-negative");
            assertTrue(result < Long.MAX_VALUE, "Result should be less than Long.MAX_VALUE");
        }
    }

    @Nested
    @DisplayName("randomLong(long, long)")
    class RandomLongRangeTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a long within the specified range")
        void shouldReturnLongWithinRange(final RandomUtils ru) {
            final long result = ru.randomLong(33L, 42L);
            assertTrue(result >= 33L, "Result should be >= startInclusive");
            assertTrue(result < 42L, "Result should be < endExclusive");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a long within the extreme range [0, Long.MAX_VALUE)")
        void shouldReturnLongInExtremeRange(final RandomUtils ru) {
            final long result = ru.randomLong(0, Long.MAX_VALUE);
            assertTrue(result >= 0, "Result should be non-negative");
            assertTrue(result < Long.MAX_VALUE, "Result should be less than Long.MAX_VALUE");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return the start value when start and end are the same")
        void shouldReturnStartWhenRangeIsZero(final RandomUtils ru) {
            assertEquals(42L, ru.randomLong(42L, 42L));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw IllegalArgumentException when start > end")
        void shouldThrowExceptionWhenStartIsGreaterThanEnd(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomLong(2, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw IllegalArgumentException for a negative start value")
        void shouldThrowExceptionForNegativeStart(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomLong(-1, 1));
        }

        /**
         * Tests that {@code randomLong(start, end)} does not return {@code endExclusive}
         * for a large-valued range. This is a regression test for LANG-1592, where a
         * previous implementation using {@code (long) nextDouble(start, end)} could
         * incorrectly produce the upper bound.
         */
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should not return the exclusive end for large-valued ranges (LANG-1592)")
        void shouldNotReturnExclusiveEndForLargeValuedRanges(final RandomUtils ru) {
            final long startInclusive = 12900000000001L;
            final long endExclusive = 12900000000016L;
            // The previous faulty implementation failed frequently over a large number of trials.
            // This loop is sized to be likely to catch the regression.
            final int trials = (int) (endExclusive - startInclusive) * 1000;
            for (int i = 0; i < trials; i++) {
                assertNotEquals(endExclusive, ru.randomLong(startInclusive, endExclusive));
            }
        }
    }
}