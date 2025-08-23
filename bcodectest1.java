package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.stream.Stream;
import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests the lenient decoding behavior of the BCodec class.
 */
class BCodecTest {

    /**
     * Provides RFC 1522 B-encoded strings that contain malformed Base64 data.
     * <p>
     * The Base64 content in these strings is technically invalid because it includes
     * trailing bits that do not form a complete 8-bit byte. A lenient decoder is
     * expected to process these by decoding the valid parts and ignoring the
     * extraneous bits, whereas a strict decoder would throw an exception.
     * </p>
     *
     * @return A stream of malformed B-encoded strings.
     */
    private static Stream<String> provideMalformedBEncodedStrings() {
        return Stream.of(
            "=?ASCII?B?ZE==?=",
            "=?ASCII?B?ZmC=?=",
            "=?ASCII?B?Zm9vYE==?=",
            "=?ASCII?B?Zm9vYmC=?=",
            "=?ASCII?B?AB==?="
        );
    }

    /**
     * Tests that the default BCodec, which operates in lenient mode, does not throw
     * a {@link DecoderException} when decoding malformed Base64 strings.
     *
     * @param malformedInput A B-encoded string with invalid Base64 content.
     */
    @DisplayName("Decoding malformed Base64 in lenient mode should not throw an exception")
    @ParameterizedTest(name = "Input: {0}")
    @MethodSource("provideMalformedBEncodedStrings")
    void decodeInLenientModeWithMalformedInputShouldNotThrowException(final String malformedInput) {
        // Arrange: Create a BCodec with its default lenient decoding policy.
        final BCodec codec = new BCodec();
        assertFalse(codec.isStrictDecoding(), "Precondition: Codec must be in lenient mode for this test.");

        // Act & Assert: Decoding the malformed input should not throw an exception.
        // The lenient policy dictates that as much data as possible is decoded,
        // and invalid trailing bits are ignored.
        assertDoesNotThrow(() -> codec.decode(malformedInput),
            "A lenient BCodec should not throw a DecoderException for malformed input.");
    }
}