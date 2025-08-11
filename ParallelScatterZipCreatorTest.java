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

class ParallelScatterZipCreatorTest extends AbstractTempDirTest {

    private interface CallableConsumer extends Consumer<Callable<? extends ScatterZipOutputStream>> {
        // empty
    }

    private interface CallableConsumerSupplier extends Function<ParallelScatterZipCreator, CallableConsumer> {
        // empty
    }

    private static final long MAX_FILE_SIZE_TO_COMPRESS = 1024 * 1024; // 1MB
    private static final int MAX_FILES_TO_COMPRESS = 50;
    private static final int NUMBER_OF_ENTRIES = 5000;
    private static final int DEFAULT_DIRECTORY_PERMISSIONS = 0664;

    // Disabled test for known issue
    @Test
    @Disabled("[COMPRESS-639]")
    public void sameZipArchiveEntryNullPointerException() throws IOException {
        final String fileContent = "A";
        final int fileCount = 100;
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final LinkedList<InputStream> inputStreams = createTestInputStreams(fileContent, fileCount);

        try (ZipArchiveOutputStream zipOutput = new ZipArchiveOutputStream(outputStream)) {
            zipOutput.setUseZip64(Zip64Mode.Always);
            ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator();

            addMultipleEntries(zipCreator, inputStreams);
            zipCreator.writeTo(zipOutput);
        } // Throws NullPointerException on close()
    }

    @Test
    void testCallableApiUsingSubmit() throws Exception {
        testCallableApi(Deflater.DEFAULT_COMPRESSION, this::writeInMemoryEntries, zipCreator -> zipCreator::submit);
    }

    @Test
    void testCallableApiUsingSubmitStreamAwareCallable() throws Exception {
        testCallableApi(Deflater.DEFAULT_COMPRESSION, this::writeInMemoryEntries, zipCreator -> zipCreator::submitStreamAwareCallable);
    }

    @Test
    void testCallableApiWithHighestLevelUsingSubmitStreamAwareCallable() throws Exception {
        testCallableApi(Deflater.BEST_COMPRESSION, this::writeTestFileEntries, zipCreator -> zipCreator::submitStreamAwareCallable);
    }

    @Test
    void testCallableWithLowestLevelApiUsingSubmit() throws Exception {
        testCallableApi(Deflater.NO_COMPRESSION, this::writeTestFileEntries, zipCreator -> zipCreator::submit);
    }

    @Test
    void testConcurrentCustomTempFolder() throws Exception {
        File zipFile = createTempFile("parallelScatterGather1", "");
        Map<String, byte[]> entries;
        ParallelScatterZipCreator zipCreator;

        // Create custom temp directory
        Path customTempDir = Paths.get("target/custom-temp-dir");
        Files.createDirectories(customTempDir);
        
        try (ZipArchiveOutputStream zipOutput = createZipOutputStream(zipFile)) {
            zipCreator = new ParallelScatterZipCreator(
                Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()),
                new DefaultBackingStoreSupplier(customTempDir)
            );
            entries = writeEntries(zipCreator);
            zipCreator.writeTo(zipOutput);
        }

