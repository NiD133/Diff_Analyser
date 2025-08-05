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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;

import org.apache.commons.compress.AbstractTest;
import org.junit.jupiter.api.Test;

class ArArchiveOutputStreamTest extends AbstractTest {

    // Constants for test data
    private static final String LONG_FILENAME = "this_is_a_long_name.txt";
    private static final String EXPECTED_ERROR_PREFIX = "File name too long";
    private static final byte[] FILE_CONTENT = {'H', 'e', 'l', 'l', 'o', ',', ' ', 
                                               'w', 'o', 'r', 'l', 'd', '!', '\n'};
    private static final int FILE_SIZE = FILE_CONTENT.length;

    @Test
    void defaultMode_shouldThrowExceptionWhenFilenameExceedsMaxLength() throws IOException {
        // Test that long filenames cause exceptions in default mode
        try (ArArchiveOutputStream outputStream = new ArArchiveOutputStream(new ByteArrayOutputStream())) {
            ArArchiveEntry entry = new ArArchiveEntry(LONG_FILENAME, 0);
            
            // Attempt to add entry with long filename should throw
            IOException ex = assertThrows(IOException.class, 
                () -> outputStream.putArchiveEntry(entry), 
                "Should throw IOException for long filenames in default mode");
            
            // Verify exception message
            assertTrue(ex.getMessage().startsWith(EXPECTED_ERROR_PREFIX),
                "Exception message should indicate filename length issue");
        }
    }

    @Test
    void bsdMode_shouldSupportLongFilenames() throws Exception {
        // Test that BSD dialect supports long filenames
        File archiveFile = createTempFile();
        
        try (ArArchiveOutputStream outputStream = new ArArchiveOutputStream(
            Files.newOutputStream(archiveFile.toPath()))) {
            
            // Enable BSD long filename support
            outputStream.setLongFileMode(ArArchiveOutputStream.LONGFILE_BSD);
            
            // Create entry with long filename
            ArArchiveEntry entry = new ArArchiveEntry(LONG_FILENAME, FILE_SIZE);
            outputStream.putArchiveEntry(entry);
            
            // Write file content
            outputStream.write(FILE_CONTENT);
            outputStream.closeArchiveEntry();
        }
        
        // Verify archive contains our long filename entry
        checkArchiveContent(archiveFile, Collections.singletonList(LONG_FILENAME));
    }
}