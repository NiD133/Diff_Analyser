package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.CodecPolicy;
import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link BCodec} focusing on strict decoding behavior.
 */
public class BCodecTest {

    /**
     * Verifies that the BCodec, when configured with a STRICT policy, throws a
     * {@link DecoderException} for inputs that are not valid Base64 encoded strings
     * according to the strict decoding rules.
     * <p>
     * Lenient decoding might process these inputs by ignoring trailing bits or characters,
     * but strict decoding requires the input to be perfectly formed.
     * </p>
     *
     * @param malformedEncodedWord An RFC 1522 encoded-word containing a malformed Base64 payload.
     */
    @ParameterizedTest(name = "run #{index} with input={0}")
    @ValueSource(strings = {
        // Malformed due to having 1 leftover byte (not a multiple of 4 Base64 characters)
        "=?ASCII?B?ZmC=?=",
        "=?ASCII?B?Zm9vYmC=?=",
        // Malformed due to having leftover bits that don't form a full byte
        "=?ASCII?B?AB==?=",
        // Malformed due to extra non-whitespace characters after the Base64 padding
        "=?ASCII?B?ZE==?=",
        "=?ASCII?B?Zm9vYE==?="
    })
    void decodeWithStrictPolicyThrowsExceptionForMalformedBase64(final String malformedEncodedWord) {
        // Arrange
        final BCodec codec = new BCodec(StandardCharsets.UTF_8, CodecPolicy.STRICT);
        assertTrue(codec.isStrictDecoding(), "Precondition failed: Codec should be in strict decoding mode.");

        // Act & Assert
        assertThrows(DecoderException.class, () -> {
            codec.decode(malformedEncodedWord);
        }, "Strict decoding should fail for malformed Base64 input: " + malformedEncodedWord);
    }
}