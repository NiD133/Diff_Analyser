/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.codec.digest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link XXHash32}.
 * The test data has been generated with the {@code xxh32sum} command line utility.
 */
class XXHash32Test {

    /**
     * Provides test data for the parameterized tests.
     *
     * @return A stream of arguments, each containing a resource path and its expected checksum.
     */
    static Stream<Arguments> testData() {
        // @formatter:off
        // Checksums created with xxh32sum from https://cyan4973.github.io/xxHash/
        return Stream.of(
            Arguments.of("org/apache/commons/codec/bla.tar", "fbb5c8d1"),
            Arguments.of("org/apache/commons/codec/bla.tar.xz", "4106a208"),
            Arguments.of("org/apache/commons/codec/small.bin", "f66c26f8")
        );
        // @formatter:on
    }

    /**
     * Helper method to load a test resource from the classpath into a byte array.
     */
    private static byte[] getResourceAsBytes(final String resourcePath) throws IOException, URISyntaxException {
        final URL url = XXHash32Test.class.getClassLoader().getResource(resourcePath);
        if (url == null) {
            throw new FileNotFoundException("Could not find resource: " + resourcePath);
        }
        final Path path = Paths.get(url.toURI());
        return Files.readAllBytes(path);
    }

    @DisplayName("Computes checksum for a complete byte array")
    @ParameterizedTest(name = "File: {0}")
    @MethodSource("testData")
    void shouldComputeCorrectChecksumForEntireFile(final String resourcePath, final String expectedChecksum)
            throws Exception {
        // Arrange
        final byte[] data = getResourceAsBytes(resourcePath);
        final XXHash32 hasher = new XXHash32();

        // Act
        hasher.update(data, 0, data.length);
        final String actualChecksum = Long.toHexString(hasher.getValue());

        // Assert
        assertEquals(expectedChecksum, actualChecksum);
    }

    @DisplayName("Computes checksum correctly with incremental updates and reset")
    @ParameterizedTest(name = "File: {0}")
    @MethodSource("testData")
    void shouldComputeCorrectChecksumWhenUpdatingIncrementally(final String resourcePath, final String expectedChecksum)
            throws Exception {
        // Arrange
        final byte[] data = getResourceAsBytes(resourcePath);
        final XXHash32 hasher = new XXHash32();

        // Act & Assert

        // 1. Test reset() functionality.
        // Update with some data, then reset. The final hash should not be affected.
        if (data.length > 0) {
            hasher.update(data, 0, data.length / 2);
            hasher.reset();
        }

        // 2. Test incremental updates.
        // Process the data in up to three chunks: first byte, middle part, and last byte.
        // This covers different update paths in the algorithm and is robust for any file size.
        if (data.length > 0) {
            hasher.update(data[0]); // First byte
        }
        if (data.length > 2) {
            hasher.update(data, 1, data.length - 2); // Middle part
        }
        if (data.length > 1) {
            hasher.update(data[data.length - 1]); // Last byte
        }

        // 3. Test that a negative length is ignored.
        hasher.update(data, 0, -1);

        // 4. Final verification.
        final String actualChecksum = Long.toHexString(hasher.getValue());
        assertEquals(expectedChecksum, actualChecksum);
    }
}