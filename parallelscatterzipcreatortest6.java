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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;

import org.apache.commons.compress.AbstractTempDirTest;
import org.apache.commons.compress.parallel.InputStreamSupplier;
import org.apache.commons.compress.parallel.ScatterGatherBackingStoreSupplier;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ParallelScatterZipCreator} using a custom temporary directory for backing stores.
 */
public class ParallelScatterZipCreatorWithCustomTempDirTest extends AbstractTempDirTest {

    private static final int NUMBER_OF_ENTRIES_TO_CREATE = 5000;

    @Test
    void createsArchiveSuccessfullyWithCustomTempDirectory() throws Exception {
        // Arrange
        final Path customTempDir = Files.createDirectories(Paths.get("target/custom-temp-dir"));
        final File resultZipFile = createTempFile("parallelScatterGather", ".zip");

        final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        final ScatterGatherBackingStoreSupplier backingStoreSupplier = new DefaultBackingStoreSupplier(customTempDir);
        final ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator(executor, backingStoreSupplier);

        // Act
        final Map<String, byte[]> expectedEntries;
        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(resultZipFile)) {
            zos.setEncoding(StandardCharsets.UTF_8.name());
            expectedEntries = addTestEntriesToCreator(zipCreator);
            zipCreator.writeTo(zos);
        }

        // Assert
        verifyZipArchiveContentAndOrder(resultZipFile, expectedEntries);
        assertNotNull(zipCreator.getStatisticsMessage(), "Statistics message should not be null");
    }

    /**
     * Adds a predefined number of test entries to the ParallelScatterZipCreator.
     *
     * @param zipCreator The creator to add entries to.
     * @return A map of entry names to their byte content, for later verification.
     */
    private Map<String, byte[]> addTestEntriesToCreator(final ParallelScatterZipCreator zipCreator) {
        final Map<String, byte[]> entries = new HashMap<>();
        for (int i = 0; i < NUMBER_OF_ENTRIES_TO_CREATE; i++) {
            final String entryName = "file" + i;
            final byte[] payloadBytes = ("content" + i).getBytes(StandardCharsets.UTF_8);
            entries.put(entryName, payloadBytes);

            final ZipArchiveEntry zipArchiveEntry = createDeflatedEntry(entryName, payloadBytes);
            final InputStreamSupplier inputStreamSupplier = () -> new ByteArrayInputStream(payloadBytes);

            // To ensure both API endpoints are tested, alternate between the two addArchiveEntry methods.
            if (i % 2 == 0) {
                zipCreator.addArchiveEntry(zipArchiveEntry, inputStreamSupplier);
            } else {
                final ZipArchiveEntryRequestSupplier requestSupplier =
                    () -> ZipArchiveEntryRequest.createZipArchiveEntryRequest(zipArchiveEntry, inputStreamSupplier);
                zipCreator.addArchiveEntry(requestSupplier);
            }
        }
        return entries;
    }

    /**
     * Creates a ZipArchiveEntry with default DEFLATED method and file permissions.
     */
    private ZipArchiveEntry createDeflatedEntry(final String name, final byte[] data) {
        final ZipArchiveEntry entry = new ZipArchiveEntry(name);
        entry.setMethod(ZipEntry.DEFLATED);
        entry.setSize(data.length);
        entry.setUnixMode(UnixStat.FILE_FLAG | 0664);
        return entry;
    }

    /**
     * Verifies that the created ZIP file contains the expected entries in the correct order and with the correct content.
     *
     * @param zipFile         The ZIP file to verify.
     * @param expectedEntries A map of expected entry names to their content.
     * @throws IOException If an I/O error occurs.
     */
    private void verifyZipArchiveContentAndOrder(final File zipFile, final Map<String, byte[]> expectedEntries) throws IOException {
        final Map<String, byte[]> remainingEntries = new HashMap<>(expectedEntries);
        try (ZipFile zf = ZipFile.builder().setFile(zipFile).get()) {
            final Enumeration<ZipArchiveEntry> entriesInPhysicalOrder = zf.getEntriesInPhysicalOrder();
            int entryIndex = 0;
            while (entriesInPhysicalOrder.hasMoreElements()) {
                final ZipArchiveEntry archiveEntry = entriesInPhysicalOrder.nextElement();
                final String entryName = archiveEntry.getName();

                // Verify content
                try (InputStream inputStream = zf.getInputStream(archiveEntry)) {
                    final byte[] actualData = IOUtils.toByteArray(inputStream);
                    final byte[] expectedData = remainingEntries.remove(entryName);
                    assertNotNull(expectedData, "Found unexpected entry in ZIP: " + entryName);
                    assertArrayEquals(expectedData, actualData, "Content mismatch for entry: " + entryName);
                }

                // Verify order
                final String expectedName = "file" + entryIndex++;
                assertEquals(expectedName, entryName, "Entry order mismatch for: " + entryName);
            }
        }
        assertTrue(remainingEntries.isEmpty(), "Some expected entries were not found in the archive: " + remainingEntries.keySet());
    }
}