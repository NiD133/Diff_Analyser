/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.commons.compress.archivers.ar;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

import org.apache.commons.compress.AbstractTest;
import org.apache.commons.compress.archivers.ArchiveException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for ArArchiveOutputStream.
 *
 * Focus: behavior around long file names with default vs. BSD long-file support.
 */
class ArArchiveOutputStreamTest extends AbstractTest {

    private static final String LONG_FILE_NAME = "this_is_a_long_name.txt";
    private static final byte[] HELLO_WORLD = "Hello, world!\n".getBytes(StandardCharsets.US_ASCII);

    @Test
    @DisplayName("Default mode rejects long file names and stream gets closed")
    void defaultMode_rejectsLongFileNames_andClosesStream() throws IOException {
        // Arrange
        final ArArchiveOutputStream capturedStream;
        try (ArArchiveOutputStream outputStream = new ArArchiveOutputStream(new ByteArrayOutputStream())) {
            capturedStream = outputStream;
            final ArArchiveEntry longNameEntry = new ArArchiveEntry(LONG_FILE_NAME, 0);

            // Act + Assert
            final ArchiveException ex =
                    assertThrows(ArchiveException.class, () -> outputStream.putArchiveEntry(longNameEntry),
                            "Default mode should reject long file names");
            assertTrue(ex.getMessage().startsWith("File name too long"),
                    "Exception message should indicate long file name");
        }

        // Assert: try-with-resources must close the stream
        assertTrue(capturedStream.isClosed(), "Stream should be closed after leaving try-with-resources");
    }

    @Test
    @DisplayName("BSD long-file mode allows writing entries with long names")
    void bsdMode_allowsLongFileNames() throws Exception {
        // Arrange
        final File archiveFile = createTempFile();

        try (ArArchiveOutputStream outputStream =
                     new ArArchiveOutputStream(Files.newOutputStream(archiveFile.toPath()))) {
            outputStream.setLongFileMode(ArArchiveOutputStream.LONGFILE_BSD);

            final ArArchiveEntry entry = new ArArchiveEntry(LONG_FILE_NAME, HELLO_WORLD.length);

            // Act
            outputStream.putArchiveEntry(entry);
            outputStream.write(HELLO_WORLD);
            outputStream.closeArchiveEntry();

            // Assert: the archive contains the long-named entry
            final List<String> expectedEntries = Collections.singletonList(LONG_FILE_NAME);
            checkArchiveContent(archiveFile, expectedEntries);
        }
    }
}