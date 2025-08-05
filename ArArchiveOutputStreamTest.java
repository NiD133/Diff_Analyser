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
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ArArchiveOutputStream} concerning long file name handling.
 * The "ar" format traditionally has a 16-character limit for file names.
 */
class ArArchiveOutputStreamTest extends AbstractTest {

    private static final String LONG_FILE_NAME = "this_is_a_long_name_that_exceeds_16_chars.txt";

    @Test
    void longFileNameShouldThrowExceptionByDefault() throws IOException {
        // Arrange
        final ArArchiveEntry entry = new ArArchiveEntry(LONG_FILE_NAME, 0);

        try (ArArchiveOutputStream arOut = new ArArchiveOutputStream(new ByteArrayOutputStream())) {
            // Act & Assert
            // The default mode is LONGFILE_ERROR, which should reject file names > 16 chars.
            final IOException ex = assertThrows(IOException.class, () -> arOut.putArchiveEntry(entry),
                    "Expected an exception for a long file name in default mode.");

            assertTrue(ex.getMessage().startsWith("File name too long"),
                    "Exception message should indicate the file name is too long.");
        }
        // The try-with-resources statement ensures the stream is closed automatically.
    }

    @Test
    void longFileNameShouldBeAllowedWithBsdDialect() throws Exception {
        // Arrange
        final File archive = createTempFile();
        final byte[] content = "Hello, world!\n".getBytes(StandardCharsets.UTF_8);
        final ArArchiveEntry entry = new ArArchiveEntry(LONG_FILE_NAME, content.length);

        // Act
        try (ArArchiveOutputStream arOut = new ArArchiveOutputStream(Files.newOutputStream(archive.toPath()))) {
            arOut.setLongFileMode(ArArchiveOutputStream.LONGFILE_BSD);
            arOut.putArchiveEntry(entry);
            arOut.write(content);
            arOut.closeArchiveEntry();
        }

        // Assert
        final List<String> expectedEntryNames = Collections.singletonList(LONG_FILE_NAME);
        checkArchiveContent(archive, expectedEntryNames);
    }
}