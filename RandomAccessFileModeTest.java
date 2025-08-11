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
 * Tests for {@link RandomAccessFileMode} enum functionality.
 * 
 * This test suite verifies:
 * - File creation with different access modes
 * - Mode string mappings
 * - Permission hierarchy (implies relationship)
 * - Conversion from standard Java NIO options
 */
class RandomAccessFileModeTest {

    // Test data constants
    private static final byte[] TEST_FILE_CONTENT = "Foo".getBytes(StandardCharsets.US_ASCII);
    private static final String TEST_FILENAME = "test.txt";

    @TempDir
    public Path tempDir;

    // Helper methods
    
    /**
     * Reads all bytes from a RandomAccessFile for testing purposes.
     */
    private byte[] readAllBytes(final RandomAccessFile randomAccessFile) throws IOException {
        return RandomAccessFiles.read(randomAccessFile, 0, (int) randomAccessFile.length());
    }

    /**
     * Creates a test file with the given content in the temp directory.
     */
    private Path createTestFile(final byte[] content) throws IOException {
        return Files.write(
            tempDir.resolve(TEST_FILENAME), 
            content, 
            StandardOpenOption.CREATE, 
            StandardOpenOption.TRUNCATE_EXISTING
        );
    }

    // Tests for file creation methods

    @ParameterizedTest
    @EnumSource(RandomAccessFileMode.class)
    void shouldCreateRandomAccessFileFromFile_ForAllModes(final RandomAccessFileMode mode) throws IOException {
        // Given: a test file with known content
        final Path testFile = createTestFile(TEST_FILE_CONTENT);
        
        // When: creating RandomAccessFile using File parameter
        try (RandomAccessFile randomAccessFile = mode.create(testFile.toFile())) {
            // Then: should be able to read the expected content
            assertArrayEquals(TEST_FILE_CONTENT, readAllBytes(randomAccessFile));
        }
    }

    @ParameterizedTest
    @EnumSource(RandomAccessFileMode.class)
    void shouldCreateRandomAccessFileFromPath_ForAllModes(final RandomAccessFileMode mode) throws IOException {
        // Given: a test file with known content
        final Path testFile = createTestFile(TEST_FILE_CONTENT);
        
        // When: creating RandomAccessFile using Path parameter with consumer
        // Then: should be able to read the expected content
        mode.accept(testFile, randomAccessFile -> 
            assertArrayEquals(TEST_FILE_CONTENT, readAllBytes(randomAccessFile))
        );
    }

    @ParameterizedTest
    @EnumSource(RandomAccessFileMode.class)
    void shouldCreateRandomAccessFileFromString_ForAllModes(final RandomAccessFileMode mode) throws IOException {
        // Given: a test file with known content
        final Path testFile = createTestFile(TEST_FILE_CONTENT);
        
        // When: creating RandomAccessFile using String path parameter
        try (RandomAccessFile randomAccessFile = mode.create(testFile.toString())) {
            // Then: should be able to read the expected content
            assertArrayEquals(TEST_FILE_CONTENT, readAllBytes(randomAccessFile));
        }
    }

    @ParameterizedTest
    @EnumSource(RandomAccessFileMode.class)
    void shouldCreateIORandomAccessFileFromString_ForAllModes(final RandomAccessFileMode mode) throws IOException {
        // Given: a test file with known content
        final Path testFile = createTestFile(TEST_FILE_CONTENT);
        
        // When: creating IORandomAccessFile using String path parameter
        try (IORandomAccessFile randomAccessFile = mode.io(testFile.toString())) {
            // Then: should be able to read the expected content
            assertArrayEquals(TEST_FILE_CONTENT, readAllBytes(randomAccessFile));
        }
    }

    // Tests for mode string mappings

    @Test
    void shouldReturnCorrectModeStrings() {
        // Verify that each enum value maps to the correct RandomAccessFile mode string
        assertEquals("r", RandomAccessFileMode.READ_ONLY.getMode());
        assertEquals("rw", RandomAccessFileMode.READ_WRITE.getMode());
        assertEquals("rwd", RandomAccessFileMode.READ_WRITE_SYNC_CONTENT.getMode());
        assertEquals("rws", RandomAccessFileMode.READ_WRITE_SYNC_ALL.getMode());
    }

