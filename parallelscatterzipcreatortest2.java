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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
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
import org.junit.jupiter.api.Test;

/**
 * Tests the callable-based API of {@link ParallelScatterZipCreator}.
 */
public class ParallelScatterZipCreatorCallableApiTest extends AbstractTempDirTest {

    // 1MB
    private static final long MAX_FILE_SIZE = 1024 * 1024;
    private static final int MAX_FILES_TO_COMPRESS = 50;

    /**
     * Tests that a ZIP archive can be created by submitting Callable tasks,
     * and verifies that the entries are written in the order they were submitted.
     */
    @Test
    void shouldCreateArchiveFromSubmittedCallables() throws Exception {
        // Arrange
        final int numEntries = 5000;
        final File resultZipFile = createTempFile("parallelScatter", ".zip");
        final Map<String, byte[]> expectedEntries = new HashMap<>();

        final ExecutorService executorService = Executors.newFixedThreadPool(1);
        final ScatterGatherBackingStoreSupplier backingStoreSupplier =
            () -> new FileBasedScatterGatherBackingStore(createTempFile("parallelscatter-backing", ""));
        final ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator(executorService, backingStoreSupplier);

        // Act
        // Create and submit all entry creation tasks
        for (int i = 0; i < numEntries; i++) {
            final String entryName = "file" + i;
            final byte[] payloadBytes = ("content" + i).getBytes(StandardCharsets.UTF_8);
            expectedEntries.put(entryName, payloadBytes);

            final ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(entryName);
            zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
            zipArchiveEntry.setSize(payloadBytes.length);
            zipArchiveEntry.setUnixMode(UnixStat.FILE_FLAG | 0664);

            final InputStreamSupplier inputStreamSupplier = () -> new ByteArrayInputStream(payloadBytes);

            // Alternate between the two createCallable method overloads to test both
            final Callable<ScatterZipOutputStream> callable;
            if (i % 2 == 0) {
                callable = zipCreator.createCallable(zipArchiveEntry, inputStreamSupplier);
            } else {
                final ZipArchiveEntryRequestSupplier requestSupplier =
                    () -> ZipArchiveEntryRequest.createZipArchiveEntryRequest(zipArchiveEntry, inputStreamSupplier);
                callable = zipCreator.createCallable(requestSupplier);
            }
            zipCreator.submit(callable);
        }

        // Write the archive to the file
        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(resultZipFile)) {
            zos.setEncoding(StandardCharsets.UTF_8.name());
            zipCreator.writeTo(zos);
        }

