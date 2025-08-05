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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for the {@link RandomAccessFileMode} enum.
 * This class focuses on verifying the behavior of factory methods, file creation,
 * and utility methods like {@code implies}, {@code accept}, and {@code apply}.
 */
public class RandomAccessFileModeTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    // -- Test Factory Methods: valueOfMode(), valueOf(OpenOption...) --

    @Test
    public void testValueOfMode_shouldReturnCorrectEnum_forValidModes() {
        assertEquals(RandomAccessFileMode.READ_ONLY, RandomAccessFileMode.valueOfMode("r"));
        assertEquals(RandomAccessFileMode.READ_WRITE, RandomAccessFileMode.valueOfMode("rw"));
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT, RandomAccessFileMode.valueOfMode("rwd"));
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_ALL, RandomAccessFileMode.valueOfMode("rws"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfMode_shouldThrowException_forInvalidMode() {
        RandomAccessFileMode.valueOfMode("invalid-mode");
    }

    @Test(expected = NullPointerException.class)
    public void testValueOfMode_shouldThrowException_forNullMode() {
        RandomAccessFileMode.valueOfMode(null);
    }

    @Test
    public void testValueOfOpenOption_shouldReturnCorrectEnum() {
        // Test combinations of StandardOpenOption
        assertEquals(RandomAccessFileMode.READ_ONLY, RandomAccessFileMode.valueOf(StandardOpenOption.READ));
        assertEquals(RandomAccessFileMode.READ_WRITE, RandomAccessFileMode.valueOf(StandardOpenOption.WRITE));
        assertEquals(RandomAccessFileMode.READ_WRITE, RandomAccessFileMode.valueOf(StandardOpenOption.READ, StandardOpenOption.WRITE));
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT, RandomAccessFileMode.valueOf(StandardOpenOption.WRITE, StandardOpenOption.DSYNC));
        assertEquals(RandomAccessFileMode.READ_WRITE_SYNC_ALL, RandomAccessFileMode.valueOf(StandardOpenOption.WRITE, StandardOpenOption.SYNC));
    }

    @Test(expected = NullPointerException.class)
    public void testValueOfOpenOption_shouldThrowException_forNullArray() {
        RandomAccessFileMode.valueOf((OpenOption[]) null);
    }

    // -- Test `implies` Method --

    @Test
    public void testImplies_shouldReturnTrue_whenModeHasMoreOrEqualPermissions() {
        // A mode always implies itself
        for (final RandomAccessFileMode mode : RandomAccessFileMode.values()) {
            assertTrue(mode.getMode() + " should imply itself", mode.implies(mode));
        }

        // Test hierarchical implications
        assertTrue(RandomAccessFileMode.READ_WRITE_SYNC_ALL.implies(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT));
        assertTrue(RandomAccessFileMode.READ_WRITE_SYNC_ALL.implies(RandomAccessFileMode.READ_WRITE));
        assertTrue(RandomAccessFileMode.READ_WRITE_SYNC_ALL.implies(RandomAccessFileMode.READ_ONLY));

        assertTrue(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT.implies(RandomAccessFileMode.READ_WRITE));
        assertTrue(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT.implies(RandomAccessFileMode.READ_ONLY));

        assertTrue(RandomAccessFileMode.READ_WRITE.implies(RandomAccessFileMode.READ_ONLY));
    }

    @Test
    public void testImplies_shouldReturnFalse_whenModeHasFewerPermissions() {
        assertFalse(RandomAccessFileMode.READ_ONLY.implies(RandomAccessFileMode.READ_WRITE));
        assertFalse(RandomAccessFileMode.READ_WRITE.implies(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT));
        assertFalse(RandomAccessFileMode.READ_WRITE_SYNC_CONTENT.implies(RandomAccessFileMode.READ_WRITE_SYNC_ALL));
    }

    // -- Test `getMode` Method --

    @Test
    public void testGetMode_shouldReturnCorrectStringRepresentation() {
        assertEquals("r", RandomAccessFileMode.READ_ONLY.getMode());
        assertEquals("rw", RandomAccessFileMode.READ_WRITE.getMode());
        assertEquals("rwd", RandomAccessFileMode.READ_WRITE_SYNC_CONTENT.getMode());
        assertEquals("rws", RandomAccessFileMode.READ_WRITE_SYNC_ALL.getMode());
    }

    // -- Test `create` and `io` Methods --

    @Test
    public void testCreate_shouldCreateFile_forWriteModes() throws IOException {
        final File newFile = new File(tempFolder.getRoot(), "test.txt");
        assertFalse("File should not exist before test", newFile.exists());

        try (RandomAccessFile raf = RandomAccessFileMode.READ_WRITE.create(newFile)) {
            assertTrue("File should exist after creation", newFile.exists());
            raf.write(1); // Verify it's writable
            assertEquals(1, raf.length());
        }
    }

    @Test
    public void testCreate_shouldOpenFile_forReadOnlyMode() throws IOException {
        final File existingFile = tempFolder.newFile("test.txt");
        Files.write(existingFile.toPath(), "data".getBytes());

        try (RandomAccessFile raf = RandomAccessFileMode.READ_ONLY.create(existingFile)) {
            assertEquals('d', raf.readByte()); // Verify it's readable
            try {
                raf.write(1); // Verify it's not writable
                fail("Expected an IOException for writing in read-only mode");
            } catch (final IOException e) {
                // Expected behavior
            }
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreate_shouldThrowException_forReadOnlyOnNonExistentFile() throws IOException {
        final File nonExistentFile = new File(tempFolder.getRoot(), "nonexistent.txt");
        RandomAccessFileMode.READ_ONLY.create(nonExistentFile);
    }

    @Test(expected = FileNotFoundException.class)
    public void testIo_shouldThrowException_forReadOnlyOnNonExistentFile() throws IOException {
        final String nonExistentFilePath = new File(tempFolder.getRoot(), "nonexistent.txt").getPath();
        RandomAccessFileMode.READ_ONLY.io(nonExistentFilePath);
    }

    @Test(expected = NullPointerException.class)
    public void testCreate_shouldThrowException_forNullFile() throws IOException {
        RandomAccessFileMode.READ_WRITE.create((File) null);
    }

    @Test(expected = NullPointerException.class)
    public void testCreate_shouldThrowException_forNullPath() throws IOException {
        RandomAccessFileMode.READ_WRITE.create((Path) null);
    }

    @Test(expected = NullPointerException.class)
    public void testCreate_shouldThrowException_forNullName() throws IOException {
        RandomAccessFileMode.READ_WRITE.create((String) null);
    }

    // -- Test `accept` and `apply` Methods --

    @Test
    public void testAccept_shouldOpenAndCloseFile_andExecuteConsumer() throws IOException {
        final Path path = tempFolder.newFile("test.txt").toPath();
        final AtomicBoolean consumerCalled = new AtomicBoolean(false);

        RandomAccessFileMode.READ_WRITE.accept(path, raf -> {
            consumerCalled.set(true);
            assertNotNull(raf);
            raf.writeUTF("hello"); // Perform an action on the file
        });

        assertTrue("Consumer should have been called", consumerCalled.get());

        // Verify content was written and file is closed by reading it again
        try (RandomAccessFile raf = new RandomAccessFile(path.toFile(), "r")) {
            assertEquals("hello", raf.readUTF());
        }
    }

    @Test
    public void testApply_shouldOpenAndCloseFile_andReturnResult() throws IOException {
        final Path path = tempFolder.newFile("test.txt").toPath();
        Files.write(path, new byte[10]); // Write 10 bytes to the file

        final Long length = RandomAccessFileMode.READ_ONLY.apply(path, RandomAccessFile::length);

        assertEquals(10L, length.longValue());
    }

    @Test(expected = IOException.class)
    public void testAccept_shouldPropagateIOException_fromConsumer() throws IOException {
        final Path path = tempFolder.newFile("test.txt").toPath();
        RandomAccessFileMode.READ_WRITE.accept(path, raf -> {
            throw new IOException("Test Exception from Consumer");
        });
    }

    @Test(expected = FileNotFoundException.class)
    public void testAccept_shouldThrowException_forReadOnlyOnNonExistentFile() throws IOException {
        final Path path = tempFolder.getRoot().toPath().resolve("nonexistent.txt");
        RandomAccessFileMode.READ_ONLY.accept(path, raf -> {});
    }

    @Test(expected = NullPointerException.class)
    public void testAccept_shouldThrowException_forNullPath() throws IOException {
        RandomAccessFileMode.READ_WRITE.accept(null, raf -> {});
    }

    @Test(expected = NullPointerException.class)
    public void testAccept_shouldThrowException_forNullConsumer() throws IOException {
        final Path path = tempFolder.newFile().toPath();
        RandomAccessFileMode.READ_WRITE.accept(path, null);
    }
}