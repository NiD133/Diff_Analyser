package org.apache.commons.io.input.buffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

public class CircularByteBufferTestTest13 {

    @Test
    void testReadByteArray() {
        final CircularByteBuffer cbb = new CircularByteBuffer();
        final String string = "0123456789";
        final byte[] bytesIn = string.getBytes(StandardCharsets.UTF_8);
        cbb.add(bytesIn, 0, 10);
        final byte[] bytesOut = new byte[10];
        cbb.read(bytesOut, 0, 10);
        assertEquals(string, new String(bytesOut, StandardCharsets.UTF_8));
    }
}
