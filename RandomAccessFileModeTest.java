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

package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * Tests {@link RandomAccessFileMode}.
 */
@DisplayName("RandomAccessFileMode")
class RandomAccessFileModeTest {

    private static final String TEST_FILE_NAME = "test.txt";
    private static final byte[] TEST_CONTENT_ASCII = "Foo".getBytes(StandardCharsets.US_ASCII);

    /**
     * Temporary directory provided by JUnit.
     */
    @TempDir
    Path tempDir;

    // -----------------
    // Helper utilities
    // -----------------

    private Path writeTempFile(final byte[] bytes) throws IOException {
        return Files.write(tempDir.resolve(TEST_FILE_NAME), bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private byte[] readAll(final RandomAccessFile raf) throws IOException {
        return RandomAccessFiles.read(raf, 0, (int) raf.length());
    }

    // ---------------
    // Factory methods
    // ---------------

    @ParameterizedTest(name = "create(File) should open and read content using mode {0}")
    @EnumSource(RandomAccessFileMode.class)
    void create_fromFile(final RandomAccessFileMode mode) throws IOException {
        final Path filePath = writeTempFile(TEST_CONTENT_ASCII);

        try (RandomAccessFile raf = mode.create(filePath.toFile())) {
            assertArrayEquals(TEST_CONTENT_ASCII, readAll(raf), "create(File) must read the bytes written to disk");
        }
    }

    @ParameterizedTest(name = "create(Path) via accept(Path, IOConsumer) should open and read content using mode {0}")
    @EnumSource(RandomAccessFileMode.class)
    void accept_withPath(final RandomAccessFileMode mode) throws IOException {
        final Path filePath = writeTempFile(TEST_CONTENT_ASCII);

        mode.accept(filePath, raf -> assertArrayEquals(TEST_CONTENT_ASCII, readAll(raf), "accept(Path, IOConsumer) must read the bytes written to disk"));
    }

    @ParameterizedTest(name = "create(String) should open and read content using mode {0}")
    @EnumSource(RandomAccessFileMode.class)
    void create_fromString(final RandomAccessFileMode mode) throws IOException {
        final Path filePath = writeTempFile(TEST_CONTENT_ASCII);

        try (RandomAccessFile raf = mode.create(filePath.toString())) {
            assertArrayEquals(TEST_CONTENT_ASCII, readAll(raf), "create(String) must read the bytes written to disk");
        }
    }

    @ParameterizedTest(name = "io(String) should open and read content using mode {0}")
    @EnumSource(RandomAccessFileMode.class)
    void io_fromString(final RandomAccessFileMode mode) throws IOException {
        final Path filePath = writeTempFile(TEST_CONTENT_ASCII);

        try (IORandomAccessFile raf = mode.io(filePath.toString())) {
            assertArrayEquals(TEST_CONTENT_ASCII, readAll(raf), "io(String) must read the bytes written to disk");
        }
    }

    // -----------
    // Properties
    // -----------

    @Test
    @DisplayName("getMode() returns the canonical RandomAccessFile string")
    void getMode_returnsCanonicalModeString() {
        assertEquals("r", RandomAccessFileMode.READ_ONLY.getMode(), "READ_ONLY maps to 'r'");
        assertEquals("rw", RandomAccessFileMode.READ_WRITE.getMode(), "READ_WRITE maps to 'rw'");
        assertEquals("rwd", RandomAccessFileMode.READ_WRITE_SYNC_CONTENT.getMode(), "READ_WRITE_SYNC_CONTENT maps to 'rwd'");
        assertEquals("rws", RandomAccessFileMode.READ_WRITE_SYNC_ALL.getMode(), "READ_WRITE_SYNC_ALL maps to 'rws'");
    }

    @Test
    @DisplayName("toString() returns the enum constant name")
    void toString_returnsEnumName() {
        assertEquals("READ_ONLY", RandomAccessFileMode.READ_ONLY.toString());
        assertEquals("READ_WRITE", RandomAccessFileMode.READ_WRITE.toString());
        assertEquals("READ_WRITE_SYNC_ALL", RandomAccessFileMode.READ_WRITE_SYNC_ALL.toString());
        assertEquals("READ_WRITE_SYNC_CONTENT", RandomAccessFileMode.READ_WRITE_SYNC_CONTENT.toString());
    }

    // -------------
    // Relationships
    // -------------

    @Test
    @DisplayName("implies() reflects increasing access levels")
    void implies_respectsAccessHierarchy() {
        assertTrue(RandomAccessFileMode.READ_WRITE_SYNC_ALL.implies(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT),
                "SYNC_ALL implies SYNC_CONTENT");
        assertTrue(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT.implies(RandomAccessFileMode.READ_WRITE),
                "SYNC_CONTENT implies READ_WRITE");
        assertTrue(RandomAccessFileMode.READ_WRITE.implies(RandomAccessFileMode.READ_ONLY),
                "READ_WRITE implies READ_ONLY");
        assertFalse(RandomAccessFileMode.READ_ONLY.implies(RandomAccessFileMode.READ_WRITE_SYNC_ALL),
                "READ_ONLY should not imply SYNC_ALL");
    }

    // ----------
    // valueOf(...)
    // ----------

    @ParameterizedTest(name = "valueOf(LinkOption {0}) implies READ_ONLY")
    @EnumSource(LinkOption.class)
    void valueOf_fromLinkOption(final LinkOption option) {
        assertTrue(RandomAccessFileMode.valueOf(option).implies(RandomAccessFileMode.READ_ONLY),
                "Any LinkOption should at least imply READ_ONLY");
    }

    @ParameterizedTest(name = "valueOf(StandardOpenOption {0}) implies READ_ONLY")
    @EnumSource(StandardOpenOption.class)
    void valueOf_fromStandardOpenOption(final StandardOpenOption option) {
        assertTrue(RandomAccessFileMode.valueOf(option).implies(RandomAccessFileMode.READ_ONLY),
                "Any StandardOpenOption should at least imply READ_ONLY");
    }

    @Test
    @DisplayName("valueOfMode(String) parses canonical RandomAccessFile strings")
    void valueOfMode_parsesCanonicalStrings() {
        assertEquals(RandomAccessFileMode.READ_ONLY, RandomAccessFileMode.valueOfMode("r"));
        assertEquals(RandomAccessFileMode.READ_WRITE, RandomAccessFileMode.valueOfMode("rw"));
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT, RandomAccessFileMode.valueOfMode("rwd"));
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_ALL, RandomAccessFileMode.valueOfMode("rws"));
    }

