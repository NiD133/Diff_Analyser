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
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;

import org.apache.commons.compress.AbstractTempDirTest;
import org.apache.commons.compress.AbstractTest;
import org.apache.commons.compress.parallel.FileBasedScatterGatherBackingStore;
import org.apache.commons.compress.parallel.InputStreamSupplier;
import org.apache.commons.compress.parallel.ScatterGatherBackingStoreSupplier;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the callable-based API of {@link ParallelScatterZipCreator}.
 * <p>
 * This class contains helper methods to test the creation of ZIP archives
 * by submitting compression tasks as {@link Callable} objects. This allows for
 * flexible testing of different submission strategies (e.g., immediate execution vs. batch submission).
 * </p>
 */
public class ParallelScatterZipCreatorCallableApiTest extends AbstractTempDirTest {

    /** Maximum size for test files from resources to be included in the test archive (1MB). */
    private static final long MAX_TEST_FILE_SIZE_BYTES = 1024 * 1024;

    /** Maximum number of test files from resources to include in the test archive. */
    private static final int MAX_TEST_FILES_TO_COMPRESS = 50;

    /** Number of small, dynamically generated entries to create for testing. */
    private static final int NUM_GENERATED_ENTRIES = 5000;

    /**
     * A factory for creating a {@link CallableTaskSubmitter}. This abstraction allows tests
     * to define how the created {@link Callable} tasks are handled (e.g., submitted to an
     * executor, collected in a list).
     */
    @FunctionalInterface
    private interface CallableTaskSubmitterFactory extends Function<ParallelScatterZipCreator, CallableTaskSubmitter> {
        // empty
    }

    /**
     * A consumer for {@link Callable} tasks. It defines the action to be performed
     * when a compression task is created.
     */
    @FunctionalInterface
    private interface CallableTaskSubmitter extends Consumer<Callable<? extends ScatterZipOutputStream>> {
        // empty
    }

    /**
     * Creates a ZIP archive from generated in-memory entries using the callable API and verifies its contents and entry order.
     *
     * @param submitterFactory A factory to create the task submitter.
     * @param result           The file to write the archive to.
     * @throws Exception if an error occurs.
     */
    private void createZipWithGeneratedEntriesAndVerify(final CallableTaskSubmitterFactory submitterFactory, final File result) throws Exception {
        createZipWithGeneratedEntriesAndVerify(submitterFactory, Deflater.DEFAULT_COMPRESSION, result);
    }

    /**
     * Creates a ZIP archive from generated in-memory entries using the callable API and verifies its contents and entry order.
     *
     * @param submitterFactory A factory to create the task submitter.
     * @param compressionLevel The compression level to use.
     * @param result           The file to write the archive to.
     * @throws Exception if an error occurs.
     */
    private void createZipWithGeneratedEntriesAndVerify(final CallableTaskSubmitterFactory submitterFactory, final int compressionLevel, final File result) throws Exception {
        final Map<String, byte[]> expectedEntries;
        final ParallelScatterZipCreator zipCreator;

        // Setup and Execution
        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(result)) {
            zos.setEncoding(StandardCharsets.UTF_8.name());
            final ExecutorService es = Executors.newFixedThreadPool(1);
            final ScatterGatherBackingStoreSupplier supp = () -> new FileBasedScatterGatherBackingStore(createTempFile("parallelscatter", "n1"));
            zipCreator = new ParallelScatterZipCreator(es, supp, compressionLevel);

            expectedEntries = submitGeneratedEntriesAsCallables(zipCreator, submitterFactory.apply(zipCreator));
            zipCreator.writeTo(zos);
        }

