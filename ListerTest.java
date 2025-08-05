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
     * Provides paths to valid archive test fixtures.
     *
     * <p>Walks through the test resources directory to find files matching:
     * <ul>
     *   <li>Archive extensions: tar, ar, arj, apk, dump</li>
     *   <li>Excludes files with "-fail" in their names</li>
     * </ul>
     * 
     * The regex pattern {@code ^(?!.*(-fail)).*\.(tar|ar|arj|apk|dump)$} ensures:
     * <ul>
     *   <li>{@code ^(?!.*(-fail))} - Negative lookahead to exclude filenames containing "-fail"</li>
     *   <li>{@code .*\.(tar|ar|arj|apk|dump)$} - Matches the specified archive extensions</li>
     * </ul>
     *
     * @return Stream of paths to valid test archives
     * @throws IOException if an I/O error occurs during file traversal
     */
    public static Stream<Path> provideValidArchiveFixtures() throws IOException {
        final Path testResourcesDir = Paths.get("src/test/resources");
        final String validArchivePattern = "^(?!.*(-fail)).*\\.(tar|ar|arj|apk|dump)$";
        return PathUtils.walk(testResourcesDir, new RegexFileFilter(validArchivePattern), 10, false);
    }

    /**
     * Tests that {@link Lister} can process valid archives without exceptions.
     *
     * <p>Each archive in the test fixtures is processed by {@link Lister#go()},
     * verifying it can handle the archive structure without errors.
     *
     * @param archivePath path to an archive file from the test fixtures
     * @throws ArchiveException if an archive processing error occurs
     * @throws IOException if an I/O error occurs
     */
    @ParameterizedTest(name = "Test valid archive: {0}")
    @MethodSource("provideValidArchiveFixtures")
    void testListerOnValidArchive(final Path archivePath) throws ArchiveException, IOException {
        new Lister(true, archivePath.toString()).go();
    }
}