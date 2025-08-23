package org.apache.commons.compress.archivers.zip;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;

import org.apache.commons.compress.AbstractTempDirTest;
import org.apache.commons.compress.parallel.FileBasedScatterGatherBackingStore;
import org.apache.commons.compress.parallel.InputStreamSupplier;
import org.apache.commons.compress.parallel.ScatterGatherBackingStoreSupplier;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

/**
 * Tests the callable-based API of {@link ParallelScatterZipCreator}.
 */
public class ParallelScatterZipCreatorCallableApiTest extends AbstractTempDirTest {

    private static final int NUM_ENTRIES = 5000;

    /**
     * Tests that an archive can be created by submitting compression tasks
     * via the {@code createCallable} and {@code submitStreamAwareCallable} methods.
     */
    @Test
    void createsArchiveInParallelUsingCallableApi() throws Exception {
        // --- ARRANGE ---
        final File resultFile = createTempFile("parallelScatterGather", ".zip");
        final ExecutorService executorService = Executors.newFixedThreadPool(1);
        final ScatterGatherBackingStoreSupplier backingStoreSupplier =
            () -> new FileBasedScatterGatherBackingStore(createTempFile("parallelscatter", "store"));

        // This map will hold the expected file contents for later verification.
        final Map<String, byte[]> expectedEntries = new HashMap<>();
        final ParallelScatterZipCreator zipCreator;

        // --- ACT ---
        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(resultFile)) {
            zos.setEncoding(StandardCharsets.UTF_8.name());
            zipCreator = new ParallelScatterZipCreator(executorService, backingStoreSupplier, Deflater.DEFAULT_COMPRESSION);

            // Create and submit all compression tasks.
            for (int i = 0; i < NUM_ENTRIES; i++) {
                final String entryName = "file" + i;
                final byte[] payload = ("content" + i).getBytes(StandardCharsets.UTF_8);
                final ZipArchiveEntry zipArchiveEntry = createZipArchiveEntry(entryName, payload);
                expectedEntries.put(entryName, payload);

                final InputStreamSupplier payloadSupplier = () -> new ByteArrayInputStream(payload);

                // Alternate between the two 'createCallable' overloads to test both APIs.
                final Callable<ScatterZipOutputStream> callable;
                if (i % 2 == 0) {
                    callable = zipCreator.createCallable(zipArchiveEntry, payloadSupplier);
                } else {
                    final ZipArchiveEntryRequestSupplier requestSupplier =
                        () -> ZipArchiveEntryRequest.createZipArchiveEntryRequest(zipArchiveEntry, payloadSupplier);
                    callable = zipCreator.createCallable(requestSupplier);
                }
                zipCreator.submitStreamAwareCallable(callable);
            }

            // After all tasks are submitted, write the final archive.
            zipCreator.writeTo(zos);
        }

        // --- ASSERT ---
        // Verify that the ZIP file contains all the expected entries in the correct order.
        assertValidZip(resultFile, expectedEntries);

        // The map should be empty after verification, confirming all entries were found.
        assertTrue(expectedEntries.isEmpty(), "All expected entries should have been found in the zip file.");

        // Also assert that statistics are available.
        assertNotNull(zipCreator.getStatisticsMessage());
    }

    /**
     * Creates a pre-configured ZipArchiveEntry for testing.
     *
     * @param name    The name of the entry.
     * @param payload The content of the entry.
     * @return A configured ZipArchiveEntry.
     */
    private ZipArchiveEntry createZipArchiveEntry(final String name, final byte[] payload) {
        final ZipArchiveEntry entry = new ZipArchiveEntry(name);
        entry.setMethod(ZipEntry.DEFLATED);
        entry.setSize(payload.length);
        entry.setUnixMode(UnixStat.FILE_FLAG | 0664);
        return entry;
    }

    /**
     * Asserts that the created ZIP file has the expected content and that entries are in the correct order.
     *
     * @param zipFile         The ZIP file to validate.
     * @param expectedEntries A map of entry names to their expected byte content. This map is modified during validation.
     * @throws IOException if an I/O error occurs.
     */
    private void assertValidZip(final File zipFile, final Map<String, byte[]> expectedEntries) throws IOException {
        try (ZipFile zf = ZipFile.builder().setFile(zipFile).get()) {
            final Enumeration<ZipArchiveEntry> entries = zf.getEntriesInPhysicalOrder();
            int entryIndex = 0;
            while (entries.hasMoreElements()) {
                final ZipArchiveEntry entry = entries.nextElement();

                // Verify content
                try (InputStream inputStream = zf.getInputStream(entry)) {
                    final byte[] actualContent = IOUtils.toByteArray(inputStream);
                    // Use remove() to get the expected content and mark it as "seen".
                    final byte[] expectedContent = expectedEntries.remove(entry.getName());
                    assertNotNull(expectedContent, "Found unexpected entry in zip: " + entry.getName());
                    assertArrayEquals(expectedContent, actualContent, "Content mismatch for entry: " + entry.getName());
                }

                // Verify order
                final String expectedName = "file" + entryIndex++;
                assertEquals(expectedName, entry.getName(), "Entry order mismatch. Expected " + expectedName + " but got " + entry.getName());
            }
        }
    }
}