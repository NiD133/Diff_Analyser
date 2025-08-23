package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.testing.EqualsTester;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

@DisplayName("TaiInstant Operations")
class TaiInstantOperationsTest {

    private static final int ONE_THIRD_NANO = 333_333_333;
    private static final int TWO_THIRDS_NANO = 666_666_667;

    @Nested
    @DisplayName("Factory and parsing")
    class FactoryAndParsing {

        static Stream<String> provider_invalidParseStrings() {
            return Stream.of(
                "A.123456789s(TAI)",      // Non-digit seconds
                "123.12345678As(TAI)",     // Non-digit nanos
                "123.123456789",           // Missing suffix
                "123.123456789s",          // Missing (TAI)
                "+123.123456789s(TAI)",    // Leading plus sign not allowed
                "-123.123s(TAI)"           // Nanos must be 9 digits
            );
        }

        @ParameterizedTest(name = "Parsing \"{0}\"")
        @MethodSource("provider_invalidParseStrings")
        void parse_withInvalidFormat_throwsException(String invalidString) {
            assertThrows(DateTimeParseException.class, () -> TaiInstant.parse(invalidString));
        }
    }

    @Nested
    @DisplayName("Modification")
    class Modification {

        static Stream<Arguments> provider_withTaiSeconds() {
            return Stream.of(
                Arguments.of(0L, 12345L, 1L, 1L, 12345L),
                Arguments.of(0L, 12345L, -1L, -1L, 12345L),
                Arguments.of(7L, 12345L, 2L, 2L, 12345L)
            );
        }

        @ParameterizedTest
        @MethodSource("provider_withTaiSeconds")
        void withTaiSeconds_replacesSecondsAndPreservesNanos(
            long initialSeconds, long initialNanos, long newSeconds,
            long expectedSeconds, long expectedNanos) {
            TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
            TaiInstant result = initial.withTaiSeconds(newSeconds);

            assertEquals(expectedSeconds, result.getTaiSeconds());
            assertEquals(expectedNanos, result.getNano());
        }

        @Test
        void withNano_givenValidNano_replacesNanosAndPreservesSeconds() {
            TaiInstant initial = TaiInstant.ofTaiSeconds(10, 500);
            TaiInstant result = initial.withNano(999);
            assertEquals(10, result.getTaiSeconds());
            assertEquals(999, result.getNano());
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, 1_000_000_000})
        void withNano_givenInvalidNano_throwsException(int invalidNano) {
            TaiInstant instant = TaiInstant.ofTaiSeconds(0, 0);
            assertThrows(IllegalArgumentException.class, () -> instant.withNano(invalidNano));
        }
    }

    @Nested
    @DisplayName("Arithmetic")
    class Arithmetic {

        @Test
        void plus_addsSecondsAndNanos_noCarry() {
            TaiInstant start = TaiInstant.ofTaiSeconds(5, ONE_THIRD_NANO);
            Duration toAdd = Duration.ofSeconds(3, ONE_THIRD_NANO);
            TaiInstant result = start.plus(toAdd);
            assertEquals(8, result.getTaiSeconds());
            assertEquals(TWO_THIRDS_NANO, result.getNano());
        }

        @Test
        void plus_addsNanos_withCarry() {
            TaiInstant start = TaiInstant.ofTaiSeconds(5, TWO_THIRDS_NANO);
            Duration toAdd = Duration.ofNanos(TWO_THIRDS_NANO);
            TaiInstant result = start.plus(toAdd);
            assertEquals(6, result.getTaiSeconds());
            assertEquals(TWO_THIRDS_NANO - 1, result.getNano());
        }

        @Test
        void plus_addsNegativeDuration_withBorrow() {
            TaiInstant start = TaiInstant.ofTaiSeconds(5, ONE_THIRD_NANO);
            Duration toAdd = Duration.ofSeconds(-2, -TWO_THIRDS_NANO); // equivalent to subtracting
            TaiInstant result = start.plus(toAdd);
            assertEquals(2, result.getTaiSeconds());
            assertEquals(TWO_THIRDS_NANO, result.getNano());
        }

        @Test
        void minus_subtractsSecondsAndNanos_noBorrow() {
            TaiInstant start = TaiInstant.ofTaiSeconds(5, TWO_THIRDS_NANO);
            Duration toSubtract = Duration.ofSeconds(3, ONE_THIRD_NANO);
            TaiInstant result = start.minus(toSubtract);
            assertEquals(2, result.getTaiSeconds());
            assertEquals(ONE_THIRD_NANO, result.getNano());
        }

        @Test
        void minus_subtractsNanos_withBorrow() {
            TaiInstant start = TaiInstant.ofTaiSeconds(5, ONE_THIRD_NANO);
            Duration toSubtract = Duration.ofNanos(TWO_THIRDS_NANO);
            TaiInstant result = start.minus(toSubtract);
            assertEquals(4, result.getTaiSeconds());
            assertEquals(TWO_THIRDS_NANO, result.getNano());
        }

        @Test
        void minus_subtractsNegativeDuration_withCarry() {
            TaiInstant start = TaiInstant.ofTaiSeconds(5, TWO_THIRDS_NANO);
            Duration toSubtract = Duration.ofSeconds(-2, -ONE_THIRD_NANO); // equivalent to adding
            TaiInstant result = start.minus(toSubtract);
            assertEquals(8, result.getTaiSeconds());
            assertEquals(0, result.getNano());
        }

        @Test
        void durationUntil_calculatesDurationBetweenInstants() {
            TaiInstant t1 = TaiInstant.ofTaiSeconds(10, 100);
            TaiInstant t2 = TaiInstant.ofTaiSeconds(12, 50);

            // Positive duration
            assertEquals(Duration.ofSeconds(1, 999_999_950), t1.durationUntil(t2));

            // Negative duration
            assertEquals(Duration.ofSeconds(-1, -999_999_950), t2.durationUntil(t1));

            // Zero duration
            assertEquals(Duration.ZERO, t1.durationUntil(t1));
        }
    }

    @Nested
    @DisplayName("Contracts")
    class Contracts {

        @Test
        void equals_and_hashCode_contract() {
            new EqualsTester()
                .addEqualityGroup(
                    TaiInstant.ofTaiSeconds(1, 1),
                    TaiInstant.ofTaiSeconds(1, 1))
                .addEqualityGroup(
                    TaiInstant.ofTaiSeconds(1, 2))
                .addEqualityGroup(
                    TaiInstant.ofTaiSeconds(2, 1))
                .testEquals();
        }

        @Test
        void serialization_deserializesToSameObject() throws IOException, ClassNotFoundException {
            TaiInstant original = TaiInstant.ofTaiSeconds(123, 456_789_012);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(original);
            }

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                TaiInstant deserialized = (TaiInstant) ois.readObject();
                assertEquals(original, deserialized);
            }
        }
    }
}