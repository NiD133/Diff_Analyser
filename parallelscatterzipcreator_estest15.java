package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for {@link ParallelScatterZipCreator}.
 */
public class ParallelScatterZipCreatorTest {

    /**
     * Tests that calling writeTo() fails with an ExecutionException
     * if a null Callable was previously submitted. The underlying cause
     * of the failure should be a NullPointerException.
     */
    @Test
    public void writeToShouldThrowExecutionExceptionWhenNullCallableWasSubmitted() {
        // Arrange: Create a scatter creator with a dedicated executor service.
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        ParallelScatterZipCreator scatterZipCreator = new ParallelScatterZipCreator(executorService);

        // Submit a null callable. The submit() method itself doesn't throw,
        // as the task is executed asynchronously.
        Callable<Object> nullCallable = null;
        scatterZipCreator.submit(nullCallable);

        // Act & Assert: Verify that writeTo() throws the expected exception.
        // The exception from the background task should be wrapped in an ExecutionException.
        ExecutionException thrown = assertThrows(ExecutionException.class, () -> {
            // A dummy output stream is sufficient for this test.
            try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(new ByteArrayOutputStream())) {
                scatterZipCreator.writeTo(zos);
            }
        });

        // Further Assert: Check the cause of the exception for correctness.
        Throwable cause = thrown.getCause();
        assertNotNull("ExecutionException should have a cause.", cause);
        assertTrue("The cause of the ExecutionException should be a NullPointerException.",
                cause instanceof NullPointerException);
        
        // No explicit executor shutdown is needed, as the writeTo() method
        // guarantees it will be shut down, even if an exception occurs.
    }
}