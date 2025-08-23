package org.apache.commons.io.input.buffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

public class CircularByteBufferTestTest5 {

    /**
     * Tests for add function with 3 arguments of type byte[], int and int.
     */
    @Test
    void testAddValidData() {
        final CircularByteBuffer cbb = new CircularByteBuffer();
        final int length = 3;
        cbb.add(new byte[] { 3, 6, 9 }, 0, length);
        assertEquals(length, cbb.getCurrentNumberOfBytes());
    }
}
