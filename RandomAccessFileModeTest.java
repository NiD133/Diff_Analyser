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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * Tests {@link RandomAccessFileMode}.
 */
class RandomAccessFileModeTest {

    private static final byte[] TEST_FILE_CONTENT = "Foo".getBytes(StandardCharsets.US_ASCII);
    private static final String TEST_FILE_NAME = "test.txt";

    @TempDir
    public Path tempDir;

    private byte[] readFromRandomAccessFile(final RandomAccessFile randomAccessFile) throws IOException {
        return RandomAccessFiles.read(randomAccessFile, 0, (int) randomAccessFile.length());
    }

    private Path createTestFile(final byte[] bytes) throws IOException {
        return Files.write(
            tempDir.resolve(TEST_FILE_NAME),
            bytes,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
        );
    }

    // Tests for factory methods

    @ParameterizedTest
    @EnumSource(RandomAccessFileMode.class)
    void testCreateFile_OpensCorrectly(final RandomAccessFileMode mode) throws IOException {
        final Path filePath = createTestFile(TEST_FILE_CONTENT);
        try (RandomAccessFile randomAccessFile = mode.create(filePath.toFile())) {
            assertArrayEquals(TEST_FILE_CONTENT, readFromRandomAccessFile(randomAccessFile));
        }
    }

    @ParameterizedTest
    @EnumSource(RandomAccessFileMode.class)
    void testCreatePath_OpensCorrectly(final RandomAccessFileMode mode) throws IOException {
        final Path filePath = createTestFile(TEST_FILE_CONTENT);
        mode.accept(filePath, raf -> 
            assertArrayEquals(TEST_FILE_CONTENT, readFromRandomAccessFile(raf))
        );
    }

    @ParameterizedTest
    @EnumSource(RandomAccessFileMode.class)
    void testCreateString_OpensCorrectly(final RandomAccessFileMode mode) throws IOException {
        final Path filePath = createTestFile(TEST_FILE_CONTENT);
        try (RandomAccessFile randomAccessFile = mode.create(filePath.toString())) {
            assertArrayEquals(TEST_FILE_CONTENT, readFromRandomAccessFile(randomAccessFile));
        }
    }

    @ParameterizedTest
    @EnumSource(RandomAccessFileMode.class)
    void testIoString_OpensCorrectly(final RandomAccessFileMode mode) throws IOException {
        final Path filePath = createTestFile(TEST_FILE_CONTENT);
        try (IORandomAccessFile randomAccessFile = mode.io(filePath.toString())) {
            assertArrayEquals(TEST_FILE_CONTENT, readFromRandomAccessFile(randomAccessFile));
        }
    }

    // Tests for getMode()

    @Test
    void testGetMode_ReturnsCorrectModeString() {
        assertEquals("r", RandomAccessFileMode.READ_ONLY.getMode());
        assertEquals("rw", RandomAccessFileMode.READ_WRITE.getMode());
        assertEquals("rwd", RandomAccessFileMode.READ_WRITE_SYNC_CONTENT.getMode());
        assertEquals("rws", RandomAccessFileMode.READ_WRITE_SYNC_ALL.getMode());
    }

    // Tests for implies()

    @Test
    void testImplies_ForModeHierarchy() {
        // Higher modes imply lower modes in the hierarchy
        assertTrue(RandomAccessFileMode.READ_WRITE_SYNC_ALL.implies(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT));
        assertTrue(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT.implies(RandomAccessFileMode.READ_WRITE));
        assertTrue(RandomAccessFileMode.READ_WRITE.implies(RandomAccessFileMode.READ_ONLY));
        
        // Lower modes do not imply higher modes
        assertFalse(RandomAccessFileMode.READ_ONLY.implies(RandomAccessFileMode.READ_WRITE_SYNC_ALL));
    }

    // Tests for valueOfMode()

    @Test
    void testValueOfMode_ReturnsCorrectMode() {
        assertEquals(RandomAccessFileMode.READ_ONLY, RandomAccessFileMode.valueOfMode("r"));
        assertEquals(RandomAccessFileMode.READ_WRITE, RandomAccessFileMode.valueOfMode("rw"));
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT, RandomAccessFileMode.valueOfMode("rwd"));
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_ALL, RandomAccessFileMode.valueOfMode("rws"));
    }

    // Tests for valueOf() with OpenOptions

    @ParameterizedTest
    @EnumSource(LinkOption.class)
    void testValueOfLinkOption_ReturnsAtLeastReadOnlyMode(final LinkOption option) {
        assertTrue(RandomAccessFileMode.valueOf(option).implies(RandomAccessFileMode.READ_ONLY));
    }

    @ParameterizedTest
    @EnumSource(StandardOpenOption.class)
    void testValueOfStandardOpenOption_ReturnsAtLeastReadOnlyMode(final StandardOpenOption option) {
        assertTrue(RandomAccessFileMode.valueOf(option).implies(RandomAccessFileMode.READ_ONLY));
    }

    @Test
    void testValueOfStandardOpenOptions_ReturnsCorrectMode() {
        // Test READ_ONLY mode
        assertEquals(RandomAccessFileMode.READ_ONLY, RandomAccessFileMode.valueOf(StandardOpenOption.READ));
        
        // Test READ_WRITE mode
        assertEquals(RandomAccessFileMode.READ_WRITE, RandomAccessFileMode.valueOf(StandardOpenOption.WRITE));
        assertEquals(RandomAccessFileMode.READ_WRITE, RandomAccessFileMode.valueOf(StandardOpenOption.READ, StandardOpenOption.WRITE));
        
        // Test READ_WRITE_SYNC_CONTENT mode
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT, RandomAccessFileMode.valueOf(StandardOpenOption.DSYNC));
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT, RandomAccessFileMode.valueOf(StandardOpenOption.WRITE, StandardOpenOption.DSYNC));
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT,
                RandomAccessFileMode.valueOf(StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.DSYNC));
        
        // Test READ_WRITE_SYNC_ALL mode
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_ALL, RandomAccessFileMode.valueOf(StandardOpenOption.SYNC));
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_ALL, RandomAccessFileMode.valueOf(StandardOpenOption.READ, StandardOpenOption.SYNC));
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_ALL,
                RandomAccessFileMode.valueOf(StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.SYNC));
    }

    // Test for standard toString()

    @Test
    void testToString_ReturnsEnumConstantName() {
        assertEquals("READ_ONLY", RandomAccessFileMode.READ_ONLY.toString());
        assertEquals("READ_WRITE", RandomAccessFileMode.READ_WRITE.toString());
        assertEquals("READ_WRITE_SYNC_ALL", RandomAccessFileMode.READ_WRITE_SYNC_ALL.toString());
        assertEquals("READ_WRITE_SYNC_CONTENT", RandomAccessFileMode.READ_WRITE_SYNC_CONTENT.toString());
    }
}