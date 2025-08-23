package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link RandomUtils}.
 */
public class RandomUtilsTest extends AbstractLangTest {

    /**
     * Delta for comparing doubles and floats.
     */
    private static final double DELTA = 1e-5;

    /**
     * Provides instances of secure, secure-strong, and insecure RandomUtils.
     */
    static Stream<RandomUtils> randomProvider() {
        return Stream.of(RandomUtils.secure(), RandomUtils.secureStrong(), RandomUtils.insecure());
    }

    // --- randomBoolean ---

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomBoolean_doesNotThrow(final RandomUtils randomUtils) {
        // This is a smoke test to ensure the method executes without error.
        final boolean result = randomUtils.randomBoolean();
        assertTrue(result || !result);
    }

    // --- randomBytes ---

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomBytes_returnsArrayOfCorrectSize(final RandomUtils randomUtils) {
        final byte[] result = randomUtils.randomBytes(20);
        assertEquals(20, result.length);
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomBytes_withZeroCount_returnsEmptyArray(final RandomUtils randomUtils) {
        assertArrayEquals(new byte[0], randomUtils.randomBytes(0));
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomBytes_withNegativeCount_throwsIllegalArgumentException(final RandomUtils randomUtils) {
        assertIllegalArgumentException(() -> randomUtils.randomBytes(-1));
    }

    // --- randomDouble ---

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomDouble_withNoArgs(final RandomUtils randomUtils) {
        final double result = randomUtils.randomDouble();
        assertTrue(result >= 0d && result < Double.MAX_VALUE);
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomDouble_withValidRange(final RandomUtils randomUtils) {
        final double result = randomUtils.randomDouble(33d, 42d);
        assertTrue(result >= 33d && result < 42d);
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomDouble_withMaxRange(final RandomUtils randomUtils) {
        final double result = randomUtils.randomDouble(0, Double.MAX_VALUE);
        assertTrue(result >= 0 && result < Double.MAX_VALUE);
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomDouble_withStartEqualsEnd(final RandomUtils randomUtils) {
        assertEquals(42.1, randomUtils.randomDouble(42.1, 42.1), DELTA);
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomDouble_withInvalidRange_throwsIllegalArgumentException(final RandomUtils randomUtils) {
        assertIllegalArgumentException(() -> randomUtils.randomDouble(2, 1));
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomDouble_withNegativeStart_throwsIllegalArgumentException(final RandomUtils randomUtils) {
        assertIllegalArgumentException(() -> randomUtils.randomDouble(-1, 1));
    }

    // --- randomFloat ---

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomFloat_withNoArgs(final RandomUtils randomUtils) {
        final float result = randomUtils.randomFloat();
        assertTrue(result >= 0f && result < Float.MAX_VALUE);
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomFloat_withValidRange(final RandomUtils randomUtils) {
        final float result = randomUtils.randomFloat(33f, 42f);
        assertTrue(result >= 33f && result < 42f);
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomFloat_withMaxRange(final RandomUtils randomUtils) {
        final float result = randomUtils.randomFloat(0, Float.MAX_VALUE);
        assertTrue(result >= 0f && result < Float.MAX_VALUE);
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomFloat_withStartEqualsEnd(final RandomUtils randomUtils) {
        assertEquals(42.1f, randomUtils.randomFloat(42.1f, 42.1f), DELTA);
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomFloat_withInvalidRange_throwsIllegalArgumentException(final RandomUtils randomUtils) {
        assertIllegalArgumentException(() -> randomUtils.randomFloat(2, 1));
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomFloat_withNegativeStart_throwsIllegalArgumentException(final RandomUtils randomUtils) {
        assertIllegalArgumentException(() -> randomUtils.randomFloat(-1, 1));
    }

    // --- randomInt ---

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomInt_withNoArgs(final RandomUtils randomUtils) {
        final int result = randomUtils.randomInt();
        assertTrue(result >= 0 && result < Integer.MAX_VALUE);
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomInt_withValidRange(final RandomUtils randomUtils) {
        final int result = randomUtils.randomInt(33, 42);
        assertTrue(result >= 33 && result < 42);
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomInt_withMaxRange(final RandomUtils randomUtils) {
        final int result = randomUtils.randomInt(0, Integer.MAX_VALUE);
        assertTrue(result >= 0 && result < Integer.MAX_VALUE);
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomInt_withStartEqualsEnd(final RandomUtils randomUtils) {
        assertEquals(42, randomUtils.randomInt(42, 42));
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomInt_withInvalidRange_throwsIllegalArgumentException(final RandomUtils randomUtils) {
        assertIllegalArgumentException(() -> randomUtils.randomInt(2, 1));
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomInt_withNegativeStart_throwsIllegalArgumentException(final RandomUtils randomUtils) {
        assertIllegalArgumentException(() -> randomUtils.randomInt(-1, 1));
    }

    // --- randomLong ---

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomLong_withNoArgs(final RandomUtils randomUtils) {
        final long result = randomUtils.randomLong();
        assertTrue(result >= 0L && result < Long.MAX_VALUE);
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomLong_withValidRange(final RandomUtils randomUtils) {
        final long result = randomUtils.randomLong(33L, 42L);
        assertTrue(result >= 33L && result < 42L);
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomLong_withMaxRange(final RandomUtils randomUtils) {
        final long result = randomUtils.randomLong(0, Long.MAX_VALUE);
        assertTrue(result >= 0 && result < Long.MAX_VALUE);
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomLong_withStartEqualsEnd(final RandomUtils randomUtils) {
        assertEquals(42L, randomUtils.randomLong(42L, 42L));
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomLong_withInvalidRange_throwsIllegalArgumentException(final RandomUtils randomUtils) {
        assertIllegalArgumentException(() -> randomUtils.randomLong(2, 1));
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomLong_withNegativeStart_throwsIllegalArgumentException(final RandomUtils randomUtils) {
        assertIllegalArgumentException(() -> randomUtils.randomLong(-1, 1));
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
    @MethodSource("randomProvider")
    void testLargeValueRangeLong(final RandomUtils randomUtils) {
        final long startInclusive = 12900000000001L;
        final long endExclusive = 12900000000016L;
        // Note: The method using 'return (long) nextDouble(startInclusive, endExclusive)'
        // takes thousands of calls to generate an error. This size loop fails most
        // of the time with the previous method.
        final int n = (int) (endExclusive - startInclusive) * 1000;
        for (int i = 0; i < n; i++) {
            assertNotEquals(endExclusive, randomUtils.randomLong(startInclusive, endExclusive));
        }
    }

    /**
     * Tests the deprecated static nextInt() method for basic functionality.
     * This is kept for coverage of the deprecated API.
     */
    @Test
    @SuppressWarnings("deprecation")
    void testStaticNextInt_withNoArgs() {
        final int result = RandomUtils.nextInt();
        assertTrue(result >= 0 && result < Integer.MAX_VALUE);
    }
}