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
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class XXHash32Test {

    private static final String RESOURCE_NOT_FOUND = "Resource not found: ";

    private Path getResourcePath(String resourcePath) throws Exception {
        URL url = getClass().getClassLoader().getResource(resourcePath);
        if (url == null) {
            throw new FileNotFoundException(RESOURCE_NOT_FOUND + resourcePath);
        }
        return Paths.get(url.toURI());
    }

    static Stream<Arguments> data() {
        // @formatter:off
        return Stream.of(
            // reference checksums created with xxh32sum
            // https://cyan4973.github.io/xxHash/
            Arguments.of("org/apache/commons/codec/bla.tar", "fbb5c8d1"),
            Arguments.of("org/apache/commons/codec/bla.tar.xz", "4106a208"),
            Arguments.of("org/apache/commons/codec/small.bin", "f66c26f8")
        );
        // @formatter:on
    }

    @ParameterizedTest
    @MethodSource("data")
    @DisplayName("Verify single update hash")
    void verifyChecksum(String resourcePath, String expectedChecksum) throws Exception {
        Path file = getResourcePath(resourcePath);
        byte[] bytes = Files.readAllBytes(file);
        
        XXHash32 hasher = new XXHash32();
        hasher.update(bytes, 0, bytes.length);
        
        assertEquals(expectedChecksum, Long.toHexString(hasher.getValue()),
            "Checksum mismatch for resource: " + resourcePath);
    }

    @ParameterizedTest
    @MethodSource("data")
    @DisplayName("Verify incremental hash updates")
    void verifyIncrementalChecksum(String resourcePath, String expectedChecksum) throws Exception {
        Path file = getResourcePath(resourcePath);
        byte[] bytes = Files.readAllBytes(file);
        
        XXHash32 hasher = new XXHash32();
        // Test reset and incremental updates
        hasher.update(bytes[0]);
        hasher.reset();
        hasher.update(bytes[0]);
        hasher.update(bytes, 1, bytes.length - 2);
        hasher.update(bytes, bytes.length - 1, 1);
        // Test handling of negative length (should be no-op)
        hasher.update(bytes, 0, -1);
        
        assertEquals(expectedChecksum, Long.toHexString(hasher.getValue()),
            "Incremental checksum mismatch for resource: " + resourcePath);
    }
}