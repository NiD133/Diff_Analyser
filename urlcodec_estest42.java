package org.apache.commons.codec.net;

import static org.junit.Assert.assertNull;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

/**
 * Unit tests for the {@link URLCodec} class.
 */
public class URLCodecTest {

    /**
     * Tests that the decode(Object) method returns null when the input is null.
     * This is the expected behavior according to the method's contract for handling
     * null inputs.
     */
    @Test
    public void decodeObjectWithNullInputShouldReturnNull() throws DecoderException {
        // Arrange
        URLCodec urlCodec = new URLCodec();
        Object nullInput = null;

        // Act
        Object result = urlCodec.decode(nullInput);

        // Assert
        assertNull("Decoding a null object should result in a null value.", result);
    }
}