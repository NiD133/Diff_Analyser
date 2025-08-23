package org.apache.commons.codec.net;

import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * Test suite for the {@link URLCodec} class.
 */
public class URLCodecTest {

    /**
     * Tests that the decode method returns null when given a null input string.
     * This is the expected behavior for handling null inputs.
     */
    @Test
    public void decodeNullStringShouldReturnNull() throws Exception {
        // Arrange: Create an instance of the codec.
        final URLCodec urlCodec = new URLCodec();

        // Act: Call the decode method with a null input.
        final String result = urlCodec.decode(null);

        // Assert: Verify that the result is null.
        assertNull("Decoding a null string should result in null.", result);
    }
}