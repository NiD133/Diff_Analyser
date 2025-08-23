package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.Test;

public class RFC1522CodecTestTest1 {

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
    void testDecodeInvalid() throws Exception {
        assertExpectedDecoderException("whatever");
        assertExpectedDecoderException("=?");
        assertExpectedDecoderException("?=");
        assertExpectedDecoderException("==");
        assertExpectedDecoderException("=??=");
        assertExpectedDecoderException("=?stuff?=");
        assertExpectedDecoderException("=?UTF-8??=");
        assertExpectedDecoderException("=?UTF-8?stuff?=");
        assertExpectedDecoderException("=?UTF-8?T?stuff");
        assertExpectedDecoderException("=??T?stuff?=");
        assertExpectedDecoderException("=?UTF-8??stuff?=");
        assertExpectedDecoderException("=?UTF-8?W?stuff?=");
    }
}
