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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests for the {@link LZMAUtils} utility class.
 */
@DisplayName("LZMAUtils")
class LZMAUtilsTest {

    /**
     * Resets the caching state after each test to ensure test isolation,
     * as some tests modify this global static state.
     */
    @AfterEach
    void resetLzmaCache() {
        // The default is 'true' unless in an OSGi environment.
        // Resetting to 'true' is safe for the context of this test suite.
        LZMAUtils.setCacheLZMAAvailablity(true);
    }

    @Nested
    @DisplayName("Availability Caching")
    class AvailabilityCachingTest {

        @Test
        @DisplayName("should have caching enabled and LZMA available by default")
        void cachingIsEnabledByDefaultAndLzmaIsPresent() {
            assertEquals(LZMAUtils.CachedAvailability.CACHED_AVAILABLE, LZMAUtils.getCachedLZMAAvailability());
            assertTrue(LZMAUtils.isLZMACompressionAvailable());
        }

        @Test
        @DisplayName("should allow disabling the availability cache")
        void canTurnOffCaching() {
            // When caching is turned off
            LZMAUtils.setCacheLZMAAvailablity(false);

            // Then the cache status should reflect it
            assertEquals(LZMAUtils.CachedAvailability.DONT_CACHE, LZMAUtils.getCachedLZMAAvailability());

            // And the availability check should still work correctly
            assertTrue(LZMAUtils.isLZMACompressionAvailable());
        }

        @Test
        @DisplayName("should re-evaluate availability when caching is re-enabled")
        void turningOnCachingReEvaluatesAvailability() {
            // Given caching is initially off
            LZMAUtils.setCacheLZMAAvailablity(false);
            assertEquals(LZMAUtils.CachedAvailability.DONT_CACHE, LZMAUtils.getCachedLZMAAvailability());

            // When caching is turned back on
            LZMAUtils.setCacheLZMAAvailablity(true);

            // Then availability is re-evaluated and cached
            assertEquals(LZMAUtils.CachedAvailability.CACHED_AVAILABLE, LZMAUtils.getCachedLZMAAvailability());
        }
    }

    @Nested
    @DisplayName("Filename Handling")
    class FilenameHandlingTest {

        @SuppressWarnings("deprecation")
        @DisplayName("getCompressedFileName should add .lzma suffix")
        @ParameterizedTest(name = "for input \"{0}\", compressed should be \"{1}\"")
        @CsvSource({
            "'',        .lzma",
            "x,         x.lzma",
            "'x.wmf ',  'x.wmf .lzma'",
            "'x.wmf\n', 'x.wmf\n.lzma'",
            "x.wmf.y,   x.wmf.y.lzma"
        })
        void getCompressedFileName_should_addLzmaSuffix(final String input, final String expected) {
            // Asserts that both the new and deprecated methods behave identically
            assertEquals(expected, LZMAUtils.getCompressedFileName(input));
            assertEquals(expected, LZMAUtils.getCompressedFilename(input));
        }

        @SuppressWarnings("deprecation")
        @DisplayName("getUncompressedFileName should remove .lzma or -lzma suffix")
        @ParameterizedTest(name = "for input \"{0}\", uncompressed should be \"{1}\"")
        @CsvSource({
            "'',          ''",
            ".lzma,       .lzma", // Suffix without a base name is not removed
            "x.lzma,      x",
            "x-lzma,      x",
            "'x.lzma ',   'x.lzma '",   // Suffix with trailing space is not removed
            "'x.lzma\n',  'x.lzma\n'",  // Suffix with trailing newline is not removed
            "x.lzma.y,    x.lzma.y"     // Suffix in the middle is not removed
        })
        void getUncompressedFileName_should_removeLzmaSuffix(final String input, final String expected) {
            // Asserts that both the new and deprecated methods behave identically
            assertEquals(expected, LZMAUtils.getUncompressedFileName(input));
            assertEquals(expected, LZMAUtils.getUncompressedFilename(input));
        }

        @SuppressWarnings("deprecation")
        @DisplayName("isCompressedFileName should correctly detect .lzma or -lzma suffix")
        @ParameterizedTest(name = "isCompressedFileName(\"{0}\") should be {1}")
        @CsvSource({
            // Positive cases
            "x.lzma,    true",
            "x-lzma,    true",
            // Negative cases
            "'',        false",
            ".lzma,     false",
            "xxgz,      false",
            "lzmaz,     false",
            "xaz,       false",
            "'x.lzma ', false",
            "'x.lzma\n',false",
            "x.lzma.y,  false"
        })
        void isCompressedFileName_should_detectLzmaSuffix(final String filename, final boolean expected) {
            // Asserts that both the new and deprecated methods behave identically
            assertEquals(expected, LZMAUtils.isCompressedFileName(filename));
            assertEquals(expected, LZMAUtils.isCompressedFilename(filename));
        }
    }

    @Nested
    @DisplayName("Header Signature Matching")
    class MatchesTest {

        private final byte[] validHeader = {(byte) 0x5D, 0, 0};

        @Test
        @DisplayName("should return false if provided length is less than header size")
        void matches_should_returnFalse_whenBufferIsTooShort() {
            assertFalse(LZMAUtils.matches(validHeader, 2),
                "Should fail because length 2 is less than the 3-byte header.");
        }

        @Test
        @DisplayName("should return true if signature matches and length is sufficient")
        void matches_should_returnTrue_whenSignatureMatches() {
            assertTrue(LZMAUtils.matches(validHeader, 3),
                "Should pass with exact length and correct signature.");
        }

        @Test
        @DisplayName("should return true if buffer starts with a valid signature and length is greater than header size")
        void matches_should_returnTrue_whenBufferIsLonger() {
            final byte[] longBuffer = {(byte) 0x5D, 0, 0, 'a', 'b'};
            assertTrue(LZMAUtils.matches(longBuffer, 5),
                "Should pass as it only checks the first 3 bytes.");
        }

        @Test
        @DisplayName("should return false if signature does not match")
        void matches_should_returnFalse_whenSignatureIsInvalid() {
            final byte[] invalidHeader = {(byte) 0x5D, 0, 'a'}; // Corrupted header
            assertFalse(LZMAUtils.matches(invalidHeader, 3),
                "Should fail due to a mismatch in the header bytes.");
        }
    }
}