/*
 * Refactored test suite for ParallelScatterZipCreator
 * Focus: Improved readability and maintainability
 */
package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.*;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.zip.*;
import org.apache.commons.compress.parallel.InputStreamSupplier;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class ParallelScatterZipCreator_ESTest {

    @Test(timeout = 4000)
    public void testWriteToResetsStatistics() throws Exception {
        // Setup: Create executor and scatter creator
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(14);
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator(executor);
        
        // Verify initial statistics
        ScatterStatistics initialStats = creator.getStatisticsMessage();
        assertEquals("Initial compression time should be negative (not started)", 
                     -1, initialStats.getCompressionElapsed());
        
        // Action: Perform write operation (null stream forces exception but stats still update)
        try {
            creator.writeTo(null);
        } catch (NullPointerException expected) {
            // Expected during test execution
        }
        
        // Verify: Statistics should reset after write operation
        ScatterStatistics finalStats = creator.getStatisticsMessage();
        assertEquals("Compression time should reset after write", 
                     0, finalStats.getCompressionElapsed());
        assertEquals("Merging time should reset after write", 
                     0, finalStats.getMergingElapsed());
    }

    @Test(timeout = 4000)
    public void testConstructorWithInvalidCompressionLevelThrows() {
        // Setup: Create executor with invalid compression level
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.prestartAllCoreThreads();
        
        File tempFile = MockFile.createTempFile("invalidCompression", ".tmp");
        DefaultBackingStoreSupplier backingStore = 
            new DefaultBackingStoreSupplier(tempFile.toPath());
        
        try {
            // Action: Attempt to create with invalid compression level
            new ParallelScatterZipCreator(executor, backingStore, -1088);
            fail("Should throw IllegalArgumentException for invalid compression level");
        } catch (IllegalArgumentException e) {
            // Verify: Exception contains expected message
            assertTrue("Exception message should mention compression range",
                       e.getMessage().contains("Compression level is expected between -1~9"));
        }
    }

    @Test(timeout = 4000)
    public void testWriteToWithNullExecutorServiceThrowsNPE() {
        // Setup: Create creator with null executor
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator(null);
        
        try {
            // Action: Attempt write operation
            creator.writeTo(null);
            fail("Should throw NullPointerException for null executor");
        } catch (NullPointerException e) {
            // Verify: NPE was thrown
            assertNotNull("Exception should have message", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testCreateCallableWithNullEntryThrowsNPE() {
        // Setup: Create basic scatter creator
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator();
        InputStreamSupplier mockSupplier = mock(InputStreamSupplier.class);
        
        try {
            // Action: Attempt to create callable with null entry
            creator.createCallable((ZipArchiveEntry) null, mockSupplier);
            fail("Should throw NullPointerException for null entry");
        } catch (NullPointerException e) {
            // Verify: NPE was thrown
            assertNotNull("Exception should have message", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testAddArchiveEntryWithNullEntryThrowsNPE() {
        // Setup: Create basic scatter creator
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator();
        InputStreamSupplier mockSupplier = mock(InputStreamSupplier.class);
        
        try {
            // Action: Attempt to add null entry
            creator.addArchiveEntry((ZipArchiveEntry) null, mockSupplier);
            fail("Should throw NullPointerException for null entry");
        } catch (NullPointerException e) {
            // Verify: NPE was thrown
            assertNotNull("Exception should have message", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testCreateCallableWithEntryWithoutMethodThrowsException() {
        // Setup: Create executor and scatter creator
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(14);
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator(executor);
        
        // Create entry without setting compression method
        ZipArchiveEntry entry = new ZipArchiveEntry("invalidEntry");
        InputStreamSupplier mockSupplier = mock(InputStreamSupplier.class);
        
        try {
            // Action: Attempt to create callable
            creator.createCallable(entry, mockSupplier);
            fail("Should throw IllegalArgumentException for missing method");
        } catch (IllegalArgumentException e) {
            // Verify: Exception contains entry name
            assertTrue("Exception message should contain entry name",
                       e.getMessage().contains("Method must be set"));
            assertTrue("Exception message should contain entry name",
                       e.getMessage().contains("invalidEntry"));
        }
    }

    @Test(timeout = 4000)
    public void testAddArchiveEntryWithEntryWithoutMethodThrowsException() {
        // Setup: Create basic scatter creator
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator();
        
        // Create entry without setting compression method
        JarArchiveEntry entry = new JarArchiveEntry("invalidJarEntry");
        
        try {
            // Action: Attempt to add entry
            creator.addArchiveEntry(entry, null);
            fail("Should throw IllegalArgumentException for missing method");
        } catch (IllegalArgumentException e) {
            // Verify: Exception contains entry name
            assertTrue("Exception message should contain entry name",
                       e.getMessage().contains("Method must be set"));
            assertTrue("Exception message should contain entry name",
                       e.getMessage().contains("invalidJarEntry"));
        }
    }

    @Test(timeout = 4000)
    public void testSubmitStreamAwareCallableWithNullThrowsNPE() {
        // Setup: Create basic scatter creator
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator();
        
        try {
            // Action: Attempt to submit null callable
            creator.submitStreamAwareCallable(null);
            fail("Should throw NullPointerException for null callable");
        } catch (NullPointerException e) {
            // Verify: NPE was thrown
            assertNotNull("Exception should have message", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testAddNullArchiveEntryRequestThenWriteToThrowsException() throws Exception {
        // Setup: Create scatter creator
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator();
        
        // Add null entry request
        creator.addArchiveEntry((ZipArchiveEntryRequestSupplier) null);
        
        try {
            // Action: Attempt write operation
            creator.writeTo(null);
            fail("Should throw ExecutionException for null entry request");
        } catch (ExecutionException e) {
            // Verify: Root cause is SecurityException
            Throwable rootCause = e.getCause();
            assertTrue("Root cause should be SecurityException", 
                       rootCause instanceof SecurityException);
            assertTrue("Exception should mention temporary file creation",
                       rootCause.getMessage().contains("temporary file"));
        }
    }
}