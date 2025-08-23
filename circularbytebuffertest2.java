package org.apache.commons.io.input.buffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

public class CircularByteBufferTestTest2 {

    @Test
    void testAddInvalidOffset() {
        final CircularByteBuffer cbb = new CircularByteBuffer();
        assertThrows(IllegalArgumentException.class, () -> cbb.add(new byte[] { 1, 2, 3 }, -1, 3));
    }
}