package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link QuotedPrintableCodec} class.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that decoding an empty string correctly results in an empty string.
     */
    @Test
    public void decodeEmptyStringShouldReturnEmptyString() throws DecoderException {
        // Arrange: Create a codec instance and the input string.
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "";

        // Act: Perform the decoding operation.
        String decodedString = codec.decode(input);

        // Assert: Verify that the output is an empty string.
        assertEquals("Decoding an empty string should yield an empty string.", "", decodedString);
    }
}