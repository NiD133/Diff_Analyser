package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link QuotedPrintableCodec}.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that decoding a null Object returns null, which is the expected behavior
     * for a null-safe decoder.
     *
     * @throws DecoderException This exception should not be thrown in this test case.
     */
    @Test
    public void decodeObjectWithNullInputShouldReturnNull() throws DecoderException {
        // Arrange
        // The 'strict' parameter does not affect the outcome for a null input.
        QuotedPrintableCodec codec = new QuotedPrintableCodec(true);

        // Act
        Object result = codec.decode((Object) null);

        // Assert
        assertNull("Decoding a null object should result in null.", result);
    }
}