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

class ListerTest {

    /**
     * Provides a stream of paths to test fixture files.
     * Only includes files with specific extensions that do not end with "-fail".
     *
     * @return a stream of paths to valid test fixture files.
     * @throws IOException if an I/O error occurs while accessing the file system.
     */
    public static Stream<Path> provideTestFixturePaths() throws IOException {
        // Define the root directory for test resources
        Path testResourcesDirectory = Paths.get("src/test/resources");

        // Define a regex filter to match files with specific extensions that do not end with "-fail"
        RegexFileFilter fileFilter = new RegexFileFilter("^(?!.*(-fail)).*\\.(tar|ar|arj|apk|dump)$");

        // Walk the file tree up to a depth of 10 to find matching files
        return PathUtils.walk(testResourcesDirectory, fileFilter, 10, false);
    }

    /**
     * Tests the Lister class's ability to process archive files.
     * 
     * @param archivePath the path to the archive file to be tested.
     * @throws ArchiveException if an error occurs while processing the archive.
     * @throws IOException if an I/O error occurs.
     */
    @ParameterizedTest
    @MethodSource("provideTestFixturePaths")
    void testListerWithValidArchives(final Path archivePath) throws ArchiveException, IOException {
        // Create a Lister instance with quiet mode enabled and the archive path as an argument
        Lister lister = new Lister(true, archivePath.toString());

        // Execute the Lister's main functionality
        lister.go();
    }
}