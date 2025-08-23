package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Test class for {@link TaiInstant}.
 * This class focuses on creation, modification, and arithmetic operations.
 */
public class TaiInstantTest {

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------
    private static Stream<Arguments> provider_invalidParseStrings() {
        return Stream.of(
            // reason, invalid string
            Arguments.of("non-numeric seconds", "A.123456789s(TAI)"),
            Arguments.of("non-numeric nanos", "123.12345678As(TAI)"),
            Arguments.of("missing suffix", "123.123456789"),
            Arguments.of("missing (TAI)", "123.123456789s"),
            Arguments.of("leading plus not allowed", "+123.123456789s(TAI)"),
            Arguments.of("nanos must be 9 digits", "-123.123s(TAI)")
        );
    }

    @ParameterizedTest(name = "parse() should fail for: {0}")
    @MethodSource("provider_invalidParseStrings")
    public void parse_withInvalidFormat_throwsException(String reason, String invalidString) {
        assertThrows(DateTimeParseException.class, () -> TaiInstant.parse(invalidString));
    }

    //-----------------------------------------------------------------------
    // withTaiSeconds()
    //-----------------------------------------------------------------------
    @Test
    public void withTaiSeconds_setsSecondsAndPreservesNanos() {
        long initialNanos = 12_345L;
        TaiInstant initial = TaiInstant.ofTaiSeconds(10L, initialNanos);

        long newTaiSeconds = 20L;
        TaiInstant result = initial.withTaiSeconds(newTaiSeconds);

        assertEquals(newTaiSeconds, result.getTaiSeconds());
        assertEquals(initialNanos, result.getNano());
    }

    //-----------------------------------------------------------------------
    // withNano()
    //-----------------------------------------------------------------------
    @Test
    public void withNano_setsNanosAndPreservesSeconds() {
        long initialSeconds = 10L;
        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, 12_345);

        int newNano = 54_321;
        TaiInstant result = initial.withNano(newNano);

        assertEquals(initialSeconds, result.getTaiSeconds());
        assertEquals(newNano, result.getNano());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 1_000_000_000})
    public void withNano_withInvalidValue_throwsException(int invalidNano) {
        TaiInstant instant = TaiInstant.ofTaiSeconds(0, 0);
        assertThrows(IllegalArgumentException.class, () -> instant.withNano(invalidNano));
    }

    //-----------------------------------------------------------------------
    // plus()
    //-----------------------------------------------------------------------
    @Test
    public void plus_addsPositiveDuration_noNanoCarry() {
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 100_000_000);
        Duration toAdd = Duration.ofSeconds(5, 200_000_000);
        
        TaiInstant result = start.plus(toAdd);
        
        assertEquals(15, result.getTaiSeconds());
        assertEquals(300_000_000, result.getNano());
    }

    @Test
    public void plus_addsPositiveDuration_withNanoCarry() {
        // 800M nanos + 300M nanos = 1.1B nanos, which is 1 second and 100M nanos
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 800_000_000);
        Duration toAdd = Duration.ofSeconds(5, 300_000_000);
        
        TaiInstant result = start.plus(toAdd);
        
        assertEquals(16, result.getTaiSeconds()); // 10s + 5s + 1s (from carry)
        assertEquals(100_000_000, result.getNano());
    }

    @Test
    public void plus_addsNegativeDuration_withNanoBorrow() {
        // 200M nanos - 300M nanos requires borrowing 1 second
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 200_000_000);
        Duration toAdd = Duration.ofSeconds(-5, -300_000_000); // Equivalent to subtraction
        
        TaiInstant result = start.plus(toAdd);
        
        assertEquals(4, result.getTaiSeconds()); // 10s - 5s - 1s (from borrow)
        assertEquals(900_000_000, result.getNano()); // 1_000_000_000 + 200_000_000 - 300_000_000
    }

    @Test
    public void plus_zeroDuration_isNoOp() {
        TaiInstant start = TaiInstant.ofTaiSeconds(123, 456);
        TaiInstant result = start.plus(Duration.ZERO);
        assertEquals(start, result);
    }

    @Test
    public void plus_durationCausingOverflow_throwsException() {
        TaiInstant start = TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 0);
        Duration toAdd = Duration.ofSeconds(1);
        assertThrows(ArithmeticException.class, () -> start.plus(toAdd));
    }

    //-----------------------------------------------------------------------
    // minus()
    //-----------------------------------------------------------------------
    @Test
    public void minus_subtractsPositiveDuration_noNanoBorrow() {
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 800_000_000);
        Duration toSubtract = Duration.ofSeconds(5, 300_000_000);
        
        TaiInstant result = start.minus(toSubtract);
        
        assertEquals(5, result.getTaiSeconds());
        assertEquals(500_000_000, result.getNano());
    }

    @Test
    public void minus_subtractsPositiveDuration_withNanoBorrow() {
        // 200M nanos - 300M nanos requires borrowing 1 second
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 200_000_000);
        Duration toSubtract = Duration.ofSeconds(5, 300_000_000);
        
        TaiInstant result = start.minus(toSubtract);
        
        assertEquals(4, result.getTaiSeconds()); // 10s - 5s - 1s (from borrow)
        assertEquals(900_000_000, result.getNano()); // 1_000_000_000 + 200_000_000 - 300_000_000
    }

    @Test
    public void minus_subtractsNegativeDuration_isAddition() {
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 100_000_000);
        Duration toSubtract = Duration.ofSeconds(-5, -200_000_000);
        
        TaiInstant result = start.minus(toSubtract);
        
        assertEquals(15, result.getTaiSeconds());
        assertEquals(300_000_000, result.getNano());
    }

    @Test
    public void minus_zeroDuration_isNoOp() {
        TaiInstant start = TaiInstant.ofTaiSeconds(123, 456);
        TaiInstant result = start.minus(Duration.ZERO);
        assertEquals(start, result);
    }

    @Test
    public void minus_durationCausingUnderflow_throwsException() {
        TaiInstant start = TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0);
        Duration toSubtract = Duration.ofSeconds(1);
        assertThrows(ArithmeticException.class, () -> start.minus(toSubtract));
    }

    //-----------------------------------------------------------------------
    // toString()
    //-----------------------------------------------------------------------
    @Test
    public void toString_returnsCorrectFormat() {
        // Standard case
        TaiInstant t1 = TaiInstant.ofTaiSeconds(123L, 123_456_789);
        assertEquals("123.123456789s(TAI)", t1.toString());

        // Negative seconds and nano padding
        TaiInstant t2 = TaiInstant.ofTaiSeconds(-123L, 1);
        assertEquals("-123.000000001s(TAI)", t2.toString());
        
        // Zero seconds
        TaiInstant t3 = TaiInstant.ofTaiSeconds(0L, 123);
        assertEquals("0.000000123s(TAI)", t3.toString());
    }
}