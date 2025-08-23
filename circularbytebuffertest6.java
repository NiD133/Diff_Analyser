package org.apache.commons.io.input.buffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

public class CircularByteBufferTestTest6 {

    @Test
    void testClear() {
        final byte[] data = { 1, 2, 3 };
        final CircularByteBuffer buffer = new CircularByteBuffer(10);
        assertEquals(0, buffer.getCurrentNumberOfBytes());
        assertFalse(buffer.hasBytes());
        buffer.add(data, 0, data.length);
        assertEquals(3, buffer.getCurrentNumberOfBytes());
        assertEquals(7, buffer.getSpace());
        assertTrue(buffer.hasBytes());
        assertTrue(buffer.hasSpace());
        buffer.clear();
        assertEquals(0, buffer.getCurrentNumberOfBytes());
        assertEquals(10, buffer.getSpace());
        assertFalse(buffer.hasBytes());
        assertTrue(buffer.hasSpace());
    }
}
