package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link BCodec} class.
 */
public class BCodecTest {

    /**
     * Tests that decoding a null Object input returns null, which is the expected
     * behavior according to the Decoder interface contract.
     *
     * @see BCodec#decode(Object)
     */
    @Test
    public void decodeObjectWithNullInputShouldReturnNull() throws DecoderException {
        // Arrange
        BCodec bCodec = new BCodec();

        // Act
        Object result = bCodec.decode((Object) null);

        // Assert
        assertNull("Decoding a null object should result in a null value.", result);
    }
}