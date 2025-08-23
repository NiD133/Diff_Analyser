package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

// Renamed from PercentCodecTestTest11 to follow standard naming conventions.
class PercentCodecTest {

    /**
     * Tests that the PercentCodec correctly handles the 'plusForSpace' option
     * for both encoding and decoding, ensuring a successful round-trip.
     */
    @Test
    void shouldEncodeSpacesAsPlusAndDecodeBackWhenPlusForSpaceIsEnabled() throws EncoderException, DecoderException {
        // Arrange
        final String originalString = "a b c d";
        final String expectedEncodedString = "a+b+c+d";
        // Instantiate codec with the 'plusForSpace' option enabled.
        final PercentCodec percentCodec = new PercentCodec(null, true);

        // Act: Encode the string
        final byte[] encodedBytes = percentCodec.encode(originalString.getBytes(StandardCharsets.UTF_8));
        final String actualEncodedString = new String(encodedBytes, StandardCharsets.UTF_8);

        // Assert: Verify that spaces were encoded to pluses
        assertEquals(expectedEncodedString, actualEncodedString);

        // Act: Decode the encoded bytes back to the original form
        final byte[] decodedBytes = percentCodec.decode(encodedBytes);
        final String roundTripString = new String(decodedBytes, StandardCharsets.UTF_8);

        // Assert: Verify that the decoded string matches the original
        assertEquals(originalString, roundTripString);
    }
}