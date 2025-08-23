package org.apache.commons.compress.archivers.cpio;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class CpioUtilTestTest7 {

    @Test
    void testOldBinMagicFromByteArraySwapped() {
        assertEquals(CpioConstants.MAGIC_OLD_BINARY, CpioUtil.byteArray2long(new byte[] { 0x71, (byte) 0xc7 }, true));
    }
}
