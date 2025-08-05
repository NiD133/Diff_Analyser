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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests {@link CountingPathVisitor}.
 */
class CountingPathVisitorTest extends TestArguments {

    @Test
    @DisplayName("A new visitor should be initialized with zero counts")
    void testNewVisitorIsInitializedWithZeroCounts() {
        // Arrange
        final CountingPathVisitor longVisitor = CountingPathVisitor.withLongCounters();
        final CountingPathVisitor bigIntVisitor = CountingPathVisitor.withBigIntegerCounters();

        // Assert that new visitors have zero counts
        assertCounts(0, 0, 0, longVisitor);
        assertCounts(0, 0, 0, bigIntVisitor);

        // Assert that new visitors of different counter types are considered equal
        Assertions.assertEquals(longVisitor, bigIntVisitor);
        Assertions.assertEquals(bigIntVisitor, longVisitor);
    }

    @DisplayName("Visiting an empty directory should count 1 directory, 0 files, and 0 bytes")
    @ParameterizedTest
    @MethodSource("countingPathVisitors")
    void testVisitEmptyDirectory(final CountingPathVisitor visitor) throws IOException {
        // Arrange
        try (TempDirectory tempDir = TempDirectory.create(getClass().getCanonicalName())) {
            final long expectedDirectoryCount = 1;
            final long expectedFileCount = 0;
            final long expectedByteCount = 0;

            // Act
            PathUtils.visitFileTree(visitor, tempDir.get());

            // Assert
            assertCounts(expectedDirectoryCount, expectedFileCount, expectedByteCount, visitor);
        }
    }

    @DisplayName("Visiting a directory with one empty file should count 1 directory, 1 file, and 0 bytes")
    @ParameterizedTest
    @MethodSource("countingPathVisitors")
    void testVisitDirectoryWithOneEmptyFile(final CountingPathVisitor visitor) throws IOException {
        // Arrange
        final String path = "src/test/resources/org/apache/commons/io/dirs-1-file-size-0";
        final long expectedDirectoryCount = 1;
        final long expectedFileCount = 1;
        final long expectedByteCount = 0;

        // Act
        PathUtils.visitFileTree(visitor, path);

        // Assert
        assertCounts(expectedDirectoryCount, expectedFileCount, expectedByteCount, visitor);
    }

    @DisplayName("Visiting a directory with one non-empty file should count 1 directory, 1 file, and 1 byte")
    @ParameterizedTest
    @MethodSource("countingPathVisitors")
    void testVisitDirectoryWithOneNonEmptyFile(final CountingPathVisitor visitor) throws IOException {
        // Arrange
        final String path = "src/test/resources/org/apache/commons/io/dirs-1-file-size-1";
        final long expectedDirectoryCount = 1;
        final long expectedFileCount = 1;
        final long expectedByteCount = 1;

        // Act
        PathUtils.visitFileTree(visitor, path);

        // Assert
        assertCounts(expectedDirectoryCount, expectedFileCount, expectedByteCount, visitor);
    }

    @DisplayName("Visiting a directory with subdirectories and files should count all items correctly")
    @ParameterizedTest
    @MethodSource("countingPathVisitors")
    void testVisitDirectoryWithSubdirectoriesAndFiles(final CountingPathVisitor visitor) throws IOException {
        // Arrange
        // The test directory "dirs-2-file-size-2" contains:
        // - 1 root directory
        // - 2 subdirectories
        // - 2 files, each 1 byte long.
        // Total: 3 directories, 2 files, 2 bytes.
        final String path = "src/test/resources/org/apache/commons/io/dirs-2-file-size-2";
        final long expectedDirectoryCount = 3;
        final long expectedFileCount = 2;
        final long expectedByteCount = 2;

        // Act
        PathUtils.visitFileTree(visitor, path);

        // Assert
        assertCounts(expectedDirectoryCount, expectedFileCount, expectedByteCount, visitor);
    }

    @DisplayName("The toString() method should execute without throwing an exception")
    @ParameterizedTest
    @MethodSource("countingPathVisitors")
    void testToStringDoesNotThrowException(final CountingPathVisitor visitor) {
        // Act & Assert
        Assertions.assertDoesNotThrow(visitor::toString, "toString() should not throw.");
        Assertions.assertNotNull(visitor.toString(), "toString() should return a non-null string.");
    }
}