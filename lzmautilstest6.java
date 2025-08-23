package org.apache.commons.compress.compressors.lzma;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class LZMAUtilsTestTest6 {

    @Test
    void testMatches() {
        final byte[] data = { (byte) 0x5D, 0, 0 };
        assertFalse(LZMAUtils.matches(data, 2));
        assertTrue(LZMAUtils.matches(data, 3));
        assertTrue(LZMAUtils.matches(data, 4));
        data[2] = '0';
        assertFalse(LZMAUtils.matches(data, 3));
    }
}
