package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * This test class focuses on the modification, parsing, and comparison methods of TaiInstant.
 * Note: The original class name "TaiInstantTestTest21" has been kept for context.
 */
public class TaiInstantTestTest21 {

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------
    @DisplayName("parse() with invalid format should throw DateTimeParseException")
    @ParameterizedTest(name = "[{index}] parsing \"{0}\"")
    @ValueSource(strings = {
        "A.123456789s(TAI)",      // Non-digit in seconds part
        "123.12345678As(TAI)",     // Non-digit in nanos part
        "123.123456789",           // Missing suffix "s(TAI)"
        "123.123456789s",          // Missing "(TAI)" part of suffix
        "+123.123456789s(TAI)",    // Leading plus sign is not accepted by the parser
        "-123.123s(TAI)"           // Nanos part must be exactly 9 digits
    })
    void parse_withInvalidFormat_shouldThrowException(String invalidString) {
        assertThrows(DateTimeParseException.class, () -> TaiInstant.parse(invalidString));
    }

    //-----------------------------------------------------------------------
    // withTaiSeconds()
    //-----------------------------------------------------------------------
    @DisplayName("withTaiSeconds() should update seconds and preserve nanos")
    @ParameterizedTest(name = "[{index}] {0}s, {1}ns -> withTaiSeconds({2})")
    @CsvSource({
        "  0, 12345,   1",
        "  0, 12345,  -1",
        "  7, 12345,   2",
        "  7, 12345,  -2",
        "-99, 12345,   3",
        "-99, 12345,  -3",
    })
    void withTaiSeconds_shouldUpdateSecondsAndPreserveNanos(long initialSeconds, long initialNanos, long newSeconds) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        TaiInstant result = initial.withTaiSeconds(newSeconds);

