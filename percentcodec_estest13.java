package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertNull;
import org.apache.commons.codec.DecoderException;

/**
 * Unit tests for the PercentCodec class.
 */
public class PercentCodecTest {

    /**
     * Tests that decoding a null Object returns null, as per the contract of the
     * {@link org.apache.commons.codec.Decoder} interface.
     */
    @Test
    public void decodeNullObjectShouldReturnNull() throws DecoderException {
        // Arrange
        // The specific configuration of the codec does not affect this test case,
        // so a default instance is sufficient.
        PercentCodec codec = new PercentCodec();

        // Act
        Object result = codec.decode((Object) null);

        // Assert
        assertNull("Decoding a null object should result in null.", result);
    }
}