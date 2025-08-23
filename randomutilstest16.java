package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link RandomUtils}.
 *
 * <p>This test class is structured using nested classes to group tests
 * for each method of the RandomUtils class.</p>
 */
@DisplayName("RandomUtils Test Suite")
public class RandomUtilsTest extends AbstractLangTest {

    /**
     * Provides streams of different RandomUtils instances (secure, insecure, etc.)
     * for parameterized tests.
     */
    static Stream<RandomUtils> randomProvider() {
        return Stream.of(RandomUtils.secure(), RandomUtils.secureStrong(), RandomUtils.insecure());
    }

    @Nested
    @DisplayName("randomBoolean()")
    class RandomBooleanTests {
        /**
         * A better test than a simple tautology. This test asserts that over a number of
         * trials, both true and false are returned. The probability of this test
         * failing is astronomically low (1/2^99).
         */
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldProduceBothTrueAndFalse(final RandomUtils ru) {
            final Set<Boolean> results = new HashSet<>();
            for (int i = 0; i < 100; i++) {
                results.add(ru.randomBoolean());
                if (results.size() == 2) {
                    break;
                }
            }
            assertEquals(2, results.size(), "Expected both true and false to be generated.");
        }
    }

    @Nested
    @DisplayName("randomBytes(int)")
    class RandomBytesTests {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldReturnArrayOfCorrectSize(final RandomUtils ru) {
            final byte[] result = ru.randomBytes(20);
            assertEquals(20, result.length);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void givenNegativeCount_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomBytes(-1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void givenZeroCount_shouldReturnEmptyArray(final RandomUtils ru) {
            assertArrayEquals(new byte[0], ru.randomBytes(0));
        }
    }

    @Nested
    @DisplayName("randomDouble()")
    class RandomDoubleTests {
        private static final double DELTA = 1e-5;

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void noArgs_shouldReturnValueBetweenZeroAndMax(final RandomUtils ru) {
            final double result = ru.randomDouble();
            assertTrue(result >= 0d && result < Double.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void givenRange_shouldReturnDoubleWithinBounds(final RandomUtils ru) {
            final double result = ru.randomDouble(33d, 42d);
            assertTrue(result >= 33d && result < 42d);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void givenMinimalRange_shouldReturnStartValue(final RandomUtils ru) {
            assertEquals(42.1, ru.randomDouble(42.1, 42.1), DELTA);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void givenZeroToMaxRange_shouldReturnDoubleWithinBounds(final RandomUtils ru) {
            final double result = ru.randomDouble(0, Double.MAX_VALUE);
            assertTrue(result >= 0 && result < Double.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void givenInvalidRange_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomDouble(2, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void givenNegativeStart_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomDouble(-1, 1));
        }
    }

    @Nested
    @DisplayName("randomFloat()")
    class RandomFloatTests {
        private static final double DELTA = 1e-5;

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void noArgs_shouldReturnValueBetweenZeroAndMax(final RandomUtils ru) {
            final float result = ru.randomFloat();
            assertTrue(result >= 0f && result < Float.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void givenRange_shouldReturnFloatWithinBounds(final RandomUtils ru) {
            final float result = ru.randomFloat(33f, 42f);
            assertTrue(result >= 33f && result < 42f);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void givenMinimalRange_shouldReturnStartValue(final RandomUtils ru) {
            assertEquals(42.1f, ru.randomFloat(42.1f, 42.1f), DELTA);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void givenZeroToMaxRange_shouldReturnFloatWithinBounds(final RandomUtils ru) {
            final float result = ru.randomFloat(0, Float.MAX_VALUE);
            assertTrue(result >= 0f && result < Float.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void givenInvalidRange_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomFloat(2, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void givenNegativeStart_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomFloat(-1, 1));
        }
    }

    @Nested
    @DisplayName("randomInt()")
    class RandomIntTests {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void noArgs_shouldReturnValueBetweenZeroAndMax(final RandomUtils ru) {
            final int result = ru.randomInt();
            assertTrue(result >= 0 && result < Integer.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void givenRange_shouldReturnIntWithinBounds(final RandomUtils ru) {
            final int result = ru.randomInt(33, 42);
            assertTrue(result >= 33 && result < 42);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void givenMinimalRange_shouldReturnStartValue(final RandomUtils ru) {
            assertEquals(42, ru.randomInt(42, 42));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void givenZeroToMaxRange_shouldReturnIntWithinBounds(final RandomUtils ru) {
            final int result = ru.randomInt(0, Integer.MAX_VALUE);
            assertTrue(result >= 0 && result < Integer.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void givenInvalidRange_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomInt(2, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void givenNegativeStart_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomInt(-1, 1));
        }
    }

    @Nested
    @DisplayName("randomLong()")
    class RandomLongTests {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void noArgs_shouldReturnValueBetweenZeroAndMax(final RandomUtils ru) {
            final long result = ru.randomLong();
            assertTrue(result >= 0L && result < Long.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void givenRange_shouldReturnLongWithinBounds(final RandomUtils ru) {
            final long result = ru.randomLong(33L, 42L);
            assertTrue(result >= 33L && result < 42L);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void givenMinimalRange_shouldReturnStartValue(final RandomUtils ru) {
            assertEquals(42L, ru.randomLong(42L, 42L));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void givenZeroToMaxRange_shouldReturnLongWithinBounds(final RandomUtils ru) {
            final long result = ru.randomLong(0, Long.MAX_VALUE);
            assertTrue(result >= 0 && result < Long.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void givenInvalidRange_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomLong(2, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void givenNegativeStart_shouldThrowIllegalArgumentException(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomLong(-1, 1));
        }

        /**
         * Test a large value for long. A previous implementation using
         * {@link RandomUtils#nextDouble(double, double)} could generate a value equal
         * to the upper limit.
         *
         * <pre>
         * return (long) nextDouble(startInclusive, endExclusive);
         * </pre>
         *
         * <p>See LANG-1592.</p>
         */
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void givenLargeValueRange_shouldNotReturnEndExclusiveValue(final RandomUtils ru) {
            final long startInclusive = 12900000000001L;
            final long endExclusive = 12900000000016L;
            // Note: The method using 'return (long) nextDouble(startInclusive, endExclusive)'
            // takes thousands of calls to generate an error. This size loop fails most
            // of the time with the previous method.
            final int n = (int) (endExclusive - startInclusive) * 1000;
            for (int i = 0; i < n; i++) {
                assertNotEquals(endExclusive, ru.randomLong(startInclusive, endExclusive),
                    () -> "Iteration " + i + " returned endExclusive value");
            }
        }
    }

    @Nested
    @DisplayName("Deprecated Static API")
    class DeprecatedStaticApiTests {
        /**
         * Tests the deprecated static method to ensure backward compatibility for exception handling.
         */
        @Test
        @Deprecated
        void nextFloat_withInvalidRange_shouldThrowIllegalArgumentException() {
            assertIllegalArgumentException(() -> RandomUtils.nextFloat(2, 1));
        }
    }
}