package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("TaiInstant Tests")
class TaiInstantTest {

    private static final TaiInstant T_NEG_ONE = TaiInstant.ofTaiSeconds(-1, 0);
    private static final TaiInstant T_ZERO = TaiInstant.ofTaiSeconds(0, 0);
    private static final TaiInstant T_ONE = TaiInstant.ofTaiSeconds(1, 0);
    private static final TaiInstant T_ONE_NANO = TaiInstant.ofTaiSeconds(1, 1);

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------
    @ParameterizedTest
    @ValueSource(strings = {
            "A.123456789s(TAI)",      // Non-numeric seconds
            "123.12345678As(TAI)",    // Non-numeric nanos
            "123.123456789",          // Missing suffix
            "123.123456789s",         // Missing (TAI)
            "+123.123456789s(TAI)",   // Explicit plus sign not allowed
            "-123.123s(TAI)"          // Nanos must be 9 digits
    })
    void parse_withInvalidFormat_throwsDateTimeParseException(String invalidString) {
        assertThrows(DateTimeParseException.class, () -> TaiInstant.parse(invalidString));
    }

    //-----------------------------------------------------------------------
    // withTaiSeconds()
    //-----------------------------------------------------------------------
    @ParameterizedTest
    @MethodSource("provider_withTaiSeconds")
    void withTaiSeconds_updatesSecondsAndPreservesNanos(long initialSeconds, long initialNanos, long newSeconds) {
        TaiInstant initial = TaiInstant.ofTaiSeconds(initialSeconds, initialNanos);
        TaiInstant result = initial.withTaiSeconds(newSeconds);

        assertAll(
                () -> assertEquals(newSeconds, result.getTaiSeconds()),
                () -> assertEquals(initialNanos, result.getNano())
        );
    }

    static Stream<Arguments> provider_withTaiSeconds() {
        long nanos = 12_345L;
        return Stream.of(
                Arguments.of(0L, nanos, 1L),
                Arguments.of(0L, nanos, -1L),
                Arguments.of(7L, nanos, 2L)
        );
    }

    //-----------------------------------------------------------------------
    // withNano()
    //-----------------------------------------------------------------------
    @Test
    void withNano_updatesNanosAndPreservesSeconds() {
        long seconds = 123L;
        int newNanos = 456;
        TaiInstant initial = TaiInstant.ofTaiSeconds(seconds, 0);
        TaiInstant result = initial.withNano(newNanos);

        assertAll(
                () -> assertEquals(seconds, result.getTaiSeconds()),
                () -> assertEquals(newNanos, result.getNano())
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 1_000_000_000})
    void withNano_withInvalidNano_throwsIllegalArgumentException(int invalidNano) {
        assertThrows(IllegalArgumentException.class, () -> T_ZERO.withNano(invalidNano));
    }

    //-----------------------------------------------------------------------
    // plus(Duration)
    //-----------------------------------------------------------------------
    @Test
    void plus_zeroDuration_returnsSameInstant() {
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 500);
        assertEquals(start, start.plus(Duration.ZERO));
    }

    @Test
    void plus_positiveDuration_withNanoCarry_isCorrect() {
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 800_000_000);
        Duration toAdd = Duration.ofNanos(300_000_000); // 0.3 seconds
        TaiInstant expected = TaiInstant.ofTaiSeconds(11, 100_000_000);

        assertEquals(expected, start.plus(toAdd));
    }

    @Test
    void plus_negativeDuration_withNanoBorrow_isCorrect() {
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 100_000_000);
        Duration toAdd = Duration.ofSeconds(-1, 300_000_000); // -0.7 seconds
        TaiInstant expected = TaiInstant.ofTaiSeconds(9, 400_000_000);

        assertEquals(expected, start.plus(toAdd));
    }

    @Test
    void plus_durationExceedingMax_throwsArithmeticException() {
        TaiInstant start = TaiInstant.ofTaiSeconds(Long.MAX_VALUE, 0);
        Duration toAdd = Duration.ofSeconds(1);
        assertThrows(ArithmeticException.class, () -> start.plus(toAdd));
    }

    //-----------------------------------------------------------------------
    // minus(Duration)
    //-----------------------------------------------------------------------
    @Test
    void minus_zeroDuration_returnsSameInstant() {
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 500);
        assertEquals(start, start.minus(Duration.ZERO));
    }

    @Test
    void minus_positiveDuration_withNanoBorrow_isCorrect() {
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 100_000_000);
        Duration toSubtract = Duration.ofNanos(300_000_000); // 0.3 seconds
        TaiInstant expected = TaiInstant.ofTaiSeconds(9, 800_000_000);

        assertEquals(expected, start.minus(toSubtract));
    }

    @Test
    void minus_negativeDuration_withNanoCarry_isCorrect() {
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 800_000_000);
        Duration toSubtract = Duration.ofSeconds(-1, 300_000_000); // -0.7 seconds
        TaiInstant expected = TaiInstant.ofTaiSeconds(11, 500_000_000);

        assertEquals(expected, start.minus(toSubtract));
    }

    @Test
    void minus_durationExceedingMin_throwsArithmeticException() {
        TaiInstant start = TaiInstant.ofTaiSeconds(Long.MIN_VALUE, 0);
        Duration toSubtract = Duration.ofSeconds(1);
        assertThrows(ArithmeticException.class, () -> start.minus(toSubtract));
    }

    //-----------------------------------------------------------------------
    // Comparison
    //-----------------------------------------------------------------------
    @Test
    void comparisonMethods_workCorrectly() {
        assertAll("comparison",
                () -> assertTrue(T_NEG_ONE.isBefore(T_ZERO)),
                () -> assertFalse(T_ZERO.isBefore(T_NEG_ONE)),
                () -> assertFalse(T_ZERO.isBefore(T_ZERO)),

                () -> assertTrue(T_ZERO.isAfter(T_NEG_ONE)),
                () -> assertFalse(T_NEG_ONE.isAfter(T_ZERO)),
                () -> assertFalse(T_ZERO.isAfter(T_ZERO)),

                () -> assertTrue(T_NEG_ONE.compareTo(T_ZERO) < 0),
                () -> assertTrue(T_ZERO.compareTo(T_NEG_ONE) > 0),
                () -> assertEquals(0, T_ZERO.compareTo(T_ZERO)),
                () -> assertTrue(T_ONE.compareTo(T_ONE_NANO) < 0)
        );
    }

    //-----------------------------------------------------------------------
    // equals() and hashCode()
    //-----------------------------------------------------------------------
    @Test
    void equalsAndHashCode_followContract() {
        new EqualsTester()
                .addEqualityGroup(TaiInstant.ofTaiSeconds(1, 2), TaiInstant.ofTaiSeconds(1, 2))
                .addEqualityGroup(TaiInstant.ofTaiSeconds(2, 2))
                .addEqualityGroup(TaiInstant.ofTaiSeconds(1, 3))
                .testEquals();
    }

    //-----------------------------------------------------------------------
    // Serialization
    //-----------------------------------------------------------------------
    @Test
    void isSerializable() throws IOException, ClassNotFoundException {
        TaiInstant original = TaiInstant.ofTaiSeconds(2, 3);
        
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