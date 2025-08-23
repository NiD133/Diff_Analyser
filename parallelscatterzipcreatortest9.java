package org.apache.commons.compress.archivers.zip;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
 * Tests the "callable" API of {@link ParallelScatterZipCreator}.
 * This API allows for client-managed, parallel submission of compression tasks.
 */
public class ParallelScatterZipCreatorCallableApiTest extends AbstractTempDirTest {

    private static final long MAX_TEST_FILE_SIZE = 1024 * 1024; // 1MB
    private static final int MAX_TEST_FILES_TO_COMPRESS = 50;
    private static final int NUMBER_OF_GENERATED_ENTRIES = 5000;

    @Test
    void constructorShouldThrowOnInvalidCompressionLevel() {
        final int invalidCompressionLevel = -2; // Valid levels are -1 and 0-9
        final ExecutorService es = Executors.newFixedThreadPool(1);
        final ScatterGatherBackingStoreSupplier supplier = () -> new FileBasedScatterGatherBackingStore(createTempFile());

        assertThrows(IllegalArgumentException.class, () -> new ParallelScatterZipCreator(es, supplier, invalidCompressionLevel));

        es.shutdownNow();
    }

    @Test
    void callableApiWithGeneratedEntriesShouldCreateCorrectZip() throws Exception {
        // Arrange
        final File resultZip = newFile("callableApiWithGeneratedEntries.zip");
        final ExecutorService es = Executors.newFixedThreadPool(1);
        final ScatterGatherBackingStoreSupplier supplier = () -> new FileBasedScatterGatherBackingStore(createTempFile());
        final ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator(es, supplier);

        // Act
        final Map<String, byte[]> expectedEntries = submitGeneratedEntriesAsCallables(zipCreator, NUMBER_OF_GENERATED_ENTRIES);
        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(resultZip)) {
            zipCreator.writeTo(zos);
        }

