package org.apache.commons.compress.compressors.lzma;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.compress.compressors.lzma.LZMAUtils;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class LZMAUtils_ESTest extends LZMAUtils_ESTest_scaffolding {

    private static final byte LZMA_MAGIC_BYTE = (byte) 93;
    private static final String LZMA_SUFFIX = ".lzma";

    @Test(timeout = 4000)
    public void testMatchesWithValidMagicByte() throws Throwable {
        byte[] byteArray = new byte[16];
        byteArray[0] = LZMA_MAGIC_BYTE;
        boolean result = LZMAUtils.matches(byteArray, LZMA_MAGIC_BYTE);
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void testMatchesWithInvalidMagicByte() throws Throwable {
        byte[] byteArray = new byte[5];
        byteArray[0] = (byte) 117;
        boolean result = LZMAUtils.matches(byteArray, LZMA_MAGIC_BYTE);
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void testMatchesWithEmptyArray() throws Throwable {
        byte[] byteArray = new byte[0];
        boolean result = LZMAUtils.matches(byteArray, 0);
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void testMatchesWithNullArray() throws Throwable {
        try {
            LZMAUtils.matches(null, LZMA_MAGIC_BYTE);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.compressors.lzma.LZMAUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testIsCompressedFilenameWithLzmaSuffix() throws Throwable {
        boolean result = LZMAUtils.isCompressedFilename("$VALUES" + LZMA_SUFFIX);
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void testIsCompressedFilenameWithoutLzmaSuffix() throws Throwable {
        boolean result = LZMAUtils.isCompressedFilename("CACHED_UNAVAILABLE");
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void testIsCompressedFilenameWithNull() throws Throwable {
        try {
            LZMAUtils.isCompressedFilename(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.compressors.FileNameUtil", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetUncompressedFilename() throws Throwable {
        String result = LZMAUtils.getUncompressedFilename("CACHED_UNAVAILABLE");
        assertEquals("CACHED_UNAVAILABLE", result);
    }

    @Test(timeout = 4000)
    public void testGetUncompressedFilenameWithEmptyString() throws Throwable {
        String result = LZMAUtils.getUncompressedFilename("");
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testGetUncompressedFilenameWithNull() throws Throwable {
        try {
            LZMAUtils.getUncompressedFilename(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.compressors.FileNameUtil", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetCompressedFilename() throws Throwable {
        String result = LZMAUtils.getCompressedFilename("h6n");
        assertEquals("h6n" + LZMA_SUFFIX, result);
    }

    @Test(timeout = 4000)
    public void testGetCompressedFilenameWithNull() throws Throwable {
        try {
            LZMAUtils.getCompressedFilename(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.compressors.FileNameUtil", e);
        }
    }

    @Test(timeout = 4000)
    public void testLZMACompressionAvailability() throws Throwable {
        LZMAUtils.setCacheLZMAAvailablity(false);
        boolean result = LZMAUtils.isLZMACompressionAvailable();
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void testCachedLZMAAvailability() throws Throwable {
        LZMAUtils.getCachedLZMAAvailability();
    }
}