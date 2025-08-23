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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.apache.commons.io.ThreadUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
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
    static Stream<Arguments> visitorFactories() {
        // @formatter:off
        return Stream.of(
            Arguments.of((Supplier<AccumulatorPathVisitor>) AccumulatorPathVisitor::withLongCounters, "Long Counters"),
            Arguments.of((Supplier<AccumulatorPathVisitor>) AccumulatorPathVisitor::withBigIntegerCounters, "BigInteger Counters"),
            Arguments.of((Supplier<AccumulatorPathVisitor>) () -> AccumulatorPathVisitor.withBigIntegerCounters(TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE), "BigInteger Counters with Filters")
        );
        // @formatter:on
    }

    /**
     * Provides a supplier for creating an AccumulatorPathVisitor that is configured to ignore file system errors,
     * such as a file not being found during a walk.
     */
    static Stream<Arguments> visitorFactoriesIgnoringErrors() {
        // @formatter:off
        return Stream.of(
            Arguments.of((Supplier<AccumulatorPathVisitor>) () -> new AccumulatorPathVisitor(
                Counters.bigIntegerPathCounters(),
                CountingPathVisitor.defaultDirectoryFilter(),
                CountingPathVisitor.defaultFileFilter()))
        );
        // @formatter:on
    }

    @Nested
    @DisplayName("Basic file tree walking and counting")
    class BasicWalkTests {

        @ParameterizedTest(name = "with {1}")
        @MethodSource("org.apache.commons.io.file.AccumulatorPathVisitorTest#visitorFactories")
        @DisplayName("should correctly count a single empty directory")
        void shouldCountSingleDirectoryWhenWalkingEmptyDirectory(final Supplier<AccumulatorPathVisitor> supplier) throws IOException {
            final AccumulatorPathVisitor visitor = supplier.get();

            Files.walkFileTree(tempDirPath, visitor);

            assertCounts(1, 0, 0, visitor.getPathCounters());
            assertEquals(1, visitor.getDirList().size());
            assertTrue(visitor.getFileList().isEmpty());
        }

        @ParameterizedTest(name = "with {1}")
        @MethodSource("org.apache.commons.io.file.AccumulatorPathVisitorTest#visitorFactories")
        @DisplayName("should correctly count a directory with one 0-byte file")
        void shouldCountOneEmptyFileWhenWalkingDirectoryWithOneEmptyFile(final Supplier<AccumulatorPathVisitor> supplier) throws IOException {
            final AccumulatorPathVisitor visitor = supplier.get();
            final Path startDir = Paths.get("src/test/resources/org/apache/commons/io/dirs-1-file-size-0");

            Files.walkFileTree(startDir, visitor);

            assertCounts(1, 1, 0, visitor.getPathCounters());
            assertEquals(1, visitor.getDirList().size());
            assertEquals(1, visitor.getFileList().size());
        }

        @ParameterizedTest(name = "with {1}")
        @MethodSource("org.apache.commons.io.file.AccumulatorPathVisitorTest#visitorFactories")
        @DisplayName("should correctly count a directory with one non-empty file")
        void shouldCountOneNonEmptyFileWhenWalkingDirectoryWithOneFile(final Supplier<AccumulatorPathVisitor> supplier) throws IOException {
            final AccumulatorPathVisitor visitor = supplier.get();
            final Path startDir = Paths.get("src/test/resources/org/apache/commons/io/dirs-1-file-size-1");

            Files.walkFileTree(startDir, visitor);

            assertCounts(1, 1, 1, visitor.getPathCounters());
            assertEquals(1, visitor.getDirList().size());
            assertEquals(1, visitor.getFileList().size());
        }

        @ParameterizedTest(name = "with {1}")
        @MethodSource("org.apache.commons.io.file.AccumulatorPathVisitorTest#visitorFactories")
        @DisplayName("should correctly count files in subdirectories")
        void shouldCountFilesInSubdirectoriesWhenWalkingDirectoryTree(final Supplier<AccumulatorPathVisitor> supplier) throws IOException {
            final AccumulatorPathVisitor visitor = supplier.get();
            final Path startDir = Paths.get("src/test/resources/org/apache/commons/io/dirs-2-file-size-2");

            Files.walkFileTree(startDir, visitor);

            assertCounts(3, 2, 2, visitor.getPathCounters());
            assertEquals(3, visitor.getDirList().size());
            assertEquals(2, visitor.getFileList().size());
        }
    }

    @Nested
    @DisplayName("Walking a file tree with concurrent modifications")
    class ConcurrencyTests {

        /**
         * Tests that the visitor remains robust and does not throw an exception when files are deleted
         * by another thread during the file tree walk. The exact number of files visited is non-deterministic.
         */
        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.AccumulatorPathVisitorTest#visitorFactoriesIgnoringErrors")
        @Timeout(value = 30, unit = TimeUnit.SECONDS)
        @DisplayName("should not fail when files are deleted concurrently during walk")
        void shouldNotFailWhenFilesAreDeletedConcurrentlyDuringWalk(final Supplier<AccumulatorPathVisitor> supplier) throws IOException, InterruptedException {
            final int fileCount = 10_000;
            final List<Path> files = new ArrayList<>(fileCount);
            for (int i = 0; i < fileCount; i++) {
                files.add(Files.createTempFile(tempDirPath, "test", ".txt"));
            }

            final AccumulatorPathVisitor visitor = supplier.get();
            final AccumulatorPathVisitor slowVisitor = new AccumulatorPathVisitor(visitor.getPathCounters(), visitor.getFileFilter(), visitor.getDirFilter()) {
                @Override
                public FileVisitResult visitFile(final Path path, final BasicFileAttributes attributes) throws IOException {
                    // Slow down the walk to increase the chance of a race condition with the deletion thread.
                    ThreadUtils.sleepQuietly(Duration.ofMillis(1));
                    return super.visitFile(path, attributes);
                }
            };

            final ExecutorService executor = Executors.newSingleThreadExecutor();
            try {
                // Start a task to delete all files concurrently.
                executor.execute(() -> {
                    for (final Path file : files) {
                        try {
                            Files.delete(file);
                        } catch (final IOException ignored) {
                            // Ignore, as the file might have already been deleted or is being accessed.
                        }
                    }
                });
                Files.walkFileTree(tempDirPath, slowVisitor);
            } finally {
                executor.shutdown();
                if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            }

            // Assert that the walk completed and counted the root directory.
            // The number of files counted is non-deterministic, as it depends on the race between walking and deleting.
            assertEquals(1, visitor.getPathCounters().getDirectoryCounter().get().longValue());
            assertTrue(visitor.getPathCounters().getFileCounter().get().longValue() < fileCount);
        }

        /**
         * Tests a synchronous deletion scenario where all files are deleted from within the visitFile method.
         * This provides a deterministic way to test handling of disappearing files.
         */
        @ParameterizedTest
        @MethodSource("org.apache.commons.io.file.AccumulatorPathVisitorTest#visitorFactoriesIgnoringErrors")
        @DisplayName("should count only visited files when all files are deleted mid-walk")
        void shouldCountOnlyVisitedFilesWhenAllFilesAreDeletedMidWalk(final Supplier<AccumulatorPathVisitor> supplier) throws IOException {
            final int fileCount = 100;
            final int deletionTriggerCount = fileCount / 2;
            final Set<Path> files = new LinkedHashSet<>(fileCount);
            for (int i = 0; i < fileCount; i++) {
                files.add(Files.createTempFile(tempDirPath, "test", ".txt"));
            }

            final AccumulatorPathVisitor visitor = supplier.get();
            final AtomicInteger visitCount = new AtomicInteger();
            final AccumulatorPathVisitor deletingVisitor = new AccumulatorPathVisitor(visitor.getPathCounters(), visitor.getFileFilter(), visitor.getDirFilter()) {
                @Override
                public FileVisitResult visitFile(final Path path, final BasicFileAttributes attributes) throws IOException {
                    if (visitCount.incrementAndGet() == deletionTriggerCount) {
                        // After visiting half the files, delete them all.
                        for (final Path file : files) {
                            Files.delete(file);
                        }
                    }
                    return super.visitFile(path, attributes);
                }
            };

            Files.walkFileTree(tempDirPath, deletingVisitor);

            // The visitor should have counted the files visited before they were all deleted.
            final int expectedFileCount = deletionTriggerCount - 1;
            assertCounts(1, expectedFileCount, 0, visitor.getPathCounters());
            assertEquals(1, visitor.getDirList().size());
            assertEquals(expectedFileCount, visitor.getFileList().size());
        }
    }

    @Nested
    @DisplayName("equals() and hashCode() contract")
    class ContractTests {

        @Test
        @DisplayName("should adhere to the equals() and hashCode() contract")
        void shouldAdhereToEqualsAndHashCodeContract() {
            final AccumulatorPathVisitor visitor1 = AccumulatorPathVisitor.withLongCounters();
            final AccumulatorPathVisitor visitor2 = AccumulatorPathVisitor.withLongCounters();

            // Two new instances should be equal
            assertEquals(visitor1, visitor2);
            assertEquals(visitor1.hashCode(), visitor2.hashCode());

            // After modification, they should no longer be equal
            visitor1.getPathCounters().getByteCounter().increment();
            assertNotEquals(visitor1, visitor2);
            assertNotEquals(visitor1.hashCode(), visitor2.hashCode());
        }
    }
}