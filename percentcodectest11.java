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

public class PercentCodecTestTest11 {

    @Test
    void testPercentEncoderDecoderWithPlusForSpace() throws Exception {
        final String input = "a b c d";
        final PercentCodec percentCodec = new PercentCodec(null, true);
        final byte[] encoded = percentCodec.encode(input.getBytes(StandardCharsets.UTF_8));
        final String encodedS = new String(encoded, StandardCharsets.UTF_8);
        assertEquals("a+b+c+d", encodedS, "PercentCodec plus for space encoding test");
        final byte[] decode = percentCodec.decode(encoded);
        assertEquals(new String(decode, StandardCharsets.UTF_8), input, "PercentCodec plus for space decoding test");
    }
}
