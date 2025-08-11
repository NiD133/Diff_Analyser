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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link LZMAUtils}.
 */
class LZMAUtilsTest {

    @AfterEach
    void restoreDefaultCachingState() {
        // Ensure caching is enabled after each test to maintain consistent state
        LZMAUtils.setCacheLZMAAvailablity(true);
    }

    @Test
    void shouldHaveCachingEnabledByDefaultWithLZMAAvailable() {
        // Given: Default state
        // When: Checking LZMA availability and caching status
        LZMAUtils.CachedAvailability cachedAvailability = LZMAUtils.getCachedLZMAAvailability();
        boolean isLZMAAvailable = LZMAUtils.isLZMACompressionAvailable();
        
        // Then: Caching should be enabled and LZMA should be available
        assertEquals(LZMAUtils.CachedAvailability.CACHED_AVAILABLE, cachedAvailability);
        assertTrue(isLZMAAvailable);
    }

    @Test
    void shouldAllowDisablingCaching() {
        // Given: Caching is initially enabled
        // When: Disabling caching
        LZMAUtils.setCacheLZMAAvailablity(false);
        
        // Then: Caching should be disabled but LZMA should still be available
        assertEquals(LZMAUtils.CachedAvailability.DONT_CACHE, LZMAUtils.getCachedLZMAAvailability());
        assertTrue(LZMAUtils.isLZMACompressionAvailable());
    }

    @Test
    void shouldReEnableCachingAndReevaluateAvailability() {
        // Given: Caching is disabled
        LZMAUtils.setCacheLZMAAvailablity(false);
        assertEquals(LZMAUtils.CachedAvailability.DONT_CACHE, LZMAUtils.getCachedLZMAAvailability());
        
        // When: Re-enabling caching
        LZMAUtils.setCacheLZMAAvailablity(true);
        
        // Then: Caching should be enabled and availability re-evaluated
        assertEquals(LZMAUtils.CachedAvailability.CACHED_AVAILABLE, LZMAUtils.getCachedLZMAAvailability());
    }

    @SuppressWarnings("deprecation")
    @Test
    void shouldGenerateCorrectCompressedFilenames() {
        // Test cases for filename compression (both new and deprecated methods)
        
        // Empty filename should get .lzma extension
        assertEquals(".lzma", LZMAUtils.getCompressedFilename(""));
        assertEquals(".lzma", LZMAUtils.getCompressedFileName(""));
        
        // Simple filename should get .lzma extension
        assertEquals("x.lzma", LZMAUtils.getCompressedFilename("x"));
        assertEquals("x.lzma", LZMAUtils.getCompressedFileName("x"));

        // Filenames with spaces and special characters
        assertEquals("x.wmf .lzma", LZMAUtils.getCompressedFilename("x.wmf "));
        assertEquals("x.wmf .lzma", LZMAUtils.getCompressedFileName("x.wmf "));
        assertEquals("x.wmf\n.lzma", LZMAUtils.getCompressedFilename("x.wmf\n"));
        assertEquals("x.wmf\n.lzma", LZMAUtils.getCompressedFileName("x.wmf\n"));
        
        // Filenames with multiple extensions
        assertEquals("x.wmf.y.lzma", LZMAUtils.getCompressedFilename("x.wmf.y"));
        assertEquals("x.wmf.y.lzma", LZMAUtils.getCompressedFileName("x.wmf.y"));
    }

