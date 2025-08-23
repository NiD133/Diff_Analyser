package org.joda.time;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the {@link Seconds#parseSeconds(String)} factory method.
 */
@DisplayName("Seconds.parseSeconds(String)")
class SecondsTest {

    @Test
    @DisplayName("should return ZERO when the input string is null")
    void parseSeconds_whenInputIsNull_returnsZero() {
        // The method defines that a null input string results in zero seconds.
        Seconds result = Seconds.parseSeconds(null);
        assertEquals(Seconds.ZERO, result);
    }

    /**
     * The parse method should correctly handle various valid ISO 8601 period formats,
     * extracting only the seconds value.
     */
    @ParameterizedTest(name = "should return {1} seconds for input \"{0}\"")
    @CsvSource({
            "PT0S,         0",
            "PT1S,         1",
            "PT-3S,       -3",
            "P0Y0M0DT2S,   2",  // A more complex format is valid if other fields are zero
            "PT0H2S,       2"   // Hours field is present but zero
    })
    void parseSeconds_whenStringIsValid_returnsCorrectSeconds(String periodString, int expectedSeconds) {
        Seconds result = Seconds.parseSeconds(periodString);
        assertEquals(expectedSeconds, result.getSeconds());
    }

    /**
     * According to the Javadoc, the parse will accept the full ISO syntax,
     * but an exception is thrown if any field other than seconds is non-zero.
     */
    @ParameterizedTest(name = "should throw exception for invalid input \"{0}\"")
    @ValueSource(strings = {
            "P1Y",     // Year field is not allowed
            "P1M",     // Month field is not allowed
            "P1W",     // Week field is not allowed
            "P1D",     // Day field is not allowed
            "PT1H",    // Hour field is not allowed
            "PT1M",    // Minute field is not allowed
            "P1DT1S"   // Day and Second fields are not allowed together
    })
    void parseSeconds_whenStringContainsNonZeroFieldsOtherThanSeconds_throwsException(String invalidPeriodString) {
        assertThrows(IllegalArgumentException.class, () -> {
            Seconds.parseSeconds(invalidPeriodString);
        });
    }
}