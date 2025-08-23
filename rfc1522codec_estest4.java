package org.apache.commons.codec.net;

import static org.junit.Assert.assertNull;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * Tests for the abstract RFC1522Codec class.
 *
 * <p>This test suite uses a concrete implementation (QCodec) to test
 * the functionality provided by the abstract base class.</p>
 */
public class RFC1522CodecTest {

    // A concrete implementation is needed to test the abstract class.
    private final RFC1522Codec codec = new QCodec();

    /**
     * Tests that the decodeText method returns null when the input is null.
     * This is the expected behavior for handling null inputs gracefully.
     */
    @Test
    public void decodeText_withNullInput_shouldReturnNull() throws DecoderException, UnsupportedEncodingException {
        // Act: Call the method under test with a null input.
        final String result = codec.decodeText(null);

        // Assert: Verify that the result is null.
        assertNull("Decoding a null string should result in null.", result);
    }
}