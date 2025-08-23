package org.apache.commons.compress.archivers.cpio;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class CpioUtilTestTest6 {

    @Test
    void testOldBinMagicFromByteArrayNotSwapped() {
        assertEquals(CpioConstants.MAGIC_OLD_BINARY, CpioUtil.byteArray2long(new byte[] { (byte) 0xc7, 0x71 }, false));
    }
}
