/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.io.file;

import static org.apache.commons.io.file.CounterAssertions.assertCounts;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
 * Tests both {@link AccumulatorPathVisitor} and its integration with {@link PathVisitorFileFilter}.
 */
class AccumulatorPathVisitorTest {

    @TempDir
    Path tempDirPath;

    /**
     * Provides different configurations of AccumulatorPathVisitor for parameterized tests.
     */
    static Stream<Arguments> accumulatorVisitorProvider() {
        // @formatter:off
        return Stream.of(
            Arguments.of((Supplier<AccumulatorPathVisitor>) AccumulatorPathVisitor::withLongCounters, "Long Counters"),
            Arguments.of((Supplier<AccumulatorPathVisitor>) AccumulatorPathVisitor::withBigIntegerCounters, "BigInteger Counters"),
            Arguments.of((Supplier<AccumulatorPathVisitor>) () ->
                AccumulatorPathVisitor.withBigIntegerCounters(TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE), "BigInteger Counters with Filters")
        );
        // @formatter:on
    }

    /**
     * Provides a visitor with BigInteger counters for concurrency tests.
     */
    static Stream<Arguments> visitorForConcurrencyTests() {
        // @formatter:off
        return Stream.of(
            Arguments.of((Supplier<AccumulatorPathVisitor>) () -> new AccumulatorPathVisitor(
                Counters.bigIntegerPathCounters(),
                CountingPathVisitor.defaultDirectoryFilter(),
                CountingPathVisitor.defaultFileFilter()), "BigInteger Counters")
        );
        // @formatter:on
    }

    /**
     * Tests that the default constructor creates a visitor that does not count bytes, files, or directories,
     * but still accumulates the visited directory paths.
     */
    @Test
    void testDefaultConstructorOnEmptyDirectory() throws IOException {
        // Arrange
        final AccumulatorPathVisitor accPathVisitor = new AccumulatorPathVisitor();
        // PathVisitorFileFilter is used to test integration between the two classes.
        final PathVisitorFileFilter countingFileFilter = new PathVisitorFileFilter(accPathVisitor);

        // Act
        Files.walkFileTree(tempDirPath, new AndFileFilter(countingFileFilter, DirectoryFileFilter.INSTANCE, EmptyFileFilter.EMPTY));

        // Assert
        assertCounts(0, 0, 0, accPathVisitor.getPathCounters()); // Default constructor uses no-op counters.
        assertEquals(1, accPathVisitor.getDirList().size());
        assertTrue(accPathVisitor.getFileList().isEmpty());
    }

    @ParameterizedTest(name = "{1}")
    @MethodSource("accumulatorVisitorProvider")
    void walkEmptyDirectory_shouldCountOneDirectoryAndZeroFiles(final Supplier<AccumulatorPathVisitor> supplier, final String name) throws IOException {
        // Arrange
        final AccumulatorPathVisitor accPathVisitor = supplier.get();
        final PathVisitorFileFilter countingFileFilter = new PathVisitorFileFilter(accPathVisitor);

        // Act
        Files.walkFileTree(tempDirPath, countingFileFilter);

        // Assert
        assertCounts(1, 0, 0, accPathVisitor.getPathCounters());
        assertEquals(1, accPathVisitor.getDirList().size());
        assertTrue(accPathVisitor.getFileList().isEmpty());
    }

    @ParameterizedTest(name = "{1}")
    @MethodSource("accumulatorVisitorProvider")
    void walkDirectoryWithOneEmptyFile_shouldCountOneFileAndZeroBytes(final Supplier<AccumulatorPathVisitor> supplier, final String name) throws IOException {
        // Arrange
        final AccumulatorPathVisitor accPathVisitor = supplier.get();
        final PathVisitorFileFilter countingFileFilter = new PathVisitorFileFilter(accPathVisitor);
        final Path startingPath = Paths.get("src/test/resources/org/apache/commons/io/dirs-1-file-size-0");

        // Act
        Files.walkFileTree(startingPath, countingFileFilter);

        // Assert
        assertCounts(1, 1, 0, accPathVisitor.getPathCounters());
        assertEquals(1, accPathVisitor.getDirList().size());
        assertEquals(1, accPathVisitor.getFileList().size());
    }

    @ParameterizedTest(name = "{1}")
    @MethodSource("accumulatorVisitorProvider")
    void walkDirectoryWithOneNonEmptyFile_shouldCountOneFileAndOneByte(final Supplier<AccumulatorPathVisitor> supplier, final String name) throws IOException {
        // Arrange
        final AccumulatorPathVisitor accPathVisitor = supplier.get();
        final PathVisitorFileFilter countingFileFilter = new PathVisitorFileFilter(accPathVisitor);
        final Path startingPath = Paths.get("src/test/resources/org/apache/commons/io/dirs-1-file-size-1");

        // Act
        Files.walkFileTree(startingPath, countingFileFilter);

        // Assert
        assertCounts(1, 1, 1, accPathVisitor.getPathCounters());
        assertEquals(1, accPathVisitor.getDirList().size());
        assertEquals(1, accPathVisitor.getFileList().size());
    }

