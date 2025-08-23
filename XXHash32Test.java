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
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Verifies XXHash32 against known-good checksums produced by xxh32sum.
 * The tests cover both whole-array and incremental updates (including reset behavior).
 */
class XXHash32Test {

    /**
     * Test data: classpath resource path and its expected checksum (lower-case hex) from xxh32sum.
     */
    public static Stream<Arguments> data() {
        return Stream.of(
            Arguments.of("org/apache/commons/codec/bla.tar", "fbb5c8d1"),
            Arguments.of("org/apache/commons/codec/bla.tar.xz", "4106a208"),
            Arguments.of("org/apache/commons/codec/small.bin", "f66c26f8")
        );
    }

    /**
     * Loads a classpath resource into a byte array.
     */
    private static byte[] readResourceBytes(final String resourcePath) throws Exception {
        final URL url = XXHash32Test.class.getClassLoader().getResource(resourcePath);
        if (url == null) {
            throw new FileNotFoundException("Could not find test resource: " + resourcePath);
        }
        final Path path = Paths.get(url.toURI());
        return Files.readAllBytes(path);
    }

    /**
     * Computes the XXHash32 (seed 0) of the given bytes, returning a lower-case hex string.
     */
    private static String hashHex(final byte[] data) {
        final XXHash32 hasher = new XXHash32();
        hasher.update(data, 0, data.length);
        return Long.toHexString(hasher.getValue());
    }

    @ParameterizedTest
    @MethodSource("data")
    void verifyChecksum_wholeArray(final String resourcePath, final String expectedHex) throws Exception {
        final byte[] bytes = readResourceBytes(resourcePath);
        final String actualHex = hashHex(bytes);
        assertEquals(expectedHex, actualHex, "Whole-array checksum mismatch for " + resourcePath);
    }

    @ParameterizedTest
    @MethodSource("data")
    void verifyChecksum_incrementalAndReset(final String resourcePath, final String expectedHex) throws Exception {
        final byte[] bytes = readResourceBytes(resourcePath);

        final XXHash32 hasher = new XXHash32();

        // First, update with a single byte and then reset to ensure reset works.
        if (bytes.length > 0) {
            hasher.update(bytes[0]);
        }
        hasher.reset();

        // Now feed the data in chunks to exercise incremental update logic:
        // - first byte
        // - middle chunk
        // - last byte
        if (bytes.length > 0) {
            hasher.update(bytes[0]);
        }
        if (bytes.length > 2) {
            hasher.update(bytes, 1, bytes.length - 2);
        }
        if (bytes.length > 1) {
            hasher.update(bytes, bytes.length - 1, 1);
        }

        // Negative length argument should be ignored and not change the hash value.
        final long beforeIgnored = hasher.getValue();
        hasher.update(bytes, 0, -1); // intentionally invalid length
        final long afterIgnored = hasher.getValue();
        assertEquals(beforeIgnored, afterIgnored, "update(..., len < 0) should be ignored");

        final String actualHex = Long.toHexString(hasher.getValue());
        assertEquals(expectedHex, actualHex, "Incremental checksum mismatch for " + resourcePath);
    }
}