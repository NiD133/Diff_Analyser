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

public class PercentCodecTestTest4 {

    @Test
    void testDecodeInvalidEncodedResultDecoding() throws Exception {
        final String inputS = "\u03B1\u03B2";
        final PercentCodec percentCodec = new PercentCodec();
        final byte[] encoded = percentCodec.encode(inputS.getBytes(StandardCharsets.UTF_8));
        try {
            // exclude one byte
            percentCodec.decode(Arrays.copyOf(encoded, encoded.length - 1));
        } catch (final Exception e) {
            assertTrue(DecoderException.class.isInstance(e) && ArrayIndexOutOfBoundsException.class.isInstance(e.getCause()));
        }
    }
}