        // Assert
        assertZipContentsAndOrderInvariance(resultZip, expectedEntries);
        assertNotNull(zipCreator.getStatisticsMessage());
    }

    @Test
    void callableApiWithCustomCompressionShouldCreateCorrectZip() throws Exception {
        // Arrange
        final File resultZip = newFile("callableApiWithCustomCompression.zip");
        final ExecutorService es = Executors.newFixedThreadPool(1);
        final ScatterGatherBackingStoreSupplier supplier = () -> new FileBasedScatterGatherBackingStore(createTempFile());
        final int compressionLevel = Deflater.NO_COMPRESSION;
        final ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator(es, supplier, compressionLevel);

        // Act
        final Map<String, byte[]> expectedEntries = submitGeneratedEntriesAsCallables(zipCreator, NUMBER_OF_GENERATED_ENTRIES);
        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(resultZip)) {
            zipCreator.writeTo(zos);
        }

        // Assert
        assertZipContentsAndOrderInvariance(resultZip, expectedEntries);
        assertNotNull(zipCreator.getStatisticsMessage());
    }

    @Test
    void callableApiWithTestFilesShouldCreateCorrectZip() throws Exception {
        // Arrange
        final File resultZip = newFile("callableApiWithTestFiles.zip");
        final ExecutorService es = Executors.newFixedThreadPool(1);
        final ScatterGatherBackingStoreSupplier supplier = () -> new FileBasedScatterGatherBackingStore(createTempFile());
        final ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator(es, supplier);

        // Act
        final Map<String, byte[]> expectedEntries = submitRealTestFilesAsCallables(zipCreator);
        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(resultZip)) {
            zipCreator.writeTo(zos);
        }

        // Assert
        assertZipContents(resultZip, expectedEntries);
        assertNotNull(zipCreator.getStatisticsMessage());
    }

    /**
     * Creates a number of simple zip entries and submits them to the creator as callables.
     *
     * @param zipCreator The creator to submit tasks to.
     * @param entryCount The number of entries to generate.
     * @return A map of entry names to their expected byte content.
     */
    private Map<String, byte[]> submitGeneratedEntriesAsCallables(final ParallelScatterZipCreator zipCreator, final int entryCount) {
        final Map<String, byte[]> entries = new HashMap<>();
        for (int i = 0; i < entryCount; i++) {
            final byte[] payloadBytes = ("content" + i).getBytes(StandardCharsets.UTF_8);
            final ZipArchiveEntry za = createZipArchiveEntry("file" + i, payloadBytes);
            entries.put(za.getName(), payloadBytes);

            final InputStreamSupplier iss = () -> new ByteArrayInputStream(payloadBytes);

            // To test both API overloads, we alternate between them.
            final Callable<ScatterZipOutputStream> callable;
            if (i % 2 == 0) {
                callable = zipCreator.createCallable(za, iss);
            } else {
                final ZipArchiveEntryRequestSupplier zaSupplier = () -> ZipArchiveEntryRequest.createZipArchiveEntryRequest(za, iss);
                callable = zipCreator.createCallable(zaSupplier);
            }
            zipCreator.submit(callable);
        }
        return entries;
    }

    /**
     * Finds test files in the resources directory and submits them to the creator as callables.
     *
     * @param zipCreator The creator to submit tasks to.
     * @return A map of entry names to their expected byte content.
     * @throws IOException if an I/O error occurs reading the test files.
     */
    private Map<String, byte[]> submitRealTestFilesAsCallables(final ParallelScatterZipCreator zipCreator) throws IOException {
        final Map<String, byte[]> entries = new HashMap<>();
        final File baseDir = AbstractTest.getFile(""); // Gets src/test/resources
        int filesCount = 0;
        for (final File file : baseDir.listFiles()) {
            if (filesCount >= MAX_TEST_FILES_TO_COMPRESS) {
                break;
            }
            if (file.isDirectory() || file.length() > MAX_TEST_FILE_SIZE) {
                continue;
            }

            final byte[] fileBytes = Files.readAllBytes(file.toPath());
            entries.put(file.getName(), fileBytes);

            final ZipArchiveEntry zipArchiveEntry = createZipArchiveEntry(file.getName(), fileBytes);
            final InputStreamSupplier iss = () -> {
                try {
                    return Files.newInputStream(file.toPath());
                } catch (final IOException e) {
                    // This should not happen in the test context, but required by the interface.
                    throw new RuntimeException(e);
                }
            };

            // To test both API overloads, we alternate between them.
            final Callable<ScatterZipOutputStream> callable;
            if (filesCount % 2 == 0) {
                callable = zipCreator.createCallable(zipArchiveEntry, iss);
            } else {
                final ZipArchiveEntryRequestSupplier zaSupplier = () -> ZipArchiveEntryRequest.createZipArchiveEntryRequest(zipArchiveEntry, iss);
                callable = zipCreator.createCallable(zaSupplier);
            }
            zipCreator.submit(callable);
            filesCount++;
        }
        return entries;
    }

    private ZipArchiveEntry createZipArchiveEntry(final String name, final byte[] payload) {
        final ZipArchiveEntry za = new ZipArchiveEntry(name);
        za.setMethod(ZipEntry.DEFLATED);
        za.setSize(payload.length);
        za.setUnixMode(UnixStat.FILE_FLAG | 0664);
        return za;
    }

    /**
     * Asserts that the zip file contains exactly the expected entries and that their content matches.
     * This version does not assume any specific order of entries in the zip file.
     *
     * @param zipFile         The generated zip file.
     * @param expectedEntries A map of expected entry names and their content.
     * @throws IOException if an I/O error occurs.
     */
    private void assertZipContents(final File zipFile, final Map<String, byte[]> expectedEntries) throws IOException {
        final Map<String, byte[]> remainingEntries = new HashMap<>(expectedEntries);
        try (ZipFile zf = ZipFile.builder().setFile(zipFile).get()) {
            final Enumeration<ZipArchiveEntry> entriesInPhysicalOrder = zf.getEntriesInPhysicalOrder();
            while (entriesInPhysicalOrder.hasMoreElements()) {
                final ZipArchiveEntry zipArchiveEntry = entriesInPhysicalOrder.nextElement();
                try (InputStream inputStream = zf.getInputStream(zipArchiveEntry)) {
                    final byte[] actual = IOUtils.toByteArray(inputStream);
                    final byte[] expected = remainingEntries.remove(zipArchiveEntry.getName());
                    assertNotNull(expected, "Found unexpected entry: " + zipArchiveEntry.getName());
                    assertArrayEquals(expected, actual, "Content mismatch for entry: " + zipArchiveEntry.getName());
                }
            }
        }
        assertTrue(remainingEntries.isEmpty(), "Some expected entries were not found in the zip: " + remainingEntries.keySet());
    }

    /**
     * Asserts that the zip file contains the expected entries in the exact order they were submitted.
     *
     * @param zipFile         The generated zip file.
     * @param expectedEntries A map of expected entry names and their content.
     * @throws IOException if an I/O error occurs.
     */
    private void assertZipContentsAndOrderInvariance(final File zipFile, final Map<String, byte[]> expectedEntries) throws IOException {
        final Map<String, byte[]> remainingEntries = new HashMap<>(expectedEntries);
        try (ZipFile zf = ZipFile.builder().setFile(zipFile).get()) {
            final Enumeration<ZipArchiveEntry> entriesInPhysicalOrder = zf.getEntriesInPhysicalOrder();
            int i = 0;
            while (entriesInPhysicalOrder.hasMoreElements()) {
                final ZipArchiveEntry zipArchiveEntry = entriesInPhysicalOrder.nextElement();
                final String expectedName = "file" + i;
                assertEquals(expectedName, zipArchiveEntry.getName(), "Entry order mismatch at index " + i);

                try (InputStream inputStream = zf.getInputStream(zipArchiveEntry)) {
                    final byte[] actual = IOUtils.toByteArray(inputStream);
                    final byte[] expected = remainingEntries.remove(zipArchiveEntry.getName());
                    assertNotNull(expected, "Found unexpected entry: " + zipArchiveEntry.getName());
                    assertArrayEquals(expected, actual, "Content mismatch for entry: " + zipArchiveEntry.getName());
                }
                i++;
            }
        }
        assertTrue(remainingEntries.isEmpty(), "Some expected entries were not found in the zip: " + remainingEntries.keySet());
    }
}