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
 */
public class RandomUtilsTest extends AbstractLangTest {

    /**
     * A small delta for comparing floating-point numbers.
     */
    private static final double DELTA = 1e-5;

    /**
     * Provides the different RandomUtils instances to be tested.
     */
    static Stream<RandomUtils> randomProvider() {
        return Stream.of(RandomUtils.secure(), RandomUtils.secureStrong(), RandomUtils.insecure());
    }

    @Nested
    @DisplayName("randomBoolean()")
    class RandomBooleanTests {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldNotThrowException(final RandomUtils randomUtils) {
            // This test simply ensures the method call completes without exceptions.
            // Asserting the result is a tautology (it's always true or false),
            // and we cannot reliably test for true randomness.
            randomUtils.randomBoolean();
        }
    }

    @Nested
    @DisplayName("randomBytes()")
    class RandomBytesTests {
        private static final String METHOD_SOURCE = "org.apache.commons.lang3.RandomUtilsTest#randomProvider";

        @ParameterizedTest
        @MethodSource(METHOD_SOURCE)
        void withPositiveCount_shouldReturnArrayOfCorrectSize(final RandomUtils randomUtils) {
            final byte[] result = randomUtils.randomBytes(20);
            assertEquals(20, result.length);
        }

        @ParameterizedTest
        @MethodSource(METHOD_SOURCE)
        void withZeroCount_shouldReturnEmptyArray(final RandomUtils randomUtils) {
            assertArrayEquals(new byte[0], randomUtils.randomBytes(0));
        }

