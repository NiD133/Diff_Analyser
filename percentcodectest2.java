package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link PercentCodec}.
 *
 * Note: The original test class was named PercentCodecTestTest2. It has been
 * renamed to follow standard naming conventions.
 */
public class PercentCodecTest {

    /**
     * This test verifies that the default PercentCodec instance correctly encodes a
     * space character into its percent-encoded form, "%20".
     *
     * The default constructor of {@link PercentCodec} sets {@code plusForSpace} to false,
     * which dictates this behavior according to RFC 3986. This test was previously
     * disabled and marked for removal, but it validates a core, non-trivial behavior
     * of the default configuration, so it has been re-enabled and clarified.
     */
    @Test
    @DisplayName("Default PercentCodec should encode a space character as %20")
    void shouldEncodeSpaceAsPercent20ByDefault() throws EncoderException {
        // Arrange
        final PercentCodec codec = new PercentCodec(); // By default, plusForSpace is false.
        final byte[] originalBytes = " ".getBytes(StandardCharsets.UTF_8);
        final byte[] expectedBytes = "%20".getBytes(StandardCharsets.UTF_8);

        // Act
        final byte[] encodedBytes = codec.encode(originalBytes);

        // Assert
        assertArrayEquals(expectedBytes, encodedBytes, "A space should be encoded as %20 by the default codec.");
    }
}