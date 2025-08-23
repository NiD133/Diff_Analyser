package org.apache.commons.io.input.buffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

public class CircularByteBufferTestTest7 {

    @Test
    void testHasSpace() {
        final CircularByteBuffer cbb = new CircularByteBuffer(1);
        assertTrue(cbb.hasSpace());
        cbb.add((byte) 1);
        assertFalse(cbb.hasSpace());
        assertEquals(1, cbb.read());
        assertTrue(cbb.hasSpace());
        cbb.add((byte) 2);
        assertFalse(cbb.hasSpace());
        assertEquals(2, cbb.read());
        assertTrue(cbb.hasSpace());
    }
}
