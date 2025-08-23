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
 * This test has been refactored for improved understandability.
 *
 * <p>The original test had a single, large test method covering many scenarios. It has been
 * broken down into smaller, focused tests with descriptive names. The complex `setUp` method has
 * been removed in favor of creating test-specific data within each test, making them self-contained
 * and easier to comprehend. A helper method was introduced to avoid repeating the calculation for
 * the expected Pearson's coefficient.
 */
@RunWith(JUnit4.class)
public class PairedStatsAccumulatorTestTest7 {

    /**
     * Calculates the expected Pearson's correlation coefficient from the accumulator's stats.
     * This is the definition of the coefficient and is used as the ground truth for assertions.
     */
    private double expectedPearsonsCorrelationCoefficient(PairedStatsAccumulator accumulator) {
        double xPopStdDev = accumulator.xStats().populationStandardDeviation();
        double yPopStdDev = accumulator.yStats().populationStandardDeviation();
        return accumulator.populationCovariance() / (xPopStdDev * yPopStdDev);
    }

    @Test
    public void pearsonsCorrelationCoefficient_insufficientData_throwsIllegalStateException() {
        // A count of 0 or 1 is not sufficient to calculate Pearson's correlation.
        PairedStatsAccumulator emptyAccumulator = new PairedStatsAccumulator();
        assertThrows(IllegalStateException.class, emptyAccumulator::pearsonsCorrelationCoefficient);

        PairedStatsAccumulator oneValueAccumulator = new PairedStatsAccumulator();
        oneValueAccumulator.add(ONE_VALUE, OTHER_ONE_VALUE);
        assertThrows(IllegalStateException.class, oneValueAccumulator::pearsonsCorrelationCoefficient);

        // Adding empty stats should not change the count.
        PairedStatsAccumulator accumulatorWithAddedEmpty = new PairedStatsAccumulator();
        accumulatorWithAddedEmpty.add(ONE_VALUE, OTHER_ONE_VALUE);
        accumulatorWithAddedEmpty.addAll(new PairedStatsAccumulator().snapshot());
        assertThrows(IllegalStateException.class, accumulatorWithAddedEmpty::pearsonsCorrelationCoefficient);
    }

    @Test
    public void pearsonsCorrelationCoefficient_zeroVariance_throwsIllegalStateException() {
        // Pearson's correlation is undefined if the variance of either variable is zero.

        // Case 1: y-variance is zero (horizontal line of points)
        PairedStatsAccumulator horizontalAccumulator =
                createFilledPairedStatsAccumulator(
                        MANY_VALUES, Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE));
        assertThrows(IllegalStateException.class, horizontalAccumulator::pearsonsCorrelationCoefficient);

