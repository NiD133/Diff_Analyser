package org.apache.commons.compress.harmony.pack200;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PushbackInputStream;
import java.io.SequenceInputStream;
import java.util.Enumeration;
import org.apache.commons.compress.harmony.pack200.BHSDCodec;
import org.apache.commons.compress.harmony.pack200.Codec;
import org.apache.commons.compress.harmony.pack200.CodecEncoding;
import org.apache.commons.compress.harmony.pack200.PopulationCodec;
import org.apache.commons.compress.harmony.pack200.RunCodec;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class CodecEncoding_ESTest extends CodecEncoding_ESTest_scaffolding {

    // Constants for canonical codec indices and expected values
    private static final int CANONICAL_CODEC_INDEX_48 = 48;
    private static final int CANONICAL_CODEC_INDEX_9 = 9;
    private static final int CANONICAL_CODEC_INDEX_40 = 40;
    private static final int CODE_116 = 116;
    private static final int CODE_140 = 140;
    private static final int CODE_141 = 141;
    private static final int CODE_144 = 144;
    private static final int CODE_146 = 146;
    private static final int CODE_152 = 152;
    private static final int CODE_181 = 181;
    private static final int CODE_257 = 257;
    private static final int INVALID_ENCODING = -2300;
    private static final int RUN_CODEC_RUN_LENGTH = 256;
    private static final int RUN_CODEC_RUN_LENGTH_44 = 44;
    private static final int RUN_CODEC_RUN_LENGTH_631 = 631;
    private static final int RUN_CODEC_RUN_LENGTH_22672 = 22672;
    private static final int[] EXPECTED_SPECIFIER_FOR_RUNCODEC = {129, 255, 116, 16, 127};
    private static final int[] EXPECTED_SPECIFIER_FOR_RUNCODEC_44 = {137, 43, 43};
    private static final int[] EXPECTED_SPECIFIER_FOR_RUNCODEC_631 = {122, 38, 26, 26};
    private static final int[] EXPECTED_SPECIFIER_FOR_RUNCODEC_22672 = {123, 87, 48, 48};

    @Test(timeout = 4000)
    public void testGetSpecifier_RunCodecWithCHAR3Default_ReturnsExpectedArray() throws Throwable {
        BHSDCodec char3Codec = Codec.CHAR3;
        RunCodec runCodec = new RunCodec(RUN_CODEC_RUN_LENGTH, char3Codec, char3Codec);
        int[] specifier = CodecEncoding.getSpecifier(runCodec, char3Codec);
        assertNotNull(specifier);
        assertArrayEquals(EXPECTED_SPECIFIER_FOR_RUNCODEC, specifier);
    }

    @Test(timeout = 4000)
    public void testGetCodec_WithCanonicalCodec115_ReturnsSignedCodec() throws Throwable {
        BHSDCodec defaultCodec = Codec.CHAR3;
        Enumeration<BufferedInputStream> emptyEnum = mock(Enumeration.class);
        when(emptyEnum.hasMoreElements()).thenReturn(false);
        SequenceInputStream emptyStream = new SequenceInputStream(emptyEnum);
        Codec codec = CodecEncoding.getCodec(115, emptyStream, defaultCodec);
        assertTrue(((BHSDCodec) codec).isSigned());
    }

    @Test(timeout = 4000)
    public void testGetSpecifierForDefaultCodec_NullDefault_ReturnsZero() throws Throwable {
        int specifier = CodecEncoding.getSpecifierForDefaultCodec(null);
        assertEquals(0, specifier);
    }

    @Test(timeout = 4000)
    public void testGetCodec_WithEncodingZero_ReturnsNull() throws Throwable {
        PipedInputStream pipedStream = new PipedInputStream();
        Codec codec = CodecEncoding.getCodec(0, pipedStream, null);
        assertNull(codec);
    }

    @Test(timeout = 4000)
    public void testGetCanonicalCodec_IndexZero_ReturnsNull() throws Throwable {
        BHSDCodec codec = CodecEncoding.getCanonicalCodec(0);
        assertNull(codec);
    }

    @Test(timeout = 4000)
    public void testGetCanonicalCodec_Index40_ReturnsSignedCodec() throws Throwable {
        BHSDCodec codec = CodecEncoding.getCanonicalCodec(CANONICAL_CODEC_INDEX_40);
        assertEquals(2, codec.getS());
    }

    @Test(timeout = 4000)
    public void testGetCanonicalCodec_Index9_ReturnsUnsignedCodec() throws Throwable {
        BHSDCodec codec = CodecEncoding.getCanonicalCodec(CANONICAL_CODEC_INDEX_9);
        assertFalse(codec.isSigned());
    }

    @Test(timeout = 4000)
    public void testGetSpecifier_PopulationCodecWithNullDefault_ThrowsNullPointerException() throws Throwable {
        PopulationCodec populationCodec = new PopulationCodec(null, null, null);
        try {
            CodecEncoding.getSpecifier(populationCodec, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetSpecifier_RunCodecWithSIGNED5Default_ReturnsExpectedArray() throws Throwable {
        BHSDCodec signed5Codec = Codec.SIGNED5;
        RunCodec runCodec = new RunCodec(4, signed5Codec, signed5Codec);
        int[] specifier = CodecEncoding.getSpecifier(runCodec, signed5Codec);
        assertNotNull(specifier);
        assertArrayEquals(new int[]{125, 27}, specifier);
    }

    @Test(timeout = 4000)
    public void testGetSpecifier_ForCHAR3Default_ReturnsExpectedArray() throws Throwable {
        BHSDCodec char3Codec = Codec.CHAR3;
        int[] specifier = CodecEncoding.getSpecifier(char3Codec, char3Codec);
        assertArrayEquals(new int[]{116, 16, 127}, specifier);
    }

    @Test(timeout = 4000)
    public void testGetCodec_WithEncoding152_ReturnsCodec() throws Throwable {
        PipedInputStream pipedStream = new PipedInputStream();
        BHSDCodec defaultCodec = Codec.UDELTA5;
        Codec codec = CodecEncoding.getCodec(CODE_152, pipedStream, defaultCodec);
        assertEquals(0, codec.lastBandLength);
    }

    @Test(timeout = 4000)
    public void testGetCodec_WithEncoding128AndNullStream_ThrowsNullPointerException() throws Throwable {
        try {
            CodecEncoding.getCodec(128, null, Codec.BYTE1);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetCodec_WithNegativeEncoding_ThrowsIllegalArgumentException() throws Throwable {
        byte[] data = new byte[3];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data, 1024, 3294);
        try {
            CodecEncoding.getCodec(INVALID_ENCODING, inputStream, Codec.UNSIGNED5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Encoding cannot be less than zero", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testGetSpecifier_ForPopulationCodec_ReturnsArrayOfLengthTwo() throws Throwable {
        BHSDCodec mdelta5Codec = Codec.MDELTA5;
        PopulationCodec populationCodec = new PopulationCodec(mdelta5Codec, mdelta5Codec, mdelta5Codec);
        int[] specifier = CodecEncoding.getSpecifier(populationCodec, mdelta5Codec);
        assertEquals(2, specifier.length);
    }

    @Test(timeout = 4000)
    public void testGetSpecifier_RunCodec44WithSIGNED5Default_ReturnsExpectedArray() throws Throwable {
        BHSDCodec signed5Codec = Codec.SIGNED5;
        BHSDCodec mdelta5Codec = Codec.MDELTA5;
        RunCodec runCodec = new RunCodec(RUN_CODEC_RUN_LENGTH_44, mdelta5Codec, signed5Codec);
        int[] specifier = CodecEncoding.getSpecifier(runCodec, signed5Codec);
        assertNotNull(specifier);
        assertArrayEquals(EXPECTED_SPECIFIER_FOR_RUNCODEC_44, specifier);
    }

    @Test(timeout = 4000)
    public void testGetSpecifier_RunCodecMaxIntWithPopulationDefault_ReturnsExpectedArray() throws Throwable {
        BHSDCodec udelta5Codec = Codec.UDELTA5;
        RunCodec runCodec = new RunCodec(Integer.MAX_VALUE, udelta5Codec, udelta5Codec);
        PopulationCodec populationCodec = new PopulationCodec(runCodec, runCodec, runCodec);
        int[] specifier = CodecEncoding.getSpecifier(runCodec, populationCodec);
        assertNotNull(specifier);
        assertArrayEquals(new int[]{124, 524286, 41, 41}, specifier);
    }

    @Test(timeout = 4000)
    public void testGetSpecifier_RunCodec631WithCHAR3Default_ReturnsExpectedArray() throws Throwable {
        BHSDCodec unsigned5Codec = Codec.UNSIGNED5;
        RunCodec runCodec = new RunCodec(RUN_CODEC_RUN_LENGTH_631, unsigned5Codec, unsigned5Codec);
        int[] specifier = CodecEncoding.getSpecifier(runCodec, Codec.CHAR3);
        assertArrayEquals(EXPECTED_SPECIFIER_FOR_RUNCODEC_631, specifier);
        assertNotNull(specifier);
    }

    @Test(timeout = 4000)
    public void testGetSpecifier_RunCodec22672WithCHAR3Default_ReturnsExpectedArray() throws Throwable {
        BHSDCodec canonicalCodec = CodecEncoding.getCanonicalCodec(CANONICAL_CODEC_INDEX_48);
        RunCodec runCodec = new RunCodec(RUN_CODEC_RUN_LENGTH_22672, canonicalCodec, canonicalCodec);
        int[] specifier = CodecEncoding.getSpecifier(runCodec, Codec.CHAR3);
        assertArrayEquals(EXPECTED_SPECIFIER_FOR_RUNCODEC_22672, specifier);
        assertNotNull(specifier);
    }

    @Test(timeout = 4000)
    public void testGetSpecifier_ForMDELTA5Default_ReturnsSingleElementArray() throws Throwable {
        BHSDCodec mdelta5Codec = Codec.MDELTA5;
        int[] specifier = CodecEncoding.getSpecifier(mdelta5Codec, mdelta5Codec);
        assertEquals(1, specifier.length);
    }

    @Test(timeout = 4000)
    public void testGetCodec_WithEncoding146AndPipedStream_ThrowsIOException() throws Throwable {
        PipedInputStream pipedStream = new PipedInputStream();
        try {
            CodecEncoding.getCodec(CODE_146, pipedStream, Codec.UDELTA5);
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("Pipe not connected", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testGetCodec_WithEncoding181AndNullStream_ThrowsNullPointerException() throws Throwable {
        try {
            CodecEncoding.getCodec(CODE_181, null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetCodec_WithEncoding141_ReturnsCodec() throws Throwable {
        BHSDCodec mdelta5Codec = Codec.MDELTA5;
        byte[] data = new byte[6];
        data[0] = (byte) -88;
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CodecEncoding.getCodec(CODE_141, inputStream, mdelta5Codec);
        assertEquals(3, inputStream.available());
    }

    @Test(timeout = 4000)
    public void testGetCodec_WithEncoding144_ReturnsCodec() throws Throwable {
        BHSDCodec mdelta5Codec = Codec.MDELTA5;
        byte[] data = new byte[16];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CodecEncoding.getCodec(CODE_144, inputStream, mdelta5Codec);
        assertEquals(15, inputStream.available());
    }

    @Test(timeout = 4000)
    public void testGetCodec_WithInvalidEncoding257_ThrowsIOException() throws Throwable {
        BHSDCodec delta5Codec = Codec.DELTA5;
        byte[] data = new byte[1];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        try {
            CodecEncoding.getCodec(CODE_257, inputStream, delta5Codec);
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("Invalid codec encoding byte (257) found", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testGetCodec_WithEncoding140_ReturnsRunCodec() throws Throwable {
        byte[] data = new byte[10];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BHSDCodec udelta5Codec = Codec.UDELTA5;
        PushbackInputStream pushbackStream = new PushbackInputStream(inputStream, 224);
        PopulationCodec populationCodec = new PopulationCodec(udelta5Codec, CODE_116, udelta5Codec);
        RunCodec runCodec = (RunCodec) CodecEncoding.getCodec(CODE_140, pushbackStream, populationCodec);
        assertEquals(8, inputStream.available());
        assertEquals(4096, runCodec.getK());
    }

    @Test(timeout = 4000)
    public void testGetCodec_WithEncoding1_ReturnsCanonicalCodec() throws Throwable {
        byte[] data = new byte[8];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BHSDCodec unsigned5Codec = Codec.UNSIGNED5;
        BHSDCodec codec = (BHSDCodec) CodecEncoding.getCodec(1, inputStream, unsigned5Codec);
        assertEquals(0L, codec.smallest());
        assertNotNull(codec);
    }

    @Test(timeout = 4000)
    public void testGetCodec_WithEncoding116AndExhaustedStream_ThrowsEOFException() throws Throwable {
        byte[] data = new byte[8];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        inputStream.read(data); // Exhaust the stream
        try {
            CodecEncoding.getCodec(CODE_116, inputStream, Codec.UNSIGNED5);
            fail("Expected EOFException");
        } catch (EOFException e) {
            assertEquals("End of buffer read whilst trying to decode codec", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testGetSpecifierForDefaultCodec_ForCHAR3Default_Returns116() throws Throwable {
        BHSDCodec char3Codec = Codec.CHAR3;
        int specifier = CodecEncoding.getSpecifierForDefaultCodec(char3Codec);
        assertEquals(116, specifier);
    }

    @Test(timeout = 4000)
    public void testGetCanonicalCodec_InvalidIndex_ThrowsArrayIndexOutOfBoundsException() throws Throwable {
        try {
            CodecEncoding.getCanonicalCodec(260);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertEquals("260", e.getMessage());
        }
    }
}