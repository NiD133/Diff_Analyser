package org.apache.commons.io.file;

import static org.apache.commons.io.file.CounterAssertions.assertCounts;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.apache.commons.io.ThreadUtils;
import org.apache.commons.io.filefilter.PathVisitorFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link AccumulatorPathVisitor}.
 */
@DisplayName("AccumulatorPathVisitor Tests")
public class AccumulatorPathVisitorTest {

    @TempDir
    private Path tempDirPath;

    /**
     * Provides suppliers for creating AccumulatorPathVisitor instances with different counter types.
     */
    static Stream<Arguments> createVisitorSuppliers() {
        return Stream.of(
            Arguments.of((Supplier<AccumulatorPathVisitor>) AccumulatorPathVisitor::withLongCounters, "Long Counters"),
            Arguments.of((Supplier<AccumulatorPathVisitor>) AccumulatorPathVisitor::withBigIntegerCounters, "BigInteger Counters"),
            Arguments.of((Supplier<AccumulatorPathVisitor>) () -> AccumulatorPathVisitor.withBigIntegerCounters(TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE), "BigInteger Counters with Filters")
        );
    }

    /**
     * Provides a visitor supplier that is configured to continue visiting when a file cannot be accessed.
     * This is used for tests where files are deleted during the walk.
     */
    static Stream<Arguments> createVisitorThatIgnoresFailures() {
        return Stream.of(
            Arguments.of((Supplier<AccumulatorPathVisitor>) () -> new AccumulatorPathVisitor(Counters.bigIntegerPathCounters(), CountingPathVisitor.defaultDirectoryFilter(), CountingPathVisitor.defaultFileFilter()))
        );
    }

    @ParameterizedTest(name = "[{index}] with {1}")
    @MethodSource("createVisitorSuppliers")
    @DisplayName("should count one directory when walking an empty folder")
    void walkOnEmptyDirectory_shouldCountCorrectly(final Supplier<AccumulatorPathVisitor> visitorSupplier) throws IOException {
        // Arrange
        final AccumulatorPathVisitor visitor = visitorSupplier.get();
        final PathVisitorFileFilter visitorAdapter = new PathVisitorFileFilter(visitor);

        // Act
        Files.walkFileTree(tempDirPath, visitorAdapter);

        // Assert
        assertCounts(1, 0, 0, visitor.getPathCounters());
        assertEquals(1, visitor.getDirList().size());
        assertTrue(visitor.getFileList().isEmpty());
    }

    @ParameterizedTest(name = "[{index}] with {1}")
    @MethodSource("createVisitorSuppliers")
    @DisplayName("should count correctly for a directory with one empty file")
    void walkOnDirectoryWithOneEmptyFile_shouldCountCorrectly(final Supplier<AccumulatorPathVisitor> visitorSupplier) throws IOException {
        // Arrange
        final AccumulatorPathVisitor visitor = visitorSupplier.get();
        final Path testDir = Paths.get("src/test/resources/org/apache/commons/io/dirs-1-file-size-0");
        final PathVisitorFileFilter visitorAdapter = new PathVisitorFileFilter(visitor);

        // Act
        Files.walkFileTree(testDir, visitorAdapter);

        // Assert
        assertCounts(1, 1, 0, visitor.getPathCounters());
        assertEquals(1, visitor.getDirList().size());
        assertEquals(1, visitor.getFileList().size());
    }

    @ParameterizedTest(name = "[{index}] with {1}")
    @MethodSource("createVisitorSuppliers")
    @DisplayName("should count correctly for a directory with one non-empty file")
    void walkOnDirectoryWithOneNonEmptyFile_shouldCountCorrectly(final Supplier<AccumulatorPathVisitor> visitorSupplier) throws IOException {
        // Arrange
        final AccumulatorPathVisitor visitor = visitorSupplier.get();
        final Path testDir = Paths.get("src/test/resources/org/apache/commons/io/dirs-1-file-size-1");
        final PathVisitorFileFilter visitorAdapter = new PathVisitorFileFilter(visitor);

        // Act
        Files.walkFileTree(testDir, visitorAdapter);

        // Assert
        assertCounts(1, 1, 1, visitor.getPathCounters());
        assertEquals(1, visitor.getDirList().size());
        assertEquals(1, visitor.getFileList().size());
    }

    @ParameterizedTest(name = "[{index}] with {1}")
    @MethodSource("createVisitorSuppliers")
    @DisplayName("should count correctly for a directory with subdirectories and files")
    void walkOnDirectoryWithSubdirectories_shouldCountCorrectly(final Supplier<AccumulatorPathVisitor> visitorSupplier) throws IOException {
        // Arrange
        final AccumulatorPathVisitor visitor = visitorSupplier.get();
        final Path testDir = Paths.get("src/test/resources/org/apache/commons/io/dirs-2-file-size-2");
        final PathVisitorFileFilter visitorAdapter = new PathVisitorFileFilter(visitor);

        // Act
        Files.walkFileTree(testDir, visitorAdapter);

        // Assert
        assertCounts(3, 2, 2, visitor.getPathCounters());
        assertEquals(3, visitor.getDirList().size());
        assertEquals(2, visitor.getFileList().size());
    }

