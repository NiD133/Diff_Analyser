package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.zip.DefaultBackingStoreSupplier;
import org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator;
import org.apache.commons.compress.archivers.zip.ScatterStatistics;
import org.apache.commons.compress.archivers.zip.ScatterZipOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntryRequestSupplier;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.parallel.InputStreamSupplier;
import org.apache.commons.compress.parallel.ScatterGatherBackingStoreSupplier;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.net.MockURI;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true) 
public class ParallelScatterZipCreator_ESTest extends ParallelScatterZipCreator_ESTest_scaffolding {

    // Test Statistics and Basic Operations
    
    @Test(timeout = 4000)
    public void testStatisticsAreUpdatedAfterWriteTo() throws Throwable {
        // Given: A ParallelScatterZipCreator with a thread pool
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(14);
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator(executor);
        
        // When: Getting initial statistics
        ScatterStatistics initialStats = creator.getStatisticsMessage();
        assertEquals(-1392409281320L, initialStats.getCompressionElapsed());
        
        // And: Writing to null output stream (edge case)
        creator.writeTo(null);
        
        // Then: Statistics should be reset/updated
        ScatterStatistics finalStats = creator.getStatisticsMessage();
        assertEquals(0L, finalStats.getCompressionElapsed());
        assertEquals(0L, finalStats.getMergingElapsed());
    }

    // Test Constructor Validation
    
    @Test(timeout = 4000)
    public void testConstructorRejectsInvalidCompressionLevel_Negative() throws Throwable {
        // Given: A thread pool and backing store supplier
        ThreadPoolExecutor.DiscardOldestPolicy policy = new ThreadPoolExecutor.DiscardOldestPolicy();
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(0, policy);
        executor.prestartAllCoreThreads();
        
        File tempFile = MockFile.createTempFile("test", "zip");
        MockFile mockFile = new MockFile(tempFile, "");
        Path path = mockFile.toPath();
        DefaultBackingStoreSupplier backingStore = new DefaultBackingStoreSupplier(path);
        
        // When/Then: Creating with invalid compression level should fail
        try {
            new ParallelScatterZipCreator(executor, backingStore, -1088);
            fail("Expected IllegalArgumentException for invalid compression level");
        } catch(IllegalArgumentException e) {
            assertEquals("Compression level is expected between -1~9", e.getMessage());
        }
    }
    
