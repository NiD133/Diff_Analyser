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
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.AbstractTest;
import org.junit.jupiter.api.Test;

class ArArchiveOutputStreamTest extends AbstractTest {

    @Test
    void testLongFileNamesCauseExceptionByDefault() throws IOException {
        // Given
        final String longFileName = "this_is_a_long_name.txt";
        final ByteArrayOutputStream outputStreamBuffer = new ByteArrayOutputStream();

        // When
        try (ArArchiveOutputStream outputStream = new ArArchiveOutputStream(outputStreamBuffer)) {
            // Create an ArArchiveEntry with a long filename.
            final ArArchiveEntry archiveEntry = new ArArchiveEntry(longFileName, 0);

            // Then
            // By default, creating an entry with a long file name should throw an IOException.
            final IOException exception = assertThrows(IOException.class, () -> outputStream.putArchiveEntry(archiveEntry));
            assertTrue(exception.getMessage().startsWith("File name too long"));
        }

        // The output stream should be closed automatically by the try-with-resources block.
        // No additional assertions needed.
    }

    @Test
    void testLongFileNamesWorkUsingBSDDialect() throws Exception {
        // Given
        final File tempFile = createTempFile();
        final String longFileName = "this_is_a_long_name.txt";
        final byte[] fileContent = { 'H', 'e', 'l', 'l', 'o', ',', ' ', 'w', 'o', 'r', 'l', 'd', '!', '\n' };
        final long fileSize = fileContent.length;

        // When
        try (ArArchiveOutputStream outputStream = new ArArchiveOutputStream(Files.newOutputStream(tempFile.toPath()))) {
            // Set the long file mode to BSD to enable long filenames.
            outputStream.setLongFileMode(ArArchiveOutputStream.LONGFILE_BSD);

            // Create an ArArchiveEntry with a long filename.
            final ArArchiveEntry archiveEntry = new ArArchiveEntry(longFileName, fileSize);
            outputStream.putArchiveEntry(archiveEntry);

            // Write the content to the archive entry
            outputStream.write(fileContent);
            outputStream.closeArchiveEntry();

            // Then
            // Check if the archive file contains the specified file.
            final List<String> expectedFiles = new ArrayList<>();
            expectedFiles.add(longFileName);
            checkArchiveContent(tempFile, expectedFiles);
        }
    }
}