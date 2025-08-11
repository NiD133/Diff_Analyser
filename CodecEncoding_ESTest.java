package org.apache.commons.compress.harmony.pack200;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;

import static org.junit.Assert.*;

public class CodecEncodingTest {

    // ---------- getCanonicalCodec ----------

    @Test
    public void getCanonicalCodec_zeroReturnsNull() {
        assertNull(CodecEncoding.getCanonicalCodec(0));
    }

    @Test
    public void getCanonicalCodec_knownEntryAttributes() {
        // 40 corresponds to a canonical BHSD codec with s == 2
        BHSDCodec c40 = CodecEncoding.getCanonicalCodec(40);
        assertNotNull(c40);
        assertEquals(2, c40.getS());

        // 9 corresponds to an unsigned codec
        BHSDCodec c9 = CodecEncoding.getCanonicalCodec(9);
        assertNotNull(c9);
        assertFalse(c9.isSigned());
    }

    @Test
    public void getCanonicalCodec_outOfRangeThrowsArrayIndexOutOfBounds() {
        try {
            CodecEncoding.getCanonicalCodec(260);
            fail("Expected ArrayIndexOutOfBoundsException for out-of-range canonical index");
        } catch (ArrayIndexOutOfBoundsException expected) {
            // expected
        }
    }

    // ---------- getSpecifierForDefaultCodec ----------

    @Test
    public void getSpecifierForDefaultCodec_returnsZeroForNull() {
        assertEquals(0, CodecEncoding.getSpecifierForDefaultCodec(null));
    }

    @Test
    public void getSpecifierForDefaultCodec_returnsCanonicalByteForKnownCodec() {
        assertEquals(116, CodecEncoding.getSpecifierForDefaultCodec(Codec.CHAR3));
    }

    // ---------- getSpecifier (BHSD, Run, Population) ----------

    @Test
    public void getSpecifier_forBHSDCodec_char3() {
        int[] spec = CodecEncoding.getSpecifier(Codec.CHAR3, Codec.CHAR3);
        assertArrayEquals(new int[] {116, 16, 127}, spec);
    }

    @Test
    public void getSpecifier_forRunCodec_sameChildCodec() {
        RunCodec run = new RunCodec(256, Codec.CHAR3, Codec.CHAR3);
        int[] spec = CodecEncoding.getSpecifier(run, Codec.CHAR3);
        assertArrayEquals(new int[] {129, 255, 116, 16, 127}, spec);
    }

    @Test
    public void getSpecifier_forRunCodec_mixedChildren() {
        RunCodec run = new RunCodec(44, Codec.MDELTA5, Codec.SIGNED5);
        int[] spec = CodecEncoding.getSpecifier(run, Codec.SIGNED5);
        assertArrayEquals(new int[] {137, 43, 43}, spec);
    }

    @Test
    public void getSpecifier_forPopulationCodec_requiresDefaultCodec() {
        PopulationCodec population = new PopulationCodec(null, null, null);
        try {
            CodecEncoding.getSpecifier(population, null);
            fail("Expected NullPointerException when default codec is missing for PopulationCodec");
        } catch (NullPointerException expected) {
            // expected
        }
    }

    @Test
    public void getSpecifier_forPopulationCodec_returnsTwoInts() {
        PopulationCodec population = new PopulationCodec(Codec.MDELTA5, Codec.MDELTA5, Codec.MDELTA5);
        int[] spec = CodecEncoding.getSpecifier(population, Codec.MDELTA5);
        assertEquals(2, spec.length);
    }

    // ---------- getCodec (decoding from headers) ----------

    @Test
    public void getCodec_zeroReturnsDefaultCodec() throws Exception {
        // value 0 means "use default"
        Codec result = CodecEncoding.getCodec(0, new ByteArrayInputStream(new byte[0]), null);
        assertNull(result);
    }

    @Test
    public void getCodec_nullInputStreamThrowsNPE() {
        try {
            CodecEncoding.getCodec(128, null, Codec.BYTE1);
            fail("Expected NullPointerException when input stream is null");
        } catch (NullPointerException expected) {
            // expected
        }
    }

    @Test
    public void getCodec_negativeEncodingThrowsIllegalArgumentException() throws Exception {
        try {
            CodecEncoding.getCodec(-1, new ByteArrayInputStream(new byte[0]), Codec.UNSIGNED5);
            fail("Expected IllegalArgumentException for negative encoding value");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    @Test
    public void getCodec_unconnectedPipedInputStreamThrowsIOException() {
        PipedInputStream in = new PipedInputStream(); // not connected
        try {
            CodecEncoding.getCodec(146, in, Codec.UDELTA5);
            fail("Expected IOException because PipedInputStream is not connected");
        } catch (IOException expected) {
            // expected
        } catch (Pack200Exception e) {
            fail("Did not expect Pack200Exception");
        }
    }

    @Test
    public void getCodec_invalidEncodingByteThrowsIOException() throws Exception {
        // 257 is outside a single-byte range and should be rejected by the API contract
        try {
            CodecEncoding.getCodec(257, new ByteArrayInputStream(new byte[1]), Codec.DELTA5);
            fail("Expected IOException for invalid codec encoding value");
        } catch (IOException expected) {
            // expected
        }
    }

    @Test
    public void getCodec_eofWhileReadingHeaderThrowsEOFException() throws Exception {
        // For composite encodings (>= 116) the method will read additional bytes from 'in'.
        // Provide an empty stream to force EOF.
        try {
            CodecEncoding.getCodec(116, new ByteArrayInputStream(new byte[0]), Codec.MDELTA5);
            fail("Expected EOFException when not enough header bytes are available");
        } catch (EOFException expected) {
            // expected
        }
    }

    @Test
    public void getCodec_canonicalSingleByteCodec_returnsBHSDCodec() throws Exception {
        // 1 maps to a canonical BHSDCodec with smallest() == 0
        BHSDCodec codec = (BHSDCodec) CodecEncoding.getCodec(1, new ByteArrayInputStream(new byte[0]), Codec.UNSIGNED5);
        assertNotNull(codec);
        assertEquals(0L, codec.smallest());
    }
}