    @Test(timeout = 4000)
    public void testConstructorRejectsInvalidCompressionLevel_TooHigh() throws Throwable {
        // Given: A ForkJoinPool and backing store supplier
        ForkJoinPool pool = new ForkJoinPool();
        MockFile mockFile = new MockFile("");
        Path path = mockFile.toPath();
        DefaultBackingStoreSupplier backingStore = new DefaultBackingStoreSupplier(path);
        
        // When/Then: Creating with compression level too high should fail
        try {
            new ParallelScatterZipCreator(pool, backingStore, 1394);
            fail("Expected IllegalArgumentException for compression level > 9");
        } catch(IllegalArgumentException e) {
            assertEquals("Compression level is expected between -1~9", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithValidCompressionLevel() throws Throwable {
        // Given: Valid parameters
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(14);
        File tempFile = MockFile.createTempFile("test", "");
        MockFile mockFile = new MockFile(tempFile, " bytes.");
        Path path = mockFile.toPath();
        DefaultBackingStoreSupplier backingStore = new DefaultBackingStoreSupplier(path);
        
        // When/Then: Creating with valid compression level should succeed
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator(executor, backingStore, 9);
        assertNotNull(creator);
        
        // Mock input stream supplier for potential future use
        InputStreamSupplier mockSupplier = mock(InputStreamSupplier.class, new ViolatedAssumptionAnswer());
        assertNotNull(mockSupplier);
    }

    // Test Callable Creation and Submission
    
    @Test(timeout = 4000)
    public void testCreateAndSubmitCallableWithRequestSupplier() throws Throwable {
        // Given: A ParallelScatterZipCreator
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(14);
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator(executor);
        
        // And: A mock request supplier
        ZipArchiveEntryRequestSupplier mockSupplier = 
            mock(ZipArchiveEntryRequestSupplier.class, new ViolatedAssumptionAnswer());
        
        // When: Creating and submitting a callable
        Callable<ScatterZipOutputStream> callable = creator.createCallable(mockSupplier);
        creator.submitStreamAwareCallable(callable);
        
        // Then: No exception should be thrown
        assertNotNull(callable);
    }

    @Test(timeout = 4000)
    public void testConstructorWithNullBackingStore() throws Throwable {
        // Given: A ForkJoinPool
        ForkJoinPool pool = new ForkJoinPool();
        
        // When/Then: Creating with null backing store should succeed
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator(pool, null);
        assertNotNull(creator);
    }

    // Test Error Conditions
    
    @Test(timeout = 4000)
    public void testWriteToWithNullExecutorServiceFails() throws Throwable {
        // Given: A creator with null executor service
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator((ExecutorService) null);
        
        // When/Then: Writing should fail with NullPointerException
        try {
            creator.writeTo(null);
            fail("Expected NullPointerException when executor service is null");
        } catch(NullPointerException e) {
            // Expected - no message validation needed as it's implementation detail
        }
    }

    @Test(timeout = 4000)
    public void testCreateCallableWithNullZipEntryFails() throws Throwable {
        // Given: A default creator
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator();
        InputStreamSupplier mockSupplier = mock(InputStreamSupplier.class, new ViolatedAssumptionAnswer());
        
        // When/Then: Creating callable with null entry should fail
        try {
            creator.createCallable((ZipArchiveEntry) null, mockSupplier);
            fail("Expected NullPointerException for null ZipArchiveEntry");
        } catch(NullPointerException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void testAddArchiveEntryWithNullZipEntryFails() throws Throwable {
        // Given: A default creator
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator();
        InputStreamSupplier mockSupplier = mock(InputStreamSupplier.class, new ViolatedAssumptionAnswer());
        
        // When/Then: Adding null entry should fail
        try {
            creator.addArchiveEntry((ZipArchiveEntry) null, mockSupplier);
            fail("Expected NullPointerException for null ZipArchiveEntry");
        } catch(NullPointerException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void testCreateCallableWithEntryMissingCompressionMethodFails() throws Throwable {
        // Given: A creator and an entry without compression method set
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(14);
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator(executor);
        ZipArchiveEntry entryWithoutMethod = new ZipArchiveEntry("test-entry");
        InputStreamSupplier mockSupplier = mock(InputStreamSupplier.class, new ViolatedAssumptionAnswer());
        
        // When/Then: Creating callable should fail due to missing method
        try {
            creator.createCallable(entryWithoutMethod, mockSupplier);
            fail("Expected IllegalArgumentException for entry without compression method");
        } catch(IllegalArgumentException e) {
            assertEquals("Method must be set on zipArchiveEntry: test-entry", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testSubmitNullCallableFails() throws Throwable {
        // Given: A default creator
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator();
        
        // When/Then: Submitting null callable should fail
        try {
            creator.submitStreamAwareCallable(null);
            fail("Expected NullPointerException for null callable");
        } catch(NullPointerException e) {
            // Expected
        }
    }

    @Test(timeout = 4000)
    public void testZipArchiveOutputStreamCreationFailure() throws Throwable {
        // Given: A creator with specific configuration
        ForkJoinPool pool = ForkJoinPool.commonPool();
        URI fileUri = MockURI.aFileURI;
        MockFile mockFile = new MockFile(fileUri);
        mockFile.renameTo(mockFile); // Simulate file operation
        Path path = mockFile.toPath();
        DefaultBackingStoreSupplier backingStore = new DefaultBackingStoreSupplier(path);
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator(pool, backingStore, 0);
        
        // When/Then: Creating ZipArchiveOutputStream should fail due to missing class
        try {
            new ZipArchiveOutputStream(mockFile, 0);
            fail("Expected NoClassDefFoundError");
        } catch(NoClassDefFoundError e) {
            assertEquals("Could not initialize class org.apache.commons.compress.archivers.zip.ZipEncodingHelper", 
                        e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testAddJarArchiveEntryWithoutCompressionMethodFails() throws Throwable {
        // Given: A default creator and JarArchiveEntry without method
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator();
        JarArchiveEntry jarEntry = new JarArchiveEntry("test-jar-entry");
        
        // When/Then: Adding JAR entry without method should fail
        try {
            creator.addArchiveEntry(jarEntry, null);
            fail("Expected IllegalArgumentException for JAR entry without compression method");
        } catch(IllegalArgumentException e) {
            assertEquals("Method must be set on zipArchiveEntry: test-jar-entry", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testWriteToWithNullRequestSupplierCausesExecutionException() throws Throwable {
        // This test runs in a separate thread to avoid timeout issues
        Future<?> future = executor.submit(new Runnable() { 
            @Override 
            public void run() { 
                try {
                    // Given: A creator with null request supplier added
                    ParallelScatterZipCreator creator = new ParallelScatterZipCreator();
                    creator.addArchiveEntry((ZipArchiveEntryRequestSupplier) null);
                    
                    // When/Then: Writing should fail with ExecutionException
                    try { 
                        creator.writeTo(null);
                        fail("Expected ExecutionException");
                    } catch(ExecutionException e) {
                        assertEquals("java.lang.SecurityException: Unable to create temporary file or directory", 
                                   e.getMessage());
                    }
                } catch(Throwable t) {
                    // Handle any declared exceptions
                }
            } 
        });
        future.get(4000, TimeUnit.MILLISECONDS);
    }

    @Test(timeout = 4000)
    public void testSubmitNullCallableAndCreateZipEntry() throws Throwable {
        // Given: A creator with ForkJoinPool
        ForkJoinPool pool = ForkJoinPool.commonPool();
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator(pool);
        
        // When: Submitting null callable (edge case)
        creator.submit((Callable<?>) null);
        
        // And: Creating a new ZipArchiveEntry
        ZipArchiveEntry entry = new ZipArchiveEntry();
        
        // Then: Entry should have expected default values
        assertEquals(-1L, entry.getLocalHeaderOffset());
    }
}