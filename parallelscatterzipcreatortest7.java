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
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;

import org.apache.commons.compress.AbstractTempDirTest;
import org.apache.commons.compress.parallel.InputStreamSupplier;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ParallelScatterZipCreator} focusing on concurrent entry processing.
 */
public class ParallelScatterZipCreatorTest extends AbstractTempDirTest {

    private static final int ENTRY_COUNT = 5000;

    /**
     * Tests that creating a zip with the default constructor correctly processes and adds all entries in parallel.
     * It verifies that both overloads of the {@code addArchiveEntry} method work as expected.
     */
    @Test
    void addArchiveEntry_withDefaultConstructor_createsCorrectZip() throws Exception {
        // Arrange
        final File resultZipFile = createTempFile("parallelScatterGather", ".zip");
        final Map<String, byte[]> expectedEntries = new HashMap<>();

        // Act
        final ParallelScatterZipCreator zipCreator;
        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(resultZipFile)) {
            zos.setEncoding(StandardCharsets.UTF_8.name());
            // Use the default constructor, which sets up its own ExecutorService
            zipCreator = new ParallelScatterZipCreator();

            // Create and add entries, alternating between the two 'addArchiveEntry' methods
            for (int i = 0; i < ENTRY_COUNT; i++) {
                final String entryName = "file" + i;
                final byte[] payload = ("content" + i).getBytes(StandardCharsets.UTF_8);
                expectedEntries.put(entryName, payload);

                final ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(entryName);
                zipArchiveEntry.setSize(payload.length);
                zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
                zipArchiveEntry.setUnixMode(UnixStat.FILE_FLAG | 0664);

                final InputStreamSupplier payloadSupplier = () -> new ByteArrayInputStream(payload);

                if (i % 2 == 0) {
                    zipCreator.addArchiveEntry(zipArchiveEntry, payloadSupplier);
                } else {
                    final ZipArchiveEntryRequestSupplier requestSupplier =
                        () -> ZipArchiveEntryRequest.createZipArchiveEntryRequest(zipArchiveEntry, payloadSupplier);
                    zipCreator.addArchiveEntry(requestSupplier);
                }
            }
            zipCreator.writeTo(zos);
        }

        // Assert
        assertZipFileHasCorrectContentsAndOrder(resultZipFile, expectedEntries);
        assertTrue(expectedEntries.isEmpty(), "All expected entries should have been found in the zip file");
        assertNotNull(zipCreator.getStatisticsMessage(), "Statistics message should be generated");
    }

    /**
     * Validates that the created ZIP file contains the expected entries in the correct order and with the correct content.
     *
     * @param zipFile The ZIP file to validate.
     * @param expectedEntries A map of expected entry names to their content. Entries found will be removed from this map.
     * @throws IOException if an I/O error occurs.
     */
    private void assertZipFileHasCorrectContentsAndOrder(final File zipFile, final Map<String, byte[]> expectedEntries) throws IOException {
        try (ZipFile zf = ZipFile.builder().setFile(zipFile).get()) {
            final Enumeration<ZipArchiveEntry> entries = zf.getEntriesInPhysicalOrder();
            int entryIndex = 0;
            while (entries.hasMoreElements()) {
                final ZipArchiveEntry entry = entries.nextElement();
                final String expectedName = "file" + entryIndex++;
                final String actualName = entry.getName();

                // 1. Assert order
                assertEquals(expectedName, actualName, "Entries should be in the order they were added.");

                // 2. Assert content
                try (InputStream inputStream = zf.getInputStream(entry)) {
                    final byte[] actualBytes = IOUtils.toByteArray(inputStream);
                    final byte[] expectedBytes = expectedEntries.remove(actualName);

                    assertNotNull(expectedBytes, "Found an unexpected entry in zip: " + actualName);
                    assertArrayEquals(expectedBytes, actualBytes, "Content of " + actualName + " should match.");
                }
            }
        }
    }
}