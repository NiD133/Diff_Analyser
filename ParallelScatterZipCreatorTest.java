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
import org.junit.jupiter.api.Test;

/**
 * Tests for ParallelScatterZipCreator which creates ZIP archives using multiple threads
 * for improved performance on multi-core systems.
 */
class ParallelScatterZipCreatorTest extends AbstractTempDirTest {

    // Test configuration constants
    private static final long MAX_TEST_FILE_SIZE_BYTES = 1024 * 1024; // 1MB
    private static final int MAX_TEST_FILES_COUNT = 50;
    private static final int SYNTHETIC_ENTRIES_COUNT = 5000;

    // Functional interfaces for testing different submission strategies
    private interface CallableSubmissionStrategy extends Consumer<Callable<? extends ScatterZipOutputStream>> {
        // Represents a strategy for submitting callables (e.g., submit() vs submitStreamAwareCallable())
    }

    private interface CallableSubmissionStrategyFactory extends Function<ParallelScatterZipCreator, CallableSubmissionStrategy> {
        // Factory for creating submission strategies from a zip creator
    }

    // Core test methods

    @Test
    void testBasicParallelZipCreationWithSubmit() throws Exception {
        final File resultZip = createTempFile("parallelScatterGather2", "");
        testCallableBasedZipCreation(zipCreator -> zipCreator::submit, resultZip);
    }

    @Test
    void testBasicParallelZipCreationWithStreamAwareSubmit() throws Exception {
        final File resultZip = createTempFile("parallelScatterGather3", "");
        testCallableBasedZipCreation(zipCreator -> zipCreator::submitStreamAwareCallable, resultZip);
    }

    @Test
    void testHighCompressionWithRealFiles() throws Exception {
        final File resultZip = createTempFile("parallelScatterGather5", "");
        testCallableBasedZipCreationWithRealFiles(
            zipCreator -> zipCreator::submitStreamAwareCallable, 
            Deflater.BEST_COMPRESSION, 
            resultZip
        );
    }

    @Test
    void testNoCompressionWithRealFiles() throws Exception {
        final File resultZip = createTempFile("parallelScatterGather4", "");
        testCallableBasedZipCreationWithRealFiles(
            zipCreator -> zipCreator::submit, 
            Deflater.NO_COMPRESSION, 
            resultZip
        );
    }

    @Test
    void testParallelZipCreationWithDefaultSettings() throws Exception {
        final File resultZip = createTempFile("parallelScatterGather1", "");
        
        final ParallelScatterZipCreator zipCreator;
        final Map<String, byte[]> expectedEntries;
        
        try (ZipArchiveOutputStream zipOutput = new ZipArchiveOutputStream(resultZip)) {
            zipOutput.setEncoding(StandardCharsets.UTF_8.name());
            zipCreator = new ParallelScatterZipCreator();

            expectedEntries = addSyntheticEntriesToZipCreator(zipCreator);
            zipCreator.writeTo(zipOutput);
        }
        
        verifyZipContentsAndRemoveFoundEntries(resultZip, expectedEntries);
        assertTrue(expectedEntries.isEmpty(), "All entries should have been found in the ZIP file");
        assertNotNull(zipCreator.getStatisticsMessage(), "Statistics message should be available");
    }

    @Test
    void testParallelZipCreationWithCustomTempDirectory() throws Exception {
        final File resultZip = createTempFile("parallelScatterGather1", "");
        
        final ParallelScatterZipCreator zipCreator;
        final Map<String, byte[]> expectedEntries;
        
        try (ZipArchiveOutputStream zipOutput = new ZipArchiveOutputStream(resultZip)) {
            zipOutput.setEncoding(StandardCharsets.UTF_8.name());

            final Path customTempDir = Paths.get("target/custom-temp-dir");
            Files.createDirectories(customTempDir);
            
            zipCreator = new ParallelScatterZipCreator(
                Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()),
                new DefaultBackingStoreSupplier(customTempDir)
            );

            expectedEntries = addSyntheticEntriesToZipCreator(zipCreator);
            zipCreator.writeTo(zipOutput);
        }
        
