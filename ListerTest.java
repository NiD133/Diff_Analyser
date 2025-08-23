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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Verifies that Lister can list the contents of known-good archive fixtures without throwing.
 * The quiet flag is used to suppress console output; the intent of the test is simply "no exceptions".
 */
class ListerTest {

    private static final Path TEST_RESOURCES_DIR = Paths.get("src/test/resources");
    private static final int MAX_WALK_DEPTH = 10;

    // File names that contain this marker are intentionally broken fixtures and must be excluded.
    private static final String FAIL_FIXTURE_MARKER = "-fail";

    // Keep this in sync with the fixture types present under src/test/resources.
    private static final Set<String> SUPPORTED_FIXTURE_EXTENSIONS = Set.of(
            "tar", "ar", "arj", "apk", "dump"
    );

    /**
     * Provides paths to all regular files under src/test/resources that:
     * - do not contain "-fail" in their file name, and
     * - have one of the supported extensions.
     *
     * Collected into a List first so the underlying directory walk stream is closed eagerly.
     */
    public static Stream<Path> fixtureArchives() throws IOException {
        try (Stream<Path> walk = Files.walk(TEST_RESOURCES_DIR, MAX_WALK_DEPTH)) {
            List<Path> fixtures = walk
                    .filter(Files::isRegularFile)
                    .filter(ListerTest::isNonFailFixture)
                    .filter(ListerTest::hasSupportedExtension)
                    .sorted()
                    .collect(Collectors.toList());
            return fixtures.stream();
        }
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("fixtureArchives")
    @DisplayName("Lister lists supported archives without throwing")
    void listsSupportedArchivesWithoutThrowing(final Path archivePath) {
        assertDoesNotThrow(() -> new Lister(true, archivePath.toString()).go(),
                () -> "Lister threw while listing: " + archivePath);
    }

    private static boolean isNonFailFixture(final Path path) {
        final String fileName = path.getFileName().toString();
        return !fileName.contains(FAIL_FIXTURE_MARKER);
    }

    private static boolean hasSupportedExtension(final Path path) {
        final String fileName = path.getFileName().toString();
        final int dot = fileName.lastIndexOf('.');
        if (dot < 0 || dot == fileName.length() - 1) {
            return false;
        }
        final String ext = fileName.substring(dot + 1);
        // Be forgiving about case; avoids missing fixtures on case-insensitive file systems.
        return SUPPORTED_FIXTURE_EXTENSIONS.contains(ext.toLowerCase());
    }
}