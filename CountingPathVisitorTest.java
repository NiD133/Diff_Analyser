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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Path;

import org.apache.commons.io.file.Counters.PathCounters;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests {@link CountingPathVisitor}.
 *
 * Notes:
 * - Directory counts include the starting directory.
 * - Tests are parameterized over multiple CountingPathVisitor configurations provided by TestArguments#countingPathVisitors.
 */
class CountingPathVisitorTest extends TestArguments {

    private static final String RES_BASE = "src/test/resources/org/apache/commons/io";
    private static final String DIRS_1_FILE_SIZE_0 = RES_BASE + "/dirs-1-file-size-0";
    private static final String DIRS_1_FILE_SIZE_1 = RES_BASE + "/dirs-1-file-size-1";
    private static final String DIRS_2_FILE_SIZE_2 = RES_BASE + "/dirs-2-file-size-2";

    /**
     * Verifies that a freshly provided visitor has zero counts and equals the default
     * visitor instances. CountingPathVisitor equality is value-based on counts and filters,
     * so different counter implementations with the same counts are equal.
     */
    private void assertVisitorInitiallyHasZeroCounts(final CountingPathVisitor visitor) {
        assertEquals(CountingPathVisitor.withLongCounters(), visitor, "Expected zero counts (long counters).");
        assertEquals(CountingPathVisitor.withBigIntegerCounters(), visitor, "Expected zero counts (BigInteger counters).");
    }

    /**
     * Visits the given path and asserts directory, file, and byte counts.
     */
    private void assertVisitCounts(final CountingPathVisitor visitor, final Path path, final long expectedDirs,
            final long expectedFiles, final long expectedBytes) throws IOException {
        final PathCounters counters = PathUtils.visitFileTree(visitor, path);
        assertCounts(expectedDirs, expectedFiles, expectedBytes, counters);
    }

    /**
     * Visits the given path (string) and asserts directory, file, and byte counts.
     */
    private void assertVisitCounts(final CountingPathVisitor visitor, final String pathString, final long expectedDirs,
            final long expectedFiles, final long expectedBytes) throws IOException {
        final PathCounters counters = PathUtils.visitFileTree(visitor, pathString);
        assertCounts(expectedDirs, expectedFiles, expectedBytes, counters);
    }

    /**
     * Tests an empty folder.
     */
    @ParameterizedTest
    @MethodSource("countingPathVisitors")
    void testCountEmptyFolder(final CountingPathVisitor visitor) throws IOException {
        assertVisitorInitiallyHasZeroCounts(visitor);

        try (TempDirectory tempDir = TempDirectory.create(getClass().getCanonicalName())) {
            // Arrange: tempDir is empty.
            // Act/Assert: starting dir counts as 1 directory; 0 files; 0 bytes.
            assertVisitCounts(visitor, tempDir.get(), 1, 0, 0);
        }
    }

    /**
     * Tests a directory with one file of size 0.
     */
    @ParameterizedTest
    @MethodSource("countingPathVisitors")
    void testCountFolders1FileSize0(final CountingPathVisitor visitor) throws IOException {
        assertVisitorInitiallyHasZeroCounts(visitor);
        assertVisitCounts(visitor, DIRS_1_FILE_SIZE_0, 1, 1, 0);
    }

    /**
     * Tests a directory with one file of size 1.
     */
    @ParameterizedTest
    @MethodSource("countingPathVisitors")
    void testCountFolders1FileSize1(final CountingPathVisitor visitor) throws IOException {
        assertVisitorInitiallyHasZeroCounts(visitor);
        assertVisitCounts(visitor, DIRS_1_FILE_SIZE_1, 1, 1, 1);
    }

    /**
     * Tests a directory with two subdirectories, each containing one file of size 1.
     * Expected: 3 directories (root + 2 subdirs), 2 files, 2 bytes total.
     */
    @ParameterizedTest
    @MethodSource("countingPathVisitors")
    void testCountFolders2FileSize2(final CountingPathVisitor visitor) throws IOException {
        assertVisitorInitiallyHasZeroCounts(visitor);
        assertVisitCounts(visitor, DIRS_2_FILE_SIZE_2, 3, 2, 2);
    }

    /**
     * Ensures toString() is safe to call.
     */
    @ParameterizedTest
    @MethodSource("countingPathVisitors")
    void testToStringDoesNotThrow(final CountingPathVisitor visitor) {
        assertDoesNotThrow(visitor::toString);
    }
}