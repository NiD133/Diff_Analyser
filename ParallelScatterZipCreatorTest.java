/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ParallelScatterZipCreatorTest extends AbstractTempDirTest {

    private static final int SYNTHETIC_ENTRIES_COUNT = 5000;
    private static final int MAX_TEST_RESOURCE_FILES = 50;
    private static final long MAX_TEST_RESOURCE_FILE_SIZE = 1024 * 1024; // 1MB

    @Test
    @DisplayName("Creates a ZIP archive using the default temporary folder")
    void createsArchiveUsingDefaultTempFolder() throws Exception {
        // Arrange
        final File result = createTempFile("parallelScatterGather1", ".zip");
        final ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator();
        final Map<String, byte[]> entries = createSyntheticEntries(SYNTHETIC_ENTRIES_COUNT);

        // Act
        addEntries(zipCreator, entries);
        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(result)) {
            zipCreator.writeTo(zos);
        }

        // Assert
        assertZipFileContents(result, entries, true);
        assertNotNull(zipCreator.getStatisticsMessage());
    }

    @Test
    @DisplayName("Creates a ZIP archive using a custom temporary folder")
    void createsArchiveUsingCustomTempFolder() throws Exception {
        // Arrange
        final File result = createTempFile("parallelScatterGather1", ".zip");
        final Path customTempDir = Paths.get("target/custom-temp-dir");
        Files.createDirectories(customTempDir);

        final ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        final ScatterGatherBackingStoreSupplier backingStoreSupplier = new DefaultBackingStoreSupplier(customTempDir);
        final ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator(es, backingStoreSupplier);
        final Map<String, byte[]> entries = createSyntheticEntries(SYNTHETIC_ENTRIES_COUNT);

        // Act
        addEntries(zipCreator, entries);
        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(result)) {
            zipCreator.writeTo(zos);
        }

        // Assert
        assertZipFileContents(result, entries, true);
        assertNotNull(zipCreator.getStatisticsMessage());
    }

    @Test
    @DisplayName("Creates a ZIP archive using the callable API with the submit() method")
    void createsArchiveUsingCallableApiWithSubmit() throws Exception {
        testCallableApi(zipCreator -> zipCreator::submit, Deflater.DEFAULT_COMPRESSION, false);
    }

    @Test
    @DisplayName("Creates a ZIP archive using the callable API with the submitStreamAwareCallable() method")
    void createsArchiveUsingCallableApiWithSubmitStreamAwareCallable() throws Exception {
        testCallableApi(zipCreator -> zipCreator::submitStreamAwareCallable, Deflater.DEFAULT_COMPRESSION, false);
    }

    @Test
    @DisplayName("Creates a ZIP archive with no compression using the callable API")
    void createsArchiveWithNoCompressionUsingCallableApi() throws Exception {
        testCallableApi(zipCreator -> zipCreator::submit, Deflater.NO_COMPRESSION, true);
    }

    @Test
    @DisplayName("Creates a ZIP archive with best compression using the callable API")
    void createsArchiveWithBestCompressionUsingCallableApi() throws Exception {
        testCallableApi(zipCreator -> zipCreator::submitStreamAwareCallable, Deflater.BEST_COMPRESSION, true);
    }

    /**
     * Helper method to test the callable-based API of {@link ParallelScatterZipCreator}.
     *
     * @param submissionTask A consumer that accepts a callable and submits it to the zipCreator.
     * @param compressionLevel The compression level to use.
     * @param useTestResources Whether to use real files from test resources or synthetic in-memory entries.
     */
    private void testCallableApi(final Consumer<Callable<ScatterZipOutputStream>> submissionTask, final int compressionLevel, final boolean useTestResources)
            throws Exception {
        // Arrange
        final File result = createTempFile("parallelScatterGather", ".zip");
        final ExecutorService es = Executors.newFixedThreadPool(1);
        final ScatterGatherBackingStoreSupplier supp = () -> new FileBasedScatterGatherBackingStore(createTempFile("parallelscatter", "n1"));
        final ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator(es, supp, compressionLevel);

        final Map<String, byte[]> entries = useTestResources ? createEntriesFromTestResources() : createSyntheticEntries(SYNTHETIC_ENTRIES_COUNT);

        // Act
        addEntriesAsCallables(zipCreator, entries, submissionTask);
        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(result)) {
            zipCreator.writeTo(zos);
        }

        // Assert
        assertZipFileContents(result, entries, !useTestResources); // Order is only predictable for synthetic entries
        assertNotNull(zipCreator.getStatisticsMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = { Deflater.BEST_COMPRESSION + 1, Deflater.DEFAULT_COMPRESSION - 1 })
    @DisplayName("Constructor throws IllegalArgumentException for invalid compression levels")
    void constructorThrowsForInvalidCompressionLevel(final int invalidCompressionLevel) {
        // Arrange
        final ExecutorService es = Executors.newFixedThreadPool(1);
        final ScatterGatherBackingStoreSupplier supp = () -> new FileBasedScatterGatherBackingStore(createTempFile());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new ParallelScatterZipCreator(es, supp, invalidCompressionLevel));
        es.shutdownNow();
    }

    @Test
    @Disabled("[COMPRESS-639] Test fails with a NullPointerException on close()")
    @DisplayName("Adding multiple entries with the same name should not throw an exception")
    void addArchiveEntryWithSameNameDoesNotThrowException() throws IOException, ExecutionException, InterruptedException {
        // Arrange
        final String fileContent = "A";
        final int numOfFiles = 100;
        final LinkedList<InputStream> inputStreams = new LinkedList<>();
        for (int i = 0; i < numOfFiles; i++) {
            inputStreams.add(new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8)));
        }

        final ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator();

        // Act
        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(new ByteArrayOutputStream())) {
            zos.setUseZip64(Zip64Mode.Always);

            for (final InputStream inputStream : inputStreams) {
                final ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("./dir/myfile.txt");
                zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
                zipCreator.addArchiveEntry(zipArchiveEntry, () -> inputStream);
            }

            zipCreator.writeTo(zos);
        } // Assert: Test passes if no exception is thrown, especially on close().
    }

    /**
     * Creates a map of synthetic file entries for testing.
     */
    private Map<String, byte[]> createSyntheticEntries(final int numEntries) {
        final Map<String, byte[]> entries = new HashMap<>();
        for (int i = 0; i < numEntries; i++) {
            final String name = "file" + i;
            final byte[] payload = ("content" + i).getBytes(StandardCharsets.UTF_8);
            entries.put(name, payload);
        }
        return entries;
    }

    /**
     * Adds entries to the zip creator, alternating between the two 'addArchiveEntry' overloads.
     */
    private void addEntries(final ParallelScatterZipCreator zipCreator, final Map<String, byte[]> entries) {
        int i = 0;
        for (final Map.Entry<String, byte[]> entry : entries.entrySet()) {
            final byte[] payloadBytes = entry.getValue();
            final ZipArchiveEntry za = createZipArchiveEntry(entry.getKey(), payloadBytes);
            final InputStreamSupplier iss = () -> new ByteArrayInputStream(payloadBytes);

            // Exercise both overloads of the addArchiveEntry method
            if (i++ % 2 == 0) {
                zipCreator.addArchiveEntry(za, iss);
            } else {
                final ZipArchiveEntryRequestSupplier zaSupplier = () -> ZipArchiveEntryRequest.createZipArchiveEntryRequest(za, iss);
                zipCreator.addArchiveEntry(zaSupplier);
            }
        }
    }

    /**
     * Adds entries to the zip creator as callables, alternating between the two 'createCallable' overloads.
     */
    private void addEntriesAsCallables(final ParallelScatterZipCreator zipCreator, final Map<String, byte[]> entries,
            final Consumer<Callable<ScatterZipOutputStream>> submissionTask) {
        int i = 0;
        for (final Map.Entry<String, byte[]> entry : entries.entrySet()) {
            final byte[] payloadBytes = entry.getValue();
            final ZipArchiveEntry za = createZipArchiveEntry(entry.getKey(), payloadBytes);
            final InputStreamSupplier iss = () -> new ByteArrayInputStream(payloadBytes);

            final Callable<ScatterZipOutputStream> callable;
            // Exercise both overloads of the createCallable method
            if (i++ % 2 == 0) {
                callable = zipCreator.createCallable(za, iss);
            } else {
                final ZipArchiveEntryRequestSupplier zaSupplier = () -> ZipArchiveEntryRequest.createZipArchiveEntryRequest(za, iss);
                callable = zipCreator.createCallable(zaSupplier);
            }
            submissionTask.accept(callable);
        }
    }

    /**
     * Creates a map of entries from files in the test resources directory.
     */
    private Map<String, byte[]> createEntriesFromTestResources() throws IOException {
        final Map<String, byte[]> entries = new HashMap<>();
        final File baseDir = AbstractTest.getFile("");
        int filesCount = 0;
        for (final File file : baseDir.listFiles()) {
            if (filesCount >= MAX_TEST_RESOURCE_FILES) {
                break;
            }
            if (file.isDirectory() || file.length() > MAX_TEST_RESOURCE_FILE_SIZE) {
                continue;
            }
            entries.put(file.getName(), Files.readAllBytes(file.toPath()));
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
     * Asserts that the given ZIP file contains exactly the expected entries with correct content.
     *
     * @param zipFile The ZIP file to verify.
     * @param expectedEntries A map of expected entry names to their content. This map is modified.
     * @param checkOrder If true, asserts that the order of entries in the ZIP matches the iteration order of the map keys.
     */
    private void assertZipFileContents(final File zipFile, final Map<String, byte[]> expectedEntries, final boolean checkOrder) throws IOException {
        final Map<String, byte[]> entriesToFind = new HashMap<>(expectedEntries);
        try (ZipFile zf = ZipFile.builder().setFile(zipFile).get()) {
            final Enumeration<ZipArchiveEntry> entriesInPhysicalOrder = zf.getEntriesInPhysicalOrder();
            int i = 0;
            while (entriesInPhysicalOrder.hasMoreElements()) {
                final ZipArchiveEntry zae = entriesInPhysicalOrder.nextElement();
                final byte[] expected = entriesToFind.remove(zae.getName());
                assertNotNull(expected, "Found unexpected entry " + zae.getName());

                try (InputStream inputStream = zf.getInputStream(zae)) {
                    final byte[] actual = IOUtils.toByteArray(inputStream);
                    assertArrayEquals(expected, actual, "Content mismatch for entry " + zae.getName());
                }

                if (checkOrder) {
                    assertEquals("file" + i++, zae.getName(), "Entry order mismatch for " + zae.getName());
                }
            }
        }
        assertTrue(entriesToFind.isEmpty(), "Some expected entries were not found in the archive: " + entriesToFind.keySet());
    }
}