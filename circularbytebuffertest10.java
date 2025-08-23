package org.apache.commons.io.input.buffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

public class CircularByteBufferTestTest10 {

    @Test
    void testPeekWithInvalidOffset() {
        final CircularByteBuffer cbb = new CircularByteBuffer();
        final IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> cbb.peek(new byte[] { 2, 4, 6, 8, 10 }, -1, 5));
        assertEquals("Illegal offset: -1", e.getMessage());
    }
}
