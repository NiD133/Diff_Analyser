package org.apache.commons.codec.binary;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link BinaryCodec}.
 */
public class BinaryCodecTest {

    /**
     * Tests that decoding a null input results in a canonical empty byte array,
     * and that this result remains the same instance after a subsequent encode/decode cycle.
     * This verifies that the codec uses a singleton instance for empty results.
     */
    @Test
    public void decodeNullThenEncodeAndDecodeShouldReturnSameEmptyArrayInstance() throws DecoderException, EncoderException {
        // Arrange: Create a BinaryCodec instance.
        BinaryCodec codec = new BinaryCodec();

        // Act:
        // 1. Decode a null object.
        Object decodedFromNull = codec.decode((Object) null);

        // 2. Encode the result from the previous step.
        Object encodedEmptyResult = codec.encode(decodedFromNull);

        // 3. Decode the encoded result.
        Object finalDecodedResult = codec.decode(encodedEmptyResult);

        // Assert:
        // First, verify the initial decoding of null produces a valid, empty byte array.
        assertNotNull("Decoding null should not return null.", decodedFromNull);
        assertTrue("The decoded object should be a byte array.", decodedFromNull instanceof byte[]);
        assertEquals("The byte array from a null decode should be empty.", 0, ((byte[]) decodedFromNull).length);

        // Finally, assert that the final result is the *exact same instance* as the initial result.
        // This confirms the codec's use of a canonical, static empty array for null/empty inputs.
        assertSame("The final decoded object should be the same instance as the one from decoding null.",
                decodedFromNull, finalDecodedResult);
    }
}