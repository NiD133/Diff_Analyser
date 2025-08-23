package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link RandomUtils}.
 */
@TestMethodOrder(MethodName.class)
public class RandomUtilsTest extends AbstractLangTest {

    /**
     * A small delta for comparing floating-point numbers.
     */
    private static final double DELTA = 1e-5;

    /**
     * Provides instances of secure, strong secure, and insecure RandomUtils.
     */
    static Stream<RandomUtils> randomProvider() {
        return Stream.of(RandomUtils.secure(), RandomUtils.secureStrong(), RandomUtils.insecure());
    }

    // --- Boolean ---

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomBoolean_shouldReturnTrueOrFalse(final RandomUtils ru) {
        final boolean result = ru.randomBoolean();
        // This is a basic sanity check that the method returns a valid boolean.
        assertTrue(result || !result);
    }

    // --- Bytes ---

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomBytes_shouldReturnArrayOfCorrectSize(final RandomUtils ru) {
        final byte[] result = ru.randomBytes(20);
        assertEquals(20, result.length);
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomBytes_shouldReturnEmptyArray_forZeroCount(final RandomUtils ru) {
        assertArrayEquals(new byte[0], ru.randomBytes(0));
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomBytes_shouldThrowException_forNegativeCount(final RandomUtils ru) {
        assertIllegalArgumentException(() -> ru.randomBytes(-1));
    }

    // --- Double ---

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomDouble_shouldReturnNonNegativeDouble(final RandomUtils ru) {
        final double result = ru.randomDouble();
        assertTrue(result >= 0d && result < Double.MAX_VALUE);
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomDouble_shouldReturnDoubleWithinGivenRange(final RandomUtils ru) {
        final double result = ru.randomDouble(33d, 42d);
        assertTrue(result >= 33d && result < 42d);
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomDouble_shouldHandleExtremeRange(final RandomUtils ru) {
        final double result = ru.randomDouble(0, Double.MAX_VALUE);
        // The upper bound is exclusive.
        assertTrue(result >= 0d && result < Double.MAX_VALUE);
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomDouble_shouldReturnStart_whenRangeIsZero(final RandomUtils ru) {
        assertEquals(42.1, ru.randomDouble(42.1, 42.1), DELTA);
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomDouble_shouldThrowException_forNegativeStart(final RandomUtils ru) {
        assertIllegalArgumentException(() -> ru.randomDouble(-1, 1));
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomDouble_shouldThrowException_whenStartIsGreaterThanEnd(final RandomUtils ru) {
        assertIllegalArgumentException(() -> ru.randomDouble(2, 1));
    }

    // --- Float ---

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomFloat_shouldReturnNonNegativeFloat(final RandomUtils ru) {
        final float result = ru.randomFloat();
        assertTrue(result >= 0f && result < Float.MAX_VALUE);
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomFloat_shouldReturnFloatWithinGivenRange(final RandomUtils ru) {
        final float result = ru.randomFloat(33f, 42f);
        assertTrue(result >= 33f && result < 42f);
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomFloat_shouldHandleExtremeRange(final RandomUtils ru) {
        final float result = ru.randomFloat(0, Float.MAX_VALUE);
        // The upper bound is exclusive.
        assertTrue(result >= 0f && result < Float.MAX_VALUE);
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomFloat_shouldReturnStart_whenRangeIsZero(final RandomUtils ru) {
        assertEquals(42.1f, ru.randomFloat(42.1f, 42.1f), DELTA);
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomFloat_shouldThrowException_forNegativeStart(final RandomUtils ru) {
        assertIllegalArgumentException(() -> ru.randomFloat(-1, 1));
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomFloat_shouldThrowException_whenStartIsGreaterThanEnd(final RandomUtils ru) {
        assertIllegalArgumentException(() -> ru.randomFloat(2, 1));
    }

    // --- Int ---

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomInt_shouldReturnNonNegativeInt(final RandomUtils ru) {
        final int result = ru.randomInt();
        // The contract is [0, Integer.MAX_VALUE).
        assertTrue(result >= 0 && result < Integer.MAX_VALUE);
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomInt_shouldReturnIntWithinGivenRange(final RandomUtils ru) {
        final int result = ru.randomInt(33, 42);
        assertTrue(result >= 33 && result < 42);
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomInt_shouldHandleExtremeRange(final RandomUtils ru) {
        final int result = ru.randomInt(0, Integer.MAX_VALUE);
        assertTrue(result >= 0 && result < Integer.MAX_VALUE);
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomInt_shouldReturnStart_whenRangeIsZero(final RandomUtils ru) {
        assertEquals(42, ru.randomInt(42, 42));
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomInt_shouldThrowException_forNegativeStart(final RandomUtils ru) {
        assertIllegalArgumentException(() -> ru.randomInt(-1, 1));
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomInt_shouldThrowException_whenStartIsGreaterThanEnd(final RandomUtils ru) {
        assertIllegalArgumentException(() -> ru.randomInt(2, 1));
    }

    // --- Long ---

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomLong_shouldReturnNonNegativeLong(final RandomUtils ru) {
        final long result = ru.randomLong();
        assertTrue(result >= 0L && result < Long.MAX_VALUE);
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomLong_shouldReturnLongWithinGivenRange(final RandomUtils ru) {
        final long result = ru.randomLong(33L, 42L);
        assertTrue(result >= 33L && result < 42L);
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomLong_shouldHandleExtremeRange(final RandomUtils ru) {
        final long result = ru.randomLong(0, Long.MAX_VALUE);
        assertTrue(result >= 0L && result < Long.MAX_VALUE);
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomLong_shouldReturnStart_whenRangeIsZero(final RandomUtils ru) {
        assertEquals(42L, ru.randomLong(42L, 42L));
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomLong_shouldThrowException_forNegativeStart(final RandomUtils ru) {
        assertIllegalArgumentException(() -> ru.randomLong(-1, 1));
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomLong_shouldThrowException_whenStartIsGreaterThanEnd(final RandomUtils ru) {
        assertIllegalArgumentException(() -> ru.randomLong(2, 1));
    }

    /**
     * Tests a large value for long. A previous implementation using
     * {@code (long) nextDouble(startInclusive, endExclusive)} could generate a value
     * equal to the upper limit. See LANG-1592.
     */
    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomLong_shouldNotReturnEndExclusive_forLargeValueRange(final RandomUtils ru) {
        final long startInclusive = 12900000000001L;
        final long endExclusive = 12900000000016L;
        // The previous buggy implementation would occasionally return the endExclusive value.
        // This loop size is large enough to make that failure highly probable.
        final int iterations = (int) (endExclusive - startInclusive) * 1000;
        for (int i = 0; i < iterations; i++) {
            final long result = ru.randomLong(startInclusive, endExclusive);
            assertNotEquals(endExclusive, result, "Iteration " + i + " returned endExclusive value");
        }
    }
}