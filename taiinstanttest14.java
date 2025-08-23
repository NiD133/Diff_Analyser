package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for arithmetic and modification methods of {@link TaiInstant}.
 */
@DisplayName("TaiInstant Arithmetic and Modification")
class TaiInstantArithmeticTest {

    private static final int NANOS_PER_SECOND = 1_000_000_000;
    private static final int ONE_THIRD_NANOS = 333_333_333;
    private static final int TWO_THIRDS_NANOS = 666_666_667;

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------
    public static Stream<Arguments> invalidParseStrings() {
        return Stream.of(
                Arguments.of("A.123456789s(TAI)"), // Non-numeric seconds
                Arguments.of("123.12345678As(TAI)"), // Non-numeric nanos
                Arguments.of("123.123456789"),      // Missing suffix
                Arguments.of("123.123456789s"),     // Missing (TAI)
                Arguments.of("+123.123456789s(TAI)"),// Explicit positive sign not allowed
                Arguments.of("-123.123s(TAI)")      // Nanos must be 9 digits
        );
    }

    @ParameterizedTest
    @MethodSource("invalidParseStrings")
    @DisplayName("parse() with invalid format throws DateTimeParseException")
    void parse_withInvalidString_throwsException(String invalidString) {
        assertThrows(DateTimeParseException.class, () -> TaiInstant.parse(invalidString));
    }

    //-----------------------------------------------------------------------
    // withTaiSeconds()
    //-----------------------------------------------------------------------
    public static Stream<Arguments> data_withTaiSeconds() {
        return Stream.of(
                Arguments.of(0L, 12345L, 1L, 1L, 12345L),
                Arguments.of(0L, 12345L, -1L, -1L, 12345L),
                Arguments.of(7L, 12345L, 2L, 2L, 12345L)
        );
    }

