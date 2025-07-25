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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Stream;

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
 * Tests both {@link AccumulatorPathVisitor} and {@link PathVisitorFileFilter}.
 */
class AccumulatorPathVisitorTest {

    /**
     * Test parameters for the parameterized tests.
     * @return stream of test arguments
     */
    static Stream<Arguments> testParameters() {
        return Stream.of(
            Arguments.of((Supplier<AccumulatorPathVisitor>) AccumulatorPathVisitor::withLongCounters),
            Arguments.of((Supplier<AccumulatorPathVisitor>) AccumulatorPathVisitor::withBigIntegerCounters),
            Arguments.of((Supplier<AccumulatorPathVisitor>) () ->
                AccumulatorPathVisitor.withBigIntegerCounters(TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE))
        );
    }

    /**
     * Test parameters for the parameterized tests that ignore failures.
     * @return stream of test arguments
     */
    static Stream<Arguments> testParametersIgnoreFailures() {
        return Stream.of(
            Arguments.of((Supplier<AccumulatorPathVisitor>) () -> new AccumulatorPathVisitor(
                Counters.bigIntegerPathCounters(),
                CountingPathVisitor.defaultDirectoryFilter(),
                CountingPathVisitor.defaultFileFilter()))
        );
    }

    @TempDir
    Path tempDirPath;

    /**
     * Tests the 0-argument constructor.
     * @throws IOException if an I/O error occurs
     */
    @Test
    void testZeroArgConstructor() throws IOException {
        // Arrange
        AccumulatorPathVisitor accumulatorPathVisitor = new AccumulatorPathVisitor();
        PathVisitorFileFilter countingFileFilter = new PathVisitorFileFilter(accumulatorPathVisitor);

        // Act
        Files.walkFileTree(tempDirPath, new AndFileFilter(countingFileFilter, DirectoryFileFilter.INSTANCE, EmptyFileFilter.EMPTY));

        // Assert
        assertCounts(0, 0, 0, accumulatorPathVisitor.getPathCounters());
        assertEquals(1, accumulatorPathVisitor.getDirList().size());
        assertTrue(accumulatorPathVisitor.getFileList().isEmpty());
        assertEquals(accumulatorPathVisitor, accumulatorPathVisitor);
        assertEquals(accumulatorPathVisitor.hashCode(), accumulatorPathVisitor.hashCode());
    }

    /**
     * Tests an empty folder.
     * @param supplier supplier of AccumulatorPathVisitor
     * @throws IOException if an I/O error occurs
     */
    @ParameterizedTest
    @MethodSource("testParameters")
    void testEmptyFolder(Supplier<AccumulatorPathVisitor> supplier) throws IOException {
        // Arrange
        AccumulatorPathVisitor accumulatorPathVisitor = supplier.get();
        PathVisitorFileFilter countingFileFilter = new PathVisitorFileFilter(accumulatorPathVisitor);

        // Act
        Files.walkFileTree(tempDirPath, new AndFileFilter(countingFileFilter, DirectoryFileFilter.INSTANCE, EmptyFileFilter.EMPTY));

        // Assert
        assertCounts(1, 0, 0, accumulatorPathVisitor.getPathCounters());
        assertEquals(1, accumulatorPathVisitor.getDirList().size());
        assertTrue(accumulatorPathVisitor.getFileList().isEmpty());
        assertEquals(accumulatorPathVisitor, accumulatorPathVisitor);
        assertEquals(accumulatorPathVisitor.hashCode(), accumulatorPathVisitor.hashCode());
    }

