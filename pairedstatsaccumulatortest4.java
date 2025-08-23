package com.google.common.math;

import static com.google.common.math.StatsTesting.EMPTY_STATS_ITERABLE;
import static com.google.common.math.StatsTesting.MANY_VALUES;
import static com.google.common.math.StatsTesting.ONE_VALUE;
import static com.google.common.math.StatsTesting.OTHER_MANY_VALUES;
import static com.google.common.math.StatsTesting.OTHER_MANY_VALUES_STATS;
import static com.google.common.math.StatsTesting.OTHER_ONE_VALUE;
import static com.google.common.math.StatsTesting.OTHER_ONE_VALUE_STATS;
import static com.google.common.math.StatsTesting.OTHER_TWO_VALUES;
import static com.google.common.math.StatsTesting.OTHER_TWO_VALUES_STATS;
import static com.google.common.math.StatsTesting.TWO_VALUES;
import static com.google.common.math.StatsTesting.assertStatsApproxEqual;
import static com.google.common.math.StatsTesting.createFilledPairedStatsAccumulator;
import static com.google.common.math.StatsTesting.createPartitionedFilledPairedStatsAccumulator;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.jspecify.annotations.NullUnmarked;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests for {@link PairedStatsAccumulator#yStats()}.
 *
 * <p>This test class is structured into two parts:
 * <ul>
 *   <li>A {@link Parameterized} test for common scenarios (empty, one, two, and many values).
 *   <li>A standard test class for specific edge cases.
 * </ul>
 */
@RunWith(Enclosed.class)
@NullUnmarked
public class PairedStatsAccumulatorYStatsTest {

  /**
   * Parameterized tests for common scenarios. Verifies that {@code yStats()} returns the correct
   * statistics regardless of whether the accumulator is populated one-by-one (with {@code add}) or
   * in bulk (with {@code addAll}).
   */
  @RunWith(Parameterized.class)
  public static class CommonScenariosTest {

    private final Iterable<Double> xValues;
    private final Iterable<Double> yValues;
    private final Stats expectedYStats;

    public CommonScenariosTest(
        String scenario,
        Iterable<Double> xValues,
        Iterable<Double> yValues,
        Stats expectedYStats) {
      this.xValues = xValues;
      this.yValues = yValues;
      this.expectedYStats = expectedYStats;
    }

    @Parameters(name = "{0}")
    public static Collection<Object[]> data() {
      return Arrays.asList(
          new Object[][] {
            {"with no values", Collections.emptyList(), Collections.emptyList(), EMPTY_STATS_ITERABLE},
            {
              "with one value",
              Collections.singletonList(ONE_VALUE),
              Collections.singletonList(OTHER_ONE_VALUE),
              OTHER_ONE_VALUE_STATS
            },
            {"with two values", TWO_VALUES, OTHER_TWO_VALUES, OTHER_TWO_VALUES_STATS},
            {"with many values", MANY_VALUES, OTHER_MANY_VALUES, OTHER_MANY_VALUES_STATS}
          });
    }

    @Test
    public void yStats_whenPopulatedWithAdd_isCorrect() {
      PairedStatsAccumulator accumulator = createFilledPairedStatsAccumulator(xValues, yValues);
      assertStatsApproxEqual(expectedYStats, accumulator.yStats());
    }

    @Test
    public void yStats_whenPopulatedWithAddAll_isCorrect() {
      // Partition into 2 chunks to test the addAll aggregation logic.
      // For 0 or 1 elements, this is equivalent to adding a single PairedStats object.
      PairedStatsAccumulator accumulator =
          createPartitionedFilledPairedStatsAccumulator(xValues, yValues, 2);
      assertStatsApproxEqual(expectedYStats, accumulator.yStats());
    }
  }

  /** Tests for edge case behaviors of {@code yStats()}. */
  public static class EdgeCasesTest {

    private static final PairedStats EMPTY_PAIRED_STATS = new PairedStatsAccumulator().snapshot();

    @Test
    public void yStats_afterAddingEmptyStatsToNonEmptyAccumulator_isUnchanged() {
      PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
      accumulator.add(ONE_VALUE, OTHER_ONE_VALUE);

      // Snapshot the state before the operation.
      Stats statsBefore = accumulator.yStats();

      // Perform the operation.
      accumulator.addAll(EMPTY_PAIRED_STATS);

      // Assert the state is unchanged.
      assertStatsApproxEqual(statsBefore, accumulator.yStats());
    }
  }
}