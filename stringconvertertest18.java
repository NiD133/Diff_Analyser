package org.joda.time.convert;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests the behavior of {@link StringConverter#getDurationMillis(Object)} for invalid inputs.
 */
class StringConverterGetDurationMillisTest {

    /**
     * Provides a stream of invalid ISO 8601 duration strings.
     * Each string is expected to cause an IllegalArgumentException when parsed.
     *
     * @return a stream of invalid duration strings.
     */
    private static Stream<String> invalidDurationStringProvider() {
        return Stream.of(
            "P2Y6M9DXYZ", // Contains invalid characters 'XYZ' at the end.
            "PTS",        // Missing 'T' separator before time components.
            "XT0S",       // Must start with 'P'.
            "PX0S",       // 'X' is not a valid period designator.
            "PT0X",       // 'X' is not a valid time unit designator (expected H, M, or S).
            "PTXS",       // 'X' is not a valid number for a time unit.
            "PT0.0.0S",   // Multiple decimal points are not allowed.
            "PT0-00S",    // A hyphen is not allowed within a number.
            "PT-.001S"    // A negative sign is only allowed at the very start of the string.
        );
    }

    @ParameterizedTest
    @MethodSource("invalidDurationStringProvider")
    void getDurationMillis_whenInputIsInvalid_throwsIllegalArgumentException(String invalidDurationString) {
        // Asserts that parsing an invalid duration string throws an IllegalArgumentException.
        assertThrows(IllegalArgumentException.class, () -> {
            StringConverter.INSTANCE.getDurationMillis(invalidDurationString);
        });
    }
}