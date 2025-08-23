package org.threeten.extra.scale;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Understandable tests for {@link TaiInstant}.
 */
class TaiInstantTest {

    @DisplayName("parse() with invalid format throws DateTimeParseException")
    @ParameterizedTest(name = "for input: \"{0}\"")
    @ValueSource(strings = {
        "A.123456789s(TAI)",      // non-digit in seconds
        "123.12345678As(TAI)",     // non-digit in nanos
        "123.123456789",           // missing suffix
        "123.123456789s",          // incomplete suffix
        "+123.123456789s(TAI)",    // leading plus not allowed
        "-123.123s(TAI)"           // nanos must be 9 digits
    })
    void parse_invalidString_throwsException(String invalidString) {
        assertThrows(DateTimeParseException.class, () -> TaiInstant.parse(invalidString));
    }

    @DisplayName("withTaiSeconds() updates seconds and preserves nanos")
    @ParameterizedTest
    @CsvSource({
        "10, 12345, 20",
        "10, 12345, -5",
        "0,  999999999, 1",
        "-10, 0, 10"
    })
    void withTaiSeconds_updatesSecondsAndPreservesNanos(long initialSeconds, int initialNanos, long newSeconds) {
        TaiInstant initialInstant = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        TaiInstant updatedInstant = initialInstant.withTaiSeconds(newSeconds);

        assertEquals(newSeconds, updatedInstant.getTaiSeconds());
        assertEquals(initialNanos, updatedInstant.getNano());
    }

    @DisplayName("withNano() updates nanos and preserves seconds")
    @ParameterizedTest
    @CsvSource({
        "10, 12345, 1",
        "10, 12345, 999999999",
        "10, 12345, 0"
    })
    void withNano_validNano_updatesNanosAndPreservesSeconds(long initialSeconds, int initialNanos, int newNanos) {
        TaiInstant initialInstant = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        TaiInstant updatedInstant = initialInstant.withNano(newNanos);

        assertEquals(initialSeconds, updatedInstant.getTaiSeconds());
        assertEquals(newNanos, updatedInstant.getNano());
    }

    @DisplayName("withNano() with out-of-range value throws IllegalArgumentException")
    @ParameterizedTest
    @ValueSource(ints = { -1, 1_000_000_000 })
    void withNano_invalidNano_throwsException(int invalidNano) {
        TaiInstant anyInstant = TaiInstant.ofTaiSeconds(0, 0);
        assertThrows(IllegalArgumentException.class, () -> anyInstant.withNano(invalidNano));
    }

