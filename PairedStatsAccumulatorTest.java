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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.jspecify.annotations.NullUnmarked;

/**
 * Tests for {@link PairedStatsAccumulator}.
 *
 * <p>This test suite is structured to verify that a {@link PairedStatsAccumulator} behaves
 * identically whether it is populated by adding individual data points one by one (via {@link
 * PairedStatsAccumulator#add}) or by merging a {@link PairedStats} object (via {@link
 * PairedStatsAccumulator#addAll}). The helper methods {@code createFilledPairedStatsAccumulator}
 * and {@code createPartitionedFilledPairedStatsAccumulator} are used to create these two types of
 * accumulators for each test scenario.
 */
@NullUnmarked
@RunWith(JUnit4.class)
public class PairedStatsAccumulatorTest {

  // -------------------------- count --------------------------

  @Test
  public void count_whenEmpty_isZero() {
    PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
    assertThat(accumulator.count()).isEqualTo(0);
  }

  @Test
  public void count_withOneValue_isOne() {
    PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
    accumulator.add(ONE_VALUE, OTHER_ONE_VALUE);
    assertThat(accumulator.count()).isEqualTo(1);
  }

  @Test
  public void count_withMultipleValues_isCorrect() {
    PairedStatsAccumulator accumulatorDirectAdd =
        createFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES);
    PairedStatsAccumulator accumulatorPartitionedAddAll =
        createPartitionedFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES, 2);

    assertThat(accumulatorDirectAdd.count()).isEqualTo(MANY_VALUES_COUNT);
    assertThat(accumulatorPartitionedAddAll.count()).isEqualTo(MANY_VALUES_COUNT);
  }

  @Test
  public void count_whenOverflows_wrapsAround() {
    PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
    accumulator.add(ONE_VALUE, OTHER_ONE_VALUE);
    for (int power = 1; power < Long.SIZE - 1; power++) {
      accumulator.addAll(accumulator.snapshot());
    }
    // Should overflow without throwing.
    accumulator.addAll(accumulator.snapshot());
    assertThat(accumulator.count()).isLessThan(0L);
  }

  // -------------------------- xStats and yStats --------------------------

  @Test
  public void stats_whenEmpty_areEmpty() {
    PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
    assertStatsApproxEqual(EMPTY_STATS_ITERABLE, accumulator.xStats());
    assertStatsApproxEqual(EMPTY_STATS_ITERABLE, accumulator.yStats());
  }

  @Test
  public void stats_withOneValue_areCorrect() {
    PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
    accumulator.add(ONE_VALUE, OTHER_ONE_VALUE);
    assertStatsApproxEqual(ONE_VALUE_STATS, accumulator.xStats());
    assertStatsApproxEqual(OTHER_ONE_VALUE_STATS, accumulator.yStats());
  }

  @Test
  public void stats_withTwoValues_areCorrect() {
    PairedStatsAccumulator accumulatorDirectAdd =
        createFilledPairedStatsAccumulator(TWO_VALUES, OTHER_TWO_VALUES);
    PairedStatsAccumulator accumulatorPartitionedAddAll =
        createPartitionedFilledPairedStatsAccumulator(TWO_VALUES, OTHER_TWO_VALUES, 1);

    assertStatsApproxEqual(TWO_VALUES_STATS, accumulatorDirectAdd.xStats());
    assertStatsApproxEqual(OTHER_TWO_VALUES_STATS, accumulatorDirectAdd.yStats());
    assertStatsApproxEqual(TWO_VALUES_STATS, accumulatorPartitionedAddAll.xStats());
    assertStatsApproxEqual(OTHER_TWO_VALUES_STATS, accumulatorPartitionedAddAll.yStats());
  }

  @Test
  public void stats_withManyValues_areCorrect() {
    PairedStatsAccumulator accumulatorDirectAdd =
        createFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES);
    PairedStatsAccumulator accumulatorPartitionedAddAll =
        createPartitionedFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES, 2);

    assertStatsApproxEqual(MANY_VALUES_STATS_ITERABLE, accumulatorDirectAdd.xStats());
    assertStatsApproxEqual(OTHER_MANY_VALUES_STATS, accumulatorDirectAdd.yStats());
    assertStatsApproxEqual(MANY_VALUES_STATS_ITERABLE, accumulatorPartitionedAddAll.xStats());
    assertStatsApproxEqual(OTHER_MANY_VALUES_STATS, accumulatorPartitionedAddAll.yStats());
  }

  // -------------------------- populationCovariance --------------------------

  @Test
  public void populationCovariance_whenEmpty_throwsIllegalStateException() {
    PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
    assertThrows(IllegalStateException.class, accumulator::populationCovariance);
  }

  @Test
  public void populationCovariance_withOneValue_isZero() {
    PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
    accumulator.add(ONE_VALUE, OTHER_ONE_VALUE);
    assertThat(accumulator.populationCovariance()).isEqualTo(0.0);
  }

  @Test
  public void populationCovariance_withTwoValues_isCorrect() {
    PairedStatsAccumulator accumulatorDirectAdd =
        createFilledPairedStatsAccumulator(TWO_VALUES, OTHER_TWO_VALUES);
    PairedStatsAccumulator accumulatorPartitionedAddAll =
        createPartitionedFilledPairedStatsAccumulator(TWO_VALUES, OTHER_TWO_VALUES, 1);

    double expected = TWO_VALUES_SUM_OF_PRODUCTS_OF_DELTAS / 2;
    assertThat(accumulatorDirectAdd.populationCovariance()).isWithin(ALLOWED_ERROR).of(expected);
    assertThat(accumulatorPartitionedAddAll.populationCovariance())
        .isWithin(ALLOWED_ERROR)
        .of(expected);
  }

  @Test
  public void populationCovariance_withManyValues_isCorrect() {
    PairedStatsAccumulator accumulatorDirectAdd =
        createFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES);
    PairedStatsAccumulator accumulatorPartitionedAddAll =
        createPartitionedFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES, 2);

    double expected = MANY_VALUES_SUM_OF_PRODUCTS_OF_DELTAS / MANY_VALUES_COUNT;
    assertThat(accumulatorDirectAdd.populationCovariance()).isWithin(ALLOWED_ERROR).of(expected);
    assertThat(accumulatorPartitionedAddAll.populationCovariance())
        .isWithin(ALLOWED_ERROR)
        .of(expected);
  }

  @Test
  public void populationCovariance_withNonFiniteValues_isNan() {
    for (ManyValues values : ALL_MANY_VALUES) {
      if (!values.hasAnyNonFinite()) {
        continue;
      }
      PairedStatsAccumulator accumulator =
          createFilledPairedStatsAccumulator(values.asIterable(), OTHER_MANY_VALUES);
      assertWithMessage("population covariance of " + values)
          .that(accumulator.populationCovariance())
          .isNaN();
    }
  }

  @Test
  public void populationCovariance_withConstantValues_isZero() {
    PairedStatsAccumulator accumulatorDirectAdd =
        createFilledPairedStatsAccumulator(
            Collections.nCopies(MANY_VALUES_COUNT, ONE_VALUE),
            Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE));
    PairedStatsAccumulator accumulatorPartitionedAddAll =
        createPartitionedFilledPairedStatsAccumulator(
            Collections.nCopies(MANY_VALUES_COUNT, ONE_VALUE),
            Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE),
            2);

    assertThat(accumulatorDirectAdd.populationCovariance()).isWithin(ALLOWED_ERROR).of(0.0);
    assertThat(accumulatorPartitionedAddAll.populationCovariance()).isWithin(ALLOWED_ERROR).of(0.0);
  }

  // -------------------------- sampleCovariance --------------------------

  @Test
  public void sampleCovariance_whenEmptyOrOneValue_throwsIllegalStateException() {
    PairedStatsAccumulator emptyAccumulator = new PairedStatsAccumulator();
    assertThrows(IllegalStateException.class, emptyAccumulator::sampleCovariance);

    PairedStatsAccumulator oneValueAccumulator = new PairedStatsAccumulator();
    oneValueAccumulator.add(ONE_VALUE, OTHER_ONE_VALUE);
    assertThrows(IllegalStateException.class, oneValueAccumulator::sampleCovariance);
  }

  @Test
  public void sampleCovariance_withTwoValues_isCorrect() {
    PairedStatsAccumulator accumulatorDirectAdd =
        createFilledPairedStatsAccumulator(TWO_VALUES, OTHER_TWO_VALUES);
    PairedStatsAccumulator accumulatorPartitionedAddAll =
        createPartitionedFilledPairedStatsAccumulator(TWO_VALUES, OTHER_TWO_VALUES, 1);

    assertThat(accumulatorDirectAdd.sampleCovariance())
        .isWithin(ALLOWED_ERROR)
        .of(TWO_VALUES_SUM_OF_PRODUCTS_OF_DELTAS);
    assertThat(accumulatorPartitionedAddAll.sampleCovariance())
        .isWithin(ALLOWED_ERROR)
        .of(TWO_VALUES_SUM_OF_PRODUCTS_OF_DELTAS);
  }

  @Test
  public void sampleCovariance_withManyValues_isCorrect() {
    PairedStatsAccumulator accumulatorDirectAdd =
        createFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES);
    PairedStatsAccumulator accumulatorPartitionedAddAll =
        createPartitionedFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES, 2);

    double expected = MANY_VALUES_SUM_OF_PRODUCTS_OF_DELTAS / (MANY_VALUES_COUNT - 1);
    assertThat(accumulatorDirectAdd.sampleCovariance()).isWithin(ALLOWED_ERROR).of(expected);
    assertThat(accumulatorPartitionedAddAll.sampleCovariance()).isWithin(ALLOWED_ERROR).of(expected);
  }

  // -------------------------- pearsonsCorrelationCoefficient --------------------------

  @Test
  public void pearsonsCorrelationCoefficient_whenEmptyOrOneValue_throwsIllegalStateException() {
    PairedStatsAccumulator emptyAccumulator = new PairedStatsAccumulator();
    assertThrows(IllegalStateException.class, emptyAccumulator::pearsonsCorrelationCoefficient);

    PairedStatsAccumulator oneValueAccumulator = new PairedStatsAccumulator();
    oneValueAccumulator.add(ONE_VALUE, OTHER_ONE_VALUE);
    assertThrows(IllegalStateException.class, oneValueAccumulator::pearsonsCorrelationCoefficient);
  }

  @Test
  public void pearsonsCorrelationCoefficient_withVaryingValues_isCorrect() {
    // Arrange
    PairedStatsAccumulator accumulatorDirectAdd =
        createFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES);
    PairedStatsAccumulator accumulatorPartitionedAddAll =
        createPartitionedFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES, 2);

    // Act
    double correlationDirect = accumulatorDirectAdd.pearsonsCorrelationCoefficient();
    double correlationPartitioned = accumulatorPartitionedAddAll.pearsonsCorrelationCoefficient();

    // Assert
    double expectedDirect =
        accumulatorDirectAdd.populationCovariance()
            / (accumulatorDirectAdd.xStats().populationStandardDeviation()
                * accumulatorDirectAdd.yStats().populationStandardDeviation());
    assertThat(correlationDirect).isWithin(ALLOWED_ERROR).of(expectedDirect);

    double expectedPartitioned =
        accumulatorPartitionedAddAll.populationCovariance()
            / (accumulatorPartitionedAddAll.xStats().populationStandardDeviation()
                * accumulatorPartitionedAddAll.yStats().populationStandardDeviation());
    assertThat(correlationPartitioned).isWithin(ALLOWED_ERROR).of(expectedPartitioned);
  }

  @Test
  public void pearsonsCorrelationCoefficient_withNonFiniteValues_isNan() {
    for (ManyValues values : ALL_MANY_VALUES) {
      if (!values.hasAnyNonFinite()) {
        continue;
      }
      PairedStatsAccumulator accumulator =
          createFilledPairedStatsAccumulator(MANY_VALUES, values.asIterable());
      assertWithMessage("Pearson's correlation for " + values)
          .that(accumulator.pearsonsCorrelationCoefficient())
          .isNaN();
    }
  }

  @Test
  public void pearsonsCorrelationCoefficient_withZeroVariance_throwsIllegalStateException() {
    // Horizontal line (y-variance is zero)
    PairedStatsAccumulator horizontalAccumulator =
        createFilledPairedStatsAccumulator(
            MANY_VALUES, Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE));
    assertThrows(IllegalStateException.class, horizontalAccumulator::pearsonsCorrelationCoefficient);

    // Vertical line (x-variance is zero)
    PairedStatsAccumulator verticalAccumulator =
        createFilledPairedStatsAccumulator(
            Collections.nCopies(OTHER_MANY_VALUES_COUNT, ONE_VALUE), OTHER_MANY_VALUES);
    assertThrows(IllegalStateException.class, verticalAccumulator::pearsonsCorrelationCoefficient);

    // Constant data (both variances are zero)
    PairedStatsAccumulator constantAccumulator =
        createFilledPairedStatsAccumulator(
            Collections.nCopies(MANY_VALUES_COUNT, ONE_VALUE),
            Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE));
    assertThrows(IllegalStateException.class, constantAccumulator::pearsonsCorrelationCoefficient);
  }

  // -------------------------- leastSquaresFit --------------------------

  @Test
  public void leastSquaresFit_whenEmptyOrOneValue_throwsIllegalStateException() {
    PairedStatsAccumulator emptyAccumulator = new PairedStatsAccumulator();
    assertThrows(IllegalStateException.class, emptyAccumulator::leastSquaresFit);

    PairedStatsAccumulator oneValueAccumulator = new PairedStatsAccumulator();
    oneValueAccumulator.add(ONE_VALUE, OTHER_ONE_VALUE);
    assertThrows(IllegalStateException.class, oneValueAccumulator::leastSquaresFit);
  }

  @Test
  public void leastSquaresFit_withVaryingValues_isCorrect() {
    // Arrange: Create accumulators using both direct add and partitioned addAll methods.
    PairedStatsAccumulator accumulatorDirectAdd =
        createFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES);
    PairedStatsAccumulator accumulatorPartitionedAddAll =
        createPartitionedFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES, 2);

    // Act and Assert for direct add accumulator
    assertDiagonalLinearTransformation(
        accumulatorDirectAdd.leastSquaresFit(),
        accumulatorDirectAdd.xStats().mean(),
        accumulatorDirectAdd.yStats().mean(),
        accumulatorDirectAdd.xStats().populationVariance(),
        accumulatorDirectAdd.populationCovariance());

    // Act and Assert for partitioned addAll accumulator
    assertDiagonalLinearTransformation(
        accumulatorPartitionedAddAll.leastSquaresFit(),
        accumulatorPartitionedAddAll.xStats().mean(),
        accumulatorPartitionedAddAll.yStats().mean(),
        accumulatorPartitionedAddAll.xStats().populationVariance(),
        accumulatorPartitionedAddAll.populationCovariance());
  }

  @Test
  public void leastSquaresFit_withNonFiniteValues_isNan() {
    for (ManyValues values : ALL_MANY_VALUES) {
      if (!values.hasAnyNonFinite()) {
        continue;
      }
      PairedStatsAccumulator accumulator =
          createFilledPairedStatsAccumulator(values.asIterable(), OTHER_MANY_VALUES);
      assertLinearTransformationNaN(accumulator.leastSquaresFit());
    }
  }

  @Test
  public void leastSquaresFit_whenDataSetIsHorizontal_returnsHorizontalLine() {
    PairedStatsAccumulator accumulator =
        createFilledPairedStatsAccumulator(
            MANY_VALUES, Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE));
    assertHorizontalLinearTransformation(
        accumulator.leastSquaresFit(), accumulator.yStats().mean());
  }

  @Test
  public void leastSquaresFit_whenDataSetIsVertical_returnsVerticalLine() {
    PairedStatsAccumulator accumulator =
        createFilledPairedStatsAccumulator(
            Collections.nCopies(OTHER_MANY_VALUES_COUNT, ONE_VALUE), OTHER_MANY_VALUES);
    assertVerticalLinearTransformation(accumulator.leastSquaresFit(), accumulator.xStats().mean());
  }

  @Test
  public void leastSquaresFit_whenDataSetIsConstant_throwsIllegalStateException() {
    PairedStatsAccumulator accumulator =
        createFilledPairedStatsAccumulator(
            Collections.nCopies(MANY_VALUES_COUNT, ONE_VALUE),
            Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE));
    assertThrows(IllegalStateException.class, accumulator::leastSquaresFit);
  }
}