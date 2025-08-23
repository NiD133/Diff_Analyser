package org.apache.commons.codec.net;

import org.apache.commons.codec.EncoderException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link BCodec}.
 *
 * Note: The original test class name `BCodec_ESTestTest13` and its scaffolding
 * suggest it was auto-generated. This version uses a more conventional name and structure.
 */
public class BCodecTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Tests that calling encode() with a null charset name string results in an
     * IllegalArgumentException. This is the expected behavior from the underlying
     * java.nio.charset.Charset.forName(null) call.
     */
    @Test
    public void encodeWithNullCharsetNameThrowsIllegalArgumentException() throws EncoderException {
        // Arrange
        final BCodec bCodec = new BCodec();
        final String inputText = "any string";
        final String nullCharsetName = null;

        // Assert: Define the expected exception and its message.
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Null charset name");

        // Act: Call the method that should throw the configured exception.
        bCodec.encode(inputText, nullCharsetName);
    }
}