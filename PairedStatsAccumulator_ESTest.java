/*
 * Copyright (C) 2012 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.common.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Understandable tests for {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulatorTest {

    private static final double TOLERANCE = 1e-9;

    private PairedStatsAccumulator accumulator;

    @Before
    public void setUp() {
        accumulator = new PairedStatsAccumulator();
    }

    @Test
    public void count_initially_isZero() {
        assertEquals(0, accumulator.count());
    }

    @Test
    public void count_afterAddingPoints_isCorrect() {
        // Act
        accumulator.add(1.0, 2.0);
        accumulator.add(3.0, 4.0);

        // Assert
        assertEquals(2, accumulator.count());
    }

    @Test
    public void addAll_withEmptyPairedStats_doesNothing() {
        // Arrange
        PairedStats emptyStats = new PairedStatsAccumulator().snapshot();
        accumulator.add(1.0, 1.0);

        // Act
        accumulator.addAll(emptyStats);

        // Assert
        assertEquals(1, accumulator.count());
    }

    @Test
    public void addAll_combinesStatisticsCorrectly() {
        // Arrange
        accumulator.add(1.0, 2.0);
        PairedStatsAccumulator anotherAccumulator = new PairedStatsAccumulator();
        anotherAccumulator.add(3.0, 4.0);
        PairedStats anotherStats = anotherAccumulator.snapshot();

        // Act
        accumulator.addAll(anotherStats);

        // Assert
        assertEquals(2, accumulator.count());
        assertEquals(4.0, accumulator.xStats().sum(), TOLERANCE); // 1.0 + 3.0
        assertEquals(6.0, accumulator.yStats().sum(), TOLERANCE); // 2.0 + 4.0
    }

    @Test(expected = NullPointerException.class)
    public void addAll_withNull_throwsNullPointerException() {
        // Act
        accumulator.addAll(null);
    }

    @Test
    public void snapshot_initially_isEmpty() {
        // Act
        PairedStats snapshot = accumulator.snapshot();

        // Assert
        assertEquals(0, snapshot.count());
        assertEquals(0.0, snapshot.xStats().sum(), TOLERANCE);
        assertEquals(0.0, snapshot.yStats().sum(), TOLERANCE);
    }

    @Test
    public void xStats_and_yStats_areInitiallyEmpty() {
        assertTrue(accumulator.xStats().count() == 0);
        assertTrue(accumulator.yStats().count() == 0);
    }

    @Test
    public void xStats_and_yStats_afterAdd_areCorrect() {
        // Act
        accumulator.add(3.0, 4.0);
        accumulator.add(5.0, 6.0);

        // Assert
        Stats xStats = accumulator.xStats();
        assertEquals(2, xStats.count());
        assertEquals(4.0, xStats.mean(), TOLERANCE);

        Stats yStats = accumulator.yStats();
        assertEquals(2, yStats.count());
        assertEquals(5.0, yStats.mean(), TOLERANCE);
    }

    @Test(expected = IllegalStateException.class)
    public void populationCovariance_withNoPoints_throwsIllegalStateException() {
        accumulator.populationCovariance();
    }

    @Test
    public void populationCovariance_withOnePoint_isZero() {
        // Arrange
        accumulator.add(1.0, 2.0);

        // Act & Assert
        assertEquals(0.0, accumulator.populationCovariance(), TOLERANCE);
    }



    @Test
    public void populationCovariance_withPerfectlyCorrelatedData_isCorrect() {
        // Arrange: y = x + 1
        accumulator.add(1.0, 2.0);
        accumulator.add(2.0, 3.0);
        accumulator.add(3.0, 4.0);

        // Act
        double populationCovariance = accumulator.populationCovariance();

        // Assert: Var(X) = ((1-2)^2 + (2-2)^2 + (3-2)^2) / 3 = 2/3.
        // Since y = x + 1, Cov(X, Y) = Cov(X, X+1) = Var(X).
        assertEquals(2.0 / 3.0, populationCovariance, TOLERANCE);
    }

    @Test(expected = IllegalStateException.class)
    public void sampleCovariance_withLessThanTwoPoints_throwsIllegalStateException() {
        // Arrange
        accumulator.add(1.0, 2.0);

        // Act
        accumulator.sampleCovariance();
    }

    @Test
    public void sampleCovariance_withMultiplePoints_isCalculatedCorrectly() {
        // Arrange: y = x + 1
        accumulator.add(1.0, 2.0);
        accumulator.add(2.0, 3.0);
        accumulator.add(3.0, 4.0);

        // Act
        double sampleCovariance = accumulator.sampleCovariance();

        // Assert: Population covariance is 2/3.
        // Sample covariance = (n / (n-1)) * population covariance = (3/2) * (2/3) = 1.
        assertEquals(1.0, sampleCovariance, TOLERANCE);
    }

    @Test(expected = IllegalStateException.class)
    public void pearsonsCorrelationCoefficient_withLessThanTwoPoints_throwsIllegalStateException() {
        accumulator.add(1.0, 1.0);
        accumulator.pearsonsCorrelationCoefficient();
    }

    @Test(expected = IllegalStateException.class)
    public void pearsonsCorrelationCoefficient_withZeroXVariance_throwsIllegalStateException() {
        accumulator.add(1.0, 1.0);
        accumulator.add(1.0, 2.0); // X values are constant
        accumulator.pearsonsCorrelationCoefficient();
    }

    @Test(expected = IllegalStateException.class)
    public void pearsonsCorrelationCoefficient_withZeroYVariance_throwsIllegalStateException() {
        accumulator.add(1.0, 1.0);
        accumulator.add(2.0, 1.0); // Y values are constant
        accumulator.pearsonsCorrelationCoefficient();
    }

    @Test
    public void pearsonsCorrelationCoefficient_withPerfectCorrelation_isOne() {
        accumulator.add(1.0, 1.0);
        accumulator.add(2.0, 2.0);
        assertEquals(1.0, accumulator.pearsonsCorrelationCoefficient(), TOLERANCE);
    }

    @Test
    public void pearsonsCorrelationCoefficient_withPerfectAntiCorrelation_isNegativeOne() {
        accumulator.add(1.0, 2.0);
        accumulator.add(2.0, 1.0);
        assertEquals(-1.0, accumulator.pearsonsCorrelationCoefficient(), TOLERANCE);
    }

    @Test
    public void pearsonsCorrelationCoefficient_withInfiniteValues_returnsNaN() {
        accumulator.add(Double.POSITIVE_INFINITY, 1.0);
        accumulator.add(2.0, Double.POSITIVE_INFINITY);
        assertTrue(Double.isNaN(accumulator.pearsonsCorrelationCoefficient()));
    }

    @Test(expected = IllegalStateException.class)
    public void leastSquaresFit_withLessThanTwoPoints_throwsIllegalStateException() {
        accumulator.add(1.0, 1.0);
        accumulator.leastSquaresFit();
    }

    @Test(expected = IllegalStateException.class)
    public void leastSquaresFit_withZeroTotalVariance_throwsIllegalStateException() {
        accumulator.add(1.0, 1.0);
        accumulator.add(1.0, 1.0);
        accumulator.leastSquaresFit();
    }

    @Test
    public void leastSquaresFit_withZeroXVariance_returnsVerticalLine() {
        // Arrange
        accumulator.add(1.0, 1.0);
        accumulator.add(1.0, 3.0); // x is constant, y varies

        // Act
        LinearTransformation fit = accumulator.leastSquaresFit();

        // Assert
        assertTrue(fit.isVertical());
        assertEquals(1.0, fit.transform(2.0), TOLERANCE); // x-intercept is 1.0
    }

    @Test
    public void leastSquaresFit_withZeroYVariance_returnsHorizontalLine() {
        // Arrange
        accumulator.add(1.0, 2.0);
        accumulator.add(3.0, 2.0); // y is constant, x varies

        // Act
        LinearTransformation fit = accumulator.leastSquaresFit();

        // Assert
        assertTrue(fit.isHorizontal());
        assertEquals(2.0, fit.transform(100.0), TOLERANCE); // y-intercept is 2.0
    }

    @Test
    public void leastSquaresFit_withPerfectlyCorrelatedData_isCorrect() {
        // Arrange: y = 2x + 1
        accumulator.add(1.0, 3.0);
        accumulator.add(2.0, 5.0);
        accumulator.add(3.0, 7.0);

        // Act
        LinearTransformation fit = accumulator.leastSquaresFit();

        // Assert
        assertEquals(2.0, fit.slope(), TOLERANCE);
        assertEquals(3.0, fit.transform(1.0), TOLERANCE); // y-intercept is 1, so f(1) = 2*1+1=3
    }

    @Test
    public void leastSquaresFit_withNaNValue_returnsNaNTransformation() {
        // Arrange
        accumulator.add(1.0, 1.0);
        accumulator.add(2.0, Double.NaN);

        // Act
        LinearTransformation fit = accumulator.leastSquaresFit();

        // Assert
        assertTrue(fit.isVertical() && fit.isVertical()); // This is how forNaN() is implemented
        assertTrue(Double.isNaN(fit.slope()));
    }
}