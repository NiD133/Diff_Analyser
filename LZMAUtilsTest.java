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
 * Tests for {@link LZMAUtils}.
 */
class LZMAUtilsTest {

    // Tests for LZMA availability caching
    @Test
    void cachingIsEnabledByDefaultAndLzmaIsAvailable() {
        // Verify default caching state and LZMA availability
        assertEquals(LZMAUtils.CachedAvailability.CACHED_AVAILABLE, LZMAUtils.getCachedLZMAAvailability());
        assertTrue(LZMAUtils.isLZMACompressionAvailable());
    }

    @Test
    void whenCachingDisabled_availabilityIsNotCached() {
        try {
            // Disable caching and verify state
            LZMAUtils.setCacheLZMAAvailablity(false);
            assertEquals(LZMAUtils.CachedAvailability.DONT_CACHE, LZMAUtils.getCachedLZMAAvailability());
            assertTrue(LZMAUtils.isLZMACompressionAvailable());
        } finally {
            // Reset to default state
            LZMAUtils.setCacheLZMAAvailablity(true);
        }
    }

    @Test
    void whenCachingReEnabled_availabilityIsRechecked() {
        try {
            // Disable caching then re-enable to verify re-evaluation
            LZMAUtils.setCacheLZMAAvailablity(false);
            LZMAUtils.setCacheLZMAAvailablity(true);
            assertEquals(LZMAUtils.CachedAvailability.CACHED_AVAILABLE, LZMAUtils.getCachedLZMAAvailability());
        } finally {
            LZMAUtils.setCacheLZMAAvailablity(true);
        }
    }

    // Tests for filename compression utilities
    @SuppressWarnings("deprecation")
    @Test
    void getCompressedFilename_returnsExpectedNames() {
        // Test empty and basic filenames
        assertEquals(".lzma", LZMAUtils.getCompressedFilename(""));
        assertEquals(".lzma", LZMAUtils.getCompressedFileName(""));
        assertEquals("x.lzma", LZMAUtils.getCompressedFilename("x"));
        assertEquals("x.lzma", LZMAUtils.getCompressedFileName("x"));

        // Test edge cases with spaces and newlines
        assertEquals("x.wmf .lzma", LZMAUtils.getCompressedFilename("x.wmf "));
        assertEquals("x.wmf .lzma", LZMAUtils.getCompressedFileName("x.wmf "));
        assertEquals("x.wmf\n.lzma", LZMAUtils.getCompressedFilename("x.wmf\n"));
        assertEquals("x.wmf\n.lzma", LZMAUtils.getCompressedFileName("x.wmf\n"));

        // Test multi-extension filenames
        assertEquals("x.wmf.y.lzma", LZMAUtils.getCompressedFilename("x.wmf.y"));
        assertEquals("x.wmf.y.lzma", LZMAUtils.getCompressedFileName("x.wmf.y"));
    }

    @SuppressWarnings("deprecation")
    @Test
    void getUncompressedFilename_returnsExpectedNames() {
        // Test empty and extension-only filenames
        assertEquals("", LZMAUtils.getUncompressedFilename(""));
        assertEquals("", LZMAUtils.getUncompressedFileName(""));
        assertEquals(".lzma", LZMAUtils.getUncompressedFilename(".lzma"));
        assertEquals(".lzma", LZMAUtils.getUncompressedFileName(".lzma"));

        // Test valid compressed filenames
        assertEquals("x", LZMAUtils.getUncompressedFilename("x.lzma"));
        assertEquals("x", LZMAUtils.getUncompressedFileName("x.lzma"));
        assertEquals("x", LZMAUtils.getUncompressedFilename("x-lzma"));
        assertEquals("x", LZMAUtils.getUncompressedFileName("x-lzma"));

        // Test edge cases with spaces and newlines
        assertEquals("x.lzma ", LZMAUtils.getUncompressedFilename("x.lzma "));
        assertEquals("x.lzma ", LZMAUtils.getUncompressedFileName("x.lzma "));
        assertEquals("x.lzma\n", LZMAUtils.getUncompressedFilename("x.lzma\n"));
        assertEquals("x.lzma\n", LZMAUtils.getUncompressedFileName("x.lzma\n"));

        // Test multi-extension filenames
        assertEquals("x.lzma.y", LZMAUtils.getUncompressedFilename("x.lzma.y"));
        assertEquals("x.lzma.y", LZMAUtils.getUncompressedFileName("x.lzma.y"));
    }

    @SuppressWarnings("deprecation")
    @Test
    void isCompressedFilename_detectsLzmaExtensions() {
        // Test non-compressed names
        assertFalse(LZMAUtils.isCompressedFilename(""));
        assertFalse(LZMAUtils.isCompressedFileName(""));
        assertFalse(LZMAUtils.isCompressedFilename(".lzma"));
        assertFalse(LZMAUtils.isCompressedFileName(".lzma"));

        // Test valid compressed names
        assertTrue(LZMAUtils.isCompressedFilename("x.lzma"));
        assertTrue(LZMAUtils.isCompressedFileName("x.lzma"));
        assertTrue(LZMAUtils.isCompressedFilename("x-lzma"));
        assertTrue(LZMAUtils.isCompressedFileName("x-lzma"));

        // Test invalid extensions
        assertFalse(LZMAUtils.isCompressedFilename("xxgz"));
        assertFalse(LZMAUtils.isCompressedFileName("xxgz"));
        assertFalse(LZMAUtils.isCompressedFilename("lzmaz"));
        assertFalse(LZMAUtils.isCompressedFileName("lzmaz"));
        assertFalse(LZMAUtils.isCompressedFilename("xaz"));
        assertFalse(LZMAUtils.isCompressedFileName("xaz"));

        // Test edge cases with spaces and newlines
        assertFalse(LZMAUtils.isCompressedFilename("x.lzma "));
        assertFalse(LZMAUtils.isCompressedFileName("x.lzma "));
        assertFalse(LZMAUtils.isCompressedFilename("x.lzma\n"));
        assertFalse(LZMAUtils.isCompressedFileName("x.lzma\n"));

        // Test multi-extension filenames
        assertFalse(LZMAUtils.isCompressedFilename("x.lzma.y"));
        assertFalse(LZMAUtils.isCompressedFileName("x.lzma.y"));
    }

    // Tests for header magic detection
    @Test
    void matches_detectsLzmaHeader() {
        final byte[] header = { (byte) 0x5D, 0, 0 };

        // Too short to be header
        assertFalse(LZMAUtils.matches(header, 2));
        // Valid header length
        assertTrue(LZMAUtils.matches(header, 3));
        // Extra bytes don't break valid header
        assertTrue(LZMAUtils.matches(header, 4));

        // Corrupt magic bytes
        header[2] = '0';
        assertFalse(LZMAUtils.matches(header, 3));
    }
}