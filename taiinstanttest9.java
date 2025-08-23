package org.threeten.extra.scale;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for modification, arithmetic, and parsing of {@link TaiInstant}.
 */
public class TaiInstantModificationAndParsingTest {

    private static final int ONE_THIRD_NANOS = 333_333_333;
    private static final int TWO_THIRDS_NANOS = 666_666_667;

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------
    public static Object[][] data_invalidStringFormats() {
        return new Object[][]{
                // Non-numeric seconds
                {"A.123456789s(TAI)"},
                // Non-numeric nanos
                {"123.12345678As(TAI)"},
                // Missing suffix
                {"123.123456789"},
                // Incomplete suffix
                {"123.123456789s"},
                // Leading plus sign not allowed by parser
                {"+123.123456789s(TAI)"},
                // Nanos part must be exactly 9 digits
                {"-123.123s(TAI)"}
        };
    }

    @ParameterizedTest
    @MethodSource("data_invalidStringFormats")
    @DisplayName("parse() should throw DateTimeParseException for invalid formats")
    public void parse_withInvalidFormat_throwsException(String invalidString) {
        assertThrows(DateTimeParseException.class, () -> TaiInstant.parse(invalidString));
    }

    //-----------------------------------------------------------------------
    // withTaiSeconds()
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("withTaiSeconds() should replace the seconds part and keep the nanos part")
    void withTaiSeconds_replacesSecondsComponent() {
        TaiInstant initial = TaiInstant.ofTaiSeconds(10, 12345);
        TaiInstant result = initial.withTaiSeconds(20);

        assertEquals(20, result.getTaiSeconds());
        assertEquals(12345, result.getNano());
    }

    //-----------------------------------------------------------------------
    // withNano()
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("withNano() should replace the nanos part for a valid input")
    void withNano_replacesNanosComponent() {
        TaiInstant initial = TaiInstant.ofTaiSeconds(10, 12345);
        TaiInstant result = initial.withNano(98765);

        assertEquals(10, result.getTaiSeconds());
        assertEquals(98765, result.getNano());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 1_000_000_000})
    @DisplayName("withNano() should throw IllegalArgumentException for out-of-range nanos")
    void withNano_withInvalidNanos_throwsException(int invalidNano) {
        TaiInstant instant = TaiInstant.ofTaiSeconds(10, 12345);
        assertThrows(IllegalArgumentException.class, () -> instant.withNano(invalidNano));
    }

    //-----------------------------------------------------------------------
    // plus()
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("plus() should correctly add a positive duration")
    void plus_addsPositiveDuration() {
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 100);
        Duration toAdd = Duration.ofSeconds(5, 200);
        TaiInstant result = start.plus(toAdd);

        assertEquals(15, result.getTaiSeconds());
        assertEquals(300, result.getNano());
    }

    @Test
    @DisplayName("plus() should handle nano-of-second overflow by incrementing seconds")
    void plus_handlesNanoOverflow() {
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 800_000_000);
        Duration toAdd = Duration.ofNanos(300_000_000); // 0.3 seconds
        TaiInstant result = start.plus(toAdd);

        assertEquals(11, result.getTaiSeconds());
        assertEquals(100_000_000, result.getNano());
    }

    @Test
    @DisplayName("plus() should correctly add values that sum to zero")
    void plus_withMixedSigns_canResultInZero() {
        TaiInstant start = TaiInstant.ofTaiSeconds(-4, TWO_THIRDS_NANOS);
        Duration toAdd = Duration.ofSeconds(3, ONE_THIRD_NANOS);
        TaiInstant result = start.plus(toAdd);

        assertEquals(0, result.getTaiSeconds());
        assertEquals(0, result.getNano());
    }

    @Test
    @DisplayName("plus() should handle arithmetic at epoch second boundaries")
    void plus_handlesEpochSecondBoundaries() {
        TaiInstant start = TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 0);
        Duration toAdd = Duration.ofSeconds(Long.MIN_VALUE, 0);
        TaiInstant result = start.plus(toAdd);

        assertEquals(-1, result.getTaiSeconds());
        assertEquals(0, result.getNano());
    }

    //-----------------------------------------------------------------------
    // minus()
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("minus() should correctly subtract a positive duration")
    void minus_subtractsPositiveDuration() {
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 500);
        Duration toSubtract = Duration.ofSeconds(5, 200);
        TaiInstant result = start.minus(toSubtract);

        assertEquals(5, result.getTaiSeconds());
        assertEquals(300, result.getNano());
    }

    @Test
    @DisplayName("minus() should handle nano-of-second underflow by borrowing from seconds")
    void minus_handlesNanoBorrowing() {
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 100_000_000);
        Duration toSubtract = Duration.ofNanos(300_000_000); // 0.3 seconds
        TaiInstant result = start.minus(toSubtract);

        assertEquals(9, result.getTaiSeconds());
        assertEquals(800_000_000, result.getNano());
    }

    @Test
    @DisplayName("minus() should correctly subtract a negative duration (which is an addition)")
    void minus_subtractsNegativeDuration() {
        TaiInstant start = TaiInstant.ofTaiSeconds(-3, 0);
        Duration toSubtract = Duration.ofSeconds(-4, TWO_THIRDS_NANOS);
        TaiInstant result = start.minus(toSubtract);

        assertEquals(0, result.getTaiSeconds());
        assertEquals(ONE_THIRD_NANOS, result.getNano());
    }

    @Test
    @DisplayName("minus() should handle arithmetic at epoch second boundaries")
    void minus_handlesEpochSecondBoundaries() {
        TaiInstant start = TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 0);
        Duration toSubtract = Duration.ofSeconds(Long.MAX_VALUE, 0);
        TaiInstant result = start.minus(toSubtract);

        assertEquals(0, result.getTaiSeconds());
        assertEquals(0, result.getNano());
    }

    //-----------------------------------------------------------------------
    // of(UtcInstant)
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("of() should throw NullPointerException for a null UtcInstant")
    public void of_nullUtcInstant_throwsException() {
        assertThrows(NullPointerException.class, () -> TaiInstant.of((UtcInstant) null));
    }
}