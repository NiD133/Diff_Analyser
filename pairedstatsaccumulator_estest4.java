package com.google.common.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulatorTest {

    // A small tolerance for floating-point comparisons.
    private static final double TOLERANCE = 1e-8;

    @Test
    public void sampleCovariance_withNegativeAndPositiveValues_isCalculatedCorrectly() {
        // Arrange
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        accumulator.add(-1.0, -1.0);
        accumulator.add(-1313.1264, 1.0);
        accumulator.add(-1.0, -1313.1264);

        // The sample covariance is calculated using the formula:
        // Cov(x, y) = Σ [ (x_i - mean_x) * (y_i - mean_y) ] / (n - 1)
        //
        // 1. Data points (n=3):
        //    P1 = (-1.0, -1.0)
        //    P2 = (-1313.1264, 1.0)
        //    P3 = (-1.0, -1313.1264)
        //
        // 2. Calculate means:
        //    mean_x = (-1.0 - 1313.1264 - 1.0) / 3 = -438.375466...
        //    mean_y = (-1.0 + 1.0 - 1313.1264) / 3 = -437.7088
        //
        // 3. Calculate sum of products of deviations:
        //    Term1 = ( -1.0 - mean_x) * ( -1.0 - mean_y)         =  190998.599488
        //    Term2 = (-1313.1264 - mean_x) * (  1.0 - mean_y)         = -383799.799370...
        //    Term3 = ( -1.0 - mean_x) * (-1313.1264 - mean_y)         = -382899.499317...
        //    Sum   = Term1 + Term2 + Term3                         = -575700.6992
        //
        // 4. Divide by (n - 1) = 2:
        //    Sample Covariance = -575700.6992 / 2 = -287850.3496
        double expectedSampleCovariance = -287850.3496;

        // Act
        double actualSampleCovariance = accumulator.sampleCovariance();

        // Assert
        assertEquals(expectedSampleCovariance, actualSampleCovariance, TOLERANCE);
    }
}