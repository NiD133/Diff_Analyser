package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("TaiInstant Tests")
class TaiInstantTest {

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------
    public static Object[][] invalidStringFormats() {
        return new Object[][]{
                {"A.123456789s(TAI)"},      // Non-numeric seconds
                {"123.12345678As(TAI)"},    // Non-numeric nanos
                {"123.123456789"},          // Missing suffix
                {"123.123456789s"},         // Missing scale
                {"+123.123456789s(TAI)"},   // Explicit positive sign not allowed
                {"-123.123s(TAI)"}          // Nanos must be 9 digits
        };
    }

    @ParameterizedTest
    @MethodSource("invalidStringFormats")
    @DisplayName("parse() throws exception for invalid formats")
    void parse_withInvalidFormat_throwsException(String invalidText) {
        assertThrows(DateTimeParseException.class, () -> TaiInstant.parse(invalidText));
    }

    //-----------------------------------------------------------------------
    // withTaiSeconds()
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("withTaiSeconds() updates seconds and preserves nanos")
    void withTaiSeconds_updatesSecondsAndPreservesNanos() {
        TaiInstant initial = TaiInstant.ofTaiSeconds(10, 500);
        long newSeconds = 25;

        TaiInstant result = initial.withTaiSeconds(newSeconds);

        assertAll(
            () -> assertEquals(newSeconds, result.getTaiSeconds(), "Seconds should be updated"),
            () -> assertEquals(initial.getNano(), result.getNano(), "Nanos should be preserved")
        );
    }

    //-----------------------------------------------------------------------
    // withNano()
    //-----------------------------------------------------------------------
    @ParameterizedTest
    @CsvSource({
        "10, 500, 12345",      // Change nano
        "20, 123, 0",          // Change to zero
        "30, 456, 999_999_999" // Change to max valid nano
    })
    @DisplayName("withNano() updates nanos and preserves seconds")
    void withNano_updatesNanosAndPreservesSeconds(long initialSeconds, int initialNanos, int newNanos) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        
        TaiInstant result = initial.withNano(newNanos);

        assertAll(
            () -> assertEquals(initial.getTaiSeconds(), result.getTaiSeconds(), "Seconds should be preserved"),
            () -> assertEquals(newNanos, result.getNano(), "Nanos should be updated")
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 1_000_000_000})
    @DisplayName("withNano() throws exception for out-of-range values")
    void withNano_withInvalidValue_throwsException(int invalidNano) {
        TaiInstant instant = TaiInstant.ofTaiSeconds(10, 500);
        assertThrows(IllegalArgumentException.class, () -> instant.withNano(invalidNano));
    }

    //-----------------------------------------------------------------------
    // plus(Duration)
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("plus() adds a positive duration without nano carry-over")
    void plus_positiveDuration_noCarry() {
        TaiInstant start = TaiInstant.ofTaiSeconds(5, 100);
        Duration toAdd = Duration.ofSeconds(10, 200);
        TaiInstant expected = TaiInstant.ofTaiSeconds(15, 300);
        assertEquals(expected, start.plus(toAdd));
    }

    @Test
    @DisplayName("plus() adds a positive duration with nano carry-over")
    void plus_positiveDuration_withNanoCarry() {
        TaiInstant start = TaiInstant.ofTaiSeconds(5, 800_000_000);
        Duration toAdd = Duration.ofNanos(300_000_000); // 0.3 seconds
        TaiInstant expected = TaiInstant.ofTaiSeconds(6, 100_000_000);
        assertEquals(expected, start.plus(toAdd));
    }

    @Test
    @DisplayName("plus() adds a negative duration without nano borrow")
    void plus_negativeDuration_noBorrow() {
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 500);
        Duration toAdd = Duration.ofSeconds(-5, -200);
        TaiInstant expected = TaiInstant.ofTaiSeconds(5, 300);
        assertEquals(expected, start.plus(toAdd));
    }

    @Test
    @DisplayName("plus() adds a negative duration with nano borrow")
    void plus_negativeDuration_withNanoBorrow() {
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 200_000_000);
        Duration toAdd = Duration.ofNanos(-500_000_000); // -0.5 seconds
        TaiInstant expected = TaiInstant.ofTaiSeconds(9, 700_000_000);
        assertEquals(expected, start.plus(toAdd));
    }

    @Test
    @DisplayName("plus() throws ArithmeticException on overflow")
    void plus_whenResultOverflows_throwsException() {
        TaiInstant start = TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 0);
        Duration toAdd = Duration.ofSeconds(1);
        assertThrows(ArithmeticException.class, () -> start.plus(toAdd));
    }

    //-----------------------------------------------------------------------
    // minus(Duration)
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("minus() subtracts a positive duration without nano borrow")
    void minus_positiveDuration_noBorrow() {
        TaiInstant start = TaiInstant.ofTaiSeconds(15, 300);
        Duration toSubtract = Duration.ofSeconds(10, 200);
        TaiInstant expected = TaiInstant.ofTaiSeconds(5, 100);
        assertEquals(expected, start.minus(toSubtract));
    }

    @Test
    @DisplayName("minus() subtracts a positive duration with nano borrow")
    void minus_positiveDuration_withNanoBorrow() {
        TaiInstant start = TaiInstant.ofTaiSeconds(6, 100_000_000);
        Duration toSubtract = Duration.ofNanos(300_000_000); // 0.3 seconds
        TaiInstant expected = TaiInstant.ofTaiSeconds(5, 800_000_000);
        assertEquals(expected, start.minus(toSubtract));
    }

    @Test
    @DisplayName("minus() subtracts a negative duration without nano carry-over")
    void minus_negativeDuration_noCarry() {
        TaiInstant start = TaiInstant.ofTaiSeconds(5, 100);
        Duration toSubtract = Duration.ofSeconds(-10, -200);
        TaiInstant expected = TaiInstant.ofTaiSeconds(15, 300);
        assertEquals(expected, start.minus(toSubtract));
    }

    @Test
    @DisplayName("minus() subtracts a negative duration with nano carry-over")
    void minus_negativeDuration_withNanoCarry() {
        TaiInstant start = TaiInstant.ofTaiSeconds(9, 700_000_000);
        Duration toSubtract = Duration.ofNanos(-500_000_000); // -0.5 seconds
        TaiInstant expected = TaiInstant.ofTaiSeconds(10, 200_000_000);
        assertEquals(expected, start.minus(toSubtract));
    }

    @Test
    @DisplayName("minus() throws ArithmeticException on underflow")
    void minus_whenResultUnderflows_throwsException() {
        TaiInstant start = TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0);
        Duration toSubtract = Duration.ofSeconds(1);
        assertThrows(ArithmeticException.class, () -> start.minus(toSubtract));
    }

    //-----------------------------------------------------------------------
    // durationUntil()
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("durationUntil() calculates a negative duration correctly")
    void durationUntil_whenEndIsBeforeStart_returnsNegativeDuration() {
        TaiInstant start = TaiInstant.ofTaiSeconds(4, 9);
        TaiInstant end = TaiInstant.ofTaiSeconds(4, 7);

        Duration duration = start.durationUntil(end);

        // The raw difference is -2 nanoseconds.
        // Duration normalizes this to -1 second and +999,999,998 nanoseconds.
        assertAll(
            () -> assertEquals(-1, duration.getSeconds()),
            () -> assertEquals(999_999_998, duration.getNano())
        );
    }
}