    @Test
    @DisplayName("plus() a zero duration returns an equal instant")
    void plus_zeroDuration_returnsSameInstant() {
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 500);
        TaiInstant result = start.plus(Duration.ZERO);
        assertEquals(start, result);
    }

    private static Stream<Arguments> plusAddsCorrectlyProvider() {
        return Stream.of(
            // Simple cases, no carry
            Arguments.of(10, 500, 5, 1000, 15, 1500),
            Arguments.of(10, 500, -5, -100, 5, 400),
            // Nanos carry-over
            Arguments.of(10, 800_000_000, 0, 300_000_000, 11, 100_000_000),
            // Nanos borrow (adding negative duration)
            Arguments.of(10, 200_000_000, 0, -500_000_000, 9, 700_000_000),
            // Complex cases
            Arguments.of(-4, 666_666_667, 3, 333_333_333, 0, 0),
            Arguments.of(0, 333_333_333, -4, 666_666_667, -3, 0)
        );
    }

    @DisplayName("plus() adds durations correctly")
    @ParameterizedTest(name = "[{index}] {0}s {1}ns + ({2}s {3}ns) = {4}s {5}ns")
    @MethodSource("plusAddsCorrectlyProvider")
    void plus_addsCorrectly(long startSecs, int startNanos, long durationSecs, int durationNanos, long expectedSecs, int expectedNanos) {
        TaiInstant start = TaiInstant.ofTaiSeconds(startSecs, startNanos);
        Duration duration = Duration.ofSeconds(durationSecs, durationNanos);
        TaiInstant result = start.plus(duration);

        assertEquals(expectedSecs, result.getTaiSeconds());
        assertEquals(expectedNanos, result.getNano());
    }

    @Test
    @DisplayName("plus() throws ArithmeticException on seconds overflow")
    void plus_durationCausingOverflow_throwsException() {
        TaiInstant start = TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 0);
        assertThrows(ArithmeticException.class, () -> start.plus(Duration.ofSeconds(1)));

        TaiInstant startMin = TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0);
        assertThrows(ArithmeticException.class, () -> startMin.plus(Duration.ofSeconds(-1)));
    }

    @Test
    @DisplayName("minus() a zero duration returns an equal instant")
    void minus_zeroDuration_returnsSameInstant() {
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 500);
        TaiInstant result = start.minus(Duration.ZERO);
        assertEquals(start, result);
    }

    private static Stream<Arguments> minusSubtractsCorrectlyProvider() {
        return Stream.of(
            // Simple cases, no borrow
            Arguments.of(10, 500, 5, 100, 5, 400),
            Arguments.of(10, 500, -5, -100, 15, 600),
            // Nanos borrow
            Arguments.of(10, 200_000_000, 0, 500_000_000, 9, 700_000_000),
            // Nanos carry-over (subtracting negative duration)
            Arguments.of(10, 800_000_000, 0, -300_000_000, 11, 100_000_000),
            // Complex cases
            Arguments.of(-4, 666_666_667, 3, 333_333_333, -7, 333_333_334),
            Arguments.of(0, 0, 0, 1, -1, 999_999_999)
        );
    }

    @DisplayName("minus() subtracts durations correctly")
    @ParameterizedTest(name = "[{index}] {0}s {1}ns - ({2}s {3}ns) = {4}s {5}ns")
    @MethodSource("minusSubtractsCorrectlyProvider")
    void minus_subtractsCorrectly(long startSecs, int startNanos, long durationSecs, int durationNanos, long expectedSecs, int expectedNanos) {
        TaiInstant start = TaiInstant.ofTaiSeconds(startSecs, startNanos);
        Duration duration = Duration.ofSeconds(durationSecs, durationNanos);
        TaiInstant result = start.minus(duration);

        assertEquals(expectedSecs, result.getTaiSeconds());
        assertEquals(expectedNanos, result.getNano());
    }

    @Test
    @DisplayName("minus() throws ArithmeticException on seconds overflow")
    void minus_durationCausingOverflow_throwsException() {
        TaiInstant start = TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0);
        assertThrows(ArithmeticException.class, () -> start.minus(Duration.ofSeconds(1)));

        TaiInstant startMax = TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 0);
        assertThrows(ArithmeticException.class, () -> startMax.minus(Duration.ofSeconds(-1)));
    }

    @Test
    @DisplayName("comparison methods (compareTo, isBefore, isAfter, equals) are consistent")
    void comparisonMethods_areConsistent() {
        assertTimeLineOrder(
            TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0),
            TaiInstant.ofTaiSeconds(-1, 999_999_998),
            TaiInstant.ofTaiSeconds(-1, 999_999_999),
            TaiInstant.ofTaiSeconds(0, 0),
            TaiInstant.ofTaiSeconds(0, 1),
            TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 999_999_999)
        );
    }

    /**
     * Helper method to assert the consistency of comparison methods.
     * It assumes the provided instants are in ascending chronological order.
     */
    private void assertTimeLineOrder(TaiInstant... instants) {
        for (int i = 0; i < instants.length; i++) {
            TaiInstant a = instants[i];
            for (int j = 0; j < instants.length; j++) {
                TaiInstant b = instants[j];
                if (i < j) {
                    assertTrue(a.compareTo(b) < 0, a + " should be before " + b);
                    assertTrue(a.isBefore(b), a + " should be before " + b);
                    assertFalse(a.isAfter(b), a + " should not be after " + b);
                    assertFalse(a.equals(b), a + " should not equal " + b);
                } else if (i > j) {
                    assertTrue(a.compareTo(b) > 0, a + " should be after " + b);
                    assertFalse(a.isBefore(b), a + " should not be before " + b);
                    assertTrue(a.isAfter(b), a + " should be after " + b);
                    assertFalse(a.equals(b), a + " should not equal " + b);
                } else {
                    assertEquals(0, a.compareTo(b), a + " should compare equal to " + b);
                    assertFalse(a.isBefore(b), a + " should not be before " + b);
                    assertFalse(a.isAfter(b), a + " should not be after " + b);
                    assertTrue(a.equals(b), a + " should equal " + b);
                }
            }
        }
    }

    @Test
    @DisplayName("equals() and hashCode() follow contract")
    void equalsAndHashCode_shouldFollowContract() {
        new EqualsTester()
            .addEqualityGroup(TaiInstant.ofTaiSeconds(5L, 20), TaiInstant.ofTaiSeconds(5L, 20))
            .addEqualityGroup(TaiInstant.ofTaiSeconds(5L, 30))
            .addEqualityGroup(TaiInstant.ofTaiSeconds(6L, 20))
            .testEquals();
    }
}