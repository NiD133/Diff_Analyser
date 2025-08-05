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
 * Tests both {@link AccumulatorPathVisitor} and {@link PathVisitorFileFilter}.
 */
class AccumulatorPathVisitorTest {

    static Stream<Arguments> testParameters() {
        // @formatter:off
        return Stream.of(
            Arguments.of((Supplier<AccumulatorPathVisitor>) AccumulatorPathVisitor::withLongCounters),
            Arguments.of((Supplier<AccumulatorPathVisitor>) AccumulatorPathVisitor::withBigIntegerCounters),
            Arguments.of((Supplier<AccumulatorPathVisitor>) () ->
                AccumulatorPathVisitor.withBigIntegerCounters(TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE)));
        // @formatter:on
    }

    static Stream<Arguments> testParametersIgnoreFailures() {
        // @formatter:off
        return Stream.of(
            Arguments.of((Supplier<AccumulatorPathVisitor>) () -> new AccumulatorPathVisitor(
                Counters.bigIntegerPathCounters(),
                CountingPathVisitor.defaultDirectoryFilter(),
                CountingPathVisitor.defaultFileFilter())));
        // @formatter:on
    }

    @TempDir
    Path tempDirPath;

    // ========================= Basic Functionality Tests =========================

    /**
     * Tests the default constructor and basic behavior.
     */
    @Test
    void testZeroArgumentConstructor() throws IOException {
        final AccumulatorPathVisitor accPathVisitor = new AccumulatorPathVisitor();
        final PathVisitorFileFilter countingFileFilter = new PathVisitorFileFilter(accPathVisitor);
        Files.walkFileTree(tempDirPath, new AndFileFilter(countingFileFilter, DirectoryFileFilter.INSTANCE, EmptyFileFilter.EMPTY));
        assertCounts(0, 0, 0, accPathVisitor.getPathCounters());
        assertEquals(1, accPathVisitor.getDirList().size());
        assertTrue(accPathVisitor.getFileList().isEmpty());
        assertEquals(accPathVisitor, accPathVisitor);
        assertEquals(accPathVisitor.hashCode(), accPathVisitor.hashCode());
    }

    /**
     * Tests visiting an empty folder.
     */
    @ParameterizedTest
    @MethodSource("testParameters")
    void testVisitEmptyFolder(final Supplier<AccumulatorPathVisitor> supplier) throws IOException {
        final AccumulatorPathVisitor accPathVisitor = supplier.get();
        final PathVisitorFileFilter countingFileFilter = new PathVisitorFileFilter(accPathVisitor);
        Files.walkFileTree(tempDirPath, new AndFileFilter(countingFileFilter, DirectoryFileFilter.INSTANCE, EmptyFileFilter.EMPTY));
        assertCounts(1, 0, 0, accPathVisitor.getPathCounters());
        assertEquals(1, accPathVisitor.getDirList().size());
        assertTrue(accPathVisitor.getFileList().isEmpty());
        assertEquals(accPathVisitor, accPathVisitor);
        assertEquals(accPathVisitor.hashCode(), accPathVisitor.hashCode());
    }

    /**
     * Tests a directory with one file of size 0.
     */
    @ParameterizedTest
    @MethodSource("testParameters")
    void testVisitFolderWithOneFileOfSizeZero(final Supplier<AccumulatorPathVisitor> supplier) throws IOException {
        final AccumulatorPathVisitor accPathVisitor = supplier.get();
        final PathVisitorFileFilter countingFileFilter = new PathVisitorFileFilter(accPathVisitor);
        Files.walkFileTree(Paths.get("src/test/resources/org/apache/commons/io/dirs-1-file-size-0"), countingFileFilter);
        assertCounts(1, 1, 0, accPathVisitor.getPathCounters());
        assertEquals(1, accPathVisitor.getDirList().size());
        assertEquals(1, accPathVisitor.getFileList().size());
        assertEquals(accPathVisitor, accPathVisitor);
        assertEquals(accPathVisitor.hashCode(), accPathVisitor.hashCode());
    }

    /**
     * Tests a directory with one file of size 1.
     */
    @ParameterizedTest
    @MethodSource("testParameters")
    void testVisitFolderWithOneFileOfSizeOne(final Supplier<AccumulatorPathVisitor> supplier) throws IOException {
        final AccumulatorPathVisitor accPathVisitor = supplier.get();
        final PathVisitorFileFilter countingFileFilter = new PathVisitorFileFilter(accPathVisitor);
        Files.walkFileTree(Paths.get("src/test/resources/org/apache/commons/io/dirs-1-file-size-1"), countingFileFilter);
        assertCounts(1, 1, 1, accPathVisitor.getPathCounters());
        assertEquals(1, accPathVisitor.getDirList().size());
        assertEquals(1, accPathVisitor.getFileList().size());
        assertEquals(accPathVisitor, accPathVisitor);
        assertEquals(accPathVisitor.hashCode(), accPathVisitor.hashCode());
    }

    /**
     * Tests a directory with two subdirectories, each containing one file of size 1.
     */
    @ParameterizedTest
    @MethodSource("testParameters")
    void testVisitFolderWithTwoSubdirectoriesAndTwoFiles(final Supplier<AccumulatorPathVisitor> supplier) throws IOException {
        final AccumulatorPathVisitor accPathVisitor = supplier.get();
        final PathVisitorFileFilter countingFileFilter = new PathVisitorFileFilter(accPathVisitor);
        Files.walkFileTree(Paths.get("src/test/resources/org/apache/commons/io/dirs-2-file-size-2"), countingFileFilter);
        assertCounts(3, 2, 2, accPathVisitor.getPathCounters());
        assertEquals(3, accPathVisitor.getDirList().size());
        assertEquals(2, accPathVisitor.getFileList().size());
        assertEquals(accPathVisitor, accPathVisitor);
        assertEquals(accPathVisitor.hashCode(), accPathVisitor.hashCode());
    }

