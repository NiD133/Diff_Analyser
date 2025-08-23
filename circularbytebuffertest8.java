package org.apache.commons.io.input.buffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

public class CircularByteBufferTestTest8 {

    @Test
    void testHasSpaceInt() {
        final CircularByteBuffer cbb = new CircularByteBuffer(1);
        assertTrue(cbb.hasSpace(1));
        cbb.add((byte) 1);
        assertFalse(cbb.hasSpace(1));
        assertEquals(1, cbb.read());
        assertTrue(cbb.hasSpace(1));
        cbb.add((byte) 2);
        assertFalse(cbb.hasSpace(1));
        assertEquals(2, cbb.read());
        assertTrue(cbb.hasSpace(1));
    }
}