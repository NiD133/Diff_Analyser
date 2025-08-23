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

public class PercentCodecTestTest1 {

    @Test
    void testBasicEncodeDecode() throws Exception {
        final PercentCodec percentCodec = new PercentCodec();
        final String input = "abcdABCD";
        final byte[] encoded = percentCodec.encode(input.getBytes(StandardCharsets.UTF_8));
        final String encodedS = new String(encoded, StandardCharsets.UTF_8);
        final byte[] decoded = percentCodec.decode(encoded);
        final String decodedS = new String(decoded, StandardCharsets.UTF_8);
        assertEquals(input, encodedS, "Basic PercentCodec encoding test");
        assertEquals(input, decodedS, "Basic PercentCodec decoding test");
    }
}
