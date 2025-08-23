package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Test class for modification, parsing, and arithmetic operations on {@link TaiInstant}.
 */
public class TaiInstantModificationAndParsingTest {

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------
    static Stream<String> provider_invalidParseStrings() {
        return Stream.of(
            "A.123456789s(TAI)",      // Non-numeric seconds
            "123.12345678As(TAI)",     // Non-numeric nanos
            "123.123456789",           // Missing suffix
            "123.123456789s",          // Missing (TAI)
            "+123.123456789s(TAI)",    // Explicit positive sign not allowed by regex
            "-123.123s(TAI)"           // Nanos must be 9 digits
        );
    }

    @ParameterizedTest
    @MethodSource("provider_invalidParseStrings")
    public void parse_withInvalidFormat_throwsDateTimeParseException(String invalidString) {
        assertThrows(DateTimeParseException.class, () -> TaiInstant.parse(invalidString));
    }

    //-----------------------------------------------------------------------
    // withTaiSeconds()
    //-----------------------------------------------------------------------
    static Stream<Arguments> provider_withTaiSeconds() {
        // initialSeconds, initialNanos, newSeconds, expectedSeconds, expectedNanos
        return Stream.of(
            Arguments.of(0L, 12345L, 1L, 1L, 12345L),
            Arguments.of(0L, 12345L, -1L, -1L, 12345L),
            Arguments.of(7L, 12345L, 2L, 2L, 12345L)
        );
    }

    @ParameterizedTest
    @MethodSource("provider_withTaiSeconds")
    public void withTaiSeconds_replacesSecondsAndPreservesNanos(
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
        // initialSeconds, initialNanos, newNano, expectedSeconds, expectedNanos
        return Stream.of(
            Arguments.of(0L, 12345L, 1, 0L, 1L),
            Arguments.of(7L, 12345L, 2, 7L, 2L),
            Arguments.of(-99L, 12345L, 3, -99L, 3L),
            Arguments.of(-99L, 12345L, 999_999_999, -99L, 999_999_999L)
        );
    }