        // Assert
        assertZipFileContainsEntriesInOrder(resultZipFile, expectedEntries, numEntries);
        assertNotNull(zipCreator.getStatisticsMessage());
    }

    /**
     * Tests creating an archive from actual files in the test resources directory.
     * This test does not assume a specific order for the entries in the final archive.
     */
    @Test
    void shouldCreateArchiveFromRealFiles() throws Exception {
        // Arrange
        final File resultZipFile = createTempFile("parallelScatterFromFiles", ".zip");
        final Map<String, byte[]> expectedEntries = new HashMap<>();

        final ExecutorService executorService = Executors.newFixedThreadPool(1);
        final ScatterGatherBackingStoreSupplier backingStoreSupplier =
            () -> new FileBasedScatterGatherBackingStore(createTempFile("parallelscatter-backing", ""));
        final ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator(executorService, backingStoreSupplier, Deflater.DEFAULT_COMPRESSION);

        // Act
        // Create and submit tasks from test resource files
        final File baseDir = AbstractTest.getFile(""); // Gets src/test/resources
        int filesCount = 0;
        for (final File file : baseDir.listFiles()) {
            if (filesCount >= MAX_FILES_TO_COMPRESS || file.isDirectory() || file.length() > MAX_FILE_SIZE) {
                continue;
            }

            final String entryName = file.getName();
            expectedEntries.put(entryName, Files.readAllBytes(file.toPath()));

            final ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(entryName);
            zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
            zipArchiveEntry.setSize(file.length());
            zipArchiveEntry.setUnixMode(UnixStat.FILE_FLAG | 0664);

            final InputStreamSupplier inputStreamSupplier = () -> Files.newInputStream(file.toPath());

            // Alternate between the two createCallable method overloads to test both
            final Callable<ScatterZipOutputStream> callable;
            if (filesCount % 2 == 0) {
                callable = zipCreator.createCallable(zipArchiveEntry, inputStreamSupplier);
            } else {
                final ZipArchiveEntryRequestSupplier requestSupplier =
                    () -> ZipArchiveEntryRequest.createZipArchiveEntryRequest(zipArchiveEntry, inputStreamSupplier);
                callable = zipCreator.createCallable(requestSupplier);
            }
            zipCreator.submit(callable);
            filesCount++;
        }

        // Write the archive to the file
        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(resultZipFile)) {
            zos.setEncoding(StandardCharsets.UTF_8.name());
            zipCreator.writeTo(zos);
        }

        // Assert
        assertZipFileContainsEntries(resultZipFile, expectedEntries);
        assertNotNull(zipCreator.getStatisticsMessage());
    }

    /**
     * Asserts that the zip file contains the expected entries and that their content is correct.
     * It also verifies that the entries appear in the exact physical order of submission.
     *
     * @param zipFile         The archive file to verify.
     * @param expectedEntries A map of entry names to their expected byte content.
     * @param numEntries      The total number of expected entries.
     */
    private void assertZipFileContainsEntriesInOrder(final File zipFile, final Map<String, byte[]> expectedEntries, final int numEntries) throws IOException {
        final Map<String, byte[]> remainingEntries = new HashMap<>(expectedEntries);
        try (ZipFile zf = ZipFile.builder().setFile(zipFile).get()) {
            final Enumeration<ZipArchiveEntry> entriesInPhysicalOrder = zf.getEntriesInPhysicalOrder();
            int entryIndex = 0;
            while (entriesInPhysicalOrder.hasMoreElements()) {
                final ZipArchiveEntry zipArchiveEntry = entriesInPhysicalOrder.nextElement();
                final String expectedName = "file" + entryIndex;

                // Assert order
                assertEquals(expectedName, zipArchiveEntry.getName(), "Entry at index " + entryIndex + " has wrong name.");

                // Assert content
                try (InputStream inputStream = zf.getInputStream(zipArchiveEntry)) {
                    final byte[] actualData = IOUtils.toByteArray(inputStream);
                    final byte[] expectedData = remainingEntries.remove(zipArchiveEntry.getName());
                    assertNotNull(expectedData, "Found unexpected entry: " + zipArchiveEntry.getName());
                    assertArrayEquals(expectedData, actualData, "Content of " + zipArchiveEntry.getName() + " does not match.");
                }
                entryIndex++;
            }
            assertEquals(numEntries, entryIndex, "The number of entries in the zip file does not match the expected count.");
        }
        assertTrue(remainingEntries.isEmpty(), "Some expected entries were not found in the zip file: " + remainingEntries.keySet());
    }

    /**
     * Asserts that the zip file contains exactly the expected entries and that their content is correct.
     * This method does not check for a specific entry order.
     *
     * @param zipFile         The archive file to verify.
     * @param expectedEntries A map of entry names to their expected byte content.
     */
    private void assertZipFileContainsEntries(final File zipFile, final Map<String, byte[]> expectedEntries) throws IOException {
        final Map<String, byte[]> remainingEntries = new HashMap<>(expectedEntries);
        try (ZipFile zf = ZipFile.builder().setFile(zipFile).get()) {
            final Enumeration<ZipArchiveEntry> entries = zf.getEntries();
            while (entries.hasMoreElements()) {
                final ZipArchiveEntry zipArchiveEntry = entries.nextElement();
                try (InputStream inputStream = zf.getInputStream(zipArchiveEntry)) {
                    final byte[] actualData = IOUtils.toByteArray(inputStream);
                    final byte[] expectedData = remainingEntries.remove(zipArchiveEntry.getName());
                    assertNotNull(expectedData, "Found unexpected entry: " + zipArchiveEntry.getName());
                    assertArrayEquals(expectedData, actualData, "Content of " + zipArchiveEntry.getName() + " does not match.");
                }
            }
        }
        assertTrue(remainingEntries.isEmpty(), "Some expected entries were not found in the zip file: " + remainingEntries.keySet());
    }
}