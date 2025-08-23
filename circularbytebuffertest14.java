package org.apache.commons.io.input.buffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

public class CircularByteBufferTestTest14 {

    @Test
    void testReadByteArrayIllegalArgumentException() {
        final CircularByteBuffer cbb = new CircularByteBuffer();
        final byte[] bytesOut = new byte[10];
        // targetOffset < 0
        assertThrows(IllegalArgumentException.class, () -> cbb.read(bytesOut, -1, 10));
        // targetOffset >= targetBuffer.length
        assertThrows(IllegalArgumentException.class, () -> cbb.read(bytesOut, 0, bytesOut.length + 1));
    }
}
