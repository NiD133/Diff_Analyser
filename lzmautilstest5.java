package org.apache.commons.compress.compressors.lzma;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class LZMAUtilsTestTest5 {

    @SuppressWarnings("deprecation")
    @Test
    void testIsCompressedFilename() {
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
}