    @SuppressWarnings("deprecation")
    @Test
    void shouldGenerateCorrectUncompressedFilenames() {
        // Test cases for filename decompression (both new and deprecated methods)
        
        // Empty filename should remain empty
        assertEquals("", LZMAUtils.getUncompressedFilename(""));
        assertEquals("", LZMAUtils.getUncompressedFileName(""));
        
        // Just .lzma extension should remain as is (no base filename)
        assertEquals(".lzma", LZMAUtils.getUncompressedFilename(".lzma"));
        assertEquals(".lzma", LZMAUtils.getUncompressedFileName(".lzma"));

        // Valid LZMA files should have extension removed
        assertEquals("x", LZMAUtils.getUncompressedFilename("x.lzma"));
        assertEquals("x", LZMAUtils.getUncompressedFileName("x.lzma"));
        assertEquals("x", LZMAUtils.getUncompressedFilename("x-lzma"));
        assertEquals("x", LZMAUtils.getUncompressedFileName("x-lzma"));

        // Files with trailing spaces or characters should not be processed
        assertEquals("x.lzma ", LZMAUtils.getUncompressedFilename("x.lzma "));
        assertEquals("x.lzma ", LZMAUtils.getUncompressedFileName("x.lzma "));
        assertEquals("x.lzma\n", LZMAUtils.getUncompressedFilename("x.lzma\n"));
        assertEquals("x.lzma\n", LZMAUtils.getUncompressedFileName("x.lzma\n"));
        assertEquals("x.lzma.y", LZMAUtils.getUncompressedFilename("x.lzma.y"));
        assertEquals("x.lzma.y", LZMAUtils.getUncompressedFileName("x.lzma.y"));
    }

    @SuppressWarnings("deprecation")
    @Test
    void shouldCorrectlyIdentifyCompressedFilenames() {
        // Test cases for compressed filename detection (both new and deprecated methods)
        
        // Empty filename and just extension are not considered compressed
        assertFalse(LZMAUtils.isCompressedFilename(""));
        assertFalse(LZMAUtils.isCompressedFileName(""));
        assertFalse(LZMAUtils.isCompressedFilename(".lzma"));
        assertFalse(LZMAUtils.isCompressedFileName(".lzma"));

        // Valid LZMA compressed filenames
        assertTrue(LZMAUtils.isCompressedFilename("x.lzma"));
        assertTrue(LZMAUtils.isCompressedFileName("x.lzma"));
        assertTrue(LZMAUtils.isCompressedFilename("x-lzma"));
        assertTrue(LZMAUtils.isCompressedFileName("x-lzma"));

        // Invalid extensions should not be recognized
        assertFalse(LZMAUtils.isCompressedFilename("xxgz"));
        assertFalse(LZMAUtils.isCompressedFileName("xxgz"));
        assertFalse(LZMAUtils.isCompressedFilename("lzmaz"));
        assertFalse(LZMAUtils.isCompressedFileName("lzmaz"));
        assertFalse(LZMAUtils.isCompressedFilename("xaz"));
        assertFalse(LZMAUtils.isCompressedFileName("xaz"));

        // Files with trailing characters after LZMA extension are not considered compressed
        assertFalse(LZMAUtils.isCompressedFilename("x.lzma "));
        assertFalse(LZMAUtils.isCompressedFileName("x.lzma "));
        assertFalse(LZMAUtils.isCompressedFilename("x.lzma\n"));
        assertFalse(LZMAUtils.isCompressedFileName("x.lzma\n"));
        assertFalse(LZMAUtils.isCompressedFilename("x.lzma.y"));
        assertFalse(LZMAUtils.isCompressedFileName("x.lzma.y"));
    }

    @Test
    void shouldMatchLZMAMagicBytes() {
        // Given: LZMA magic bytes signature
        final byte[] validLZMASignature = { (byte) 0x5D, 0, 0 };
        
        // When/Then: Should not match with insufficient bytes
        assertFalse(LZMAUtils.matches(validLZMASignature, 2));
        
        // When/Then: Should match with exact or more bytes
        assertTrue(LZMAUtils.matches(validLZMASignature, 3));
        assertTrue(LZMAUtils.matches(validLZMASignature, 4));
        
        // Given: Invalid signature (corrupted third byte)
        validLZMASignature[2] = '0';
        
        // When/Then: Should not match corrupted signature
        assertFalse(LZMAUtils.matches(validLZMASignature, 3));
    }
}