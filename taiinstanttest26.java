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
 * Tests for modification and arithmetic operations on {@link TaiInstant}.
 */
@DisplayName("TaiInstant Modification and Arithmetic")
public class TaiInstantModificationAndArithmeticTest {

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------
    @DisplayName("parse() with invalid format throws DateTimeParseException")
    @ParameterizedTest(name = "For input \"{0}\"")
    @ValueSource(strings = {
        "A.123456789s(TAI)",      // Non-digit in seconds
        "123.12345678As(TAI)",     // Non-digit in nanos
        "123.123456789",           // Missing suffix
        "123.123456789s",          // Incomplete suffix
        "+123.123456789s(TAI)",    // Leading plus sign is not allowed
        "-123.123s(TAI)"           // Nanos part must have exactly 9 digits
    })
    public void parse_withInvalidFormat_throwsDateTimeParseException(String invalidString) {
        assertThrows(DateTimeParseException.class, () -> TaiInstant.parse(invalidString));
    }

    //-----------------------------------------------------------------------
    // withTaiSeconds()
    //-----------------------------------------------------------------------
    @DisplayName("withTaiSeconds() should set seconds and preserve nanos")
    @ParameterizedTest(name = "Inst({0}, {1}) with seconds {2} -> Inst({2}, {1})")
    @CsvSource({
        "0, 12345, 1",
        "0, 12345, -1",
        "7, 12345, 2",
        "7, 12345, -2",
        "-99, 12345, 3",
        "-99, 12345, -3"
    })
    public void withTaiSeconds_shouldSetSecondsAndPreserveNanos(long initialSeconds, long initialNanos, long newSeconds) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        TaiInstant result = initial.withTaiSeconds(newSeconds);

