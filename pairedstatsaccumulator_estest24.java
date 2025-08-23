package com.google.common.math;

import org.junit.Test;

/**
 * This test class verifies the behavior of the {@link PairedStats} class.
 * It was refactored from an auto-generated test that was originally, and misleadingly,
 * targeting {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulator_ESTestTest24 {

    /**
     * Verifies that the {@link PairedStats} constructor throws an {@link IllegalArgumentException}
     * when the provided x-stats and y-stats objects have different counts.
     */
    @Test(expected = IllegalArgumentException.class)
    public void pairedStatsConstructor_throwsIllegalArgumentException_whenXAndYStatsCountsDiffer() {
        // Arrange: Create two Stats objects with a different number of values.
        // The PairedStats class requires the count of x and y values to be identical.
        Stats xStatsWithOneValue = Stats.of(154.3); // This object has a count of 1.
        Stats yStatsWithNoValues = Stats.of();      // This object has a count of 0.

        // Act: Attempt to create a PairedStats object with the mismatched Stats.
        // This is expected to throw an IllegalArgumentException due to the count mismatch,
        // as enforced by a precondition check in the constructor.
        new PairedStats(xStatsWithOneValue, yStatsWithNoValues, 0.0);

        // Assert: The test passes if the expected exception is thrown.
    }
}