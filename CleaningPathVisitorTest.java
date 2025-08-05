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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.file.Counters.PathCounters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests {@link CleaningPathVisitor} - a visitor that deletes files but not directories.
 */
class CleaningPathVisitorTest extends TestArguments {

    // Test resource paths for different directory structures
    private static final String TEST_RESOURCES_BASE = "src/test/resources/org/apache/commons/io/";
    private static final String EMPTY_DIR_RESOURCE = TEST_RESOURCES_BASE + "dirs-1-file-size-0";
    private static final String SINGLE_FILE_SIZE_0_RESOURCE = TEST_RESOURCES_BASE + "dirs-1-file-size-0";
    private static final String SINGLE_FILE_SIZE_1_RESOURCE = TEST_RESOURCES_BASE + "dirs-1-file-size-1";
    private static final String TWO_FILES_SIZE_2_RESOURCE = TEST_RESOURCES_BASE + "dirs-2-file-size-2";
    
    // Expected file name in test resources
    private static final String TEST_FILE_NAME = "file-size-1.bin";

    @TempDir
    private Path tempDir;

    // ========== Empty Directory Tests ==========

    @ParameterizedTest
    @MethodSource("cleaningPathVisitors")
    void shouldCleanEmptyDirectory_WhenDirectoryIsEmpty(final CleaningPathVisitor visitor) throws IOException {
        // Given: An empty directory
        // When: Cleaning the directory
        CleaningPathVisitor result = cleanDirectoryAndGetResult(visitor);
        
        // Then: Should visit 1 directory, 0 files, 0 bytes
        assertDirectoryCleaningCounts(result, 1, 0, 0);
    }

    @ParameterizedTest
    @MethodSource("pathCounters")
    void shouldCleanEmptyDirectory_WhenConstructedWithNullSkipArray(final PathCounters pathCounters) throws IOException {
        // Given: A visitor with null skip array
        CleaningPathVisitor visitor = new CleaningPathVisitor(pathCounters, (String[]) null);
        
        // When: Cleaning empty directory
        CleaningPathVisitor result = cleanDirectoryAndGetResult(visitor);
        
        // Then: Should visit 1 directory, 0 files, 0 bytes
        assertDirectoryCleaningCounts(result, 1, 0, 0);
    }

    // ========== Single File Tests ==========

    @ParameterizedTest
    @MethodSource("cleaningPathVisitors")
    void shouldCleanDirectoryWithZeroByteFile_WhenDirectoryContainsSingleEmptyFile(final CleaningPathVisitor visitor) throws IOException {
        // Given: A directory with one empty file (0 bytes)
        copyTestResourceToTempDir(SINGLE_FILE_SIZE_0_RESOURCE);
        
        // When: Cleaning the directory
        CleaningPathVisitor result = cleanDirectoryAndGetResult(visitor);
        
        // Then: Should visit 1 directory, 1 file, 0 bytes
        assertDirectoryCleaningCounts(result, 1, 1, 0);
        assertVisitorIdentityAndEquality(visitor, result);
    }

    @ParameterizedTest
    @MethodSource("cleaningPathVisitors")
    void shouldCleanDirectoryWithSingleByteFile_WhenDirectoryContainsOneByteFile(final CleaningPathVisitor visitor) throws IOException {
        // Given: A directory with one file containing 1 byte
        copyTestResourceToTempDir(SINGLE_FILE_SIZE_1_RESOURCE);
        
        // When: Cleaning the directory
        CleaningPathVisitor result = cleanDirectoryAndGetResult(visitor);
        
        // Then: Should visit 1 directory, 1 file, 1 byte
        assertDirectoryCleaningCounts(result, 1, 1, 1);
        assertVisitorIdentityAndEquality(visitor, result);
    }

    @ParameterizedTest
    @MethodSource("pathCounters")
    void shouldSkipSpecifiedFile_WhenFileIsInSkipList(final PathCounters pathCounters) throws IOException {
        // Given: A directory with one file and a visitor configured to skip that file
        copyTestResourceToTempDir(SINGLE_FILE_SIZE_1_RESOURCE);
        CleaningPathVisitor visitor = new CleaningPathVisitor(pathCounters, TEST_FILE_NAME);
        
        // When: Cleaning the directory
        CleaningPathVisitor result = cleanDirectoryAndGetResult(visitor);
        
        // Then: Should count the file but not delete it
        assertDirectoryCleaningCounts(result, 1, 1, 1);
        assertSame(visitor, result);
        
        // And: The skipped file should still exist
        Path skippedFile = tempDir.resolve(TEST_FILE_NAME);
        assertTrue(Files.exists(skippedFile), "Skipped file should still exist");
        
        // Cleanup: Remove the file for test isolation
        Files.delete(skippedFile);
        
        assertVisitorIdentityAndEquality(visitor, result);
    }

