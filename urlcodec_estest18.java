package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link URLCodec}.
 */
public class URLCodecTest {

    /**
     * Tests that decoding a plus sign '+' correctly results in a space character ' ',
     * as per the www-form-urlencoded specification where spaces are encoded as pluses.
     */
    @Test
    public void decodePlusSignShouldReturnSpace() throws DecoderException {
        // Arrange
        final URLCodec urlCodec = new URLCodec();
        final String encodedPlus = "+";
        final String expectedSpace = " ";

        // Act
        final String decodedString = urlCodec.decode(encodedPlus);

        // Assert
        assertEquals(expectedSpace, decodedString);
    }
}