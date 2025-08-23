package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link ParallelScatterZipCreator}.
 * This class demonstrates how to refactor a generated test for clarity and correctness.
 */
public class ParallelScatterZipCreatorTest {

    @Test(timeout = 4000)
    public void statisticsShouldBeUpdatedAfterWritingArchive() throws Exception {
        // Arrange: Set up the ParallelScatterZipCreator with an executor service.
        // One thread is sufficient for this test case.
        ScheduledThreadPoolExecutor executorService = new ScheduledThreadPoolExecutor(1);
        ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator(executorService);

        // Act:
        // 1. Get statistics before the zip creation process is finalized.
        ScatterStatistics initialStatistics = zipCreator.getStatisticsMessage();

        // 2. Finalize the archive creation by calling writeTo.
        //    No entries have been added, so the process is trivial.
        //    The method is expected to handle a null output stream in this scenario.
        zipCreator.writeTo(null);

        // 3. Get statistics again after the process has completed.
        ScatterStatistics finalStatistics = zipCreator.getStatisticsMessage();

        // Assert: Verify the state of the statistics at each stage.

        // Before writeTo() is called, compression is considered ongoing.
        // The elapsed time is calculated from the start time until the moment of the call.
        assertTrue("Initial compression time should be non-negative.",
            initialStatistics.getCompressionElapsed() >= 0);
        // Merging hasn't started yet, so its elapsed time should be zero.
        assertEquals("Initial merging time should be 0.",
            0L, initialStatistics.getMergingElapsed());

        // After writeTo() completes, the timestamps for compression and merging are finalized.
        // Both values should be non-negative.
        assertTrue("Final compression time should be non-negative.",
            finalStatistics.getCompressionElapsed() >= 0);
        assertTrue("Final merging time should be non-negative.",
            finalStatistics.getMergingElapsed() >= 0);

        // The final, completed compression time should be at least as long as the
        // in-progress duration measured before calling writeTo().
        assertTrue("Final compression time should be greater than or equal to the initial time.",
            finalStatistics.getCompressionElapsed() >= initialStatistics.getCompressionElapsed());
    }
}