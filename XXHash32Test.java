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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for XXHash32 implementation using test files with known checksums.
 * Reference checksums were generated using xxh32sum from https://cyan4973.github.io/xxHash/
 */
class XXHash32Test {

    private static final int BUFFER_SIZE = 10240;

    /**
     * Test data containing file paths and their expected XXHash32 checksums.
     * These checksums were verified using the reference xxh32sum implementation.
     */
    public static Stream<Arguments> testFiles() {
        return Stream.of(
            Arguments.of("org/apache/commons/codec/bla.tar", "fbb5c8d1"),
            Arguments.of("org/apache/commons/codec/bla.tar.xz", "4106a208"),
            Arguments.of("org/apache/commons/codec/small.bin", "f66c26f8")
        );
    }

    /**
     * Reads all bytes from an input stream into a byte array.
     */
    private static byte[] readAllBytes(final InputStream input) throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        IOUtils.copyLarge(input, output, new byte[BUFFER_SIZE]);
        return output.toByteArray();
    }

    /**
     * Loads a test file from the classpath and returns its Path.
     */
    private Path loadTestFile(final String resourcePath) throws Exception {
        final URL url = XXHash32Test.class.getClassLoader().getResource(resourcePath);
        if (url == null) {
            throw new FileNotFoundException("Test file not found: " + resourcePath);
        }
        return Paths.get(url.toURI());
    }

    /**
     * Tests that XXHash32 produces the correct checksum when processing entire file at once.
     */
    @ParameterizedTest
    @MethodSource("testFiles")
    public void shouldProduceCorrectChecksumForCompleteFile(final String filePath, final String expectedChecksum) throws Exception {
        // Given
        final Path testFile = loadTestFile(filePath);
        final XXHash32 hasher = new XXHash32();
        
        // When
        try (InputStream inputStream = Files.newInputStream(testFile)) {
            final byte[] fileBytes = readAllBytes(inputStream);
            hasher.update(fileBytes, 0, fileBytes.length);
        }
        
        // Then
        final String actualChecksum = Long.toHexString(hasher.getValue());
        assertEquals(expectedChecksum, actualChecksum, 
            "XXHash32 checksum mismatch for file: " + testFile);
    }

    /**
     * Tests that XXHash32 produces the correct checksum when processing file in chunks
     * and handles edge cases like reset() and negative length parameters.
     */
    @ParameterizedTest
    @MethodSource("testFiles")
    public void shouldProduceCorrectChecksumForIncrementalProcessing(final String filePath, final String expectedChecksum) throws Exception {
        // Given
        final Path testFile = loadTestFile(filePath);
        final XXHash32 hasher = new XXHash32();
        
        // When
        try (InputStream inputStream = Files.newInputStream(testFile)) {
            final byte[] fileBytes = readAllBytes(inputStream);
            
            // Test reset functionality: add some data then reset
            hasher.update(fileBytes[0]);
            hasher.reset();
            
            // Process file in chunks to test incremental updates
            processFileInChunks(hasher, fileBytes);
            
            // Test that negative length is ignored (edge case)
            hasher.update(fileBytes, 0, -1);
        }
        
        // Then
        final String actualChecksum = Long.toHexString(hasher.getValue());
        assertEquals(expectedChecksum, actualChecksum, 
            "XXHash32 incremental checksum mismatch for file: " + testFile);
    }

    /**
     * Processes file bytes in multiple chunks to test incremental hashing:
     * - First byte individually
     * - Middle bytes as array slice  
     * - Last byte individually
     */
    private void processFileInChunks(final XXHash32 hasher, final byte[] fileBytes) {
        if (fileBytes.length == 0) {
            return;
        }
        
        // Process first byte
        hasher.update(fileBytes[0]);
        
        if (fileBytes.length > 2) {
            // Process middle bytes (all except first and last)
            hasher.update(fileBytes, 1, fileBytes.length - 2);
        }
        
        if (fileBytes.length > 1) {
            // Process last byte
            hasher.update(fileBytes, fileBytes.length - 1, 1);
        }
    }
}