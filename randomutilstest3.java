package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests the instance methods of {@link RandomUtils} using various random number generator providers.
 */
public class RandomUtilsTest extends AbstractLangTest {

    /**
     * A delta for comparing double and float values.
     */
    private static final double DELTA = 1e-5;

    /**
     * Provides instances of different RandomUtils implementations for parameterized tests.
     *
     * @return a stream of RandomUtils instances (secure, secure-strong, and insecure).
     */
    static Stream<RandomUtils> randomProvider() {
        return Stream.of(RandomUtils.secure(), RandomUtils.secureStrong(), RandomUtils.insecure());
    }

    @Nested
    @DisplayName("For randomBoolean()")
    class RandomBooleanTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should execute without throwing an exception")
        void testRandomBoolean(final RandomUtils randomUtils) {
            // The test ensures the method can be called without error.
            // Asserting the result is either true or false is a tautology.
            assertDoesNotThrow(randomUtils::randomBoolean);
        }
    }

    @Nested
    @DisplayName("For randomBytes(int)")
    class RandomBytesTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return an array of the specified size for a positive count")
        void testRandomBytesWithPositiveCount(final RandomUtils randomUtils) {
            final byte[] result = randomUtils.randomBytes(20);
            assertEquals(20, result.length);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw IllegalArgumentException for a negative count")
        void testRandomBytesWithNegativeCountThrowsException(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomBytes(-1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return an empty array for a zero count")
        void testRandomBytesWithZeroCount(final RandomUtils randomUtils) {
            assertArrayEquals(new byte[0], randomUtils.randomBytes(0));
        }
    }

    @Nested
    @DisplayName("For randomDouble(...)")
    class RandomDoubleTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a value within the default range [0, Double.MAX_VALUE)")
        void testRandomDoubleDefaultRange(final RandomUtils randomUtils) {
            final double result = randomUtils.randomDouble();
            assertTrue(result >= 0d, "Result must be non-negative");
            assertTrue(result < Double.MAX_VALUE, "Result must be less than Double.MAX_VALUE");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a value within the specified range")
        void testRandomDoubleWithRange(final RandomUtils randomUtils) {
            final double result = randomUtils.randomDouble(33d, 42d);
            assertTrue(result >= 33d, "Result must be >= startInclusive");
            assertTrue(result < 42d, "Result must be < endExclusive");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a value within the extreme range [0, Double.MAX_VALUE)")
        void testRandomDoubleExtremeRange(final RandomUtils randomUtils) {
            final double result = randomUtils.randomDouble(0, Double.MAX_VALUE);
            assertTrue(result >= 0, "Result must be non-negative");
            assertTrue(result < Double.MAX_VALUE, "Result must be less than Double.MAX_VALUE (exclusive bound)");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return the start value when start equals end")
        void testRandomDoubleWithMinimalRange(final RandomUtils randomUtils) {
            assertEquals(42.1, randomUtils.randomDouble(42.1, 42.1), DELTA);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw IllegalArgumentException when start is negative")
        void testRandomDoubleWithNegativeStartThrowsException(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomDouble(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw IllegalArgumentException when start is greater than end")
        void testRandomDoubleWithStartGreaterThanEndThrowsException(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomDouble(2, 1));
        }
    }

    @Nested
    @DisplayName("For randomFloat(...)")
    class RandomFloatTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a value within the default range [0, Float.MAX_VALUE)")
        void testRandomFloatDefaultRange(final RandomUtils randomUtils) {
            final float result = randomUtils.randomFloat();
            assertTrue(result >= 0f, "Result must be non-negative");
            assertTrue(result < Float.MAX_VALUE, "Result must be less than Float.MAX_VALUE");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a value within the specified range")
        void testRandomFloatWithRange(final RandomUtils randomUtils) {
            final float result = randomUtils.randomFloat(33f, 42f);
            assertTrue(result >= 33f, "Result must be >= startInclusive");
            assertTrue(result < 42f, "Result must be < endExclusive");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a value within the extreme range [0, Float.MAX_VALUE)")
        void testRandomFloatExtremeRange(final RandomUtils randomUtils) {
            final float result = randomUtils.randomFloat(0, Float.MAX_VALUE);
            assertTrue(result >= 0f, "Result must be non-negative");
            assertTrue(result < Float.MAX_VALUE, "Result must be less than Float.MAX_VALUE (exclusive bound)");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return the start value when start equals end")
        void testRandomFloatWithMinimalRange(final RandomUtils randomUtils) {
            assertEquals(42.1f, randomUtils.randomFloat(42.1f, 42.1f), DELTA);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw IllegalArgumentException when start is negative")
        void testRandomFloatWithNegativeStartThrowsException(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomFloat(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw IllegalArgumentException when start is greater than end")
        void testRandomFloatWithStartGreaterThanEndThrowsException(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomFloat(2, 1));
        }
    }

    @Nested
    @DisplayName("For randomInt(...)")
    class RandomIntTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a value within the default range [0, Integer.MAX_VALUE)")
        void testRandomIntDefaultRange(final RandomUtils randomUtils) {
            final int result = randomUtils.randomInt();
            assertTrue(result >= 0, "Result must be non-negative");
            assertTrue(result < Integer.MAX_VALUE, "Result must be less than Integer.MAX_VALUE");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a value within the specified range")
        void testRandomIntWithRange(final RandomUtils randomUtils) {
            final int result = randomUtils.randomInt(33, 42);
            assertTrue(result >= 33, "Result must be >= startInclusive");
            assertTrue(result < 42, "Result must be < endExclusive");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a value within the extreme range [0, Integer.MAX_VALUE)")
        void testRandomIntExtremeRange(final RandomUtils randomUtils) {
            final int result = randomUtils.randomInt(0, Integer.MAX_VALUE);
            assertTrue(result >= 0, "Result must be non-negative");
            assertTrue(result < Integer.MAX_VALUE, "Result must be less than Integer.MAX_VALUE");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return the start value when start equals end")
        void testRandomIntWithMinimalRange(final RandomUtils randomUtils) {
            assertEquals(42, randomUtils.randomInt(42, 42));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw IllegalArgumentException when start is negative")
        void testRandomIntWithNegativeStartThrowsException(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomInt(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw IllegalArgumentException when start is greater than end")
        void testRandomIntWithStartGreaterThanEndThrowsException(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomInt(2, 1));
        }
    }

    @Nested
    @DisplayName("For randomLong(...)")
    class RandomLongTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a value within the default range [0, Long.MAX_VALUE)")
        void testRandomLongDefaultRange(final RandomUtils randomUtils) {
            final long result = randomUtils.randomLong();
            assertTrue(result >= 0L, "Result must be non-negative");
            assertTrue(result < Long.MAX_VALUE, "Result must be less than Long.MAX_VALUE");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a value within the specified range")
        void testRandomLongWithRange(final RandomUtils randomUtils) {
            final long result = randomUtils.randomLong(33L, 42L);
            assertTrue(result >= 33L, "Result must be >= startInclusive");
            assertTrue(result < 42L, "Result must be < endExclusive");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return a value within the extreme range [0, Long.MAX_VALUE)")
        void testRandomLongExtremeRange(final RandomUtils randomUtils) {
            final long result = randomUtils.randomLong(0, Long.MAX_VALUE);
            assertTrue(result >= 0, "Result must be non-negative");
            assertTrue(result < Long.MAX_VALUE, "Result must be less than Long.MAX_VALUE");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should return the start value when start equals end")
        void testRandomLongWithMinimalRange(final RandomUtils randomUtils) {
            assertEquals(42L, randomUtils.randomLong(42L, 42L));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw IllegalArgumentException when start is negative")
        void testRandomLongWithNegativeStartThrowsException(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomLong(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should throw IllegalArgumentException when start is greater than end")
        void testRandomLongWithStartGreaterThanEndThrowsException(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomLong(2, 1));
        }

        /**
         * Tests that {@link RandomUtils#randomLong(long, long)} does not generate a value equal to the upper limit.
         * This was a bug (LANG-1592) in a previous implementation that used {@code nextDouble}.
         */
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("should not return the exclusive end for a large value range (LANG-1592)")
        void testRandomLongDoesNotReturnExclusiveEndForLargeValueRange(final RandomUtils randomUtils) {
            final long startInclusive = 12900000000001L;
            final long endExclusive = 12900000000016L;
            // The previous faulty implementation using `(long) nextDouble(start, end)`
            // would fail this test frequently, as it could generate the endExclusive value.
            final int loopSize = (int) (endExclusive - startInclusive) * 1000;
            for (int i = 0; i < loopSize; i++) {
                assertNotEquals(endExclusive, randomUtils.randomLong(startInclusive, endExclusive));
            }
        }
    }
}