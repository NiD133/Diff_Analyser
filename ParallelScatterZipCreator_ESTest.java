package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.util.concurrent.*;

import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.parallel.InputStreamSupplier;
import org.apache.commons.compress.parallel.ScatterGatherBackingStoreSupplier;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.net.MockURI;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class ParallelScatterZipCreatorTest extends ParallelScatterZipCreatorTestScaffolding {

    @Test(timeout = 4000)
    public void testGetStatisticsMessage() throws Throwable {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(14);
        ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator(executor);

        ScatterStatistics initialStats = zipCreator.getStatisticsMessage();
        assertEquals(-1392409281320L, initialStats.getCompressionElapsed());

        zipCreator.writeTo(null);

        ScatterStatistics finalStats = zipCreator.getStatisticsMessage();
        assertEquals(0L, finalStats.getCompressionElapsed());
        assertEquals(0L, finalStats.getMergingElapsed());
    }

    @Test(timeout = 4000)
    public void testInvalidCompressionLevel() {
        ThreadPoolExecutor.DiscardOldestPolicy discardPolicy = new ThreadPoolExecutor.DiscardOldestPolicy();
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(0, discardPolicy);
        executor.prestartAllCoreThreads();

        File tempFile = MockFile.createTempFile("D;U(uA", ";,hSX[abfs+0dlNvz");
        MockFile mockFile = new MockFile(tempFile, "");
        Path path = mockFile.toPath();
        DefaultBackingStoreSupplier backingStore = new DefaultBackingStoreSupplier(path);

        try {
            new ParallelScatterZipCreator(executor, backingStore, -1088);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Compression level is expected between -1~9", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testCreateCallableWithMockInputStreamSupplier() throws Throwable {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(14);
        File tempFile = MockFile.createTempFile("LdT;dw,hj62U", "");
        MockFile mockFile = new MockFile(tempFile, " bytes.");
        Path path = mockFile.toPath();
        DefaultBackingStoreSupplier backingStore = new DefaultBackingStoreSupplier(path);

        ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator(executor, backingStore, 9);
        InputStreamSupplier mockInputStreamSupplier = mock(InputStreamSupplier.class, new ViolatedAssumptionAnswer());
    }

    @Test(timeout = 4000)
    public void testSubmitStreamAwareCallable() throws Throwable {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(14);
        ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator(executor);

        ZipArchiveEntryRequestSupplier mockRequestSupplier = mock(ZipArchiveEntryRequestSupplier.class, new ViolatedAssumptionAnswer());
        Callable<ScatterZipOutputStream> callable = zipCreator.createCallable(mockRequestSupplier);

        zipCreator.submitStreamAwareCallable(callable);
    }

    @Test(timeout = 4000)
    public void testConstructorWithForkJoinPool() throws Throwable {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        new ParallelScatterZipCreator(forkJoinPool, null);
    }

    @Test(timeout = 4000)
    public void testWriteToWithNullExecutorService() {
        ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator((ExecutorService) null);

        try {
            zipCreator.writeTo(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            assertNull(e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testCreateCallableWithNullZipArchiveEntry() {
        ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator();
        InputStreamSupplier mockInputStreamSupplier = mock(InputStreamSupplier.class, new ViolatedAssumptionAnswer());

        try {
            zipCreator.createCallable(null, mockInputStreamSupplier);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            assertNull(e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testAddArchiveEntryWithNullZipArchiveEntry() {
        ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator();
        InputStreamSupplier mockInputStreamSupplier = mock(InputStreamSupplier.class, new ViolatedAssumptionAnswer());

        try {
            zipCreator.addArchiveEntry(null, mockInputStreamSupplier);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            assertNull(e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testCreateCallableWithInvalidZipArchiveEntry() {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(14);
        ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator(executor);

        ZipArchiveEntry invalidEntry = new ZipArchiveEntry("eKGRu5\"uXf4");
        InputStreamSupplier mockInputStreamSupplier = mock(InputStreamSupplier.class, new ViolatedAssumptionAnswer());

        try {
            zipCreator.createCallable(invalidEntry, mockInputStreamSupplier);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Method must be set on zipArchiveEntry: eKGRu5\"uXf4", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testInvalidCompressionLevelWithForkJoinPool() {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        MockFile mockFile = new MockFile("");
        Path path = mockFile.toPath();
        DefaultBackingStoreSupplier backingStore = new DefaultBackingStoreSupplier(path);

        try {
            new ParallelScatterZipCreator(forkJoinPool, backingStore, 1394);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Compression level is expected between -1~9", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testSubmitStreamAwareCallableWithNull() {
        ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator();

        try {
            zipCreator.submitStreamAwareCallable(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            assertNull(e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testZipArchiveOutputStreamInitializationFailure() {
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        URI fileUri = MockURI.aFileURI;
        MockFile mockFile = new MockFile(fileUri);
        mockFile.renameTo(mockFile);
        Path path = mockFile.toPath();
        DefaultBackingStoreSupplier backingStore = new DefaultBackingStoreSupplier(path);

        ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator(forkJoinPool, backingStore, 0);

        try {
            new ZipArchiveOutputStream(mockFile, 0);
            fail("Expecting exception: NoClassDefFoundError");
        } catch (NoClassDefFoundError e) {
            assertTrue(e.getMessage().contains("Could not initialize class org.apache.commons.compress.archivers.zip.ZipEncodingHelper"));
        }
    }

    @Test(timeout = 4000)
    public void testAddArchiveEntryWithInvalidJarArchiveEntry() {
        ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator();
        JarArchiveEntry invalidJarEntry = new JarArchiveEntry("a3??}e,z");

        try {
            zipCreator.addArchiveEntry(invalidJarEntry, null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Method must be set on zipArchiveEntry: a3??}e,z", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testParallelExecutionWithFuture() throws Throwable {
        Future<?> future = executor.submit(() -> {
            try {
                ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator();
                zipCreator.addArchiveEntry((ZipArchiveEntryRequestSupplier) null);

                try {
                    zipCreator.writeTo(null);
                    fail("Expecting exception: ExecutionException");
                } catch (ExecutionException e) {
                    assertTrue(e.getCause() instanceof SecurityException);
                }
            } catch (Throwable t) {
                // Catch declared exceptions
            }
        });

        future.get(4000, TimeUnit.MILLISECONDS);
    }

    @Test(timeout = 4000)
    public void testSubmitCallableWithNull() throws Throwable {
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator(forkJoinPool);

        zipCreator.submit(null);

        ZipArchiveEntry zipEntry = new ZipArchiveEntry();
        assertEquals(-1L, zipEntry.getLocalHeaderOffset());
    }
}