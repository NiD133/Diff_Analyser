package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for PercentCodec focused on clarity and maintainability.
 */
class PercentCodecTest {

    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    private static byte[] bytes(final String s) {
        return s.getBytes(UTF_8);
    }

    private static String str(final byte[] bytes) {
        return new String(bytes, UTF_8);
    }

    @Test
    void encodesAndDecodesBasicAsciiUnchanged() throws Exception {
        final PercentCodec codec = new PercentCodec();
        final String input = "abcdABCD";

        final byte[] encoded = codec.encode(bytes(input));
        final byte[] decoded = codec.decode(encoded);

        assertEquals(input, str(encoded), "ASCII should be unchanged when encoding");
        assertEquals(input, str(decoded), "Round-trip decode should match original");
    }

    @Test
    @Disabled("Spaces are not encoded to %20 by default; enable if behavior/spec changes")
    void encodesSpaceAsPercent20_whenRequired() throws Exception {
        final PercentCodec codec = new PercentCodec();
        final String input = " ";

        final byte[] encoded = codec.encode(bytes(input));

        assertArrayEquals(bytes("%20"), encoded);
    }

    @Test
    void configurableAlwaysEncodeChars_arePercentEncoded_andRoundTripDecodes() throws Exception {
        final String input = "abc123_-.*\u03B1\u03B2"; // includes Greek alpha, beta
        final PercentCodec codec = new PercentCodec(bytes("abcdef"), false);

        final byte[] encoded = codec.encode(bytes(input));
        final byte[] decoded = codec.decode(encoded);

        assertEquals("%61%62%63123_-.*%CE%B1%CE%B2", str(encoded));
        assertEquals(input, str(decoded));
    }

    @Test
    void decodingTruncatedPercentSequence_throwsDecoderExceptionWithCause() throws Exception {
        final PercentCodec codec = new PercentCodec();
        final String input = "\u03B1\u03B2";

        final byte[] properlyEncoded = codec.encode(bytes(input));
        final byte[] truncated = Arrays.copyOf(properlyEncoded, properlyEncoded.length - 1); // drop last byte

        final DecoderException ex = assertThrows(DecoderException.class, () -> codec.decode(truncated));
        // Implementation detail: root cause is ArrayIndexOutOfBoundsException for truncated byte sequence.
        assertSame(ArrayIndexOutOfBoundsException.class, ex.getCause().getClass());
    }

    @Test
    void decodeObject_null_returnsNull() throws Exception {
        final PercentCodec codec = new PercentCodec();
        assertNull(codec.decode((Object) null));
    }

    @Test
    void decodeObject_unsupportedType_throwsDecoderException() {
        final PercentCodec codec = new PercentCodec();
        assertThrows(DecoderException.class, () -> codec.decode("test"));
    }

    @Test
    void encodeObject_null_returnsNull() throws Exception {
        final PercentCodec codec = new PercentCodec();
        assertNull(codec.encode((Object) null));
    }

    @Test
    void encodeObject_unsupportedType_throwsEncoderException() {
        final PercentCodec codec = new PercentCodec();
        assertThrows(EncoderException.class, () -> codec.encode("test"));
    }

    @Test
    void constructingWithNonAsciiAlwaysEncodeChars_throwsIllegalArgumentException() {
        final byte[] invalid = { (byte) -1, (byte) 'A' };
        assertThrows(IllegalArgumentException.class, () -> new PercentCodec(invalid, true));
    }

    @Test
    void nullAndEmptyInput_areHandledGracefully() throws Exception {
        final PercentCodec codec = new PercentCodec(null, true);

        // Nulls
        assertNull(codec.encode((byte[]) null), "Encoding null should return null");
        assertNull(codec.decode((byte[]) null), "Decoding null should return null");

        // Empty array
        final byte[] empty = bytes("");
        // Historically this test asserts reference equality (not just content) for encode(empty)
        assertSame(empty, codec.encode(empty), "Encoding empty array should return the same instance");
        assertArrayEquals(empty, codec.decode(empty), "Decoding empty array should return an empty array");
    }

    @Test
    void plusForSpace_encodesSpacesAsPlus_andDecodesBack() throws Exception {
        final PercentCodec codec = new PercentCodec(null, true);
        final String input = "a b c d";

        final byte[] encoded = codec.encode(bytes(input));
        final byte[] decoded = codec.decode(encoded);

        assertEquals("a+b+c+d", str(encoded), "Spaces should be encoded as '+' when plusForSpace is true");
        assertEquals(input, str(decoded), "Round-trip decode should match original");
    }

    @Test
    void safeAsciiChars_encodeDecodeViaObject_apiRoundTrips() throws Exception {
        final PercentCodec codec = new PercentCodec(null, true);
        final String input = "abc123_-.*";

        final Object encoded = codec.encode((Object) bytes(input));
        final Object decoded = codec.decode(encoded);

        assertEquals(input, str((byte[]) encoded), "Safe ASCII should be unchanged when encoding");
        assertEquals(input, str((byte[]) decoded), "Round-trip decode should match original");
    }

    @Test
    void unsafeChars_arePercentEncoded_andRoundTripDecodes() throws Exception {
        final PercentCodec codec = new PercentCodec();
        final String input = "\u03B1\u03B2\u03B3\u03B4\u03B5\u03B6% ";

        final byte[] encoded = codec.encode(bytes(input));
        final byte[] decoded = codec.decode(encoded);

        assertEquals("%CE%B1%CE%B2%CE%B3%CE%B4%CE%B5%CE%B6%25 ", str(encoded));
        assertEquals(input, str(decoded));
    }
}