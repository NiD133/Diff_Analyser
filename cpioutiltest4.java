package org.apache.commons.compress.archivers.cpio;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class CpioUtilTestTest4 {

    @Test
    void testOldBinMagic2ByteArrayNotSwapped() {
        assertArrayEquals(new byte[] { (byte) 0xc7, 0x71 }, CpioUtil.long2byteArray(CpioConstants.MAGIC_OLD_BINARY, 2, false));
    }
}
