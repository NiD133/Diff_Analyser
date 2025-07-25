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
 * Tests for {@link AccumulatorPathVisitor} and {@link PathVisitorFileFilter}.
 *
 * The tests verify that the AccumulatorPathVisitor correctly accumulates file and directory paths
 * and counts them as it walks a file tree.  It also tests edge cases such as empty directories and
 * concurrent file deletion during the walk.
 */
class AccumulatorPathVisitorTest {

    @TempDir
    Path tempDirPath;

    /**
     * Provides different ways to create an AccumulatorPathVisitor for parameterized tests.
     */
    static Stream<Arguments> accumulatorPathVisitorSuppliers() {
        // @formatter:off
        return Stream.of(
            Arguments.of((Supplier<AccumulatorPathVisitor>) AccumulatorPathVisitor::withLongCounters),
            Arguments.of((Supplier<AccumulatorPathVisitor>) AccumulatorPathVisitor::withBigIntegerCounters),
            Arguments.of((Supplier<AccumulatorPathVisitor>) () ->
                AccumulatorPathVisitor.withBigIntegerCounters(TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE)));
        // @formatter:on
    }

    /**
     * Provides AccumulatorPathVisitor suppliers configured to ignore failures, suitable for tests that
     * intentionally cause exceptions during file system operations.
     */
    static Stream<Arguments> accumulatorPathVisitorSuppliersIgnoringFailures() {
        // @formatter:off
        return Stream.of(
            Arguments.of((Supplier<AccumulatorPathVisitor>) () -> new AccumulatorPathVisitor(
                Counters.bigIntegerPathCounters(),
                CountingPathVisitor.defaultDirectoryFilter(),
                CountingPathVisitor.defaultFileFilter())));
        // @formatter:on
    }

    /**
     * Tests the 0-argument constructor.  This constructor is deprecated and should still function as expected.
     */
    @Test
    void test0ArgConstructor() throws IOException {
        // Given an AccumulatorPathVisitor created with the default constructor
        final AccumulatorPathVisitor accPathVisitor = new AccumulatorPathVisitor();
        final PathVisitorFileFilter countingFileFilter = new PathVisitorFileFilter(accPathVisitor);

        // When we walk an empty temp directory with the visitor
        Files.walkFileTree(tempDirPath, new AndFileFilter(countingFileFilter, DirectoryFileFilter.INSTANCE, EmptyFileFilter.EMPTY));

        // Then the visitor should have counted 0 files, 0 bytes, and 0 directories,
        // and its directory list should contain only the root directory.
        assertCounts(0, 0, 0, accPathVisitor.getPathCounters());
        assertEquals(1, accPathVisitor.getDirList().size());
        assertTrue(accPathVisitor.getFileList().isEmpty());

        // And the visitor should be equal to itself and have a consistent hashCode.
        assertEquals(accPathVisitor, accPathVisitor);
        assertEquals(accPathVisitor.hashCode(), accPathVisitor.hashCode());
    }

    /**
     * Tests walking an empty folder.  The visitor should count one directory (the root).
     */
    @ParameterizedTest
    @MethodSource("accumulatorPathVisitorSuppliers")
    void testEmptyFolder(final Supplier<AccumulatorPathVisitor> supplier) throws IOException {
        // Given an AccumulatorPathVisitor
        final AccumulatorPathVisitor accPathVisitor = supplier.get();
        final PathVisitorFileFilter countingFileFilter = new PathVisitorFileFilter(accPathVisitor);

        // When we walk an empty temp directory with the visitor
        Files.walkFileTree(tempDirPath, new AndFileFilter(countingFileFilter, DirectoryFileFilter.INSTANCE, EmptyFileFilter.EMPTY));

        // Then the visitor should have counted 1 directory, 0 files, and 0 bytes,
        // and its directory list should contain only the root directory.
        assertCounts(1, 0, 0, accPathVisitor.getPathCounters());
        assertEquals(1, accPathVisitor.getDirList().size());
        assertTrue(accPathVisitor.getFileList().isEmpty());

        // And the visitor should be equal to itself and have a consistent hashCode.
        assertEquals(accPathVisitor, accPathVisitor);
        assertEquals(accPathVisitor.hashCode(), accPathVisitor.hashCode());
    }