        PairedStatsAccumulator horizontalAccumulatorByAddAll =
                createPartitionedFilledPairedStatsAccumulator(
                        MANY_VALUES, Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE), 2);
        assertThrows(
                IllegalStateException.class, horizontalAccumulatorByAddAll::pearsonsCorrelationCoefficient);

        // Case 2: x-variance is zero (vertical line of points)
        PairedStatsAccumulator verticalAccumulator =
                createFilledPairedStatsAccumulator(
                        Collections.nCopies(OTHER_MANY_VALUES_COUNT, ONE_VALUE), OTHER_MANY_VALUES);
        assertThrows(IllegalStateException.class, verticalAccumulator::pearsonsCorrelationCoefficient);

        PairedStatsAccumulator verticalAccumulatorByAddAll =
                createPartitionedFilledPairedStatsAccumulator(
                        Collections.nCopies(OTHER_MANY_VALUES_COUNT, ONE_VALUE), OTHER_MANY_VALUES, 2);
        assertThrows(
                IllegalStateException.class, verticalAccumulatorByAddAll::pearsonsCorrelationCoefficient);

        // Case 3: Both variances are zero (all points are the same)
        PairedStatsAccumulator constantAccumulator =
                createFilledPairedStatsAccumulator(
                        Collections.nCopies(MANY_VALUES_COUNT, ONE_VALUE),
                        Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE));
        assertThrows(IllegalStateException.class, constantAccumulator::pearsonsCorrelationCoefficient);

        PairedStatsAccumulator constantAccumulatorByAddAll =
                createPartitionedFilledPairedStatsAccumulator(
                        Collections.nCopies(MANY_VALUES_COUNT, ONE_VALUE),
                        Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE),
                        2);
        assertThrows(
                IllegalStateException.class, constantAccumulatorByAddAll::pearsonsCorrelationCoefficient);
    }

    @Test
    public void pearsonsCorrelationCoefficient_nonFiniteData_isNan() {
        // With non-finite values, the result should be NaN.
        for (ManyValues yValues : ALL_MANY_VALUES) {
            if (yValues.hasAnyNonFinite()) {
                // Test with add()
                PairedStatsAccumulator accumulator =
                        createFilledPairedStatsAccumulator(MANY_VALUES, yValues.asIterable());
                assertWithMessage("Dataset: " + yValues)
                        .that(accumulator.pearsonsCorrelationCoefficient())
                        .isNaN();

                // Test with addAll()
                PairedStatsAccumulator accumulatorByAddAll =
                        createPartitionedFilledPairedStatsAccumulator(MANY_VALUES, yValues.asIterable(), 2);
                assertWithMessage("Dataset via addAll: " + yValues)
                        .that(accumulatorByAddAll.pearsonsCorrelationCoefficient())
                        .isNaN();
            }
        }
    }

    @Test
    public void pearsonsCorrelationCoefficient_validFiniteData_isCorrect() {
        // Test with two values
        PairedStatsAccumulator twoValuesAccumulator =
                createFilledPairedStatsAccumulator(TWO_VALUES, OTHER_TWO_VALUES);
        assertThat(twoValuesAccumulator.pearsonsCorrelationCoefficient())
                .isWithin(ALLOWED_ERROR)
                .of(expectedPearsonsCorrelationCoefficient(twoValuesAccumulator));

        // Test with many values, added via add()
        PairedStatsAccumulator manyValuesAccumulator =
                createFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES);
        assertThat(manyValuesAccumulator.pearsonsCorrelationCoefficient())
                .isWithin(ALLOWED_ERROR)
                .of(expectedPearsonsCorrelationCoefficient(manyValuesAccumulator));

        // Test with many values, added via addAll()
        PairedStatsAccumulator manyValuesAccumulatorByAddAll =
                createPartitionedFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES, 2);
        assertThat(manyValuesAccumulatorByAddAll.pearsonsCorrelationCoefficient())
                .isWithin(ALLOWED_ERROR)
                .of(expectedPearsonsCorrelationCoefficient(manyValuesAccumulatorByAddAll));

        // Test with a variety of other finite datasets
        for (ManyValues yValues : ALL_MANY_VALUES) {
            if (!yValues.hasAnyNonFinite()) {
                // Test with add()
                PairedStatsAccumulator accumulator =
                        createFilledPairedStatsAccumulator(MANY_VALUES, yValues.asIterable());
                assertWithMessage("Dataset: " + yValues)
                        .that(accumulator.pearsonsCorrelationCoefficient())
                        .isWithin(ALLOWED_ERROR)
                        .of(expectedPearsonsCorrelationCoefficient(accumulator));

                // Test with addAll()
                PairedStatsAccumulator accumulatorByAddAll =
                        createPartitionedFilledPairedStatsAccumulator(MANY_VALUES, yValues.asIterable(), 2);
                assertWithMessage("Dataset via addAll: " + yValues)
                        .that(accumulatorByAddAll.pearsonsCorrelationCoefficient())
                        .isWithin(ALLOWED_ERROR)
                        .of(expectedPearsonsCorrelationCoefficient(accumulatorByAddAll));
            }
        }
    }
}