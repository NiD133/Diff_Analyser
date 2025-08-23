package org.apache.commons.compress.compressors.lzma;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class LZMAUtilsTestTest4 {

    @SuppressWarnings("deprecation")
    @Test
    void testGetUncompressedFilename() {
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
}
