package org.apache.commons.io.input.buffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

public class CircularByteBufferTestTest1 {

    @Test
    void testAddByteSmallestBuffer() {
        final CircularByteBuffer cbb = new CircularByteBuffer(1);
        cbb.add((byte) 1);
        assertEquals(1, cbb.read());
        cbb.add((byte) 2);
        assertEquals(2, cbb.read());
    }
}
