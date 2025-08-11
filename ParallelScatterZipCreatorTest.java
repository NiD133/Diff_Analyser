package org.apache.commons.compress.archivers.zip;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for ParallelScatterZipCreator that emphasize clarity and intent.
 * 
 * - Each test writes a zip file, then verifies its content.
 * - Helper methods encapsulate common steps: creating entries, submitting work, and verifying results.
 * - Two submission styles are covered:
 *   1) submit(Callable<?>) and
 *   2) submitStreamAwareCallable(Callable<? extends ScatterZipOutputStream>)
 */
class ParallelScatterZipCreatorTest extends AbstractTempDirTest {

    /**
     * Consumer accepting callables created by ParallelScatterZipCreator.
     * Intentionally typed to ScatterZipOutputStream to cover submitStreamAwareCallable.
     * Note: zipCreator::submit also matches via erasure at call sites.
     */
    private interface CallableConsumer extends Consumer<Callable<? extends ScatterZipOutputStream>> {
        // empty
    }

    /**
     * Provides a CallableConsumer bound to a specific ParallelScatterZipCreator, e.g. zipCreator::submit.
     */
    private interface CallableConsumerSupplier extends Function<ParallelScatterZipCreator, CallableConsumer> {
        // empty
    }

    // Limits for resource-backed file tests (not the synthetic in-memory ones).
    private static final long MAX_TEST_FILE_SIZE = 1024L * 1024L; // 1MB
    private static final int MAX_TEST_FILES_COUNT = 50;

    // Number of synthetic entries for the in-memory tests
    private final int NUM_ITEMS = 5000;

    // --------------------------------------------------------------------------------------------
    // Test cases
    // --------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Submit callables using submit(...)")
    void testCallableApiUsingSubmit() throws Exception {
        final File zipFile = createTempFile("parallelScatterGather-submit", ".zip");
        runCallableApi(zipCreator -> zipCreator::submit, zipFile);
    }

    @Test
    @DisplayName("Submit callables using submitStreamAwareCallable(...)")
    void testCallableApiUsingSubmitStreamAwareCallable() throws Exception {
        final File zipFile = createTempFile("parallelScatterGather-submitStreamAware", ".zip");
        runCallableApi(zipCreator -> zipCreator::submitStreamAwareCallable, zipFile);
    }

    @Test
    @DisplayName("Highest compression level using submitStreamAwareCallable(...) with resource files")
    void testCallableApiWithHighestLevelUsingSubmitStreamAwareCallable() throws Exception {
        final File zipFile = createTempFile("parallelScatterGather-highest", ".zip");
        runCallableApiWithTestFiles(zipCreator -> zipCreator::submitStreamAwareCallable, Deflater.BEST_COMPRESSION, zipFile);
    }

    @Test
    @DisplayName("Lowest compression level using submit(...) with resource files")
    void testCallableWithLowestLevelApiUsingSubmit() throws Exception {
        final File zipFile = createTempFile("parallelScatterGather-lowest", ".zip");
        runCallableApiWithTestFiles(zipCreator -> zipCreator::submit, Deflater.NO_COMPRESSION, zipFile);
    }

    @Test
    @DisplayName("Concurrent creation using default temp folder")
    void testConcurrentDefaultTempFolder() throws Exception {
        final File zipFile = createTempFile("parallelScatterGather-defaultTemp", ".zip");
        final ParallelScatterZipCreator zipCreator;
        final Map<String, byte[]> expectedEntries;

        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(zipFile)) {
            zos.setEncoding(StandardCharsets.UTF_8.name());

            zipCreator = new ParallelScatterZipCreator();
            expectedEntries = enqueueSyntheticEntries(zipCreator);

            zipCreator.writeTo(zos);
        }

