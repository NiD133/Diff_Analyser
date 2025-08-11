package org.apache.commons.codec.binary;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.CodecPolicy;
import org.junit.Test;

/**
 * Readable, behavior-focused tests for Base16.
 *
 * These tests avoid internal implementation details and focus on the public API:
 * - Encoding/decoding behavior (including case selection)
 * - Error handling for invalid input and strict policy
 * - Alphabet membership checks
 */
public class Base16Test {

    private static String encodeToAsciiString(Base16 codec, byte[] data) {
        return new String(codec.encode(data), StandardCharsets.US_ASCII);
    }

    // Encoding

    @Test
    public void encodesToUppercaseByDefault() {
        Base16 codec = new Base16(); // default: upper-case alphabet
        byte[] input = new byte[] { 0x00, (byte) 0xE9, 0x00 }; // 00 E9 00
        String encoded = encodeToAsciiString(codec, input);
        assertEquals("00E900", encoded);
    }

    @Test
    public void encodesToLowercaseWhenRequested() {
        Base16 codec = new Base16(true); // lower-case alphabet
        byte[] input = new byte[] { 0x00, (byte) 0xE9, 0x00 }; // 00 E9 00
        String encoded = encodeToAsciiString(codec, input);
        assertEquals("00e900", encoded);
    }

    @Test
    public void encodesEmptyArrayToEmptyString() {
        Base16 codec = new Base16();
        assertEquals("", encodeToAsciiString(codec, new byte[0]));
    }

    // Decoding

    @Test
    public void decodesValidPair() {
        Base16 codec = new Base16();
        assertArrayEquals(new byte[] { 33 }, codec.decode("21"));
    }

    @Test
    public void roundTripEncodeDecode() {
        Base16 codec = new Base16();
        byte[] original = new byte[] { 0x00, 0x01, 0x02, 0x0F, 0x10, (byte) 0xFF, 0x7F };
        String encoded = encodeToAsciiString(codec, original);
        byte[] decoded = codec.decode(encoded);
        assertArrayEquals(original, decoded);
    }

    @Test
    public void decodeEmptyStringToEmptyArray() {
        Base16 codec = new Base16();
        assertArrayEquals(new byte[0], codec.decode(""));
    }

    @Test
    public void decodeRejectsInvalidCharacter() {
        Base16 codec = new Base16();
        assertThrows(IllegalArgumentException.class, () -> codec.decode("Gw")); // 'G' is not in upper-case hex [0-9A-F]
    }

    @Test
    public void strictDecodingRejectsOddLength() {
        Base16 strict = new Base16(false, CodecPolicy.STRICT);
        assertThrows(IllegalArgumentException.class, () -> strict.decode("F")); // single trailing nibble
    }

    @Test
    public void decodeIsCaseSensitiveToConfiguredAlphabet() {
        // Upper-case codec rejects lower-case input
        Base16 upper = new Base16(false);
        assertThrows(IllegalArgumentException.class, () -> upper.decode("e9"));

        // Lower-case codec rejects upper-case input
        Base16 lower = new Base16(true);
        assertThrows(IllegalArgumentException.class, () -> lower.decode("E9"));
    }

    // Alphabet membership

    @Test
    public void isInAlphabetForUppercaseCodec() {
        Base16 upper = new Base16(false);
        assertTrue(upper.isInAlphabet((byte) '0'));
        assertTrue(upper.isInAlphabet((byte) 'A'));
        assertTrue(upper.isInAlphabet((byte) 'F'));

        assertFalse(upper.isInAlphabet((byte) 'a')); // lower-case not allowed for upper-case codec
        assertFalse(upper.isInAlphabet((byte) 'G'));
        assertFalse(upper.isInAlphabet((byte) '\\'));
    }

    @Test
    public void isInAlphabetForLowercaseCodec() {
        Base16 lower = new Base16(true);
        assertTrue(lower.isInAlphabet((byte) '0'));
        assertTrue(lower.isInAlphabet((byte) 'a'));
        assertTrue(lower.isInAlphabet((byte) 'f'));

        assertFalse(lower.isInAlphabet((byte) 'A')); // upper-case not allowed for lower-case codec
        assertFalse(lower.isInAlphabet((byte) 'G'));
    }

    // Construction and contracts

    @Test
    public void constructingWithNullPolicyThrows() {
        assertThrows(NullPointerException.class, () -> new Base16(true, null));
    }
}