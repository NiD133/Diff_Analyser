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
 * Test class for {@link TaiInstant} focusing on modification, arithmetic, and parsing.
 */
class TaiInstantArithmeticTest {

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------
    static Stream<String> provider_invalidParseStrings() {
        return Stream.of(
                "A.123456789s(TAI)",      // Non-numeric seconds
                "123.12345678As(TAI)",     // Non-numeric nanos
                "123.123456789",           // Missing suffix
                "123.123456789s",          // Missing (TAI)
                "+123.123456789s(TAI)",    // Explicit plus sign not allowed
                "-123.123s(TAI)"           // Nanos must be 9 digits
        );
    }

    @ParameterizedTest
    @MethodSource("provider_invalidParseStrings")
    void parse_withInvalidFormat_throwsDateTimeParseException(String invalidString) {
        assertThrows(DateTimeParseException.class, () -> TaiInstant.parse(invalidString));
    }

    //-----------------------------------------------------------------------
    // withTaiSeconds()
    //-----------------------------------------------------------------------
    static Stream<Arguments> provider_withTaiSeconds() {
        return Stream.of(
                Arguments.of(0L, 12345L, 1L),
                Arguments.of(0L, 12345L, -1L),
                Arguments.of(7L, 12345L, 2L),
                Arguments.of(7L, 12345L, -2L),
                Arguments.of(-99L, 12345L, 3L),
                Arguments.of(-99L, 12345L, -3L)
        );
    }

    @ParameterizedTest
    @MethodSource("provider_withTaiSeconds")
    void withTaiSeconds_replacesSecondsAndPreservesNanos(long initialSeconds, long initialNanos, long newSeconds) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        TaiInstant result = initial.withTaiSeconds(newSeconds);

