package org.apache.commons.compress.archivers.zip;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;

import org.apache.commons.compress.AbstractTempDirTest;
import org.apache.commons.compress.AbstractTest;
import org.apache.commons.compress.parallel.FileBasedScatterGatherBackingStore;
import org.apache.commons.compress.parallel.InputStreamSupplier;
import org.apache.commons.compress.parallel.ScatterGatherBackingStoreSupplier;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ParallelScatterZipCreator}.
 * This class focuses on verifying the two main APIs for adding entries:
 * 1. The direct {@code addArchiveEntry()} method.
 * 2. The {@code createCallable()} method for deferred submission.
 */
public class ParallelScatterZipCreatorTest extends AbstractTempDirTest {

    private static final int NUM_ENTRIES_IN_MEMORY_TEST = 500;
    private static final int MAX_FILES_FROM_RESOURCES = 50;
    private static final long MAX_FILE_SIZE_FROM_RESOURCES = 1024 * 1024; // 1MB

    /**
     * Tests creating a ZIP archive using the {@code addArchiveEntry} API with in-memory data.
     */
    @Test
    public void testCreatesArchiveUsingAddArchiveEntryApi() throws Exception {
        final File result = createTempFile("scatter-add-api", ".zip");
        final Map<String, byte[]> expectedEntries;

        // Note: Using a single-threaded executor to ensure the test is deterministic
        // and focuses on the correctness of the scatter-gather mechanism, not just concurrency.
        final ExecutorService executorService = Executors.newFixedThreadPool(1);
        final ScatterGatherBackingStoreSupplier backingStoreSupplier =
            () -> new FileBasedScatterGatherBackingStore(createTempFile("parallelscatter", "n1"));

        final ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator(executorService, backingStoreSupplier);

        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(result)) {
            zos.setEncoding(StandardCharsets.UTF_8.name());
            expectedEntries = addInMemoryEntriesUsingAddApi(zipCreator);
            zipCreator.writeTo(zos);
        }

