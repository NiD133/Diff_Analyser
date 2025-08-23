package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.compress.parallel.InputStreamSupplier;
import org.junit.After;
import org.junit.Test;

/**
 * Readable, focused unit tests for ParallelScatterZipCreator.
 * 
 * These tests avoid heavy threading/mocking and exercise clear, documented behaviors:
 * - Constructor argument validation
 * - Null argument validation
 * - Preconditions for creating compression callables
 */
public class ParallelScatterZipCreatorTest {

    // A simple executor to pass into constructors when needed.
    private final ExecutorService singleThread = Executors.newSingleThreadExecutor();

    @After
    public void tearDown() {
        singleThread.shutdownNow();
    }

    @Test
    public void constructorRejectsCompressionLevelBelowMinusOne() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new ParallelScatterZipCreator(singleThread, new DefaultBackingStoreSupplier(null), -2)
        );
    }

    @Test
    public void constructorRejectsCompressionLevelAboveNine() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new ParallelScatterZipCreator(singleThread, new DefaultBackingStoreSupplier(null), 10)
        );
    }

    @Test
    public void constructorAcceptsValidCompressionLevels() {
        // Should not throw
        assertNotNull(new ParallelScatterZipCreator(singleThread, new DefaultBackingStoreSupplier(null), -1));
        assertNotNull(new ParallelScatterZipCreator(singleThread, new DefaultBackingStoreSupplier(null), 0));
        assertNotNull(new ParallelScatterZipCreator(singleThread, new DefaultBackingStoreSupplier(null), 9));
    }

    @Test
    public void getStatisticsMessageIsAlwaysNonNull() {
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator();
        assertNotNull(creator.getStatisticsMessage());
    }

    @Test
    public void createCallableRejectsNullEntry() {
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator();
        InputStreamSupplier source = () -> new ByteArrayInputStream(new byte[0]);

        assertThrows(NullPointerException.class, () -> creator.createCallable(null, source));
    }

    @Test
    public void addArchiveEntryRejectsNullEntry() {
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator();
        InputStreamSupplier source = () -> new ByteArrayInputStream(new byte[0]);

        assertThrows(NullPointerException.class, () -> creator.addArchiveEntry(null, source));
    }

    @Test
    public void createCallableRequiresMethodToBeSetOnEntry() {
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator();
        ZipArchiveEntry entry = new ZipArchiveEntry("file.txt");
        InputStreamSupplier source = () -> new ByteArrayInputStream(new byte[0]);

        // Expect IllegalArgumentException because method (STORED/DEFLATED) is not set on the entry.
        assertThrows(IllegalArgumentException.class, () -> creator.createCallable(entry, source));
    }

    @Test
    public void createCallableSucceedsWhenMethodIsSet() {
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator();
        ZipArchiveEntry entry = new ZipArchiveEntry("file.txt");
        entry.setMethod(ZipMethod.DEFLATED.getCode());
        InputStreamSupplier source = () -> new ByteArrayInputStream("data".getBytes());

        // Should not throw: only creates the callable; it doesn't execute compression yet.
        assertNotNull(creator.createCallable(entry, source));
    }

    @Test
    public void submitRejectsNullCallable() {
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator();
        assertThrows(NullPointerException.class, () -> creator.submit(null));
    }

    @Test
    public void submitStreamAwareCallableRejectsNullCallable() {
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator();
        assertThrows(NullPointerException.class, () -> creator.submitStreamAwareCallable(null));
    }

    @Test
    public void writeToThrowsWhenExecutorServiceIsNull() {
        ParallelScatterZipCreator creator = new ParallelScatterZipCreator((ExecutorService) null);
        // We don't need a real ZipArchiveOutputStream here; the null executor should cause failure early.
        assertThrows(NullPointerException.class, () -> creator.writeTo(null));
    }
}