    /**
     * Tests the equals() and hashCode() methods of AccumulatorPathVisitor.
     */
    @Test
    void testEqualsHashCode() {
        // Given two AccumulatorPathVisitors with the same initial state
        final AccumulatorPathVisitor visitor0 = AccumulatorPathVisitor.withLongCounters();
        final AccumulatorPathVisitor visitor1 = AccumulatorPathVisitor.withLongCounters();

        // Then they should be equal and have the same hashCode
        assertEquals(visitor0, visitor0);
        assertEquals(visitor0, visitor1);
        assertEquals(visitor1, visitor0);
        assertEquals(visitor0.hashCode(), visitor0.hashCode());
        assertEquals(visitor0.hashCode(), visitor1.hashCode());
        assertEquals(visitor1.hashCode(), visitor0.hashCode());

        // When we modify one of the visitors
        visitor0.getPathCounters().getByteCounter().increment();

        // Then they should no longer be equal and should have different hashCodes
        assertEquals(visitor0, visitor0); // Reflexive check after modification
        assertNotEquals(visitor0, visitor1);
        assertNotEquals(visitor1, visitor0);
        assertEquals(visitor0.hashCode(), visitor0.hashCode()); // Reflexive check after modification
        assertNotEquals(visitor0.hashCode(), visitor1.hashCode());
        assertNotEquals(visitor1.hashCode(), visitor0.hashCode());
    }

    /**
     * Tests a directory with one file of size 0.
     */
    @ParameterizedTest
    @MethodSource("accumulatorPathVisitorSuppliers")
    void testFolders1FileSize0(final Supplier<AccumulatorPathVisitor> supplier) throws IOException {
        // Given an AccumulatorPathVisitor and a directory with one file of size 0
        final AccumulatorPathVisitor accPathVisitor = supplier.get();
        final PathVisitorFileFilter countingFileFilter = new PathVisitorFileFilter(accPathVisitor);
        final Path testDir = Paths.get("src/test/resources/org/apache/commons/io/dirs-1-file-size-0");

        // When we walk the directory with the visitor
        Files.walkFileTree(testDir, countingFileFilter);

        // Then the visitor should have counted 1 directory, 1 file, and 0 bytes.
        assertEquals(1, accPathVisitor.getDirList().size());
        assertEquals(1, accPathVisitor.getFileList().size());
        assertCounts(1, 1, 0, accPathVisitor.getPathCounters());

        // And the visitor should be equal to itself and have a consistent hashCode.
        assertEquals(accPathVisitor, accPathVisitor);
        assertEquals(accPathVisitor.hashCode(), accPathVisitor.hashCode());
    }

    /**
     * Tests a directory with one file of size 1.
     */
    @ParameterizedTest
    @MethodSource("accumulatorPathVisitorSuppliers")
    void testFolders1FileSize1(final Supplier<AccumulatorPathVisitor> supplier) throws IOException {
        // Given an AccumulatorPathVisitor and a directory with one file of size 1
        final AccumulatorPathVisitor accPathVisitor = supplier.get();
        final PathVisitorFileFilter countingFileFilter = new PathVisitorFileFilter(accPathVisitor);
        final Path testDir = Paths.get("src/test/resources/org/apache/commons/io/dirs-1-file-size-1");

        // When we walk the directory with the visitor
        Files.walkFileTree(testDir, countingFileFilter);

        // Then the visitor should have counted 1 directory, 1 file, and 1 bytes.
        assertCounts(1, 1, 1, accPathVisitor.getPathCounters());
        assertEquals(1, accPathVisitor.getDirList().size());
        assertEquals(1, accPathVisitor.getFileList().size());

        // And the visitor should be equal to itself and have a consistent hashCode.
        assertEquals(accPathVisitor, accPathVisitor);
        assertEquals(accPathVisitor.hashCode(), accPathVisitor.hashCode());
    }

    /**
     * Tests a directory with two subdirectories, each containing one file of size 1.
     */
    @ParameterizedTest
    @MethodSource("accumulatorPathVisitorSuppliers")
    void testFolders2FileSize2(final Supplier<AccumulatorPathVisitor> supplier) throws IOException {
        // Given an AccumulatorPathVisitor and a directory with two subdirectories,
        // each containing one file of size 1
        final AccumulatorPathVisitor accPathVisitor = supplier.get();
        final PathVisitorFileFilter countingFileFilter = new PathVisitorFileFilter(accPathVisitor);
        final Path testDir = Paths.get("src/test/resources/org/apache/commons/io/dirs-2-file-size-2");

        // When we walk the directory with the visitor
        Files.walkFileTree(testDir, countingFileFilter);

        // Then the visitor should have counted 3 directories, 2 files, and 2 bytes.
        assertCounts(3, 2, 2, accPathVisitor.getPathCounters());
        assertEquals(3, accPathVisitor.getDirList().size());
        assertEquals(2, accPathVisitor.getFileList().size());

        // And the visitor should be equal to itself and have a consistent hashCode.
        assertEquals(accPathVisitor, accPathVisitor);
        assertEquals(accPathVisitor.hashCode(), accPathVisitor.hashCode());
    }

