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
 * Tests CleaningPathVisitor.
 *
 * High-level expectations:
 * - It counts visited directories, files, and file bytes.
 * - It deletes files (not directories) unless the file is explicitly skipped.
 * - equals/hashCode depend on the visitor's state (counters and configuration).
 */
class CleaningPathVisitorTest extends TestArguments {

    private static final Path RES_DIR_1_FILE_SIZE_0 = Paths.get("src/test/resources/org/apache/commons/io/dirs-1-file-size-0");
    private static final Path RES_DIR_1_FILE_SIZE_1 = Paths.get("src/test/resources/org/apache/commons/io/dirs-1-file-size-1");
    private static final Path RES_DIR_2_FILE_SIZE_2 = Paths.get("src/test/resources/org/apache/commons/io/dirs-2-file-size-2");

    @TempDir
    private Path tempDir;

    // ---------- Helpers ----------

    private CleaningPathVisitor walk(final CleaningPathVisitor visitor, final Path start) throws IOException {
        // visitFileTree returns the same instance, useful for fluent assertions
        return PathUtils.visitFileTree(visitor, start);
    }

    private void copyToTemp(final Path source) throws IOException {
        PathUtils.copyDirectory(source, tempDir);
    }

    private void walkAndAssertCounts(final CleaningPathVisitor visitor, final int dirs, final int files, final long bytes)
        throws IOException {
        final CleaningPathVisitor returned = walk(visitor, tempDir);
        assertCounts(dirs, files, bytes, returned);
        assertSame(visitor, returned, "visitFileTree must return the same visitor instance");
    }

    // ---------- Tests ----------

    /**
     * An empty directory: nothing to delete, but the directory is still visited.
     */
    @ParameterizedTest
    @MethodSource("cleaningPathVisitors")
    void emptyDirectory_isVisited_onlyDirectoryCounted(final CleaningPathVisitor visitor) throws IOException {
        walkAndAssertCounts(visitor, 1, 0, 0);
    }

    /**
     * Constructor accepting a null skip-array should behave the same as the default constructor.
     */
    @ParameterizedTest
    @MethodSource("pathCounters")
    void emptyDirectory_withNullSkipCtorArg_isVisited(final PathCounters counters) throws IOException {
        final CleaningPathVisitor visitor = new CleaningPathVisitor(counters, (String[]) null);
        walkAndAssertCounts(visitor, 1, 0, 0);
    }

    /**
     * One zero-byte file: it is visited and deleted; counters reflect the visit.
     */
    @ParameterizedTest
    @MethodSource("cleaningPathVisitors")
    void oneZeroByteFile_isDeletedAndCounted(final CleaningPathVisitor visitor) throws IOException {
        copyToTemp(RES_DIR_1_FILE_SIZE_0);
        walkAndAssertCounts(visitor, 1, 1, 0);
    }

    /**
     * One file with size 1 byte: it is visited and deleted; counters reflect the visit.
     */
    @ParameterizedTest
    @MethodSource("cleaningPathVisitors")
    void oneByteFile_isDeletedAndCounted(final CleaningPathVisitor visitor) throws IOException {
        copyToTemp(RES_DIR_1_FILE_SIZE_1);
        walkAndAssertCounts(visitor, 1, 1, 1);
    }

    /**
     * One 1-byte file but configured to skip that file: file remains on disk; counts still reflect the visit.
     */
    @ParameterizedTest
    @MethodSource("pathCounters")
    void oneByteFile_isSkipped_whenConfigured(final PathCounters pathCounters) throws IOException {
        copyToTemp(RES_DIR_1_FILE_SIZE_1);

        final String skippedFileName = "file-size-1.bin";
        final Path skippedFile = tempDir.resolve(skippedFileName);
        final CleaningPathVisitor visitor = new CleaningPathVisitor(pathCounters, skippedFileName);

        walkAndAssertCounts(visitor, 1, 1, 1);

        // The skipped file must not be deleted by the visitor.
        Assertions.assertTrue(Files.exists(skippedFile), "Expected skipped file to remain");

        // Clean up the skipped file so the temp directory can be removed on test teardown.
        Files.delete(skippedFile);
    }

    /**
     * Two subdirectories each with a 1-byte file: all files are deleted; counts reflect the visit.
     */
    @ParameterizedTest
    @MethodSource("cleaningPathVisitors")
    void twoFilesInTwoSubDirs_areDeletedAndCounted(final CleaningPathVisitor visitor) throws IOException {
        copyToTemp(RES_DIR_2_FILE_SIZE_2);
        walkAndAssertCounts(visitor, 3, 2, 2);
    }

    /**
     * equals/hashCode must:
     * - be reflexive
     * - be equal for two newly created visitors with the same configuration/state
     * - change as the internal counters change
     */
    @Test
    void equalsAndHashCode_reflectVisitorState() {
        final CountingPathVisitor v0 = CleaningPathVisitor.withLongCounters();
        final CountingPathVisitor v1 = CleaningPathVisitor.withLongCounters();

        // Newly created visitors with same state should be equal.
        assertEquals(v0, v0);
        assertEquals(v0, v1);
        assertEquals(v1, v0);
        assertEquals(v0.hashCode(), v1.hashCode());

        // Mutate one visitor; equals/hashCode should now differ.
        v0.getPathCounters().getByteCounter().increment();

        assertEquals(v0, v0);
        assertNotEquals(v0, v1);
        assertNotEquals(v1, v0);
        assertNotEquals(v0.hashCode(), v1.hashCode());
    }
}