package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the handling of null inputs by the {@link BCodec}.
 */
class BCodecTest {

    private BCodec bCodec;

    @BeforeEach
    void setUp() {
        // A new BCodec instance is created before each test for isolation.
        this.bCodec = new BCodec();
    }

    @Test
    void testEncodeNullReturnsNull() throws EncoderException {
        // The StringEncoder contract specifies that encoding a null string should return null.
        assertNull(bCodec.encode(null));
    }

    @Test
    void testDecodeNullReturnsNull() throws DecoderException {
        // The StringDecoder contract specifies that decoding a null string should return null.
        assertNull(bCodec.decode(null));
    }
}