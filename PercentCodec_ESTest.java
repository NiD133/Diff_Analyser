package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for PercentCodec.
 *
 * These tests avoid implementation details (like object identity of returned arrays)
 * and instead assert on observable behavior that future maintainers care about.
 */
public class PercentCodecTest {

    private static byte[] ascii(final String s) {
        return s.getBytes(StandardCharsets.US_ASCII);
    }

    // ----- Null and empty inputs -----

    @Test
    public void encode_null_returnsNull() throws EncoderException {
        PercentCodec codec = new PercentCodec();

        byte[] encoded = codec.encode((byte[]) null);

        assertNull(encoded);
    }

    @Test
    public void decode_null_returnsNull() throws DecoderException {
        PercentCodec codec = new PercentCodec();

        byte[] decoded = codec.decode((byte[]) null);

        assertNull(decoded);
    }

    @Test
    public void encode_empty_returnsEmpty() throws EncoderException {
        PercentCodec codec = new PercentCodec();

        byte[] encoded = codec.encode(new byte[0]);

        assertArrayEquals(new byte[0], encoded);
    }

    @Test
    public void decode_empty_returnsEmpty() throws DecoderException {
        PercentCodec codec = new PercentCodec();

        byte[] decoded = codec.decode(new byte[0]);

        assertArrayEquals(new byte[0], decoded);
    }

    // ----- Basic ASCII and non-ASCII behavior -----

    @Test
    public void encode_leaves_safeAscii_unchanged() throws EncoderException {
        PercentCodec codec = new PercentCodec();
        byte[] input = ascii("abcXYZ-_.");

        byte[] encoded = codec.encode(input);

        assertArrayEquals(input, encoded);
    }

    @Test
    public void encode_nonAscii_isPercentEncoded() throws EncoderException {
        PercentCodec codec = new PercentCodec();
        byte[] input = new byte[] { (byte) 0x8E, 0x00 }; // 0x8E is non-ASCII, 0x00 stays as is

        byte[] encoded = codec.encode(input);

        assertArrayEquals(new byte[] { '%', '8', 'E', 0x00 }, encoded);
    }

    @Test
    public void decode_percentTriplet_restoresNonAscii() throws DecoderException {
        PercentCodec codec = new PercentCodec();
        byte[] input = new byte[] { '%', '8', 'E', 0x00 };

        byte[] decoded = codec.decode(input);

        assertArrayEquals(new byte[] { (byte) 0x8E, 0x00 }, decoded);
    }

    @Test
    public void roundTrip_mixedAsciiAndNonAscii_preservesOriginal() throws EncoderException, DecoderException {
        PercentCodec codec = new PercentCodec();
        byte[] original = new byte[] { 'a', (byte) 0x8E, 'b', '%', 0x00 };

        byte[] encoded = codec.encode(original);
        byte[] decoded = codec.decode(encoded);

        assertArrayEquals(original, decoded);
    }

    // ----- plusForSpace option -----

    @Test
    public void encode_plusForSpaceTrue_encodesSpaceAsPlus_andDecodesBack() throws EncoderException, DecoderException {
        PercentCodec codec = new PercentCodec(new byte[0], true);
        byte[] input = ascii("a b c");

        byte[] encoded = codec.encode(input);
        assertArrayEquals(ascii("a+b+c"), encoded);

        byte[] decoded = codec.decode(encoded);
        assertArrayEquals(input, decoded);
    }

    // ----- Always-encode ASCII set -----

    @Test
    public void encode_respectsAlwaysEncodeChars() throws EncoderException, DecoderException {
        // Always encode '?'
        PercentCodec codec = new PercentCodec(new byte[] { (byte) '?' }, false);
        byte[] input = ascii("ab?cd");

        byte[] encoded = codec.encode(input);
        assertArrayEquals(ascii("ab%3Fcd"), encoded);

        byte[] decoded = codec.decode(encoded);
        assertArrayEquals(input, decoded);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_rejectsNegativeAlwaysEncodeByte() {
        // Only US-ASCII allowed (>= 0)
        new PercentCodec(new byte[] { (byte) -1 }, true);
    }

    // ----- Object-based API -----

    @Test
    public void encode_object_null_returnsNull() throws EncoderException {
        PercentCodec codec = new PercentCodec();

        Object encoded = codec.encode((Object) null);

        assertNull(encoded);
    }

    @Test
    public void decode_object_null_returnsNull() throws DecoderException {
        PercentCodec codec = new PercentCodec();

        Object decoded = codec.decode((Object) null);

        assertNull(decoded);
    }

    @Test
    public void encode_object_wrongType_throwsEncoderException() {
        PercentCodec codec = new PercentCodec();

        try {
            codec.encode(new Object());
            fail("Expected EncoderException for non-byte[] input");
        } catch (EncoderException expected) {
            // ok
        }
    }

    @Test
    public void decode_object_wrongType_throwsDecoderException() {
        PercentCodec codec = new PercentCodec();

        try {
            codec.decode(new Object());
            fail("Expected DecoderException for non-byte[] input");
        } catch (DecoderException expected) {
            // ok
        }
    }

    // ----- Invalid input -----

    @Test
    public void decode_withTrailingPercent_throwsDecoderException() {
        PercentCodec codec = new PercentCodec();
        byte[] bad = ascii("ABC%"); // incomplete percent triplet

        try {
            codec.decode(bad);
            fail("Expected DecoderException for invalid percent sequence");
        } catch (DecoderException expected) {
            // ok
        }
    }
}