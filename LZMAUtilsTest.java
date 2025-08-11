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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Focused, readable tests for LZMAUtils.
 *
 * Test goals:
 * - Caching behavior: default state and ability to toggle.
 * - File name mapping: compression and decompression (new and deprecated APIs behave identically).
 * - Suffix detection for compressed names (new and deprecated APIs behave identically).
 * - Signature matching for .lzma headers.
 */
class LZMAUtilsTest {

    @AfterEach
    void resetCachingToDefault() {
        // Most environments default to caching enabled and available; ensure a clean state between tests.
        LZMAUtils.setCacheLZMAAvailablity(true);
    }

    // --- Caching behavior ----------------------------------------------------

    @Test
    void defaultCachingEnabled_andLzmaAvailable() {
        assertEquals(
                LZMAUtils.CachedAvailability.CACHED_AVAILABLE,
                LZMAUtils.getCachedLZMAAvailability(),
                "Expected LZMA to be cached as available by default"
        );
        assertTrue(LZMAUtils.isLZMACompressionAvailable(), "LZMA should be available");
    }

    @Test
    void canDisableCaching_withoutAffectingAvailability() {
        try {
            LZMAUtils.setCacheLZMAAvailablity(false);

            assertEquals(
                    LZMAUtils.CachedAvailability.DONT_CACHE,
                    LZMAUtils.getCachedLZMAAvailability(),
                    "Expected caching to be disabled"
            );
            assertTrue(
                    LZMAUtils.isLZMACompressionAvailable(),
                    "Disabling caching must not change availability"
            );
        } finally {
            LZMAUtils.setCacheLZMAAvailablity(true);
        }
    }

    @Test
    void enablingCaching_reEvaluatesAvailability() {
        try {
            LZMAUtils.setCacheLZMAAvailablity(false);
            assertEquals(LZMAUtils.CachedAvailability.DONT_CACHE, LZMAUtils.getCachedLZMAAvailability());

            LZMAUtils.setCacheLZMAAvailablity(true);
            assertEquals(
                    LZMAUtils.CachedAvailability.CACHED_AVAILABLE,
                    LZMAUtils.getCachedLZMAAvailability(),
                    "Re-enabling caching should re-check and cache availability"
            );
        } finally {
            LZMAUtils.setCacheLZMAAvailablity(true);
        }
    }

    // --- File name mapping ---------------------------------------------------

    @Nested
    @SuppressWarnings("deprecation")
    class FileNameMapping {

        // Helpers to assert that new and deprecated methods behave identically
        private void assertCompressedMapping(String expected, String input) {
            assertEquals(expected, LZMAUtils.getCompressedFileName(input), "new API");
            assertEquals(expected, LZMAUtils.getCompressedFilename(input), "deprecated API");
        }

        private void assertUncompressedMapping(String expected, String input) {
            assertEquals(expected, LZMAUtils.getUncompressedFileName(input), "new API");
            assertEquals(expected, LZMAUtils.getUncompressedFilename(input), "deprecated API");
        }

        @ParameterizedTest(name = "compress: \"{1}\" -> \"{0}\"")
        @CsvSource({
                ".lzma,''",
                "x.lzma,x",
                "'x.wmf .lzma','x.wmf '",
                "'x.wmf\n.lzma','x.wmf\n'",
                "x.wmf.y.lzma,x.wmf.y"
        })
        void compressedFileNameMapping(String expected, String input) {
            assertCompressedMapping(expected, input);
        }

        @ParameterizedTest(name = "uncompress: \"{1}\" -> \"{0}\"")
        @CsvSource({
                "'',''",
                ".lzma,.lzma",
                "x,x.lzma",
                "x,x-lzma",
                "'x.lzma ', 'x.lzma '",
                "'x.lzma\n','x.lzma\n'",
                "x.lzma.y,x.lzma.y"
        })
        void uncompressedFileNameMapping(String expected, String input) {
            assertUncompressedMapping(expected, input);
        }
    }

    // --- Compressed filename detection ---------------------------------------

    @Nested
    @SuppressWarnings("deprecation")
    class CompressedNameDetection {

        private void assertIsCompressed(String name) {
            assertTrue(LZMAUtils.isCompressedFileName(name), "new API should detect: " + name);
            assertTrue(LZMAUtils.isCompressedFilename(name), "deprecated API should detect: " + name);
        }

        private void assertIsNotCompressed(String name) {
            assertFalse(LZMAUtils.isCompressedFileName(name), "new API should not detect: " + name);
            assertFalse(LZMAUtils.isCompressedFilename(name), "deprecated API should not detect: " + name);
        }

        @ParameterizedTest(name = "is compressed: {0}")
        @ValueSource(strings = {
                "x.lzma",
                "x-lzma"
        })
        void detectsCompressedNames(String name) {
            assertIsCompressed(name);
        }

        @ParameterizedTest(name = "is NOT compressed: {0}")
        @ValueSource(strings = {
                "",           // empty
                ".lzma",      // just the extension
                "xxgz",       // other suffix
                "lzmaz",      // similar but not exact
                "xaz",        // noise
                "x.lzma ",    // trailing space
                "x.lzma\n",   // trailing newline
                "x.lzma.y"    // followed by more text
        })
        void rejectsNonCompressedNames(String name) {
            assertIsNotCompressed(name);
        }
    }

    // --- Signature matching --------------------------------------------------

    @Test
    void matchesLzmaHeaderMagicBytes() {
        final byte[] header = { (byte) 0x5D, 0, 0 };

        assertFalse(LZMAUtils.matches(header, 2), "Not enough bytes to match header");
        assertTrue(LZMAUtils.matches(header, 3), "Exact header length should match");
        assertTrue(LZMAUtils.matches(header, 4), "Extra bytes allowed after matching header");

        header[2] = '0'; // corrupt the third magic byte
        assertFalse(LZMAUtils.matches(header, 3), "Corrupted header should not match");
    }
}