    // ========== Multiple Files Tests ==========

    @ParameterizedTest
    @MethodSource("cleaningPathVisitors")
    void shouldCleanDirectoryWithMultipleFiles_WhenDirectoryContainsTwoSubdirectoriesWithFiles(final CleaningPathVisitor visitor) throws IOException {
        // Given: A directory structure with 2 subdirectories, each containing 1 file of 1 byte
        copyTestResourceToTempDir(TWO_FILES_SIZE_2_RESOURCE);
        
        // When: Cleaning the directory
        CleaningPathVisitor result = cleanDirectoryAndGetResult(visitor);
        
        // Then: Should visit 3 directories (root + 2 subdirs), 2 files, 2 bytes total
        assertDirectoryCleaningCounts(result, 3, 2, 2);
        assertVisitorIdentityAndEquality(visitor, result);
    }

    // ========== Equals and HashCode Tests ==========

    @Test
    void shouldImplementEqualsAndHashCodeCorrectly_WhenComparingVisitorInstances() {
        // Given: Two fresh visitor instances
        CountingPathVisitor visitor1 = CleaningPathVisitor.withLongCounters();
        CountingPathVisitor visitor2 = CleaningPathVisitor.withLongCounters();
        
        // Then: Should be equal initially
        assertVisitorsAreEqual(visitor1, visitor2);
        
        // When: One visitor's state changes
        visitor1.getPathCounters().getByteCounter().increment();
        
        // Then: Should no longer be equal
        assertVisitorsAreNotEqual(visitor1, visitor2);
        
        // But: Each visitor should still equal itself
        assertEquals(visitor1, visitor1);
        assertEquals(visitor1.hashCode(), visitor1.hashCode());
    }

    // ========== Helper Methods ==========

    private CleaningPathVisitor cleanDirectoryAndGetResult(CleaningPathVisitor visitor) throws IOException {
        return PathUtils.visitFileTree(visitor, tempDir);
    }

    private void copyTestResourceToTempDir(String resourcePath) throws IOException {
        PathUtils.copyDirectory(Paths.get(resourcePath), tempDir);
    }

    private void assertDirectoryCleaningCounts(CleaningPathVisitor visitor, 
                                             int expectedDirectories, 
                                             int expectedFiles, 
                                             int expectedBytes) {
        assertCounts(expectedDirectories, expectedFiles, expectedBytes, visitor);
    }

    private void assertVisitorIdentityAndEquality(CleaningPathVisitor original, CleaningPathVisitor result) {
        assertSame(original, result, "Visitor should return the same instance");
        
        // Test inequality with a fresh instance
        CleaningPathVisitor freshVisitor = CleaningPathVisitor.withLongCounters();
        assertNotEquals(result, freshVisitor);
        assertNotEquals(result.hashCode(), freshVisitor.hashCode());
        
        // Test self-equality
        assertEquals(result, result);
        assertEquals(result.hashCode(), result.hashCode());
    }

    private void assertVisitorsAreEqual(CountingPathVisitor visitor1, CountingPathVisitor visitor2) {
        assertEquals(visitor1, visitor1, "Visitor should equal itself");
        assertEquals(visitor1, visitor2, "Fresh visitors should be equal");
        assertEquals(visitor2, visitor1, "Equality should be symmetric");
        assertEquals(visitor1.hashCode(), visitor1.hashCode(), "HashCode should be consistent");
        assertEquals(visitor1.hashCode(), visitor2.hashCode(), "Equal objects should have equal hashCodes");
        assertEquals(visitor2.hashCode(), visitor1.hashCode(), "HashCode equality should be symmetric");
    }

    private void assertVisitorsAreNotEqual(CountingPathVisitor visitor1, CountingPathVisitor visitor2) {
        assertEquals(visitor1, visitor1, "Visitor should still equal itself");
        assertNotEquals(visitor1, visitor2, "Modified visitor should not equal unmodified visitor");
        assertNotEquals(visitor2, visitor1, "Inequality should be symmetric");
        assertEquals(visitor1.hashCode(), visitor1.hashCode(), "HashCode should still be consistent");
        assertNotEquals(visitor1.hashCode(), visitor2.hashCode(), "Unequal objects should have different hashCodes");
        assertNotEquals(visitor2.hashCode(), visitor1.hashCode(), "HashCode inequality should be symmetric");
    }
}