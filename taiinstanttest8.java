package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for modification and arithmetic operations on {@link TaiInstant}.
 */
class TaiInstantModificationAndArithmeticTest {

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------
    static Stream<Arguments> provideInvalidParseStrings() {
        return Stream.of(
            Arguments.of("A.123456789s(TAI)"),   // Non-numeric seconds
            Arguments.of("123.12345678As(TAI)"),  // Non-numeric nanos
            Arguments.of("123.123456789"),       // Missing suffix
            Arguments.of("123.123456789s"),      // Missing (TAI)
            Arguments.of("+123.123456789s(TAI)"), // Explicit plus sign not allowed
            Arguments.of("-123.123s(TAI)")       // Nanos must be 9 digits
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidParseStrings")
    void parse_shouldThrowException_forInvalidFormats(String invalidString) {
        assertThrows(DateTimeParseException.class, () -> TaiInstant.parse(invalidString));
    }

    //-----------------------------------------------------------------------
    // withTaiSeconds()
    //-----------------------------------------------------------------------
    @ParameterizedTest
    @CsvSource({
        "10, 12345, 20",
        "10, 12345, -5",
        "-10, 987, 0"
    })
    void withTaiSeconds_shouldSetSecondsComponent_andPreserveNanos(
            long initialSeconds, int initialNanos, long newSeconds) {
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
        "10, 12345, 1, 10, 1",
        "10, 12345, 999999999, 10, 999999999",
        "-5, 500, 0, -5, 0"
    })
    void withNano_shouldSetNanoComponent(
            long initialSeconds, int initialNanos, int newNano, long expectedSeconds, int expectedNanos) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        TaiInstant result = initial.withNano(newNano);

        assertEquals(expectedSeconds, result.getTaiSeconds());
        assertEquals(expectedNanos, result.getNano());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 1_000_000_000})
    void withNano_shouldThrowException_forOutOfRangeValue(int invalidNano) {
        TaiInstant instant = TaiInstant.ofTaiSeconds(10, 12345);
        assertThrows(IllegalArgumentException.class, () -> instant.withNano(invalidNano));
    }

    //-----------------------------------------------------------------------
    // plus(Duration)
    //-----------------------------------------------------------------------
    @ParameterizedTest(name = "[{index}] {0}s, {1}ns + ({2}s, {3}ns) -> {4}s, {5}ns")
    @CsvSource({
        // Simple addition
        "0, 0, 1, 0, 1, 0",
        // Addition with nano carry-over
        "-4, 666666667, 0, 333333333, -3, 0",
        // Adding a negative duration (subtraction)
        "-3, 0, -2, 0, -5, 0",
        // Complex interaction with negative values and nanos
        "-4, 666666667, -1, 333333334, -4, 1",
        // Edge case: overflow behavior
        "9223372036854775807, 0, -9223372036854775808, 0, -1, 0" // Long.MAX_VALUE + Long.MIN_VALUE
    })
    void plus_shouldAddDurationCorrectly(
            long initialSeconds, int initialNanos, long durationSeconds, int durationNanos,
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
    @ParameterizedTest(name = "[{index}] {0}s, {1}ns - ({2}s, {3}ns) -> {4}s, {5}ns")
    @CsvSource({
        // Simple subtraction
        "3, 0, 2, 0, 1, 0",
        // Subtraction with nano borrow
        "-3, 0, 0, 1, -4, 999999999",
        // Subtracting a negative duration (addition)
        "1, 0, -2, 0, 3, 0",
        // Complex interaction with negative values and nanos
        "-4, 666666667, -1, 666666667, -3, 0",
        // Edge case: subtracting itself
        "9223372036854775807, 0, 9223372036854775807, 0, 0, 0" // Long.MAX_VALUE - Long.MAX_VALUE
    })
    void minus_shouldSubtractDurationCorrectly(
            long initialSeconds, int initialNanos, long durationSeconds, int durationNanos,
            long expectedSeconds, int expectedNanos) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        Duration duration = Duration.ofSeconds(durationSeconds, durationNanos);
        TaiInstant result = initial.minus(duration);

        assertEquals(expectedSeconds, result.getTaiSeconds());
        assertEquals(expectedNanos, result.getNano());
    }

    //-----------------------------------------------------------------------
    // of(UtcInstant)
    //-----------------------------------------------------------------------
    // The TAI epoch is 1958-01-01, which corresponds to Modified Julian Day 36204.
    private static final int TAI_EPOCH_MJD = 36204;
    // On 1972-01-01 (the introduction of official UTC), TAI was ahead of UTC by 10 seconds.
    // This test assumes a fixed offset for dates before 1972.
    private static final int TAI_MINUS_UTC_SECONDS_AT_UTC_EPOCH = 10;
    private static final int SECONDS_PER_DAY = 86400;

    @Test
    void of_utcInstant_shouldPerformCorrectConversion() {
        // Test a wide range of days around the TAI epoch
        for (int dayOffset = -1000; dayOffset < 1000; dayOffset++) {
            // Test a few seconds within each day
            for (int secondOfDay = 0; secondOfDay < 10; secondOfDay++) {
                long mjd = TAI_EPOCH_MJD + dayOffset;
                long nanoOfDay = secondOfDay * 1_000_000_000L + 2L;
                UtcInstant utcInstant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);

                TaiInstant taiInstant = TaiInstant.of(utcInstant);

                long expectedTaiSeconds = (long) dayOffset * SECONDS_PER_DAY + secondOfDay + TAI_MINUS_UTC_SECONDS_AT_UTC_EPOCH;
                assertEquals(expectedTaiSeconds, taiInstant.getTaiSeconds());
                assertEquals(2, taiInstant.getNano());
            }
        }
    }
}