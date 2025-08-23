package org.apache.commons.compress.archivers.cpio;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class CpioUtilTestTest1 {

    @Test
    void testByteArray2longThrowsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> CpioUtil.byteArray2long(new byte[1], true));
    }
}
