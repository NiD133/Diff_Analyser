package com.google.common.math;

import static com.google.common.math.StatsTesting.ALLOWED_ERROR;
import static com.google.common.math.StatsTesting.ALL_MANY_VALUES;
import static com.google.common.math.StatsTesting.MANY_VALUES;
import static com.google.common.math.StatsTesting.MANY_VALUES_COUNT;
import static com.google.common.math.StatsTesting.ONE_VALUE;
import static com.google.common.math.StatsTesting.OTHER_MANY_VALUES;
import static com.google.common.math.StatsTesting.OTHER_MANY_VALUES_COUNT;
import static com.google.common.math.StatsTesting.OTHER_ONE_VALUE;
import static com.google.common.math.StatsTesting.OTHER_TWO_VALUES;
import static com.google.common.math.StatsTesting.TWO_VALUES;
import static com.google.common.math.StatsTesting.assertDiagonalLinearTransformation;
import static com.google.common.math.StatsTesting.assertHorizontalLinearTransformation;
import static com.google.common.math.StatsTesting.assertLinearTransformationNaN;
import static com.google.common.math.StatsTesting.assertVerticalLinearTransformation;
import static com.google.common.math.StatsTesting.createFilledPairedStatsAccumulator;
import static com.google.common.math.StatsTesting.createPartitionedFilledPairedStatsAccumulator;
import static org.junit.Assert.assertThrows;

import com.google.common.math.StatsTesting.ManyValues;
import java.util.Collections;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link PairedStatsAccumulator#leastSquaresFit()}.
 * This class focuses on various data scenarios and edge cases for linear regression.
 */
@RunWith(JUnit4.class)
public class PairedStatsAccumulatorLeastSquaresFitTest {

    @Test
    public void leastSquaresFit_empty_throwsIllegalStateException() {
        // An accumulator with no data cannot produce a fit.
        PairedStatsAccumulator emptyAccumulator = new PairedStatsAccumulator();
        assertThrows(IllegalStateException.class, emptyAccumulator::leastSquaresFit);

        // Same for an accumulator where empty stats have been added.
        PairedStatsAccumulator accumulatorFromEmptyStats = new PairedStatsAccumulator();
        accumulatorFromEmptyStats.addAll(emptyAccumulator.snapshot());
        assertThrows(IllegalStateException.class, accumulatorFromEmptyStats::leastSquaresFit);
    }

    @Test
    public void leastSquaresFit_onePoint_throwsIllegalStateException() {
        // An accumulator with a single data point cannot produce a fit.
        PairedStatsAccumulator oneValueAccumulator = new PairedStatsAccumulator();
        oneValueAccumulator.add(ONE_VALUE, OTHER_ONE_VALUE);
        assertThrows(IllegalStateException.class, oneValueAccumulator::leastSquaresFit);
    }

    @Test
    public void leastSquaresFit_twoPoints_isCorrect() {
        PairedStatsAccumulator accumulator =
                createFilledPairedStatsAccumulator(TWO_VALUES, OTHER_TWO_VALUES);

        LinearTransformation fit = accumulator.leastSquaresFit();

        assertDiagonalLinearTransformation(
                fit,
                accumulator.xStats().mean(),
                accumulator.yStats().mean(),
                accumulator.xStats().populationVariance(),
                accumulator.populationCovariance());
    }

    @Test
    public void leastSquaresFit_twoPoints_viaAddAll_isCorrect() {
        // Test with an accumulator built by adding partitioned stats.
        PairedStatsAccumulator accumulator =
                createPartitionedFilledPairedStatsAccumulator(TWO_VALUES, OTHER_TWO_VALUES, 1);

        LinearTransformation fit = accumulator.leastSquaresFit();

        assertDiagonalLinearTransformation(
                fit,
                accumulator.xStats().mean(),
                accumulator.yStats().mean(),
                accumulator.xStats().populationVariance(),
                accumulator.populationCovariance());
    }

    @Test
    public void leastSquaresFit_manyPoints_isCorrect() {
        PairedStatsAccumulator accumulator =
                createFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES);

