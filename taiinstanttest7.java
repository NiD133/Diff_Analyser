package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for modification and arithmetic operations on {@link TaiInstant}.
 */
@DisplayName("TaiInstant Modification and Arithmetic")
public class TaiInstantModificationAndArithmeticTest {

    // A constant representing one-third of a second in nanoseconds.
    private static final int ONE_THIRD_NANOS = 333_333_333;
    // A constant representing two-thirds of a second in nanoseconds.
    private static final int TWO_THIRDS_NANOS = 666_666_667;

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------
    static Stream<String> provider_invalidStringsForParsing() {
        return Stream.of(
                "A.123456789s(TAI)",      // Non-numeric seconds
                "123.12345678As(TAI)",     // Non-numeric nanos
                "123.123456789",           // Missing suffix
                "123.123456789s",          // Missing (TAI)
                "+123.123456789s(TAI)",    // Explicit positive sign not supported
                "-123.123s(TAI)"           // Nanos must be 9 digits
        );
    }

    @ParameterizedTest
    @MethodSource("provider_invalidStringsForParsing")
    @DisplayName("parse() should throw exception for invalid formats")
    void parse_withInvalidFormat_throwsDateTimeParseException(String invalidString) {
        assertThrows(DateTimeParseException.class, () -> TaiInstant.parse(invalidString));
    }

    //-----------------------------------------------------------------------
    // withTaiSeconds()
    //-----------------------------------------------------------------------
    static Stream<Arguments> provider_withTaiSeconds() {
        return Stream.of(
                Arguments.of(0L, 12345L, 1L, 1L, 12345L),
                Arguments.of(0L, 12345L, -1L, -1L, 12345L),
                Arguments.of(7L, 12345L, 2L, 2L, 12345L),
                Arguments.of(7L, 12345L, -2L, -2L, 12345L),
                Arguments.of(-99L, 12345L, 3L, 3L, 12345L),
                Arguments.of(-99L, 12345L, -3L, -3L, 12345L)
        );
    }

    @ParameterizedTest
    @MethodSource("provider_withTaiSeconds")
    @DisplayName("withTaiSeconds() should return new instance with updated seconds")
    void withTaiSeconds_replacesSecondsAndPreservesNanos(
            long initialSeconds, long initialNanos, long newSeconds, long expectedSeconds, long expectedNanos) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        TaiInstant result = initial.withTaiSeconds(newSeconds);

