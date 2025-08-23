package org.apache.commons.compress.archivers.cpio;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class CpioUtilTestTest3 {

    @Test
    void testLong2byteArrayWithZeroThrowsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> CpioUtil.long2byteArray(0L, 0, false));
    }
}