        // Verification
        verifyZipContents(result, expectedEntries, true); // Check order for generated entries
        assertNotNull(zipCreator.getStatisticsMessage());
    }

    /**
     * Creates a ZIP archive from test files on disk using the callable API and verifies its contents.
     *
     * @param submitterFactory A factory to create the task submitter.
     * @param compressionLevel The compression level to use.
     * @param result           The file to write the archive to.
     * @throws Exception if an error occurs.
     */
    private void createZipFromTestFilesWithCallablesAndVerify(final CallableTaskSubmitterFactory submitterFactory, final int compressionLevel, final File result) throws Exception {
        final ParallelScatterZipCreator zipCreator;
        final Map<String, byte[]> expectedEntries;

        // Setup and Execution
        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(result)) {
            zos.setEncoding(StandardCharsets.UTF_8.name());
            final ExecutorService es = Executors.newFixedThreadPool(1);
            final ScatterGatherBackingStoreSupplier supp = () -> new FileBasedScatterGatherBackingStore(createTempFile("parallelscatter", "n1"));
            zipCreator = new ParallelScatterZipCreator(es, supp, compressionLevel);

            expectedEntries = submitTestFilesAsCallables(zipCreator, submitterFactory.apply(zipCreator));
            zipCreator.writeTo(zos);
        }

        // Verification
        verifyZipContents(result, expectedEntries, false); // Do not check order for file system entries
        assertNotNull(zipCreator.getStatisticsMessage());
    }

    private ZipArchiveEntry createZipArchiveEntry(final Map<String, byte[]> entries, final int index, final byte[] payloadBytes) {
        final String entryName = "file" + index;
        final ZipArchiveEntry za = new ZipArchiveEntry(entryName);
        entries.put(entryName, payloadBytes);
        za.setMethod(ZipEntry.DEFLATED);
        za.setSize(payloadBytes.length);
        za.setUnixMode(UnixStat.FILE_FLAG | 0664);
        return za;
    }

    /**
     * Creates and submits {@value #NUM_GENERATED_ENTRIES} archive entries as callables.
     *
     * @param zipCreator The parallel zip creator.
     * @param submitter  The consumer for the created callable tasks.
     * @return A map of entry names to their expected content.
     */
    private Map<String, byte[]> submitGeneratedEntriesAsCallables(final ParallelScatterZipCreator zipCreator, final CallableTaskSubmitter submitter) {
        final Map<String, byte[]> entries = new HashMap<>();
        for (int i = 0; i < NUM_GENERATED_ENTRIES; i++) {
            final byte[] payloadBytes = ("content" + i).getBytes();
            final ZipArchiveEntry za = createZipArchiveEntry(entries, i, payloadBytes);
            final InputStreamSupplier iss = () -> new ByteArrayInputStream(payloadBytes);

            final Callable<ScatterZipOutputStream> callable;
            if (i % 2 == 0) {
                // Test the (ZipArchiveEntry, InputStreamSupplier) API
                callable = zipCreator.createCallable(za, iss);
            } else {
                // Test the ZipArchiveEntryRequestSupplier API
                final ZipArchiveEntryRequestSupplier zaSupplier = () -> ZipArchiveEntryRequest.createZipArchiveEntryRequest(za, iss);
                callable = zipCreator.createCallable(zaSupplier);
            }
            submitter.accept(callable);
        }
        return entries;
    }

    /**
     * Scans the test resources directory and submits files that meet the size criteria as callables.
     * <p>
     * This method skips directories and files larger than {@value #MAX_TEST_FILE_SIZE_BYTES}. It also limits
     * the total number of files to {@value #MAX_TEST_FILES_TO_COMPRESS}.
     * </p>
     *
     * @param zipCreator The parallel zip creator.
     * @param submitter  The consumer for the created callable tasks.
     * @return A map of entry names to their expected content.
     * @throws IOException if an I/O error occurs.
     */
    private Map<String, byte[]> submitTestFilesAsCallables(final ParallelScatterZipCreator zipCreator, final CallableTaskSubmitter submitter) throws IOException {
        final Map<String, byte[]> entries = new HashMap<>();
        final File baseDir = AbstractTest.getFile(""); // Gets src/test/resources
        int filesCount = 0;
        for (final File file : baseDir.listFiles()) {
            if (filesCount >= MAX_TEST_FILES_TO_COMPRESS) {
                break;
            }
            if (file.isDirectory() || file.length() > MAX_TEST_FILE_SIZE_BYTES) {
                continue;
            }

            final byte[] fileBytes = Files.readAllBytes(file.toPath());
            entries.put(file.getName(), fileBytes);

            final ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file.getName());
            zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
            zipArchiveEntry.setSize(file.length());
            zipArchiveEntry.setUnixMode(UnixStat.FILE_FLAG | 0664);

            final InputStreamSupplier iss = () -> {
                try {
                    return Files.newInputStream(file.toPath());
                } catch (final IOException e) {
                    throw new UncheckedIOException(e);
                }
            };

            final Callable<ScatterZipOutputStream> callable;
            if (filesCount % 2 == 0) {
                callable = zipCreator.createCallable(zipArchiveEntry, iss);
            } else {
                final ZipArchiveEntryRequestSupplier zaSupplier = () -> ZipArchiveEntryRequest.createZipArchiveEntryRequest(zipArchiveEntry, iss);
                callable = zipCreator.createCallable(zaSupplier);
            }
            submitter.accept(callable);
            filesCount++;
        }
        return entries;
    }

    @Test
    @DisplayName("ParallelScatterZipCreator constructor should throw IllegalArgumentException for invalid compression level")
    void constructorShouldThrowForInvalidCompressionLevel() {
        final int invalidCompressionLevel = Deflater.BEST_COMPRESSION + 1;
        final ExecutorService es = Executors.newFixedThreadPool(1);
        final ScatterGatherBackingStoreSupplier supplier = () -> new FileBasedScatterGatherBackingStore(createTempFile("parallelscatter", "n1"));

        assertThrows(IllegalArgumentException.class, () -> new ParallelScatterZipCreator(es, supplier, invalidCompressionLevel));

        es.shutdownNow();
    }

    /**
     * Verifies the contents of a ZIP file against a map of expected entries.
     *
     * @param zipFile         The ZIP file to verify.
     * @param expectedEntries A map of expected entry names to their byte content. This map will be modified.
     * @param checkOrder      If true, verifies that the entry order matches the sequence "file0", "file1", ...
     * @throws IOException if an I/O error occurs.
     */
    private void verifyZipContents(final File zipFile, final Map<String, byte[]> expectedEntries, final boolean checkOrder) throws IOException {
        try (ZipFile zf = ZipFile.builder().setFile(zipFile).get()) {
            final Enumeration<ZipArchiveEntry> entriesInPhysicalOrder = zf.getEntriesInPhysicalOrder();
            int entryIndex = 0;
            while (entriesInPhysicalOrder.hasMoreElements()) {
                final ZipArchiveEntry zipArchiveEntry = entriesInPhysicalOrder.nextElement();
                final String entryName = zipArchiveEntry.getName();

                try (InputStream inputStream = zf.getInputStream(zipArchiveEntry)) {
                    final byte[] actualBytes = IOUtils.toByteArray(inputStream);
                    final byte[] expectedBytes = expectedEntries.remove(entryName);

                    assertNotNull(expectedBytes, "Found unexpected entry " + entryName);
                    assertArrayEquals(expectedBytes, actualBytes, "Content mismatch for entry " + entryName);
                }

                if (checkOrder) {
                    assertEquals("file" + entryIndex++, entryName, "Entry order mismatch");
                }
            }
        }
        assertTrue(expectedEntries.isEmpty(), "Some expected entries were not found in the zip file: " + expectedEntries.keySet());
    }
}