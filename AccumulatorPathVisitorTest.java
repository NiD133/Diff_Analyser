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
 * AccumulatorPathVisitor collects paths during file tree traversal and maintains counters
 * for directories, files, and total bytes visited.
 */
class AccumulatorPathVisitorTest {

    private static final String TEST_RESOURCES_PATH = "src/test/resources/org/apache/commons/io/";
    private static final String EMPTY_DIR_TEST_PATH = TEST_RESOURCES_PATH + "dirs-1-file-size-0";
    private static final String SINGLE_FILE_TEST_PATH = TEST_RESOURCES_PATH + "dirs-1-file-size-1";
    private static final String MULTIPLE_FILES_TEST_PATH = TEST_RESOURCES_PATH + "dirs-2-file-size-2";
    
    private static final int CONCURRENT_DELETE_FILE_COUNT = 10_000;
    private static final int SYNC_DELETE_FILE_COUNT = 100;
    private static final Duration FILE_VISIT_DELAY = Duration.ofMillis(10);
    private static final Duration DELETION_WAIT_TIMEOUT = Duration.ofSeconds(5);

    @TempDir
    Path tempDirPath;

    /**
     * Provides different AccumulatorPathVisitor configurations for parameterized tests.
     * Tests with Long counters, BigInteger counters, and custom filters.
     */
    static Stream<Arguments> standardVisitorConfigurations() {
        return Stream.of(
            Arguments.of(namedSupplier("LongCounters", AccumulatorPathVisitor::withLongCounters)),
            Arguments.of(namedSupplier("BigIntegerCounters", AccumulatorPathVisitor::withBigIntegerCounters)),
            Arguments.of(namedSupplier("BigIntegerWithFilters", () ->
                AccumulatorPathVisitor.withBigIntegerCounters(TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE)))
        );
    }

    /**
     * Provides AccumulatorPathVisitor configurations that ignore failures during traversal.
     * Used for testing concurrent file deletion scenarios.
     */
    static Stream<Arguments> failureIgnoringVisitorConfigurations() {
        return Stream.of(
            Arguments.of(namedSupplier("IgnoreFailures", () -> new AccumulatorPathVisitor(
                Counters.bigIntegerPathCounters(),
                CountingPathVisitor.defaultDirectoryFilter(),
                CountingPathVisitor.defaultFileFilter())))
        );
    }

    /**
     * Helper method to create named suppliers for better test display names.
     */
    private static Supplier<AccumulatorPathVisitor> namedSupplier(String name, Supplier<AccumulatorPathVisitor> supplier) {
        return new Supplier<AccumulatorPathVisitor>() {
            @Override
            public AccumulatorPathVisitor get() {
                return supplier.get();
            }
            
            @Override
            public String toString() {
                return name;
            }
        };
    }

    /**
     * Tests that the default constructor creates a visitor that doesn't count anything
     * when used with filters that exclude all files.
     */
    @Test
    void testDefaultConstructor_WithEmptyFileFilter_CountsOnlyDirectory() throws IOException {
        // Given: A visitor with default constructor and filters that exclude files
        final AccumulatorPathVisitor visitor = new AccumulatorPathVisitor();
        final PathVisitorFileFilter fileFilter = new PathVisitorFileFilter(visitor);
        final AndFileFilter excludeFilesFilter = new AndFileFilter(
            fileFilter, 
            DirectoryFileFilter.INSTANCE, 
            EmptyFileFilter.EMPTY
        );
        
        // When: Walking the directory tree with filters
        Files.walkFileTree(tempDirPath, excludeFilesFilter);
        
        // Then: Only the directory should be counted
        assertCounts(0, 0, 0, visitor.getPathCounters());
        assertEquals(1, visitor.getDirList().size(), "Should have visited exactly one directory");
        assertTrue(visitor.getFileList().isEmpty(), "Should not have visited any files");
        
        // Verify equals and hashCode work correctly
        assertEquals(visitor, visitor);
        assertEquals(visitor.hashCode(), visitor.hashCode());
    }

    /**
     * Tests visiting an empty directory.
     */
    @ParameterizedTest(name = "EmptyFolder with {0}")
    @MethodSource("standardVisitorConfigurations")
    void testEmptyDirectory_CountsOnlyTheDirectory(final Supplier<AccumulatorPathVisitor> visitorSupplier) throws IOException {
        // Given: An empty directory and a visitor
        final AccumulatorPathVisitor visitor = visitorSupplier.get();
        final PathVisitorFileFilter fileFilter = new PathVisitorFileFilter(visitor);
        final AndFileFilter excludeFilesFilter = new AndFileFilter(
            fileFilter, 
            DirectoryFileFilter.INSTANCE, 
            EmptyFileFilter.EMPTY
        );
        
        // When: Walking the empty directory
        Files.walkFileTree(tempDirPath, excludeFilesFilter);
        
        // Then: Should count only the directory itself
        assertCounts(1, 0, 0, visitor.getPathCounters());
        assertEquals(1, visitor.getDirList().size(), "Should have visited exactly one directory");
        assertTrue(visitor.getFileList().isEmpty(), "Should not have visited any files");
    }

