package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.mock;

/**
 * Tests for {@link ParallelScatterZipCreator}.
 * This test focuses on the creation and submission of compression tasks.
 */
public class ParallelScatterZipCreatorTest {

    /**
     * Verifies that a compression task (Callable) can be created from a
     * ZipArchiveEntryRequestSupplier and submitted for execution without
     * causing an immediate exception.
     */
    @Test
    public void createAndSubmitCallableFromRequestSupplierSucceeds() {
        // Arrange: Set up the parallel zip creator with a single-threaded executor.
        // The ParallelScatterZipCreator takes ownership and will shut down the executor.
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator(executorService);

        // Create a mock supplier for zip archive entry requests.
        final ZipArchiveEntryRequestSupplier requestSupplier = mock(ZipArchiveEntryRequestSupplier.class);

        // Act: Create a callable for the compression task and submit it.
        final Callable<ScatterZipOutputStream> compressionTask = zipCreator.createCallable(requestSupplier);
        zipCreator.submitStreamAwareCallable(compressionTask);

        // Assert: The test's purpose is to ensure the above calls execute without throwing
        // an exception. A more complex test would be needed to verify the task's result.
        // No explicit assert is needed here; the test passes if no exception is thrown.

        // It's good practice to ensure the executor is shut down, although the
        // ParallelScatterZipCreator's writeTo() method is designed to do this.
        // Since writeTo() is not called, we shut it down to prevent resource leaks.
        executorService.shutdown();
    }
}