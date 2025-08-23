package org.threeten.extra.scale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for arithmetic and modification methods of {@link TaiInstant}.
 */
class TaiInstantArithmeticTest {

    private static final int NANOS_PER_SECOND = 1_000_000_000;
    private static final int ONE_THIRD_NANOS = 333_333_333;
    private static final int TWO_THIRDS_NANOS = 666_666_667;

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------

    private static Stream<Arguments> provideInvalidStringsForParsing() {
        return Stream.of(
                Arguments.of("A.123456789s(TAI)"), // Non-digit in seconds
                Arguments.of("123.12345678As(TAI)"), // Non-digit in nanos
                Arguments.of("123.123456789"),      // Missing suffix
                Arguments.of("123.123456789s"),       // Missing scale
                Arguments.of("+123.123456789s(TAI)"),// Explicit positive sign not allowed
                Arguments.of("-123.123s(TAI)")       // Nanos must be 9 digits
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidStringsForParsing")
    void parse_withInvalidFormat_throwsDateTimeParseException(String invalidString) {
        assertThrows(DateTimeParseException.class, () -> TaiInstant.parse(invalidString));
    }

    //-----------------------------------------------------------------------
    // withTaiSeconds()
    //-----------------------------------------------------------------------

    private static Stream<Arguments> provideArgumentsForWithTaiSeconds() {
        // Arguments: initialSeconds, initialNanos, newSeconds, expectedSeconds, expectedNanos
        return Stream.of(
                Arguments.of(0L, 12345L, 1L, 1L, 12345L),
                Arguments.of(0L, 12345L, -1L, -1L, 12345L),
                Arguments.of(7L, 12345L, 2L, 2L, 12345L)
        );
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForWithTaiSeconds")
    void withTaiSeconds_shouldSetSecondsAndKeepNanos(
            long initialSeconds, long initialNanos, long newSeconds, long expectedSeconds, long expectedNanos) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        TaiInstant result = initial.withTaiSeconds(newSeconds);
        assertEquals(expectedSeconds, result.getTaiSeconds());
        assertEquals(expectedNanos, result.getNano());
    }

    //-----------------------------------------------------------------------
    // withNano()
    //-----------------------------------------------------------------------

    private static Stream<Arguments> provideValidArgumentsForWithNano() {
        // Arguments: initialSeconds, initialNanos, newNano, expectedSeconds, expectedNanos
        return Stream.of(
                Arguments.of(0L, 12345L, 1, 0L, 1L),
                Arguments.of(7L, 12345L, 2, 7L, 2L),
                Arguments.of(-99L, 12345L, 3, -99L, 3L),
                Arguments.of(-99L, 12345L, 999_999_999, -99L, 999_999_999L)
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidArgumentsForWithNano")
    void withNano_shouldSetNanosAndKeepSeconds(
            long initialSeconds, long initialNanos, int newNano, long expectedSeconds, long expectedNanos) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        TaiInstant result = initial.withNano(newNano);
        assertEquals(expectedSeconds, result.getTaiSeconds());
        assertEquals(expectedNanos, result.getNano());
    }

    private static Stream<Arguments> provideInvalidArgumentsForWithNano() {
        // Arguments: initialSeconds, initialNanos, invalidNano
        return Stream.of(
                Arguments.of(-99L, 12345L, -1),
                Arguments.of(-99L, 12345L, NANOS_PER_SECOND)
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidArgumentsForWithNano")
    void withNano_withOutOfRangeValue_throwsIllegalArgumentException(long initialSeconds, long initialNanos, int invalidNano) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        assertThrows(IllegalArgumentException.class, () -> initial.withNano(invalidNano));
    }

    //-----------------------------------------------------------------------
    // plus(Duration)
    //-----------------------------------------------------------------------

    private static Stream<Arguments> provideArgumentsForPlus() {
        // A comprehensive set of arguments to test addition logic, including carry-overs.
        // Columns: initialSec, initialNano, plusSec, plusNano, expectedSec, expectedNano
        return Stream.of(
                // Edge case: overflow
                Arguments.of(Long.MIN_VALUE, 0, Long.MAX_VALUE, 0, -1L, 0),

                // --- Test cases for initial instant: -4s and 2/3 nanos ---
                // Adding a duration that causes nano part to become 1 (carry from nanos)
                Arguments.of(-4L, TWO_THIRDS_NANOS, -1L, ONE_THIRD_NANOS + 1, -4L, 1),
                // Adding a duration that causes nano part to become 0 (carry from nanos)
                Arguments.of(-4L, TWO_THIRDS_NANOS, 0L, ONE_THIRD_NANOS, -3L, 0),
                // Adding a simple positive duration
                Arguments.of(-4L, TWO_THIRDS_NANOS, 1L, 0, -3L, TWO_THIRDS_NANOS),

                // --- Test cases for initial instant: -3s and 0 nanos ---
                Arguments.of(-3L, 0, -1L, 0, -4L, 0),
                Arguments.of(-3L, 0, 0L, 1, -3L, 1),
                Arguments.of(-3L, 0, 3L, 0, 0L, 0),

                // --- Test cases for initial instant: 0s and 1/3 nanos ---
                // Adding a duration that causes nano part to become 0 (carry from nanos)
                Arguments.of(0L, ONE_THIRD_NANOS, -1L, TWO_THIRDS_NANOS, 0L, 0),
                // Adding a simple positive duration
                Arguments.of(0L, ONE_THIRD_NANOS, 1L, 0, 1L, ONE_THIRD_NANOS),

                // --- Test cases for initial instant: 3s and 0 nanos ---
                Arguments.of(3L, 0, -4L, TWO_THIRDS_NANOS, -1L, TWO_THIRDS_NANOS),
                Arguments.of(3L, 0, -3L, 0, 0L, 0),
                Arguments.of(3L, 0, 3L, 0, 6L, 0),

                // Edge case: overflow
                Arguments.of(Long.MAX_VALUE, 0, Long.MIN_VALUE, 0, -1L, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForPlus")
    void plus_duration_shouldCalculateCorrectResult(
            long initialSeconds, int initialNanos, long secondsToAdd, int nanosToAdd, long expectedSeconds, int expectedNanos) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        Duration durationToAdd = Duration.ofSeconds(secondsToAdd, nanosToAdd);
        TaiInstant result = initial.plus(durationToAdd);

        assertEquals(expectedSeconds, result.getTaiSeconds());
        assertEquals(expectedNanos, result.getNano());
    }

    //-----------------------------------------------------------------------
    // minus(Duration)
    //-----------------------------------------------------------------------

    private static Stream<Arguments> provideArgumentsForMinus() {
        // A comprehensive set of arguments to test subtraction logic, including borrowing.
        // Columns: initialSec, initialNano, minusSec, minusNano, expectedSec, expectedNano
        return Stream.of(
                // Edge case: overflow
                Arguments.of(Long.MIN_VALUE, 0, Long.MIN_VALUE + 1, 0, -1L, 0),

                // --- Test cases for initial instant: -4s and 2/3 nanos ---
                // Subtracting a duration that results in 0
                Arguments.of(-4L, TWO_THIRDS_NANOS, -4L, TWO_THIRDS_NANOS, 0L, 0),
                // Subtracting a duration that requires borrowing from seconds
                Arguments.of(-4L, TWO_THIRDS_NANOS, 0L, TWO_THIRDS_NANOS + 1, -5L, NANOS_PER_SECOND - 1),
                // Subtracting a simple positive duration
                Arguments.of(-4L, TWO_THIRDS_NANOS, 1L, 0, -5L, TWO_THIRDS_NANOS),

                // --- Test cases for initial instant: -3s and 0 nanos ---
                Arguments.of(-3L, 0, -3L, 0, 0L, 0),
                // Subtracting 1 nano requires borrowing from seconds
                Arguments.of(-3L, 0, 0L, 1, -4L, NANOS_PER_SECOND - 1),
                Arguments.of(-3L, 0, 1L, 0, -4L, 0),

                // --- Test cases for initial instant: 0s and 1/3 nanos ---
                // Subtracting a duration that results in 0
                Arguments.of(0L, ONE_THIRD_NANOS, 0L, ONE_THIRD_NANOS, 0L, 0),
                // Subtracting a duration that requires borrowing from seconds
                Arguments.of(0L, ONE_THIRD_NANOS, 0L, TWO_THIRDS_NANOS, -1L, TWO_THIRDS_NANOS),

                // --- Test cases for initial instant: 3s and 0 nanos ---
                Arguments.of(3L, 0, -3L, 0, 6L, 0),
                Arguments.of(3L, 0, 3L, 0, 0L, 0),
                // Subtracting 1 nano requires borrowing from seconds
                Arguments.of(3L, 0, 0L, 1, 2L, NANOS_PER_SECOND - 1),

                // Edge case: no overflow
                Arguments.of(Long.MAX_VALUE, 0, Long.MAX_VALUE, 0, 0L, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForMinus")
    void minus_duration_shouldCalculateCorrectResult(
            long initialSeconds, int initialNanos, long secondsToSubtract, int nanosToSubtract, long expectedSeconds, int expectedNanos) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        Duration durationToSubtract = Duration.ofSeconds(secondsToSubtract, nanosToSubtract);
        TaiInstant result = initial.minus(durationToSubtract);

        assertEquals(expectedSeconds, result.getTaiSeconds());
        assertEquals(expectedNanos, result.getNano());
    }

    //-----------------------------------------------------------------------
    // compareTo()
    //-----------------------------------------------------------------------

    @Test
    void compareTo_withNull_throwsNullPointerException() {
        TaiInstant instant = TaiInstant.ofTaiSeconds(0L, 0);
        assertThrows(NullPointerException.class, () -> instant.compareTo(null));
    }
}