        assertEquals(newSeconds, result.getTaiSeconds());
        assertEquals(initialNanos, result.getNano());
    }

    //-----------------------------------------------------------------------
    // withNano()
    //-----------------------------------------------------------------------
    static Stream<Arguments> provider_withNano_validCases() {
        return Stream.of(
                Arguments.of(0L, 12345L, 1),
                Arguments.of(7L, 12345L, 2),
                Arguments.of(-99L, 12345L, 3),
                Arguments.of(-99L, 12345L, 999_999_999)
        );
    }

    @ParameterizedTest
    @MethodSource("provider_withNano_validCases")
    void withNano_replacesNanos(long initialSeconds, long initialNanos, int newNanos) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        TaiInstant result = initial.withNano(newNanos);

        assertEquals(initialSeconds, result.getTaiSeconds());
        assertEquals(newNanos, result.getNano());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 1_000_000_000})
    void withNano_withInvalidNanos_throwsIllegalArgumentException(int invalidNano) {
        TaiInstant instant = TaiInstant.ofTaiSeconds(0, 0);
        assertThrows(IllegalArgumentException.class, () -> instant.withNano(invalidNano));
    }

    //-----------------------------------------------------------------------
    // plus(Duration)
    //-----------------------------------------------------------------------
    /**
     * Data provider for the plus() method.
     * Arguments: initialSeconds, initialNanos, secondsToAdd, nanosToAdd, expectedSeconds, expectedNanos
     */
    static Object[][] provider_plus() {
        return new Object[][]{
                // Boundary conditions
                {Long.MIN_VALUE, 0, Long.MAX_VALUE, 0, -1L, 0},

                // --- Initial instant: -4s, 666_666_667ns ---
                {-4L, 666_666_667, -4L, 666_666_667, -7L, 333_333_334},
                {-4L, 666_666_667, -3L, 0, -7L, 666_666_667},
                {-4L, 666_666_667, 0, 333_333_333, -3L, 0},
                {-4L, 666_666_667, 3L, 333_333_333, 0L, 0},

                // --- Initial instant: -3s, 0ns ---
                {-3L, 0, -4L, 666_666_667, -7L, 666_666_667},
                {-3L, 0, -3L, 0, -6L, 0},
                {-3L, 0, 0, 1, -3L, 1},
                {-3L, 0, 3L, 333_333_333, 0L, 333_333_333},

                // --- Initial instant: 0s, 0ns ---
                {0L, 0, -1L, 999_999_999, -1L, 999_999_999},
                {0L, 0, 0, 0, 0L, 0},
                {0L, 0, 0, 1, 0L, 1},
                {0L, 0, 3L, 333_333_333, 3L, 333_333_333},

                // --- Initial instant: 0s, 333_333_333ns ---
                {0L, 333_333_333, -1L, 666_666_667, 0L, 0},
                {0L, 333_333_333, 0, 666_666_666, 0L, 999_999_999},
                {0L, 333_333_333, 3L, 333_333_333, 3L, 666_666_666},

                // --- Initial instant: 3s, 333_333_333ns ---
                {3L, 333_333_333, -4L, 666_666_667, 0L, 0},
                {3L, 333_333_333, 0, 0, 3L, 333_333_333},
                {3L, 333_333_333, 3L, 333_333_333, 6L, 666_666_666},

                // Boundary conditions
                {Long.MAX_VALUE, 0, Long.MIN_VALUE, 0, -1L, 0}
        };
    }

    @ParameterizedTest
    @MethodSource("provider_plus")
    void plus_addsDurationCorrectly(long seconds, int nanos, long plusSeconds, int plusNanos, long expectedSeconds, int expectedNanos) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(seconds, nanos);
        Duration toAdd = Duration.ofSeconds(plusSeconds, plusNanos);
        TaiInstant result = initial.plus(toAdd);

        assertEquals(expectedSeconds, result.getTaiSeconds());
        assertEquals(expectedNanos, result.getNano());
    }

    //-----------------------------------------------------------------------
    // minus(Duration)
    //-----------------------------------------------------------------------
    /**
     * Data provider for the minus() method.
     * Arguments: initialSeconds, initialNanos, secondsToSubtract, nanosToSubtract, expectedSeconds, expectedNanos
     */
    static Object[][] provider_minus() {
        return new Object[][]{
                // Boundary conditions
                {Long.MIN_VALUE, 0, Long.MIN_VALUE + 1, 0, -1L, 0},

                // --- Initial instant: -4s, 666_666_667ns ---
                {-4L, 666_666_667, -4L, 666_666_667, 0L, 0},
                {-4L, 666_666_667, 0, 1, -4L, 666_666_666},
                {-4L, 666_666_667, 3L, 333_333_333, -7L, 333_333_334},

                // --- Initial instant: -3s, 0ns ---
                {-3L, 0, -4L, 666_666_667, 0L, 333_333_333},
                {-3L, 0, 0, 1, -4L, 999_999_999},
                {-3L, 0, 3L, 333_333_333, -7L, 666_666_667},

                // --- Initial instant: 0s, 0ns ---
                {0L, 0, -1L, 999_999_999, 0L, 1},
                {0L, 0, 0, 1, -1L, 999_999_999},
                {0L, 0, 3L, 333_333_333, -4L, 666_666_667},

                // --- Initial instant: 3s, 333_333_333ns ---
                {3L, 333_333_333, -1L, 666_666_667, 3L, 0},
                {3L, 333_333_333, 0, 333_333_333, 3L, 0},
                {3L, 333_333_333, 3L, 333_333_333, 0L, 0},

                // Boundary conditions
                {Long.MAX_VALUE, 0, Long.MAX_VALUE, 0, 0L, 0}
        };
    }

    @ParameterizedTest
    @MethodSource("provider_minus")
    void minus_subtractsDurationCorrectly(long seconds, int nanos, long minusSeconds, int minusNanos, long expectedSeconds, int expectedNanos) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(seconds, nanos);
        Duration toSubtract = Duration.ofSeconds(minusSeconds, minusNanos);
        TaiInstant result = initial.minus(toSubtract);

        assertEquals(expectedSeconds, result.getTaiSeconds());
        assertEquals(expectedNanos, result.getNano());
    }

    @Test
    void minus_fromMaxInstant_whenResultOverflows_throwsArithmeticException() {
        TaiInstant maxInstant = TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 999_999_999);
        // Subtracting a negative duration is equivalent to adding a positive one.
        // Duration of -1s + 999,999,999ns is effectively -1 nanosecond.
        // maxInstant - (-1ns) = maxInstant + 1ns, which overflows.
        Duration negativeNano = Duration.ofSeconds(-1, 999_999_999);
        assertThrows(ArithmeticException.class, () -> maxInstant.minus(negativeNano));
    }

    //-----------------------------------------------------------------------
    // Comparison helpers
    //-----------------------------------------------------------------------
    /**
     * Asserts the ordering and equality contract for a list of ordered instants.
     * This is a helper method that may be used by other tests in a larger suite.
     *
     * @param instants an array of TaiInstant, sorted in ascending order.
     */
    @SuppressWarnings("unused") // Acknowledging this helper is not used in this specific file.
    void assertOrdering(TaiInstant... instants) {
        for (int i = 0; i < instants.length; i++) {
            TaiInstant a = instants[i];
            for (int j = 0; j < instants.length; j++) {
                TaiInstant b = instants[j];
                if (i < j) {
                    assertTrue(a.compareTo(b) < 0, a + " should be before " + b);
                    assertFalse(a.equals(b));
                    assertTrue(a.isBefore(b));
                    assertFalse(a.isAfter(b));
                } else if (i > j) {
                    assertTrue(a.compareTo(b) > 0, a + " should be after " + b);
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
}