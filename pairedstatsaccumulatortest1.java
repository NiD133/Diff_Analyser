package com.google.common.math;

import static com.google.common.math.StatsTesting.MANY_VALUES;
import static com.google.common.math.StatsTesting.MANY_VALUES_COUNT;
import static com.google.common.math.StatsTesting.ONE_VALUE;
import static com.google.common.math.StatsTesting.OTHER_MANY_VALUES;
import static com.google.common.math.StatsTesting.OTHER_ONE_VALUE;
import static com.google.common.math.StatsTesting.OTHER_TWO_VALUES;
import static com.google.common.math.StatsTesting.TWO_VALUES;
import static com.google.common.math.StatsTesting.createFilledPairedStatsAccumulator;
import static com.google.common.math.StatsTesting.createPartitionedFilledPairedStatsAccumulator;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link PairedStatsAccumulator#count()}.
 */
@DisplayName("PairedStatsAccumulator.count()")
class PairedStatsAccumulatorTest {

    /**
     * Provides various PairedStatsAccumulator instances for testing. Each instance is created using
     * different methods (e.g., direct `add`, `addAll` with partitions) to ensure consistent behavior.
     */
    private static Stream<Arguments> accumulatorsProvider() {
        // Scenario 1: Empty accumulator
        PairedStatsAccumulator emptyAccumulator = new PairedStatsAccumulator();
        PairedStatsAccumulator emptyAccumulatorByAddAll = new PairedStatsAccumulator();
        emptyAccumulatorByAddAll.addAll(new PairedStatsAccumulator().snapshot());

        // Scenario 2: Accumulator with a single pair of values
        PairedStatsAccumulator oneValueAccumulator = new PairedStatsAccumulator();
        oneValueAccumulator.add(ONE_VALUE, OTHER_ONE_VALUE);
        PairedStatsAccumulator oneValueAccumulatorByAddAll = new PairedStatsAccumulator();
        oneValueAccumulatorByAddAll.add(ONE_VALUE, OTHER_ONE_VALUE);
        oneValueAccumulatorByAddAll.addAll(new PairedStatsAccumulator().snapshot()); // Add an empty snapshot

        // Scenario 3: Accumulator with two pairs of values
        PairedStatsAccumulator twoValuesAccumulator =
                createFilledPairedStatsAccumulator(TWO_VALUES, OTHER_TWO_VALUES);
        PairedStatsAccumulator twoValuesAccumulatorByAddAll =
                createPartitionedFilledPairedStatsAccumulator(TWO_VALUES, OTHER_TWO_VALUES, 1);

        // Scenario 4: Accumulator with many pairs of values
        PairedStatsAccumulator manyValuesAccumulator =
                createFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES);
        PairedStatsAccumulator manyValuesAccumulatorByAddAll =
                createPartitionedFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES, 2);

        return Stream.of(
                arguments("Empty accumulator", emptyAccumulator, 0L),
                arguments("Empty accumulator from addAll(empty)", emptyAccumulatorByAddAll, 0L),
                arguments("Single-pair accumulator", oneValueAccumulator, 1L),
                arguments("Single-pair accumulator with addAll(empty)", oneValueAccumulatorByAddAll, 1L),
                arguments("Two-pair accumulator", twoValuesAccumulator, 2L),
                arguments("Two-pair accumulator from partitioned addAll", twoValuesAccumulatorByAddAll, 2L),
                arguments("Many-pair accumulator", manyValuesAccumulator, (long) MANY_VALUES_COUNT),
                arguments("Many-pair accumulator from partitioned addAll", manyValuesAccumulatorByAddAll, (long) MANY_VALUES_COUNT)
        );
    }

    @ParameterizedTest(name = "[{index}] {0}: count should be {2}")
    @MethodSource("accumulatorsProvider")
    void count_returnsCorrectNumberOfPairs(
            String description, PairedStatsAccumulator accumulator, long expectedCount) {
        // WHEN the count is retrieved
        long actualCount = accumulator.count();

        // THEN it should match the number of pairs added
        assertThat(actualCount).isEqualTo(expectedCount);
    }
}