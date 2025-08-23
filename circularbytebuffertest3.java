package org.apache.commons.io.input.buffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

public class CircularByteBufferTestTest3 {

    @Test
    void testAddNegativeLength() {
        final CircularByteBuffer cbb = new CircularByteBuffer();
        final byte[] targetBuffer = { 1, 2, 3 };
        assertThrows(IllegalArgumentException.class, () -> cbb.add(targetBuffer, 0, -1));
    }
}
