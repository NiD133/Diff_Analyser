package com.google.common.math;

import static com.google.common.math.StatsTesting.EMPTY_STATS_ITERABLE;
import static com.google.common.math.StatsTesting.MANY_VALUES;
import static com.google.common.math.StatsTesting.MANY_VALUES_STATS_ITERABLE;
import static com.google.common.math.StatsTesting.ONE_VALUE;
import static com.google.common.math.StatsTesting.ONE_VALUE_STATS;
import static com.google.common.math.StatsTesting.OTHER_MANY_VALUES;
import static com.google.common.math.StatsTesting.OTHER_ONE_VALUE;
import static com.google.common.math.StatsTesting.OTHER_TWO_VALUES;
import static com.google.common.math.StatsTesting.TWO_VALUES;
import static com.google.common.math.StatsTesting.TWO_VALUES_STATS;
import static com.google.common.math.StatsTesting.assertStatsApproxEqual;
import static com.google.common.math.StatsTesting.createFilledPairedStatsAccumulator;
import static com.google.common.math.StatsTesting.createPartitionedFilledPairedStatsAccumulator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link PairedStatsAccumulator#xStats()}.
 */
@RunWith(JUnit4.class)
public class PairedStatsAccumulatorXStatsTest {

  @Test
  public void xStats_forEmptyAccumulator_isEmpty() {
    // Test an accumulator that is empty from the start.
    PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
    assertStatsApproxEqual(EMPTY_STATS_ITERABLE, accumulator.xStats());

    // Test an accumulator that is empty after adding empty stats.
    PairedStatsAccumulator accumulatorFromAddAll = new PairedStatsAccumulator();
    accumulatorFromAddAll.addAll(new PairedStatsAccumulator().snapshot());
    assertStatsApproxEqual(EMPTY_STATS_ITERABLE, accumulatorFromAddAll.xStats());
  }

  @Test
  public void xStats_forSingleValuePair_isCorrect() {
    // Test an accumulator with one pair added via add().
    PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
    accumulator.add(ONE_VALUE, OTHER_ONE_VALUE);
    assertStatsApproxEqual(ONE_VALUE_STATS, accumulator.xStats());

    // Test that adding empty stats to a non-empty accumulator is a no-op.
    PairedStatsAccumulator accumulatorAfterAddingEmpty = new PairedStatsAccumulator();
    accumulatorAfterAddingEmpty.add(ONE_VALUE, OTHER_ONE_VALUE);
    accumulatorAfterAddingEmpty.addAll(new PairedStatsAccumulator().snapshot());
    assertStatsApproxEqual(ONE_VALUE_STATS, accumulatorAfterAddingEmpty.xStats());
  }

  @Test
  public void xStats_forTwoValuePairs_isCorrect() {
    // Test an accumulator with two pairs added individually via add().
    PairedStatsAccumulator accumulator =
        createFilledPairedStatsAccumulator(TWO_VALUES, OTHER_TWO_VALUES);
    assertStatsApproxEqual(TWO_VALUES_STATS, accumulator.xStats());

    // Test an accumulator with two pairs added from another PairedStats object via addAll().
    PairedStatsAccumulator accumulatorFromAddAll =
        createPartitionedFilledPairedStatsAccumulator(TWO_VALUES, OTHER_TWO_VALUES, 1);
    assertStatsApproxEqual(TWO_VALUES_STATS, accumulatorFromAddAll.xStats());
  }

  @Test
  public void xStats_forManyValuePairs_isCorrect() {
    // Test an accumulator with many pairs added individually via add().
    PairedStatsAccumulator accumulator =
        createFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES);
    assertStatsApproxEqual(MANY_VALUES_STATS_ITERABLE, accumulator.xStats());

    // Test an accumulator with many pairs added from another PairedStats object via addAll().
    PairedStatsAccumulator accumulatorFromAddAll =
        createPartitionedFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES, 2);
    assertStatsApproxEqual(MANY_VALUES_STATS_ITERABLE, accumulatorFromAddAll.xStats());
  }
}