        verifyZipContentsAndRemoveFoundEntries(resultZip, expectedEntries);
        assertTrue(expectedEntries.isEmpty(), "All entries should have been found in the ZIP file");
        assertNotNull(zipCreator.getStatisticsMessage(), "Statistics message should be available");
    }

    // Validation tests for constructor parameters

    @Test
    void testConstructorRejectsCompressionLevelTooHigh() {
        final int invalidCompressionLevel = Deflater.BEST_COMPRESSION + 1;
        final ExecutorService executor = Executors.newFixedThreadPool(1);
        
        try {
            assertThrows(IllegalArgumentException.class, () -> 
                new ParallelScatterZipCreator(
                    executor,
                    () -> new FileBasedScatterGatherBackingStore(createTempFile("parallelscatter", "n1")), 
                    invalidCompressionLevel
                )
            );
        } finally {
            executor.shutdownNow();
        }
    }

    @Test
    void testConstructorRejectsCompressionLevelTooLow() {
        final int invalidCompressionLevel = Deflater.DEFAULT_COMPRESSION - 1;
        final ExecutorService executor = Executors.newFixedThreadPool(1);
        
        try {
            assertThrows(IllegalArgumentException.class, () -> 
                new ParallelScatterZipCreator(
                    executor,
                    () -> new FileBasedScatterGatherBackingStore(createTempFile("parallelscatter", "n1")), 
                    invalidCompressionLevel
                )
            );
        } finally {
            executor.shutdownNow();
        }
    }

    // Known issue test (disabled)

    @Test
    @Disabled("[COMPRESS-639] - NullPointerException when using same ZipArchiveEntry multiple times")
    public void testSameZipArchiveEntryNullPointerException() throws IOException, ExecutionException, InterruptedException {
        final ByteArrayOutputStream zipOutput = new ByteArrayOutputStream();
        final String fileContent = "A";
        final int duplicateFilesCount = 100;
        
        // Create multiple input streams with the same content
        final LinkedList<InputStream> inputStreams = new LinkedList<>();
        for (int i = 0; i < duplicateFilesCount; i++) {
            inputStreams.add(new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8)));
        }

        final ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator();
        
        try (ZipArchiveOutputStream zipArchiveOutput = new ZipArchiveOutputStream(zipOutput)) {
            zipArchiveOutput.setUseZip64(Zip64Mode.Always);

            // Add the same entry multiple times (this should cause the NPE)
            for (final InputStream inputStream : inputStreams) {
                final ZipArchiveEntry duplicateEntry = new ZipArchiveEntry("./dir/myfile.txt");
                duplicateEntry.setMethod(ZipEntry.DEFLATED);
                zipCreator.addArchiveEntry(duplicateEntry, () -> inputStream);
            }

            zipCreator.writeTo(zipArchiveOutput);
        } // Should throw NullPointerException on close()
    }

    // Helper methods for test execution

    /**
     * Tests ZIP creation using callable-based approach with synthetic entries.
     */
    private void testCallableBasedZipCreation(
            final CallableSubmissionStrategyFactory strategyFactory, 
            final File resultZip) throws Exception {
        testCallableBasedZipCreation(strategyFactory, Deflater.DEFAULT_COMPRESSION, resultZip);
    }

    /**
     * Tests ZIP creation using callable-based approach with synthetic entries and specified compression level.
     */
    private void testCallableBasedZipCreation(
            final CallableSubmissionStrategyFactory strategyFactory, 
            final int compressionLevel, 
            final File resultZip) throws Exception {
        
        final Map<String, byte[]> expectedEntries;
        final ParallelScatterZipCreator zipCreator;
        
        try (ZipArchiveOutputStream zipOutput = new ZipArchiveOutputStream(resultZip)) {
            zipOutput.setEncoding(StandardCharsets.UTF_8.name());
            
            final ExecutorService executor = Executors.newFixedThreadPool(1);
            final ScatterGatherBackingStoreSupplier backingStoreSupplier = 
                () -> new FileBasedScatterGatherBackingStore(createTempFile("parallelscatter", "n1"));

            zipCreator = new ParallelScatterZipCreator(executor, backingStoreSupplier, compressionLevel);
            expectedEntries = addSyntheticEntriesAsCallables(zipCreator, strategyFactory.apply(zipCreator));
            zipCreator.writeTo(zipOutput);
        }

        verifyZipContentsAndRemoveFoundEntries(resultZip, expectedEntries);
        assertTrue(expectedEntries.isEmpty(), "All entries should have been found in the ZIP file");
        assertNotNull(zipCreator.getStatisticsMessage(), "Statistics message should be available");
    }

    /**
     * Tests ZIP creation using callable-based approach with real test files.
     */
    private void testCallableBasedZipCreationWithRealFiles(
            final CallableSubmissionStrategyFactory strategyFactory, 
            final int compressionLevel, 
            final File resultZip) throws Exception {
        
        final ParallelScatterZipCreator zipCreator;
        final Map<String, byte[]> expectedEntries;
        
        try (ZipArchiveOutputStream zipOutput = new ZipArchiveOutputStream(resultZip)) {
            zipOutput.setEncoding(StandardCharsets.UTF_8.name());
            
            final ExecutorService executor = Executors.newFixedThreadPool(1);
            final ScatterGatherBackingStoreSupplier backingStoreSupplier = 
                () -> new FileBasedScatterGatherBackingStore(createTempFile("parallelscatter", "n1"));

            zipCreator = new ParallelScatterZipCreator(executor, backingStoreSupplier, compressionLevel);
            expectedEntries = addRealTestFilesAsCallables(zipCreator, strategyFactory.apply(zipCreator));
            zipCreator.writeTo(zipOutput);
        }

        verifyZipContentsMatch(resultZip, expectedEntries);
        assertNotNull(zipCreator.getStatisticsMessage(), "Statistics message should be available");
    }

    // Helper methods for creating ZIP entries

    /**
     * Creates a ZIP archive entry with the specified index and payload.
     */
    private ZipArchiveEntry createZipArchiveEntry(final Map<String, byte[]> entriesMap, final int index, final byte[] payload) {
        final ZipArchiveEntry entry = new ZipArchiveEntry("file" + index);
        entriesMap.put(entry.getName(), payload);
        entry.setMethod(ZipEntry.DEFLATED);
        entry.setSize(payload.length);
        entry.setUnixMode(UnixStat.FILE_FLAG | 0664);
        return entry;
    }

    /**
     * Adds synthetic entries directly to the ZIP creator (not using callables).
     */
    private Map<String, byte[]> addSyntheticEntriesToZipCreator(final ParallelScatterZipCreator zipCreator) {
        final Map<String, byte[]> entries = new HashMap<>();
        
        for (int i = 0; i < SYNTHETIC_ENTRIES_COUNT; i++) {
            final byte[] payload = ("content" + i).getBytes();
            final ZipArchiveEntry entry = createZipArchiveEntry(entries, i, payload);
            final InputStreamSupplier inputSupplier = () -> new ByteArrayInputStream(payload);
            
            // Alternate between two different methods of adding entries
            if (i % 2 == 0) {
                zipCreator.addArchiveEntry(entry, inputSupplier);
            } else {
                final ZipArchiveEntryRequestSupplier requestSupplier = 
                    () -> ZipArchiveEntryRequest.createZipArchiveEntryRequest(entry, inputSupplier);
                zipCreator.addArchiveEntry(requestSupplier);
            }
        }
        
        return entries;
    }

    /**
     * Adds synthetic entries to the ZIP creator using callables.
     */
    private Map<String, byte[]> addSyntheticEntriesAsCallables(
            final ParallelScatterZipCreator zipCreator, 
            final CallableSubmissionStrategy submissionStrategy) {
        
        final Map<String, byte[]> entries = new HashMap<>();
        
        for (int i = 0; i < SYNTHETIC_ENTRIES_COUNT; i++) {
            final byte[] payload = ("content" + i).getBytes();
            final ZipArchiveEntry entry = createZipArchiveEntry(entries, i, payload);
            final InputStreamSupplier inputSupplier = () -> new ByteArrayInputStream(payload);
            
            final Callable<ScatterZipOutputStream> callable;
            
            // Alternate between two different methods of creating callables
            if (i % 2 == 0) {
                callable = zipCreator.createCallable(entry, inputSupplier);
            } else {
                final ZipArchiveEntryRequestSupplier requestSupplier = 
                    () -> ZipArchiveEntryRequest.createZipArchiveEntryRequest(entry, inputSupplier);
                callable = zipCreator.createCallable(requestSupplier);
            }

            submissionStrategy.accept(callable);
        }
        
        return entries;
    }

    /**
     * Adds real test files to the ZIP creator using callables.
     * Uses files from src/test/resources with size and count limits.
     */
    private Map<String, byte[]> addRealTestFilesAsCallables(
            final ParallelScatterZipCreator zipCreator, 
            final CallableSubmissionStrategy submissionStrategy) throws IOException {
        
        final Map<String, byte[]> entries = new HashMap<>();
        final File testResourcesDir = AbstractTest.getFile("");
        int processedFilesCount = 0;
        
        for (final File file : testResourcesDir.listFiles()) {
            // Stop if we've processed enough files
            if (processedFilesCount >= MAX_TEST_FILES_COUNT) {
                break;
            }

            // Skip directories and files that are too large
            if (file.isDirectory() || file.length() > MAX_TEST_FILE_SIZE_BYTES) {
                continue;
            }

            // Read file content and store it for later verification
            entries.put(file.getName(), Files.readAllBytes(file.toPath()));

            // Create ZIP entry for this file
            final ZipArchiveEntry zipEntry = new ZipArchiveEntry(file.getName());
            zipEntry.setMethod(ZipEntry.DEFLATED);
            zipEntry.setSize(file.length());
            zipEntry.setUnixMode(UnixStat.FILE_FLAG | 0664);

            final InputStreamSupplier inputSupplier = () -> {
                try {
                    return Files.newInputStream(file.toPath());
                } catch (final IOException e) {
                    return null; // This will cause issues, but matches original behavior
                }
            };

            final Callable<ScatterZipOutputStream> callable;
            
            // Alternate between two different methods of creating callables
            if (processedFilesCount % 2 == 0) {
                callable = zipCreator.createCallable(zipEntry, inputSupplier);
            } else {
                final ZipArchiveEntryRequestSupplier requestSupplier = 
                    () -> ZipArchiveEntryRequest.createZipArchiveEntryRequest(zipEntry, inputSupplier);
                callable = zipCreator.createCallable(requestSupplier);
            }

            submissionStrategy.accept(callable);
            processedFilesCount++;
        }
        
        return entries;
    }

    // Helper methods for verification

    /**
     * Verifies that the ZIP file contains the expected entries and removes found entries from the map.
     * Also verifies that entries are in the expected order.
     */
    private void verifyZipContentsAndRemoveFoundEntries(final File zipFile, final Map<String, byte[]> expectedEntries) throws IOException {
        try (ZipFile zip = ZipFile.builder().setFile(zipFile).get()) {
            final Enumeration<ZipArchiveEntry> zipEntries = zip.getEntriesInPhysicalOrder();
            int entryIndex = 0;
            
            while (zipEntries.hasMoreElements()) {
                final ZipArchiveEntry zipEntry = zipEntries.nextElement();
                
                // Verify entry content matches expected
                try (InputStream entryInput = zip.getInputStream(zipEntry)) {
                    final byte[] actualContent = IOUtils.toByteArray(entryInput);
                    final byte[] expectedContent = expectedEntries.remove(zipEntry.getName());
                    assertArrayEquals(expectedContent, actualContent, "Content mismatch for " + zipEntry.getName());
                }
                
                // Verify entry order matches the order they were added
                assertEquals("file" + entryIndex++, zipEntry.getName(), "Entry order mismatch for " + zipEntry.getName());
            }
        }
    }

    /**
     * Verifies that the ZIP file contains entries with content matching the expected entries map.
     */
    private void verifyZipContentsMatch(final File zipFile, final Map<String, byte[]> expectedEntries) throws IOException {
        try (ZipFile zip = ZipFile.builder().setFile(zipFile).get()) {
            final Enumeration<ZipArchiveEntry> zipEntries = zip.getEntriesInPhysicalOrder();
            
            while (zipEntries.hasMoreElements()) {
                final ZipArchiveEntry zipEntry = zipEntries.nextElement();
                
                try (InputStream entryInput = zip.getInputStream(zipEntry)) {
                    final byte[] actualContent = IOUtils.toByteArray(entryInput);
                    final byte[] expectedContent = expectedEntries.remove(zipEntry.getName());
                    assertArrayEquals(expectedContent, actualContent, "Content mismatch for " + zipEntry.getName());
                }
            }
        }
    }
}