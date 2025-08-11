package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.parallel.InputStreamSupplier;
import org.apache.commons.compress.parallel.ScatterGatherBackingStoreSupplier;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for the {@link ParallelScatterZipCreator} class.
 */
public class ParallelScatterZipCreatorTest {

    @Test
    public void constructorWithNullBackingStoreSupplierSucceeds() {
        // Arrange
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // Act & Assert (does not throw)
        try {
            new ParallelScatterZipCreator(executorService, null);
        } finally {
            executorService.shutdown();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsExceptionForTooLowCompressionLevel() {
        // Arrange
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        ScatterGatherBackingStoreSupplier supplier = mock(ScatterGatherBackingStoreSupplier.class);
        int invalidLevel = -2; // Deflater.DEFAULT_COMPRESSION is -1, valid levels are -1 and 0-9.

        // Act & Assert
        try {
            new ParallelScatterZipCreator(executorService, supplier, invalidLevel);
        } finally {
            executorService.shutdown();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsExceptionForTooHighCompressionLevel() {
        // Arrange
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        ScatterGatherBackingStoreSupplier supplier = mock(ScatterGatherBackingStoreSupplier.class);
        int invalidLevel = 10; // Valid levels are -1 and 0-9.

        // Act & Assert
        try {
            new ParallelScatterZipCreator(executorService, supplier, invalidLevel);
        } finally {
            executorService.shutdown();
        }
    }

    @Test(expected = NullPointerException.class)
    public void addArchiveEntryThrowsExceptionForNullEntry() {
        // Arrange
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator();
        InputStreamSupplier supplier = mock(InputStreamSupplier.class);

        // Act
        creator.addArchiveEntry((ZipArchiveEntry) null, supplier);
    }

    @Test
    public void addArchiveEntryThrowsExceptionIfEntryMethodNotSet() {
        // Arrange
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator();
        // An entry must have a compression method set before being added.
        ZipArchiveEntry entry = new ZipArchiveEntry("entry-without-method");
        InputStreamSupplier supplier = mock(InputStreamSupplier.class);

        // Act & Assert
        try {
            creator.addArchiveEntry(entry, supplier);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Method must be set on zipArchiveEntry: entry-without-method", e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void createCallableThrowsExceptionForNullEntry() {
        // Arrange
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator();
        InputStreamSupplier supplier = mock(InputStreamSupplier.class);

        // Act
        creator.createCallable((ZipArchiveEntry) null, supplier);
    }

    @Test
    public void createCallableThrowsExceptionIfEntryMethodNotSet() {
        // Arrange
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator();
        // An entry must have a compression method set.
        ZipArchiveEntry entry = new ZipArchiveEntry("entry-without-method");
        InputStreamSupplier supplier = mock(InputStreamSupplier.class);

        // Act & Assert
        try {
            creator.createCallable(entry, supplier);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Method must be set on zipArchiveEntry: entry-without-method", e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void submitThrowsExceptionForNullCallable() {
        // Arrange
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator();

        // Act
        creator.submit(null);
    }

    @Test(expected = NullPointerException.class)
    public void submitStreamAwareCallableThrowsExceptionForNullCallable() {
        // Arrange
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator();

        // Act
        creator.submitStreamAwareCallable(null);
    }

    @Test
    public void submitStreamAwareCallableAcceptsValidCallable() {
        // Arrange
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator();
        ZipArchiveEntryRequestSupplier requestSupplier = mock(ZipArchiveEntryRequestSupplier.class);
        Callable<ScatterZipOutputStream> callable = creator.createCallable(requestSupplier);

        // Act & Assert (does not throw)
        creator.submitStreamAwareCallable(callable);
        // Success is indicated by no exception being thrown.
    }

    @Test(expected = NullPointerException.class)
    public void writeToThrowsExceptionWhenConstructedWithNullExecutor() throws Exception {
        // Arrange
        // The public API allows passing a null ExecutorService.
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator(null);
        ZipArchiveOutputStream zaos = mock(ZipArchiveOutputStream.class);

        // Act: This should throw an NPE because it tries to shut down a null executor.
        creator.writeTo(zaos);
    }

    @Test
    public void writeToPropagatesExceptionFromFailingTask() throws Exception {
        // Arrange
        ZipArchiveOutputStream zaos = mock(ZipArchiveOutputStream.class);
        ExecutorService service = Executors.newSingleThreadExecutor();
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator(service);

        // Create a supplier that will throw an exception when accessed by the worker thread.
        final IOException simulatedException = new IOException("Simulated I/O failure");
        InputStreamSupplier failingSupplier = () -> {
            throw simulatedException;
        };

        ZipArchiveEntry entry = new ZipArchiveEntry("failing-entry");
        entry.setMethod(ZipEntry.DEFLATED);

        // Act: Add the failing entry. This submits the task, but the exception is deferred.
        creator.addArchiveEntry(entry, failingSupplier);

        // Assert: The exception is thrown when writeTo() waits for the future to complete.
        try {
            creator.writeTo(zaos);
            fail("Expected ExecutionException to be thrown");
        } catch (ExecutionException e) {
            // The ExecutionException should wrap our original simulated exception.
            assertSame("The cause of the ExecutionException should be the simulated IOException",
                simulatedException, e.getCause());
        } finally {
            // The creator shuts down the service; we await termination to ensure clean exit.
            service.awaitTermination(1, TimeUnit.SECONDS);
        }
    }

    @Test
    public void getStatisticsMessageReturnsUpdatedStatsAfterWriting() throws Exception {
        // Arrange
        ZipArchiveOutputStream zaos = mock(ZipArchiveOutputStream.class);
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator();

        // Act: Write an empty creator to the output stream.
        creator.writeTo(zaos);
        ScatterStatistics stats = creator.getStatisticsMessage();

        // Assert: After writing, elapsed times should be calculated.
        // We assert that the values are non-negative, as exact times can vary.
        assertTrue("Compression elapsed time should be non-negative", stats.getCompressionElapsed() >= 0);
        assertTrue("Merging elapsed time should be non-negative", stats.getMergingElapsed() >= 0);
    }
}