        @ParameterizedTest
        @MethodSource(METHOD_SOURCE)
        void withNegativeCount_shouldThrowException(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomBytes(-1));
        }
    }

    @Nested
    @DisplayName("randomDouble()")
    class RandomDoubleTests {
        private static final String METHOD_SOURCE = "org.apache.commons.lang3.RandomUtilsTest#randomProvider";

        @ParameterizedTest
        @MethodSource(METHOD_SOURCE)
        void withoutArgs_shouldBeWithinDefaultBounds(final RandomUtils randomUtils) {
            final double result = randomUtils.randomDouble();
            assertTrue(result >= 0d, "Result should be non-negative");
            assertTrue(result < Double.MAX_VALUE, "Result should be less than Double.MAX_VALUE");
        }

        @ParameterizedTest
        @MethodSource(METHOD_SOURCE)
        void withRange_shouldBeWithinBounds(final RandomUtils randomUtils) {
            final double result = randomUtils.randomDouble(33d, 42d);
            assertTrue(result >= 33d, "Result should be >= startInclusive");
            assertTrue(result < 42d, "Result should be < endExclusive");
        }

        @ParameterizedTest
        @MethodSource(METHOD_SOURCE)
        void withMaximumRange_shouldBeWithinBounds(final RandomUtils randomUtils) {
            final double result = randomUtils.randomDouble(0, Double.MAX_VALUE);
            assertTrue(result >= 0, "Result should be non-negative");
            assertTrue(result < Double.MAX_VALUE, "Result should be less than Double.MAX_VALUE");
        }

        @ParameterizedTest
        @MethodSource(METHOD_SOURCE)
        void withZeroRange_shouldReturnStartValue(final RandomUtils randomUtils) {
            assertEquals(42.1, randomUtils.randomDouble(42.1, 42.1), DELTA);
        }

        @ParameterizedTest
        @MethodSource(METHOD_SOURCE)
        void withInvalidRange_shouldThrowException(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomDouble(2, 1));
        }

        @ParameterizedTest
        @MethodSource(METHOD_SOURCE)
        void withNegativeStart_shouldThrowException(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomDouble(-1, 1));
        }
    }

    @Nested
    @DisplayName("randomFloat()")
    class RandomFloatTests {
        private static final String METHOD_SOURCE = "org.apache.commons.lang3.RandomUtilsTest#randomProvider";

        @ParameterizedTest
        @MethodSource(METHOD_SOURCE)
        void withoutArgs_shouldBeWithinDefaultBounds(final RandomUtils randomUtils) {
            final float result = randomUtils.randomFloat();
            assertTrue(result >= 0f, "Result should be non-negative");
            assertTrue(result < Float.MAX_VALUE, "Result should be less than Float.MAX_VALUE");
        }

        @ParameterizedTest
        @MethodSource(METHOD_SOURCE)
        void withRange_shouldBeWithinBounds(final RandomUtils randomUtils) {
            final float result = randomUtils.randomFloat(33f, 42f);
            assertTrue(result >= 33f, "Result should be >= startInclusive");
            assertTrue(result < 42f, "Result should be < endExclusive");
        }

        @ParameterizedTest
        @MethodSource(METHOD_SOURCE)
        void withMaximumRange_shouldBeWithinBounds(final RandomUtils randomUtils) {
            final float result = randomUtils.randomFloat(0, Float.MAX_VALUE);
            assertTrue(result >= 0f, "Result should be non-negative");
            assertTrue(result < Float.MAX_VALUE, "Result should be less than Float.MAX_VALUE");
        }

        @ParameterizedTest
        @MethodSource(METHOD_SOURCE)
        void withZeroRange_shouldReturnStartValue(final RandomUtils randomUtils) {
            assertEquals(42.1f, randomUtils.randomFloat(42.1f, 42.1f), DELTA);
        }

        @ParameterizedTest
        @MethodSource(METHOD_SOURCE)
        void withInvalidRange_shouldThrowException(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomFloat(2, 1));
        }

        @ParameterizedTest
        @MethodSource(METHOD_SOURCE)
        void withNegativeStart_shouldThrowException(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomFloat(-1, 1));
        }
    }

    @Nested
    @DisplayName("randomInt()")
    class RandomIntTests {
        private static final String METHOD_SOURCE = "org.apache.commons.lang3.RandomUtilsTest#randomProvider";

        @ParameterizedTest
        @MethodSource(METHOD_SOURCE)
        void withoutArgs_shouldBeWithinDefaultBounds(final RandomUtils randomUtils) {
            final int result = randomUtils.randomInt();
            assertTrue(result >= 0, "Result should be non-negative");
            assertTrue(result < Integer.MAX_VALUE, "Result should be less than Integer.MAX_VALUE");
        }

        @ParameterizedTest
        @MethodSource(METHOD_SOURCE)
        void withRange_shouldBeWithinBounds(final RandomUtils randomUtils) {
            final int result = randomUtils.randomInt(33, 42);
            assertTrue(result >= 33, "Result should be >= startInclusive");
            assertTrue(result < 42, "Result should be < endExclusive");
        }

        @ParameterizedTest
        @MethodSource(METHOD_SOURCE)
        void withMaximumRange_shouldBeWithinBounds(final RandomUtils randomUtils) {
            final int result = randomUtils.randomInt(0, Integer.MAX_VALUE);
            assertTrue(result >= 0, "Result should be non-negative");
            assertTrue(result < Integer.MAX_VALUE, "Result should be less than Integer.MAX_VALUE");
        }

        @ParameterizedTest
        @MethodSource(METHOD_SOURCE)
        void withZeroRange_shouldReturnStartValue(final RandomUtils randomUtils) {
            assertEquals(42, randomUtils.randomInt(42, 42));
        }

        @ParameterizedTest
        @MethodSource(METHOD_SOURCE)
        void withInvalidRange_shouldThrowException(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomInt(2, 1));
        }

        @ParameterizedTest
        @MethodSource(METHOD_SOURCE)
        void withNegativeStart_shouldThrowException(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomInt(-1, 1));
        }
    }

    @Nested
    @DisplayName("randomLong()")
    class RandomLongTests {
        private static final String METHOD_SOURCE = "org.apache.commons.lang3.RandomUtilsTest#randomProvider";

        @ParameterizedTest
        @MethodSource(METHOD_SOURCE)
        void withoutArgs_shouldBeWithinDefaultBounds(final RandomUtils randomUtils) {
            final long result = randomUtils.randomLong();
            assertTrue(result >= 0L, "Result should be non-negative");
            assertTrue(result < Long.MAX_VALUE, "Result should be less than Long.MAX_VALUE");
        }

        @ParameterizedTest
        @MethodSource(METHOD_SOURCE)
        void withRange_shouldBeWithinBounds(final RandomUtils randomUtils) {
            final long result = randomUtils.randomLong(33L, 42L);
            assertTrue(result >= 33L, "Result should be >= startInclusive");
            assertTrue(result < 42L, "Result should be < endExclusive");
        }

        @ParameterizedTest
        @MethodSource(METHOD_SOURCE)
        void withMaximumRange_shouldBeWithinBounds(final RandomUtils randomUtils) {
            final long result = randomUtils.randomLong(0, Long.MAX_VALUE);
            assertTrue(result >= 0, "Result should be non-negative");
            assertTrue(result < Long.MAX_VALUE, "Result should be less than Long.MAX_VALUE");
        }

        @ParameterizedTest
        @MethodSource(METHOD_SOURCE)
        void withZeroRange_shouldReturnStartValue(final RandomUtils randomUtils) {
            assertEquals(42L, randomUtils.randomLong(42L, 42L));
        }

        @ParameterizedTest
        @MethodSource(METHOD_SOURCE)
        void withInvalidRange_shouldThrowException(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomLong(2, 1));
        }

        @ParameterizedTest
        @MethodSource(METHOD_SOURCE)
        void withNegativeStart_shouldThrowException(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomLong(-1, 1));
        }

        /**
         * Test a large value for long. A previous implementation using
         * {@code (long) nextDouble(startInclusive, endExclusive)} could generate a value
         * equal to the upper limit.
         * <p>See LANG-1592.</p>
         */
        @ParameterizedTest
        @MethodSource(METHOD_SOURCE)
        void withLargeValueRange_shouldNotReturnEndExclusiveValue(final RandomUtils randomUtils) {
            final long startInclusive = 12900000000001L;
            final long endExclusive = 12900000000016L;
            // The previous buggy method fails frequently with a loop of this size.
            final int n = (int) (endExclusive - startInclusive) * 1000;
            for (int i = 0; i < n; i++) {
                assertNotEquals(endExclusive, randomUtils.randomLong(startInclusive, endExclusive));
            }
        }
    }
}