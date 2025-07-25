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
        // Test that an exception is thrown when a long file name is used with the default settings
        final ArArchiveOutputStream outputStreamReference;
        try (ArArchiveOutputStream outputStream = new ArArchiveOutputStream(new ByteArrayOutputStream())) {
            outputStreamReference = outputStream;
            final ArArchiveEntry longNameEntry = new ArArchiveEntry("this_is_a_long_name.txt", 0);
            
            // Expect an IOException due to the long file name
            final IOException exception = assertThrows(IOException.class, () -> outputStream.putArchiveEntry(longNameEntry));
            assertTrue(exception.getMessage().startsWith("File name too long"));
        }
        // Ensure the output stream is closed after the operation
        assertTrue(outputStreamReference.isClosed());
    }

    @Test
    void testLongFileNamesWorkUsingBSDDialect() throws Exception {
        // Test that long file names are supported when using the BSD dialect
        final File tempFile = createTempFile();
        try (ArArchiveOutputStream outputStream = new ArArchiveOutputStream(Files.newOutputStream(tempFile.toPath()))) {
            outputStream.setLongFileMode(ArArchiveOutputStream.LONGFILE_BSD);
            final ArArchiveEntry longNameEntry = new ArArchiveEntry("this_is_a_long_name.txt", 14);
            
            // Add the entry and write data to it
            outputStream.putArchiveEntry(longNameEntry);
            outputStream.write(new byte[] { 'H', 'e', 'l', 'l', 'o', ',', ' ', 'w', 'o', 'r', 'l', 'd', '!', '\n' });
            outputStream.closeArchiveEntry();
            
            // Verify the archive content
            final List<String> expectedFileNames = new ArrayList<>();
            expectedFileNames.add("this_is_a_long_name.txt");
            checkArchiveContent(tempFile, expectedFileNames);
        }
    }
}