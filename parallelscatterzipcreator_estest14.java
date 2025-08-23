package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for {@link ParallelScatterZipCreator} focusing on exception handling.
 */
public class ParallelScatterZipCreatorTest {

    /**
     * Tests that writeTo() propagates exceptions from worker threads.
     * If a submitted task fails (e.g., due to a NullPointerException),
     * the call to writeTo() should fail with an ExecutionException that
     * wraps the original exception.
     */
    @Test(timeout = 4000)
    public void writeToShouldThrowExecutionExceptionWhenWorkerTaskFails() throws Exception {
        // Arrange: Create a ParallelScatterZipCreator, which uses an internal thread pool.
        final ParallelScatterZipCreator scatterZipCreator = new ParallelScatterZipCreator();

        // Act: Submit a task that is guaranteed to fail by providing a null supplier.
        // This will cause a NullPointerException to be thrown inside the worker thread.
        scatterZipCreator.addArchiveEntry((ZipArchiveEntryRequestSupplier) null);

        // Assert: The writeTo() method should rethrow the failure from the worker thread,
        // wrapped in an ExecutionException.
        try {
            // The ZipArchiveOutputStream is not used before the exception is thrown, so it can be null for this test.
            scatterZipCreator.writeTo(null);
            fail("Expected ExecutionException was not thrown.");
        } catch (final ExecutionException e) {
            // Verify that the cause of the execution failure is the expected NullPointerException.
            final Throwable cause = e.getCause();
            assertNotNull("ExecutionException should have a cause.", cause);
            assertTrue("The cause of the ExecutionException should be a NullPointerException.",
                    cause instanceof NullPointerException);
        }
    }
}