    @ParameterizedTest
    @MethodSource("data_withTaiSeconds")
    @DisplayName("withTaiSeconds() should set the seconds field, keeping nanos unchanged")
    void withTaiSeconds_setsSecondsAndKeepsNanos(
            long initialSeconds, long initialNanos, long newSeconds, long expectedSeconds, long expectedNanos) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        TaiInstant result = initial.withTaiSeconds(newSeconds);
        assertEquals(expectedSeconds, result.getTaiSeconds());
        assertEquals(expectedNanos, result.getNano());
    }

    //-----------------------------------------------------------------------
    // withNano()
    //-----------------------------------------------------------------------
    public static Stream<Arguments> data_withNano_valid() {
        return Stream.of(
                Arguments.of(0L, 12345L, 1, 0L, 1L),
                Arguments.of(7L, 12345L, 2, 7L, 2L),
                Arguments.of(-99L, 12345L, 3, -99L, 3L),
                Arguments.of(-99L, 12345L, 999_999_999, -99L, 999_999_999L)
        );
    }

    @ParameterizedTest
    @MethodSource("data_withNano_valid")
    @DisplayName("withNano() should set the nano-of-second field, keeping seconds unchanged")
    void withNano_setsNanoCorrectly(
            long initialSeconds, long initialNanos, int newNano, long expectedSeconds, long expectedNanos) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        TaiInstant result = initial.withNano(newNano);
        assertEquals(expectedSeconds, result.getTaiSeconds());
        assertEquals(expectedNanos, result.getNano());
    }

    public static Stream<Arguments> data_withNano_invalid() {
        return Stream.of(
                Arguments.of(-1),
                Arguments.of(1_000_000_000)
        );
    }

    @ParameterizedTest
    @MethodSource("data_withNano_invalid")
    @DisplayName("withNano() with out-of-range value throws IllegalArgumentException")
    void withNano_withInvalidValue_throwsException(int invalidNano) {
        TaiInstant instant = TaiInstant.ofTaiSeconds(0, 0);
        assertThrows(IllegalArgumentException.class, () -> instant.withNano(invalidNano));
    }

    //-----------------------------------------------------------------------
    // plus()
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("plus() handles positive addition with nano carry-over")
    void plus_withNanoCarryOver() {
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 800_000_000);
        Duration toAdd = Duration.ofNanos(300_000_000); // 0.3 seconds
        TaiInstant result = start.plus(toAdd);

        assertEquals(11, result.getTaiSeconds());
        assertEquals(100_000_000, result.getNano()); // 800M + 300M = 1.1B
    }

    @Test
    @DisplayName("plus() handles addition that would overflow Long")
    void plus_overflow_throwsException() {
        TaiInstant max = TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 0);
        assertThrows(ArithmeticException.class, () -> max.plus(Duration.ofSeconds(1)));

        TaiInstant min = TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0);
        assertThrows(ArithmeticException.class, () -> min.plus(Duration.ofSeconds(-1)));
    }

    @Test
    @DisplayName("plus() adding Long.MAX_VALUE and Long.MIN_VALUE seconds results in -1 seconds")
    void plus_minAndMaxSeconds_isNegativeOne() {
        TaiInstant min = TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0);
        TaiInstant result = min.plus(Duration.ofSeconds(Long.MAX_VALUE));
        assertEquals(-1, result.getTaiSeconds());
        assertEquals(0, result.getNano());
    }

    /**
     * Data provider for comprehensive plus() tests.
     * Columns: initialSeconds, initialNanos, deltaSeconds, deltaNanos, expectedSeconds, expectedNanos
     */
    public static Object[][] data_plus() {
        return new Object[][]{
                // Basic cases
                {0L, 0, 1L, 0, 1L, 0},
                {0L, 0, -1L, 0, -1L, 0},
                {0L, 1, 0L, 1, 0L, 2},
                // Nano carry-over
                {-4L, TWO_THIRDS_NANOS, -4L, TWO_THIRDS_NANOS, -7L, ONE_THIRD_NANOS + 1},
                {-4L, TWO_THIRDS_NANOS, 0L, ONE_THIRD_NANOS, -3L, 0},
                // No nano carry-over
                {-3L, 0, -2L, 0, -5L, 0},
                {-3L, 0, 0L, 1, -3L, 1},
                // Edge cases near zero
                {-1L, 0, 1L, 0, 0L, 0},
                {0L, 333333333, -1L, 666666667, 0L, 0},
                // Boundary value arithmetic
                {Long.MIN_VALUE, 0, Long.MAX_VALUE, 0, -1L, 0},
                {Long.MAX_VALUE, 0, Long.MIN_VALUE, 0, -1L, 0}
        };
    }

    @ParameterizedTest(name = "[{index}] {0}s {1}ns + {2}s {3}ns = {4}s {5}ns")
    @MethodSource("data_plus")
    @DisplayName("plus() calculates correct sum for various second/nano combinations")
    void plus_calculatesCorrectSum(long seconds, int nanos, long plusSeconds, int plusNanos, long expectedSeconds, int expectedNanos) {
        TaiInstant start = TaiInstant.ofTaiSeconds(seconds, nanos);
        Duration toAdd = Duration.ofSeconds(plusSeconds, plusNanos);
        TaiInstant result = start.plus(toAdd);

        assertEquals(expectedSeconds, result.getTaiSeconds());
        assertEquals(expectedNanos, result.getNano());
    }

    //-----------------------------------------------------------------------
    // minus()
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("minus() handles subtraction with nano borrow")
    void minus_withNanoBorrow() {
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 100_000_000);
        Duration toSubtract = Duration.ofNanos(300_000_000); // 0.3 seconds
        TaiInstant result = start.minus(toSubtract);

        assertEquals(9, result.getTaiSeconds());
        assertEquals(800_000_000, result.getNano()); // 10.1s - 0.3s = 9.8s
    }

    @Test
    @DisplayName("minus() from MIN_VALUE seconds with positive nanos throws exception")
    void minus_fromMinValue_whenSubtractingNanos_throwsException() {
        TaiInstant i = TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0);
        assertThrows(ArithmeticException.class, () -> i.minus(Duration.ofNanos(1)));
    }

    @Test
    @DisplayName("minus() handles subtraction that would overflow Long")
    void minus_overflow_throwsException() {
        TaiInstant min = TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0);
        assertThrows(ArithmeticException.class, () -> min.minus(Duration.ofSeconds(1)));

        TaiInstant max = TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 0);
        assertThrows(ArithmeticException.class, () -> max.minus(Duration.ofSeconds(-1)));
    }

    /**
     * Data provider for comprehensive minus() tests.
     * Columns: initialSeconds, initialNanos, deltaSeconds, deltaNanos, expectedSeconds, expectedNanos
     */
    public static Object[][] data_minus() {
        return new Object[][]{
                // Basic cases
                {1L, 0, 1L, 0, 0L, 0},
                {1L, 0, -1L, 0, 2L, 0},
                {0L, 2, 0L, 1, 0L, 1},
                // Nano borrow
                {-3L, 0, 0L, 1, -4L, NANOS_PER_SECOND - 1},
                {-4L, TWO_THIRDS_NANOS, -1L, TWO_THIRDS_NANOS, -3L, 0},
                // No nano borrow
                {-2L, 0, -3L, 0, 1L, 0},
                {-2L, 0, 0L, 1, -3L, NANOS_PER_SECOND - 1},
                // Edge cases near zero
                {0L, 0, 1L, 0, -1L, 0},
                {0L, 0, 0L, 1, -1L, NANOS_PER_SECOND - 1},
                // Boundary value arithmetic
                {Long.MAX_VALUE, 0, Long.MAX_VALUE, 0, 0L, 0}
        };
    }

    @ParameterizedTest(name = "[{index}] {0}s {1}ns - {2}s {3}ns = {4}s {5}ns")
    @MethodSource("data_minus")
    @DisplayName("minus() calculates correct difference for various second/nano combinations")
    void minus_calculatesCorrectDifference(long seconds, int nanos, long minusSeconds, int minusNanos, long expectedSeconds, int expectedNanos) {
        TaiInstant start = TaiInstant.ofTaiSeconds(seconds, nanos);
        Duration toSubtract = Duration.ofSeconds(minusSeconds, minusNanos);
        TaiInstant result = start.minus(toSubtract);

        assertEquals(expectedSeconds, result.getTaiSeconds());
        assertEquals(expectedNanos, result.getNano());
    }
}