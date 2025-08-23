package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Contains tests for the {@link PercentCodec} class, focusing on specific encoding and decoding scenarios.
 */
public class PercentCodec_ESTestTest18 extends PercentCodec_ESTest_scaffolding {

    /**
     * Tests that the 'plusForSpace' behavior (encoding space as '+' and decoding '+' as space)
     * takes precedence over the 'alwaysEncodeChars' list.
     *
     * <p>This test configures the codec to both treat '+' as a character that should be
     * percent-encoded (to "%2B") and to use '+' as an encoding for the space character.
     * It verifies that the 'plusForSpace' rule is applied, correctly decoding '+' to a space
     * and then re-encoding the space back to '+'.</p>
     */
    @Test
    public void plusForSpaceFlagShouldHavePrecedenceOverAlwaysEncodeList() throws Exception {
        // Arrange: Configure a codec where '+' is in the 'alwaysEncodeChars' list,
        // but 'plusForSpace' is also enabled.
        final byte[] alwaysEncodeChars = {(byte) '+'};
        final boolean plusForSpace = true;
        final PercentCodec codec = new PercentCodec(alwaysEncodeChars, plusForSpace);

        final byte[] plusSign = {(byte) '+'};
        final byte[] space = {(byte) ' '};

        // Act:
        // 1. Decode the plus sign. With 'plusForSpace' enabled, it should become a space.
        final byte[] decodedBytes = codec.decode(plusSign);

        // 2. Re-encode the resulting space. It should be encoded back to a plus sign.
        final byte[] reEncodedBytes = codec.encode(decodedBytes);

        // Assert:
        // Verify that '+' was correctly decoded to a space.
        assertArrayEquals("Decoding '+' with plusForSpace should yield a space", space, decodedBytes);

        // Verify that the space was encoded back to '+', not percent-encoded to "%2B",
        // confirming that the 'plusForSpace' rule has precedence.
        assertArrayEquals("Encoding a space with plusForSpace should yield '+'", plusSign, reEncodedBytes);
    }
}