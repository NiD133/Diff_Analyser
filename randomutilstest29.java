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

@DisplayName("RandomUtils Test Suite")
class RandomUtilsTest extends AbstractLangTest {

    /**
     * Provides the different RandomUtils instances (secure, secureStrong, insecure) for parameterized tests.
     */
    static Stream<RandomUtils> randomProvider() {
        return Stream.of(RandomUtils.secure(), RandomUtils.secureStrong(), RandomUtils.insecure());
    }

    @Nested
    @DisplayName("randomBoolean()")
    class RandomBooleanTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldReturnBooleanWithoutError(final RandomUtils ru) {
            final boolean result = ru.randomBoolean();
            // This is a smoke test to ensure the method returns a valid boolean without throwing an exception.
            assertTrue(result || !result);
        }
    }

    @Nested
    @DisplayName("randomBytes()")
    class RandomBytesTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldReturnArrayOfCorrectSize(final RandomUtils ru) {
            final byte[] result = ru.randomBytes(20);
            assertEquals(20, result.length);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldReturnEmptyArrayForZeroCount(final RandomUtils ru) {
            assertArrayEquals(new byte[0], ru.randomBytes(0));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldThrowExceptionForNegativeCount(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomBytes(-1));
        }
    }

    @Nested
    @DisplayName("randomInt()")
    class RandomIntTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldGenerateValueWithinRange(final RandomUtils ru) {
            final int result = ru.randomInt(33, 42);
            assertTrue(result >= 33 && result < 42);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldGenerateValueWithinExtremeRange(final RandomUtils ru) {
            final int result = ru.randomInt(0, Integer.MAX_VALUE);
            assertTrue(result >= 0 && result < Integer.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldGeneratePositiveValueForDefaultRange(final RandomUtils ru) {
            final int result = ru.randomInt();
            assertTrue(result >= 0 && result < Integer.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldReturnStartValueForMinimalRange(final RandomUtils ru) {
            assertEquals(42, ru.randomInt(42, 42));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldThrowExceptionForInvalidRange(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomInt(2, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldThrowExceptionForNegativeStart(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomInt(-1, 1));
        }
    }

    @Nested
    @DisplayName("randomLong()")
    class RandomLongTest {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldGenerateValueWithinRange(final RandomUtils ru) {
            final long result = ru.randomLong(33L, 42L);
            assertTrue(result >= 33L && result < 42L);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldGenerateValueWithinExtremeRange(final RandomUtils ru) {
            final long result = ru.randomLong(0, Long.MAX_VALUE);
            assertTrue(result >= 0L && result < Long.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldGeneratePositiveValueForDefaultRange(final RandomUtils ru) {
            final long result = ru.randomLong();
            assertTrue(result >= 0L && result < Long.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldReturnStartValueForMinimalRange(final RandomUtils ru) {
            assertEquals(42L, ru.randomLong(42L, 42L));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldThrowExceptionForInvalidRange(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomLong(2, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldThrowExceptionForNegativeStart(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomLong(-1, 1));
        }

        /**
         * Test a large value for long. A previous implementation using
         * {@code (long) nextDouble(startInclusive, endExclusive)} could generate a value equal
         * to the upper limit. See LANG-1592.
         */
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldNotGenerateExclusiveUpperBoundOnLargeRange(final RandomUtils ru) {
            final long startInclusive = 12900000000001L;
            final long endExclusive = 12900000000016L;
            // The previous buggy method fails this test most of the time.
            final int n = (int) (endExclusive - startInclusive) * 1000;
            for (int i = 0; i < n; i++) {
                assertNotEquals(endExclusive, ru.randomLong(startInclusive, endExclusive));
            }
        }
    }

    @Nested
    @DisplayName("randomDouble()")
    class RandomDoubleTest {
        private static final double DELTA = 1e-5;

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldGenerateValueWithinRange(final RandomUtils ru) {
            final double result = ru.randomDouble(33d, 42d);
            assertTrue(result >= 33d && result < 42d);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldGenerateValueWithinExtremeRange(final RandomUtils ru) {
            final double result = ru.randomDouble(0, Double.MAX_VALUE);
            assertTrue(result >= 0 && result < Double.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldGeneratePositiveValueForDefaultRange(final RandomUtils ru) {
            final double result = ru.randomDouble();
            assertTrue(result >= 0d && result < Double.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldReturnStartValueForMinimalRange(final RandomUtils ru) {
            assertEquals(42.1, ru.randomDouble(42.1, 42.1), DELTA);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldThrowExceptionForInvalidRange(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomDouble(2, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldThrowExceptionForNegativeStart(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomDouble(-1, 1));
        }
    }

    @Nested
    @DisplayName("randomFloat()")
    class RandomFloatTest {
        private static final double DELTA = 1e-5;

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldGenerateValueWithinRange(final RandomUtils ru) {
            final float result = ru.randomFloat(33f, 42f);
            assertTrue(result >= 33f && result < 42f);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldGenerateValueWithinExtremeRange(final RandomUtils ru) {
            final float result = ru.randomFloat(0, Float.MAX_VALUE);
            assertTrue(result >= 0f && result < Float.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldGeneratePositiveValueForDefaultRange(final RandomUtils ru) {
            final float result = ru.randomFloat();
            assertTrue(result >= 0f && result < Float.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldReturnStartValueForMinimalRange(final RandomUtils ru) {
            assertEquals(42.1f, ru.randomFloat(42.1f, 42.1f), DELTA);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldThrowExceptionForInvalidRange(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomFloat(2, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void shouldThrowExceptionForNegativeStart(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomFloat(-1, 1));
        }
    }

    /**
     * This test is retained to ensure the deprecated static method `nextLong()` continues to function
     * as expected until it is removed. It is not parameterized as it tests a static method.
     */
    @Test
    @SuppressWarnings("deprecation")
    void testDeprecatedNextLongStaticMethod() {
        final long result = RandomUtils.nextLong();
        assertTrue(result >= 0L && result < Long.MAX_VALUE);
    }
}