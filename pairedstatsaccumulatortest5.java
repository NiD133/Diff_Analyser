package com.google.common.math;

import static com.google.common.math.StatsTesting.ALLOWED_ERROR;
import static com.google.common.math.StatsTesting.ALL_MANY_VALUES;
import static com.google.common.math.StatsTesting.MANY_VALUES;
import static com.google.common.math.StatsTesting.MANY_VALUES_COUNT;
import static com.google.common.math.StatsTesting.MANY_VALUES_SUM_OF_PRODUCTS_OF_DELTAS;
import static com.google.common.math.StatsTesting.ONE_VALUE;
import static com.google.common.math.StatsTesting.OTHER_MANY_VALUES;
import static com.google.common.math.StatsTesting.OTHER_MANY_VALUES_COUNT;
import static com.google.common.math.StatsTesting.OTHER_ONE_VALUE;
import static com.google.common.math.StatsTesting.OTHER_TWO_VALUES;
import static com.google.common.math.StatsTesting.TWO_VALUES;
import static com.google.common.math.StatsTesting.TWO_VALUES_SUM_OF_PRODUCTS_OF_DELTAS;
import static com.google.common.math.StatsTesting.createFilledPairedStatsAccumulator;
import static com.google.common.math.StatsTesting.createPartitionedFilledPairedStatsAccumulator;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static org.junit.Assert.assertThrows;

import com.google.common.math.StatsTesting.ManyValues;
import java.util.Collections;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link PairedStatsAccumulator#populationCovariance()}.
 */
@RunWith(JUnit4.class)
public class PairedStatsAccumulatorTestTest5 {

  @Test
  public void populationCovariance_emptyAccumulator_throwsIllegalStateException() {
    PairedStatsAccumulator emptyAccumulator = new PairedStatsAccumulator();
    assertThrows(IllegalStateException.class, emptyAccumulator::populationCovariance);

    // Also test an accumulator that has had an empty PairedStats added to it
    PairedStatsAccumulator accumulatorFromEmpty = new PairedStatsAccumulator();
    accumulatorFromEmpty.addAll(emptyAccumulator.snapshot());
    assertThrows(IllegalStateException.class, accumulatorFromEmpty::populationCovariance);
  }

  @Test
  public void populationCovariance_singlePair_isZero() {
    PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
    accumulator.add(ONE_VALUE, OTHER_ONE_VALUE);
    assertThat(accumulator.populationCovariance()).isEqualTo(0.0);

    // Adding an empty snapshot should not change the result
    accumulator.addAll(new PairedStatsAccumulator().snapshot());
    assertThat(accumulator.populationCovariance()).isEqualTo(0.0);
  }

  @Test
  public void populationCovariance_twoPairs_isCorrect() {
    double expectedCovariance = TWO_VALUES_SUM_OF_PRODUCTS_OF_DELTAS / 2.0;

    // Test with accumulator built by adding pairs one by one
    PairedStatsAccumulator accumulatorByAdd =
        createFilledPairedStatsAccumulator(TWO_VALUES, OTHER_TWO_VALUES);
    assertThat(accumulatorByAdd.populationCovariance())
        .isWithin(ALLOWED_ERROR)
        .of(expectedCovariance);

    // Test with accumulator built by adding stats of partitions
    PairedStatsAccumulator accumulatorByAddAll =
        createPartitionedFilledPairedStatsAccumulator(TWO_VALUES, OTHER_TWO_VALUES, 1);
    assertThat(accumulatorByAddAll.populationCovariance())
        .isWithin(ALLOWED_ERROR)
        .of(expectedCovariance);
  }

  @Test
  public void populationCovariance_manyPairs_isCorrect() {
    double expectedCovariance = MANY_VALUES_SUM_OF_PRODUCTS_OF_DELTAS / MANY_VALUES_COUNT;

    PairedStatsAccumulator accumulatorByAdd =
        createFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES);
    assertThat(accumulatorByAdd.populationCovariance())
        .isWithin(ALLOWED_ERROR)
        .of(expectedCovariance);

