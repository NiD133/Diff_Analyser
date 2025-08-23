package org.apache.commons.compress.archivers.cpio;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class CpioUtilTestTest5 {

    @Test
    void testOldBinMagic2ByteArraySwapped() {
        assertArrayEquals(new byte[] { 0x71, (byte) 0xc7 }, CpioUtil.long2byteArray(CpioConstants.MAGIC_OLD_BINARY, 2, true));
    }
}