        assertArchiveContents(result, expectedEntries, true);
        assertNotNull(zipCreator.getStatisticsMessage());
    }

    /**
     * Tests creating a ZIP archive using the {@code createCallable} API with in-memory data.
     */
    @Test
    public void testCreatesArchiveUsingCreateCallableApi() throws Exception {
        final File result = createTempFile("scatter-callable-api", ".zip");
        final Map<String, byte[]> expectedEntries;

        final ExecutorService executorService = Executors.newFixedThreadPool(1);
        final ScatterGatherBackingStoreSupplier backingStoreSupplier =
            () -> new FileBasedScatterGatherBackingStore(createTempFile("parallelscatter", "n1"));

        final ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator(executorService, backingStoreSupplier);

        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(result)) {
            zos.setEncoding(StandardCharsets.UTF_8.name());
            expectedEntries = addInMemoryEntriesUsingCallableApi(zipCreator);
            zipCreator.writeTo(zos);
        }

        assertArchiveContents(result, expectedEntries, true);
        assertNotNull(zipCreator.getStatisticsMessage());
    }

    /**
     * Tests creating a ZIP archive using the {@code createCallable} API with actual files from test resources.
     */
    @Test
    public void testCreatesArchiveFromFilesUsingCreateCallableApi() throws Exception {
        final File result = createTempFile("scatter-files-callable-api", ".zip");
        final Map<String, byte[]> expectedEntries;

        final ExecutorService executorService = Executors.newFixedThreadPool(1);
        final ScatterGatherBackingStoreSupplier backingStoreSupplier =
            () -> new FileBasedScatterGatherBackingStore(createTempFile("parallelscatter", "n1"));

        final ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator(executorService, backingStoreSupplier, Deflater.DEFAULT_COMPRESSION);

        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(result)) {
            zos.setEncoding(StandardCharsets.UTF_8.name());
            expectedEntries = addTestFilesUsingCallableApi(zipCreator);
            zipCreator.writeTo(zos);
        }

        // With real files, the order of completion is not guaranteed, so we don't check entry order.
        assertArchiveContents(result, expectedEntries, false);
        assertNotNull(zipCreator.getStatisticsMessage());
    }

    /**
     * Regression test for COMPRESS-639. Verifies that adding multiple entries with the exact same name
     * does not cause a NullPointerException during the write phase.
     */
    @Test
    @Disabled("[COMPRESS-639] This test is disabled as it exposes a known issue.")
    public void addMultipleEntriesWithSameName_shouldNotThrowException() {
        assertDoesNotThrow(() -> {
            final ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
            final String fileContent = "A";
            final int numOfFiles = 100;
            final LinkedList<InputStream> inputStreams = new LinkedList<>();
            for (int i = 0; i < numOfFiles; i++) {
                inputStreams.add(new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8)));
            }

            final ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator();
            try (ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(testOutputStream)) {
                zipArchiveOutputStream.setUseZip64(Zip64Mode.Always);
                for (final InputStream inputStream : inputStreams) {
                    final ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("./dir/myfile.txt");
                    zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
                    zipCreator.addArchiveEntry(zipArchiveEntry, () -> inputStream);
                }
                zipCreator.writeTo(zipArchiveOutputStream);
            }
        }, "Adding multiple entries with the same name should not throw an exception.");
    }

    /**
     * Adds entries to the creator using the {@code addArchiveEntry} methods.
     * This method explicitly tests both overloads of {@code addArchiveEntry}.
     */
    private Map<String, byte[]> addInMemoryEntriesUsingAddApi(final ParallelScatterZipCreator zipCreator) {
        final Map<String, byte[]> entries = new HashMap<>();
        final int halfNumEntries = NUM_ENTRIES_IN_MEMORY_TEST / 2;

        // First half: Test addArchiveEntry(ZipArchiveEntry, InputStreamSupplier)
        for (int i = 0; i < halfNumEntries; i++) {
            final byte[] payload = ("content" + i).getBytes();
            final ZipArchiveEntry za = createZipArchiveEntry("file" + i, payload, entries);
            final InputStreamSupplier iss = () -> new ByteArrayInputStream(payload);
            zipCreator.addArchiveEntry(za, iss);
        }

        // Second half: Test addArchiveEntry(ZipArchiveEntryRequestSupplier)
        for (int i = halfNumEntries; i < NUM_ENTRIES_IN_MEMORY_TEST; i++) {
            final byte[] payload = ("content" + i).getBytes();
            final ZipArchiveEntry za = createZipArchiveEntry("file" + i, payload, entries);
            final InputStreamSupplier iss = () -> new ByteArrayInputStream(payload);
            final ZipArchiveEntryRequestSupplier zaSupplier = () -> ZipArchiveEntryRequest.createZipArchiveEntryRequest(za, iss);
            zipCreator.addArchiveEntry(zaSupplier);
        }
        return entries;
    }

    /**
     * Adds entries to the creator using the {@code createCallable} methods.
     * This method explicitly tests both overloads of {@code createCallable}.
     */
    private Map<String, byte[]> addInMemoryEntriesUsingCallableApi(final ParallelScatterZipCreator zipCreator) {
        final Map<String, byte[]> entries = new HashMap<>();
        final int halfNumEntries = NUM_ENTRIES_IN_MEMORY_TEST / 2;

        // First half: Test createCallable(ZipArchiveEntry, InputStreamSupplier)
        for (int i = 0; i < halfNumEntries; i++) {
            final byte[] payload = ("content" + i).getBytes();
            final ZipArchiveEntry za = createZipArchiveEntry("file" + i, payload, entries);
            final InputStreamSupplier iss = () -> new ByteArrayInputStream(payload);
            zipCreator.submit(zipCreator.createCallable(za, iss));
        }

        // Second half: Test createCallable(ZipArchiveEntryRequestSupplier)
        for (int i = halfNumEntries; i < NUM_ENTRIES_IN_MEMORY_TEST; i++) {
            final byte[] payload = ("content" + i).getBytes();
            final ZipArchiveEntry za = createZipArchiveEntry("file" + i, payload, entries);
            final InputStreamSupplier iss = () -> new ByteArrayInputStream(payload);
            final ZipArchiveEntryRequestSupplier zaSupplier = () -> ZipArchiveEntryRequest.createZipArchiveEntryRequest(za, iss);
            zipCreator.submit(zipCreator.createCallable(zaSupplier));
        }
        return entries;
    }

    /**
     * Scans the test resources directory and adds a limited number of files to the creator
     * using the {@code createCallable} API.
     */
    private Map<String, byte[]> addTestFilesUsingCallableApi(final ParallelScatterZipCreator zipCreator) throws IOException {
        final Map<String, byte[]> entries = new HashMap<>();
        final File baseDir = AbstractTest.getFile("");
        int filesCount = 0;

        for (final File file : baseDir.listFiles()) {
            if (filesCount >= MAX_FILES_FROM_RESOURCES) {
                break;
            }
            if (file.isDirectory() || file.length() > MAX_FILE_SIZE_FROM_RESOURCES) {
                continue;
            }

            final byte[] payload = Files.readAllBytes(file.toPath());
            final ZipArchiveEntry za = createZipArchiveEntry(file.getName(), payload, entries);
            final InputStreamSupplier iss = () -> {
                try {
                    return Files.newInputStream(file.toPath());
                } catch (final IOException e) {
                    // Should not happen in this test context
                    return null;
                }
            };

            final Callable<ScatterZipOutputStream> callable;
            // Alternate between the two API overloads
            if (filesCount % 2 == 0) {
                callable = zipCreator.createCallable(za, iss);
            } else {
                final ZipArchiveEntryRequestSupplier zaSupplier = () -> ZipArchiveEntryRequest.createZipArchiveEntryRequest(za, iss);
                callable = zipCreator.createCallable(zaSupplier);
            }
            zipCreator.submit(callable);
            filesCount++;
        }
        return entries;
    }

    /**
     * Helper to create a {@link ZipArchiveEntry} and store its expected content.
     */
    private ZipArchiveEntry createZipArchiveEntry(final String name, final byte[] payload, final Map<String, byte[]> entries) {
        final ZipArchiveEntry za = new ZipArchiveEntry(name);
        za.setMethod(ZipEntry.DEFLATED);
        za.setSize(payload.length);
        za.setUnixMode(UnixStat.FILE_FLAG | 0664);
        entries.put(name, payload);
        return za;
    }

    /**
     * Asserts that the contents of the created ZIP file match the expected entries.
     *
     * @param archiveFile      The ZIP file to validate.
     * @param expectedEntries  A map of entry names to their expected byte content.
     * @param checkOrder       If true, asserts that the order of entries in the ZIP matches the insertion order.
     */
    private void assertArchiveContents(final File archiveFile, final Map<String, byte[]> expectedEntries, final boolean checkOrder) throws IOException {
        final Map<String, byte[]> remainingEntries = new HashMap<>(expectedEntries);
        try (ZipFile zf = ZipFile.builder().setFile(archiveFile).get()) {
            final Enumeration<ZipArchiveEntry> entriesInPhysicalOrder = zf.getEntriesInPhysicalOrder();
            int entryIndex = 0;
            while (entriesInPhysicalOrder.hasMoreElements()) {
                final ZipArchiveEntry entry = entriesInPhysicalOrder.nextElement();
                try (InputStream inputStream = zf.getInputStream(entry)) {
                    final byte[] actual = IOUtils.toByteArray(inputStream);
                    final byte[] expected = remainingEntries.remove(entry.getName());

                    assertNotNull(expected, "Found unexpected entry: " + entry.getName());
                    assertArrayEquals(expected, actual, "Content mismatch for entry: " + entry.getName());
                }
                if (checkOrder) {
                    assertEquals("file" + entryIndex++, entry.getName(), "Entry order mismatch for: " + entry.getName());
                }
            }
        }
        assertTrue(remainingEntries.isEmpty(), "Some expected entries were not found in the archive: " + remainingEntries.keySet());
    }
}