    // ========================= Object Contract Tests =========================

    /**
     * Tests equals() and hashCode() implementations.
     */
    @Test
    void testEqualsHashCode() {
        final AccumulatorPathVisitor visitor0 = AccumulatorPathVisitor.withLongCounters();
        final AccumulatorPathVisitor visitor1 = AccumulatorPathVisitor.withLongCounters();
        assertEquals(visitor0, visitor0);
        assertEquals(visitor0, visitor1);
        assertEquals(visitor1, visitor0);
        assertEquals(visitor0.hashCode(), visitor0.hashCode());
        assertEquals(visitor0.hashCode(), visitor1.hashCode());
        assertEquals(visitor1.hashCode(), visitor0.hashCode());
        
        // Modify state and verify inequality
        visitor0.getPathCounters().getByteCounter().increment();
        assertEquals(visitor0, visitor0);
        assertNotEquals(visitor0, visitor1);
        assertNotEquals(visitor1, visitor0);
        assertEquals(visitor0.hashCode(), visitor0.hashCode());
        assertNotEquals(visitor0.hashCode(), visitor1.hashCode());
        assertNotEquals(visitor1.hashCode(), visitor0.hashCode());
    }

    // ========================= Concurrency Tests =========================

    /**
     * Tests behavior when files are deleted asynchronously during traversal.
     * This simulates real-world scenarios where files might change during traversal.
     */
    @ParameterizedTest
    @MethodSource("testParametersIgnoreFailures")
    void testVisitFolderWhileDeletingFilesAsynchronously(final Supplier<AccumulatorPathVisitor> supplier) throws IOException, InterruptedException {
        // Create test files
        final int fileCount = 100;
        final List<Path> files = new ArrayList<>(fileCount);
        for (int i = 1; i <= fileCount; i++) {
            files.add(Files.createTempFile(tempDirPath, "test", ".txt"));
        }

        final AccumulatorPathVisitor accPathVisitor = supplier.get();
        final PathVisitorFileFilter countingFileFilter = new SlowPathVisitorFileFilter(accPathVisitor);
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final AtomicBoolean deletionCompleted = new AtomicBoolean();

        try (executor) {
            // Start file deletion in separate thread
            executor.execute(() -> {
                files.forEach(file -> {
                    try {
                        Files.delete(file);
                    } catch (IOException ignored) {
                        // Expected during concurrent modification
                    }
                });
                deletionCompleted.set(true);
            });

            // Walk file tree while files are being deleted
            Files.walkFileTree(tempDirPath, countingFileFilter);
        } finally {
            // Ensure cleanup if deletion didn't complete
            if (!deletionCompleted.get()) {
                executor.awaitTermination(5, TimeUnit.SECONDS);
            }
        }

        // Validate visitor state
        assertEquals(accPathVisitor, accPathVisitor);
        assertEquals(accPathVisitor.hashCode(), accPathVisitor.hashCode());
    }

    /**
     * Tests behavior when files are deleted synchronously during traversal.
     * Deletes files after visiting half of them to test interruption handling.
     */
    @ParameterizedTest
    @MethodSource("testParametersIgnoreFailures")
    void testVisitFolderWhileDeletingFilesSynchronously(final Supplier<AccumulatorPathVisitor> supplier) throws IOException {
        // Create test files
        final int fileCount = 100;
        final int deletionTrigger = fileCount / 2;
        final Set<Path> files = new LinkedHashSet<>(fileCount);
        for (int i = 1; i <= fileCount; i++) {
            files.add(Files.createTempFile(tempDirPath, "test", ".txt"));
        }

        final AccumulatorPathVisitor accPathVisitor = supplier.get();
        final AtomicInteger visitedCount = new AtomicInteger();
        final PathVisitorFileFilter countingFileFilter = new PathVisitorFileFilter(accPathVisitor) {
            @Override
            public FileVisitResult visitFile(final Path path, final BasicFileAttributes attributes) throws IOException {
                if (visitedCount.incrementAndGet() == deletionTrigger) {
                    // Delete all files after visiting half of them
                    files.forEach(file -> {
                        try {
                            Files.delete(file);
                        } catch (IOException ignored) {
                            // Expected during concurrent modification
                        }
                    });
                }
                return super.visitFile(path, attributes);
            }
        };

        // Walk file tree (files will be deleted midway)
        Files.walkFileTree(tempDirPath, countingFileFilter);

        // Validate visitor state
        assertCounts(1, deletionTrigger - 1, 0, accPathVisitor.getPathCounters());
        assertEquals(1, accPathVisitor.getDirList().size());
        assertEquals(deletionTrigger - 1, accPathVisitor.getFileList().size());
        assertEquals(accPathVisitor, accPathVisitor);
        assertEquals(accPathVisitor.hashCode(), accPathVisitor.hashCode());
    }

    // ========================= Helper Classes =========================

    /**
     * PathVisitorFileFilter that introduces small delays during file visits
     * to increase likelihood of concurrency issues during tests.
     */
    private static final class SlowPathVisitorFileFilter extends PathVisitorFileFilter {
        SlowPathVisitorFileFilter(AccumulatorPathVisitor visitor) {
            super(visitor);
        }

        @Override
        public FileVisitResult visitFile(final Path path, final BasicFileAttributes attributes) throws IOException {
            try {
                // Introduce delay to increase concurrency exposure
                ThreadUtils.sleep(Duration.ofMillis(10));
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
            return super.visitFile(path, attributes);
        }
    }
}