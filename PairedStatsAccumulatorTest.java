/*
 * Copyright (C) 2012 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.math;

import static com.google.common.math.StatsTesting.ALLOWED_ERROR;
import static com.google.common.math.StatsTesting.ALL_MANY_VALUES;
import static com.google.common.math.StatsTesting.EMPTY_STATS_ITERABLE;
import static com.google.common.math.StatsTesting.MANY_VALUES;
import static com.google.common.math.StatsTesting.MANY_VALUES_COUNT;
import static com.google.common.math.StatsTesting.MANY_VALUES_STATS_ITERABLE;
import static com.google.common.math.StatsTesting.MANY_VALUES_SUM_OF_PRODUCTS_OF_DELTAS;
import static com.google.common.math.StatsTesting.ONE_VALUE;
import static com.google.common.math.StatsTesting.ONE_VALUE_STATS;
import static com.google.common.math.StatsTesting.OTHER_MANY_VALUES;
import static com.google.common.math.StatsTesting.OTHER_MANY_VALUES_COUNT;
import static com.google.common.math.StatsTesting.OTHER_MANY_VALUES_STATS;
import static com.google.common.math.StatsTesting.OTHER_ONE_VALUE;
import static com.google.common.math.StatsTesting.OTHER_ONE_VALUE_STATS;
import static com.google.common.math.StatsTesting.OTHER_TWO_VALUES;
import static com.google.common.math.StatsTesting.OTHER_TWO_VALUES_STATS;
import static com.google.common.math.StatsTesting.TWO_VALUES;
import static com.google.common.math.StatsTesting.TWO_VALUES_STATS;
import static com.google.common.math.StatsTesting.TWO_VALUES_SUM_OF_PRODUCTS_OF_DELTAS;
import static com.google.common.math.StatsTesting.assertDiagonalLinearTransformation;
import static com.google.common.math.StatsTesting.assertHorizontalLinearTransformation;
import static com.google.common.math.StatsTesting.assertLinearTransformationNaN;
import static com.google.common.math.StatsTesting.assertStatsApproxEqual;
import static com.google.common.math.StatsTesting.assertVerticalLinearTransformation;
import static com.google.common.math.StatsTesting.createFilledPairedStatsAccumulator;
import static com.google.common.math.StatsTesting.createPartitionedFilledPairedStatsAccumulator;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static org.junit.Assert.assertThrows;

import com.google.common.math.StatsTesting.ManyValues;
import java.util.Collections;
import junit.framework.TestCase;
import org.jspecify.annotations.NullUnmarked;

@NullUnmarked
public class PairedStatsAccumulatorTest extends TestCase {

  private PairedStatsAccumulator emptyAccumulator;
  private PairedStatsAccumulator emptyAccumulatorByAddAllEmptyPairedStats;
  private PairedStatsAccumulator oneValueAccumulator;
  private PairedStatsAccumulator oneValueAccumulatorByAddAllEmptyPairedStats;
  private PairedStatsAccumulator twoValuesAccumulator;
  private PairedStatsAccumulator twoValuesAccumulatorByAddAllPartitionedPairedStats;
  private PairedStatsAccumulator manyValuesAccumulator;
  private PairedStatsAccumulator manyValuesAccumulatorByAddAllPartitionedPairedStats;
  private PairedStatsAccumulator horizontalValuesAccumulator;
  private PairedStatsAccumulator horizontalValuesAccumulatorByAddAllPartitionedPairedStats;
  private PairedStatsAccumulator verticalValuesAccumulator;
  private PairedStatsAccumulator verticalValuesAccumulatorByAddAllPartitionedPairedStats;
  private PairedStatsAccumulator constantValuesAccumulator;
  private PairedStatsAccumulator constantValuesAccumulatorByAddAllPartitionedPairedStats;

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    emptyAccumulator = new PairedStatsAccumulator();

    emptyAccumulatorByAddAllEmptyPairedStats = new PairedStatsAccumulator();
    emptyAccumulatorByAddAllEmptyPairedStats.addAll(emptyAccumulator.snapshot());

    oneValueAccumulator = new PairedStatsAccumulator();
    oneValueAccumulator.add(ONE_VALUE, OTHER_ONE_VALUE);

    oneValueAccumulatorByAddAllEmptyPairedStats = new PairedStatsAccumulator();
    oneValueAccumulatorByAddAllEmptyPairedStats.add(ONE_VALUE, OTHER_ONE_VALUE);
    oneValueAccumulatorByAddAllEmptyPairedStats.addAll(emptyAccumulator.snapshot());

    twoValuesAccumulator = createFilledPairedStatsAccumulator(TWO_VALUES, OTHER_TWO_VALUES);
    twoValuesAccumulatorByAddAllPartitionedPairedStats =
        createPartitionedFilledPairedStatsAccumulator(TWO_VALUES, OTHER_TWO_VALUES, 1);

    manyValuesAccumulator = createFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES);
    manyValuesAccumulatorByAddAllPartitionedPairedStats =
        createPartitionedFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES, 2);

    horizontalValuesAccumulator =
        createFilledPairedStatsAccumulator(
            MANY_VALUES, Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE));
    horizontalValuesAccumulatorByAddAllPartitionedPairedStats =
        createPartitionedFilledPairedStatsAccumulator(
            MANY_VALUES, Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE), 2);

    verticalValuesAccumulator =
        createFilledPairedStatsAccumulator(
            Collections.nCopies(OTHER_MANY_VALUES_COUNT, ONE_VALUE), OTHER_MANY_VALUES);
    verticalValuesAccumulatorByAddAllPartitionedPairedStats =
        createPartitionedFilledPairedStatsAccumulator(
            Collections.nCopies(OTHER_MANY_VALUES_COUNT, ONE_VALUE), OTHER_MANY_VALUES, 2);

    constantValuesAccumulator =
        createFilledPairedStatsAccumulator(
            Collections.nCopies(MANY_VALUES_COUNT, ONE_VALUE),
            Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE));
    constantValuesAccumulatorByAddAllPartitionedPairedStats =
        createPartitionedFilledPairedStatsAccumulator(
            Collections.nCopies(MANY_VALUES_COUNT, ONE_VALUE),
            Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE),
            2);
  }

  // Helper methods for common assertions
  private void assertPopulationCovarianceFails(PairedStatsAccumulator accumulator) {
    assertThrows(IllegalStateException.class, accumulator::populationCovariance);
  }

  private void assertPopulationCovarianceEquals(
      PairedStatsAccumulator accumulator, double expected, double tolerance) {
    assertThat(accumulator.populationCovariance()).isWithin(tolerance).of(expected);
  }

  private void assertSampleCovarianceFails(PairedStatsAccumulator accumulator) {
    assertThrows(IllegalStateException.class, accumulator::sampleCovariance);
  }

  private void assertSampleCovarianceEquals(
      PairedStatsAccumulator accumulator, double expected, double tolerance) {
    assertThat(accumulator.sampleCovariance()).isWithin(tolerance).of(expected);
  }

  private void assertPearsonsCorrelationFails(PairedStatsAccumulator accumulator) {
    assertThrows(IllegalStateException.class, accumulator::pearsonsCorrelationCoefficient);
  }

  private void assertPearsonsCorrelationEquals(
      PairedStatsAccumulator accumulator, double expected, double tolerance) {
    double actual = accumulator.pearsonsCorrelationCoefficient();
    if (Double.isNaN(expected)) {
      assertThat(actual).isNaN();
    } else {
      assertThat(actual).isWithin(tolerance).of(expected);
    }
  }

  private void assertLeastSquaresFitFails(PairedStatsAccumulator accumulator) {
    assertThrows(IllegalStateException.class, accumulator::leastSquaresFit);
  }

  // Test methods
  public void testCount() {
    assertThat(emptyAccumulator.count()).isEqualTo(0);
    assertThat(emptyAccumulatorByAddAllEmptyPairedStats.count()).isEqualTo(0);
    assertThat(oneValueAccumulator.count()).isEqualTo(1);
    assertThat(oneValueAccumulatorByAddAllEmptyPairedStats.count()).isEqualTo(1);
    assertThat(twoValuesAccumulator.count()).isEqualTo(2);
    assertThat(twoValuesAccumulatorByAddAllPartitionedPairedStats.count()).isEqualTo(2);
    assertThat(manyValuesAccumulator.count()).isEqualTo(MANY_VALUES_COUNT);
    assertThat(manyValuesAccumulatorByAddAllPartitionedPairedStats.count())
        .isEqualTo(MANY_VALUES_COUNT);
  }

  public void testCountOverflow_doesNotThrow() {
    PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
    accumulator.add(ONE_VALUE, OTHER_ONE_VALUE);
    for (int power = 1; power < Long.SIZE - 1; power++) {
      accumulator.addAll(accumulator.snapshot());
    }
    accumulator.addAll(accumulator.snapshot());
    assertThat(accumulator.count()).isLessThan(0L);
  }

  public void testXStats() {
    assertStatsApproxEqual(EMPTY_STATS_ITERABLE, emptyAccumulator.xStats());
    assertStatsApproxEqual(EMPTY_STATS_ITERABLE, emptyAccumulatorByAddAllEmptyPairedStats.xStats());
    assertStatsApproxEqual(ONE_VALUE_STATS, oneValueAccumulator.xStats());
    assertStatsApproxEqual(ONE_VALUE_STATS, oneValueAccumulatorByAddAllEmptyPairedStats.xStats());
    assertStatsApproxEqual(TWO_VALUES_STATS, twoValuesAccumulator.xStats());
    assertStatsApproxEqual(
        TWO_VALUES_STATS, twoValuesAccumulatorByAddAllPartitionedPairedStats.xStats());
    assertStatsApproxEqual(MANY_VALUES_STATS_ITERABLE, manyValuesAccumulator.xStats());
    assertStatsApproxEqual(
        MANY_VALUES_STATS_ITERABLE, manyValuesAccumulatorByAddAllPartitionedPairedStats.xStats());
  }

  public void testYStats() {
    assertStatsApproxEqual(EMPTY_STATS_ITERABLE, emptyAccumulator.yStats());
    assertStatsApproxEqual(EMPTY_STATS_ITERABLE, emptyAccumulatorByAddAllEmptyPairedStats.yStats());
    assertStatsApproxEqual(OTHER_ONE_VALUE_STATS, oneValueAccumulator.yStats());
    assertStatsApproxEqual(
        OTHER_ONE_VALUE_STATS, oneValueAccumulatorByAddAllEmptyPairedStats.yStats());
    assertStatsApproxEqual(OTHER_TWO_VALUES_STATS, twoValuesAccumulator.yStats());
    assertStatsApproxEqual(
        OTHER_TWO_VALUES_STATS, twoValuesAccumulatorByAddAllPartitionedPairedStats.yStats());
    assertStatsApproxEqual(OTHER_MANY_VALUES_STATS, manyValuesAccumulator.yStats());
    assertStatsApproxEqual(
        OTHER_MANY_VALUES_STATS, manyValuesAccumulatorByAddAllPartitionedPairedStats.yStats());
  }

  // Population covariance tests
  public void testPopulationCovariance_empty() {
    assertPopulationCovarianceFails(emptyAccumulator);
    assertPopulationCovarianceFails(emptyAccumulatorByAddAllEmptyPairedStats);
  }

  public void testPopulationCovariance_oneValue() {
    assertThat(oneValueAccumulator.populationCovariance()).isEqualTo(0.0);
    assertThat(oneValueAccumulatorByAddAllEmptyPairedStats.populationCovariance()).isEqualTo(0.0);
  }

  public void testPopulationCovariance_twoValues() {
    double expected = TWO_VALUES_SUM_OF_PRODUCTS_OF_DELTAS / 2;
    assertPopulationCovarianceEquals(twoValuesAccumulator, expected, ALLOWED_ERROR);
    assertPopulationCovarianceEquals(
        twoValuesAccumulatorByAddAllPartitionedPairedStats, expected, ALLOWED_ERROR);
  }

  public void testPopulationCovariance_manyValues() {
    double expected = MANY_VALUES_SUM_OF_PRODUCTS_OF_DELTAS / MANY_VALUES_COUNT;
    assertPopulationCovarianceEquals(manyValuesAccumulator, expected, ALLOWED_ERROR);
    assertPopulationCovarianceEquals(
        manyValuesAccumulatorByAddAllPartitionedPairedStats, expected, ALLOWED_ERROR);
  }

  public void testPopulationCovariance_horizontal() {
    assertPopulationCovarianceEquals(horizontalValuesAccumulator, 0.0, ALLOWED_ERROR);
    assertPopulationCovarianceEquals(
        horizontalValuesAccumulatorByAddAllPartitionedPairedStats, 0.0, ALLOWED_ERROR);
  }

  public void testPopulationCovariance_vertical() {
    assertPopulationCovarianceEquals(verticalValuesAccumulator, 0.0, ALLOWED_ERROR);
    assertPopulationCovarianceEquals(
        verticalValuesAccumulatorByAddAllPartitionedPairedStats, 0.0, ALLOWED_ERROR);
  }

  public void testPopulationCovariance_constant() {
    assertPopulationCovarianceEquals(constantValuesAccumulator, 0.0, ALLOWED_ERROR);
    assertPopulationCovarianceEquals(
        constantValuesAccumulatorByAddAllPartitionedPairedStats, 0.0, ALLOWED_ERROR);
  }

  public void testPopulationCovariance_manyValuesWithNonFiniteX() {
    for (ManyValues values : ALL_MANY_VALUES) {
      PairedStatsAccumulator accumulator =
          createFilledPairedStatsAccumulator(values.asIterable(), OTHER_MANY_VALUES);
      PairedStatsAccumulator accumulatorByAddAll =
          createPartitionedFilledPairedStatsAccumulator(values.asIterable(), OTHER_MANY_VALUES, 2);

      if (values.hasAnyNonFinite()) {
        assertWithMessage("population covariance of " + values)
            .that(accumulator.populationCovariance())
            .isNaN();
        assertWithMessage("population covariance by addAll(PairedStats) of " + values)
            .that(accumulatorByAddAll.populationCovariance())
            .isNaN();
      } else {
        double expected = MANY_VALUES_SUM_OF_PRODUCTS_OF_DELTAS / MANY_VALUES_COUNT;
        assertWithMessage("population covariance of " + values)
            .that(accumulator.populationCovariance())
            .isWithin(ALLOWED_ERROR)
            .of(expected);
        assertWithMessage("population covariance by addAll(PairedStats) of " + values)
            .that(accumulatorByAddAll.populationCovariance())
            .isWithin(ALLOWED_ERROR)
            .of(expected);
      }
    }
  }

  // Sample covariance tests
  public void testSampleCovariance_empty() {
    assertSampleCovarianceFails(emptyAccumulator);
    assertSampleCovarianceFails(emptyAccumulatorByAddAllEmptyPairedStats);
  }

  public void testSampleCovariance_oneValue() {
    assertSampleCovarianceFails(oneValueAccumulator);
    assertSampleCovarianceFails(oneValueAccumulatorByAddAllEmptyPairedStats);
  }

  public void testSampleCovariance_twoValues() {
    double expected = TWO_VALUES_SUM_OF_PRODUCTS_OF_DELTAS;
    assertSampleCovarianceEquals(twoValuesAccumulator, expected, ALLOWED_ERROR);
    assertSampleCovarianceEquals(
        twoValuesAccumulatorByAddAllPartitionedPairedStats, expected, ALLOWED_ERROR);
  }

  public void testSampleCovariance_manyValues() {
    double expected = MANY_VALUES_SUM_OF_PRODUCTS_OF_DELTAS / (MANY_VALUES_COUNT - 1);
    assertSampleCovarianceEquals(manyValuesAccumulator, expected, ALLOWED_ERROR);
    assertSampleCovarianceEquals(
        manyValuesAccumulatorByAddAllPartitionedPairedStats, expected, ALLOWED_ERROR);
  }

  public void testSampleCovariance_horizontal() {
    assertSampleCovarianceEquals(horizontalValuesAccumulator, 0.0, ALLOWED_ERROR);
    assertSampleCovarianceEquals(
        horizontalValuesAccumulatorByAddAllPartitionedPairedStats, 0.0, ALLOWED_ERROR);
  }

  public void testSampleCovariance_vertical() {
    assertSampleCovarianceEquals(verticalValuesAccumulator, 0.0, ALLOWED_ERROR);
    assertSampleCovarianceEquals(
        verticalValuesAccumulatorByAddAllPartitionedPairedStats, 0.0, ALLOWED_ERROR);
  }

  public void testSampleCovariance_constant() {
    assertSampleCovarianceEquals(constantValuesAccumulator, 0.0, ALLOWED_ERROR);
    assertSampleCovarianceEquals(
        constantValuesAccumulatorByAddAllPartitionedPairedStats, 0.0, ALLOWED_ERROR);
  }

  // Pearson's correlation tests
  public void testPearsonsCorrelationCoefficient_empty() {
    assertPearsonsCorrelationFails(emptyAccumulator);
    assertPearsonsCorrelationFails(emptyAccumulatorByAddAllEmptyPairedStats);
  }

  public void testPearsonsCorrelationCoefficient_oneValue() {
    assertPearsonsCorrelationFails(oneValueAccumulator);
    assertPearsonsCorrelationFails(oneValueAccumulatorByAddAllEmptyPairedStats);
  }

  public void testPearsonsCorrelationCoefficient_twoValues() {
    double expected =
        twoValuesAccumulator.populationCovariance()
            / (twoValuesAccumulator.xStats().populationStandardDeviation()
                * twoValuesAccumulator.yStats().populationStandardDeviation());
    assertPearsonsCorrelationEquals(twoValuesAccumulator, expected, ALLOWED_ERROR);
  }

  public void testPearsonsCorrelationCoefficient_manyValues() {
    double expected =
        manyValuesAccumulator.populationCovariance()
            / (manyValuesAccumulator.xStats().populationStandardDeviation()
                * manyValuesAccumulator.yStats().populationStandardDeviation());
    assertPearsonsCorrelationEquals(manyValuesAccumulator, expected, ALLOWED_ERROR);
    assertPearsonsCorrelationEquals(
        manyValuesAccumulatorByAddAllPartitionedPairedStats, expected, ALLOWED_ERROR);
  }

  public void testPearsonsCorrelationCoefficient_manyValuesWithNonFiniteY() {
    for (ManyValues values : ALL_MANY_VALUES) {
      PairedStatsAccumulator accumulator =
          createFilledPairedStatsAccumulator(MANY_VALUES, values.asIterable());
      PairedStatsAccumulator accumulatorByAddAll =
          createPartitionedFilledPairedStatsAccumulator(MANY_VALUES, values.asIterable(), 2);

      if (values.hasAnyNonFinite()) {
        assertWithMessage("Pearson's correlation coefficient of " + values)
            .that(accumulator.pearsonsCorrelationCoefficient())
            .isNaN();
        assertWithMessage("Pearson's correlation coefficient by addAll(PairedStats) of " + values)
            .that(accumulatorByAddAll.pearsonsCorrelationCoefficient())
            .isNaN();
      } else {
        double expected =
            accumulator.populationCovariance()
                / (accumulator.xStats().populationStandardDeviation()
                    * accumulator.yStats().populationStandardDeviation());
        assertWithMessage("Pearson's correlation coefficient of " + values)
            .that(accumulator.pearsonsCorrelationCoefficient())
            .isWithin(ALLOWED_ERROR)
            .of(expected);
        assertWithMessage("Pearson's correlation coefficient by addAll(PairedStats) of " + values)
            .that(accumulatorByAddAll.pearsonsCorrelationCoefficient())
            .isWithin(ALLOWED_ERROR)
            .of(expected);
      }
    }
  }

  public void testPearsonsCorrelationCoefficient_horizontal() {
    assertPearsonsCorrelationFails(horizontalValuesAccumulator);
    assertPearsonsCorrelationFails(
        horizontalValuesAccumulatorByAddAllPartitionedPairedStats);
  }

  public void testPearsonsCorrelationCoefficient_vertical() {
    assertPearsonsCorrelationFails(verticalValuesAccumulator);
    assertPearsonsCorrelationFails(
        verticalValuesAccumulatorByAddAllPartitionedPairedStats);
  }

  public void testPearsonsCorrelationCoefficient_constant() {
    assertPearsonsCorrelationFails(constantValuesAccumulator);
    assertPearsonsCorrelationFails(
        constantValuesAccumulatorByAddAllPartitionedPairedStats);
  }

  // Least squares fit tests
  public void testLeastSquaresFit_empty() {
    assertLeastSquaresFitFails(emptyAccumulator);
    assertLeastSquaresFitFails(emptyAccumulatorByAddAllEmptyPairedStats);
  }

  public void testLeastSquaresFit_oneValue() {
    assertLeastSquaresFitFails(oneValueAccumulator);
    assertLeastSquaresFitFails(oneValueAccumulatorByAddAllEmptyPairedStats);
  }

  public void testLeastSquaresFit_twoValues() {
    assertDiagonalLinearTransformation(
        twoValuesAccumulator.leastSquaresFit(),
        twoValuesAccumulator.xStats().mean(),
        twoValuesAccumulator.yStats().mean(),
        twoValuesAccumulator.xStats().populationVariance(),
        twoValuesAccumulator.populationCovariance());
    assertDiagonalLinearTransformation(
        twoValuesAccumulatorByAddAllPartitionedPairedStats.leastSquaresFit(),
        twoValuesAccumulatorByAddAllPartitionedPairedStats.xStats().mean(),
        twoValuesAccumulatorByAddAllPartitionedPairedStats.yStats().mean(),
        twoValuesAccumulatorByAddAllPartitionedPairedStats.xStats().populationVariance(),
        twoValuesAccumulatorByAddAllPartitionedPairedStats.populationCovariance());
  }

  public void testLeastSquaresFit_manyValues() {
    assertDiagonalLinearTransformation(
        manyValuesAccumulator.leastSquaresFit(),
        manyValuesAccumulator.xStats().mean(),
        manyValuesAccumulator.yStats().mean(),
        manyValuesAccumulator.xStats().populationVariance(),
        manyValuesAccumulator.populationCovariance());
    assertDiagonalLinearTransformation(
        manyValuesAccumulatorByAddAllPartitionedPairedStats.leastSquaresFit(),
        manyValuesAccumulatorByAddAllPartitionedPairedStats.xStats().mean(),
        manyValuesAccumulatorByAddAllPartitionedPairedStats.yStats().mean(),
        manyValuesAccumulatorByAddAllPartitionedPairedStats.xStats().populationVariance(),
        manyValuesAccumulatorByAddAllPartitionedPairedStats.populationCovariance());
  }

  public void testLeastSquaresFit_manyValuesWithNonFiniteX() {
    for (ManyValues values : ALL_MANY_VALUES) {
      PairedStatsAccumulator accumulator =
          createFilledPairedStatsAccumulator(values.asIterable(), OTHER_MANY_VALUES);
      PairedStatsAccumulator accumulatorByAddAll =
          createPartitionedFilledPairedStatsAccumulator(values.asIterable(), OTHER_MANY_VALUES, 2);

      LinearTransformation fit = accumulator.leastSquaresFit();
      LinearTransformation fitByAddAll = accumulatorByAddAll.leastSquaresFit();

      if (values.hasAnyNonFinite()) {
        assertLinearTransformationNaN(fit);
        assertLinearTransformationNaN(fitByAddAll);
      } else {
        assertDiagonalLinearTransformation(
            fit,
            accumulator.xStats().mean(),
            accumulator.yStats().mean(),
            accumulator.xStats().populationVariance(),
            accumulator.populationCovariance());
        assertDiagonalLinearTransformation(
            fitByAddAll,
            accumulatorByAddAll.xStats().mean(),
            accumulatorByAddAll.yStats().mean(),
            accumulatorByAddAll.xStats().populationVariance(),
            accumulatorByAddAll.populationCovariance());
      }
    }
  }

  public void testLeastSquaresFit_horizontal() {
    assertHorizontalLinearTransformation(
        horizontalValuesAccumulator.leastSquaresFit(), horizontalValuesAccumulator.yStats().mean());
    assertHorizontalLinearTransformation(
        horizontalValuesAccumulatorByAddAllPartitionedPairedStats.leastSquaresFit(),
        horizontalValuesAccumulatorByAddAllPartitionedPairedStats.yStats().mean());
  }

  public void testLeastSquaresFit_vertical() {
    assertVerticalLinearTransformation(
        verticalValuesAccumulator.leastSquaresFit(), verticalValuesAccumulator.xStats().mean());
    assertVerticalLinearTransformation(
        verticalValuesAccumulatorByAddAllPartitionedPairedStats.leastSquaresFit(),
        verticalValuesAccumulatorByAddAllPartitionedPairedStats.xStats().mean());
  }

  public void testLeastSquaresFit_constant() {
    assertLeastSquaresFitFails(constantValuesAccumulator);
    assertLeastSquaresFitFails(constantValuesAccumulatorByAddAllPartitionedPairedStats);
  }
}