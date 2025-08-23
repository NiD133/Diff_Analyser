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
import org.jspecify.annotations.NullUnmarked;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for PairedStatsAccumulator.
 *
 * This suite verifies:
 * - Correctness of statistics after adding values directly vs. via addAll(PairedStats).
 * - Error handling for insufficient data and degenerate datasets.
 * - Behavior with finite and non-finite values.
 * - Least squares fit characteristics for general, horizontal, vertical, and constant datasets.
 */
@NullUnmarked
public class PairedStatsAccumulatorTest {

  private static final int PARTITIONS = 2;

  // Naming convention: <shape><Scenario>, where Scenario ∈ {Direct, ViaAddAll}.
  private PairedStatsAccumulator emptyDirect;
  private PairedStatsAccumulator emptyViaAddAll;

  private PairedStatsAccumulator oneDirect;
  private PairedStatsAccumulator oneViaAddAll;

  private PairedStatsAccumulator twoDirect;
  private PairedStatsAccumulator twoViaAddAll;

  private PairedStatsAccumulator manyDirect;
  private PairedStatsAccumulator manyViaAddAll;

  private PairedStatsAccumulator horizontalDirect;
  private PairedStatsAccumulator horizontalViaAddAll;

  private PairedStatsAccumulator verticalDirect;
  private PairedStatsAccumulator verticalViaAddAll;

  private PairedStatsAccumulator constantDirect;
  private PairedStatsAccumulator constantViaAddAll;

