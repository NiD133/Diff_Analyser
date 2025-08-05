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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.file.Counters.PathCounters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests {@link CleaningPathVisitor}.
 */
@DisplayName("CleaningPathVisitor tests")
class CleaningPathVisitorTest extends TestArguments {

    @TempDir
    private Path tempDir;
    private Path resourceDir;

    @BeforeEach
    void setUp() {
        resourceDir = Paths.get("src/test/resources/org/apache/commons/io/file");
    }

    @Nested
    @DisplayName("Cleaning functionality tests")
    class CleaningFunctionalityTests {

        @DisplayName("should visit an empty directory and delete nothing")
        @ParameterizedTest(name = "with {0}")
        @MethodSource("org.apache.commons.io.file.TestArguments#cleaningPathVisitors")
        void whenCleaningEmptyDirectory_thenVisitsDirectoryButDeletesNothing(final CleaningPathVisitor visitor) throws IOException {
            // Arrange: tempDir is already empty.

            // Act
            Files.walkFileTree(tempDir, visitor);

            // Assert: The visitor should count 1 directory, 0 files, and 0 bytes.
            assertCounts(1, 0, 0, visitor);
        }

        @DisplayName("should handle a null skip array when cleaning an empty directory")
        @ParameterizedTest(name = "with {0}")
        @MethodSource("org.apache.commons.io.file.TestArguments#pathCounters")
        void whenCleaningWithNullSkipList_thenBehavesAsIfEmpty(final PathCounters pathCounters) throws IOException {
            // Arrange
            final CleaningPathVisitor visitor = new CleaningPathVisitor(pathCounters, (String[]) null);

            // Act
            Files.walkFileTree(tempDir, visitor);

            // Assert: A null skip list is treated as empty; counts are for an empty directory.
            assertCounts(1, 0, 0, visitor);
        }

        @DisplayName("should delete a zero-byte file and update counts")
        @ParameterizedTest(name = "with {0}")
        @MethodSource("org.apache.commons.io.file.TestArguments#cleaningPathVisitors")
        void whenDirectoryHasZeroByteFile_thenFileIsDeletedAndCountsAreCorrect(final CleaningPathVisitor visitor) throws IOException {
            // Arrange
            PathUtils.copyDirectory(resourceDir.resolve("dirs-1-file-size-0"), tempDir);

            // Act
            PathUtils.visitFileTree(visitor, tempDir);

            // Assert: Counts 1 directory, 1 file, and 0 bytes.
            assertCounts(1, 1, 0, visitor);
        }

        @DisplayName("should delete a one-byte file and update counts")
        @ParameterizedTest(name = "with {0}")
        @MethodSource("org.apache.commons.io.file.TestArguments#cleaningPathVisitors")
        void whenDirectoryHasOneByteFile_thenFileIsDeletedAndCountsAreCorrect(final CleaningPathVisitor visitor) throws IOException {
            // Arrange
            PathUtils.copyDirectory(resourceDir.resolve("dirs-1-file-size-1"), tempDir);

            // Act
            PathUtils.visitFileTree(visitor, tempDir);

            // Assert: Counts 1 directory, 1 file, and 1 byte.
            assertCounts(1, 1, 1, visitor);
        }

        @DisplayName("should not delete a file that is in the skip list")
        @ParameterizedTest(name = "with {0}")
        @MethodSource("org.apache.commons.io.file.TestArguments#pathCounters")
        void whenFileIsSkipped_thenItIsNotDeleted(final PathCounters pathCounters) throws IOException {
            // Arrange
            PathUtils.copyDirectory(resourceDir.resolve("dirs-1-file-size-1"), tempDir);
            final String fileToSkip = "file-size-1.bin";
            final Path skippedFilePath = tempDir.resolve(fileToSkip);
            final CleaningPathVisitor visitor = new CleaningPathVisitor(pathCounters, fileToSkip);

            // Act
            PathUtils.visitFileTree(visitor, tempDir);

            // Assert
            // The visitor still counts everything it visits.
            assertCounts(1, 1, 1, visitor);
            // Verify the skipped file was not deleted.
            assertTrue(Files.exists(skippedFilePath), "Skipped file should not have been deleted.");

            // Clean up the skipped file manually since the visitor did not delete it.
            Files.delete(skippedFilePath);
        }

        @DisplayName("should delete all files in a complex directory structure")
        @ParameterizedTest(name = "with {0}")
        @MethodSource("org.apache.commons.io.file.TestArguments#cleaningPathVisitors")
        void whenDirectoryHasSubdirectories_thenAllFilesAreDeleted(final CleaningPathVisitor visitor) throws IOException {
            // Arrange
            PathUtils.copyDirectory(resourceDir.resolve("dirs-2-file-size-2"), tempDir);

            // Act
            PathUtils.visitFileTree(visitor, tempDir);

            // Assert: Counts 3 directories, 2 files, and 2 bytes.
            assertCounts(3, 2, 2, visitor);
        }
    }

    @Nested
    @DisplayName("equals() and hashCode() contract tests")
    class EqualsAndHashCodeTest {

        @Test
        @DisplayName("should be equal for two new instances")
        void testEqualsAndHashCodeForNewInstances() {
            final CleaningPathVisitor visitor1 = CleaningPathVisitor.withLongCounters();
            final CleaningPathVisitor visitor2 = CleaningPathVisitor.withLongCounters();

            assertEquals(visitor1, visitor2);
            assertEquals(visitor1.hashCode(), visitor2.hashCode());
        }

        @Test
        @DisplayName("should be equal to itself")
        void testEqualsAndHashCodeForSameInstance() {
            final CleaningPathVisitor visitor = CleaningPathVisitor.withLongCounters();
            assertEquals(visitor, visitor);
            assertEquals(visitor.hashCode(), visitor.hashCode());
        }

        @Test
        @DisplayName("should not be equal after state changes")
        void testEqualsAndHashCodeAfterStateChange() {
            // Arrange
            final CleaningPathVisitor visitor1 = CleaningPathVisitor.withLongCounters();
            final CleaningPathVisitor visitor2 = CleaningPathVisitor.withLongCounters();

            // Act: Modify the state of one visitor
            visitor1.getPathCounters().getByteCounter().increment();

            // Assert
            assertNotEquals(visitor1, visitor2);
            assertNotEquals(visitor1.hashCode(), visitor2.hashCode());
        }
    }
}