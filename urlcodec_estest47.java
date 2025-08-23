package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

import java.nio.charset.StandardCharsets;
import java.util.BitSet;
import org.apache.commons.codec.DecoderException;

/**
 * Contains tests for the static utility methods in {@link URLCodec}.
 */
public class URLCodecStaticTest {

    /**
     * Tests that decoding a byte array containing a plus sign correctly converts it to a space.
     * It then tests that re-encoding this result (a space and several null bytes) correctly
     * converts the space back to a plus sign and percent-encodes the null bytes.
     * This verifies the round-trip behavior for special WWW-Form-URL characters.
     */
    @Test
    public void testDecodePlusAndEncodeSpaceWithNullBytes() throws DecoderException {
        //
        // Part 1: Test decoding of '+' to space
        //

        // Arrange: Create a byte array representing "+", followed by 7 null bytes.
        byte[] urlEncodedBytes = new byte[] {
            (byte) '+', 0, 0, 0, 0, 0, 0, 0
        };
        // The expected result is a space character followed by the same null bytes.
        byte[] expectedDecodedBytes = new byte[] {
            (byte) ' ', 0, 0, 0, 0, 0, 0, 0
        };

        // Act: Decode the URL-encoded byte array.
        byte[] actualDecodedBytes = URLCodec.decodeUrl(urlEncodedBytes);

        // Assert: Verify that the '+' was correctly decoded to a space.
        assertArrayEquals(expectedDecodedBytes, actualDecodedBytes);

        //
        // Part 2: Test re-encoding of the decoded result
        //

        // Arrange: The space should be encoded back to '+', and each null byte (0x00)
        // should be percent-encoded as "%00".
        byte[] expectedReEncodedBytes = "+%00%00%00%00%00%00%00".getBytes(StandardCharsets.US_ASCII);

        // Act: Re-encode the decoded bytes using the standard WWW-FORM-URL safe characters.
        BitSet wwwFormUrlSafe = URLCodec.WWW_FORM_URL;
        byte[] actualReEncodedBytes = URLCodec.encodeUrl(wwwFormUrlSafe, actualDecodedBytes);

        // Assert: Verify the re-encoded result matches the expected byte array.
        assertArrayEquals(expectedReEncodedBytes, actualReEncodedBytes);
    }
}