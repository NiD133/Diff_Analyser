package com.google.common.math;

import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests for {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulatorTest {

    @Test
    public void add_afterAddingPairedStatsWithMismatchedCounts_throwsIllegalStateException() {
        // ARRANGE: Create an accumulator and put it into an inconsistent state.
        // This is done by adding a PairedStats object where the x-stats and y-stats
        // have different counts (1 and 0, respectively). The addAll() method
        // allows this when the accumulator is empty, leading to an invalid state.
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        Stats xStatsWithOneValue = Stats.of(1.0);
        Stats emptyYStats = Stats.of(); // An empty Stats object has a count of 0.
        PairedStats inconsistentPairedStats = new PairedStats(xStatsWithOneValue, emptyYStats, 0.0);
        
        accumulator.addAll(inconsistentPairedStats);

        // ACT & ASSERT: Attempting to add a new pair should fail. The add() method
        // needs to calculate the mean of the y-values, which is not possible for an
        // empty y-stats collection, resulting in an IllegalStateException.
        try {
            accumulator.add(2.0, 2.0);
            fail("Expected an IllegalStateException to be thrown due to inconsistent state");
        } catch (IllegalStateException expected) {
            // This is the expected behavior.
        }
    }
}