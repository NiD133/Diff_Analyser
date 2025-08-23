package org.apache.commons.compress.archivers.zip;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    // Define constants for test configuration
    private static final long MAX_FILE_SIZE_BYTES = 1024 * 1024; // 1MB
    private static final int MAX_FILES_TO_COMPRESS = 50;

    /**
     * Tests that a ZIP archive can be created from files using the
     * {@link ParallelScatterZipCreator#createCallable(ZipArchiveEntry, InputStreamSupplier)}
     * and {@link ParallelScatterZipCreator#submit(Callable)} methods.
     */
    @Test
    public void createArchiveFromFilesUsingCreateCallableAndSubmit() throws Exception {
        // Arrange
        final File resultZipFile = createTempFile("parallelScatterGather", ".zip");
        // Use a single-threaded executor for deterministic testing
        final ExecutorService executorService = Executors.newFixedThreadPool(1);
        final ScatterGatherBackingStoreSupplier backingStoreSupplier =
            () -> new FileBasedScatterGatherBackingStore(createTempFile("parallelscatter", "temp-store"));

        final ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator(
            executorService, backingStoreSupplier, Deflater.NO_COMPRESSION);

        // Act
        // Add files from test resources to the creator and get a map of expected contents
        final Map<String, byte[]> expectedEntries = addTestResourceFilesToCreator(zipCreator);

        // Write the archive to the output stream
        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(resultZipFile)) {
            zos.setEncoding(StandardCharsets.UTF_8.name());
            zipCreator.writeTo(zos);
        }

        // Assert
        assertNotNull(zipCreator.getStatisticsMessage(), "Statistics message should not be null");
        assertZipFileContents(resultZipFile, expectedEntries);
    }

    /**
     * Scans the test resources directory, adds valid files to the {@link ParallelScatterZipCreator}
     * via its callable API, and returns a map of expected file contents.
     *
     * @param zipCreator The creator instance to add entries to.
     * @return A map where keys are entry names and values are the expected file contents.
     * @throws IOException if an I/O error occurs while reading files.
     */
    private Map<String, byte[]> addTestResourceFilesToCreator(final ParallelScatterZipCreator zipCreator) throws IOException {
        final Map<String, byte[]> expectedEntries = new HashMap<>();
        final File[] resourceFiles = AbstractTest.getFile("").listFiles();
        if (resourceFiles == null) {
            return expectedEntries;
        }

        int filesAdded = 0;
        for (final File file : resourceFiles) {
            if (filesAdded >= MAX_FILES_TO_COMPRESS) {
                break;
            }
            // Skip directories and files that are too large
            if (file.isDirectory() || file.length() > MAX_FILE_SIZE_BYTES) {
                continue;
            }

            final String entryName = file.getName();
            final byte[] fileContent = Files.readAllBytes(file.toPath());
            expectedEntries.put(entryName, fileContent);

            final ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(entryName);
            zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
            zipArchiveEntry.setSize(file.length());
            zipArchiveEntry.setUnixMode(UnixStat.FILE_FLAG | 0664);

            final InputStreamSupplier inputStreamSupplier = () -> Files.newInputStream(file.toPath());

            // Alternate between the two 'createCallable' overloads to test both
            final Callable<ScatterZipOutputStream> callable;
            if (filesAdded % 2 == 0) {
                callable = zipCreator.createCallable(zipArchiveEntry, inputStreamSupplier);
            } else {
                final ZipArchiveEntryRequestSupplier requestSupplier =
                    () -> ZipArchiveEntryRequest.createZipArchiveEntryRequest(zipArchiveEntry, inputStreamSupplier);
                callable = zipCreator.createCallable(requestSupplier);
            }

            zipCreator.submit(callable);
            filesAdded++;
        }
        return expectedEntries;
    }

    /**
     * Asserts that the contents of the created ZIP file match the expected entries.
     *
     * @param zipFile         The ZIP file to validate.
     * @param expectedEntries A map of expected entry names to their byte content.
     * @throws IOException if an I/O error occurs while reading the ZIP file.
     */
    private void assertZipFileContents(final File zipFile, final Map<String, byte[]> expectedEntries) throws IOException {
        // Create a mutable copy of expected entries to track which ones are found.
        final Map<String, byte[]> remainingEntries = new HashMap<>(expectedEntries);

        try (ZipFile zf = ZipFile.builder().setFile(zipFile).get()) {
            final Enumeration<ZipArchiveEntry> entries = zf.getEntriesInPhysicalOrder();
            while (entries.hasMoreElements()) {
                final ZipArchiveEntry entry = entries.nextElement();
                final byte[] expectedContent = remainingEntries.remove(entry.getName());

                assertNotNull(expectedContent, "Found unexpected entry in ZIP: " + entry.getName());

                try (InputStream inputStream = zf.getInputStream(entry)) {
                    final byte[] actualContent = IOUtils.toByteArray(inputStream);
                    assertArrayEquals(expectedContent, actualContent, "Content mismatch for entry: " + entry.getName());
                }
            }
        }

        assertTrue(remainingEntries.isEmpty(), "Some expected entries were not found in the ZIP: " + remainingEntries.keySet());
    }
}