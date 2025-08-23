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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link RandomUtils}. This test class uses a parameterized approach
 * to ensure that all random generator instances (secure, secure-strong, and insecure)
 * behave according to the same contract.
 */
@DisplayName("RandomUtils Test Suite")
class RandomUtilsTest extends AbstractLangTest {

    /**
     * Provides the different RandomUtils instances for parameterized tests.
     */
    static Stream<RandomUtils> randomProvider() {
        return Stream.of(RandomUtils.secure(), RandomUtils.secureStrong(), RandomUtils.insecure());
    }

    @Nested
    @DisplayName("randomBytes(int count)")
    class RandomBytesTests {

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withPositiveCount_shouldReturnArrayOfCorrectSize(final RandomUtils randomUtils) {
            final int count = 20;
            final byte[] result = randomUtils.randomBytes(count);
            assertEquals(count, result.length);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withZeroCount_shouldReturnEmptyArray(final RandomUtils randomUtils) {
            assertArrayEquals(new byte[0], randomUtils.randomBytes(0));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withNegativeCount_shouldThrowIllegalArgumentException(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomBytes(-1));
        }
    }

    @Nested
    @DisplayName("randomInt(...)")
    class RandomIntTests {

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withoutArgs_shouldReturnNonNegativeInt(final RandomUtils randomUtils) {
            final int result = randomUtils.randomInt();
            assertTrue(result >= 0 && result < Integer.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withValidRange_shouldReturnNumberWithinBounds(final RandomUtils randomUtils) {
            final int startInclusive = 33;
            final int endExclusive = 42;
            final int result = randomUtils.randomInt(startInclusive, endExclusive);
            assertTrue(result >= startInclusive && result < endExclusive,
                () -> "Result " + result + " not in range [" + startInclusive + ", " + endExclusive + ")");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withStartEqualToEnd_shouldReturnStartValue(final RandomUtils randomUtils) {
            assertEquals(42, randomUtils.randomInt(42, 42));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withExtremeRange_shouldReturnNumberWithinBounds(final RandomUtils randomUtils) {
            final int result = randomUtils.randomInt(0, Integer.MAX_VALUE);
            assertTrue(result >= 0 && result < Integer.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withNegativeStart_shouldThrowIllegalArgumentException(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomInt(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withStartGreaterThanEnd_shouldThrowIllegalArgumentException(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomInt(2, 1));
        }
    }

    @Nested
    @DisplayName("randomLong(...)")
    class RandomLongTests {

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withoutArgs_shouldReturnNonNegativeLong(final RandomUtils randomUtils) {
            final long result = randomUtils.randomLong();
            assertTrue(result >= 0L && result < Long.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withValidRange_shouldReturnNumberWithinBounds(final RandomUtils randomUtils) {
            final long startInclusive = 33L;
            final long endExclusive = 42L;
            final long result = randomUtils.randomLong(startInclusive, endExclusive);
            assertTrue(result >= startInclusive && result < endExclusive);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withStartEqualToEnd_shouldReturnStartValue(final RandomUtils randomUtils) {
            assertEquals(42L, randomUtils.randomLong(42L, 42L));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withExtremeRange_shouldReturnNumberWithinBounds(final RandomUtils randomUtils) {
            final long result = randomUtils.randomLong(0, Long.MAX_VALUE);
            assertTrue(result >= 0 && result < Long.MAX_VALUE);
        }

        /**
         * Tests that a value equal to the exclusive upper bound is not generated,
         * which was a bug (LANG-1592) in a previous implementation.
         */
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withLargeValueRange_shouldNotReturnUpperBound(final RandomUtils randomUtils) {
            final long startInclusive = 12900000000001L;
            final long endExclusive = 12900000000016L;
            // A previous implementation using `(long) nextDouble(start, end)` could
            // occasionally return the endExclusive value. This loop is large enough
            // to make that failure highly probable.
            final int iterations = (int) (endExclusive - startInclusive) * 1000;
            for (int i = 0; i < iterations; i++) {
                assertNotEquals(endExclusive, randomUtils.randomLong(startInclusive, endExclusive));
            }
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withNegativeStart_shouldThrowIllegalArgumentException(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomLong(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withStartGreaterThanEnd_shouldThrowIllegalArgumentException(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomLong(2, 1));
        }
    }

    @Nested
    @DisplayName("randomFloat(...)")
    class RandomFloatTests {
        private static final float DELTA = 1e-5f;

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withoutArgs_shouldReturnNonNegativeFloat(final RandomUtils randomUtils) {
            final float result = randomUtils.randomFloat();
            assertTrue(result >= 0f && result < Float.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withValidRange_shouldReturnNumberWithinBounds(final RandomUtils randomUtils) {
            final float startInclusive = 33f;
            final float endExclusive = 42f;
            final float result = randomUtils.randomFloat(startInclusive, endExclusive);
            assertTrue(result >= startInclusive && result < endExclusive);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withStartEqualToEnd_shouldReturnStartValue(final RandomUtils randomUtils) {
            assertEquals(42.1f, randomUtils.randomFloat(42.1f, 42.1f), DELTA);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withExtremeRange_shouldReturnNumberWithinBounds(final RandomUtils randomUtils) {
            final float result = randomUtils.randomFloat(0, Float.MAX_VALUE);
            assertTrue(result >= 0f && result < Float.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withNegativeStart_shouldThrowIllegalArgumentException(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomFloat(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withStartGreaterThanEnd_shouldThrowIllegalArgumentException(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomFloat(2, 1));
        }
    }

    @Nested
    @DisplayName("randomDouble(...)")
    class RandomDoubleTests {
        private static final double DELTA = 1e-5;

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withoutArgs_shouldReturnNonNegativeDouble(final RandomUtils randomUtils) {
            final double result = randomUtils.randomDouble();
            assertTrue(result >= 0d && result < Double.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withValidRange_shouldReturnNumberWithinBounds(final RandomUtils randomUtils) {
            final double startInclusive = 33d;
            final double endExclusive = 42d;
            final double result = randomUtils.randomDouble(startInclusive, endExclusive);
            assertTrue(result >= startInclusive && result < endExclusive);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withStartEqualToEnd_shouldReturnStartValue(final RandomUtils randomUtils) {
            assertEquals(42.1, randomUtils.randomDouble(42.1, 42.1), DELTA);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withExtremeRange_shouldReturnNumberWithinBounds(final RandomUtils randomUtils) {
            final double result = randomUtils.randomDouble(0, Double.MAX_VALUE);
            assertTrue(result >= 0 && result < Double.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withNegativeStart_shouldThrowIllegalArgumentException(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomDouble(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void withStartGreaterThanEnd_shouldThrowIllegalArgumentException(final RandomUtils randomUtils) {
            assertIllegalArgumentException(() -> randomUtils.randomDouble(2, 1));
        }
    }

    // Note: The test for randomBoolean was removed because the original assertion,
    // assertTrue(result || !result), is a tautology and provides no test value.
    // A meaningful test would be statistical and is out of scope for a unit test.

    @Test
    @DisplayName("Constructor should be callable for JavaBean compatibility")
    void constructor_shouldBeCallable() {
        // This constructor is public to permit tools that require a JavaBean instance to operate.
        // It is deprecated and scheduled to be private in a future version.
        assertNotNull(new RandomUtils());
    }
}