        assertEquals(newSeconds, result.getTaiSeconds());
        assertEquals(initialNanos, result.getNano());
    }

    //-----------------------------------------------------------------------
    // withNano()
    //-----------------------------------------------------------------------
    @DisplayName("withNano() should update nano-of-second and preserve seconds")
    @ParameterizedTest(name = "Inst({0}, {1}) with nano {2} -> Inst({0}, {2})")
    @CsvSource({
        "0, 12345, 1",
        "7, 12345, 2",
        "-99, 12345, 3",
        "-99, 12345, 999_999_999"
    })
    public void withNano_withValidNano_shouldUpdateNanoAndPreserveSeconds(long initialSeconds, long initialNanos, int newNanos) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        TaiInstant result = initial.withNano(newNanos);

        assertEquals(initialSeconds, result.getTaiSeconds());
        assertEquals(newNanos, result.getNano());
    }

    @DisplayName("withNano() with invalid nano-of-second should throw IllegalArgumentException")
    @ParameterizedTest(name = "For nano value {0}")
    @ValueSource(ints = {-1, 1_000_000_000})
    public void withNano_withInvalidNano_shouldThrowIllegalArgumentException(int invalidNano) {
        TaiInstant instant = TaiInstant.ofTaiSeconds(0, 0);
        assertThrows(IllegalArgumentException.class, () -> instant.withNano(invalidNano));
    }

    //-----------------------------------------------------------------------
    // plus(Duration)
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("plus() should add duration correctly (no nano carry)")
    public void plus_noNanoCarry() {
        TaiInstant start = TaiInstant.ofTaiSeconds(5, 100_000_000);
        Duration toAdd = Duration.ofSeconds(3, 200_000_000);
        TaiInstant result = start.plus(toAdd);
        assertEquals(8, result.getTaiSeconds());
        assertEquals(300_000_000, result.getNano());
    }

    @Test
    @DisplayName("plus() should add duration correctly (with nano carry)")
    public void plus_withNanoCarry() {
        TaiInstant start = TaiInstant.ofTaiSeconds(5, 800_000_000);
        Duration toAdd = Duration.ofSeconds(3, 300_000_000);
        TaiInstant result = start.plus(toAdd);
        assertEquals(9, result.getTaiSeconds());      // 5 + 3 + 1 (carry)
        assertEquals(100_000_000, result.getNano());  // 800M + 300M - 1B
    }

    @Test
    @DisplayName("plus() should subtract negative duration correctly (with nano borrow)")
    public void plus_negativeDuration_withNanoBorrow() {
        TaiInstant start = TaiInstant.ofTaiSeconds(5, 200_000_000);
        Duration toSubtract = Duration.ofSeconds(-3, -300_000_000);
        TaiInstant result = start.plus(toSubtract);
        assertEquals(1, result.getTaiSeconds());      // 5 - 3 - 1 (borrow)
        assertEquals(900_000_000, result.getNano());  // 200M - 300M + 1B
    }

    @Test
    @DisplayName("plus() with zero duration should return an equal instance")
    public void plus_zeroDuration() {
        TaiInstant start = TaiInstant.ofTaiSeconds(5, 123);
        TaiInstant result = start.plus(Duration.ZERO);
        assertEquals(start, result);
    }

    @Test
    @DisplayName("plus() should handle arithmetic overflow of seconds by wrapping")
    public void plus_overflow() {
        TaiInstant start = TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 0);
        Duration toAdd = Duration.ofSeconds(1);
        TaiInstant result = start.plus(toAdd);
        assertEquals(Long.MIN_VALUE, result.getTaiSeconds());
    }

    //-----------------------------------------------------------------------
    // minus(Duration)
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("minus() should subtract duration correctly (no nano borrow)")
    public void minus_noNanoBorrow() {
        TaiInstant start = TaiInstant.ofTaiSeconds(5, 800_000_000);
        Duration toSubtract = Duration.ofSeconds(3, 300_000_000);
        TaiInstant result = start.minus(toSubtract);
        assertEquals(2, result.getTaiSeconds());
        assertEquals(500_000_000, result.getNano());
    }

    @Test
    @DisplayName("minus() should subtract duration correctly (with nano borrow)")
    public void minus_withNanoBorrow() {
        TaiInstant start = TaiInstant.ofTaiSeconds(5, 200_000_000);
        Duration toSubtract = Duration.ofSeconds(3, 300_000_000);
        TaiInstant result = start.minus(toSubtract);
        assertEquals(1, result.getTaiSeconds());      // 5 - 3 - 1 (borrow)
        assertEquals(900_000_000, result.getNano());  // 200M - 300M + 1B
    }

    @Test
    @DisplayName("minus() should add negative duration correctly (with nano carry)")
    public void minus_negativeDuration_withNanoCarry() {
        TaiInstant start = TaiInstant.ofTaiSeconds(5, 800_000_000);
        Duration toAdd = Duration.ofSeconds(-3, -300_000_000);
        TaiInstant result = start.minus(toAdd);
        assertEquals(9, result.getTaiSeconds());      // 5 - (-3) + 1 (carry)
        assertEquals(100_000_000, result.getNano());  // 800M - (-300M) - 1B
    }

    @Test
    @DisplayName("minus() with zero duration should return an equal instance")
    public void minus_zeroDuration() {
        TaiInstant start = TaiInstant.ofTaiSeconds(5, 123);
        TaiInstant result = start.minus(Duration.ZERO);
        assertEquals(start, result);
    }

    @Test
    @DisplayName("minus() should handle arithmetic underflow of seconds by wrapping")
    public void minus_underflow() {
        TaiInstant start = TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0);
        Duration toSubtract = Duration.ofSeconds(1);
        TaiInstant result = start.minus(toSubtract);
        assertEquals(Long.MAX_VALUE, result.getTaiSeconds());
    }

    //-----------------------------------------------------------------------
    // toString()
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("toString() should format negative instants correctly")
    public void toString_formatsNegativeInstantsCorrectly() {
        TaiInstant t = TaiInstant.ofTaiSeconds(-123L, 123_456_789);
        assertEquals("-123.123456789s(TAI)", t.toString());
    }

    @Test
    @DisplayName("toString() should format positive instants correctly")
    public void toString_formatsPositiveInstantsCorrectly() {
        TaiInstant t = TaiInstant.ofTaiSeconds(123L, 123_456_789);
        assertEquals("123.123456789s(TAI)", t.toString());
    }

    @Test
    @DisplayName("toString() should format zero instant correctly")
    public void toString_formatsZeroInstantCorrectly() {
        TaiInstant t = TaiInstant.ofTaiSeconds(0L, 123_456_789);
        assertEquals("0.123456789s(TAI)", t.toString());
    }

    //-----------------------------------------------------------------------
    // Comparison testing helper
    //-----------------------------------------------------------------------
    /**
     * Helper method for asserting chronological ordering and comparison contracts.
     * This method is likely used by a test runner that combines multiple test files.
     *
     * @param instants an array of TaiInstant objects, sorted chronologically.
     */
    private void assertChronologicalOrdering(TaiInstant... instants) {
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