    @ParameterizedTest(name = "{1}")
    @MethodSource("accumulatorVisitorProvider")
    void walkDirectoryWithSubdirectories_shouldCountAllDirectoriesAndFiles(final Supplier<AccumulatorPathVisitor> supplier, final String name) throws IOException {
        // Arrange
        final AccumulatorPathVisitor accPathVisitor = supplier.get();
        final PathVisitorFileFilter countingFileFilter = new PathVisitorFileFilter(accPathVisitor);
        final Path startingPath = Paths.get("src/test/resources/org/apache/commons/io/dirs-2-file-size-2");

        // Act
        Files.walkFileTree(startingPath, countingFileFilter);

        // Assert
        assertCounts(3, 2, 2, accPathVisitor.getPathCounters());
        assertEquals(3, accPathVisitor.getDirList().size());
        assertEquals(2, accPathVisitor.getFileList().size());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        final AccumulatorPathVisitor visitor1 = AccumulatorPathVisitor.withLongCounters();
        final AccumulatorPathVisitor visitor2 = AccumulatorPathVisitor.withLongCounters();

        // Assert initial state
        assertEquals(visitor1, visitor2);
        assertEquals(visitor1.hashCode(), visitor2.hashCode());

        // Act: Modify one visitor
        visitor1.getPathCounters().getByteCounter().increment();

        // Assert modified state
        assertNotEquals(visitor1, visitor2);
        assertNotEquals(visitor1.hashCode(), visitor2.hashCode());
    }

    /**
     * Tests that walking a directory tree does not crash or deadlock when files are being deleted concurrently by another thread.
     * This test verifies robustness in a volatile file system environment. Due to the non-deterministic nature of
     * concurrent operations, it does not assert final counts. The test passes if {@link Files#walkFileTree}
     * completes without throwing an unhandled exception.
     */
    @ParameterizedTest(name = "{1}")
    @MethodSource("visitorForConcurrencyTests")
    void walkDirectory_shouldNotCrashWhenFilesAreDeletedConcurrently(final Supplier<AccumulatorPathVisitor> supplier, final String name) throws IOException, InterruptedException {
        // Arrange
        final int fileCount = 1000; // Reduced for test stability and speed
        final List<Path> files = new ArrayList<>(fileCount);
        for (int i = 0; i < fileCount; i++) {
            files.add(Files.createTempFile(tempDirPath, "test", ".txt"));
        }

        final AccumulatorPathVisitor accPathVisitor = supplier.get();
        final PathVisitorFileFilter countingFileFilter = new PathVisitorFileFilter(accPathVisitor) {
            @Override
            public FileVisitResult visitFile(final Path path, final BasicFileAttributes attributes) throws IOException {
                // Slow down the walk to increase the chance of a concurrent deletion.
                ThreadUtils.sleepQuietly(Duration.ofMillis(1));
                try {
                    return super.visitFile(path, attributes);
                } catch (final UncheckedIOException e) {
                    // This is expected if the file is deleted after the walk finds it but before the visitor processes it.
                    return FileVisitResult.CONTINUE;
                }
            }
        };

        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final AtomicBoolean deletionFinished = new AtomicBoolean(false);

        // Act
        try {
            executor.execute(() -> {
                files.forEach(file -> {
                    try {
                        Files.delete(file);
                    } catch (final IOException ignored) {
                        // Ignored: file may have already been deleted or is being accessed.
                    }
                });
                deletionFinished.set(true);
            });
            // This walk is expected to encounter IOExceptions for files deleted mid-walk,
            // which are handled by the visitor and do not crash the process.
            Files.walkFileTree(tempDirPath, countingFileFilter);
        } finally {
            // Assert
            executor.shutdown();
            // Wait for the deletion thread to complete to ensure a clean exit.
            final boolean terminated = executor.awaitTermination(5, TimeUnit.SECONDS);
            assertTrue(terminated, "Deletion thread did not terminate in time.");
            assertTrue(deletionFinished.get(), "Deletion thread did not finish.");
        }
    }

    /**
     * Tests that the visitor correctly counts only the files visited *before* a mass deletion occurs mid-walk.
     * This test is synchronous to ensure deterministic behavior.
     */
    @ParameterizedTest(name = "{1}")
    @MethodSource("visitorForConcurrencyTests")
    void walkDirectory_shouldCountOnlyFilesVisitedBeforeSyncDeletion(final Supplier<AccumulatorPathVisitor> supplier, final String name) throws IOException {
        // Arrange
        final int fileCount = 100;
        final int deletionTriggerCount = fileCount / 2;

        // Create files with predictable names to ensure a deterministic walk order.
        final Set<Path> files = IntStream.rangeClosed(1, fileCount)
            .mapToObj(i -> tempDirPath.resolve(String.format("file-%03d.txt", i)))
            .collect(Collectors.toSet());
        for (final Path file : files) {
            Files.createFile(file);
        }

        final AccumulatorPathVisitor accPathVisitor = supplier.get();
        final AtomicInteger visitCount = new AtomicInteger();
        final PathVisitorFileFilter countingFileFilter = new PathVisitorFileFilter(accPathVisitor) {
            @Override
            public FileVisitResult visitFile(final Path path, final BasicFileAttributes attributes) throws IOException {
                if (visitCount.incrementAndGet() == deletionTriggerCount) {
                    // When the 50th file is visited, delete all files.
                    for (final Path fileToDelete : files) {
                        Files.delete(fileToDelete);
                    }
                }
                // The call to super.visitFile for the 50th file will fail with a
                // NoSuchFileException because it was just deleted.
                return super.visitFile(path, attributes);
            }
        };

        // Act
        try {
            Files.walkFileTree(tempDirPath, countingFileFilter);
        } catch (final IOException e) {
            // An IOException (likely NoSuchFileException) is expected when the walk
            // attempts to process the 50th file, which was just deleted. This terminates the walk.
        }

        // Assert
        // The walk successfully processed 49 files before the 50th visit triggered the deletion and a subsequent exception.
        final int expectedFileCount = deletionTriggerCount - 1;
        assertCounts(1, expectedFileCount, 0, accPathVisitor.getPathCounters());
        assertEquals(1, accPathVisitor.getDirList().size());
        assertEquals(expectedFileCount, accPathVisitor.getFileList().size());
    }
}