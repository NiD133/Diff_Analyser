package org.apache.commons.io.file;

import static org.apache.commons.io.file.CounterAssertions.assertCounts;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.commons.io.ThreadUtils;
import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.EmptyFileFilter;
import org.apache.commons.io.filefilter.PathVisitorFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for {@link AccumulatorPathVisitor} and {@link PathVisitorFileFilter}.
 */
class AccumulatorPathVisitorTest {

    @TempDir
    Path tempDirPath;

    /**
     * Provides test parameters for various configurations of AccumulatorPathVisitor.
     */
    static Stream<Arguments> provideTestParameters() {
        return Stream.of(
            Arguments.of((Supplier<AccumulatorPathVisitor>) AccumulatorPathVisitor::withLongCounters),
            Arguments.of((Supplier<AccumulatorPathVisitor>) AccumulatorPathVisitor::withBigIntegerCounters),
            Arguments.of((Supplier<AccumulatorPathVisitor>) () ->
                AccumulatorPathVisitor.withBigIntegerCounters(TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE))
        );
    }

    /**
     * Provides test parameters for configurations that ignore failures.
     */
    static Stream<Arguments> provideTestParametersIgnoreFailures() {
        return Stream.of(
            Arguments.of((Supplier<AccumulatorPathVisitor>) () -> new AccumulatorPathVisitor(
                Counters.bigIntegerPathCounters(),
                CountingPathVisitor.defaultDirectoryFilter(),
                CountingPathVisitor.defaultFileFilter()))
        );
    }

    /**
     * Tests the default constructor of AccumulatorPathVisitor.
     */
    @Test
    void testDefaultConstructor() throws IOException {
        AccumulatorPathVisitor visitor = new AccumulatorPathVisitor();
        PathVisitorFileFilter fileFilter = new PathVisitorFileFilter(visitor);
        Files.walkFileTree(tempDirPath, new AndFileFilter(fileFilter, DirectoryFileFilter.INSTANCE, EmptyFileFilter.EMPTY));

        assertCounts(0, 0, 0, visitor.getPathCounters());
        assertEquals(1, visitor.getDirList().size());
        assertTrue(visitor.getFileList().isEmpty());
        assertEquals(visitor, visitor);
        assertEquals(visitor.hashCode(), visitor.hashCode());
    }

    /**
     * Tests an empty folder with various visitor configurations.
     */
    @ParameterizedTest
    @MethodSource("provideTestParameters")
    void testEmptyFolder(Supplier<AccumulatorPathVisitor> visitorSupplier) throws IOException {
        AccumulatorPathVisitor visitor = visitorSupplier.get();
        PathVisitorFileFilter fileFilter = new PathVisitorFileFilter(visitor);
        Files.walkFileTree(tempDirPath, new AndFileFilter(fileFilter, DirectoryFileFilter.INSTANCE, EmptyFileFilter.EMPTY));

        assertCounts(1, 0, 0, visitor.getPathCounters());
        assertEquals(1, visitor.getDirList().size());
        assertTrue(visitor.getFileList().isEmpty());
        assertEquals(visitor, visitor);
        assertEquals(visitor.hashCode(), visitor.hashCode());
    }

    /**
     * Tests the equals and hashCode methods of AccumulatorPathVisitor.
     */
    @Test
    void testEqualsAndHashCode() {
        AccumulatorPathVisitor visitor1 = AccumulatorPathVisitor.withLongCounters();
        AccumulatorPathVisitor visitor2 = AccumulatorPathVisitor.withLongCounters();

        assertEquals(visitor1, visitor1);
        assertEquals(visitor1, visitor2);
        assertEquals(visitor1.hashCode(), visitor2.hashCode());

        visitor1.getPathCounters().getByteCounter().increment();
        assertNotEquals(visitor1, visitor2);
        assertNotEquals(visitor1.hashCode(), visitor2.hashCode());
    }

    /**
     * Tests a directory with one file of size 0.
     */
    @ParameterizedTest
    @MethodSource("provideTestParameters")
    void testSingleFileSize0(Supplier<AccumulatorPathVisitor> visitorSupplier) throws IOException {
        AccumulatorPathVisitor visitor = visitorSupplier.get();
        PathVisitorFileFilter fileFilter = new PathVisitorFileFilter(visitor);
        Files.walkFileTree(Paths.get("src/test/resources/org/apache/commons/io/dirs-1-file-size-0"), fileFilter);

        assertCounts(1, 1, 0, visitor.getPathCounters());
        assertEquals(1, visitor.getDirList().size());
        assertEquals(1, visitor.getFileList().size());
        assertEquals(visitor, visitor);
        assertEquals(visitor.hashCode(), visitor.hashCode());
    }