    /**
     * Tests the equals and hashCode methods.
     */
    @Test
    void testEqualsAndHashcode() {
        // Arrange
        AccumulatorPathVisitor visitor0 = AccumulatorPathVisitor.withLongCounters();
        AccumulatorPathVisitor visitor1 = AccumulatorPathVisitor.withLongCounters();

        // Assert
        assertEquals(visitor0, visitor0);
        assertEquals(visitor0, visitor1);
        assertEquals(visitor1, visitor0);
        assertEquals(visitor0.hashCode(), visitor0.hashCode());
        assertEquals(visitor0.hashCode(), visitor1.hashCode());
        assertEquals(visitor1.hashCode(), visitor0.hashCode());

        // Arrange
        visitor0.getPathCounters().getByteCounter().increment();

        // Assert
        assertEquals(visitor0, visitor0);
        assertNotEquals(visitor0, visitor1);
        assertNotEquals(visitor1, visitor0);
        assertEquals(visitor0.hashCode(), visitor0.hashCode());
        assertNotEquals(visitor0.hashCode(), visitor1.hashCode());
        assertNotEquals(visitor1.hashCode(), visitor0.hashCode());
    }

    /**
     * Tests a directory with one file of size 0.
     * @param supplier supplier of AccumulatorPathVisitor
     * @throws IOException if an I/O error occurs
     */
    @ParameterizedTest
    @MethodSource("testParameters")
    void testDirectoryWithOneFileSize0(Supplier<AccumulatorPathVisitor> supplier) throws IOException {
        // Arrange
        AccumulatorPathVisitor accumulatorPathVisitor = supplier.get();
        PathVisitorFileFilter countingFileFilter = new PathVisitorFileFilter(accumulatorPathVisitor);

        // Act
        Files.walkFileTree(Paths.get("src/test/resources/org/apache/commons/io/dirs-1-file-size-0"), countingFileFilter);

        // Assert
        assertCounts(1, 1, 0, accumulatorPathVisitor.getPathCounters());
        assertEquals(1, accumulatorPathVisitor.getDirList().size());
        assertEquals(1, accumulatorPathVisitor.getFileList().size());
        assertEquals(accumulatorPathVisitor, accumulatorPathVisitor);
        assertEquals(accumulatorPathVisitor.hashCode(), accumulatorPathVisitor.hashCode());
    }

    /**
     * Tests a directory with one file of size 1.
     * @param supplier supplier of AccumulatorPathVisitor
     * @throws IOException if an I/O error occurs
     */
    @ParameterizedTest
    @MethodSource("testParameters")
    void testDirectoryWithOneFileSize1(Supplier<AccumulatorPathVisitor> supplier) throws IOException {
        // Arrange
        AccumulatorPathVisitor accumulatorPathVisitor = supplier.get();
        PathVisitorFileFilter countingFileFilter = new PathVisitorFileFilter(accumulatorPathVisitor);

        // Act
        Files.walkFileTree(Paths.get("src/test/resources/org/apache/commons/io/dirs-1-file-size-1"), countingFileFilter);

        // Assert
        assertCounts(1, 1, 1, accumulatorPathVisitor.getPathCounters());
        assertEquals(1, accumulatorPathVisitor.getDirList().size());
        assertEquals(1, accumulatorPathVisitor.getFileList().size());
        assertEquals(accumulatorPathVisitor, accumulatorPathVisitor);
        assertEquals(accumulatorPathVisitor.hashCode(), accumulatorPathVisitor.hashCode());
    }

    /**
     * Tests a directory with two subdirectories, each containing one file of size 1.
     * @param supplier supplier of AccumulatorPathVisitor
     * @throws IOException if an I/O error occurs
     */
    @ParameterizedTest
    @MethodSource("testParameters")
    void testDirectoryWithTwoSubdirectoriesEachContainingOneFileSize1(Supplier<AccumulatorPathVisitor> supplier) throws IOException {
        // Arrange
        AccumulatorPathVisitor accumulatorPathVisitor = supplier.get();
        PathVisitorFileFilter countingFileFilter = new PathVisitorFileFilter(accumulatorPathVisitor);

        // Act
        Files.walkFileTree(Paths.get("src/test/resources/org/apache/commons/io/dirs-2-file-size-2"), countingFileFilter);

        // Assert
        assertCounts(3, 2, 2, accumulatorPathVisitor.getPathCounters());
        assertEquals(3, accumulatorPathVisitor.getDirList().size());
        assertEquals(2, accumulatorPathVisitor.getFileList().size());
        assertEquals(accumulatorPathVisitor, accumulatorPathVisitor);
        assertEquals(accumulatorPathVisitor.hashCode(), accumulatorPathVisitor.hashCode());
    }

