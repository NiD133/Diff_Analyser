package com.google.common.math;

import static com.google.common.math.StatsTesting.*;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static org.junit.Assert.assertThrows;

import com.google.common.math.StatsTesting.ManyValues;
import java.util.Collections;
import junit.framework.TestCase;
import org.jspecify.annotations.NullUnmarked;

/**
 * Unit tests for {@link PairedStatsAccumulator}.
 * This class tests the statistics methods for instances built with {@link PairedStatsAccumulator#add},
 * and various error cases of that method.
 * For tests of the {@link PairedStatsAccumulator#snapshot} method which returns {@link PairedStats} instances,
 * see {@link PairedStatsTest}.
 */
@NullUnmarked
public class PairedStatsAccumulatorTest extends TestCase {

  private PairedStatsAccumulator emptyAccumulator;
  private PairedStatsAccumulator oneValueAccumulator;
  private PairedStatsAccumulator twoValuesAccumulator;
  private PairedStatsAccumulator manyValuesAccumulator;
  private PairedStatsAccumulator horizontalValuesAccumulator;
  private PairedStatsAccumulator verticalValuesAccumulator;
  private PairedStatsAccumulator constantValuesAccumulator;

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    // Initialize accumulators for different test scenarios
    emptyAccumulator = new PairedStatsAccumulator();

    oneValueAccumulator = new PairedStatsAccumulator();
    oneValueAccumulator.add(ONE_VALUE, OTHER_ONE_VALUE);

    twoValuesAccumulator = createFilledPairedStatsAccumulator(TWO_VALUES, OTHER_TWO_VALUES);

