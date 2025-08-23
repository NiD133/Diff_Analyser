package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Tests for RandomUtils")
class RandomUtilsTest extends AbstractLangTest {

    /**
     * For comparing doubles and floats.
     */
    private static final double DELTA = 1e-5;
    private static Stream<RandomUtils> randomProvider;

    @BeforeAll
    static void setup() {
        randomProvider = Stream.of(RandomUtils.secure(), RandomUtils.secureStrong(), RandomUtils.insecure());
    }

    @Nested
    @DisplayName("randomBoolean()")
    class RandomBooleanTests {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#setup")
        void randomBoolean_shouldNotFail(final RandomUtils ru) {
            // This test simply ensures the method can be called on all RandomUtils types without throwing an exception.
            // A true/false result is guaranteed by the language, so asserting it is redundant.
            ru.randomBoolean();
        }
    }

    @Nested
    @DisplayName("randomBytes()")
    class RandomBytesTests {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#setup")
        void randomBytes_shouldReturnArrayOfGivenSize(final RandomUtils ru) {
            final byte[] result = ru.randomBytes(20);
            assertEquals(20, result.length);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#setup")
        void randomBytes_shouldReturnEmptyArray_forZeroSize(final RandomUtils ru) {
            assertArrayEquals(new byte[0], ru.randomBytes(0));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#setup")
        void randomBytes_shouldThrowException_forNegativeSize(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomBytes(-1));
        }
    }

    @Nested
    @DisplayName("randomDouble()")
    class RandomDoubleTests {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#setup")
        void randomDouble_shouldReturnPositiveValue_whenNoRangeIsGiven(final RandomUtils ru) {
            final double result = ru.randomDouble();
            assertTrue(result >= 0d && result < Double.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#setup")
        void randomDouble_shouldReturnValueInSpecifiedRange(final RandomUtils ru) {
            final double result = ru.randomDouble(33d, 42d);
            assertTrue(result >= 33d && result < 42d);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#setup")
        void randomDouble_shouldHandleExtremeRange(final RandomUtils ru) {
            final double result = ru.randomDouble(0, Double.MAX_VALUE);
            assertTrue(result >= 0d && result < Double.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#setup")
        void randomDouble_shouldReturnStart_whenRangeIsZero(final RandomUtils ru) {
            assertEquals(42.1, ru.randomDouble(42.1, 42.1), DELTA);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#setup")
        void randomDouble_shouldThrowException_whenStartIsAfterEnd(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomDouble(2, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#setup")
        void randomDouble_shouldThrowException_whenStartIsNegative(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomDouble(-1, 1));
        }
    }

    @Nested
    @DisplayName("randomFloat()")
    class RandomFloatTests {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#setup")
        void randomFloat_shouldReturnPositiveValue_whenNoRangeIsGiven(final RandomUtils ru) {
            final float result = ru.randomFloat();
            assertTrue(result >= 0f && result < Float.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#setup")
        void randomFloat_shouldReturnValueInSpecifiedRange(final RandomUtils ru) {
            final float result = ru.randomFloat(33f, 42f);
            assertTrue(result >= 33f && result < 42f);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#setup")
        void randomFloat_shouldHandleExtremeRange(final RandomUtils ru) {
            final float result = ru.randomFloat(0, Float.MAX_VALUE);
            assertTrue(result >= 0f && result < Float.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#setup")
        void randomFloat_shouldReturnStart_whenRangeIsZero(final RandomUtils ru) {
            assertEquals(42.1f, ru.randomFloat(42.1f, 42.1f), DELTA);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#setup")
        void randomFloat_shouldThrowException_whenStartIsAfterEnd(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomFloat(2, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#setup")
        void randomFloat_shouldThrowException_whenStartIsNegative(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomFloat(-1, 1));
        }
    }

    @Nested
    @DisplayName("randomInt()")
    class RandomIntTests {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#setup")
        void randomInt_shouldReturnPositiveValue_whenNoRangeIsGiven(final RandomUtils ru) {
            final int result = ru.randomInt();
            assertTrue(result >= 0 && result < Integer.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#setup")
        void randomInt_shouldReturnIntegerInSpecifiedRange(final RandomUtils ru) {
            final int result = ru.randomInt(33, 42);
            assertTrue(result >= 33 && result < 42);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#setup")
        void randomInt_shouldHandleExtremeRange(final RandomUtils ru) {
            final int result = ru.randomInt(0, Integer.MAX_VALUE);
            assertTrue(result >= 0 && result < Integer.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#setup")
        void randomInt_shouldReturnStart_whenRangeIsZero(final RandomUtils ru) {
            assertEquals(42, ru.randomInt(42, 42));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#setup")
        void randomInt_shouldThrowException_whenStartIsAfterEnd(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomInt(2, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#setup")
        void randomInt_shouldThrowException_whenStartIsNegative(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomInt(-1, 1));
        }
    }

    @Nested
    @DisplayName("randomLong()")
    class RandomLongTests {
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#setup")
        void randomLong_shouldReturnPositiveValue_whenNoRangeIsGiven(final RandomUtils ru) {
            final long result = ru.randomLong();
            assertTrue(result >= 0L && result < Long.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#setup")
        void randomLong_shouldReturnValueInSpecifiedRange(final RandomUtils ru) {
            final long result = ru.randomLong(33L, 42L);
            assertTrue(result >= 33L && result < 42L);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#setup")
        void randomLong_shouldHandleExtremeRange(final RandomUtils ru) {
            final long result = ru.randomLong(0, Long.MAX_VALUE);
            assertTrue(result >= 0L && result < Long.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#setup")
        void randomLong_shouldReturnStart_whenRangeIsZero(final RandomUtils ru) {
            assertEquals(42L, ru.randomLong(42L, 42L));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#setup")
        void randomLong_shouldThrowException_whenStartIsAfterEnd(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomLong(2, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#setup")
        void randomLong_shouldThrowException_whenStartIsNegative(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomLong(-1, 1));
        }

        /**
         * Test a large value for long to prevent regression of LANG-1592. A previous
         * implementation using {@code (long) nextDouble(startInclusive, endExclusive)}
         * could incorrectly generate a value equal to the upper limit.
         */
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#setup")
        void randomLong_shouldNotReturnEndExclusive_forLargeRanges(final RandomUtils ru) {
            final long startInclusive = 12900000000001L;
            final long endExclusive = 12900000000016L;
            // The method is called many times to increase the probability of finding
            // the edge-case bug if it is reintroduced.
            final int n = (int) (endExclusive - startInclusive) * 1000;
            for (int i = 0; i < n; i++) {
                assertNotEquals(endExclusive, ru.randomLong(startInclusive, endExclusive));
            }
        }
    }

    @Nested
    @DisplayName("Tests for deprecated static methods")
    class DeprecatedStaticMethodsTests {
        @Test
        @Deprecated
        @DisplayName("static nextBytes() should throw exception for negative count")
        void staticNextBytes_shouldThrowException_forNegativeCount() {
            assertIllegalArgumentException(() -> RandomUtils.nextBytes(-1));
        }
    }
}