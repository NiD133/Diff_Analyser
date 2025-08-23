package org.apache.commons.codec.net;

import org.apache.commons.codec.CodecPolicy;
import org.junit.Test;

import java.nio.charset.Charset;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link BCodec}.
 */
public class BCodecTest {

    /**
     * Tests that a BCodec instance created with the STRICT policy
     * correctly reports that it is in strict decoding mode.
     */
    @Test
    public void testIsStrictDecodingReturnsTrueWhenConstructedWithStrictPolicy() {
        // Arrange
        final CodecPolicy policy = CodecPolicy.STRICT;
        final BCodec bCodec = new BCodec(Charset.defaultCharset(), policy);

        // Act
        final boolean isStrict = bCodec.isStrictDecoding();

        // Assert
        assertTrue("isStrictDecoding() should return true for a codec constructed with STRICT policy.", isStrict);
    }

    /**
     * Tests that a BCodec instance created with the LENIENT policy
     * correctly reports that it is not in strict decoding mode.
     * This is the default behavior.
     */
    @Test
    public void testIsStrictDecodingReturnsFalseWhenConstructedWithLenientPolicy() {
        // Arrange
        final CodecPolicy policy = CodecPolicy.LENIENT;
        final BCodec bCodec = new BCodec(Charset.defaultCharset(), policy);

        // Act
        final boolean isStrict = bCodec.isStrictDecoding();

        // Assert
        assertFalse("isStrictDecoding() should return false for a codec constructed with LENIENT policy.", isStrict);
    }
}