        verifyZipContents(zipFile, entries);
    }

    @Test
    void testConcurrentDefaultTempFolder() throws Exception {
        File zipFile = createTempFile("parallelScatterGather1", "");
        Map<String, byte[]> entries;
        ParallelScatterZipCreator zipCreator;

        try (ZipArchiveOutputStream zipOutput = createZipOutputStream(zipFile)) {
            zipCreator = new ParallelScatterZipCreator();
            entries = writeEntries(zipCreator);
            zipCreator.writeTo(zipOutput);
        }

        verifyZipContents(zipFile, entries);
    }

    @Test
    void testThrowsExceptionWithCompressionLevelTooBig() {
        int invalidCompressionLevel = Deflater.BEST_COMPRESSION + 1;
        ExecutorService threadPool = Executors.newFixedThreadPool(1);
        
        assertThrows(IllegalArgumentException.class, () -> 
            new ParallelScatterZipCreator(
                threadPool,
                () -> new FileBasedScatterGatherBackingStore(createTempFile("parallelscatter", "n1")),
                invalidCompressionLevel
            )
        );
        threadPool.shutdownNow();
    }

    @Test
    void testThrowsExceptionWithCompressionLevelTooSmall() {
        int invalidCompressionLevel = Deflater.DEFAULT_COMPRESSION - 1;
        ExecutorService threadPool = Executors.newFixedThreadPool(1);
        
        assertThrows(IllegalArgumentException.class, () -> 
            new ParallelScatterZipCreator(
                threadPool,
                () -> new FileBasedScatterGatherBackingStore(createTempFile("parallelscatter", "n1")),
                invalidCompressionLevel
            )
        );
        threadPool.shutdownNow();
    }

    // ==================== HELPER METHODS ====================

    private void addMultipleEntries(ParallelScatterZipCreator creator, LinkedList<InputStream> streams) {
        for (InputStream inputStream : streams) {
            ZipArchiveEntry entry = new ZipArchiveEntry("./dir/myfile.txt");
            entry.setMethod(ZipEntry.DEFLATED);
            creator.addArchiveEntry(entry, () -> inputStream);
        }
    }

    private LinkedList<InputStream> createTestInputStreams(String content, int count) {
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
        LinkedList<InputStream> streams = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            streams.add(new ByteArrayInputStream(contentBytes));
        }
        return streams;
    }

    private ZipArchiveOutputStream createZipOutputStream(File file) throws IOException {
        ZipArchiveOutputStream zos = new ZipArchiveOutputStream(file);
        zos.setEncoding(StandardCharsets.UTF_8.name());
        return zos;
    }

    private ZipArchiveEntry createZipArchiveEntry(Map<String, byte[]> entries, int index, byte[] payload) {
        String entryName = "file" + index;
        ZipArchiveEntry entry = new ZipArchiveEntry(entryName);
        entries.put(entryName, payload);
        entry.setMethod(ZipEntry.DEFLATED);
        entry.setSize(payload.length);
        entry.setUnixMode(UnixStat.FILE_FLAG | DEFAULT_DIRECTORY_PERMISSIONS);
        return entry;
    }

    private void verifyZipContents(File zipFile, Map<String, byte[]> expectedEntries) throws IOException {
        try (ZipFile zip = ZipFile.builder().setFile(zipFile).get()) {
            Enumeration<ZipArchiveEntry> physicalEntries = zip.getEntriesInPhysicalOrder();
            int index = 0;
            
            while (physicalEntries.hasMoreElements()) {
                ZipArchiveEntry entry = physicalEntries.nextElement();
                String expectedName = "file" + index++;
                assertEquals(expectedName, entry.getName(), "Entry name mismatch");
                
                try (InputStream entryStream = zip.getInputStream(entry)) {
                    byte[] actualContent = IOUtils.toByteArray(entryStream);
                    byte[] expectedContent = expectedEntries.remove(entry.getName());
                    assertArrayEquals(expectedContent, actualContent, "Content mismatch for " + entry.getName());
                }
            }
        }
        assertTrue(expectedEntries.isEmpty(), "All entries should be processed");
    }

    private void testCallableApi(int compressionLevel, EntryWriter entryWriter, 
                                 CallableConsumerSupplier consumerSupplier) throws Exception {
        File zipFile = createTempFile("parallelScatterGather", "");
        Map<String, byte[]> entries;
        ParallelScatterZipCreator zipCreator;
        
        try (ZipArchiveOutputStream zipOutput = createZipOutputStream(zipFile)) {
            ExecutorService threadPool = Executors.newFixedThreadPool(1);
            ScatterGatherBackingStoreSupplier storeSupplier = () -> 
                new FileBasedScatterGatherBackingStore(createTempFile("parallelscatter", "n1"));
                
            zipCreator = new ParallelScatterZipCreator(threadPool, storeSupplier, compressionLevel);
            entries = entryWriter.writeEntries(zipCreator, consumerSupplier.apply(zipCreator));
            zipCreator.writeTo(zipOutput);
        }

        verifyZipContents(zipFile, entries);
        assertNotNull(zipCreator.getStatisticsMessage());
    }

    private Map<String, byte[]> writeInMemoryEntries(ParallelScatterZipCreator creator, 
                                                     CallableConsumer consumer) {
        Map<String, byte[]> entries = new HashMap<>();
        for (int i = 0; i < NUMBER_OF_ENTRIES; i++) {
            byte[] content = ("content" + i).getBytes();
            ZipArchiveEntry entry = createZipArchiveEntry(entries, i, content);
            InputStreamSupplier streamSupplier = () -> new ByteArrayInputStream(content);
            
            Callable<ScatterZipOutputStream> task = (i % 2 == 0) ?
                creator.createCallable(entry, streamSupplier) :
                creator.createCallable(() -> ZipArchiveEntryRequest.createZipArchiveEntryRequest(entry, streamSupplier));
                
            consumer.accept(task);
        }
        return entries;
    }

    private Map<String, byte[]> writeTestFileEntries(ParallelScatterZipCreator creator, 
                                                    CallableConsumer consumer) throws IOException {
        Map<String, byte[]> entries = new HashMap<>();
        File baseDir = AbstractTest.getFile("");
        int filesAdded = 0;
        
        for (File file : baseDir.listFiles()) {
            if (filesAdded >= MAX_FILES_TO_COMPRESS) break;
            if (file.isDirectory() || file.length() > MAX_FILE_SIZE_TO_COMPRESS) continue;
            
            byte[] fileContent = Files.readAllBytes(file.toPath());
            entries.put(file.getName(), fileContent);
            
            ZipArchiveEntry entry = new ZipArchiveEntry(file.getName());
            entry.setMethod(ZipEntry.DEFLATED);
            entry.setSize(file.length());
            entry.setUnixMode(UnixStat.FILE_FLAG | DEFAULT_DIRECTORY_PERMISSIONS);
            
            InputStreamSupplier streamSupplier = () -> {
                try {
                    return Files.newInputStream(file.toPath());
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            };
            
            Callable<ScatterZipOutputStream> task = (filesAdded % 2 == 0) ?
                creator.createCallable(entry, streamSupplier) :
                creator.createCallable(() -> ZipArchiveEntryRequest.createZipArchiveEntryRequest(entry, streamSupplier));
                
            consumer.accept(task);
            filesAdded++;
        }
        return entries;
    }

    private Map<String, byte[]> writeEntries(ParallelScatterZipCreator creator) {
        Map<String, byte[]> entries = new HashMap<>();
        for (int i = 0; i < NUMBER_OF_ENTRIES; i++) {
            byte[] content = ("content" + i).getBytes();
            ZipArchiveEntry entry = createZipArchiveEntry(entries, i, content);
            InputStreamSupplier streamSupplier = () -> new ByteArrayInputStream(content);
            
            if (i % 2 == 0) {
                creator.addArchiveEntry(entry, streamSupplier);
            } else {
                creator.addArchiveEntry(() -> ZipArchiveEntryRequest.createZipArchiveEntryRequest(entry, streamSupplier));
            }
        }
        return entries;
    }

    @FunctionalInterface
    private interface EntryWriter {
        Map<String, byte[]> writeEntries(ParallelScatterZipCreator creator, CallableConsumer consumer) throws IOException;
    }
}