    /**
     * Tests that equals() and hashCode() methods work correctly.
     */
    @Test
    void testEqualsAndHashCode_ConsidersCounterState() {
        // Given: Two visitors with the same initial state
        final AccumulatorPathVisitor visitor1 = AccumulatorPathVisitor.withLongCounters();
        final AccumulatorPathVisitor visitor2 = AccumulatorPathVisitor.withLongCounters();
        
        // Then: Initially they should be equal
        assertEquals(visitor1, visitor1, "Visitor should equal itself");
        assertEquals(visitor1, visitor2, "Visitors with same state should be equal");
        assertEquals(visitor2, visitor1, "Equality should be symmetric");
        assertEquals(visitor1.hashCode(), visitor2.hashCode(), "Equal objects should have equal hash codes");
        
        // When: Modifying one visitor's state
        visitor1.getPathCounters().getByteCounter().increment();
        
        // Then: They should no longer be equal
        assertEquals(visitor1, visitor1, "Visitor should still equal itself");
        assertNotEquals(visitor1, visitor2, "Visitors with different states should not be equal");
        assertNotEquals(visitor2, visitor1, "Inequality should be symmetric");
        assertNotEquals(visitor1.hashCode(), visitor2.hashCode(), "Unequal objects should have different hash codes");
    }

    /**
     * Tests visiting a directory containing one empty file.
     */
    @ParameterizedTest(name = "SingleEmptyFile with {0}")
    @MethodSource("standardVisitorConfigurations")
    void testDirectoryWithSingleEmptyFile(final Supplier<AccumulatorPathVisitor> visitorSupplier) throws IOException {
        // Given: A test directory with one empty file
        final AccumulatorPathVisitor visitor = visitorSupplier.get();
        final PathVisitorFileFilter fileFilter = new PathVisitorFileFilter(visitor);
        
        // When: Walking the directory tree
        Files.walkFileTree(Paths.get(EMPTY_DIR_TEST_PATH), fileFilter);
        
        // Then: Should count 1 directory, 1 file, 0 bytes
        assertCounts(1, 1, 0, visitor.getPathCounters());
        assertEquals(1, visitor.getDirList().size(), "Should have visited exactly one directory");
        assertEquals(1, visitor.getFileList().size(), "Should have visited exactly one file");
    }

    /**
     * Tests visiting a directory containing one file with 1 byte.
     */
    @ParameterizedTest(name = "SingleByteFile with {0}")
    @MethodSource("standardVisitorConfigurations")
    void testDirectoryWithSingleByteFile(final Supplier<AccumulatorPathVisitor> visitorSupplier) throws IOException {
        // Given: A test directory with one 1-byte file
        final AccumulatorPathVisitor visitor = visitorSupplier.get();
        final PathVisitorFileFilter fileFilter = new PathVisitorFileFilter(visitor);
        
        // When: Walking the directory tree
        Files.walkFileTree(Paths.get(SINGLE_FILE_TEST_PATH), fileFilter);
        
        // Then: Should count 1 directory, 1 file, 1 byte
        assertCounts(1, 1, 1, visitor.getPathCounters());
        assertEquals(1, visitor.getDirList().size(), "Should have visited exactly one directory");
        assertEquals(1, visitor.getFileList().size(), "Should have visited exactly one file");
    }

    /**
     * Tests visiting a directory with two subdirectories, each containing one file.
     */
    @ParameterizedTest(name = "MultipleDirectoriesAndFiles with {0}")
    @MethodSource("standardVisitorConfigurations")
    void testMultipleDirectoriesWithFiles(final Supplier<AccumulatorPathVisitor> visitorSupplier) throws IOException {
        // Given: A test directory structure with 2 subdirectories and 2 files
        final AccumulatorPathVisitor visitor = visitorSupplier.get();
        final PathVisitorFileFilter fileFilter = new PathVisitorFileFilter(visitor);
        
        // When: Walking the directory tree
        Files.walkFileTree(Paths.get(MULTIPLE_FILES_TEST_PATH), fileFilter);
        
        // Then: Should count 3 directories (root + 2 subdirs), 2 files, 2 bytes total
        assertCounts(3, 2, 2, visitor.getPathCounters());
        assertEquals(3, visitor.getDirList().size(), "Should have visited three directories");
        assertEquals(2, visitor.getFileList().size(), "Should have visited two files");
    }

