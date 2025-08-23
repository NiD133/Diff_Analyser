package org.apache.commons.io.input.buffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

public class CircularByteBufferTestTest4 {

    @Test
    void testAddNullBuffer() {
        final CircularByteBuffer cbb = new CircularByteBuffer();
        assertThrows(NullPointerException.class, () -> cbb.add(null, 0, 3));
    }
}