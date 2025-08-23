package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * This test suite focuses on the modification, arithmetic, and parsing
 * functionalities of the TaiInstant class.
 */
@DisplayName("TAIInstant Modification, Arithmetic and Parsing Tests")
public class TaiInstantTestTest4 {

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("parse()")
    class ParseTests {

        /**
         * Provides a stream of invalid string formats that should fail parsing.
         * @return A stream of arguments containing invalid strings.
         */
        static Stream<String> provideInvalidStringsForParsing() {
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
        @MethodSource("provideInvalidStringsForParsing")
        @DisplayName("should throw exception for invalid string formats")
        void parse_withInvalidFormat_throwsDateTimeParseException(String invalidString) {
            assertThrows(DateTimeParseException.class, () -> TaiInstant.parse(invalidString));
        }
    }

    //-----------------------------------------------------------------------
    // withTaiSeconds()
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("withTaiSeconds()")
    class WithTaiSecondsTests {

        @Test
        @DisplayName("should update seconds while preserving nanos")
        void withTaiSeconds_updatesSecondsAndPreservesNanos() {
            long initialSeconds = 7L;
            long initialNanos = 12345L;
            long newSeconds = 2L;

            TaiInstant initialInstant = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
            TaiInstant updatedInstant = initialInstant.withTaiSeconds(newSeconds);

            assertEquals(newSeconds, updatedInstant.getTaiSeconds());
            assertEquals(initialNanos, updatedInstant.getNano());
        }
    }

    //-----------------------------------------------------------------------
    // withNano()
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("withNano()")
    class WithNanoTests {

        @Test
        @DisplayName("should update nanos for valid input")
        void withNano_withValidValue_updatesNanos() {
            TaiInstant initialInstant = TaiInstant.ofTaiSeconds(7L, 12345L);
            int newNano = 98765;
            TaiInstant updatedInstant = initialInstant.withNano(newNano);

            assertEquals(7L, updatedInstant.getTaiSeconds());
            assertEquals(newNano, updatedInstant.getNano());
        }

        @ParameterizedTest
        @ValueSource(ints = { -1, 1_000_000_000 })
        @DisplayName("should throw exception for out-of-range nano values")
        void withNano_withInvalidValue_throwsIllegalArgumentException(int invalidNano) {
            TaiInstant instant = TaiInstant.ofTaiSeconds(0L, 0L);
            assertThrows(IllegalArgumentException.class, () -> instant.withNano(invalidNano));
        }
    }

    //-----------------------------------------------------------------------
    // plus(Duration)
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("plus(Duration)")
    class PlusDurationTests {

        @Test
        @DisplayName("adding zero duration returns an equal instant")
        void plus_zeroDuration_returnsEqualInstant() {
            TaiInstant start = TaiInstant.ofTaiSeconds(100, 500);
            TaiInstant result = start.plus(Duration.ZERO);
            assertEquals(start, result);
        }

        @Test
        @DisplayName("when nano addition overflows, correctly carries to seconds")
        void plus_whenNanoAdditionOverflows_incrementsSecond() {
            TaiInstant start = TaiInstant.ofTaiSeconds(10, 800_000_000);
            Duration toAdd = Duration.ofNanos(300_000_000); // 800M + 300M = 1.1B
            TaiInstant result = start.plus(toAdd);

            assertEquals(11, result.getTaiSeconds());
            assertEquals(100_000_000, result.getNano());
        }

        @Test
        @DisplayName("when adding negative duration, correctly subtracts")
        void plus_negativeDuration_subtractsCorrectly() {
            TaiInstant start = TaiInstant.ofTaiSeconds(10, 500);
            Duration toAdd = Duration.ofSeconds(-3, -100);
            TaiInstant result = start.plus(toAdd);

            assertEquals(7, result.getTaiSeconds());
            assertEquals(400, result.getNano());
        }

        @Test
        @DisplayName("when adding negative duration causes nano underflow, correctly borrows from seconds")
        void plus_nanoUnderflow_correctlyBorrowsFromSeconds() {
            TaiInstant start = TaiInstant.ofTaiSeconds(10, 100_000_000);
            Duration toAdd = Duration.ofNanos(-300_000_000); // 100M - 300M = -200M
            TaiInstant result = start.plus(toAdd);

            assertEquals(9, result.getTaiSeconds());
            assertEquals(800_000_000, result.getNano());
        }
    }

    //-----------------------------------------------------------------------
    // minus(Duration)
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("minus(Duration)")
    class MinusDurationTests {

        @Test
        @DisplayName("subtracting zero duration returns an equal instant")
        void minus_zeroDuration_returnsEqualInstant() {
            TaiInstant start = TaiInstant.ofTaiSeconds(100, 500);
            TaiInstant result = start.minus(Duration.ZERO);
            assertEquals(start, result);
        }

        @Test
        @DisplayName("when nano subtraction underflows, correctly borrows from seconds")
        void minus_whenNanoSubtractionUnderflows_decrementsSecond() {
            TaiInstant start = TaiInstant.ofTaiSeconds(10, 100_000_000);
            Duration toSubtract = Duration.ofNanos(300_000_000); // 100M - 300M = -200M
            TaiInstant result = start.minus(toSubtract);

            assertEquals(9, result.getTaiSeconds());
            assertEquals(800_000_000, result.getNano());
        }

        @Test
        @DisplayName("when subtracting negative duration, correctly adds")
        void minus_negativeDuration_addsCorrectly() {
            TaiInstant start = TaiInstant.ofTaiSeconds(10, 100);
            Duration toSubtract = Duration.ofSeconds(-3, -400);
            TaiInstant result = start.minus(toSubtract);

            assertEquals(13, result.getTaiSeconds());
            assertEquals(500, result.getNano());
        }

        @Test
        @DisplayName("when subtracting negative duration causes nano overflow, correctly carries to seconds")
        void minus_nanoOverflow_correctlyCarriesToSeconds() {
            TaiInstant start = TaiInstant.ofTaiSeconds(10, 800_000_000);
            Duration toSubtract = Duration.ofNanos(-300_000_000); // 800M - (-300M) = 1.1B
            TaiInstant result = start.minus(toSubtract);

            assertEquals(11, result.getTaiSeconds());
            assertEquals(100_000_000, result.getNano());
        }
    }

    //-----------------------------------------------------------------------
    // ofTaiSeconds(long, long)
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("ofTaiSeconds(long, long)")
    class OfTaiSecondsTests {

        @Test
        @DisplayName("with negative nano adjustment, normalizes the result")
        void ofTaiSeconds_withNegativeNanoAdjustment_normalizesResult() {
            TaiInstant test = TaiInstant.ofTaiSeconds(2L, -1);
            assertEquals(1, test.getTaiSeconds());
            assertEquals(999_999_999, test.getNano());
        }
    }
}