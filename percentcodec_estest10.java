package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertSame;

import java.nio.charset.StandardCharsets;

/**
 * Unit tests for the {@link PercentCodec} class.
 */
public class PercentCodecTest {

    /**
     * Tests that the encode() method returns the identical byte array instance
     * when the input contains only "safe" characters that do not require encoding.
     * This verifies an important optimization that avoids unnecessary array allocation.
     */
    @Test
    public void testEncodeWithOnlySafeBytesReturnsSameArrayInstance() throws Exception {
        // Arrange: Create a codec and an input byte array with characters that
        // the default PercentCodec instance should not encode.
        final PercentCodec percentCodec = new PercentCodec();
        final byte[] safeBytes = "abcdef-12345".getBytes(StandardCharsets.US_ASCII);

        // Act: Call the encode method.
        final byte[] encodedBytes = percentCodec.encode(safeBytes);

        // Assert: The returned array should be the same instance as the input array,
        // not just an equal one. This confirms the optimization is in place.
        assertSame(
            "The returned array should be the same instance as the input for performance optimization",
            safeBytes,
            encodedBytes
        );
    }
}