    /**
     * Tests concurrent file deletion during directory traversal (IO-755).
     * Creates many files and deletes them asynchronously while walking the tree.
     */
    @ParameterizedTest(name = "ConcurrentDeletion with {0}")
    @MethodSource("failureIgnoringVisitorConfigurations")
    void testConcurrentFileDeletionDuringTraversal(final Supplier<AccumulatorPathVisitor> visitorSupplier) 
            throws IOException, InterruptedException {
        // Given: Many temporary files
        final List<Path> createdFiles = createTempFiles(CONCURRENT_DELETE_FILE_COUNT);
        
        // And: A visitor that handles failures gracefully
        final AccumulatorPathVisitor visitor = visitorSupplier.get();
        final PathVisitorFileFilter fileFilter = createSlowFileVisitor(visitor);
        
        // When: Walking the tree while deleting files concurrently
        final AtomicBoolean deletionComplete = new AtomicBoolean(false);
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        
        try {
            // Start deleting files in background
            executor.execute(() -> {
                deleteAllFiles(createdFiles);
                deletionComplete.set(true);
            });
            
            // Walk the directory tree (some files may be deleted during traversal)
            Files.walkFileTree(tempDirPath, fileFilter);
        } finally {
            // Ensure cleanup completes
            waitForDeletion(executor, deletionComplete);
        }
        
        // Then: Visitor should handle the concurrent modifications gracefully
        assertEquals(visitor, visitor);
        assertEquals(visitor.hashCode(), visitor.hashCode());
    }

    /**
     * Tests synchronous file deletion during directory traversal (IO-755).
     * Deletes all files after visiting half of them.
     */
    @ParameterizedTest(name = "SynchronousDeletion with {0}")
    @MethodSource("failureIgnoringVisitorConfigurations")
    void testSynchronousFileDeletionDuringTraversal(final Supplier<AccumulatorPathVisitor> visitorSupplier) 
            throws IOException {
        // Given: A known number of temporary files
        final Set<Path> createdFiles = createTempFileSet(SYNC_DELETE_FILE_COUNT);
        final int deletionTriggerPoint = SYNC_DELETE_FILE_COUNT / 2;
        
        // And: A visitor that deletes all files after visiting half
        final AccumulatorPathVisitor visitor = visitorSupplier.get();
        final AtomicInteger visitCount = new AtomicInteger();
        final PathVisitorFileFilter fileFilter = new PathVisitorFileFilter(visitor) {
            @Override
            public FileVisitResult visitFile(final Path path, final BasicFileAttributes attributes) throws IOException {
                if (visitCount.incrementAndGet() == deletionTriggerPoint) {
                    // Delete all files when we reach the halfway point
                    deleteAllFilesImmediately(createdFiles);
                }
                return super.visitFile(path, attributes);
            }
        };
        
        // When: Walking the directory tree
        Files.walkFileTree(tempDirPath, fileFilter);
        
        // Then: Should have visited only files before deletion occurred
        final int expectedFileCount = deletionTriggerPoint - 1;
        assertCounts(1, expectedFileCount, 0, visitor.getPathCounters());
        assertEquals(1, visitor.getDirList().size(), "Should have visited exactly one directory");
        assertEquals(expectedFileCount, visitor.getFileList().size(), 
            "Should have visited files up to deletion point minus one");
    }

    // Helper methods

    private List<Path> createTempFiles(int count) throws IOException {
        final List<Path> files = new ArrayList<>(count);
        for (int i = 1; i <= count; i++) {
            final Path tempFile = Files.createTempFile(tempDirPath, "test", ".txt");
            assertTrue(Files.exists(tempFile), "Created file should exist");
            files.add(tempFile);
        }
        return files;
    }

    private Set<Path> createTempFileSet(int count) throws IOException {
        final Set<Path> files = new LinkedHashSet<>(count);
        for (int i = 1; i <= count; i++) {
            final Path tempFile = Files.createTempFile(tempDirPath, "test", ".txt");
            assertTrue(Files.exists(tempFile), "Created file should exist");
            files.add(tempFile);
        }
        return files;
    }

    private PathVisitorFileFilter createSlowFileVisitor(AccumulatorPathVisitor visitor) {
        return new PathVisitorFileFilter(visitor) {
            @Override
            public FileVisitResult visitFile(final Path path, final BasicFileAttributes attributes) throws IOException {
                // Introduce delay to increase chance of concurrent modification
                try {
                    ThreadUtils.sleep(FILE_VISIT_DELAY);
                } catch (final InterruptedException ignore) {
                    // Ignore interruption during test
                }
                return super.visitFile(path, attributes);
            }
        };
    }

    private void deleteAllFiles(List<Path> files) {
        for (final Path file : files) {
            try {
                Files.delete(file);
            } catch (final IOException ignored) {
                // Ignore deletion failures in concurrent test
            }
        }
    }

    private void deleteAllFilesImmediately(Set<Path> files) throws IOException {
        for (final Path file : files) {
            Files.delete(file);
        }
    }

    private void waitForDeletion(ExecutorService executor, AtomicBoolean deletionComplete) 
            throws InterruptedException {
        if (!deletionComplete.get()) {
            ThreadUtils.sleep(Duration.ofSeconds(1));
        }
        if (!deletionComplete.get()) {
            executor.awaitTermination(DELETION_WAIT_TIMEOUT.getSeconds(), TimeUnit.SECONDS);
        }
        executor.shutdownNow();
    }
}