    @ParameterizedTest
    @MethodSource("provider_withNano_valid")
    public void withNano_replacesNanosAndPreservesSeconds(
            long initialSeconds, long initialNanos, int newNano, long expectedSeconds, long expectedNanos) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        TaiInstant result = initial.withNano(newNano);
        assertEquals(expectedSeconds, result.getTaiSeconds());
        assertEquals(expectedNanos, result.getNano());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 1_000_000_000})
    public void withNano_withInvalidNanoValue_throwsIllegalArgumentException(int invalidNano) {
        TaiInstant instant = TaiInstant.ofTaiSeconds(0, 0);
        assertThrows(IllegalArgumentException.class, () -> instant.withNano(invalidNano));
    }

    //-----------------------------------------------------------------------
    // plus(Duration)
    //-----------------------------------------------------------------------
    private static Stream<Arguments> provider_plus() {
        return Stream.of(
            // Arguments: initialSecs, initialNanos, durationSecs, durationNanos, expectedSecs, expectedNanos

            // --- Simple cases with no nano carry-over ---
            Arguments.of(0L, 0, 0L, 0, 0L, 0),
            Arguments.of(3L, 0, 2L, 0, 5L, 0),
            Arguments.of(3L, 500_000_000, 2L, 100_000_000, 5L, 600_000_000),
            Arguments.of(-3L, 0, -2L, 0, -5L, 0),

            // --- Nano-of-second carry-over ---
            Arguments.of(3L, 500_000_000, 2L, 500_000_000, 6L, 0), // Nanos sum to exactly 1 second
            Arguments.of(3L, 800_000_000, 2L, 300_000_000, 6L, 100_000_000), // Nanos sum to > 1 second
            Arguments.of(-3L, 800_000_000, 0L, 300_000_000, -2L, 100_000_000), // Carry with negative seconds

            // --- Negative duration (subtraction) with no nano borrow ---
            Arguments.of(3L, 500_000_000, -2L, -100_000_000, 1L, 400_000_000),

            // --- Negative duration (subtraction) with nano borrow ---
            Arguments.of(3L, 100_000_000, -2L, -200_000_000, 0L, 900_000_000),
            Arguments.of(0L, 100_000_000, 0L, -200_000_000, -1L, 900_000_000), // Borrow from zero seconds

            // --- Boundary conditions ---
            Arguments.of(Long.MAX_VALUE, 0, Long.MIN_VALUE, 0, -1L, 0),
            Arguments.of(Long.MIN_VALUE, 0, Long.MAX_VALUE, 0, -1L, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provider_plus")
    public void plus_addsDurationCorrectly(
            long initialSeconds, int initialNanos,
            long durationSeconds, int durationNanos,
            long expectedSeconds, int expectedNanos) {
        
        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        Duration duration = Duration.ofSeconds(durationSeconds, durationNanos);
        TaiInstant result = initial.plus(duration);
        
        assertEquals(expectedSeconds, result.getTaiSeconds());
        assertEquals(expectedNanos, result.getNano());
    }

    //-----------------------------------------------------------------------
    // minus(Duration)
    //-----------------------------------------------------------------------
    private static Stream<Arguments> provider_minus() {
        return Stream.of(
            // Arguments: initialSecs, initialNanos, durationSecs, durationNanos, expectedSecs, expectedNanos

            // --- Simple cases with no nano borrow ---
            Arguments.of(0L, 0, 0L, 0, 0L, 0),
            Arguments.of(5L, 0, 2L, 0, 3L, 0),
            Arguments.of(5L, 600_000_000, 2L, 100_000_000, 3L, 500_000_000),
            Arguments.of(-3L, 0, 2L, 0, -5L, 0),

            // --- Nano-of-second borrow ---
            Arguments.of(6L, 0, 2L, 500_000_000, 3L, 500_000_000), // Borrow from seconds
            Arguments.of(6L, 100_000_000, 2L, 300_000_000, 3L, 800_000_000),
            Arguments.of(0L, 0, 0L, 1, -1L, 999_999_999), // Borrow from zero

            // --- Negative duration (addition) with no nano carry ---
            Arguments.of(1L, 400_000_000, -2L, -100_000_000, 3L, 500_000_000),

            // --- Negative duration (addition) with nano carry ---
            Arguments.of(0L, 900_000_000, -2L, -200_000_000, 3L, 100_000_000),

            // --- Boundary conditions ---
            Arguments.of(Long.MAX_VALUE, 0, Long.MAX_VALUE, 0, 0L, 0),
            Arguments.of(Long.MIN_VALUE, 0, Long.MIN_VALUE + 1, 0, -1L, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provider_minus")
    public void minus_subtractsDurationCorrectly(
            long initialSeconds, int initialNanos,
            long durationSeconds, int durationNanos,
            long expectedSeconds, int expectedNanos) {

        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        Duration duration = Duration.ofSeconds(durationSeconds, durationNanos);
        TaiInstant result = initial.minus(duration);

        assertEquals(expectedSeconds, result.getTaiSeconds());
        assertEquals(expectedNanos, result.getNano());
    }

    //-----------------------------------------------------------------------
    // Comparisons
    //-----------------------------------------------------------------------
    /**
     * Helper method to verify comparison contracts (compareTo, isBefore, isAfter, equals)
     * for a set of instants that are assumed to be in ascending order.
     *
     * @param instants An array of TaiInstant objects, sorted chronologically.
     */
    void assertAscendingOrderAndComparisonContracts(TaiInstant... instants) {
        for (int i = 0; i < instants.length; i++) {
            TaiInstant a = instants[i];
            for (int j = 0; j < instants.length; j++) {
                TaiInstant b = instants[j];
                if (i < j) {
                    assertTrue(a.compareTo(b) < 0, a + " should be less than " + b);
                    assertFalse(a.equals(b));
                    assertTrue(a.isBefore(b));
                    assertFalse(a.isAfter(b));
                } else if (i > j) {
                    assertTrue(a.compareTo(b) > 0, a + " should be greater than " + b);
                    assertFalse(a.equals(b));
                    assertFalse(a.isBefore(b));
                    assertTrue(a.isAfter(b));
                } else {
                    assertEquals(0, a.compareTo(b), a + " should be equal to " + b);
                    assertTrue(a.equals(b));
                    assertFalse(a.isBefore(b));
                    assertFalse(a.isAfter(b));
                }
            }
        }
    }

    @Test
    public void comparison_contracts_hold() {
        assertAscendingOrderAndComparisonContracts(
            TaiInstant.ofTaiSeconds(-1, 0),
            TaiInstant.ofTaiSeconds(0, 0),
            TaiInstant.ofTaiSeconds(0, 1),
            TaiInstant.ofTaiSeconds(1, 0),
            TaiInstant.ofTaiSeconds(1, 999_999_999)
        );
    }

    //-----------------------------------------------------------------------
    // ofTaiSeconds() overflow
    //-----------------------------------------------------------------------
    @Test
    public void ofTaiSeconds_withNanoOverflow_throwsArithmeticException() {
        assertThrows(ArithmeticException.class, () -> TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 1_000_000_000));
    }
}