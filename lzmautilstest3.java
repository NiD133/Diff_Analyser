package org.apache.commons.compress.compressors.lzma;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class LZMAUtilsTestTest3 {

    @SuppressWarnings("deprecation")
    @Test
    void testGetCompressedFilename() {
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
}
