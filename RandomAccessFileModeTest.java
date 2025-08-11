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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests {@link RandomAccessFileMode}.
 */
@DisplayName("Tests for RandomAccessFileMode")
class RandomAccessFileModeTest {

    private static final byte[] FILE_CONTENT_BYTES = "Foo".getBytes(StandardCharsets.US_ASCII);
    private static final String FILE_NAME = "test.txt";

    /**
     * Temporary directory managed by JUnit.
     */
    @TempDir
    public Path tempDir;

    private byte[] readAllBytes(final RandomAccessFile randomAccessFile) throws IOException {
        return RandomAccessFiles.read(randomAccessFile, 0, (int) randomAccessFile.length());
    }

    private Path createTestFile() throws IOException {
        return Files.write(tempDir.resolve(FILE_NAME), FILE_CONTENT_BYTES, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Test
    void getMode_should_returnCorrectStringIdentifier() {
        assertEquals("r", RandomAccessFileMode.READ_ONLY.getMode());
        assertEquals("rw", RandomAccessFileMode.READ_WRITE.getMode());
        assertEquals("rwd", RandomAccessFileMode.READ_WRITE_SYNC_CONTENT.getMode());
        assertEquals("rws", RandomAccessFileMode.READ_WRITE_SYNC_ALL.getMode());
    }

    @Test
    void implies_should_correctlyReflectAccessLevelHierarchy() {
        // Aliases for readability
        final RandomAccessFileMode r = RandomAccessFileMode.READ_ONLY;
        final RandomAccessFileMode rw = RandomAccessFileMode.READ_WRITE;
        final RandomAccessFileMode rwd = RandomAccessFileMode.READ_WRITE_SYNC_CONTENT;
        final RandomAccessFileMode rws = RandomAccessFileMode.READ_WRITE_SYNC_ALL;

        // A mode always implies itself
        assertTrue(r.implies(r));
        assertTrue(rw.implies(rw));
        assertTrue(rwd.implies(rwd));
        assertTrue(rws.implies(rws));

        // Test hierarchy
        assertTrue(rws.implies(rwd));
        assertTrue(rwd.implies(rw));
        assertTrue(rw.implies(r));

        // Test non-implication
        assertFalse(r.implies(rw));
        assertFalse(rw.implies(rwd));
        assertFalse(rwd.implies(rws));
    }

    @Test
    void toString_should_returnEnumConstantName() {
        assertEquals("READ_ONLY", RandomAccessFileMode.READ_ONLY.toString());
        assertEquals("READ_WRITE", RandomAccessFileMode.READ_WRITE.toString());
        assertEquals("READ_WRITE_SYNC_ALL", RandomAccessFileMode.READ_WRITE_SYNC_ALL.toString());
        assertEquals("READ_WRITE_SYNC_CONTENT", RandomAccessFileMode.READ_WRITE_SYNC_CONTENT.toString());
    }

    @Nested
    @DisplayName("Factory method: valueOfMode(String)")
    class ValueOfModeTests {
        @ParameterizedTest
        @CsvSource({
            "r,   READ_ONLY",
            "rw,  READ_WRITE",
            "rwd, READ_WRITE_SYNC_CONTENT",
            "rws, READ_WRITE_SYNC_ALL"
        })
        void valueOfMode_should_returnCorrectEnum_forValidModeString(final String mode, final RandomAccessFileMode expected) {
            assertEquals(expected, RandomAccessFileMode.valueOfMode(mode));
        }

        @Test
        void valueOfMode_should_throwException_forInvalidMode() {
            assertThrows(IllegalArgumentException.class, () -> RandomAccessFileMode.valueOfMode("invalid"));
        }
    }

    @Nested
    @DisplayName("Factory method: valueOf(OpenOption...)")
    class ValueOfOpenOptionTests {
        static Stream<Arguments> valueOfOpenOptionProvider() {
            return Stream.of(
                // READ_ONLY cases
                Arguments.of(RandomAccessFileMode.READ_ONLY, new OpenOption[]{StandardOpenOption.READ}),
                // READ_WRITE cases
                Arguments.of(RandomAccessFileMode.READ_WRITE, new OpenOption[]{StandardOpenOption.WRITE}),
                Arguments.of(RandomAccessFileMode.READ_WRITE, new OpenOption[]{StandardOpenOption.READ, StandardOpenOption.WRITE}),
                // READ_WRITE_SYNC_CONTENT cases
                Arguments.of(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT, new OpenOption[]{StandardOpenOption.DSYNC}),
                Arguments.of(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT, new OpenOption[]{StandardOpenOption.WRITE, StandardOpenOption.DSYNC}),
                Arguments.of(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT, new OpenOption[]{StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.DSYNC}),
                // READ_WRITE_SYNC_ALL cases
                Arguments.of(RandomAccessFileMode.READ_WRITE_SYNC_ALL, new OpenOption[]{StandardOpenOption.SYNC}),
                Arguments.of(RandomAccessFileMode.READ_WRITE_SYNC_ALL, new OpenOption[]{StandardOpenOption.READ, StandardOpenOption.SYNC}),
                Arguments.of(RandomAccessFileMode.READ_WRITE_SYNC_ALL, new OpenOption[]{StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.SYNC})
            );
        }

        @ParameterizedTest
        @MethodSource("valueOfOpenOptionProvider")
        void valueOf_should_returnCorrectMode_forGivenOpenOptions(final RandomAccessFileMode expected, final OpenOption... options) {
            assertEquals(expected, RandomAccessFileMode.valueOf(options));
        }
    }

    @Nested
    @DisplayName("File creation and access methods")
    class FileAccessTests {
        private Path testFilePath;

        @BeforeEach
        void setUp() throws IOException {
            testFilePath = createTestFile();
        }

        @ParameterizedTest
        @EnumSource(RandomAccessFileMode.class)
        void create_fromFile_should_openFileAndAllowReading(final RandomAccessFileMode mode) throws IOException {
            try (RandomAccessFile raf = mode.create(testFilePath.toFile())) {
                assertArrayEquals(FILE_CONTENT_BYTES, readAllBytes(raf));
            }
        }

        @ParameterizedTest
        @EnumSource(RandomAccessFileMode.class)
        void create_fromPath_should_openFileAndAllowReading(final RandomAccessFileMode mode) throws IOException {
            try (RandomAccessFile raf = mode.create(testFilePath)) {
                assertArrayEquals(FILE_CONTENT_BYTES, readAllBytes(raf));
            }
        }

        @ParameterizedTest
        @EnumSource(RandomAccessFileMode.class)
        void create_fromString_should_openFileAndAllowReading(final RandomAccessFileMode mode) throws IOException {
            try (RandomAccessFile raf = mode.create(testFilePath.toString())) {
                assertArrayEquals(FILE_CONTENT_BYTES, readAllBytes(raf));
            }
        }

        @ParameterizedTest
        @EnumSource(RandomAccessFileMode.class)
        void accept_should_provideReadableRandomAccessFile(final RandomAccessFileMode mode) throws IOException {
            mode.accept(testFilePath, raf -> assertArrayEquals(FILE_CONTENT_BYTES, readAllBytes(raf)));
        }

        @ParameterizedTest
        @EnumSource(RandomAccessFileMode.class)
        void io_fromString_should_openFileAndAllowReading(final RandomAccessFileMode mode) throws IOException {
            try (IORandomAccessFile ioraf = mode.io(testFilePath.toString())) {
                assertArrayEquals(FILE_CONTENT_BYTES, readAllBytes(ioraf));
            }
        }
    }
}