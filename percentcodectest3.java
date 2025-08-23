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

public class PercentCodecTestTest3 {

    @Test
    void testConfigurablePercentEncoder() throws Exception {
        final String input = "abc123_-.*\u03B1\u03B2";
        final PercentCodec percentCodec = new PercentCodec("abcdef".getBytes(StandardCharsets.UTF_8), false);
        final byte[] encoded = percentCodec.encode(input.getBytes(StandardCharsets.UTF_8));
        final String encodedS = new String(encoded, StandardCharsets.UTF_8);
        assertEquals("%61%62%63123_-.*%CE%B1%CE%B2", encodedS, "Configurable PercentCodec encoding test");
        final byte[] decoded = percentCodec.decode(encoded);
        assertEquals(new String(decoded, StandardCharsets.UTF_8), input, "Configurable PercentCodec decoding test");
    }
}
