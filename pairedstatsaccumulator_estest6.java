package com.google.common.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link PairedStatsAccumulator}.
 */
@RunWith(JUnit4.class)
public class PairedStatsAccumulatorTest {

    // A small tolerance for floating-point comparisons.
    private static final double TOLERANCE = 1e-9;

    @Test
    public void populationCovariance_afterAddingAllFromPairedStats_isCalculatedCorrectly() {
        // Arrange: Create a PairedStats object with known statistical properties
        // from a simple dataset: (1, 2) and (3, 4).
        //
        // For this data:
        //   - count = 2
        //   - x-mean = (1 + 3) / 2 = 2.0
        //   - y-mean = (2 + 4) / 2 = 3.0
        //   - Sum of products of deltas = (1-2)*(2-3) + (3-2)*(4-3) = (-1)*(-1) + (1)*(1) = 2.0
        //   - Expected population covariance = (Sum of products of deltas) / count = 2.0 / 2 = 1.0
        PairedStatsAccumulator sourceAccumulator = new PairedStatsAccumulator();
        sourceAccumulator.add(1.0, 2.0);
        sourceAccumulator.add(3.0, 4.0);
        PairedStats statsToAdd = sourceAccumulator.snapshot();

        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        double expectedCovariance = 1.0;

        // Act: Add the pre-computed stats to the accumulator under test.
        accumulator.addAll(statsToAdd);
        double actualCovariance = accumulator.populationCovariance();

        // Assert: The calculated covariance should match the expected value.
        assertEquals(expectedCovariance, actualCovariance, TOLERANCE);
    }
}