        assertEquals(newSeconds, result.getTaiSeconds());
        assertEquals(initialNanos, result.getNano());
    }

    //-----------------------------------------------------------------------
    // withNano()
    //-----------------------------------------------------------------------
    @DisplayName("withNano() with a valid value should update nanos and preserve seconds")
    @ParameterizedTest(name = "[{index}] {0}s, {1}ns -> withNano({2})")
    @CsvSource({
        "  0, 12345,           0",
        "  7, 12345,           1",
        "-99, 12345, 999_999_999",
    })
    void withNano_withValidValue_shouldUpdateNanos(long initialSeconds, int initialNanos, int newNanos) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        TaiInstant result = initial.withNano(newNanos);

        assertEquals(initialSeconds, result.getTaiSeconds());
        assertEquals(newNanos, result.getNano());
    }

    @DisplayName("withNano() with an invalid value should throw IllegalArgumentException")
    @ParameterizedTest(name = "[{index}] withNano({0})")
    @ValueSource(ints = { -1, 1_000_000_000 })
    void withNano_withInvalidValue_shouldThrowException(int invalidNano) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(0, 0);
        assertThrows(IllegalArgumentException.class, () -> initial.withNano(invalidNano));
    }

    //-----------------------------------------------------------------------
    // plus()
    //-----------------------------------------------------------------------
    @DisplayName("plus() should correctly add durations")
    @ParameterizedTest(name = "[{index}] ({0}s, {1}ns) + ({2}s, {3}ns) = ({4}s, {5}ns)")
    @CsvSource({
        // Simple addition, no carry
        " 2, 500_000_000,  3, 100_000_000,  5, 600_000_000",
        // Nano overflow (carry-over to seconds)
        " 2, 800_000_000,  3, 400_000_000,  6, 200_000_000",
        // Negative duration (subtraction), no borrow
        " 5, 600_000_000, -3, 100_000_000,  2, 500_000_000",
        // Negative duration with nano underflow (borrow from seconds)
        " 5, 100_000_000, -3, -400_000_000,  2, 500_000_000",
        // Negative initial value
        "-5, 100_000_000,  2, 200_000_000, -3, 300_000_000",
        // Zero duration
        " 1, 1, 0, 0, 1, 1"
    })
    void plus_shouldCorrectlyAddDurations(
            long initialSeconds, int initialNanos,
            long durationSeconds, int durationNanos,
            long expectedSeconds, int expectedNanos) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        Duration duration = Duration.ofSeconds(durationSeconds, durationNanos);
        TaiInstant result = initial.plus(duration);

        assertEquals(expectedSeconds, result.getTaiSeconds());
        assertEquals(expectedNanos, result.getNano());
    }

    @Test
    @DisplayName("plus() should handle boundary values without throwing an exception")
    void plus_shouldHandleBoundaryValues() {
        TaiInstant min = TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0);
        Duration maxDuration = Duration.ofSeconds(Long.MAX_VALUE, 0);
        TaiInstant result = min.plus(maxDuration);
        assertEquals(-1, result.getTaiSeconds());
    }

    //-----------------------------------------------------------------------
    // minus()
    //-----------------------------------------------------------------------
    @DisplayName("minus() should correctly subtract durations")
    @ParameterizedTest(name = "[{index}] ({0}s, {1}ns) - ({2}s, {3}ns) = ({4}s, {5}ns)")
    @CsvSource({
        // Simple subtraction, no borrow
        " 5, 600_000_000,  3, 100_000_000,  2, 500_000_000",
        // Nano underflow (borrow from seconds)
        " 5, 100_000_000,  2, 400_000_000,  2, 700_000_000",
        // Negative duration (addition), no carry
        " 2, 500_000_000, -3, -100_000_000,  5, 600_000_000",
        // Negative duration with nano overflow (carry-over to seconds)
        " 2, 500_000_000, -3, -800_000_000,  6, 300_000_000",
        // Negative initial value
        "-5, 800_000_000,  2, 200_000_000, -8, 600_000_000",
        // Zero duration
        " 1, 1, 0, 0, 1, 1"
    })
    void minus_shouldCorrectlySubtractDurations(
            long initialSeconds, int initialNanos,
            long durationSeconds, int durationNanos,
            long expectedSeconds, int expectedNanos) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        Duration duration = Duration.ofSeconds(durationSeconds, durationNanos);
        TaiInstant result = initial.minus(duration);

        assertEquals(expectedSeconds, result.getTaiSeconds());
        assertEquals(expectedNanos, result.getNano());
    }

    @Test
    @DisplayName("minus() should handle boundary values")
    void minus_shouldHandleBoundaryValues() {
        // Test case: Long.MIN_VALUE - (Long.MIN_VALUE + 1) == -1
        TaiInstant min = TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0);
        Duration minPlusOneDuration = Duration.ofSeconds(Long.MIN_VALUE + 1, 0);
        TaiInstant result1 = min.minus(minPlusOneDuration);
        assertEquals(-1, result1.getTaiSeconds());

        // Test case: Long.MAX_VALUE - Long.MAX_VALUE == 0
        TaiInstant max = TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 0);
        Duration maxDuration = Duration.ofSeconds(Long.MAX_VALUE, 0);
        TaiInstant result2 = max.minus(maxDuration);
        assertEquals(0, result2.getTaiSeconds());
    }

    //-----------------------------------------------------------------------
    // comparisons
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("Comparison methods (compareTo, isAfter, isBefore, equals) should be consistent")
    void comparisonMethods_shouldBehaveConsistently() {
        // A pre-sorted list of instants to test against.
        TaiInstant[] instants = new TaiInstant[] {
            TaiInstant.ofTaiSeconds(-2L, 0),
            TaiInstant.ofTaiSeconds(-2L, 999_999_998),
            TaiInstant.ofTaiSeconds(-2L, 999_999_999),
            TaiInstant.ofTaiSeconds(-1L, 0),
            TaiInstant.ofTaiSeconds(-1L, 1),
            TaiInstant.ofTaiSeconds(-1L, 999_999_998),
            TaiInstant.ofTaiSeconds(-1L, 999_999_999),
            TaiInstant.ofTaiSeconds(0L, 0),
            TaiInstant.ofTaiSeconds(0L, 1),
            TaiInstant.ofTaiSeconds(0L, 2),
            TaiInstant.ofTaiSeconds(0L, 999_999_999),
            TaiInstant.ofTaiSeconds(1L, 0),
            TaiInstant.ofTaiSeconds(2L, 0)
        };
        assertComparisonContracts(instants);
    }

    /**
     * Helper that asserts the contracts of comparison methods for a sorted array of instants.
     * It checks every pair of instants (a, b) from the list to ensure their relative ordering
     * is reported consistently by compareTo, isBefore, isAfter, and equals.
     */
    private void assertComparisonContracts(TaiInstant... instants) {
        for (int i = 0; i < instants.length; i++) {
            TaiInstant a = instants[i];
            for (int j = 0; j < instants.length; j++) {
                TaiInstant b = instants[j];
                if (i < j) {
                    assertTrue(a.compareTo(b) < 0, a + " should be less than " + b);
                    assertTrue(a.isBefore(b), a + " should be before " + b);
                    assertFalse(a.isAfter(b), a + " should not be after " + b);
                    assertFalse(a.equals(b), a + " should not be equal to " + b);
                } else if (i > j) {
                    assertTrue(a.compareTo(b) > 0, a + " should be greater than " + b);
                    assertFalse(a.isBefore(b), a + " should not be before " + b);
                    assertTrue(a.isAfter(b), a + " should be after " + b);
                    assertFalse(a.equals(b), a + " should not be equal to " + b);
                } else {
                    assertEquals(0, a.compareTo(b), a + " should be equal to " + b);
                    assertFalse(a.isBefore(b), a + " should not be before " + b);
                    assertFalse(a.isAfter(b), a + " should not be after " + b);
                    assertTrue(a.equals(b), a + " should be equal to " + b);
                }
            }
        }
    }
}