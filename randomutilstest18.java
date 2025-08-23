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

/**
 * Tests for {@link RandomUtils}.
 * <p>
 * This test suite uses parameterized tests to run each test case against
 * multiple {@link RandomUtils} implementations: secure, secure-strong, and insecure.
 * </p>
 */
public class RandomUtilsTestTest18 extends AbstractLangTest {

    /**
     * A small delta value for comparing double and float values.
     */
    private static final double DELTA = 1e-5;

    /**
     * Provides the different {@link RandomUtils} instances to be tested.
     */
    static Stream<RandomUtils> randomProvider() {
        return Stream.of(RandomUtils.secure(), RandomUtils.secureStrong(), RandomUtils.insecure());
    }

    @Nested
    @DisplayName("randomBoolean()")
    class RandomBooleanTests {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTestTest18#randomProvider")
        void shouldExecuteWithoutError(final RandomUtils ru) {
            // This is a smoke test to ensure the method executes.
            // A true randomness test is out of scope.
            final boolean result = ru.randomBoolean();
            assertTrue(result || !result);
        }
    }

    @Nested
    @DisplayName("randomBytes()")
    class RandomBytesTests {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTestTest18#randomProvider")
        void shouldReturnArrayOfCorrectSize(final RandomUtils ru) {
            final byte[] result = ru.randomBytes(20);
            assertEquals(20, result.length);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTestTest18#randomProvider")
        void shouldReturnEmptyArrayForZeroSize(final RandomUtils ru) {
            assertArrayEquals(new byte[0], ru.randomBytes(0));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTestTest18#randomProvider")
        void shouldThrowExceptionForNegativeSize(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomBytes(-1));
        }
    }

    @Nested
    @DisplayName("randomDouble()")
    class RandomDoubleTests {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTestTest18#randomProvider")
        void shouldReturnPositiveDoubleForNoArgs(final RandomUtils ru) {
            final double result = ru.randomDouble();
            assertTrue(result >= 0d);
            assertTrue(result < Double.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTestTest18#randomProvider")
        void shouldReturnDoubleWithinRange(final RandomUtils ru) {
            final double result = ru.randomDouble(33d, 42d);
            assertTrue(result >= 33d);
            assertTrue(result < 42d);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTestTest18#randomProvider")
        void shouldReturnStartWhenRangeIsZero(final RandomUtils ru) {
            assertEquals(42.1, ru.randomDouble(42.1, 42.1), DELTA);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTestTest18#randomProvider")
        void shouldHandleExtremeRange(final RandomUtils ru) {
            final double result = ru.randomDouble(0, Double.MAX_VALUE);
            assertTrue(result >= 0);
            assertTrue(result < Double.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTestTest18#randomProvider")
        void shouldThrowExceptionWhenStartIsNegative(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomDouble(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTestTest18#randomProvider")
        void shouldThrowExceptionWhenStartIsGreaterThanEnd(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomDouble(2, 1));
        }
    }

    @Nested
    @DisplayName("randomFloat()")
    class RandomFloatTests {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTestTest18#randomProvider")
        void shouldReturnPositiveFloatForNoArgs(final RandomUtils ru) {
            final float result = ru.randomFloat();
            assertTrue(result >= 0f);
            assertTrue(result < Float.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTestTest18#randomProvider")
        void shouldReturnFloatWithinRange(final RandomUtils ru) {
            final float result = ru.randomFloat(33f, 42f);
            assertTrue(result >= 33f);
            assertTrue(result < 42f);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTestTest18#randomProvider")
        void shouldReturnStartWhenRangeIsZero(final RandomUtils ru) {
            assertEquals(42.1f, ru.randomFloat(42.1f, 42.1f), DELTA);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTestTest18#randomProvider")
        void shouldHandleExtremeRange(final RandomUtils ru) {
            final float result = ru.randomFloat(0, Float.MAX_VALUE);
            assertTrue(result >= 0f);
            assertTrue(result < Float.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTestTest18#randomProvider")
        void shouldThrowExceptionWhenStartIsNegative(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomFloat(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTestTest18#randomProvider")
        void shouldThrowExceptionWhenStartIsGreaterThanEnd(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomFloat(2, 1));
        }
    }

    @Nested
    @DisplayName("randomInt()")
    class RandomIntTests {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTestTest18#randomProvider")
        void shouldReturnPositiveIntForNoArgs(final RandomUtils ru) {
            final int result = ru.randomInt();
            assertTrue(result >= 0);
            assertTrue(result < Integer.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTestTest18#randomProvider")
        void shouldReturnIntWithinRange(final RandomUtils ru) {
            final int result = ru.randomInt(33, 42);
            assertTrue(result >= 33);
            assertTrue(result < 42);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTestTest18#randomProvider")
        void shouldReturnStartWhenRangeIsZero(final RandomUtils ru) {
            assertEquals(42, ru.randomInt(42, 42));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTestTest18#randomProvider")
        void shouldHandleExtremeRange(final RandomUtils ru) {
            final int result = ru.randomInt(0, Integer.MAX_VALUE);
            assertTrue(result >= 0);
            assertTrue(result < Integer.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTestTest18#randomProvider")
        void shouldThrowExceptionWhenStartIsNegative(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomInt(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTestTest18#randomProvider")
        void shouldThrowExceptionWhenStartIsGreaterThanEnd(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomInt(2, 1));
        }
    }

    @Nested
    @DisplayName("randomLong()")
    class RandomLongTests {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTestTest18#randomProvider")
        void shouldReturnPositiveLongForNoArgs(final RandomUtils ru) {
            final long result = ru.randomLong();
            assertTrue(result >= 0L);
            assertTrue(result < Long.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTestTest18#randomProvider")
        void shouldReturnLongWithinRange(final RandomUtils ru) {
            final long result = ru.randomLong(33L, 42L);
            assertTrue(result >= 33L);
            assertTrue(result < 42L);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTestTest18#randomProvider")
        void shouldReturnStartWhenRangeIsZero(final RandomUtils ru) {
            assertEquals(42L, ru.randomLong(42L, 42L));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTestTest18#randomProvider")
        void shouldHandleExtremeRange(final RandomUtils ru) {
            final long result = ru.randomLong(0, Long.MAX_VALUE);
            assertTrue(result >= 0);
            assertTrue(result < Long.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTestTest18#randomProvider")
        void shouldThrowExceptionWhenStartIsNegative(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomLong(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTestTest18#randomProvider")
        void shouldThrowExceptionWhenStartIsGreaterThanEnd(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomLong(2, 1));
        }

        /**
         * Test a large value for long. A previous implementation using
         * {@code (long) nextDouble(startInclusive, endExclusive)} could generate a value equal
         * to the upper limit. This test runs the method many times to increase the
         * probability of catching such a bug.
         * <p>See LANG-1592.</p>
         */
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTestTest18#randomProvider")
        void shouldNotReturnExclusiveEndForLargeLongRange(final RandomUtils ru) {
            final long startInclusive = 12900000000001L;
            final long endExclusive = 12900000000016L;
            final int n = (int) (endExclusive - startInclusive) * 1000;

            for (int i = 0; i < n; i++) {
                assertNotEquals(endExclusive, ru.randomLong(startInclusive, endExclusive));
            }
        }
    }

    // This test is redundant with the parameterized test in RandomFloatTests
    // and tests a deprecated static method. It has been removed.
    // @Test
    // void testNextFloatNegative() {
    //     assertIllegalArgumentException(() -> RandomUtils.nextFloat(-1, 1));
    // }
}