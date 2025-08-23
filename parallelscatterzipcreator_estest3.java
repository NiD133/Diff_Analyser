package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.Deflater;

import org.apache.commons.compress.parallel.ScatterGatherBackingStoreSupplier;
import org.evosuite.runtime.mock.java.io.MockFile;

/**
 * Test suite for {@link ParallelScatterZipCreator}.
 */
public class ParallelScatterZipCreator_ESTestTest3 extends ParallelScatterZipCreator_ESTest_scaffolding {

    /**
     * Verifies that the ParallelScatterZipCreator can be successfully instantiated
     * with a custom ExecutorService, a backing store supplier, and a specific
     * compression level.
     */
    @Test(timeout = 4000)
    public void shouldConstructSuccessfullyWithCustomExecutorAndBackingStore() throws Throwable {
        // Arrange: Set up the necessary components for the creator.
        
        // 1. An executor service for parallel processing.
        final ExecutorService executorService = Executors.newFixedThreadPool(2);

        // 2. A supplier for the backing store, which requires a temporary directory.
        final File tempDir = MockFile.createTempFile("parallel-scatter-test", ".dir");
        tempDir.delete(); // Delete the file to replace it with a directory
        tempDir.mkdir();
        final Path tempPath = tempDir.toPath();
        final ScatterGatherBackingStoreSupplier backingStoreSupplier = new DefaultBackingStoreSupplier(tempPath);

        // 3. A specific compression level. Using the constant is more readable than a magic number.
        final int compressionLevel = Deflater.BEST_COMPRESSION; // Equivalent to 9

        // Act: Instantiate the class under test.
        final ParallelScatterZipCreator parallelScatterZipCreator =
            new ParallelScatterZipCreator(executorService, backingStoreSupplier, compressionLevel);

        // Assert: The primary goal is to ensure construction succeeds without exceptions.
        // A not-null check makes this intent explicit.
        assertNotNull("The creator should be instantiated successfully.", parallelScatterZipCreator);
    }
}