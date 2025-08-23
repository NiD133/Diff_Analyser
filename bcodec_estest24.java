package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link BCodec}.
 */
public class BCodecTest {

    /**
     * Tests that a BCodec created with the default constructor has lenient decoding
     * enabled, meaning strict decoding is false.
     */
    @Test
    public void isStrictDecodingShouldBeFalseForDefaultCodec() {
        // Arrange: Create a BCodec instance using the default constructor.
        // The default behavior is lenient decoding.
        final BCodec codec = new BCodec();

        // Act: Check if the codec is in strict decoding mode.
        final boolean isStrict = codec.isStrictDecoding();

        // Assert: The result should be false, as the default policy is lenient.
        assertFalse("Default BCodec should use lenient, not strict, decoding.", isStrict);
    }
}