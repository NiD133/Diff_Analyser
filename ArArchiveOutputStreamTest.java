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

/**
 * Tests for ArArchiveOutputStream focusing on file name length handling.
 * 
 * AR archives have a 16-character limit for file names by default.
 * This test suite verifies the behavior when dealing with long file names.
 */
class ArArchiveOutputStreamTest extends AbstractTest {

    // AR format has a 16-character limit for file names
    private static final String LONG_FILE_NAME = "this_is_a_long_name.txt";
    private static final String HELLO_WORLD_CONTENT = "Hello, world!\n";
    
    /**
     * Verifies that attempting to add an entry with a file name longer than 16 characters
     * throws an IOException when using the default error mode.
     * 
     * The AR format traditionally limits file names to 16 characters.
     * By default, ArArchiveOutputStream is configured to throw an exception
     * when encountering longer file names.
     */
    @Test
    void testLongFileNamesCauseExceptionByDefault() throws IOException {
        // Given: An AR archive output stream with default settings
        final ArArchiveOutputStream outputStream = new ArArchiveOutputStream(new ByteArrayOutputStream());
        
        try {
            // When: Attempting to add an entry with a name longer than 16 characters
            final ArArchiveEntry entryWithLongName = new ArArchiveEntry(LONG_FILE_NAME, 0);
            
            // Then: An IOException should be thrown
            final IOException exception = assertThrows(
                IOException.class, 
                () -> outputStream.putArchiveEntry(entryWithLongName),
                "Expected IOException when adding entry with long file name in default mode"
            );
            
            assertTrue(
                exception.getMessage().startsWith("File name too long"),
                "Exception message should indicate file name length issue"
            );
        } finally {
            outputStream.close();
        }
        
        // Verify the stream was properly closed
        assertTrue(outputStream.isClosed(), "Output stream should be closed after test");
    }

    /**
     * Verifies that long file names can be successfully written when using BSD dialect mode.
     * 
     * BSD AR format provides an extension mechanism to support file names longer than
     * 16 characters. This test ensures that when BSD mode is enabled, long file names
     * are properly handled and the archive can be created successfully.
     */
    @Test
    void testLongFileNamesWorkUsingBSDDialect() throws Exception {
        // Given: A temporary file to write the archive to
        final File archiveFile = createTempFile();
        
        try (ArArchiveOutputStream outputStream = new ArArchiveOutputStream(Files.newOutputStream(archiveFile.toPath()))) {
            // Enable BSD dialect to support long file names
            outputStream.setLongFileMode(ArArchiveOutputStream.LONGFILE_BSD);
            
            // When: Adding an entry with a long file name
            final ArArchiveEntry entryWithLongName = new ArArchiveEntry(
                LONG_FILE_NAME, 
                HELLO_WORLD_CONTENT.getBytes().length
            );
            
            outputStream.putArchiveEntry(entryWithLongName);
            outputStream.write(HELLO_WORLD_CONTENT.getBytes());
            outputStream.closeArchiveEntry();
        }
        
        // Then: Verify the archive was created correctly with the expected content
        final List<String> expectedFileNames = new ArrayList<>();
        expectedFileNames.add(LONG_FILE_NAME);
        
        checkArchiveContent(archiveFile, expectedFileNames);
    }
}