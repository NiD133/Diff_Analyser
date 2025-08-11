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

package org.apache.commons.compress.archivers;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.commons.io.file.PathUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Test suite for the {@link Lister} class that verifies archive listing functionality
 * across various archive formats using test fixture files.
 */
class ListerTest {

    // Test fixture configuration
    private static final String TEST_RESOURCES_PATH = "src/test/resources";
    private static final int MAX_DIRECTORY_DEPTH = 10;
    private static final boolean FOLLOW_SYMBOLIC_LINKS = false;
    private static final boolean ENABLE_QUIET_MODE = true;
    
    // Regex pattern explanation:
    // ^(?!.*(-fail)).*  - Start of line, negative lookahead to exclude files containing "-fail"
    // \\.(tar|ar|arj|apk|dump)$  - Match files ending with supported archive extensions
    private static final String VALID_ARCHIVE_FILES_REGEX = "^(?!.*(-fail)).*\\.(tar|ar|arj|apk|dump)$";

    /**
     * Provides test fixture archive files for parameterized testing.
     * 
     * <p>This method scans the test resources directory for archive files with specific extensions
     * (tar, ar, arj, apk, dump) while excluding any files whose names contain "-fail" to avoid
     * testing with intentionally corrupted or invalid archive files.</p>
     *
     * @return a stream of paths to valid archive test fixture files
     * @throws IOException if an I/O error occurs while scanning the directory
     */
    public static Stream<Path> provideValidArchiveFixtures() throws IOException {
        Path testResourcesDirectory = Paths.get(TEST_RESOURCES_PATH);
        RegexFileFilter archiveFileFilter = new RegexFileFilter(VALID_ARCHIVE_FILES_REGEX);
        
        return PathUtils.walk(
            testResourcesDirectory, 
            archiveFileFilter, 
            MAX_DIRECTORY_DEPTH, 
            FOLLOW_SYMBOLIC_LINKS
        );
    }

    /**
     * Tests that the Lister can successfully process various archive formats without throwing exceptions.
     * 
     * <p>This test verifies that the {@link Lister#go()} method can handle different archive types
     * (tar, ar, arj, apk, dump) by attempting to list their contents. The test uses quiet mode
     * to suppress output during testing.</p>
     *
     * @param archiveFilePath path to the archive file to be tested
     * @throws ArchiveException if the archive format is unsupported or corrupted
     * @throws IOException if an I/O error occurs while reading the archive file
     */
    @ParameterizedTest(name = "Should successfully list contents of archive: {0}")
    @MethodSource("provideValidArchiveFixtures")
    void shouldListArchiveContentsWithoutErrors(final Path archiveFilePath) 
            throws ArchiveException, IOException {
        
        // Given: A valid archive file and a Lister configured in quiet mode
        String archivePathString = archiveFilePath.toString();
        Lister archiveLister = new Lister(ENABLE_QUIET_MODE, archivePathString);
        
        // When & Then: The lister should process the archive without throwing exceptions
        archiveLister.go();
    }
}