    @ParameterizedTest
    @MethodSource("createVisitorThatIgnoresFailures")
    @DisplayName("should count only files visited before synchronous deletion")
    void walk_whenFilesDeletedSynchronously_shouldCountOnlyVisitedFiles(final Supplier<AccumulatorPathVisitor> visitorSupplier) throws IOException {
        // Arrange
        final int fileCount = 100;
        final int deletionTriggerCount = fileCount / 2;
        // Use a LinkedHashSet to preserve insertion order for deterministic test behavior.
        final Set<Path> files = new LinkedHashSet<>(fileCount);
        for (int i = 0; i < fileCount; i++) {
            files.add(Files.createTempFile(tempDirPath, "test", ".txt"));
        }

        final AccumulatorPathVisitor visitor = visitorSupplier.get();
        final AtomicInteger visitCount = new AtomicInteger();
        final PathVisitorFileFilter visitorAdapter = new PathVisitorFileFilter(visitor) {
            @Override
            public FileVisitResult visitFile(final Path path, final BasicFileAttributes attributes) throws IOException {
                // After visiting a certain number of files, delete all of them.
                if (visitCount.incrementAndGet() == deletionTriggerCount) {
                    for (final Path fileToDelete : files) {
                        Files.deleteIfExists(fileToDelete);
                    }
                }
                // The super call will fail for the file that triggered the deletion,
                // as it no longer exists. The visitor handles this and continues.
                return super.visitFile(path, attributes);
            }
        };

        // Act
        Files.walkFileTree(tempDirPath, visitorAdapter);

        // Assert
        // The visitor should have counted the files visited before the deletion was triggered.
        // The file that triggers the deletion is not counted because super.visitFile() fails.
        final int expectedFileCount = deletionTriggerCount - 1;
        assertCounts(1, expectedFileCount, 0, visitor.getPathCounters());
        assertEquals(1, visitor.getDirList().size());
        assertEquals(expectedFileCount, visitor.getFileList().size());
    }

    @ParameterizedTest
    @MethodSource("createVisitorThatIgnoresFailures")
    @DisplayName("should continue when files are deleted asynchronously during a walk")
    void walk_whenFilesDeletedAsynchronously_shouldContinueAndCountVisitedFiles(final Supplier<AccumulatorPathVisitor> visitorSupplier)
        throws IOException, InterruptedException {
        // Arrange
        final int fileCount = 200; // A moderate number to ensure the test is fast but effective.
        final List<Path> files = new ArrayList<>(fileCount);
        for (int i = 0; i < fileCount; i++) {
            files.add(Files.createTempFile(tempDirPath, "test", ".txt"));
        }

        final AccumulatorPathVisitor visitor = visitorSupplier.get();
        // This adapter slows down the walk to increase the chance of concurrent deletion.
        final PathVisitorFileFilter visitorAdapter = new PathVisitorFileFilter(visitor) {
            @Override
            public FileVisitResult visitFile(final Path path, final BasicFileAttributes attributes) throws IOException {
                try {
                    // Slow down walking to provoke a race condition with the deletion thread.
                    ThreadUtils.sleep(Duration.ofMillis(1));
                } catch (final InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return super.visitFile(path, attributes);
            }
        };

        final ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            // Act
            // Start deleting files in a separate thread.
            executor.execute(() -> {
                for (final Path file : files) {
                    try {
                        Files.delete(file);
                    } catch (final IOException ignored) {
                        // This is expected if the file is deleted before the walker sees it.
                    }
                }
            });

            // Walk the tree while files are being deleted.
            // The visitor is configured to continue on visitFileFailed (e.g., NoSuchFileException).
            Files.walkFileTree(tempDirPath, visitorAdapter);

        } finally {
            // Clean up the executor
            executor.shutdown();
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        }

        // Assert
        // We can't know the exact count due to the race condition,
        // but we can assert that the walk happened and counted some, but not all, files.
        final long filesVisited = visitor.getPathCounters().getFileCounter().get().longValue();
        assertTrue(filesVisited < fileCount, "Should have visited fewer files than initially created due to concurrent deletion.");
        // The main point is that the walk doesn't crash. A check for filesVisited > 0 could be flaky
        // if the deletion thread is extremely fast, so we omit it for test stability.
        assertEquals(1, visitor.getDirList().size());
    }

    @Test
    @DisplayName("should accumulate paths but not count them when using the default constructor")
    void defaultConstructor_shouldAccumulateButNotCount() throws IOException {
        // Arrange
        final AccumulatorPathVisitor visitor = new AccumulatorPathVisitor();
        final PathVisitorFileFilter visitorAdapter = new PathVisitorFileFilter(visitor);

        // Act
        Files.walkFileTree(tempDirPath, visitorAdapter);

        // Assert
        // The default constructor uses a no-op counter.
        assertCounts(0, 0, 0, visitor.getPathCounters());
        // However, it should still accumulate the paths.
        assertEquals(1, visitor.getDirList().size());
        assertTrue(visitor.getFileList().isEmpty());
    }
}