    @Test
    @DisplayName("valueOf(OpenOption...) picks the best-fit RandomAccessFileMode")
    void valueOf_fromOpenOptions_combinesAsExpected() {
        // READ_ONLY
        assertEquals(RandomAccessFileMode.READ_ONLY, RandomAccessFileMode.valueOf(StandardOpenOption.READ));

        // READ_WRITE
        assertEquals(RandomAccessFileMode.READ_WRITE, RandomAccessFileMode.valueOf(StandardOpenOption.WRITE));
        assertEquals(RandomAccessFileMode.READ_WRITE, RandomAccessFileMode.valueOf(StandardOpenOption.READ, StandardOpenOption.WRITE));

        // READ_WRITE_SYNC_CONTENT
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT, RandomAccessFileMode.valueOf(StandardOpenOption.DSYNC));
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT, RandomAccessFileMode.valueOf(StandardOpenOption.WRITE, StandardOpenOption.DSYNC));
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT,
                RandomAccessFileMode.valueOf(StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.DSYNC));

        // READ_WRITE_SYNC_ALL
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_ALL, RandomAccessFileMode.valueOf(StandardOpenOption.SYNC));
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_ALL, RandomAccessFileMode.valueOf(StandardOpenOption.READ, StandardOpenOption.SYNC));
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_ALL,
                RandomAccessFileMode.valueOf(StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.SYNC));
    }
}