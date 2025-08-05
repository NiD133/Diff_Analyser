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
package org.apache.commons.compress.compressors.lzma;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link LZMAUtils}.
 */
class LZMAUtilsTest {

    /**
     * Test that caching is enabled by default and LZMA compression is available.
     */
    @Test
    void testDefaultCachingAndLZMAAvailability() {
        assertEquals(LZMAUtils.CachedAvailability.CACHED_AVAILABLE, LZMAUtils.getCachedLZMAAvailability());
        assertTrue(LZMAUtils.isLZMACompressionAvailable());
    }

    /**
     * Test that caching can be disabled and LZMA compression is still available.
     */
    @Test
    void testDisableCaching() {
        try {
            LZMAUtils.setCacheLZMAAvailablity(false);
            assertEquals(LZMAUtils.CachedAvailability.DONT_CACHE, LZMAUtils.getCachedLZMAAvailability());
            assertTrue(LZMAUtils.isLZMACompressionAvailable());
        } finally {
            // Ensure caching is re-enabled after the test
            LZMAUtils.setCacheLZMAAvailablity(true);
        }
    }

    /**
     * Test the generation of compressed filenames.
     */
    @SuppressWarnings("deprecation")
    @Test
    void testCompressedFilenameGeneration() {
        assertEquals(".lzma", LZMAUtils.getCompressedFilename(""));
        assertEquals(".lzma", LZMAUtils.getCompressedFileName(""));
        assertEquals("x.lzma", LZMAUtils.getCompressedFilename("x"));
        assertEquals("x.lzma", LZMAUtils.getCompressedFileName("x"));

        assertEquals("x.wmf .lzma", LZMAUtils.getCompressedFilename("x.wmf "));
        assertEquals("x.wmf .lzma", LZMAUtils.getCompressedFileName("x.wmf "));
        assertEquals("x.wmf\n.lzma", LZMAUtils.getCompressedFilename("x.wmf\n"));
        assertEquals("x.wmf\n.lzma", LZMAUtils.getCompressedFileName("x.wmf\n"));
        assertEquals("x.wmf.y.lzma", LZMAUtils.getCompressedFilename("x.wmf.y"));
        assertEquals("x.wmf.y.lzma", LZMAUtils.getCompressedFileName("x.wmf.y"));
    }

    /**
     * Test the generation of uncompressed filenames.
     */
    @SuppressWarnings("deprecation")
    @Test
    void testUncompressedFilenameGeneration() {
        assertEquals("", LZMAUtils.getUncompressedFilename(""));
        assertEquals("", LZMAUtils.getUncompressedFileName(""));
        assertEquals(".lzma", LZMAUtils.getUncompressedFilename(".lzma"));
        assertEquals(".lzma", LZMAUtils.getUncompressedFileName(".lzma"));

        assertEquals("x", LZMAUtils.getUncompressedFilename("x.lzma"));
        assertEquals("x", LZMAUtils.getUncompressedFileName("x.lzma"));
        assertEquals("x", LZMAUtils.getUncompressedFilename("x-lzma"));
        assertEquals("x", LZMAUtils.getUncompressedFileName("x-lzma"));

        assertEquals("x.lzma ", LZMAUtils.getUncompressedFilename("x.lzma "));
        assertEquals("x.lzma ", LZMAUtils.getUncompressedFileName("x.lzma "));
        assertEquals("x.lzma\n", LZMAUtils.getUncompressedFilename("x.lzma\n"));
        assertEquals("x.lzma\n", LZMAUtils.getUncompressedFileName("x.lzma\n"));
        assertEquals("x.lzma.y", LZMAUtils.getUncompressedFilename("x.lzma.y"));
        assertEquals("x.lzma.y", LZMAUtils.getUncompressedFileName("x.lzma.y"));
    }

    /**
     * Test detection of compressed filenames.
     */
    @SuppressWarnings("deprecation")
    @Test
    void testCompressedFilenameDetection() {
        assertFalse(LZMAUtils.isCompressedFilename(""));
        assertFalse(LZMAUtils.isCompressedFileName(""));
        assertFalse(LZMAUtils.isCompressedFilename(".lzma"));
        assertFalse(LZMAUtils.isCompressedFileName(".lzma"));

        assertTrue(LZMAUtils.isCompressedFilename("x.lzma"));
        assertTrue(LZMAUtils.isCompressedFileName("x.lzma"));
        assertTrue(LZMAUtils.isCompressedFilename("x-lzma"));
        assertTrue(LZMAUtils.isCompressedFileName("x-lzma"));

        assertFalse(LZMAUtils.isCompressedFilename("xxgz"));
        assertFalse(LZMAUtils.isCompressedFileName("xxgz"));
        assertFalse(LZMAUtils.isCompressedFilename("lzmaz"));
        assertFalse(LZMAUtils.isCompressedFileName("lzmaz"));
        assertFalse(LZMAUtils.isCompressedFilename("xaz"));
        assertFalse(LZMAUtils.isCompressedFileName("xaz"));

        assertFalse(LZMAUtils.isCompressedFilename("x.lzma "));
        assertFalse(LZMAUtils.isCompressedFileName("x.lzma "));
        assertFalse(LZMAUtils.isCompressedFilename("x.lzma\n"));
        assertFalse(LZMAUtils.isCompressedFileName("x.lzma\n"));
        assertFalse(LZMAUtils.isCompressedFilename("x.lzma.y"));
        assertFalse(LZMAUtils.isCompressedFileName("x.lzma.y"));
    }

    /**
     * Test matching of LZMA file signatures.
     */
    @Test
    void testLZMASignatureMatching() {
        final byte[] data = { (byte) 0x5D, 0, 0 };
        assertFalse(LZMAUtils.matches(data, 2)); // Not enough bytes to match
        assertTrue(LZMAUtils.matches(data, 3));  // Exact match
        assertTrue(LZMAUtils.matches(data, 4));  // More bytes than needed
        data[2] = '0'; // Change the last byte
        assertFalse(LZMAUtils.matches(data, 3)); // No longer matches
    }

    /**
     * Test that turning on caching re-evaluates LZMA availability.
     */
    @Test
    void testReEvaluateAvailabilityOnCaching() {
        try {
            LZMAUtils.setCacheLZMAAvailablity(false);
            assertEquals(LZMAUtils.CachedAvailability.DONT_CACHE, LZMAUtils.getCachedLZMAAvailability());
            LZMAUtils.setCacheLZMAAvailablity(true);
            assertEquals(LZMAUtils.CachedAvailability.CACHED_AVAILABLE, LZMAUtils.getCachedLZMAAvailability());
        } finally {
            // Ensure caching is re-enabled after the test
            LZMAUtils.setCacheLZMAAvailablity(true);
        }
    }
}