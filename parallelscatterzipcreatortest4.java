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

public class ParallelScatterZipCreatorTestTest4 extends AbstractTempDirTest {

    // The test will not read files larger than 1MB
    private static final long MAX_FILE_SIZE_BYTES = 1024 * 1024;

    // The test will not compress more than 50 files
    private static final int MAX_FILES_TO_COMPRESS = 50;

    /**
     * Tests that ParallelScatterZipCreator can correctly create a ZIP archive
     * using the createCallable() and submitStreamAwareCallable() methods with the highest compression level.
     */
    @Test
    void shouldCreateZipUsingCallablesAndBestCompression() throws Exception {
        // Arrange
        final File outputZipFile = createTempFile("parallelScatterGather", ".zip");
        final ExecutorService executorService = Executors.newFixedThreadPool(1);
        final ScatterGatherBackingStoreSupplier backingStoreSupplier =
            () -> new FileBasedScatterGatherBackingStore(createTempFile("parallelscatter-backing", ""));

        final ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator(
            executorService, backingStoreSupplier, Deflater.BEST_COMPRESSION);

        // Submit files from the test resources directory for compression
        final Map<String, byte[]> expectedEntries = submitTestFilesToCreator(zipCreator);

        // Act
        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(outputZipFile)) {
            zos.setEncoding(StandardCharsets.UTF_8.name());
            zipCreator.writeTo(zos);
        }

        // Assert
        assertNotNull(zipCreator.getStatisticsMessage(), "Statistics message should not be null");
        assertZipFileContainsEntries(outputZipFile, expectedEntries);
    }

    /**
     * Scans the test resources directory, creates compression tasks for the files,
     * and submits them to the ParallelScatterZipCreator.
     *
     * @param zipCreator The creator instance to submit tasks to.
     * @return A map of expected entry names to their file content.
     * @throws IOException if an I/O error occurs reading the files.
     */
    private Map<String, byte[]> submitTestFilesToCreator(final ParallelScatterZipCreator zipCreator) throws IOException {
        final Map<String, byte[]> expectedEntries = new HashMap<>();
        final File testResourceDir = AbstractTest.getFile("");
        int filesCount = 0;

        for (final File file : testResourceDir.listFiles()) {
            if (filesCount >= MAX_FILES_TO_COMPRESS) {
                break;
            }
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

            // To test both overloads of createCallable(), we alternate between them.
            final Callable<ScatterZipOutputStream> callable;
            if (filesCount % 2 == 0) {
                callable = zipCreator.createCallable(zipArchiveEntry, inputStreamSupplier);
            } else {
                final ZipArchiveEntryRequestSupplier requestSupplier =
                    () -> ZipArchiveEntryRequest.createZipArchiveEntryRequest(zipArchiveEntry, inputStreamSupplier);
                callable = zipCreator.createCallable(requestSupplier);
            }

            zipCreator.submitStreamAwareCallable(callable);
            filesCount++;
        }
        return expectedEntries;
    }

    /**
     * Asserts that the created ZIP file contains exactly the expected entries with matching content.
     *
     * @param zipFile         The ZIP file to verify.
     * @param expectedEntries A map of expected entry names to their content.
     * @throws IOException if an I/O error occurs reading the ZIP file.
     */
    private void assertZipFileContainsEntries(final File zipFile, final Map<String, byte[]> expectedEntries) throws IOException {
        final Map<String, byte[]> remainingEntries = new HashMap<>(expectedEntries);
        try (ZipFile zf = ZipFile.builder().setFile(zipFile).get()) {
            final Enumeration<ZipArchiveEntry> entries = zf.getEntries();
            while (entries.hasMoreElements()) {
                final ZipArchiveEntry entry = entries.nextElement();
                final String entryName = entry.getName();

                assertTrue(remainingEntries.containsKey(entryName), "Found unexpected entry in ZIP: " + entryName);

                try (InputStream inputStream = zf.getInputStream(entry)) {
                    final byte[] actualContent = IOUtils.toByteArray(inputStream);
                    final byte[] expectedContent = remainingEntries.remove(entryName);
                    assertArrayEquals(expectedContent, actualContent, "Content mismatch for entry: " + entryName);
                }
            }
        }
        assertTrue(remainingEntries.isEmpty(), "Some expected entries were not found in the ZIP: " + remainingEntries.keySet());
    }
}