package org.apache.commons.compress.compressors.lzma;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Focused, readable tests for LZMAUtils.
 * 
 * What we cover:
 * - File name detection and mapping (both current and deprecated methods)
 * - Signature matching for LZMA header bytes
 * - Null argument handling
 * - Caching toggle behavior
 */
public class LZMAUtilsTest {

    // LZMA header bytes according to the implementation (0x5D 00 00)
    private static final byte[] LZMA_HEADER = new byte[] { (byte) 0x5D, 0, 0 };

    // ----------------------------------------------------------------------
    // Signature matching
    // ----------------------------------------------------------------------

    @Test
    public void matches_returnsTrue_forValidLzmaHeader() {
        byte[] header = new byte[] { LZMA_HEADER[0], LZMA_HEADER[1], LZMA_HEADER[2], 1, 2 };
        assertTrue(LZMAUtils.matches(header, 3));
    }

    @Test
    public void matches_returnsFalse_whenHeaderDoesNotMatch() {
        byte[] wrongHeader = new byte[] { (byte) 0x7F, 0, 0, 1, 2 };
        assertFalse(LZMAUtils.matches(wrongHeader, 3));
    }

    @Test
    public void matches_returnsFalse_whenInsufficientLength() {
        byte[] tooShort = new byte[] { LZMA_HEADER[0], LZMA_HEADER[1] }; // only 2 bytes of the 3-byte header
        assertFalse(LZMAUtils.matches(tooShort, 2));
    }

    @Test
    public void matches_throwsNPE_forNullSignature() {
        assertThrows(NullPointerException.class, () -> LZMAUtils.matches(null, 3));
    }

    // ----------------------------------------------------------------------
    // File name utilities (current API)
    // ----------------------------------------------------------------------

    @Test
    public void isCompressedFileName_detectsDotLzmaSuffix() {
        assertTrue(LZMAUtils.isCompressedFileName("archive.lzma"));
    }

    @Test
    public void isCompressedFileName_detectsDashLzmaSuffix() {
        assertTrue(LZMAUtils.isCompressedFileName("archive-lzma"));
    }

    @Test
    public void isCompressedFileName_returnsFalse_forOtherExtensions() {
        assertFalse(LZMAUtils.isCompressedFileName("readme.txt"));
        assertFalse(LZMAUtils.isCompressedFileName("no-suffix"));
    }

    @Test
    public void getCompressedFileName_addsLzmaSuffix() {
        assertEquals("file.lzma", LZMAUtils.getCompressedFileName("file"));
    }

    @Test
    public void getUncompressedFileName_removesDotLzmaSuffix() {
        assertEquals("file", LZMAUtils.getUncompressedFileName("file.lzma"));
    }

    @Test
    public void getUncompressedFileName_removesDashLzmaSuffix() {
        assertEquals("file", LZMAUtils.getUncompressedFileName("file-lzma"));
    }

    @Test
    public void getUncompressedFileName_returnsInputWhenNoSuffix() {
        assertEquals("file", LZMAUtils.getUncompressedFileName("file"));
        assertEquals("", LZMAUtils.getUncompressedFileName(""));
    }

    // ----------------------------------------------------------------------
    // File name utilities (deprecated aliases) – kept for regression safety
    // ----------------------------------------------------------------------

    @Test
    @SuppressWarnings("deprecation")
    public void deprecated_isCompressedFilename_behavesLikeCurrentMethod() {
        assertTrue(LZMAUtils.isCompressedFilename("data.lzma"));
        assertFalse(LZMAUtils.isCompressedFilename("data.bin"));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void deprecated_getCompressedFilename_behavesLikeCurrentMethod() {
        assertEquals("h6n.lzma", LZMAUtils.getCompressedFilename("h6n"));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void deprecated_getUncompressedFilename_behavesLikeCurrentMethod() {
        assertEquals("data", LZMAUtils.getUncompressedFilename("data.lzma"));
        assertEquals("data", LZMAUtils.getUncompressedFilename("data-lzma"));
        assertEquals("plain", LZMAUtils.getUncompressedFilename("plain"));
        assertEquals("", LZMAUtils.getUncompressedFilename(""));
    }

    // ----------------------------------------------------------------------
    // Null handling
    // ----------------------------------------------------------------------

    @Test
    public void nullArguments_throwNullPointerException() {
        assertThrows(NullPointerException.class, () -> LZMAUtils.isCompressedFileName(null));
        assertThrows(NullPointerException.class, () -> LZMAUtils.getCompressedFileName(null));
        assertThrows(NullPointerException.class, () -> LZMAUtils.getUncompressedFileName(null));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void nullArguments_throwNullPointerException_onDeprecatedMethods() {
        assertThrows(NullPointerException.class, () -> LZMAUtils.isCompressedFilename(null));
        assertThrows(NullPointerException.class, () -> LZMAUtils.getCompressedFilename(null));
        assertThrows(NullPointerException.class, () -> LZMAUtils.getUncompressedFilename(null));
    }

    // ----------------------------------------------------------------------
    // Availability and caching
    // ----------------------------------------------------------------------

    @Test
    public void setCacheLZMAAvailablity_togglesCachingState() {
        // Snapshot current "doCache" mode based on enum value.
        LZMAUtils.CachedAvailability before = LZMAUtils.getCachedLZMAAvailability();

        try {
            // Disable caching -> must be DONT_CACHE
            LZMAUtils.setCacheLZMAAvailablity(false);
            assertEquals(LZMAUtils.CachedAvailability.DONT_CACHE, LZMAUtils.getCachedLZMAAvailability());

            // Enable caching -> result is either AVAILABLE or UNAVAILABLE
            LZMAUtils.setCacheLZMAAvailablity(true);
            LZMAUtils.CachedAvailability cached = LZMAUtils.getCachedLZMAAvailability();
            assertTrue(cached == LZMAUtils.CachedAvailability.CACHED_AVAILABLE
                    || cached == LZMAUtils.CachedAvailability.CACHED_UNAVAILABLE);

            // Smoke check: method should be callable regardless of caching mode.
            // We don't assert the value itself to avoid environment dependence.
            LZMAUtils.isLZMACompressionAvailable();
        } finally {
            // Restore caching mode (best-effort): DONT_CACHE if it was, otherwise enable caching.
            if (before == LZMAUtils.CachedAvailability.DONT_CACHE) {
                LZMAUtils.setCacheLZMAAvailablity(false);
            } else {
                LZMAUtils.setCacheLZMAAvailablity(true);
            }
        }
    }
}