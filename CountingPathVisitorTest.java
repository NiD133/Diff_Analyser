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
 * Tests {@link CountingPathVisitor}.
 */
class CountingPathVisitorTest extends TestArguments {

    // Test resource paths
    private static final String DIR_1_FILE_SIZE_0 = 
        "src/test/resources/org/apache/commons/io/dirs-1-file-size-0";
    private static final String DIR_1_FILE_SIZE_1 = 
        "src/test/resources/org/apache/commons/io/dirs-1-file-size-1";
    private static final String DIR_2_FILE_SIZE_2 = 
        "src/test/resources/org/apache/commons/io/dirs-2-file-size-2";

    /**
     * Asserts visitor has zero counts before any traversal occurs.
     * This ensures the visitor starts in a clean state.
     */
    private void assertInitialState(final CountingPathVisitor visitor) {
        Assertions.assertEquals(CountingPathVisitor.withLongCounters(), visitor);
        Assertions.assertEquals(CountingPathVisitor.withBigIntegerCounters(), visitor);
    }

    /**
     * Tests visitor counts for an empty directory structure.
     * Expects: 1 directory (root), 0 files, 0 bytes.
     */
    @ParameterizedTest
    @MethodSource("countingPathVisitors")
    void testEmptyDirectory(final CountingPathVisitor visitor) throws IOException {
        assertInitialState(visitor);
        try (TempDirectory tempDir = TempDirectory.create(getClass().getCanonicalName())) {
            assertCounts(1, 0, 0, PathUtils.visitFileTree(visitor, tempDir.get()));
        }
    }

    /**
     * Tests visitor counts for a directory containing one zero-byte file.
     * Expects: 1 directory, 1 file, 0 bytes.
     */
    @ParameterizedTest
    @MethodSource("countingPathVisitors")
    void testDirectoryWithOneZeroByteFile(final CountingPathVisitor visitor) throws IOException {
        assertInitialState(visitor);
        assertCounts(
            1, /* directories */
            1, /* files */
            0, /* bytes */
            PathUtils.visitFileTree(visitor, DIR_1_FILE_SIZE_0)
        );
    }

    /**
     * Tests visitor counts for a directory containing one single-byte file.
     * Expects: 1 directory, 1 file, 1 byte.
     */
    @ParameterizedTest
    @MethodSource("countingPathVisitors")
    void testDirectoryWithOneSingleByteFile(final CountingPathVisitor visitor) throws IOException {
        assertInitialState(visitor);
        assertCounts(
            1, /* directories */
            1, /* files */
            1, /* bytes */
            PathUtils.visitFileTree(visitor, DIR_1_FILE_SIZE_1)
        );
    }

    /**
     * Tests visitor counts for a nested directory structure:
     * - Root directory
     *   - Subdirectory 1
     *     - File (1 byte)
     *   - Subdirectory 2
     *     - File (1 byte)
     * Expects: 3 directories, 2 files, 2 bytes.
     */
    @ParameterizedTest
    @MethodSource("countingPathVisitors")
    void testNestedDirectoriesWithTwoFiles(final CountingPathVisitor visitor) throws IOException {
        assertInitialState(visitor);
        assertCounts(
            3, /* directories (root + 2 subdirectories) */
            2, /* files */
            2, /* bytes (1+1) */
            PathUtils.visitFileTree(visitor, DIR_2_FILE_SIZE_2)
        );
    }

    /**
     * Tests that toString() method doesn't throw exceptions.
     * This ensures basic debugging functionality works.
     */
    @ParameterizedTest
    @MethodSource("countingPathVisitors")
    void testToStringDoesNotThrow(final CountingPathVisitor visitor) {
        // toString should safely return string representation
        Assertions.assertDoesNotThrow(visitor::toString);
    }
}