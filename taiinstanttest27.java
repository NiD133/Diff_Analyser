package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for modification and arithmetic operations on {@link TaiInstant}.
 */
@DisplayName("TaiInstant modification and arithmetic")
class TaiInstantOperationsTest {

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------
    @ParameterizedTest
    @ValueSource(strings = {
            "A.123456789s(TAI)",      // Non-numeric seconds
            "123.12345678As(TAI)",     // Non-numeric nanos
            "123.123456789",           // Missing suffix
            "123.123456789s",          // Missing (TAI)
            "+123.123456789s(TAI)",    // Explicit positive sign not supported
            "-123.123s(TAI)"           // Nanos must be 9 digits
    })
    void parse_withInvalidFormat_shouldThrowDateTimeParseException(String invalidString) {
        assertThrows(DateTimeParseException.class, () -> TaiInstant.parse(invalidString));
    }

    //-----------------------------------------------------------------------
    // withTaiSeconds()
    //-----------------------------------------------------------------------
    @ParameterizedTest
    @CsvSource({
            "  0, 12345,    1 ",
            "  7, 12345,   -2 ",
            "-99, 12345,    0 "
    })
    void withTaiSeconds_shouldUpdateSecondsAndPreserveNanos(long initialSeconds, int initialNanos, long newSeconds) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        TaiInstant result = initial.withTaiSeconds(newSeconds);

        assertEquals(newSeconds, result.getTaiSeconds());
        assertEquals(initialNanos, result.getNano());
    }

    //-----------------------------------------------------------------------
    // withNano()
    //-----------------------------------------------------------------------
    @ParameterizedTest
    @CsvSource({
            "  0, 12345,           1 ",
            "  7, 12345,           2 ",
            "-99, 12345,           0 ",
            "-99, 12345, 999_999_999 "
    })
    void withNano_withValidValue_shouldUpdateNanosAndPreserveSeconds(long initialSeconds, int initialNanos, int newNanos) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        TaiInstant result = initial.withNano(newNanos);

        assertEquals(initialSeconds, result.getTaiSeconds());
        assertEquals(newNanos, result.getNano());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 1_000_000_000})
    void withNano_withInvalidValue_shouldThrowIllegalArgumentException(int invalidNano) {
        TaiInstant instant = TaiInstant.ofTaiSeconds(0, 0);
        assertThrows(IllegalArgumentException.class, () -> instant.withNano(invalidNano));
    }

    //-----------------------------------------------------------------------
    // plus()
    //-----------------------------------------------------------------------
    @ParameterizedTest
    @CsvSource({
            // initialSeconds, initialNanos, plusSeconds, plusNanos, expectedSeconds, expectedNanos
            " 10, 100_000_000,   5, 200_000_000,  15, 300_000_000", // Simple addition
            " 10, 800_000_000,   0, 300_000_000,  11, 100_000_000", // Addition with nano carry-over
            "-10, 800_000_000,   1, 300_000_000,  -8, 100_000_000", // Negative initial instant with carry-over
            " 10, 100_000_000,  -2, 200_000_000,   8, 300_000_000", // Add negative seconds
            " 10, 100_000_000,  -2,-800_000_000,   7, 300_000_000", // Add negative duration with borrow
            " -3,           0,  -1, 999_999_999,  -4, 999_999_999", // From original data: negative values
            " -4, 666_666_667,  -1, 333_333_334,  -4,           1", // From original data: complex interaction
    })
    void plus_shouldAddDurationCorrectly(
            long initialSeconds, int initialNanos,
            long plusSeconds, int plusNanos,
            long expectedSeconds, int expectedNanos) {

        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        Duration durationToAdd = Duration.ofSeconds(plusSeconds, plusNanos);
        TaiInstant result = initial.plus(durationToAdd);

        assertEquals(expectedSeconds, result.getTaiSeconds());
        assertEquals(expectedNanos, result.getNano());
    }

    //-----------------------------------------------------------------------
    // minus()
    //-----------------------------------------------------------------------
    @ParameterizedTest
    @CsvSource({
            // initialSeconds, initialNanos, minusSeconds, minusNanos, expectedSeconds, expectedNanos
            " 15, 300_000_000,   5, 200_000_000,  10, 100_000_000", // Simple subtraction
            " 11, 100_000_000,   0, 300_000_000,  10, 800_000_000", // Subtraction with nano borrow
            "-10, 100_000_000,   1, 300_000_000, -12, 800_000_000", // Negative initial instant with borrow
            " 10, 300_000_000,  -2, 200_000_000,  12, 100_000_000", // Subtract negative seconds
            " 10, 800_000_000,  -2,-300_000_000,  13, 100_000_000", // Subtract negative duration with carry
            " -3,           0,  -1, 999_999_999,  -3,           1", // From original data: negative values
            "  3, 333_333_333,  -1, 666_666_667,   3,           0", // From original data: complex interaction
    })
    void minus_shouldSubtractDurationCorrectly(
            long initialSeconds, int initialNanos,
            long minusSeconds, int minusNanos,
            long expectedSeconds, int expectedNanos) {

        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        Duration durationToSubtract = Duration.ofSeconds(minusSeconds, minusNanos);
        TaiInstant result = initial.minus(durationToSubtract);

        assertEquals(expectedSeconds, result.getTaiSeconds());
        assertEquals(expectedNanos, result.getNano());
    }

    //-----------------------------------------------------------------------
    // toString()
    //-----------------------------------------------------------------------
    @Test
    void toString_withZeroSeconds_shouldFormatWithLeadingZero() {
        TaiInstant instantWithZeroSeconds = TaiInstant.ofTaiSeconds(0L, 567);
        assertEquals("0.000000567s(TAI)", instantWithZeroSeconds.toString());
    }
}