package com.google.common.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link PairedStatsAccumulator}.
 */
public class PairedStatsAccumulatorTest {

    @Test
    public void snapshot_isImmutable_andNotAffectedBySubsequentAccumulatorModifications() {
        // Arrange: Create an accumulator and add initial data.
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        accumulator.add(10.0, 20.0);
        accumulator.add(30.0, 40.0);

        // Act: Take a snapshot of the accumulator's state.
        PairedStats snapshot = accumulator.snapshot();

        // Assert: Verify the state at the time the snapshot was taken.
        assertEquals(2L, snapshot.count());
        assertEquals(2L, accumulator.count());

        // Act: Modify the original accumulator after taking the snapshot.
        accumulator.add(50.0, 60.0); // Add another pair
        accumulator.addAll(snapshot); // Add the snapshot's data back into the accumulator

        // Assert: The snapshot remains unchanged, while the accumulator's state has been updated.
        assertEquals(
                "Snapshot's count should remain unchanged after modifying the original accumulator.",
                2L,
                snapshot.count());

        assertEquals(
                "Accumulator's count should reflect all additions.",
                5L, // 2 initial pairs + 1 new pair + 2 pairs from the snapshot
                accumulator.count());
    }
}