    @Test
    void shouldConvertModeStringToEnum() {
        // Verify conversion from RandomAccessFile mode strings to enum values
        assertEquals(RandomAccessFileMode.READ_ONLY, RandomAccessFileMode.valueOfMode("r"));
        assertEquals(RandomAccessFileMode.READ_WRITE, RandomAccessFileMode.valueOfMode("rw"));
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT, RandomAccessFileMode.valueOfMode("rwd"));
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_ALL, RandomAccessFileMode.valueOfMode("rws"));
    }

    // Tests for permission hierarchy

    @Test
    void shouldRespectPermissionHierarchy_ImpliesRelationship() {
        // Test the permission hierarchy: higher permission modes imply lower ones
        
        // READ_WRITE_SYNC_ALL (highest) implies READ_WRITE_SYNC_CONTENT
        assertTrue(RandomAccessFileMode.READ_WRITE_SYNC_ALL.implies(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT));
        
        // READ_WRITE_SYNC_CONTENT implies READ_WRITE
        assertTrue(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT.implies(RandomAccessFileMode.READ_WRITE));
        
        // READ_WRITE implies READ_ONLY (lowest)
        assertTrue(RandomAccessFileMode.READ_WRITE.implies(RandomAccessFileMode.READ_ONLY));
        
        // Lower permission modes should NOT imply higher ones
        assertFalse(RandomAccessFileMode.READ_ONLY.implies(RandomAccessFileMode.READ_WRITE_SYNC_ALL));
    }

    // Tests for NIO option conversion

    @ParameterizedTest
    @EnumSource(LinkOption.class)
    void shouldConvertLinkOptionsToValidModes(final LinkOption linkOption) {
        // All LinkOption conversions should result in at least READ_ONLY permission
        assertTrue(RandomAccessFileMode.valueOf(linkOption).implies(RandomAccessFileMode.READ_ONLY));
    }

    @ParameterizedTest
    @EnumSource(StandardOpenOption.class)
    void shouldConvertStandardOpenOptionsToValidModes(final StandardOpenOption openOption) {
        // All StandardOpenOption conversions should result in at least READ_ONLY permission
        assertTrue(RandomAccessFileMode.valueOf(openOption).implies(RandomAccessFileMode.READ_ONLY));
    }

    @Test
    void shouldConvertSpecificOpenOptionCombinationsCorrectly() {
        // Test specific StandardOpenOption combinations and their expected RandomAccessFileMode mappings
        
        // READ_ONLY mappings
        assertEquals(RandomAccessFileMode.READ_ONLY, 
            RandomAccessFileMode.valueOf(StandardOpenOption.READ));
        
        // READ_WRITE mappings
        assertEquals(RandomAccessFileMode.READ_WRITE, 
            RandomAccessFileMode.valueOf(StandardOpenOption.WRITE));
        assertEquals(RandomAccessFileMode.READ_WRITE, 
            RandomAccessFileMode.valueOf(StandardOpenOption.READ, StandardOpenOption.WRITE));
        
        // READ_WRITE_SYNC_CONTENT mappings (DSYNC)
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT, 
            RandomAccessFileMode.valueOf(StandardOpenOption.DSYNC));
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT, 
            RandomAccessFileMode.valueOf(StandardOpenOption.WRITE, StandardOpenOption.DSYNC));
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT,
            RandomAccessFileMode.valueOf(StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.DSYNC));
        
        // READ_WRITE_SYNC_ALL mappings (SYNC)
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_ALL, 
            RandomAccessFileMode.valueOf(StandardOpenOption.SYNC));
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_ALL, 
            RandomAccessFileMode.valueOf(StandardOpenOption.READ, StandardOpenOption.SYNC));
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_ALL,
            RandomAccessFileMode.valueOf(StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.SYNC));
    }

    // Tests for standard enum behavior

    @Test
    void shouldReturnCorrectEnumNames_ToString() {
        // Verify standard Enum.toString() behavior returns the enum constant names
        assertEquals("READ_ONLY", RandomAccessFileMode.READ_ONLY.toString());
        assertEquals("READ_WRITE", RandomAccessFileMode.READ_WRITE.toString());
        assertEquals("READ_WRITE_SYNC_ALL", RandomAccessFileMode.READ_WRITE_SYNC_ALL.toString());
        assertEquals("READ_WRITE_SYNC_CONTENT", RandomAccessFileMode.READ_WRITE_SYNC_CONTENT.toString());
    }
}