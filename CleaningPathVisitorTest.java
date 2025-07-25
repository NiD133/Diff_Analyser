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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.file.Counters.PathCounters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests {@link CleaningPathVisitor}.
 *
 * <p>
 * The general pattern for these tests is:
 * 1. Copy a directory structure to a temporary directory.
 * 2. Create a {@link CleaningPathVisitor} with specific configuration.
 * 3. Use {@link PathUtils#visitFileTree(PathVisitor, Path)} to apply the visitor.
 * 4. Assert that the counters in the visitor have the expected values.
 * 5. Verify specific files are deleted or not deleted based on the visitor's configuration.
 * </p>
 */
class CleaningPathVisitorTest extends TestArguments {

    @TempDir
    private Path tempDir;

    /**
     * Applies the CleaningPathVisitor to the tempDir and asserts the expected counts for an empty directory.
     *
     * @param visitor The CleaningPathVisitor to apply.
     * @throws IOException If an I/O error occurs.
     */
    private void applyCleanEmptyDirectory(final CleaningPathVisitor visitor) throws IOException {
        Files.walkFileTree(tempDir, visitor);
        assertCounts(1, 0, 0, visitor); // Expect 1 directory, 0 files, 0 bytes.
    }

    /**
     * Tests that the CleaningPathVisitor correctly processes an empty directory.
     * It verifies that the visitor counts one directory and no files or bytes.
     *
     * @param visitor The CleaningPathVisitor to use, provided by {@link #cleaningPathVisitors()}.
     * @throws IOException If an I/O error occurs.
     */
    @ParameterizedTest
    @MethodSource("cleaningPathVisitors")
    void testCleanEmptyDirectory(final CleaningPathVisitor visitor) throws IOException {
        applyCleanEmptyDirectory(visitor);
    }

    /**
     * Tests that the CleaningPathVisitor correctly processes an empty directory when constructed with a null 'skip' argument.
     * It verifies that the visitor counts one directory and no files or bytes.
     *
     * @param pathCounters The PathCounters to use, provided by {@link #pathCounters()}.
     * @throws IOException If an I/O error occurs.
     */
    @ParameterizedTest
    @MethodSource("pathCounters")
    void testCleanEmptyDirectoryNullCtorArg(final PathCounters pathCounters) throws IOException {
        applyCleanEmptyDirectory(new CleaningPathVisitor(pathCounters, (String[]) null));
    }

    /**
     * Tests that the CleaningPathVisitor correctly processes a directory containing one file of size 0.
     * It verifies that the visitor counts one directory, one file, and zero bytes.
     *
     * @param visitor The CleaningPathVisitor to use, provided by {@link #cleaningPathVisitors()}.
     * @throws IOException If an I/O error occurs.
     */
    @ParameterizedTest
    @MethodSource("cleaningPathVisitors")
    void testCleanFolders1FileSize0(final CleaningPathVisitor visitor) throws IOException {
        // Copy a directory with 1 file of size 0 to the temp directory.
        PathUtils.copyDirectory(Paths.get("src/test/resources/org/apache/commons/io/dirs-1-file-size-0"), tempDir);

        // Apply the visitor and retrieve the same instance to allow for method chaining in the tested class
        final CleaningPathVisitor visitFileTree = PathUtils.visitFileTree(visitor, tempDir);
        assertCounts(1, 1, 0, visitFileTree); // Expect 1 directory, 1 file, 0 bytes.
        assertSame(visitor, visitFileTree);

        // Verify equals and hashCode methods work as expected.
        assertNotEquals(visitFileTree, CleaningPathVisitor.withLongCounters());
        assertNotEquals(visitFileTree.hashCode(), CleaningPathVisitor.withLongCounters().hashCode());
        assertEquals(visitFileTree, visitFileTree);
        assertEquals(visitFileTree.hashCode(), visitFileTree.hashCode());
    }

    /**
     * Tests that the CleaningPathVisitor correctly processes a directory containing one file of size 1.
     * It verifies that the visitor counts one directory, one file, and one byte.
     *
     * @param visitor The CleaningPathVisitor to use, provided by {@link #cleaningPathVisitors()}.
     * @throws IOException If an I/O error occurs.
     */
    @ParameterizedTest
    @MethodSource("cleaningPathVisitors")
    void testCleanFolders1FileSize1(final CleaningPathVisitor visitor) throws IOException {
        // Copy a directory with 1 file of size 1 to the temp directory.
        PathUtils.copyDirectory(Paths.get("src/test/resources/org/apache/commons/io/dirs-1-file-size-1"), tempDir);

        // Apply the visitor and retrieve the same instance to allow for method chaining in the tested class
        final CleaningPathVisitor visitFileTree = PathUtils.visitFileTree(visitor, tempDir);
        assertCounts(1, 1, 1, visitFileTree); // Expect 1 directory, 1 file, 1 byte.
        assertSame(visitor, visitFileTree);

        // Verify equals and hashCode methods work as expected.
        assertNotEquals(visitFileTree, CleaningPathVisitor.withLongCounters());
        assertNotEquals(visitFileTree.hashCode(), CleaningPathVisitor.withLongCounters().hashCode());
        assertEquals(visitFileTree, visitFileTree);
        assertEquals(visitFileTree.hashCode(), visitFileTree.hashCode());
    }

    /**
     * Tests that the CleaningPathVisitor correctly skips a file when configured to do so.
     * It verifies that the visitor counts one directory, one file, and one byte, and that the skipped file still exists after visiting.
     *
     * @param pathCounters The PathCounters to use, provided by {@link #pathCounters()}.
     * @throws IOException If an I/O error occurs.
     */
    @ParameterizedTest
    @MethodSource("pathCounters")
    void testCleanFolders1FileSize1Skip(final PathCounters pathCounters) throws IOException {
        // Copy a directory with 1 file of size 1 to the temp directory.
        PathUtils.copyDirectory(Paths.get("src/test/resources/org/apache/commons/io/dirs-1-file-size-1"), tempDir);

        final String skipFileName = "file-size-1.bin";
        final CountingPathVisitor visitor = new CleaningPathVisitor(pathCounters, skipFileName);

        // Apply the visitor and retrieve the same instance to allow for method chaining in the tested class
        final CountingPathVisitor visitFileTree = PathUtils.visitFileTree(visitor, tempDir);
        assertCounts(1, 1, 1, visitFileTree); // Expect 1 directory, 1 file, 1 byte.
        assertSame(visitor, visitFileTree);

        // Assert that the skipped file still exists.
        final Path skippedFile = tempDir.resolve(skipFileName);
        Assertions.assertTrue(Files.exists(skippedFile));

        // Clean up: delete the skipped file.
        Files.delete(skippedFile);

        // Verify equals and hashCode methods work as expected.
        assertNotEquals(visitFileTree, CleaningPathVisitor.withLongCounters());
        assertNotEquals(visitFileTree.hashCode(), CleaningPathVisitor.withLongCounters().hashCode());
        assertEquals(visitFileTree, visitFileTree);
        assertEquals(visitFileTree.hashCode(), visitFileTree.hashCode());
    }

    /**
     * Tests that the CleaningPathVisitor correctly processes a directory with two subdirectories, each containing one file of size 1.
     * It verifies that the visitor counts three directories, two files, and two bytes.
     *
     * @param visitor The CleaningPathVisitor to use, provided by {@link #cleaningPathVisitors()}.
     * @throws IOException If an I/O error occurs.
     */
    @ParameterizedTest
    @MethodSource("cleaningPathVisitors")
    void testCleanFolders2FileSize2(final CleaningPathVisitor visitor) throws IOException {
        // Copy a directory with 2 subdirectories, each containing 1 file of size 1, to the temp directory.
        PathUtils.copyDirectory(Paths.get("src/test/resources/org/apache/commons/io/dirs-2-file-size-2"), tempDir);

        // Apply the visitor and retrieve the same instance to allow for method chaining in the tested class
        final CleaningPathVisitor visitFileTree = PathUtils.visitFileTree(visitor, tempDir);
        assertCounts(3, 2, 2, visitFileTree); // Expect 3 directories, 2 files, 2 bytes.
        assertSame(visitor, visitFileTree);

        // Verify equals and hashCode methods work as expected.
        assertNotEquals(visitFileTree, CleaningPathVisitor.withLongCounters());
        assertNotEquals(visitFileTree.hashCode(), CleaningPathVisitor.withLongCounters().hashCode());
        assertEquals(visitFileTree, visitFileTree);
        assertEquals(visitFileTree.hashCode(), visitFileTree.hashCode());
    }

    /**
     * Tests the equals and hashCode methods of the CleaningPathVisitor.
     */
    @Test
    void testEqualsHashCode() {
        final CountingPathVisitor visitor0 = CleaningPathVisitor.withLongCounters();
        final CountingPathVisitor visitor1 = CleaningPathVisitor.withLongCounters();

        // Test equality with itself and another instance.
        assertEquals(visitor0, visitor0);
        assertEquals(visitor0, visitor1);
        assertEquals(visitor1, visitor0);

        // Test hashCode with itself and another instance.
        assertEquals(visitor0.hashCode(), visitor0.hashCode());
        assertEquals(visitor0.hashCode(), visitor1.hashCode());
        assertEquals(visitor1.hashCode(), visitor0.hashCode());

        // Modify one visitor and test equality and hashCode again.
        visitor0.getPathCounters().getByteCounter().increment();
        assertEquals(visitor0, visitor0); // Still equals itself
        assertNotEquals(visitor0, visitor1); // Not equals another different instance
        assertNotEquals(visitor1, visitor0); // Not equals another different instance

        assertEquals(visitor0.hashCode(), visitor0.hashCode()); // Hash code must be the same
        assertNotEquals(visitor0.hashCode(), visitor1.hashCode()); // Hash code must be different
        assertNotEquals(visitor1.hashCode(), visitor0.hashCode()); // Hash code must be different
    }
}