    PairedStatsAccumulator accumulatorByAddAll =
        createPartitionedFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES, 2);
    assertThat(accumulatorByAddAll.populationCovariance())
        .isWithin(ALLOWED_ERROR)
        .of(expectedCovariance);
  }

  @Test
  public void populationCovariance_withNonFiniteValues_isNan() {
    // Test that for any dataset containing non-finite values, the covariance is NaN.
    ALL_MANY_VALUES.stream()
        .filter(ManyValues::hasAnyNonFinite)
        .forEach(
            values -> {
              PairedStatsAccumulator accumulatorByAdd =
                  createFilledPairedStatsAccumulator(values.asIterable(), OTHER_MANY_VALUES);
              PairedStatsAccumulator accumulatorByAddAll =
                  createPartitionedFilledPairedStatsAccumulator(
                      values.asIterable(), OTHER_MANY_VALUES, 2);

              assertWithMessage("population covariance of " + values)
                  .that(accumulatorByAdd.populationCovariance())
                  .isNaN();
              assertWithMessage("population covariance by addAll of " + values)
                  .that(accumulatorByAddAll.populationCovariance())
                  .isNaN();
            });
  }

  @Test
  public void populationCovariance_withOnlyFiniteValues_isCorrect() {
    // Test that for various datasets containing only finite values, the covariance is correct.
    double expectedCovariance = MANY_VALUES_SUM_OF_PRODUCTS_OF_DELTAS / MANY_VALUES_COUNT;
    ALL_MANY_VALUES.stream()
        .filter(values -> !values.hasAnyNonFinite())
        .forEach(
            values -> {
              PairedStatsAccumulator accumulatorByAdd =
                  createFilledPairedStatsAccumulator(values.asIterable(), OTHER_MANY_VALUES);
              PairedStatsAccumulator accumulatorByAddAll =
                  createPartitionedFilledPairedStatsAccumulator(
                      values.asIterable(), OTHER_MANY_VALUES, 2);

              assertWithMessage("population covariance of " + values)
                  .that(accumulatorByAdd.populationCovariance())
                  .isWithin(ALLOWED_ERROR)
                  .of(expectedCovariance);
              assertWithMessage("population covariance by addAll of " + values)
                  .that(accumulatorByAddAll.populationCovariance())
                  .isWithin(ALLOWED_ERROR)
                  .of(expectedCovariance);
            });
  }

  @Test
  public void populationCovariance_withConstantCoordinate_isZero() {
    // Horizontal data (y is constant), so covariance should be zero.
    PairedStatsAccumulator horizontalByAdd =
        createFilledPairedStatsAccumulator(
            MANY_VALUES, Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE));
    assertThat(horizontalByAdd.populationCovariance()).isWithin(ALLOWED_ERROR).of(0.0);
    PairedStatsAccumulator horizontalByAddAll =
        createPartitionedFilledPairedStatsAccumulator(
            MANY_VALUES, Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE), 2);
    assertThat(horizontalByAddAll.populationCovariance()).isWithin(ALLOWED_ERROR).of(0.0);

    // Vertical data (x is constant), so covariance should be zero.
    PairedStatsAccumulator verticalByAdd =
        createFilledPairedStatsAccumulator(
            Collections.nCopies(OTHER_MANY_VALUES_COUNT, ONE_VALUE), OTHER_MANY_VALUES);
    assertThat(verticalByAdd.populationCovariance()).isWithin(ALLOWED_ERROR).of(0.0);
    PairedStatsAccumulator verticalByAddAll =
        createPartitionedFilledPairedStatsAccumulator(
            Collections.nCopies(OTHER_MANY_VALUES_COUNT, ONE_VALUE), OTHER_MANY_VALUES, 2);
    assertThat(verticalByAddAll.populationCovariance()).isWithin(ALLOWED_ERROR).of(0.0);

    // Constant data (both x and y are constant), so covariance should be zero.
    PairedStatsAccumulator constantByAdd =
        createFilledPairedStatsAccumulator(
            Collections.nCopies(MANY_VALUES_COUNT, ONE_VALUE),
            Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE));
    assertThat(constantByAdd.populationCovariance()).isWithin(ALLOWED_ERROR).of(0.0);
    PairedStatsAccumulator constantByAddAll =
        createPartitionedFilledPairedStatsAccumulator(
            Collections.nCopies(MANY_VALUES_COUNT, ONE_VALUE),
            Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE),
            2);
    assertThat(constantByAddAll.populationCovariance()).isWithin(ALLOWED_ERROR).of(0.0);
  }
}