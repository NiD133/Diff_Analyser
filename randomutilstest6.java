package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("RandomUtils instance methods")
public class RandomUtilsInstanceTest extends AbstractLangTest {

    /**
     * A small delta for comparing floating-point numbers.
     */
    private static final double DELTA = 1e-5;

    /**
     * Provides streams of different RandomUtils instances for parameterized tests.
     * @return A stream of RandomUtils instances (secure, secureStrong, insecure).
     */
    static Stream<RandomUtils> randomProvider() {
        return Stream.of(RandomUtils.secure(), RandomUtils.secureStrong(), RandomUtils.insecure());
    }

    @Nested
    @DisplayName("randomBoolean()")
    class RandomBooleanTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsInstanceTest#randomProvider")
        void shouldExecuteWithoutError(final RandomUtils ru) {
            // This is a smoke test to ensure the method executes without error.
            // A probabilistic test to check for both true and false is not suitable for a unit test.
            ru.randomBoolean();
        }
    }

    @Nested
    @DisplayName("randomBytes(int count)")
    class RandomBytesTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsInstanceTest#randomProvider")
        void shouldReturnArrayOfCorrectSize(final RandomUtils ru) {
            final byte[] result = ru.randomBytes(20);
            assertEquals(20, result.length);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsInstanceTest#randomProvider")
        void shouldReturnEmptyArrayForZeroCount(final RandomUtils ru) {
            final byte[] result = ru.randomBytes(0);
            assertArrayEquals(new byte[0], result);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsInstanceTest#randomProvider")
        void shouldThrowExceptionForNegativeCount(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomBytes(-1));
        }
    }

    @Nested
    @DisplayName("randomDouble()")
    class RandomDoubleNoArgsTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsInstanceTest#randomProvider")
        void shouldReturnDoubleInDefaultRange(final RandomUtils ru) {
            final double result = ru.randomDouble();
            assertTrue(result >= 0d, "Result should be non-negative");
            assertTrue(result < Double.MAX_VALUE, "Result should be less than Double.MAX_VALUE");
        }
    }

    @Nested
    @DisplayName("randomDouble(double, double)")
    class RandomDoubleRangeTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsInstanceTest#randomProvider")
        void shouldReturnDoubleWithinRange(final RandomUtils ru) {
            final double result = ru.randomDouble(33d, 42d);
            assertTrue(result >= 33d, "Result should be greater than or equal to startInclusive");
            assertTrue(result < 42d, "Result should be less than endExclusive");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsInstanceTest#randomProvider")
        void shouldHandleExtremeRange(final RandomUtils ru) {
            final double result = ru.randomDouble(0, Double.MAX_VALUE);
            assertTrue(result >= 0, "Result should be non-negative");
            assertTrue(result < Double.MAX_VALUE, "Result should be less than Double.MAX_VALUE");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsInstanceTest#randomProvider")
        void shouldReturnStartWhenStartEqualsEnd(final RandomUtils ru) {
            final double result = ru.randomDouble(42.1, 42.1);
            assertEquals(42.1, result, DELTA);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsInstanceTest#randomProvider")
        void shouldThrowExceptionWhenStartIsNegative(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomDouble(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsInstanceTest#randomProvider")
        void shouldThrowExceptionWhenStartIsGreaterThanEnd(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomDouble(2, 1));
        }
    }

    @Nested
    @DisplayName("randomFloat()")
    class RandomFloatNoArgsTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsInstanceTest#randomProvider")
        void shouldReturnFloatInDefaultRange(final RandomUtils ru) {
            final float result = ru.randomFloat();
            assertTrue(result >= 0f, "Result should be non-negative");
            assertTrue(result < Float.MAX_VALUE, "Result should be less than Float.MAX_VALUE");
        }
    }

    @Nested
    @DisplayName("randomFloat(float, float)")
    class RandomFloatRangeTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsInstanceTest#randomProvider")
        void shouldReturnFloatWithinRange(final RandomUtils ru) {
            final float result = ru.randomFloat(33f, 42f);
            assertTrue(result >= 33f, "Result should be greater than or equal to startInclusive");
            assertTrue(result < 42f, "Result should be less than endExclusive");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsInstanceTest#randomProvider")
        void shouldHandleExtremeRange(final RandomUtils ru) {
            final float result = ru.randomFloat(0, Float.MAX_VALUE);
            assertTrue(result >= 0f, "Result should be non-negative");
            assertTrue(result < Float.MAX_VALUE, "Result should be less than Float.MAX_VALUE");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsInstanceTest#randomProvider")
        void shouldReturnStartWhenStartEqualsEnd(final RandomUtils ru) {
            final float result = ru.randomFloat(42.1f, 42.1f);
            assertEquals(42.1f, result, DELTA);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsInstanceTest#randomProvider")
        void shouldThrowExceptionWhenStartIsNegative(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomFloat(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsInstanceTest#randomProvider")
        void shouldThrowExceptionWhenStartIsGreaterThanEnd(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomFloat(2, 1));
        }
    }

    @Nested
    @DisplayName("randomInt()")
    class RandomIntNoArgsTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsInstanceTest#randomProvider")
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
        @MethodSource("org.apache.commons.lang3.RandomUtilsInstanceTest#randomProvider")
        void shouldReturnIntWithinRange(final RandomUtils ru) {
            final int result = ru.randomInt(33, 42);
            assertTrue(result >= 33, "Result should be greater than or equal to startInclusive");
            assertTrue(result < 42, "Result should be less than endExclusive");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsInstanceTest#randomProvider")
        void shouldHandleExtremeRange(final RandomUtils ru) {
            final int result = ru.randomInt(0, Integer.MAX_VALUE);
            assertTrue(result >= 0, "Result should be non-negative");
            assertTrue(result < Integer.MAX_VALUE, "Result should be less than Integer.MAX_VALUE");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsInstanceTest#randomProvider")
        void shouldReturnStartWhenStartEqualsEnd(final RandomUtils ru) {
            assertEquals(42, ru.randomInt(42, 42));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsInstanceTest#randomProvider")
        void shouldThrowExceptionWhenStartIsNegative(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomInt(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsInstanceTest#randomProvider")
        void shouldThrowExceptionWhenStartIsGreaterThanEnd(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomInt(2, 1));
        }
    }

    @Nested
    @DisplayName("randomLong()")
    class RandomLongNoArgsTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsInstanceTest#randomProvider")
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
        @MethodSource("org.apache.commons.lang3.RandomUtilsInstanceTest#randomProvider")
        void shouldReturnLongWithinRange(final RandomUtils ru) {
            final long result = ru.randomLong(33L, 42L);
            assertTrue(result >= 33L, "Result should be greater than or equal to startInclusive");
            assertTrue(result < 42L, "Result should be less than endExclusive");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsInstanceTest#randomProvider")
        void shouldHandleExtremeRange(final RandomUtils ru) {
            final long result = ru.randomLong(0, Long.MAX_VALUE);
            assertTrue(result >= 0, "Result should be non-negative");
            assertTrue(result < Long.MAX_VALUE, "Result should be less than Long.MAX_VALUE");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsInstanceTest#randomProvider")
        void shouldReturnStartWhenStartEqualsEnd(final RandomUtils ru) {
            assertEquals(42L, ru.randomLong(42L, 42L));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsInstanceTest#randomProvider")
        void shouldThrowExceptionWhenStartIsNegative(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomLong(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsInstanceTest#randomProvider")
        void shouldThrowExceptionWhenStartIsGreaterThanEnd(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomLong(2, 1));
        }

        /**
         * Test for a bug (LANG-1592) where a previous implementation using
         * {@code (long) nextDouble(startInclusive, endExclusive)} could incorrectly
         * generate a value equal to the exclusive upper bound.
         */
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsInstanceTest#randomProvider")
        @DisplayName("should not return endExclusive for large value range (LANG-1592)")
        void shouldNotReturnEndExclusiveForLargeValueRange(final RandomUtils ru) {
            final long startInclusive = 12900000000001L;
            final long endExclusive = 12900000000016L;
            // The loop size is chosen to have a high probability of failure
            // with the previous buggy implementation.
            final int n = (int) (endExclusive - startInclusive) * 1000;
            for (int i = 0; i < n; i++) {
                assertNotEquals(endExclusive, ru.randomLong(startInclusive, endExclusive));
            }
        }
    }
}