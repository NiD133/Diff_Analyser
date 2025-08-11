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
 * Tests {@link DeletingPathVisitor}.
 */
class CleaningPathVisitorTest extends TestArguments {

    @TempDir
    private Path tempDir;

    /**
     * Helper method to test cleaning an empty directory.
     * @param visitor CleaningPathVisitor instance to test
     */
    private void applyCleanEmptyDirectory(final CleaningPathVisitor visitor) throws IOException {
        Files.walkFileTree(tempDir, visitor);
        // Verify only directory count (1) with no files or bytes
        assertCounts(1, 0, 0, visitor);
    }

    /**
     * Tests cleaning an empty directory using parameterized visitors.
     * @param visitor CleaningPathVisitor instance from method source
     */
    @ParameterizedTest
    @MethodSource("cleaningPathVisitors")
    void testCleanEmptyDirectory(final CleaningPathVisitor visitor) throws IOException {
        applyCleanEmptyDirectory(visitor);
    }

    /**
     * Tests cleaning an empty directory with null skip parameter in constructor.
     * @param pathCounters PathCounters implementation from method source
     */
    @ParameterizedTest
    @MethodSource("pathCounters")
    void testCleanEmptyDirectoryWithNullSkipParameter(final PathCounters pathCounters) throws IOException {
        applyCleanEmptyDirectory(new CleaningPathVisitor(pathCounters, (String[]) null));
    }

    /**
     * Tests cleaning a directory containing one empty file (size 0).
     * @param visitor CleaningPathVisitor instance from method source
     */
    @ParameterizedTest
    @MethodSource("cleaningPathVisitors")
    void testCleanDirectoryWithOneEmptyFile(final CleaningPathVisitor visitor) throws IOException {
        // Setup: Copy test directory structure
        PathUtils.copyDirectory(Paths.get("src/test/resources/org/apache/commons/io/dirs-1-file-size-0"), tempDir);
        
        final CleaningPathVisitor resultVisitor = PathUtils.visitFileTree(visitor, tempDir);
        
        // Verify: 1 directory, 1 file, 0 bytes
        assertCounts(1, 1, 0, resultVisitor);
        // Confirm we got the same visitor instance back
        assertSame(visitor, resultVisitor);
    }

    /**
     * Tests cleaning a directory containing one file of size 1 byte.
     * @param visitor CleaningPathVisitor instance from method source
     */
    @ParameterizedTest
    @MethodSource("cleaningPathVisitors")
    void testCleanDirectoryWithOneByteFile(final CleaningPathVisitor visitor) throws IOException {
        PathUtils.copyDirectory(Paths.get("src/test/resources/org/apache/commons/io/dirs-1-file-size-1"), tempDir);
        
        final CleaningPathVisitor resultVisitor = PathUtils.visitFileTree(visitor, tempDir);
        
        // Verify: 1 directory, 1 file, 1 byte
        assertCounts(1, 1, 1, resultVisitor);
        assertSame(visitor, resultVisitor);
    }

    /**
     * Tests that specified files are skipped during cleaning.
     * @param pathCounters PathCounters implementation from method source
     */
    @ParameterizedTest
    @MethodSource("pathCounters")
    void testSkipFileDuringCleaning(final PathCounters pathCounters) throws IOException {
        // Setup test directory and file to skip
        PathUtils.copyDirectory(Paths.get("src/test/resources/org/apache/commons/io/dirs-1-file-size-1"), tempDir);
        final String skipFileName = "file-size-1.bin";
        
        // Create visitor that skips specified file
        final CleaningPathVisitor visitor = new CleaningPathVisitor(pathCounters, skipFileName);
        final CleaningPathVisitor resultVisitor = PathUtils.visitFileTree(visitor, tempDir);
        
        // Verify counts include the skipped file
        assertCounts(1, 1, 1, resultVisitor);
        assertSame(visitor, resultVisitor);
        
        // Confirm skipped file still exists
        final Path skippedFile = tempDir.resolve(skipFileName);
        assertTrue(Files.exists(skippedFile));
        
        // Cleanup skipped file
        Files.delete(skippedFile);
    }

    /**
     * Tests cleaning a directory with nested structure (3 directories, 2 files).
     * @param visitor CleaningPathVisitor instance from method source
     */
    @ParameterizedTest
    @MethodSource("cleaningPathVisitors")
    void testCleanNestedDirectoryStructure(final CleaningPathVisitor visitor) throws IOException {
        PathUtils.copyDirectory(Paths.get("src/test/resources/org/apache/commons/io/dirs-2-file-size-2"), tempDir);
        
        final CleaningPathVisitor resultVisitor = PathUtils.visitFileTree(visitor, tempDir);
        
        // Verify: 3 directories (root + 2 subdirs), 2 files, 2 bytes
        assertCounts(3, 2, 2, resultVisitor);
        assertSame(visitor, resultVisitor);
    }

    /**
     * Tests equals() and hashCode() contract for CleaningPathVisitor.
     */
    @Test
    void testEqualsHashCode() {
        final CleaningPathVisitor visitor0 = CleaningPathVisitor.withLongCounters();
        final CleaningPathVisitor visitor1 = CleaningPathVisitor.withLongCounters();
        
        // Equality checks
        assertEquals(visitor0, visitor0, "Instance should equal itself");
        assertEquals(visitor0, visitor1, "Identical instances should be equal");
        assertEquals(visitor0.hashCode(), visitor1.hashCode(), "Hash codes should match for equal instances");
        
        // Modify state and check inequality
        visitor0.getPathCounters().getByteCounter().increment();
        assertNotEquals(visitor0, visitor1, "Modified instance should not equal original");
        assertNotEquals(visitor0.hashCode(), visitor1.hashCode(), "Hash codes should differ after modification");
    }
}