package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.BitSet;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for URLCodec.
 *
 * These tests favor clarity over corner-case exhaustiveness:
 * - Grouped by feature (encoding/decoding, charsets, null/empty inputs, object overloads).
 * - Self-explanatory test names.
 * - Arrange-Act-Assert structure.
 */
public class URLCodecTest {

    private static final String UTF_8 = "UTF-8";
    private static final String UNSUPPORTED_CHARSET = "no-such-charset-xyz";

    /* ---------------------------------------------------------------------
     * Encoding basics
     * ------------------------------------------------------------------- */

    @Test
    public void encode_leavesSafeCharsButEscapesSemicolon() throws Exception {
        // Arrange
        URLCodec codec = new URLCodec();

        // Act
        String encoded = codec.encode("VoM;");

        // Assert
        assertEquals("VoM%3B", encoded);
    }

    @Test
    public void encode_replacesSpaceWithPlus() throws Exception {
        // Arrange
        URLCodec codec = new URLCodec();

        // Act
        String encoded = codec.encode("a b");

        // Assert
        assertEquals("a+b", encoded);
    }

    @Test
    public void encode_escapesPlusSign() throws Exception {
        // Arrange
        URLCodec codec = new URLCodec();

        // Act
        String encoded = codec.encode("a+b");

        // Assert
        assertEquals("a%2Bb", encoded);
    }

    /* ---------------------------------------------------------------------
     * Decoding basics
     * ------------------------------------------------------------------- */

    @Test
    public void decode_convertsPlusToSpace() throws Exception {
        // Arrange
        URLCodec codec = new URLCodec();

        // Act
        String decoded = codec.decode("+");

        // Assert
        assertEquals(" ", decoded);
    }

    @Test
    public void decode_withExplicitCharset_roundTripsUnicode() throws Exception {
        // Arrange
        URLCodec codec = new URLCodec(UTF_8);
        String original = "Hello, 世界";

        // Act
        String encoded = codec.encode(original, UTF_8);
        String decoded = codec.decode(encoded, UTF_8);

        // Assert
        assertEquals(original, decoded);
    }

    /* ---------------------------------------------------------------------
     * Byte[] round-trips
     * ------------------------------------------------------------------- */

    @Test
    public void encodeThenDecode_byteArray_roundTripsEvenWithNegativeBytes() throws Exception {
        // Arrange
        URLCodec codec = new URLCodec();
        byte[] original = new byte[] {0, -63, 10, -1, 42};

        // Act
        byte[] encoded = codec.encode(original);
        byte[] decoded = codec.decode(encoded);

        // Assert
        assertArrayEquals(original, decoded);
        assertNotSame(encoded, decoded); // ensure a new array was produced
    }

    /* ---------------------------------------------------------------------
     * Static helpers: encodeUrl/decodeUrl
     * ------------------------------------------------------------------- */

    @Test
    public void encodeUrl_withWwwFormSafeSet_replacesSpaceWithPlus() throws Exception {
        // Arrange
        byte[] ascii = "a b".getBytes("US-ASCII");

        // Act
        byte[] encoded = URLCodec.encodeUrl(URLCodec.WWW_FORM_URL, ascii);

        // Assert
        assertEquals("a+b", new String(encoded, "US-ASCII"));
    }

    @Test
    public void decodeUrl_treatsPlusAsSpace() throws Exception {
        // Arrange
        byte[] plus = "+".getBytes("US-ASCII");

        // Act
        byte[] decoded = URLCodec.decodeUrl(plus);

        // Assert
        assertArrayEquals(" ".getBytes("US-ASCII"), decoded);
    }

    @Test
    public void encodeUrl_nullBytes_returnsNull() {
        assertNull(URLCodec.encodeUrl(URLCodec.WWW_FORM_URL, null));
    }

    @Test
    public void decodeUrl_nullBytes_returnsNull() throws Exception {
        assertNull(URLCodec.decodeUrl(null));
    }

    @Test
    public void decodeUrl_rejectsDanglingPercent() {
        // Arrange
        byte[] bad = "%".getBytes();

        // Act + Assert
        try {
            URLCodec.decodeUrl(bad);
            fail("Expected DecoderException for dangling %");
        } catch (DecoderException expected) {
            // OK
        }
    }

    @Test
    public void decodeUrl_rejectsNonHexDigitsAfterPercent() {
        // Arrange
        byte[] bad = "%G0".getBytes(); // 'G' is not hex

        // Act + Assert
        try {
            URLCodec.decodeUrl(bad);
            fail("Expected DecoderException for non-hex digits");
        } catch (DecoderException expected) {
            // OK
        }
    }

    /* ---------------------------------------------------------------------
     * Charsets
     * ------------------------------------------------------------------- */

