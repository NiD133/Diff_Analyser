package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.IntStream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link DayOfYear#toString()} method.
 */
class DayOfYearToStringTest {

    private static final int MAX_POSSIBLE_DAY_OF_YEAR = 366;

    /**
     * Provides a stream of all valid day-of-year values for the parameterized test.
     *
     * @return a stream of integers from 1 to 366.
     */
    private static IntStream allPossibleDayOfYearValues() {
        return IntStream.rangeClosed(1, MAX_POSSIBLE_DAY_OF_YEAR);
    }

    @ParameterizedTest(name = "DayOfYear.of({0}) should be represented as \"DayOfYear:{0}\"")
    @MethodSource("allPossibleDayOfYearValues")
    void toString_returnsCorrectlyFormattedString(int day) {
        // Arrange
        DayOfYear dayOfYear = DayOfYear.of(day);
        String expectedFormat = "DayOfYear:" + day;

        // Act
        String actual = dayOfYear.toString();

        // Assert
        assertEquals(expectedFormat, actual);
    }
}