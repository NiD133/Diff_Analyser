package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests the {@code toString()} method of the {@link DayOfMonth} class.
 */
class DayOfMonthToStringTest {

    /**
     * Provides a stream of all valid integer values for a DayOfMonth (1 to 31).
     *
     * @return a stream of integers from 1 to 31.
     */
    private static Stream<Integer> validDayOfMonthValues() {
        return IntStream.rangeClosed(1, 31).boxed();
    }

    @ParameterizedTest(name = "DayOfMonth.of({0}) should be represented as \"DayOfMonth:{0}\"")
    @MethodSource("validDayOfMonthValues")
    @DisplayName("toString() returns the correctly formatted string for all valid days")
    void toString_returnsCorrectlyFormattedString(int dayValue) {
        // Arrange
        DayOfMonth dayOfMonth = DayOfMonth.of(dayValue);
        String expectedString = "DayOfMonth:" + dayValue;

        // Act
        String actualString = dayOfMonth.toString();

        // Assert
        assertEquals(expectedString, actualString,
                "The string representation should follow the 'DayOfMonth:value' format.");
    }
}