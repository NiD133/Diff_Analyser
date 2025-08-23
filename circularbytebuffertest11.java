package org.apache.commons.io.input.buffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

public class CircularByteBufferTestTest11 {

    @Test
    void testPeekWithNegativeLength() {
        final CircularByteBuffer cbb = new CircularByteBuffer();
        final IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> cbb.peek(new byte[] { 1, 4, 3 }, 0, -1));
        assertEquals("Illegal length: -1", e.getMessage());
    }
}