    /**
     * Tests a directory with one file of size 1.
     */
    @ParameterizedTest
    @MethodSource("provideTestParameters")
    void testSingleFileSize1(Supplier<AccumulatorPathVisitor> visitorSupplier) throws IOException {
        AccumulatorPathVisitor visitor = visitorSupplier.get();
        PathVisitorFileFilter fileFilter = new PathVisitorFileFilter(visitor);
        Files.walkFileTree(Paths.get("src/test/resources/org/apache/commons/io/dirs-1-file-size-1"), fileFilter);

        assertCounts(1, 1, 1, visitor.getPathCounters());
        assertEquals(1, visitor.getDirList().size());
        assertEquals(1, visitor.getFileList().size());
        assertEquals(visitor, visitor);
        assertEquals(visitor.hashCode(), visitor.hashCode());
    }

    /**
     * Tests a directory with two subdirectories, each containing one file of size 1.
     */
    @ParameterizedTest
    @MethodSource("provideTestParameters")
    void testTwoSubdirectoriesWithFiles(Supplier<AccumulatorPathVisitor> visitorSupplier) throws IOException {
        AccumulatorPathVisitor visitor = visitorSupplier.get();
        PathVisitorFileFilter fileFilter = new PathVisitorFileFilter(visitor);
        Files.walkFileTree(Paths.get("src/test/resources/org/apache/commons/io/dirs-2-file-size-2"), fileFilter);

        assertCounts(3, 2, 2, visitor.getPathCounters());
        assertEquals(3, visitor.getDirList().size());
        assertEquals(2, visitor.getFileList().size());
        assertEquals(visitor, visitor);
        assertEquals(visitor.hashCode(), visitor.hashCode());
    }

    /**
     * Tests a directory with 100 files, deleting them midway through the visit asynchronously.
     */
    @ParameterizedTest
    @MethodSource("provideTestParametersIgnoreFailures")
    void testAsyncFileDeletion(Supplier<AccumulatorPathVisitor> visitorSupplier) throws IOException, InterruptedException {
        final int fileCount = 10_000;
        List<Path> files = createTempFiles(fileCount);

        AccumulatorPathVisitor visitor = visitorSupplier.get();
        PathVisitorFileFilter fileFilter = createSlowFileFilter(visitor);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        AtomicBoolean deletionCompleted = new AtomicBoolean();

        try {
            startFileDeletionTask(executor, files, deletionCompleted);
            Files.walkFileTree(tempDirPath, fileFilter);
        } finally {
            waitForDeletionCompletion(executor, deletionCompleted);
        }

        assertEquals(visitor, visitor);
        assertEquals(visitor.hashCode(), visitor.hashCode());
    }

    /**
     * Tests a directory with 100 files, deleting them midway through the visit synchronously.
     */
    @ParameterizedTest
    @MethodSource("provideTestParametersIgnoreFailures")
    void testSyncFileDeletion(Supplier<AccumulatorPathVisitor> visitorSupplier) throws IOException {
        final int fileCount = 100;
        final int halfwayPoint = fileCount / 2;
        Set<Path> files = createTempFiles(fileCount);

        AccumulatorPathVisitor visitor = visitorSupplier.get();
        AtomicInteger visitCount = new AtomicInteger();

        PathVisitorFileFilter fileFilter = new PathVisitorFileFilter(visitor) {
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attributes) throws IOException {
                if (visitCount.incrementAndGet() == halfwayPoint) {
                    deleteFiles(files);
                }
                return super.visitFile(path, attributes);
            }
        };

        Files.walkFileTree(tempDirPath, fileFilter);

        assertCounts(1, halfwayPoint - 1, 0, visitor.getPathCounters());
        assertEquals(1, visitor.getDirList().size());
        assertEquals(halfwayPoint - 1, visitor.getFileList().size());
        assertEquals(visitor, visitor);
        assertEquals(visitor.hashCode(), visitor.hashCode());
    }

    // Helper methods

    private List<Path> createTempFiles(int count) throws IOException {
        List<Path> files = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            Path tempFile = Files.createTempFile(tempDirPath, "test", ".txt");
            assertTrue(Files.exists(tempFile));
            files.add(tempFile);
        }
        return files;
    }

    private PathVisitorFileFilter createSlowFileFilter(AccumulatorPathVisitor visitor) {
        return new PathVisitorFileFilter(visitor) {
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attributes) throws IOException {
                try {
                    ThreadUtils.sleep(Duration.ofMillis(10));
                } catch (InterruptedException ignore) {
                    // Ignored
                }
                return super.visitFile(path, attributes);
            }
        };
    }

    private void startFileDeletionTask(ExecutorService executor, List<Path> files, AtomicBoolean deletionCompleted) {
        executor.execute(() -> {
            for (Path file : files) {
                try {
                    Files.delete(file);
                } catch (IOException ignored) {
                    // Ignored
                }
            }
            deletionCompleted.set(true);
        });
    }

    private void waitForDeletionCompletion(ExecutorService executor, AtomicBoolean deletionCompleted) throws InterruptedException {
        if (!deletionCompleted.get()) {
            ThreadUtils.sleep(Duration.ofMillis(1000));
        }
        if (!deletionCompleted.get()) {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }
        executor.shutdownNow();
    }

    private void deleteFiles(Set<Path> files) throws IOException {
        for (Path file : files) {
            Files.delete(file);
        }
    }
}