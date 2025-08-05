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

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests {@link CountingPathVisitor} functionality for counting directories, files, and total bytes
 * when traversing file system hierarchies.
 */
class CountingPathVisitorTest extends TestArguments {

    // Test data directory paths
    private static final String TEST_RESOURCES_BASE = "src/test/resources/org/apache/commons/io/";
    private static final String SINGLE_EMPTY_FILE_DIR = TEST_RESOURCES_BASE + "dirs-1-file-size-0";
    private static final String SINGLE_ONE_BYTE_FILE_DIR = TEST_RESOURCES_BASE + "dirs-1-file-size-1";
    private static final String TWO_DIRS_TWO_FILES_DIR = TEST_RESOURCES_BASE + "dirs-2-file-size-2";

    /**
     * Verifies that a fresh visitor has zero counts and equals other fresh visitors.
     * This ensures proper initialization and equality implementation.
     */
    private void assertVisitorHasZeroCounts(final CountingPathVisitor visitor) {
        Assertions.assertEquals(CountingPathVisitor.withLongCounters(), visitor,
            "Fresh visitor should equal a new long counter visitor");
        Assertions.assertEquals(CountingPathVisitor.withBigIntegerCounters(), visitor,
            "Fresh visitor should equal a new BigInteger counter visitor");
    }

    /**
     * Tests counting an empty directory.
     * Expected: 1 directory, 0 files, 0 bytes total
     */
    @ParameterizedTest
    @MethodSource("countingPathVisitors")
    void testCountEmptyFolder(final CountingPathVisitor visitor) throws IOException {
        // Given: A fresh visitor with zero counts
        assertVisitorHasZeroCounts(visitor);
        
        // When: Visiting an empty temporary directory
        try (TempDirectory tempDir = TempDirectory.create(getClass().getCanonicalName())) {
            CountingPathVisitor result = PathUtils.visitFileTree(visitor, tempDir.get());
            
            // Then: Should count 1 directory, 0 files, 0 bytes
            int expectedDirectories = 1;
            int expectedFiles = 0;
            long expectedBytes = 0;
            assertCounts(expectedDirectories, expectedFiles, expectedBytes, result);
        }
    }

    /**
     * Tests counting a directory containing one empty file (0 bytes).
     * Expected: 1 directory, 1 file, 0 bytes total
     */
    @ParameterizedTest
    @MethodSource("countingPathVisitors")
    void testCountFolders1FileSize0(final CountingPathVisitor visitor) throws IOException {
        // Given: A fresh visitor with zero counts
        assertVisitorHasZeroCounts(visitor);
        
        // When: Visiting a directory with one empty file
        CountingPathVisitor result = PathUtils.visitFileTree(visitor, SINGLE_EMPTY_FILE_DIR);
        
        // Then: Should count 1 directory, 1 file, 0 bytes
        int expectedDirectories = 1;
        int expectedFiles = 1;
        long expectedBytes = 0;
        assertCounts(expectedDirectories, expectedFiles, expectedBytes, result);
    }

    /**
     * Tests counting a directory containing one file with 1 byte.
     * Expected: 1 directory, 1 file, 1 byte total
     */
    @ParameterizedTest
    @MethodSource("countingPathVisitors")
    void testCountFolders1FileSize1(final CountingPathVisitor visitor) throws IOException {
        // Given: A fresh visitor with zero counts
        assertVisitorHasZeroCounts(visitor);
        
        // When: Visiting a directory with one 1-byte file
        CountingPathVisitor result = PathUtils.visitFileTree(visitor, SINGLE_ONE_BYTE_FILE_DIR);
        
        // Then: Should count 1 directory, 1 file, 1 byte
        int expectedDirectories = 1;
        int expectedFiles = 1;
        long expectedBytes = 1;
        assertCounts(expectedDirectories, expectedFiles, expectedBytes, result);
    }

    /**
     * Tests counting a directory structure with two subdirectories, each containing one 1-byte file.
     * Structure: root/ -> subdir1/file1(1 byte), subdir2/file2(1 byte)
     * Expected: 3 directories (root + 2 subdirs), 2 files, 2 bytes total
     */
    @ParameterizedTest
    @MethodSource("countingPathVisitors")
    void testCountFolders2FileSize2(final CountingPathVisitor visitor) throws IOException {
        // Given: A fresh visitor with zero counts
        assertVisitorHasZeroCounts(visitor);
        
        // When: Visiting a directory tree with 2 subdirectories and 2 files
        CountingPathVisitor result = PathUtils.visitFileTree(visitor, TWO_DIRS_TWO_FILES_DIR);
        
        // Then: Should count 3 directories (root + 2 subdirs), 2 files, 2 bytes
        int expectedDirectories = 3;
        int expectedFiles = 2;
        long expectedBytes = 2;
        assertCounts(expectedDirectories, expectedFiles, expectedBytes, result);
    }

    /**
     * Tests that the toString() method doesn't throw exceptions.
     * This is a defensive test to ensure string representation is always available for debugging.
     */
    @ParameterizedTest
    @MethodSource("countingPathVisitors")
    void testToStringDoesNotThrowException(final CountingPathVisitor visitor) {
        // When: Calling toString() on a visitor
        // Then: Should not throw any exception
        Assertions.assertDoesNotThrow(() -> {
            String result = visitor.toString();
            Assertions.assertNotNull(result, "toString() should never return null");
        }, "toString() method should never throw an exception");
    }
}