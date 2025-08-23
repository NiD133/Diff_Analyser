package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * Tests for {@link RFC1522Codec} focusing on handling of malformed input.
 */
public class RFC1522CodecTest {

    /**
     * Verifies that the decodeText method throws a StringIndexOutOfBoundsException
     * when given a string that is too short to be a valid RFC 1522 encoded-word.
     *
     * An RFC 1522 encoded-word must follow the format "=?charset?encoding?encoded-text?=".
     * The input "=?=" is syntactically incorrect as it lacks the required sections,
     * which should cause a parsing failure.
     */
    @Test(expected = StringIndexOutOfBoundsException.class)
    public void decodeTextWithTruncatedInputShouldThrowException() throws DecoderException, UnsupportedEncodingException {
        // Arrange
        final BCodec codec = new BCodec();
        final String truncatedInput = "=?=";

        // Act: Attempt to decode the malformed string.
        // The internal parsing logic is expected to fail with an out-of-bounds access
        // due to the input's insufficient length.
        codec.decodeText(truncatedInput);

        // Assert: The test passes if the expected StringIndexOutOfBoundsException is thrown.
        // Note: A DecoderException might be more appropriate for this type of error,
        // but this test validates the current implementation's behavior.
    }
}