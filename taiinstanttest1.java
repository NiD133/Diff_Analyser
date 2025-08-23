package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Serializable;
import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Test class for {@link TaiInstant}.
 */
@DisplayName("TaiInstant")
class TaiInstantTest {

    private static final int NANOS_PER_SECOND = 1_000_000_000;
    private static final TaiInstant TAI_ZERO = TaiInstant.ofTaiSeconds(0, 0);

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------
    @DisplayName("parse() should throw for invalid formats")
    @ParameterizedTest(name = "for input string: \"{0}\"")
    @ValueSource(strings = {
            "A.123456789s(TAI)",      // Non-numeric seconds
            "123.12345678As(TAI)",    // Non-numeric nanos
            "123.123456789",          // Missing suffix
            "123.123456789s",         // Missing (TAI)
            "+123.123456789s(TAI)",   // Explicit positive sign not allowed
            "-123.123s(TAI)"          // Nanos must be 9 digits
    })
    void parse_withInvalidFormat_throwsDateTimeParseException(String invalidString) {
        assertThrows(DateTimeParseException.class, () -> TaiInstant.parse(invalidString));
    }

    //-----------------------------------------------------------------------
    // withTaiSeconds()
    //-----------------------------------------------------------------------
    @DisplayName("withTaiSeconds() should update seconds and preserve nanos")
    @ParameterizedTest(name = "from {0}s {1}ns, setting seconds to {2} -> {2}s {1}ns")
    @CsvSource({
            "0, 12345,  1",
            "0, 12345, -1",
            "7, 12345,  2",
    })
    void withTaiSeconds_updatesSecondsAndPreservesNanos(long initialSeconds, long initialNanos, long newSeconds) {
        // Arrange
        TaiInstant initialInstant = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);

        // Act
        TaiInstant result = initialInstant.withTaiSeconds(newSeconds);

