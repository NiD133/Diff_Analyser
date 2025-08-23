package org.apache.commons.codec.net;

import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * Unit tests for the {@link QuotedPrintableCodec} class, focusing on the handling of null inputs.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that {@link QuotedPrintableCodec#decode(String, String)} returns null
     * when the input string to be decoded is null.
     */
    @Test
    public void decodeWithNullStringShouldReturnNull() throws Exception {
        // Arrange
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String nullInputString = null;
        String charsetName = "UTF-8"; // Charset is irrelevant when the input string is null.

        // Act
        String result = codec.decode(nullInputString, charsetName);

        // Assert
        assertNull("Decoding a null string should result in null.", result);
    }
}