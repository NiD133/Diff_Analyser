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

public class PairedStatsAccumulatorTestTest7 extends TestCase {

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
        twoValuesAccumulatorByAddAllPartitionedPairedStats = createPartitionedFilledPairedStatsAccumulator(TWO_VALUES, OTHER_TWO_VALUES, 1);
        manyValuesAccumulator = createFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES);
        manyValuesAccumulatorByAddAllPartitionedPairedStats = createPartitionedFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES, 2);
        horizontalValuesAccumulator = createFilledPairedStatsAccumulator(MANY_VALUES, Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE));
        horizontalValuesAccumulatorByAddAllPartitionedPairedStats = createPartitionedFilledPairedStatsAccumulator(MANY_VALUES, Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE), 2);
        verticalValuesAccumulator = createFilledPairedStatsAccumulator(Collections.nCopies(OTHER_MANY_VALUES_COUNT, ONE_VALUE), OTHER_MANY_VALUES);
        verticalValuesAccumulatorByAddAllPartitionedPairedStats = createPartitionedFilledPairedStatsAccumulator(Collections.nCopies(OTHER_MANY_VALUES_COUNT, ONE_VALUE), OTHER_MANY_VALUES, 2);
        constantValuesAccumulator = createFilledPairedStatsAccumulator(Collections.nCopies(MANY_VALUES_COUNT, ONE_VALUE), Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE));
        constantValuesAccumulatorByAddAllPartitionedPairedStats = createPartitionedFilledPairedStatsAccumulator(Collections.nCopies(MANY_VALUES_COUNT, ONE_VALUE), Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE), 2);
    }

    public void testPearsonsCorrelationCoefficient() {
        assertThrows(IllegalStateException.class, () -> emptyAccumulator.pearsonsCorrelationCoefficient());
        assertThrows(IllegalStateException.class, () -> emptyAccumulatorByAddAllEmptyPairedStats.pearsonsCorrelationCoefficient());
        assertThrows(IllegalStateException.class, () -> oneValueAccumulator.pearsonsCorrelationCoefficient());
        assertThrows(IllegalStateException.class, () -> oneValueAccumulatorByAddAllEmptyPairedStats.pearsonsCorrelationCoefficient());
        assertThat(twoValuesAccumulator.pearsonsCorrelationCoefficient()).isWithin(ALLOWED_ERROR).of(twoValuesAccumulator.populationCovariance() / (twoValuesAccumulator.xStats().populationStandardDeviation() * twoValuesAccumulator.yStats().populationStandardDeviation()));
        assertThat(manyValuesAccumulator.pearsonsCorrelationCoefficient()).isWithin(ALLOWED_ERROR).of(manyValuesAccumulator.populationCovariance() / (manyValuesAccumulator.xStats().populationStandardDeviation() * manyValuesAccumulator.yStats().populationStandardDeviation()));
        assertThat(manyValuesAccumulatorByAddAllPartitionedPairedStats.pearsonsCorrelationCoefficient()).isWithin(ALLOWED_ERROR).of(manyValuesAccumulatorByAddAllPartitionedPairedStats.populationCovariance() / (manyValuesAccumulatorByAddAllPartitionedPairedStats.xStats().populationStandardDeviation() * manyValuesAccumulatorByAddAllPartitionedPairedStats.yStats().populationStandardDeviation()));
        // For datasets of many double values, we test many combinations of finite and non-finite
        // y-values:
        for (ManyValues values : ALL_MANY_VALUES) {
            PairedStatsAccumulator accumulator = createFilledPairedStatsAccumulator(MANY_VALUES, values.asIterable());
            PairedStatsAccumulator accumulatorByAddAllPartitionedPairedStats = createPartitionedFilledPairedStatsAccumulator(MANY_VALUES, values.asIterable(), 2);
            double pearsonsCorrelationCoefficient = accumulator.pearsonsCorrelationCoefficient();
            double pearsonsCorrelationCoefficientByAddAllPartitionedPairedStats = accumulatorByAddAllPartitionedPairedStats.pearsonsCorrelationCoefficient();
            if (values.hasAnyNonFinite()) {
                assertWithMessage("Pearson's correlation coefficient of " + values).that(pearsonsCorrelationCoefficient).isNaN();
                assertWithMessage("Pearson's correlation coefficient by addAll(PairedStats) of " + values).that(pearsonsCorrelationCoefficient).isNaN();
            } else {
                assertWithMessage("Pearson's correlation coefficient of " + values).that(pearsonsCorrelationCoefficient).isWithin(ALLOWED_ERROR).of(accumulator.populationCovariance() / (accumulator.xStats().populationStandardDeviation() * accumulator.yStats().populationStandardDeviation()));
                assertWithMessage("Pearson's correlation coefficient by addAll(PairedStats) of " + values).that(pearsonsCorrelationCoefficientByAddAllPartitionedPairedStats).isWithin(ALLOWED_ERROR).of(accumulatorByAddAllPartitionedPairedStats.populationCovariance() / (accumulatorByAddAllPartitionedPairedStats.xStats().populationStandardDeviation() * accumulatorByAddAllPartitionedPairedStats.yStats().populationStandardDeviation()));
            }
        }
        assertThrows(IllegalStateException.class, () -> horizontalValuesAccumulator.pearsonsCorrelationCoefficient());
        assertThrows(IllegalStateException.class, () -> horizontalValuesAccumulatorByAddAllPartitionedPairedStats.pearsonsCorrelationCoefficient());
        assertThrows(IllegalStateException.class, () -> verticalValuesAccumulator.pearsonsCorrelationCoefficient());
        assertThrows(IllegalStateException.class, () -> verticalValuesAccumulatorByAddAllPartitionedPairedStats.pearsonsCorrelationCoefficient());
        assertThrows(IllegalStateException.class, () -> constantValuesAccumulator.pearsonsCorrelationCoefficient());
        assertThrows(IllegalStateException.class, () -> constantValuesAccumulatorByAddAllPartitionedPairedStats.pearsonsCorrelationCoefficient());
    }
}