    /**
     * Tests IO-755 with a directory with 100 files, and delete all of them midway through the visit.
     * @param supplier supplier of AccumulatorPathVisitor
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted
     */
    @ParameterizedTest
    @MethodSource("testParametersIgnoreFailures")
    void testIO755DeleteFilesMidVisitAsync(Supplier<AccumulatorPathVisitor> supplier) throws IOException, InterruptedException {
        // Arrange
        int count = 10_000;
        List<Path> files = new ArrayList<>(count);

        // Create files
        for (int i = 1; i <= count; i++) {
            Path tempFile = Files.createTempFile(tempDirPath, "test", ".txt");
            assertTrue(Files.exists(tempFile));
            files.add(tempFile);
        }

        AccumulatorPathVisitor accumulatorPathVisitor = supplier.get();
        PathVisitorFileFilter countingFileFilter = new PathVisitorFileFilter(accumulatorPathVisitor) {
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attributes) throws IOException {
                try {
                    Thread.sleep(Duration.ofMillis(10).toMillis());
                } catch (InterruptedException ignore) {
                    // Ignore
                }
                return super.visitFile(path, attributes);
            }
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        AtomicBoolean deleted = new AtomicBoolean();

        try {
            executor.execute(() -> {
                for (Path file : files) {
                    try {
                        Files.delete(file);
                    } catch (IOException ignored) {
                        // Ignore
                    }
                }
                deleted.set(true);
            });

            Files.walkFileTree(tempDirPath, countingFileFilter);
        } finally {
            if (!deleted.get()) {
                Thread.sleep(Duration.ofMillis(1000).toMillis());
            }
            if (!deleted.get()) {
                executor.awaitTermination(5, TimeUnit.SECONDS);
            }
            executor.shutdownNow();
        }

        // Assert
        assertEquals(accumulatorPathVisitor, accumulatorPathVisitor);
        assertEquals(accumulatorPathVisitor.hashCode(), accumulatorPathVisitor.hashCode());
    }

    /**
     * Tests IO-755 with a directory with 100 files, and delete all of them midway through the visit.
     * @param supplier supplier of AccumulatorPathVisitor
     * @throws IOException if an I/O error occurs
     */
    @ParameterizedTest
    @MethodSource("testParametersIgnoreFailures")
    void testIO755DeleteFilesMidVisitSync(Supplier<AccumulatorPathVisitor> supplier) throws IOException {
        // Arrange
        int count = 100;
        int marker = count / 2;
        List<Path> files = new ArrayList<>(count);

        for (int i = 1; i <= count; i++) {
            Path tempFile = Files.createTempFile(tempDirPath, "test", ".txt");
            assertTrue(Files.exists(tempFile));
            files.add(tempFile);
        }

        AccumulatorPathVisitor accumulatorPathVisitor = supplier.get();
        AtomicInteger visitCount = new AtomicInteger();

        PathVisitorFileFilter countingFileFilter = new PathVisitorFileFilter(accumulatorPathVisitor) {
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attributes) throws IOException {
                if (visitCount.incrementAndGet() == marker) {
                    for (Path file : files) {
                        Files.delete(file);
                    }
                }
                return super.visitFile(path, attributes);
            }
        };

        Files.walkFileTree(tempDirPath, countingFileFilter);

        // Assert
        assertCounts(1, marker - 1, 0, accumulatorPathVisitor.getPathCounters());
        assertEquals(1, accumulatorPathVisitor.getDirList().size());
        assertEquals(marker - 1, accumulatorPathVisitor.getFileList().size());
        assertEquals(accumulatorPathVisitor, accumulatorPathVisitor);
        assertEquals(accumulatorPathVisitor.hashCode(), accumulatorPathVisitor.hashCode());
    }
}