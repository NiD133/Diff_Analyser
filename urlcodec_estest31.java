package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

import java.nio.charset.StandardCharsets;

/**
 * This class contains improved tests for the {@link URLCodec} class.
 */
public class URLCodecTest {

    /**
     * Tests that the static {@code encodeUrl} method correctly percent-encodes bytes
     * that are not in the provided "safe" character set.
     *
     * <p>This improved test verifies the actual content of the encoded byte array, ensuring
     * each unsafe byte is converted to a three-character sequence: '%' followed by its
     * two-digit hexadecimal representation.</p>
     */
    @Test
    public void encodeUrlShouldCorrectlyPercentEncodeUnsafeBytes() {
        // Arrange: Define a set of bytes that are not considered safe for URLs
        // and must be percent-encoded. The value -93 from the original test is
        // 0xA3 in hexadecimal.
        final byte[] unsafeBytes = {0x00, (byte) 0xA3, (byte) 0xFF};

        // The expected output is the US-ASCII byte representation of the
        // percent-encoded string. Each byte is converted to a '%' character
        // followed by its two-digit uppercase hex value.
        // 0x00 -> "%00"
        // 0xA3 -> "%A3"
        // 0xFF -> "%FF"
        final byte[] expectedEncoding = "%00%A3%FF".getBytes(StandardCharsets.US_ASCII);

        // Act: Encode the unsafe bytes using the standard WWW-form-URL safe character set.
        final byte[] actualEncoding = URLCodec.encodeUrl(URLCodec.WWW_FORM_URL, unsafeBytes);

        // Assert: Verify that the encoded output matches the expected format and content.
        assertArrayEquals(expectedEncoding, actualEncoding);
    }
}