  @Before
  public void setUp() {
    // Empty
    emptyDirect = new PairedStatsAccumulator();
    emptyViaAddAll = new PairedStatsAccumulator();
    emptyViaAddAll.addAll(emptyDirect.snapshot());

    // One value
    oneDirect = new PairedStatsAccumulator();
    oneDirect.add(ONE_VALUE, OTHER_ONE_VALUE);
    oneViaAddAll = new PairedStatsAccumulator();
    oneViaAddAll.add(ONE_VALUE, OTHER_ONE_VALUE);
    oneViaAddAll.addAll(emptyDirect.snapshot()); // no-op addAll still leaves one value

    // Two values
    twoDirect = createFilledPairedStatsAccumulator(TWO_VALUES, OTHER_TWO_VALUES);
    twoViaAddAll = createPartitionedFilledPairedStatsAccumulator(TWO_VALUES, OTHER_TWO_VALUES, 1);

    // Many values
    manyDirect = createFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES);
    manyViaAddAll =
        createPartitionedFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES, PARTITIONS);

    // Horizontal line (varying x, constant y)
    horizontalDirect =
        createFilledPairedStatsAccumulator(
            MANY_VALUES, Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE));
    horizontalViaAddAll =
        createPartitionedFilledPairedStatsAccumulator(
            MANY_VALUES, Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE), PARTITIONS);

    // Vertical line (constant x, varying y)
    verticalDirect =
        createFilledPairedStatsAccumulator(
            Collections.nCopies(OTHER_MANY_VALUES_COUNT, ONE_VALUE), OTHER_MANY_VALUES);
    verticalViaAddAll =
        createPartitionedFilledPairedStatsAccumulator(
            Collections.nCopies(OTHER_MANY_VALUES_COUNT, ONE_VALUE), OTHER_MANY_VALUES, PARTITIONS);

    // Constant point (constant x, constant y)
    constantDirect =
        createFilledPairedStatsAccumulator(
            Collections.nCopies(MANY_VALUES_COUNT, ONE_VALUE),
            Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE));
    constantViaAddAll =
        createPartitionedFilledPairedStatsAccumulator(
            Collections.nCopies(MANY_VALUES_COUNT, ONE_VALUE),
            Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE),
            PARTITIONS);
  }

  // -------------------- Basic structure --------------------

  @Test
  public void count_matchesExpected() {
    assertCount(0, emptyDirect, emptyViaAddAll);
    assertCount(1, oneDirect, oneViaAddAll);
    assertCount(2, twoDirect, twoViaAddAll);
    assertCount(MANY_VALUES_COUNT, manyDirect, manyViaAddAll);
  }

  @Test
  public void count_overflow_doesNotThrow() {
    PairedStatsAccumulator acc = new PairedStatsAccumulator();
    acc.add(ONE_VALUE, OTHER_ONE_VALUE);
    for (int power = 1; power < Long.SIZE - 1; power++) {
      acc.addAll(acc.snapshot());
    }
    // Should overflow without throwing.
    acc.addAll(acc.snapshot());
    assertThat(acc.count()).isLessThan(0L);
  }

  @Test
  public void xStats_matchesExpected() {
    assertXStatsEqual(EMPTY_STATS_ITERABLE, emptyDirect, emptyViaAddAll);
    assertXStatsEqual(ONE_VALUE_STATS, oneDirect, oneViaAddAll);
    assertXStatsEqual(TWO_VALUES_STATS, twoDirect, twoViaAddAll);
    assertXStatsEqual(MANY_VALUES_STATS_ITERABLE, manyDirect, manyViaAddAll);
  }

  @Test
  public void yStats_matchesExpected() {
    assertYStatsEqual(EMPTY_STATS_ITERABLE, emptyDirect, emptyViaAddAll);
    assertYStatsEqual(OTHER_ONE_VALUE_STATS, oneDirect, oneViaAddAll);
    assertYStatsEqual(OTHER_TWO_VALUES_STATS, twoDirect, twoViaAddAll);
    assertYStatsEqual(OTHER_MANY_VALUES_STATS, manyDirect, manyViaAddAll);
  }

  // -------------------- Covariance --------------------

  @Test
  public void populationCovariance_matchesExpected() {
    assertThrows(IllegalStateException.class, () -> emptyDirect.populationCovariance());
    assertThrows(IllegalStateException.class, () -> emptyViaAddAll.populationCovariance());

    assertThat(oneDirect.populationCovariance()).isEqualTo(0.0);
    assertThat(oneViaAddAll.populationCovariance()).isEqualTo(0.0);

    double expectedTwo = TWO_VALUES_SUM_OF_PRODUCTS_OF_DELTAS / 2;
    assertPopulationCovariance(expectedTwo, twoDirect, twoViaAddAll);

    double expectedMany = MANY_VALUES_SUM_OF_PRODUCTS_OF_DELTAS / MANY_VALUES_COUNT;
    assertPopulationCovariance(expectedMany, manyDirect, manyViaAddAll);

    // Stress many finite/non-finite x-values against fixed y-values.
    for (ManyValues xVals : ALL_MANY_VALUES) {
      PairedStatsAccumulator acc =
          createFilledPairedStatsAccumulator(xVals.asIterable(), OTHER_MANY_VALUES);
      PairedStatsAccumulator accViaAddAll =
          createPartitionedFilledPairedStatsAccumulator(
              xVals.asIterable(), OTHER_MANY_VALUES, PARTITIONS);

      if (xVals.hasAnyNonFinite()) {
        assertWithMessage("population covariance of " + xVals)
            .that(acc.populationCovariance())
            .isNaN();
        assertWithMessage("population covariance by addAll(PairedStats) of " + xVals)
            .that(accViaAddAll.populationCovariance())
            .isNaN();
      } else {
        assertWithMessage("population covariance of " + xVals)
            .that(acc.populationCovariance())
            .isWithin(ALLOWED_ERROR)
            .of(expectedMany);
        assertWithMessage("population covariance by addAll(PairedStats) of " + xVals)
            .that(accViaAddAll.populationCovariance())
            .isWithin(ALLOWED_ERROR)
            .of(expectedMany);
      }
    }

    // Degenerate shapes
    assertPopulationCovariance(0.0, horizontalDirect, horizontalViaAddAll);
    assertPopulationCovariance(0.0, verticalDirect, verticalViaAddAll);
    assertPopulationCovariance(0.0, constantDirect, constantViaAddAll);
  }

  @Test
  public void sampleCovariance_matchesExpected() {
    assertThrows(IllegalStateException.class, () -> emptyDirect.sampleCovariance());
    assertThrows(IllegalStateException.class, () -> emptyViaAddAll.sampleCovariance());

    assertThrows(IllegalStateException.class, () -> oneDirect.sampleCovariance());
    assertThrows(IllegalStateException.class, () -> oneViaAddAll.sampleCovariance());

    double expectedTwo = TWO_VALUES_SUM_OF_PRODUCTS_OF_DELTAS;
    assertSampleCovariance(expectedTwo, twoDirect, twoViaAddAll);

    double expectedMany = MANY_VALUES_SUM_OF_PRODUCTS_OF_DELTAS / (MANY_VALUES_COUNT - 1);
    assertSampleCovariance(expectedMany, manyDirect, manyViaAddAll);

    // Degenerate shapes
    assertSampleCovariance(0.0, horizontalDirect, horizontalViaAddAll);
    assertSampleCovariance(0.0, verticalDirect, verticalViaAddAll);
    assertSampleCovariance(0.0, constantDirect, constantViaAddAll);
  }

  // -------------------- Pearson correlation --------------------

  @Test
  public void pearsonsCorrelationCoefficient_matchesExpectedOrThrows() {
    assertThrows(IllegalStateException.class, () -> emptyDirect.pearsonsCorrelationCoefficient());
    assertThrows(IllegalStateException.class, () -> emptyViaAddAll.pearsonsCorrelationCoefficient());
    assertThrows(IllegalStateException.class, () -> oneDirect.pearsonsCorrelationCoefficient());
    assertThrows(IllegalStateException.class, () -> oneViaAddAll.pearsonsCorrelationCoefficient());

    // Computed definition checks
    assertThat(twoDirect.pearsonsCorrelationCoefficient())
        .isWithin(ALLOWED_ERROR)
        .of(
            twoDirect.populationCovariance()
                / (twoDirect.xStats().populationStandardDeviation()
                    * twoDirect.yStats().populationStandardDeviation()));

    assertThat(manyDirect.pearsonsCorrelationCoefficient())
        .isWithin(ALLOWED_ERROR)
        .of(
            manyDirect.populationCovariance()
                / (manyDirect.xStats().populationStandardDeviation()
                    * manyDirect.yStats().populationStandardDeviation()));

    assertThat(manyViaAddAll.pearsonsCorrelationCoefficient())
        .isWithin(ALLOWED_ERROR)
        .of(
            manyViaAddAll.populationCovariance()
                / (manyViaAddAll.xStats().populationStandardDeviation()
                    * manyViaAddAll.yStats().populationStandardDeviation()));

    // Stress many finite/non-finite y-values against fixed x-values.
    for (ManyValues yVals : ALL_MANY_VALUES) {
      PairedStatsAccumulator acc =
          createFilledPairedStatsAccumulator(MANY_VALUES, yVals.asIterable());
      PairedStatsAccumulator accViaAddAll =
          createPartitionedFilledPairedStatsAccumulator(
              MANY_VALUES, yVals.asIterable(), PARTITIONS);

      double r = acc.pearsonsCorrelationCoefficient();
      double rViaAddAll = accViaAddAll.pearsonsCorrelationCoefficient();

      if (yVals.hasAnyNonFinite()) {
        assertWithMessage("Pearson's correlation coefficient of " + yVals).that(r).isNaN();
        assertWithMessage("Pearson's correlation coefficient by addAll(PairedStats) of " + yVals)
            .that(rViaAddAll)
            .isNaN();
      } else {
        double expected =
            acc.populationCovariance()
                / (acc.xStats().populationStandardDeviation()
                    * acc.yStats().populationStandardDeviation());
        double expectedViaAddAll =
            accViaAddAll.populationCovariance()
                / (accViaAddAll.xStats().populationStandardDeviation()
                    * accViaAddAll.yStats().populationStandardDeviation());

        assertWithMessage("Pearson's correlation coefficient of " + yVals)
            .that(r)
            .isWithin(ALLOWED_ERROR)
            .of(expected);
        assertWithMessage("Pearson's correlation coefficient by addAll(PairedStats) of " + yVals)
            .that(rViaAddAll)
            .isWithin(ALLOWED_ERROR)
            .of(expectedViaAddAll);
      }
    }

    // Degenerate shapes where variance is zero -> exception
    assertThrows(
        IllegalStateException.class, () -> horizontalDirect.pearsonsCorrelationCoefficient());
    assertThrows(
        IllegalStateException.class, () -> horizontalViaAddAll.pearsonsCorrelationCoefficient());
    assertThrows(
        IllegalStateException.class, () -> verticalDirect.pearsonsCorrelationCoefficient());
    assertThrows(
        IllegalStateException.class, () -> verticalViaAddAll.pearsonsCorrelationCoefficient());
    assertThrows(
        IllegalStateException.class, () -> constantDirect.pearsonsCorrelationCoefficient());
    assertThrows(
        IllegalStateException.class, () -> constantViaAddAll.pearsonsCorrelationCoefficient());
  }

  // -------------------- Least squares fit --------------------

  @Test
  public void leastSquaresFit_matchesExpectedOrThrows() {
    assertThrows(IllegalStateException.class, () -> emptyDirect.leastSquaresFit());
    assertThrows(IllegalStateException.class, () -> emptyViaAddAll.leastSquaresFit());
    assertThrows(IllegalStateException.class, () -> oneDirect.leastSquaresFit());
    assertThrows(IllegalStateException.class, () -> oneViaAddAll.leastSquaresFit());

    // General case (diagonal)
    assertDiagonalLinearTransformation(
        twoDirect.leastSquaresFit(),
        twoDirect.xStats().mean(),
        twoDirect.yStats().mean(),
        twoDirect.xStats().populationVariance(),
        twoDirect.populationCovariance());
    assertDiagonalLinearTransformation(
        twoViaAddAll.leastSquaresFit(),
        twoViaAddAll.xStats().mean(),
        twoViaAddAll.yStats().mean(),
        twoViaAddAll.xStats().populationVariance(),
        twoViaAddAll.populationCovariance());

    assertDiagonalLinearTransformation(
        manyDirect.leastSquaresFit(),
        manyDirect.xStats().mean(),
        manyDirect.yStats().mean(),
        manyDirect.xStats().populationVariance(),
        manyDirect.populationCovariance());
    assertDiagonalLinearTransformation(
        manyViaAddAll.leastSquaresFit(),
        manyViaAddAll.xStats().mean(),
        manyViaAddAll.yStats().mean(),
        manyViaAddAll.xStats().populationVariance(),
        manyViaAddAll.populationCovariance());

    // Stress many finite/non-finite x-values against fixed y-values.
    for (ManyValues xVals : ALL_MANY_VALUES) {
      PairedStatsAccumulator acc =
          createFilledPairedStatsAccumulator(xVals.asIterable(), OTHER_MANY_VALUES);
      PairedStatsAccumulator accViaAddAll =
          createPartitionedFilledPairedStatsAccumulator(
              xVals.asIterable(), OTHER_MANY_VALUES, PARTITIONS);

      LinearTransformation fit = acc.leastSquaresFit();
      LinearTransformation fitViaAddAll = accViaAddAll.leastSquaresFit();

      if (xVals.hasAnyNonFinite()) {
        assertLinearTransformationNaN(fit);
        assertLinearTransformationNaN(fitViaAddAll);
      } else {
        assertDiagonalLinearTransformation(
            fit,
            acc.xStats().mean(),
            acc.yStats().mean(),
            acc.xStats().populationVariance(),
            acc.populationCovariance());
        assertDiagonalLinearTransformation(
            fitViaAddAll,
            accViaAddAll.xStats().mean(),
            accViaAddAll.yStats().mean(),
            accViaAddAll.xStats().populationVariance(),
            accViaAddAll.populationCovariance());
      }
    }

    // Degenerate shapes
    assertHorizontalLinearTransformation(
        horizontalDirect.leastSquaresFit(), horizontalDirect.yStats().mean());
    assertHorizontalLinearTransformation(
        horizontalViaAddAll.leastSquaresFit(), horizontalViaAddAll.yStats().mean());
    assertVerticalLinearTransformation(
        verticalDirect.leastSquaresFit(), verticalDirect.xStats().mean());
    assertVerticalLinearTransformation(
        verticalViaAddAll.leastSquaresFit(), verticalViaAddAll.xStats().mean());

    assertThrows(IllegalStateException.class, () -> constantDirect.leastSquaresFit());
    assertThrows(IllegalStateException.class, () -> constantViaAddAll.leastSquaresFit());
  }

  // -------------------- Helpers (reduce repetition) --------------------

  private static void assertCount(long expected, PairedStatsAccumulator... accs) {
    for (PairedStatsAccumulator acc : accs) {
      assertThat(acc.count()).isEqualTo(expected);
    }
  }

  private static void assertXStatsEqual(Stats expected, PairedStatsAccumulator... accs) {
    for (PairedStatsAccumulator acc : accs) {
      assertStatsApproxEqual(expected, acc.xStats());
    }
  }

  private static void assertYStatsEqual(Stats expected, PairedStatsAccumulator... accs) {
    for (PairedStatsAccumulator acc : accs) {
      assertStatsApproxEqual(expected, acc.yStats());
    }
  }

  private static void assertPopulationCovariance(
      double expected, PairedStatsAccumulator... accs) {
    for (PairedStatsAccumulator acc : accs) {
      assertThat(acc.populationCovariance()).isWithin(ALLOWED_ERROR).of(expected);
    }
  }

  private static void assertSampleCovariance(double expected, PairedStatsAccumulator... accs) {
    for (PairedStatsAccumulator acc : accs) {
      assertThat(acc.sampleCovariance()).isWithin(ALLOWED_ERROR).of(expected);
    }
  }
}