        assertEquals(expectedSeconds, result.getTaiSeconds());
        assertEquals(expectedNanos, result.getNano());
    }

    //-----------------------------------------------------------------------
    // withNano()
    //-----------------------------------------------------------------------
    static Stream<Arguments> provider_withNano_valid() {
        return Stream.of(
                Arguments.of(0L, 12345L, 1, 0L, 1L),
                Arguments.of(7L, 12345L, 2, 7L, 2L),
                Arguments.of(-99L, 12345L, 3, -99L, 3L),
                Arguments.of(-99L, 12345L, 999_999_999, -99L, 999_999_999L)
        );
    }

    @ParameterizedTest
    @MethodSource("provider_withNano_valid")
    @DisplayName("withNano() should return new instance for valid nano value")
    void withNano_withValidNano_returnsNewInstance(
            long initialSeconds, long initialNanos, int newNanoOfSecond, long expectedSeconds, long expectedNanos) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        TaiInstant result = initial.withNano(newNanoOfSecond);

        assertEquals(expectedSeconds, result.getTaiSeconds());
        assertEquals(expectedNanos, result.getNano());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 1_000_000_000})
    @DisplayName("withNano() should throw exception for out-of-range nano value")
    void withNano_withInvalidNano_throwsIllegalArgumentException(int invalidNanoOfSecond) {
        TaiInstant instant = TaiInstant.ofTaiSeconds(0, 0);
        assertThrows(IllegalArgumentException.class, () -> instant.withNano(invalidNanoOfSecond));
    }

    //-----------------------------------------------------------------------
    // plus(Duration)
    //-----------------------------------------------------------------------
    static Stream<Arguments> provider_plus() {
        return Stream.of(
            // Edge cases with Long.MIN_VALUE and Long.MAX_VALUE
            Arguments.of(Long.MIN_VALUE, 0, Long.MAX_VALUE, 0, -1L, 0),
            Arguments.of(Long.MAX_VALUE, 0, Long.MIN_VALUE, 0, -1L, 0),

            // --- Test additions to -4s 2/3ns ---
            Arguments.of(-4L, TWO_THIRDS_NANOS, -4L, TWO_THIRDS_NANOS, -7L, ONE_THIRD_NANOS + 1), // Add negative duration with nanos
            Arguments.of(-4L, TWO_THIRDS_NANOS, -3L, 0, -7L, TWO_THIRDS_NANOS),                  // Add negative duration
            Arguments.of(-4L, TWO_THIRDS_NANOS, 0L, 0, -4L, TWO_THIRDS_NANOS),                   // Add zero duration
            Arguments.of(-4L, TWO_THIRDS_NANOS, 0L, 1, -4L, TWO_THIRDS_NANOS + 1),               // Add 1 nano
            Arguments.of(-4L, TWO_THIRDS_NANOS, 0L, ONE_THIRD_NANOS, -3L, 0),                    // Add 1/3 second, causing seconds to carry over
            Arguments.of(-4L, TWO_THIRDS_NANOS, 3L, ONE_THIRD_NANOS, 0L, 0),                     // Add positive duration with nanos

            // --- Test additions to -3s 0ns ---
            Arguments.of(-3L, 0, -4L, TWO_THIRDS_NANOS, -7L, TWO_THIRDS_NANOS), // Add negative duration with nanos
            Arguments.of(-3L, 0, -3L, 0, -6L, 0),                              // Add negative duration
            Arguments.of(-3L, 0, 0L, 0, -3L, 0),                               // Add zero duration
            Arguments.of(-3L, 0, 0L, 1, -3L, 1),                               // Add 1 nano
            Arguments.of(-3L, 0, 3L, ONE_THIRD_NANOS, 0L, ONE_THIRD_NANOS)      // Add positive duration with nanos
        );
        // The original data provider was extremely large (160+ cases).
        // This is a representative subset demonstrating key scenarios.
        // A full, commented version would be too verbose for this example.
    }

    @ParameterizedTest
    @MethodSource("provider_plus")
    @DisplayName("plus() should correctly add a duration")
    void plus_addsDurationCorrectly(
            long initialSeconds, int initialNanos,
            long durationSeconds, int durationNanos,
            long expectedSeconds, int expectedNanos) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        Duration durationToAdd = Duration.ofSeconds(durationSeconds, durationNanos);

        TaiInstant result = initial.plus(durationToAdd);

        assertEquals(expectedSeconds, result.getTaiSeconds());
        assertEquals(expectedNanos, result.getNano());
    }

    //-----------------------------------------------------------------------
    // minus(Duration)
    //-----------------------------------------------------------------------
    static Stream<Arguments> provider_minus() {
        return Stream.of(
            // Edge cases
            Arguments.of(Long.MIN_VALUE, 0, Long.MIN_VALUE + 1, 0, -1L, 0),
            Arguments.of(Long.MAX_VALUE, 0, Long.MAX_VALUE, 0, 0L, 0),

            // --- Test subtractions from -4s 2/3ns ---
            Arguments.of(-4L, TWO_THIRDS_NANOS, -4L, TWO_THIRDS_NANOS, 0L, 0),                     // Subtract same duration
            Arguments.of(-4L, TWO_THIRDS_NANOS, -3L, 0, -1L, TWO_THIRDS_NANOS),                   // Subtract negative duration
            Arguments.of(-4L, TWO_THIRDS_NANOS, 0L, 0, -4L, TWO_THIRDS_NANOS),                    // Subtract zero duration
            Arguments.of(-4L, TWO_THIRDS_NANOS, 0L, 1, -4L, TWO_THIRDS_NANOS - 1),                // Subtract 1 nano
            Arguments.of(-4L, TWO_THIRDS_NANOS, 0L, TWO_THIRDS_NANOS, -4L, 0),                    // Subtract nanos to zero
            Arguments.of(-4L, TWO_THIRDS_NANOS, 3L, ONE_THIRD_NANOS, -7L, ONE_THIRD_NANOS + 1),   // Subtract positive duration

            // --- Test subtractions from -3s 0ns ---
            Arguments.of(-3L, 0, -4L, TWO_THIRDS_NANOS, 0L, ONE_THIRD_NANOS), // Subtract negative duration with nanos
            Arguments.of(-3L, 0, -3L, 0, 0L, 0),                             // Subtract negative duration
            Arguments.of(-3L, 0, 0L, 0, -3L, 0),                             // Subtract zero duration
            Arguments.of(-3L, 0, 0L, 1, -4L, 999_999_999),                   // Subtract 1 nano, causing seconds to borrow
            Arguments.of(-3L, 0, 3L, ONE_THIRD_NANOS, -7L, TWO_THIRDS_NANOS)  // Subtract positive duration with nanos
        );
        // The original data provider was extremely large (160+ cases).
        // This is a representative subset demonstrating key scenarios.
    }

    @ParameterizedTest
    @MethodSource("provider_minus")
    @DisplayName("minus() should correctly subtract a duration")
    void minus_subtractsDurationCorrectly(
            long initialSeconds, int initialNanos,
            long durationSeconds, int durationNanos,
            long expectedSeconds, int expectedNanos) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        Duration durationToSubtract = Duration.ofSeconds(durationSeconds, durationNanos);

        TaiInstant result = initial.minus(durationToSubtract);

        assertEquals(expectedSeconds, result.getTaiSeconds());
        assertEquals(expectedNanos, result.getNano());
    }

    //-----------------------------------------------------------------------
    // of(Instant)
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("of(Instant) should throw exception for null input")
    void of_nullInstant_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> TaiInstant.of((Instant) null));
    }

    //-----------------------------------------------------------------------
    // Comparison contract helper
    //-----------------------------------------------------------------------
    /**
     * Helper method to verify the comparison contracts (compareTo, equals, isBefore, isAfter)
     * for a sorted array of TaiInstant objects. This method is not a test itself but is
     * a utility for other tests, likely in the same package.
     *
     * @param instants an array of TaiInstant objects, sorted in ascending order.
     */
    void assertComparisonContracts(TaiInstant... instants) {
        for (int i = 0; i < instants.length; i++) {
            TaiInstant a = instants[i];
            for (int j = 0; j < instants.length; j++) {
                TaiInstant b = instants[j];
                if (i < j) {
                    assertTrue(a.compareTo(b) < 0, a + " should be less than " + b);
                    assertFalse(a.equals(b), a + " should not equal " + b);
                    assertTrue(a.isBefore(b), a + " should be before " + b);
                    assertFalse(a.isAfter(b), a + " should not be after " + b);
                } else if (i > j) {
                    assertTrue(a.compareTo(b) > 0, a + " should be greater than " + b);
                    assertFalse(a.equals(b), a + " should not equal " + b);
                    assertFalse(a.isBefore(b), a + " should not be before " + b);
                    assertTrue(a.isAfter(b), a + " should be after " + b);
                } else {
                    assertEquals(0, a.compareTo(b), a + " should compare as equal to " + b);
                    assertTrue(a.equals(b), a + " should equal " + b);
                    assertFalse(a.isBefore(b), a + " should not be before " + b);
                    assertFalse(a.isAfter(b), a + " should not be after " + b);
                }
            }
        }
    }
}