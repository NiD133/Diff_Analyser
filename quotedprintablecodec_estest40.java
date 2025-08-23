package org.apache.commons.codec.net;

import static org.junit.Assert.assertNull;

import java.nio.charset.Charset;
import org.apache.commons.codec.DecoderException;
import org.junit.Test;

// Note: The class name and inheritance are kept from the original for context.
// In a real-world scenario, this class would likely be renamed to QuotedPrintableCodecTest.
public class QuotedPrintableCodec_ESTestTest40 extends QuotedPrintableCodec_ESTest_scaffolding {

    /**
     * Tests that the decode(String, Charset) method correctly handles a null input
     * by returning null, as per its contract.
     */
    @Test
    public void decodeWithNullStringShouldReturnNull() throws DecoderException {
        // Arrange
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        final String nullInput = null;
        // The charset is required by the method signature but is not used when the input is null.
        final Charset charset = Charset.defaultCharset();

        // Act
        final String result = codec.decode(nullInput, charset);

        // Assert
        assertNull("Decoding a null string should return null.", result);
    }
}