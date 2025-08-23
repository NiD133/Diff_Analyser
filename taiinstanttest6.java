package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.Instant;
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
 * Tests for parsing, modification, and arithmetic operations on {@link TaiInstant}.
 */
class TaiInstantArithmeticTest {

    private static final long NANOS_PER_SECOND = 1_000_000_000L;

    @Nested
    @DisplayName("Factory and Parsing")
    class FactoryAndParsing {

        @Test
        void of_fromInstant_convertsCorrectly() {
            // The TAI epoch is 1958-01-01. The Unix epoch (Instant.EPOCH) is 1970-01-01.
            // There are 4383 days between them (12 years, including 3 leap years: 1960, 1964, 1968).
            final long DAYS_BETWEEN_EPOCHS = (1970 - 1958) * 365 + 3;
            final long SECONDS_PER_DAY = 24 * 60 * 60;
            // At the Unix epoch, the offset between TAI and UTC was 10 seconds.
            // This is a simplification of a complex history, but it's the standard offset used for conversions.
            final long TAI_UTC_OFFSET_SECONDS = 10;

            long expectedTaiSecondsAtUnixEpoch = (DAYS_BETWEEN_EPOCHS * SECONDS_PER_DAY) + TAI_UTC_OFFSET_SECONDS;

            // Test with an instant at the Unix epoch plus 2 nanoseconds
            Instant instant = Instant.ofEpochSecond(0, 2);
            TaiInstant test = TaiInstant.of(instant);

            assertEquals(expectedTaiSecondsAtUnixEpoch, test.getTaiSeconds());
            assertEquals(2, test.getNano());
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "A.123456789s(TAI)",      // Non-numeric seconds
            "123.12345678As(TAI)",    // Non-numeric nanos
            "123.123456789",          // Missing suffix
            "123.123456789s",         // Missing (TAI)
            "+123.123456789s(TAI)",   // Explicit positive sign not allowed
            "-123.123s(TAI)"          // Nanos must be 9 digits
        })
        void parse_withInvalidFormat_throwsException(String invalidString) {
            assertThrows(DateTimeParseException.class, () -> TaiInstant.parse(invalidString));
        }
    }

    @Nested
    @DisplayName("Modification")
    class Modification {

        @Test
        void withTaiSeconds_replacesSecondsComponent() {
            TaiInstant initial = TaiInstant.ofTaiSeconds(100, 12345);
            TaiInstant result = initial.withTaiSeconds(200);
            assertEquals(200, result.getTaiSeconds());
            assertEquals(12345, result.getNano());
        }

        @Test
        void withNano_replacesNanoComponent() {
            TaiInstant initial = TaiInstant.ofTaiSeconds(100, 12345);
            TaiInstant result = initial.withNano(54321);
            assertEquals(100, result.getTaiSeconds());
            assertEquals(54321, result.getNano());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.scale.TaiInstantArithmeticTest#provideInvalidNanoValues")
        void withNano_withInvalidNano_throwsException(int invalidNano) {
            TaiInstant instant = TaiInstant.ofTaiSeconds(0, 0);
            assertThrows(IllegalArgumentException.class, () -> instant.withNano(invalidNano));
        }
    }

    static Stream<Arguments> provideInvalidNanoValues() {
        return Stream.of(
            Arguments.of(-1),
            Arguments.of(1_000_000_000)
        );
    }

    @Nested
    @DisplayName("Arithmetic: plus()")
    class PlusOperation {

        @Test
        void plus_zeroDuration_returnsSameInstant() {
            TaiInstant initial = TaiInstant.ofTaiSeconds(50, 100);
            TaiInstant result = initial.plus(Duration.ZERO);
            assertEquals(initial, result);
        }

        @Test
        void plus_positiveDuration_noNanoCarry() {
            TaiInstant initial = TaiInstant.ofTaiSeconds(10, 500_000_000);
            Duration toAdd = Duration.ofSeconds(5, 100_000_000);
            TaiInstant result = initial.plus(toAdd);
            assertEquals(15, result.getTaiSeconds());
            assertEquals(600_000_000, result.getNano());
        }

        @Test
        void plus_positiveDuration_withNanoCarry() {
            TaiInstant initial = TaiInstant.ofTaiSeconds(10, 800_000_000);
            Duration toAdd = Duration.ofNanos(300_000_000); // 0.3 seconds
            TaiInstant result = initial.plus(toAdd);
            assertEquals(11, result.getTaiSeconds());
            assertEquals(100_000_000, result.getNano());
        }

        @Test
        void plus_negativeDuration_noNanoBorrow() {
            TaiInstant initial = TaiInstant.ofTaiSeconds(10, 800_000_000);
            Duration toAdd = Duration.ofSeconds(-5, -300_000_000);
            TaiInstant result = initial.plus(toAdd);
            assertEquals(5, result.getTaiSeconds());
            assertEquals(500_000_000, result.getNano());
        }

        @Test
        void plus_negativeDuration_withNanoBorrow() {
            TaiInstant initial = TaiInstant.ofTaiSeconds(10, 200_000_000);
            Duration toAdd = Duration.ofNanos(-500_000_000);
            TaiInstant result = initial.plus(toAdd);
            assertEquals(9, result.getTaiSeconds());
            assertEquals(NANOS_PER_SECOND - 300_000_000, result.getNano());
        }

        @Test
        void plus_durationThatCrossesZero() {
            TaiInstant initial = TaiInstant.ofTaiSeconds(-2, 800_000_000);
            Duration toAdd = Duration.ofSeconds(3, 400_000_000);
            TaiInstant result = initial.plus(toAdd);
            // -2s + 0.8ns + 3s + 0.4ns = 1s + 1.2ns = 2s + 0.2ns
            assertEquals(2, result.getTaiSeconds());
            assertEquals(200_000_000, result.getNano());
        }
    }

    @Nested
    @DisplayName("Arithmetic: minus()")
    class MinusOperation {

        @Test
        void minus_zeroDuration_returnsSameInstant() {
            TaiInstant initial = TaiInstant.ofTaiSeconds(50, 100);
            TaiInstant result = initial.minus(Duration.ZERO);
            assertEquals(initial, result);
        }

        @Test
        void minus_positiveDuration_noNanoBorrow() {
            TaiInstant initial = TaiInstant.ofTaiSeconds(10, 500_000_000);
            Duration toSubtract = Duration.ofSeconds(5, 100_000_000);
            TaiInstant result = initial.minus(toSubtract);
            assertEquals(5, result.getTaiSeconds());
            assertEquals(400_000_000, result.getNano());
        }

        @Test
        void minus_positiveDuration_withNanoBorrow() {
            TaiInstant initial = TaiInstant.ofTaiSeconds(10, 200_000_000);
            Duration toSubtract = Duration.ofNanos(300_000_000);
            TaiInstant result = initial.minus(toSubtract);
            assertEquals(9, result.getTaiSeconds());
            assertEquals(NANOS_PER_SECOND - 100_000_000, result.getNano());
        }

        @Test
        void minus_negativeDuration_noNanoCarry() {
            TaiInstant initial = TaiInstant.ofTaiSeconds(10, 200_000_000);
            Duration toSubtract = Duration.ofSeconds(-5, -300_000_000);
            TaiInstant result = initial.minus(toSubtract);
            assertEquals(15, result.getTaiSeconds());
            assertEquals(500_000_000, result.getNano());
        }

        @Test
        void minus_negativeDuration_withNanoCarry() {
            TaiInstant initial = TaiInstant.ofTaiSeconds(10, 800_000_000);
            Duration toSubtract = Duration.ofNanos(-500_000_000);
            TaiInstant result = initial.minus(toSubtract);
            assertEquals(11, result.getTaiSeconds());
            assertEquals(300_000_000, result.getNano());
        }

        @Test
        void minus_durationThatCrossesZero() {
            TaiInstant initial = TaiInstant.ofTaiSeconds(2, 200_000_000);
            Duration toSubtract = Duration.ofSeconds(3, 400_000_000);
            TaiInstant result = initial.minus(toSubtract);
            // 2s + 0.2ns - (3s + 0.4ns) = -1s - 0.2ns = -2s + 0.8ns
            assertEquals(-2, result.getTaiSeconds());
            assertEquals(800_000_000, result.getNano());
        }
    }
}