        LinearTransformation fit = accumulator.leastSquaresFit();

        assertDiagonalLinearTransformation(
                fit,
                accumulator.xStats().mean(),
                accumulator.yStats().mean(),
                accumulator.xStats().populationVariance(),
                accumulator.populationCovariance());
    }

    @Test
    public void leastSquaresFit_manyPoints_viaAddAll_isCorrect() {
        // Test with an accumulator built by adding partitioned stats.
        PairedStatsAccumulator accumulator =
                createPartitionedFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES, 2);

        LinearTransformation fit = accumulator.leastSquaresFit();

        assertDiagonalLinearTransformation(
                fit,
                accumulator.xStats().mean(),
                accumulator.yStats().mean(),
                accumulator.xStats().populationVariance(),
                accumulator.populationCovariance());
    }

    @Test
    public void leastSquaresFit_withNonFiniteValues_returnsNaNFit() {
        // The fit should be NaN if any x-value is non-finite.
        for (ManyValues values : ALL_MANY_VALUES) {
            if (values.hasAnyNonFinite()) {
                PairedStatsAccumulator accumulator =
                        createFilledPairedStatsAccumulator(values.asIterable(), OTHER_MANY_VALUES);
                PairedStatsAccumulator accumulatorByAddAll =
                        createPartitionedFilledPairedStatsAccumulator(values.asIterable(), OTHER_MANY_VALUES, 2);

                assertLinearTransformationNaN(accumulator.leastSquaresFit());
                assertLinearTransformationNaN(accumulatorByAddAll.leastSquaresFit());
            }
        }
    }

    @Test
    public void leastSquaresFit_horizontalLineData_isCorrect() {
        // Data where y is constant (zero y-variance) should result in a horizontal line fit.
        PairedStatsAccumulator accumulator =
                createFilledPairedStatsAccumulator(
                        MANY_VALUES, Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE));
        PairedStatsAccumulator accumulatorByAddAll =
                createPartitionedFilledPairedStatsAccumulator(
                        MANY_VALUES, Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE), 2);

        assertHorizontalLinearTransformation(
                accumulator.leastSquaresFit(), accumulator.yStats().mean());
        assertHorizontalLinearTransformation(
                accumulatorByAddAll.leastSquaresFit(), accumulatorByAddAll.yStats().mean());
    }

    @Test
    public void leastSquaresFit_verticalLineData_isCorrect() {
        // Data where x is constant (zero x-variance) should result in a vertical line fit.
        PairedStatsAccumulator accumulator =
                createFilledPairedStatsAccumulator(
                        Collections.nCopies(OTHER_MANY_VALUES_COUNT, ONE_VALUE), OTHER_MANY_VALUES);
        PairedStatsAccumulator accumulatorByAddAll =
                createPartitionedFilledPairedStatsAccumulator(
                        Collections.nCopies(OTHER_MANY_VALUES_COUNT, ONE_VALUE), OTHER_MANY_VALUES, 2);

        assertVerticalLinearTransformation(
                accumulator.leastSquaresFit(), accumulator.xStats().mean());
        assertVerticalLinearTransformation(
                accumulatorByAddAll.leastSquaresFit(), accumulatorByAddAll.xStats().mean());
    }

    @Test
    public void leastSquaresFit_constantData_throwsIllegalStateException() {
        // Data where both x and y are constant (zero variance in both) cannot produce a fit.
        PairedStatsAccumulator accumulator =
                createFilledPairedStatsAccumulator(
                        Collections.nCopies(MANY_VALUES_COUNT, ONE_VALUE),
                        Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE));
        PairedStatsAccumulator accumulatorByAddAll =
                createPartitionedFilledPairedStatsAccumulator(
                        Collections.nCopies(MANY_VALUES_COUNT, ONE_VALUE),
                        Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE),
                        2);

        assertThrows(IllegalStateException.class, accumulator::leastSquaresFit);
        assertThrows(IllegalStateException.class, accumulatorByAddAll::leastSquaresFit);
    }
}