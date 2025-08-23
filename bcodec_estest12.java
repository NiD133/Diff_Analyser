package org.apache.commons.codec.net;

import java.nio.charset.IllegalCharsetNameException;
import org.apache.commons.codec.EncoderException;
import org.junit.Test;

/**
 * Test suite for the exception handling in {@link BCodec}.
 */
public class BCodecTest {

    /**
     * Tests that calling {@link BCodec#encode(String, String)} with an empty
     * string for the charset name throws an {@link IllegalCharsetNameException}.
     * <p>
     * This behavior is expected because the underlying {@link java.nio.charset.Charset#forName(String)}
     * method, which {@code BCodec} uses, throws this exception for invalid charset names.
     * </p>
     * @throws EncoderException if the encoding process fails for a reason other than the tested exception.
     */
    @Test(expected = IllegalCharsetNameException.class)
    public void encodeWithEmptyCharsetNameShouldThrowException() throws EncoderException {
        // Arrange: Create a BCodec instance and define the input string and the invalid charset.
        final BCodec codec = new BCodec();
        final String inputText = "This is some sample text.";
        final String emptyCharsetName = "";

        // Act & Assert: Attempt to encode the string with the empty charset name.
        // The test framework will verify that an IllegalCharsetNameException is thrown.
        codec.encode(inputText, emptyCharsetName);
    }
}