    @Test
    public void defaultCharset_isUtf8_byDefaultConstructor() {
        // Arrange
        URLCodec codec = new URLCodec();

        // Act + Assert
        assertEquals(UTF_8, codec.getDefaultCharset());
        assertEquals(UTF_8, codec.getEncoding()); // deprecated alias
    }

    @Test
    public void customCharset_isReflectedByGetters() {
        // Arrange
        URLCodec codec = new URLCodec("ISO-8859-1");

        // Act + Assert
        assertEquals("ISO-8859-1", codec.getDefaultCharset());
        assertEquals("ISO-8859-1", codec.getEncoding()); // deprecated alias
    }

    @Test
    public void charset_canBeEmptyOrNull_butIsReportedBack() {
        assertEquals("", new URLCodec("").getDefaultCharset());
        assertNull(new URLCodec((String) null).getDefaultCharset());
    }

    @Test
    public void encode_withUnsupportedCharset_throwsUnsupportedEncodingException() {
        // Arrange
        URLCodec codec = new URLCodec();

        // Act + Assert
        try {
            codec.encode("abc", UNSUPPORTED_CHARSET);
            fail("Expected UnsupportedEncodingException");
        } catch (UnsupportedEncodingException expected) {
            // OK
        }
    }

    @Test
    public void decode_withUnsupportedCharset_throwsUnsupportedEncodingException() {
        // Arrange
        URLCodec codec = new URLCodec();

        // Act + Assert
        try {
            codec.decode("abc", UNSUPPORTED_CHARSET);
            fail("Expected UnsupportedEncodingException");
        } catch (UnsupportedEncodingException expected) {
            // OK
        } catch (DecoderException unexpected) {
            fail("Expected UnsupportedEncodingException, not DecoderException");
        }
    }

    @Test
    public void encode_withNullDefaultCharset_throwsNullPointerException() {
        // Arrange
        URLCodec codec = new URLCodec((String) null);

        // Act + Assert
        try {
            codec.encode("");
            fail("Expected NullPointerException when default charset is null");
        } catch (NullPointerException expected) {
            // OK (documenting current behavior)
        } catch (EncoderException unexpected) {
            fail("Expected NullPointerException, not EncoderException");
        }
    }

    /* ---------------------------------------------------------------------
     * Null and empty inputs
     * ------------------------------------------------------------------- */

    @Test
    public void encode_decode_string_handleNullAndEmpty() throws Exception {
        // Arrange
        URLCodec codec = new URLCodec();

        // Act + Assert (null)
        assertNull(codec.encode((String) null));
        assertNull(codec.decode((String) null));

        // Act + Assert (empty)
        assertEquals("", codec.encode(""));
        assertEquals("", codec.decode(""));
        assertEquals("", codec.decode("", UTF_8));
    }

    @Test
    public void encode_decode_bytes_handleNullAndEmpty() throws Exception {
        // Arrange
        URLCodec codec = new URLCodec();

        // Act + Assert (null)
        assertNull(codec.encode((byte[]) null));
        assertNull(codec.decode((byte[]) null));

        // Act + Assert (empty)
        assertArrayEquals(new byte[0], codec.encode(new byte[0]));
        assertArrayEquals(new byte[0], codec.decode(new byte[0]));
        assertArrayEquals(new byte[0], URLCodec.decodeUrl(new byte[0]));
    }

    /* ---------------------------------------------------------------------
     * Object overloads
     * ------------------------------------------------------------------- */

    @Test
    public void encode_object_overload_supportsStrings() throws Exception {
        // Arrange
        URLCodec codec = new URLCodec();

        // Act
        Object encoded = codec.encode((Object) "a b");

        // Assert
        assertEquals("a+b", encoded);
    }

    @Test
    public void encode_object_overload_rejectsUnsupportedTypes() {
        // Arrange
        URLCodec codec = new URLCodec();

        // Act + Assert
        try {
            codec.encode((Object) codec);
            fail("Expected EncoderException for unsupported type");
        } catch (EncoderException expected) {
            // OK
        }
    }

    @Test
    public void decode_object_overload_supportsStrings() throws Exception {
        // Arrange
        URLCodec codec = new URLCodec();

        // Act
        Object decoded = codec.decode((Object) "a+b");

        // Assert
        assertEquals("a b", decoded);
    }

    @Test
    public void decode_object_overload_rejectsUnsupportedTypes() {
        // Arrange
        URLCodec codec = new URLCodec();

        // Act + Assert
        try {
            codec.decode(new Object());
            fail("Expected DecoderException for unsupported type");
        } catch (DecoderException expected) {
            // OK
        }
    }

    @Test
    public void encode_decode_object_handleNull() throws Exception {
        // Arrange
        URLCodec codec = new URLCodec();

        // Act + Assert
        assertNull(codec.encode((Object) null));
        assertNull(codec.decode((Object) null));
    }
}