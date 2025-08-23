package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.Test;

public class RFC1522CodecTestTest2 {

    private void assertExpectedDecoderException(final String s) {
        assertThrows(DecoderException.class, () -> new RFC1522TestCodec().decodeText(s));
    }

    static class RFC1522TestCodec extends RFC1522Codec {

        RFC1522TestCodec() {
            super(StandardCharsets.UTF_8);
        }

        @Override
        protected byte[] doDecoding(final byte[] bytes) {
            return bytes;
        }

        @Override
        protected byte[] doEncoding(final byte[] bytes) {
            return bytes;
        }

        @Override
        protected String getEncoding() {
            return "T";
        }
    }

    @Test
    void testNullInput() throws Exception {
        final RFC1522TestCodec testCodec = new RFC1522TestCodec();
        assertNull(testCodec.decodeText(null));
        assertNull(testCodec.encodeText(null, CharEncoding.UTF_8));
    }
}
