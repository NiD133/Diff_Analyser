package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class PercentCodecTestTest10 {

    @Test
    void testPercentEncoderDecoderWithNullOrEmptyInput() throws Exception {
        final PercentCodec percentCodec = new PercentCodec(null, true);
        assertNull(percentCodec.encode(null), "Null input value encoding test");
        assertNull(percentCodec.decode(null), "Null input value decoding test");
        final byte[] emptyInput = "".getBytes(StandardCharsets.UTF_8);
        assertEquals(percentCodec.encode(emptyInput), emptyInput, "Empty input value encoding test");
        assertArrayEquals(percentCodec.decode(emptyInput), emptyInput, "Empty input value decoding test");
    }
}
