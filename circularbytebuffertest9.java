package org.apache.commons.io.input.buffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

public class CircularByteBufferTestTest9 {

    @Test
    void testPeekWithExcessiveLength() {
        assertFalse(new CircularByteBuffer().peek(new byte[] { 1, 3, 5, 7, 9 }, 0, 6));
    }
}