    /**
     * Tests walking a directory while asynchronously deleting files within it (IO-755).
     * This test is designed to simulate a scenario where files are being deleted concurrently
     * with the directory traversal, which can lead to unexpected exceptions.
     */
    @ParameterizedTest
    @MethodSource("accumulatorPathVisitorSuppliersIgnoringFailures")
    void testFolderWhileDeletingAsync(final Supplier<AccumulatorPathVisitor> supplier) throws IOException, InterruptedException {
        // Given a directory with a large number of files and an AccumulatorPathVisitor
        final int count = 10_000;
        final List<Path> files = new ArrayList<>(count);
        for (int i = 1; i <= count; i++) {
            final Path tempFile = Files.createTempFile(tempDirPath, "test", ".txt");
            assertTrue(Files.exists(tempFile));
            files.add(tempFile);
        }

        final AccumulatorPathVisitor accPathVisitor = supplier.get();
        final PathVisitorFileFilter countingFileFilter = new PathVisitorFileFilter(accPathVisitor) {
            @Override
            public FileVisitResult visitFile(final Path path, final BasicFileAttributes attributes) throws IOException {
                // Slow down the walking a bit to try and cause conflicts with the deletion thread
                try {
                    ThreadUtils.sleep(Duration.ofMillis(10));
                } catch (final InterruptedException ignore) {
                    // e.printStackTrace();
                }
                return super.visitFile(path, attributes);
            }
        };

        // When we walk the directory while asynchronously deleting the files
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final AtomicBoolean deleted = new AtomicBoolean();
        try {
            executor.execute(() -> {
                for (final Path file : files) {
                    try {
                        // File deletion is slow compared to tree walking, so we go as fast as we can here
                        Files.delete(file);
                    } catch (final IOException ignored) {
                        // e.printStackTrace();
                    }
                }
                deleted.set(true);
            });
            Files.walkFileTree(tempDirPath, countingFileFilter);
        } finally {
            // Ensure the deletion thread completes, even if it takes a while
            if (!deleted.get()) {
                ThreadUtils.sleep(Duration.ofMillis(1000));
            }
            if (!deleted.get()) {
                executor.awaitTermination(5, TimeUnit.SECONDS);
            }
            executor.shutdownNow();
        }

        // Then the visitor should still be equal to itself and have a consistent hashCode.
        assertEquals(accPathVisitor, accPathVisitor);
        assertEquals(accPathVisitor.hashCode(), accPathVisitor.hashCode());
        // Note: The exact counts are difficult to assert because of the concurrent deletion.
        // We focus on ensuring that the walk completes without throwing unexpected exceptions.
    }

    /**
     * Tests walking a directory while synchronously deleting files midway through the visit (IO-755).
     * This is a more controlled version of the asynchronous deletion test, allowing us to assert
     * the expected counts more precisely.
     */
    @ParameterizedTest
    @MethodSource("accumulatorPathVisitorSuppliersIgnoringFailures")
    void testFolderWhileDeletingSync(final Supplier<AccumulatorPathVisitor> supplier) throws IOException {
        // Given a directory with a number of files and an AccumulatorPathVisitor
        final int count = 100;
        final int marker = count / 2; // Delete files after visiting half of them
        final Set<Path> files = new LinkedHashSet<>(count);
        for (int i = 1; i <= count; i++) {
            final Path tempFile = Files.createTempFile(tempDirPath, "test", ".txt");
            assertTrue(Files.exists(tempFile));
            files.add(tempFile);
        }

        final AccumulatorPathVisitor accPathVisitor = supplier.get();
        final AtomicInteger visitCount = new AtomicInteger();
        final PathVisitorFileFilter countingFileFilter = new PathVisitorFileFilter(accPathVisitor) {
            @Override
            public FileVisitResult visitFile(final Path path, final BasicFileAttributes attributes) throws IOException {
                if (visitCount.incrementAndGet() == marker) {
                    // Now that we've visited half the files, delete them all
                    for (final Path file : files) {
                        Files.delete(file);
                    }
                }
                return super.visitFile(path, attributes);
            }
        };

        // When we walk the directory and delete the files synchronously after visiting half of them
        Files.walkFileTree(tempDirPath, countingFileFilter);

        // Then the visitor should have counted the directories, and only the files visited before deletion.
        // The number of bytes counted is unpredictable due to deletion.  We assert the directory and file counts.
        assertCounts(1, marker - 1, 0, accPathVisitor.getPathCounters());
        assertEquals(1, accPathVisitor.getDirList().size());
        assertEquals(marker - 1, accPathVisitor.getFileList().size());

        // And the visitor should be equal to itself and have a consistent hashCode.
        assertEquals(accPathVisitor, accPathVisitor);
        assertEquals(accPathVisitor.hashCode(), accPathVisitor.hashCode());
    }

}