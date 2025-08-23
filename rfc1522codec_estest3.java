package org.apache.commons.codec.net;

import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

/**
 * Tests for the {@link BCodec} class, focusing on behaviors inherited from {@link RFC1522Codec}.
 */
public class BCodecTest {

    /**
     * Tests that encoding a null text string using {@link BCodec#encodeText(String, String)}
     * returns null, as the method contract specifies that null input should result in null output.
     */
    @Test
    public void testEncodeTextWithNullInputShouldReturnNull() throws EncoderException {
        // Arrange: Create a BCodec instance. The charset is specified but will not be used
        // because the method returns early when the input text is null.
        final BCodec bCodec = new BCodec();
        final String charsetName = "UTF-8";

        // Act: Call the encodeText method with a null input string.
        final String encodedText = bCodec.encodeText(null, charsetName);

        // Assert: Verify that the result is null.
        assertNull("Encoding a null string should return null.", encodedText);
    }
}