        // Assert
        assertAll(
                () -> assertEquals(newSeconds, result.getTaiSeconds(), "Seconds should be updated"),
                () -> assertEquals(initialNanos, result.getNano(), "Nanos should be preserved")
        );
    }

    //-----------------------------------------------------------------------
    // withNano()
    //-----------------------------------------------------------------------
    @DisplayName("withNano() should update nanos and preserve seconds")
    @ParameterizedTest(name = "from {0}s {1}ns, setting nanos to {2} -> {0}s {2}ns")
    @CsvSource({
            "  0, 12345,           1",
            "  7, 12345,           2",
            "-99, 12345,           3",
            "-99, 12345, 999999999"
    })
    void withNano_updatesNanosAndPreservesSeconds(long initialSeconds, long initialNanos, int newNanos) {
        // Arrange
        TaiInstant initialInstant = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);

        // Act
        TaiInstant result = initialInstant.withNano(newNanos);

        // Assert
        assertAll(
                () -> assertEquals(initialSeconds, result.getTaiSeconds(), "Seconds should be preserved"),
                () -> assertEquals(newNanos, result.getNano(), "Nanos should be updated")
        );
    }

    @DisplayName("withNano() should throw for out-of-range values")
    @ParameterizedTest(name = "for invalid nano value: {0}")
    @ValueSource(ints = {-1, 1_000_000_000})
    void withNano_withInvalidValue_throwsIllegalArgumentException(int invalidNano) {
        // Arrange
        TaiInstant instant = TAI_ZERO;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> instant.withNano(invalidNano));
    }

    //-----------------------------------------------------------------------
    // plus(Duration)
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("plus() should add a duration correctly when nanos do not overflow")
    void plus_addsDurationWithoutNanoOverflow() {
        // Arrange
        TaiInstant initial = TaiInstant.ofTaiSeconds(5, 100_000_000);
        Duration toAdd = Duration.ofSeconds(10, 200_000_000);

        // Act
        TaiInstant result = initial.plus(toAdd);

        // Assert
        assertEquals(15, result.getTaiSeconds());
        assertEquals(300_000_000, result.getNano());
    }

    @Test
    @DisplayName("plus() should add a duration correctly when nanos overflow")
    void plus_addsDurationWithNanoOverflow() {
        // Arrange
        TaiInstant initial = TaiInstant.ofTaiSeconds(5, 800_000_000);
        Duration toAdd = Duration.ofSeconds(10, 300_000_000); // 800M + 300M = 1.1s

        // Act
        TaiInstant result = initial.plus(toAdd);

        // Assert
        assertEquals(16, result.getTaiSeconds()); // 5s + 10s + 1s (from overflow)
        assertEquals(100_000_000, result.getNano()); // 1.1s -> 100M ns
    }

    @Test
    @DisplayName("plus() should handle adding a negative duration")
    void plus_addsNegativeDuration() {
        // Arrange
        TaiInstant initial = TaiInstant.ofTaiSeconds(10, 500_000_000);
        Duration toAdd = Duration.ofSeconds(-2, -100_000_000);

        // Act
        TaiInstant result = initial.plus(toAdd);

        // Assert
        assertEquals(8, result.getTaiSeconds());
        assertEquals(400_000_000, result.getNano());
    }

    //-----------------------------------------------------------------------
    // minus(Duration)
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("minus() should subtract a duration correctly when nanos do not underflow")
    void minus_subtractsDurationWithoutNanoBorrow() {
        // Arrange
        TaiInstant initial = TaiInstant.ofTaiSeconds(15, 300_000_000);
        Duration toSubtract = Duration.ofSeconds(10, 200_000_000);

        // Act
        TaiInstant result = initial.minus(toSubtract);

        // Assert
        assertEquals(5, result.getTaiSeconds());
        assertEquals(100_000_000, result.getNano());
    }

    @Test
    @DisplayName("minus() should subtract a duration correctly when nanos underflow (borrow)")
    void minus_subtractsDurationWithNanoBorrow() {
        // Arrange
        TaiInstant initial = TaiInstant.ofTaiSeconds(5, 100_000_000);
        Duration toSubtract = Duration.ofSeconds(2, 200_000_000); // 100M - 200M = -100M

        // Act
        TaiInstant result = initial.minus(toSubtract);

        // Assert
        assertEquals(2, result.getTaiSeconds()); // 5s - 2s - 1s (borrowed)
        assertEquals(900_000_000, result.getNano()); // 1,100M - 200M = 900M
    }

    @Test
    @DisplayName("minus() should handle subtracting a negative duration")
    void minus_subtractsNegativeDuration() {
        // Arrange
        TaiInstant initial = TaiInstant.ofTaiSeconds(10, 500_000_000);
        Duration toSubtract = Duration.ofSeconds(-2, -100_000_000);

        // Act
        TaiInstant result = initial.minus(toSubtract);

        // Assert
        assertEquals(12, result.getTaiSeconds());
        assertEquals(600_000_000, result.getNano());
    }

    //-----------------------------------------------------------------------
    // compareTo(), isBefore(), isAfter(), equals()
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("comparison methods should work correctly")
    void comparisonMethods_workCorrectly() {
        // Arrange
        TaiInstant t1 = TaiInstant.ofTaiSeconds(10, 0);
        TaiInstant t2 = TaiInstant.ofTaiSeconds(10, 1);
        TaiInstant t3 = TaiInstant.ofTaiSeconds(11, 0);
        TaiInstant t1_equal = TaiInstant.ofTaiSeconds(10, 0);

        // Assert
        assertAll("t1 comparisons",
                () -> assertTrue(t1.compareTo(t2) < 0, "t1 should be less than t2"),
                () -> assertTrue(t1.isBefore(t2), "t1 should be before t2"),
                () -> assertFalse(t1.isAfter(t2), "t1 should not be after t2"),
                () -> assertEquals(0, t1.compareTo(t1_equal), "t1 should be equal to t1_equal"),
                () -> assertTrue(t1.equals(t1_equal), "t1 should equal t1_equal")
        );

        assertAll("t2 comparisons",
                () -> assertTrue(t2.compareTo(t1) > 0, "t2 should be greater than t1"),
                () -> assertFalse(t2.isBefore(t1), "t2 should not be before t1"),
                () -> assertTrue(t2.isAfter(t1), "t2 should be after t1")
        );

        assertAll("transitivity",
                () -> assertTrue(t1.isBefore(t3), "t1 should be before t3"),
                () -> assertTrue(t2.isBefore(t3), "t2 should be before t3")
        );
    }

    //-----------------------------------------------------------------------
    // General contract tests
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("class should implement Serializable and Comparable")
    void class_implementsSerializableAndComparable() {
        assertTrue(Serializable.class.isAssignableFrom(TaiInstant.class));
        assertTrue(Comparable.class.isAssignableFrom(TaiInstant.class));
    }
}