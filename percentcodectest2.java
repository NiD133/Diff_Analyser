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

public class PercentCodecTestTest2 {

    @Test
    // TODO Should be removed?
    @Disabled
    void testBasicSpace() throws Exception {
        final PercentCodec percentCodec = new PercentCodec();
        final String input = " ";
        final byte[] encoded = percentCodec.encode(input.getBytes(StandardCharsets.UTF_8));
        assertArrayEquals("%20".getBytes(StandardCharsets.UTF_8), encoded);
    }
}