        assertZipContainsExpectedContentInOrder(zipFile, expectedEntries);
        assertTrue(expectedEntries.isEmpty(), "All expected entries must be found and removed");
        assertNotNull(zipCreator.getStatisticsMessage(), "Statistics message should be available");
    }

    @Test
    @DisplayName("Concurrent creation using custom temp folder")
    void testConcurrentCustomTempFolder() throws Exception {
        final File zipFile = createTempFile("parallelScatterGather-customTemp", ".zip");
        final ParallelScatterZipCreator zipCreator;
        final Map<String, byte[]> expectedEntries;

        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(zipFile)) {
            zos.setEncoding(StandardCharsets.UTF_8.name());

            final Path customTempDir = Paths.get("target/custom-temp-dir");
            Files.createDirectories(customTempDir);

            zipCreator = new ParallelScatterZipCreator(
                Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()),
                new DefaultBackingStoreSupplier(customTempDir)
            );

            expectedEntries = enqueueSyntheticEntries(zipCreator);
            zipCreator.writeTo(zos);
        }

        assertZipContainsExpectedContentInOrder(zipFile, expectedEntries);
        assertTrue(expectedEntries.isEmpty(), "All expected entries must be found and removed");
        assertNotNull(zipCreator.getStatisticsMessage(), "Statistics message should be available");
    }

    @Test
    @DisplayName("Reject compression level > BEST_COMPRESSION")
    void testThrowsExceptionWithCompressionLevelTooBig() {
        final int illegalCompressionLevel = Deflater.BEST_COMPRESSION + 1;
        final ExecutorService es = Executors.newFixedThreadPool(1);

        assertThrows(IllegalArgumentException.class,
            () -> new ParallelScatterZipCreator(
                es,
                () -> new FileBasedScatterGatherBackingStore(createTempFile("parallelscatter", "n1")),
                illegalCompressionLevel
            ),
            "Compression level above BEST_COMPRESSION should be rejected"
        );
        es.shutdownNow();
    }

    @Test
    @DisplayName("Reject compression level < NO_COMPRESSION (except DEFAULT)")
    void testThrowsExceptionWithCompressionLevelTooSmall() {
        final int illegalCompressionLevel = Deflater.DEFAULT_COMPRESSION - 1;
        final ExecutorService es = Executors.newFixedThreadPool(1);

        assertThrows(IllegalArgumentException.class,
            () -> new ParallelScatterZipCreator(
                es,
                () -> new FileBasedScatterGatherBackingStore(createTempFile("parallelscatter", "n1")),
                illegalCompressionLevel
            ),
            "Compression level below NO_COMPRESSION (and not DEFAULT) should be rejected"
        );
        es.shutdownNow();
    }

    @Test
    @Disabled("[COMPRESS-639] Reproduces a NullPointerException when submitting the same ZipArchiveEntry name many times")
    void sameZipArchiveEntryNullPointerException() throws IOException, ExecutionException, InterruptedException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        final String fileContent = "A";
        final int numberOfFiles = 100;

        final LinkedList<InputStream> inputs = new LinkedList<>();
        for (int i = 0; i < numberOfFiles; i++) {
            inputs.add(new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8)));
        }

        final ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator();
        try (ZipArchiveOutputStream zaos = new ZipArchiveOutputStream(out)) {
            zaos.setUseZip64(Zip64Mode.Always);

            for (final InputStream in : inputs) {
                final ZipArchiveEntry entry = new ZipArchiveEntry("./dir/myfile.txt");
                entry.setMethod(ZipEntry.DEFLATED);
                zipCreator.addArchiveEntry(entry, () -> in);
            }

            zipCreator.writeTo(zaos);
        } // Historically threw NPE on close() for COMPRESS-639
    }

    // --------------------------------------------------------------------------------------------
    // Test drivers (high-level orchestration)
    // --------------------------------------------------------------------------------------------

    /**
     * Drives the "callable API" tests using synthetic in-memory entries.
     * Uses the provided submission style (submit or submitStreamAwareCallable).
     */
    private void runCallableApi(final CallableConsumerSupplier submitterSupplier, final File zipFile) throws Exception {
        runCallableApi(submitterSupplier, Deflater.DEFAULT_COMPRESSION, zipFile);
    }

    /**
     * Drives the "callable API" tests using synthetic in-memory entries with a specified compression level.
     */
    private void runCallableApi(final CallableConsumerSupplier submitterSupplier, final int compressionLevel, final File zipFile) throws Exception {
        final Map<String, byte[]> expectedEntries;
        final ParallelScatterZipCreator zipCreator;

        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(zipFile)) {
            zos.setEncoding(StandardCharsets.UTF_8.name());

            final ExecutorService es = Executors.newFixedThreadPool(1);
            final ScatterGatherBackingStoreSupplier storeSupplier =
                () -> new FileBasedScatterGatherBackingStore(createTempFile("parallelscatter", "n1"));

            zipCreator = new ParallelScatterZipCreator(es, storeSupplier, compressionLevel);

            expectedEntries = enqueueSyntheticEntriesAsCallables(zipCreator, submitterSupplier.apply(zipCreator));
            zipCreator.writeTo(zos);
        }

        assertZipContainsExpectedContentInOrder(zipFile, expectedEntries);
        assertTrue(expectedEntries.isEmpty(), "All expected entries must be found and removed");
        assertNotNull(zipCreator.getStatisticsMessage(), "Statistics message should be available");
    }

    /**
     * Drives the "callable API" tests using small files from test resources.
     * Limits the number and size of files to keep test execution fast and stable.
     */
    private void runCallableApiWithTestFiles(final CallableConsumerSupplier submitterSupplier, final int compressionLevel, final File zipFile) throws Exception {
        final Map<String, byte[]> expectedEntries;
        final ParallelScatterZipCreator zipCreator;

        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(zipFile)) {
            zos.setEncoding(StandardCharsets.UTF_8.name());

            final ExecutorService es = Executors.newFixedThreadPool(1);
            final ScatterGatherBackingStoreSupplier storeSupplier =
                () -> new FileBasedScatterGatherBackingStore(createTempFile("parallelscatter", "n1"));

            zipCreator = new ParallelScatterZipCreator(es, storeSupplier, compressionLevel);

            expectedEntries = enqueueResourceBackedEntriesAsCallables(zipCreator, submitterSupplier.apply(zipCreator));
            zipCreator.writeTo(zos);
        }

        // Content must match (order is not asserted for resource-backed test).
        assertZipContainsExpectedContent(zipFile, expectedEntries);
        assertNotNull(zipCreator.getStatisticsMessage(), "Statistics message should be available");
    }

    // --------------------------------------------------------------------------------------------
    // Entry creation and submission helpers
    // --------------------------------------------------------------------------------------------

    /**
     * Enqueues NUM_ITEMS synthetic entries in alternating ways:
     * - even: addArchiveEntry(ZipArchiveEntry, InputStreamSupplier)
     * - odd:  addArchiveEntry(ZipArchiveEntryRequestSupplier)
     *
     * Returns a map of expected content for later verification.
     */
    private Map<String, byte[]> enqueueSyntheticEntries(final ParallelScatterZipCreator zipCreator) {
        final Map<String, byte[]> expected = new HashMap<>();

        for (int i = 0; i < NUM_ITEMS; i++) {
            final byte[] payload = ("content" + i).getBytes(StandardCharsets.UTF_8);
            final ZipArchiveEntry zae = newEntry(expected, i, payload);

            final InputStreamSupplier iss = () -> new ByteArrayInputStream(payload);

            if (i % 2 == 0) {
                zipCreator.addArchiveEntry(zae, iss);
            } else {
                final ZipArchiveEntryRequestSupplier request =
                    () -> ZipArchiveEntryRequest.createZipArchiveEntryRequest(zae, iss);
                zipCreator.addArchiveEntry(request);
            }
        }
        return expected;
    }

    /**
     * Same as enqueueSyntheticEntries but using the Callable-based API and the provided submission style.
     */
    private Map<String, byte[]> enqueueSyntheticEntriesAsCallables(final ParallelScatterZipCreator zipCreator,
                                                                  final CallableConsumer submitter) {
        final Map<String, byte[]> expected = new HashMap<>();

        for (int i = 0; i < NUM_ITEMS; i++) {
            final byte[] payload = ("content" + i).getBytes(StandardCharsets.UTF_8);
            final ZipArchiveEntry zae = newEntry(expected, i, payload);
            final InputStreamSupplier iss = () -> new ByteArrayInputStream(payload);

            final Callable<ScatterZipOutputStream> callable =
                (i % 2 == 0)
                    ? zipCreator.createCallable(zae, iss)
                    : zipCreator.createCallable(() -> ZipArchiveEntryRequest.createZipArchiveEntryRequest(zae, iss));

            submitter.accept(callable);
        }
        return expected;
    }

    /**
     * Enqueues small files from the test resources directory as callables, using the given submission style.
     * Skips directories and files larger than MAX_TEST_FILE_SIZE and limits to MAX_TEST_FILES_COUNT files.
     */
    private Map<String, byte[]> enqueueResourceBackedEntriesAsCallables(final ParallelScatterZipCreator zipCreator,
                                                                        final CallableConsumer submitter) throws IOException {
        final Map<String, byte[]> expected = new HashMap<>();

        final File baseDir = AbstractTest.getFile("");
        int filesCount = 0;

        for (final File file : baseDir.listFiles()) {
            if (filesCount >= MAX_TEST_FILES_COUNT) {
                break; // do not compress too many files
            }
            if (file.isDirectory() || file.length() > MAX_TEST_FILE_SIZE) {
                continue; // skip files that are too large or directories
            }

            expected.put(file.getName(), Files.readAllBytes(file.toPath()));

            final ZipArchiveEntry zae = new ZipArchiveEntry(file.getName());
            zae.setMethod(ZipEntry.DEFLATED);
            zae.setSize(file.length());
            zae.setUnixMode(UnixStat.FILE_FLAG | 0664);

            // For tests we return null on failure to keep behavior consistent with the original.
            final InputStreamSupplier iss = () -> {
                try {
                    return Files.newInputStream(file.toPath());
                } catch (final IOException e) {
                    return null;
                }
            };

            final Callable<ScatterZipOutputStream> callable =
                (filesCount % 2 == 0)
                    ? zipCreator.createCallable(zae, iss)
                    : zipCreator.createCallable(() -> ZipArchiveEntryRequest.createZipArchiveEntryRequest(zae, iss));

            submitter.accept(callable);
            filesCount++;
        }

        return expected;
    }

    /**
     * Creates a deflated ZipArchiveEntry for a synthetic "file{i}" and records its expected content.
     */
    private ZipArchiveEntry newEntry(final Map<String, byte[]> expected, final int index, final byte[] payload) {
        final ZipArchiveEntry zae = new ZipArchiveEntry("file" + index);
        expected.put(zae.getName(), payload);
        zae.setMethod(ZipEntry.DEFLATED);
        zae.setSize(payload.length);
        zae.setUnixMode(UnixStat.FILE_FLAG | 0664);
        return zae;
    }

    // --------------------------------------------------------------------------------------------
    // Verification helpers
    // --------------------------------------------------------------------------------------------

    /**
     * Verifies that the zip contains exactly the entries in the expected map and that
     * their content matches. Order is asserted to be file0, file1, ... based on synthetic naming.
     * Removes entries from the map as they are validated.
     */
    private void assertZipContainsExpectedContentInOrder(final File zipFile, final Map<String, byte[]> expected) throws IOException {
        try (ZipFile zf = ZipFile.builder().setFile(zipFile).get()) {
            final Enumeration<ZipArchiveEntry> entries = zf.getEntriesInPhysicalOrder();
            int i = 0;

            while (entries.hasMoreElements()) {
                final ZipArchiveEntry ze = entries.nextElement();
                final String name = ze.getName();

                try (InputStream in = zf.getInputStream(ze)) {
                    final byte[] actual = IOUtils.toByteArray(in);
                    final byte[] exp = expected.remove(name);
                    assertArrayEquals(exp, actual, "Content mismatch for entry " + name);
                }

                // Check order (only valid for synthetic entries)
                assertEquals("file" + i++, name, "Unexpected entry order for " + name);
            }
        }
    }

    /**
     * Verifies that the zip contains exactly the entries in the expected map and that
     * their content matches. Order is not asserted.
     * Removes entries from the map as they are validated.
     */
    private void assertZipContainsExpectedContent(final File zipFile, final Map<String, byte[]> expected) throws IOException {
        try (ZipFile zf = ZipFile.builder().setFile(zipFile).get()) {
            final Enumeration<ZipArchiveEntry> entries = zf.getEntriesInPhysicalOrder();

            while (entries.hasMoreElements()) {
                final ZipArchiveEntry ze = entries.nextElement();
                final String name = ze.getName();

                try (InputStream in = zf.getInputStream(ze)) {
                    final byte[] actual = IOUtils.toByteArray(in);
                    final byte[] exp = expected.remove(name);
                    assertArrayEquals(exp, actual, "Content mismatch for entry " + name);
                }
            }
        }
    }
}