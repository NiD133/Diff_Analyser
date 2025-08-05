package org.apache.commons.compress.archivers.zip;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
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

    // Constants
    private static final long MAX_FILE_SIZE = 1024 * 1024; // 1MB
    private static final int MAX_FILES = 50;
    private static final int NUM_ITEMS = 5000;

    // Interfaces for functional programming
    private interface CallableConsumer extends Consumer<Callable<? extends ScatterZipOutputStream>> {}
    private interface CallableConsumerSupplier extends Function<ParallelScatterZipCreator, CallableConsumer> {}

    // Helper methods
    private void executeCallableApiTest(CallableConsumerSupplier consumerSupplier, File result) throws Exception {
        executeCallableApiTest(consumerSupplier, Deflater.DEFAULT_COMPRESSION, result);
    }

    private void executeCallableApiTest(CallableConsumerSupplier consumerSupplier, int compressionLevel, File result) throws Exception {
        Map<String, byte[]> entries;
        ParallelScatterZipCreator zipCreator;

        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(result)) {
            zos.setEncoding(StandardCharsets.UTF_8.name());
            ExecutorService executor = Executors.newFixedThreadPool(1);

            ScatterGatherBackingStoreSupplier storeSupplier = () -> new FileBasedScatterGatherBackingStore(createTempFile("parallelscatter", "n1"));
            zipCreator = new ParallelScatterZipCreator(executor, storeSupplier, compressionLevel);

            entries = createEntriesAsCallable(zipCreator, consumerSupplier.apply(zipCreator));
            zipCreator.writeTo(zos);
        }

        validateZipFileContents(result, entries);
        assertTrue(entries.isEmpty());
        assertNotNull(zipCreator.getStatisticsMessage());
    }

    private void executeCallableApiWithTestFiles(CallableConsumerSupplier consumerSupplier, int compressionLevel, File result) throws Exception {
        ParallelScatterZipCreator zipCreator;
        Map<String, byte[]> entries;

        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(result)) {
            zos.setEncoding(StandardCharsets.UTF_8.name());
            ExecutorService executor = Executors.newFixedThreadPool(1);

            ScatterGatherBackingStoreSupplier storeSupplier = () -> new FileBasedScatterGatherBackingStore(createTempFile("parallelscatter", "n1"));
            zipCreator = new ParallelScatterZipCreator(executor, storeSupplier, compressionLevel);

            entries = createTestFilesAsCallable(zipCreator, consumerSupplier.apply(zipCreator));
            zipCreator.writeTo(zos);
        }

        validateZipFileContents(result, entries);
        assertNotNull(zipCreator.getStatisticsMessage());
    }

    private ZipArchiveEntry createZipEntry(Map<String, byte[]> entries, int index, byte[] payload) {
        ZipArchiveEntry entry = new ZipArchiveEntry("file" + index);
        entries.put(entry.getName(), payload);
        entry.setMethod(ZipEntry.DEFLATED);
        entry.setSize(payload.length);
        entry.setUnixMode(UnixStat.FILE_FLAG | 0664);
        return entry;
    }

    private void validateZipFileContents(File result, Map<String, byte[]> entries) throws IOException {
        try (ZipFile zipFile = ZipFile.builder().setFile(result).get()) {
            Enumeration<ZipArchiveEntry> zipEntries = zipFile.getEntriesInPhysicalOrder();
            int index = 0;

            while (zipEntries.hasMoreElements()) {
                ZipArchiveEntry zipEntry = zipEntries.nextElement();
                try (InputStream inputStream = zipFile.getInputStream(zipEntry)) {
                    byte[] actual = IOUtils.toByteArray(inputStream);
                    byte[] expected = entries.remove(zipEntry.getName());
                    assertArrayEquals(expected, actual, "Mismatch in entry: " + zipEntry.getName());
                }
                assertEquals("file" + index++, zipEntry.getName(), "Unexpected order for entry: " + zipEntry.getName());
            }
        }
    }

    // Test cases
    @Test
    @Disabled("[COMPRESS-639]")
    public void testNullPointerExceptionOnSameZipEntry() throws IOException, ExecutionException, InterruptedException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String content = "A";
        int numFiles = 100;
        LinkedList<InputStream> streams = new LinkedList<>();

        for (int i = 0; i < numFiles; i++) {
            streams.add(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
        }

        ParallelScatterZipCreator zipCreator = new ParallelScatterZipCreator();
        try (ZipArchiveOutputStream zipOutputStream = new ZipArchiveOutputStream(outputStream)) {
            zipOutputStream.setUseZip64(Zip64Mode.Always);

            for (InputStream stream : streams) {
                ZipArchiveEntry entry = new ZipArchiveEntry("./dir/myfile.txt");
                entry.setMethod(ZipEntry.DEFLATED);
                zipCreator.addArchiveEntry(entry, () -> stream);
            }

            zipCreator.writeTo(zipOutputStream);
        } // Throws NullPointerException on close()
    }

    @Test
    void testCallableApiUsingSubmit() throws Exception {
        File result = createTempFile("parallelScatterGather2", "");
        executeCallableApiTest(zipCreator -> zipCreator::submit, result);
    }

    @Test
    void testCallableApiUsingSubmitStreamAwareCallable() throws Exception {
        File result = createTempFile("parallelScatterGather3", "");
        executeCallableApiTest(zipCreator -> zipCreator::submitStreamAwareCallable, result);
    }

    @Test
    void testCallableApiWithHighestCompressionLevel() throws Exception {
        File result = createTempFile("parallelScatterGather5", "");
        executeCallableApiWithTestFiles(zipCreator -> zipCreator::submitStreamAwareCallable, Deflater.BEST_COMPRESSION, result);
    }

    @Test
    void testCallableApiWithNoCompression() throws Exception {
        File result = createTempFile("parallelScatterGather4", "");
        executeCallableApiWithTestFiles(zipCreator -> zipCreator::submit, Deflater.NO_COMPRESSION, result);
    }

    @Test
    void testConcurrentWithCustomTempFolder() throws Exception {
        File result = createTempFile("parallelScatterGather1", "");
        ParallelScatterZipCreator zipCreator;
        Map<String, byte[]> entries;

        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(result)) {
            zos.setEncoding(StandardCharsets.UTF_8.name());

            Path customDir = Paths.get("target/custom-temp-dir");
            Files.createDirectories(customDir);
            zipCreator = new ParallelScatterZipCreator(
                    Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()),
                    new DefaultBackingStoreSupplier(customDir)
            );

            entries = createEntries(zipCreator);
            zipCreator.writeTo(zos);
        }

        validateZipFileContents(result, entries);
        assertTrue(entries.isEmpty());
        assertNotNull(zipCreator.getStatisticsMessage());
    }

    @Test
    void testConcurrentWithDefaultTempFolder() throws Exception {
        File result = createTempFile("parallelScatterGather1", "");
        ParallelScatterZipCreator zipCreator;
        Map<String, byte[]> entries;

        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(result)) {
            zos.setEncoding(StandardCharsets.UTF_8.name());
            zipCreator = new ParallelScatterZipCreator();

            entries = createEntries(zipCreator);
            zipCreator.writeTo(zos);
        }

        validateZipFileContents(result, entries);
        assertTrue(entries.isEmpty());
        assertNotNull(zipCreator.getStatisticsMessage());
    }

    @Test
    void testInvalidCompressionLevelTooHigh() {
        int invalidCompressionLevel = Deflater.BEST_COMPRESSION + 1;
        ExecutorService executor = Executors.newFixedThreadPool(1);

        assertThrows(IllegalArgumentException.class, () -> new ParallelScatterZipCreator(
                executor,
                () -> new FileBasedScatterGatherBackingStore(createTempFile("parallelscatter", "n1")),
                invalidCompressionLevel
        ));

        executor.shutdownNow();
    }

    @Test
    void testInvalidCompressionLevelTooLow() {
        int invalidCompressionLevel = Deflater.DEFAULT_COMPRESSION - 1;
        ExecutorService executor = Executors.newFixedThreadPool(1);

        assertThrows(IllegalArgumentException.class, () -> new ParallelScatterZipCreator(
                executor,
                () -> new FileBasedScatterGatherBackingStore(createTempFile("parallelscatter", "n1")),
                invalidCompressionLevel
        ));

        executor.shutdownNow();
    }

    // Helper methods for entry creation
    private Map<String, byte[]> createEntries(ParallelScatterZipCreator zipCreator) {
        Map<String, byte[]> entries = new HashMap<>();
        for (int i = 0; i < NUM_ITEMS; i++) {
            byte[] payload = ("content" + i).getBytes();
            ZipArchiveEntry entry = createZipEntry(entries, i, payload);
            InputStreamSupplier supplier = () -> new ByteArrayInputStream(payload);

            if (i % 2 == 0) {
                zipCreator.addArchiveEntry(entry, supplier);
            } else {
                ZipArchiveEntryRequestSupplier requestSupplier = () -> ZipArchiveEntryRequest.createZipArchiveEntryRequest(entry, supplier);
                zipCreator.addArchiveEntry(requestSupplier);
            }
        }
        return entries;
    }

    private Map<String, byte[]> createEntriesAsCallable(ParallelScatterZipCreator zipCreator, CallableConsumer consumer) {
        Map<String, byte[]> entries = new HashMap<>();
        for (int i = 0; i < NUM_ITEMS; i++) {
            byte[] payload = ("content" + i).getBytes();
            ZipArchiveEntry entry = createZipEntry(entries, i, payload);
            InputStreamSupplier supplier = () -> new ByteArrayInputStream(payload);
            Callable<ScatterZipOutputStream> callable;

            if (i % 2 == 0) {
                callable = zipCreator.createCallable(entry, supplier);
            } else {
                ZipArchiveEntryRequestSupplier requestSupplier = () -> ZipArchiveEntryRequest.createZipArchiveEntryRequest(entry, supplier);
                callable = zipCreator.createCallable(requestSupplier);
            }

            consumer.accept(callable);
        }
        return entries;
    }

    private Map<String, byte[]> createTestFilesAsCallable(ParallelScatterZipCreator zipCreator, CallableConsumer consumer) throws IOException {
        Map<String, byte[]> entries = new HashMap<>();
        File baseDir = AbstractTest.getFile("");
        int fileCount = 0;

        for (File file : baseDir.listFiles()) {
            if (fileCount >= MAX_FILES || file.isDirectory() || file.length() > MAX_FILE_SIZE) {
                continue;
            }

            entries.put(file.getName(), Files.readAllBytes(file.toPath()));

            ZipArchiveEntry entry = new ZipArchiveEntry(file.getName());
            entry.setMethod(ZipEntry.DEFLATED);
            entry.setSize(file.length());
            entry.setUnixMode(UnixStat.FILE_FLAG | 0664);

            InputStreamSupplier supplier = () -> {
                try {
                    return Files.newInputStream(file.toPath());
                } catch (IOException e) {
                    return null;
                }
            };

            Callable<ScatterZipOutputStream> callable;
            if (fileCount % 2 == 0) {
                callable = zipCreator.createCallable(entry, supplier);
            } else {
                ZipArchiveEntryRequestSupplier requestSupplier = () -> ZipArchiveEntryRequest.createZipArchiveEntryRequest(entry, supplier);
                callable = zipCreator.createCallable(requestSupplier);
            }

            consumer.accept(callable);
            fileCount++;
        }
        return entries;
    }
}