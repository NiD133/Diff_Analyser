package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link RandomUtils}.
 * This version of the test class focuses on the instance methods of RandomUtils,
 * testing them against secure, secure-strong, and insecure random providers.
 */
// Renamed from RandomUtilsTestTest20 for clarity and standard convention.
public class RandomUtilsTest extends AbstractLangTest {

    /**
     * A small delta value for floating-point comparisons.
     */
    private static final double DELTA = 1e-5;

    /**
     * Provides the different RandomUtils instances to be tested.
     */
    static Stream<RandomUtils> randomProvider() {
        return Stream.of(RandomUtils.secure(), RandomUtils.secureStrong(), RandomUtils.insecure());
    }

    // --- Test Groups for each method ---

    @Nested
    @DisplayName("randomBoolean()")
    class RandomBooleanTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldNotFail(final RandomUtils ru) {
            final boolean result = ru.randomBoolean();
            // This is a tautology, but it ensures the method returns a boolean
            // without throwing an exception, which is the primary goal of this test.
            assertTrue(result || !result);
        }
    }

    @Nested
    @DisplayName("randomBytes(int)")
    class RandomBytesTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withPositiveCount_shouldReturnArrayOfCorrectSize(final RandomUtils ru) {
            final byte[] result = ru.randomBytes(20);
            assertEquals(20, result.length);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withZeroCount_shouldReturnEmptyArray(final RandomUtils ru) {
            final byte[] result = ru.randomBytes(0);
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
        void noArgs_shouldReturnPositiveValue(final RandomUtils ru) {
            final double result = ru.randomDouble();
            assertTrue(result >= 0d && result < Double.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void inRange_shouldReturnDoubleWithinBounds(final RandomUtils ru) {
            final double result = ru.randomDouble(33d, 42d);
            assertTrue(result >= 33d && result < 42d);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withExtremeRange_shouldReturnDoubleWithinBounds(final RandomUtils ru) {
            final double result = ru.randomDouble(0, Double.MAX_VALUE);
            assertTrue(result >= 0 && result < Double.MAX_VALUE, "Result should be within [0, Double.MAX_VALUE)");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withSameStartAndEnd_shouldReturnStartValue(final RandomUtils ru) {
            assertEquals(42.1, ru.randomDouble(42.1, 42.1), DELTA);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withNegativeStart_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomDouble(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withStartAfterEnd_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomDouble(2, 1));
        }
    }

    @Nested
    @DisplayName("randomFloat()")
    class RandomFloatTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void noArgs_shouldReturnPositiveValue(final RandomUtils ru) {
            final float result = ru.randomFloat();
            assertTrue(result >= 0f && result < Float.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void inRange_shouldReturnFloatWithinBounds(final RandomUtils ru) {
            final float result = ru.randomFloat(33f, 42f);
            assertTrue(result >= 33f && result < 42f);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withExtremeRange_shouldReturnFloatWithinBounds(final RandomUtils ru) {
            final float result = ru.randomFloat(0, Float.MAX_VALUE);
            assertTrue(result >= 0f && result < Float.MAX_VALUE, "Result should be within [0, Float.MAX_VALUE)");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withSameStartAndEnd_shouldReturnStartValue(final RandomUtils ru) {
            assertEquals(42.1f, ru.randomFloat(42.1f, 42.1f), DELTA);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withNegativeStart_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomFloat(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withStartAfterEnd_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomFloat(2, 1));
        }
    }

    @Nested
    @DisplayName("randomInt()")
    class RandomIntTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void noArgs_shouldReturnPositiveValue(final RandomUtils ru) {
            final int result = ru.randomInt();
            // Javadoc states: between 0 (inclusive) and Integer.MAX_VALUE (exclusive).
            assertTrue(result >= 0 && result < Integer.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void inRange_shouldReturnIntWithinBounds(final RandomUtils ru) {
            final int result = ru.randomInt(33, 42);
            assertTrue(result >= 33 && result < 42);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withExtremeRange_shouldReturnIntWithinBounds(final RandomUtils ru) {
            final int result = ru.randomInt(0, Integer.MAX_VALUE);
            assertTrue(result >= 0 && result < Integer.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withSameStartAndEnd_shouldReturnStartValue(final RandomUtils ru) {
            assertEquals(42, ru.randomInt(42, 42));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withNegativeStart_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomInt(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withStartAfterEnd_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomInt(2, 1));
        }
    }

    @Nested
    @DisplayName("randomLong()")
    class RandomLongTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void noArgs_shouldReturnPositiveValue(final RandomUtils ru) {
            final long result = ru.randomLong();
            assertTrue(result >= 0L && result < Long.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void inRange_shouldReturnLongWithinBounds(final RandomUtils ru) {
            final long result = ru.randomLong(33L, 42L);
            assertTrue(result >= 33L && result < 42L);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withExtremeRange_shouldReturnLongWithinBounds(final RandomUtils ru) {
            final long result = ru.randomLong(0, Long.MAX_VALUE);
            assertTrue(result >= 0 && result < Long.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withSameStartAndEnd_shouldReturnStartValue(final RandomUtils ru) {
            assertEquals(42L, ru.randomLong(42L, 42L));
        }

        /**
         * Test a large value for long. A previous implementation using
         * {@code (long) nextDouble(startInclusive, endExclusive)} could generate a value
         * equal to the upper limit. See LANG-1592.
         */
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void forLargeRange_shouldNotReturnEndExclusiveValue(final RandomUtils ru) {
            final long startInclusive = 12900000000001L;
            final long endExclusive = 12900000000016L;
            // Note: The previous buggy implementation takes thousands of calls to generate an
            // error. This loop size fails most of the time with that implementation.
            final int n = (int) (endExclusive - startInclusive) * 1000;
            for (int i = 0; i < n; i++) {
                assertNotEquals(endExclusive, ru.randomLong(startInclusive, endExclusive));
            }
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withNegativeStart_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomLong(-1, 1));
        }



        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withStartAfterEnd_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomLong(2, 1));
        }
    }

    @Nested
    @DisplayName("Static (deprecated) methods")
    class StaticMethodTest {
        /**
         * Tests the deprecated static method {@link RandomUtils#nextInt(int, int)} for backward compatibility.
         */
        @Test
        void staticNextInt_inRange_shouldReturnIntWithinBounds() {
            final int result = RandomUtils.nextInt(33, 42);
            assertTrue(result >= 33 && result < 42);
        }
    }
}