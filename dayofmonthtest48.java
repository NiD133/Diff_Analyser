package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link DayOfMonth#compareTo(DayOfMonth)}.
 */
class DayOfMonthCompareToTest {

    private static final int MAX_DAY_OF_MONTH = 31;

    /**
     * Provides all possible pairs of DayOfMonth instances for comparison testing.
     *
     * @return a stream of arguments, each containing two DayOfMonth objects and the expected comparison signum.
     */
    private static Stream<Arguments> provideDayOfMonthPairs() {
        // Generates all 31x31 possible pairs of DayOfMonth(i) and DayOfMonth(j)
        // and the expected signum of their comparison.
        return IntStream.rangeClosed(1, MAX_DAY_OF_MONTH).boxed().flatMap(i ->
                IntStream.rangeClosed(1, MAX_DAY_OF_MONTH).mapToObj(j ->
                        Arguments.of(DayOfMonth.of(i), DayOfMonth.of(j), Integer.compare(i, j))
                )
        );
    }

    @DisplayName("compareTo should be consistent with the integer values of the days")
    @ParameterizedTest(name = "DayOfMonth.of({0}).compareTo(DayOfMonth.of({1})) should have signum {2}")
    @MethodSource("provideDayOfMonthPairs")
    void compareTo_returnsCorrectSignum(DayOfMonth day1, DayOfMonth day2, int expectedSignum) {
        int actualSignum = Integer.signum(day1.compareTo(day2));
        assertEquals(expectedSignum, actualSignum);
    }
}