    manyValuesAccumulator = createFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES);

    horizontalValuesAccumulator = createFilledPairedStatsAccumulator(
        MANY_VALUES, Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE));

    verticalValuesAccumulator = createFilledPairedStatsAccumulator(
        Collections.nCopies(OTHER_MANY_VALUES_COUNT, ONE_VALUE), OTHER_MANY_VALUES);

    constantValuesAccumulator = createFilledPairedStatsAccumulator(
        Collections.nCopies(MANY_VALUES_COUNT, ONE_VALUE),
        Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE));
  }

  public void testCount() {
    assertThat(emptyAccumulator.count()).isEqualTo(0);
    assertThat(oneValueAccumulator.count()).isEqualTo(1);
    assertThat(twoValuesAccumulator.count()).isEqualTo(2);
    assertThat(manyValuesAccumulator.count()).isEqualTo(MANY_VALUES_COUNT);
  }

  public void testCountOverflow_doesNotThrow() {
    PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
    accumulator.add(ONE_VALUE, OTHER_ONE_VALUE);
    for (int power = 1; power < Long.SIZE - 1; power++) {
      accumulator.addAll(accumulator.snapshot());
    }
    // Should overflow without throwing.
    accumulator.addAll(accumulator.snapshot());
    assertThat(accumulator.count()).isLessThan(0L);
  }

  public void testXStats() {
    assertStatsApproxEqual(EMPTY_STATS_ITERABLE, emptyAccumulator.xStats());
    assertStatsApproxEqual(ONE_VALUE_STATS, oneValueAccumulator.xStats());
    assertStatsApproxEqual(TWO_VALUES_STATS, twoValuesAccumulator.xStats());
    assertStatsApproxEqual(MANY_VALUES_STATS_ITERABLE, manyValuesAccumulator.xStats());
  }

  public void testYStats() {
    assertStatsApproxEqual(EMPTY_STATS_ITERABLE, emptyAccumulator.yStats());
    assertStatsApproxEqual(OTHER_ONE_VALUE_STATS, oneValueAccumulator.yStats());
    assertStatsApproxEqual(OTHER_TWO_VALUES_STATS, twoValuesAccumulator.yStats());
    assertStatsApproxEqual(OTHER_MANY_VALUES_STATS, manyValuesAccumulator.yStats());
  }

  public void testPopulationCovariance() {
    assertThrows(IllegalStateException.class, () -> emptyAccumulator.populationCovariance());
    assertThat(oneValueAccumulator.populationCovariance()).isEqualTo(0.0);
    assertThat(twoValuesAccumulator.populationCovariance())
        .isWithin(ALLOWED_ERROR)
        .of(TWO_VALUES_SUM_OF_PRODUCTS_OF_DELTAS / 2);
    assertThat(manyValuesAccumulator.populationCovariance())
        .isWithin(ALLOWED_ERROR)
        .of(MANY_VALUES_SUM_OF_PRODUCTS_OF_DELTAS / MANY_VALUES_COUNT);
    assertThat(horizontalValuesAccumulator.populationCovariance()).isWithin(ALLOWED_ERROR).of(0.0);
    assertThat(verticalValuesAccumulator.populationCovariance()).isWithin(ALLOWED_ERROR).of(0.0);
    assertThat(constantValuesAccumulator.populationCovariance()).isWithin(ALLOWED_ERROR).of(0.0);
  }

  public void testSampleCovariance() {
    assertThrows(IllegalStateException.class, () -> emptyAccumulator.sampleCovariance());
    assertThrows(IllegalStateException.class, () -> oneValueAccumulator.sampleCovariance());
    assertThat(twoValuesAccumulator.sampleCovariance())
        .isWithin(ALLOWED_ERROR)
        .of(TWO_VALUES_SUM_OF_PRODUCTS_OF_DELTAS);
    assertThat(manyValuesAccumulator.sampleCovariance())
        .isWithin(ALLOWED_ERROR)
        .of(MANY_VALUES_SUM_OF_PRODUCTS_OF_DELTAS / (MANY_VALUES_COUNT - 1));
    assertThat(horizontalValuesAccumulator.sampleCovariance()).isWithin(ALLOWED_ERROR).of(0.0);
    assertThat(verticalValuesAccumulator.sampleCovariance()).isWithin(ALLOWED_ERROR).of(0.0);
    assertThat(constantValuesAccumulator.sampleCovariance()).isWithin(ALLOWED_ERROR).of(0.0);
  }

  public void testPearsonsCorrelationCoefficient() {
    assertThrows(
        IllegalStateException.class, () -> emptyAccumulator.pearsonsCorrelationCoefficient());
    assertThrows(
        IllegalStateException.class, () -> oneValueAccumulator.pearsonsCorrelationCoefficient());
    assertThat(twoValuesAccumulator.pearsonsCorrelationCoefficient())
        .isWithin(ALLOWED_ERROR)
        .of(
            twoValuesAccumulator.populationCovariance()
                / (twoValuesAccumulator.xStats().populationStandardDeviation()
                    * twoValuesAccumulator.yStats().populationStandardDeviation()));
    assertThat(manyValuesAccumulator.pearsonsCorrelationCoefficient())
        .isWithin(ALLOWED_ERROR)
        .of(
            manyValuesAccumulator.populationCovariance()
                / (manyValuesAccumulator.xStats().populationStandardDeviation()
                    * manyValuesAccumulator.yStats().populationStandardDeviation()));
    assertThrows(
        IllegalStateException.class,
        () -> horizontalValuesAccumulator.pearsonsCorrelationCoefficient());
    assertThrows(
        IllegalStateException.class,
        () -> verticalValuesAccumulator.pearsonsCorrelationCoefficient());
    assertThrows(
        IllegalStateException.class,
        () -> constantValuesAccumulator.pearsonsCorrelationCoefficient());
  }

  public void testLeastSquaresFit() {
    assertThrows(IllegalStateException.class, () -> emptyAccumulator.leastSquaresFit());
    assertThrows(IllegalStateException.class, () -> oneValueAccumulator.leastSquaresFit());
    assertDiagonalLinearTransformation(
        twoValuesAccumulator.leastSquaresFit(),
        twoValuesAccumulator.xStats().mean(),
        twoValuesAccumulator.yStats().mean(),
        twoValuesAccumulator.xStats().populationVariance(),
        twoValuesAccumulator.populationCovariance());
    assertDiagonalLinearTransformation(
        manyValuesAccumulator.leastSquaresFit(),
        manyValuesAccumulator.xStats().mean(),
        manyValuesAccumulator.yStats().mean(),
        manyValuesAccumulator.xStats().populationVariance(),
        manyValuesAccumulator.populationCovariance());
    assertHorizontalLinearTransformation(
        horizontalValuesAccumulator.leastSquaresFit(), horizontalValuesAccumulator.yStats().mean());
    assertVerticalLinearTransformation(
        verticalValuesAccumulator.leastSquaresFit(), verticalValuesAccumulator.xStats().mean());
    assertThrows(IllegalStateException.class, () -> constantValuesAccumulator.leastSquaresFit());
  }
}