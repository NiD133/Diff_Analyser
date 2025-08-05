package org.apache.commons.compress.compressors.lzma;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link LZMAUtils} utility class.
 */
public class LZMAUtilsTest {

    private static final byte[] LZMA_HEADER = {(byte) 0x5D, 0x00, 0x00};

    /**
     * The LZMAUtils class uses a static field to cache availability.
     * To ensure test independence, this method uses reflection to reset the
     * cache to its initial state before each test runs.
     */
    @Before
    public void resetLzmaAvailabilityCache() throws Exception {
        Field cacheField = LZMAUtils.class.getDeclaredField("cachedLZMAAvailability");
        cacheField.setAccessible(true);

        // Get the nested enum type LZMAUtils$CachedAvailability
        Class<?> cachedAvailabilityEnum = Class.forName("org.apache.commons.compress.compressors.lzma.LZMAUtils$CachedAvailability");
        
        // Get the DONT_CACHE enum constant
        Object dontCacheEnumValue = Enum.valueOf((Class<Enum>) cachedAvailabilityEnum, "DONT_CACHE");

        // Reset the static field to DONT_CACHE
        cacheField.set(null, dontCacheEnumValue);
    }

    //region matches() Tests

    @Test
    public void matches_shouldReturnTrue_forValidLzmaHeader() {
        byte[] signature = {LZMA_HEADER[0], LZMA_HEADER[1], LZMA_HEADER[2], 'a', 'b'};
        assertTrue("Should match a valid LZMA header", LZMAUtils.matches(signature, signature.length));
    }

    @Test
    public void matches_shouldReturnFalse_forIncorrectHeader() {
        byte[] signature = {'N', 'O', 'T', 'L', 'Z', 'M', 'A'};
        assertFalse("Should not match an invalid header", LZMAUtils.matches(signature, signature.length));
    }

    @Test
    public void matches_shouldReturnFalse_whenLengthIsShorterThanHeader() {
        byte[] signature = {LZMA_HEADER[0], LZMA_HEADER[1]};
        assertFalse("Should return false if the provided length is less than the magic number length",
                LZMAUtils.matches(signature, signature.length));
    }

    @Test
    public void matches_shouldReturnFalse_forEmptySignature() {
        assertFalse("Should return false for an empty byte array", LZMAUtils.matches(new byte[0], 0));
    }

    @Test(expected = NullPointerException.class)
    public void matches_shouldThrowException_forNullSignature() {
        LZMAUtils.matches(null, 3);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void matches_shouldThrowException_whenLengthExceedsSignatureArrayBounds() {
        // The length parameter (10) is greater than the signature array's length (5)
        LZMAUtils.matches(new byte[5], 10);
    }

    //endregion

    //region Filename Utility Tests

    @Test
    public void isCompressedFileName_shouldReturnTrue_forLzmaSuffix() {
        assertTrue("'.lzma' suffix should be identified", LZMAUtils.isCompressedFileName("test.lzma"));
        assertTrue("'.LZMA' suffix (case-insensitive) should be identified", LZMAUtils.isCompressedFileName("test.LZMA"));
    }

    @Test
    public void isCompressedFileName_shouldReturnFalse_forNonLzmaSuffix() {
        assertFalse("Filename without .lzma suffix should return false", LZMAUtils.isCompressedFileName("test.txt"));
        assertFalse("Empty filename should return false", LZMAUtils.isCompressedFileName(""));
    }

    @Test
    public void getUncompressedFileName_shouldRemoveLzmaSuffix() {
        assertEquals("Should remove '.lzma' suffix", "test", LZMAUtils.getUncompressedFileName("test.lzma"));
        assertEquals("Should remove '.LZMA' suffix (case-insensitive)", "test", LZMAUtils.getUncompressedFileName("test.LZMA"));
    }

    @Test
    public void getUncompressedFileName_shouldReturnOriginal_whenNoLzmaSuffix() {
        assertEquals("Should return original filename if no suffix", "test.txt", LZMAUtils.getUncompressedFileName("test.txt"));
        assertEquals("Should return empty string for empty input", "", LZMAUtils.getUncompressedFileName(""));
    }

    @Test
    public void getCompressedFileName_shouldAppendLzmaSuffix() {
        assertEquals("Should append '.lzma' suffix", "test.lzma", LZMAUtils.getCompressedFileName("test"));
    }

    @Test
    public void getCompressedFileName_shouldHandleEmptyString() {
        assertEquals("Should return '.lzma' for an empty string input", ".lzma", LZMAUtils.getCompressedFileName(""));
    }

    //endregion

    //region Deprecated Filename Utility Tests

    @Test
    @SuppressWarnings("deprecation")
    public void deprecatedMethods_shouldBehaveLikeNewMethods() {
        // Test that deprecated methods delegate to the new ones correctly.
        String compressed = "test.lzma";
        String uncompressed = "test";

        assertEquals(LZMAUtils.isCompressedFileName(compressed), LZMAUtils.isCompressedFilename(compressed));
        assertEquals(LZMAUtils.getUncompressedFileName(compressed), LZMAUtils.getUncompressedFilename(compressed));
        assertEquals(LZMAUtils.getCompressedFileName(uncompressed), LZMAUtils.getCompressedFilename(uncompressed));
    }

    //endregion

    //region Availability and Caching Tests

    @Test
    public void isLZMACompressionAvailable_shouldCacheResult_whenCachingIsEnabled() {
        LZMAUtils.setCacheLZMAAvailablity(true);
        assertEquals("Cache should be empty initially",
                LZMAUtils.CachedAvailability.DONT_CACHE, LZMAUtils.getCachedLZMAAvailability());

        // First call: performs the check and caches the result (false in a standard test env)
        boolean available = LZMAUtils.isLZMACompressionAvailable();
        assertFalse("LZMA should be unavailable in this test environment", available);

        // Verify that the result is now cached
        assertEquals("Cache should now hold the 'unavailable' status",
                LZMAUtils.CachedAvailability.CACHED_UNAVAILABLE, LZMAUtils.getCachedLZMAAvailability());

        // Second call: should return the cached result without re-checking
        assertFalse("Second call should return the cached value", LZMAUtils.isLZMACompressionAvailable());
    }

    @Test
    public void isLZMACompressionAvailable_shouldNotCacheResult_whenCachingIsDisabled() {
        LZMAUtils.setCacheLZMAAvailablity(false);
        assertEquals("Caching should be explicitly disabled",
                LZMAUtils.CachedAvailability.DONT_CACHE, LZMAUtils.getCachedLZMAAvailability());

        // First call: performs the check but should not cache the result
        boolean available = LZMAUtils.isLZMACompressionAvailable();
        assertFalse("LZMA should be unavailable in this test environment", available);

        // Verify that the cache state has not changed
        assertEquals("Cache should remain in the 'don't cache' state",
                LZMAUtils.CachedAvailability.DONT_CACHE, LZMAUtils.getCachedLZMAAvailability());
    }

    //endregion
}