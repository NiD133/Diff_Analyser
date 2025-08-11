package org.apache.commons.compress.harmony.pack200;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.shaded.org.mockito.Mockito;

import java.io.*;
import java.util.Enumeration;

import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class CodecEncoding_ESTest extends CodecEncoding_ESTest_scaffolding {

    private static final int TIMEOUT = 4000;

    @Test(timeout = TIMEOUT)
    public void testRunCodecWithCHAR3() throws Throwable {
        // Test the specifier for a RunCodec with CHAR3 codec
        BHSDCodec char3Codec = Codec.CHAR3;
        RunCodec runCodec = new RunCodec(256, char3Codec, char3Codec);
        int[] specifier = CodecEncoding.getSpecifier(runCodec, char3Codec);

        assertNotNull(specifier);
        assertArrayEquals(new int[]{129, 255, 116, 16, 127}, specifier);
    }

    @Test(timeout = TIMEOUT)
    public void testGetCodecWithMockedEnumeration() throws Throwable {
        // Test getting a codec with a mocked enumeration
        BHSDCodec char3Codec = Codec.CHAR3;
        Enumeration<BufferedInputStream> enumeration = mock(Enumeration.class, new ViolatedAssumptionAnswer());
        doReturn(false).when(enumeration).hasMoreElements();
        SequenceInputStream sequenceInputStream = new SequenceInputStream(enumeration);

        BHSDCodec codec = (BHSDCodec) CodecEncoding.getCodec(115, sequenceInputStream, char3Codec);

        assertTrue(codec.isSigned());
    }

    @Test(timeout = TIMEOUT)
    public void testGetSpecifierForNullDefaultCodec() throws Throwable {
        // Test getting specifier for a null default codec
        int specifier = CodecEncoding.getSpecifierForDefaultCodec(null);

        assertEquals(0, specifier);
    }

    @Test(timeout = TIMEOUT)
    public void testGetCodecWithPipedStream() throws Throwable {
        // Test getting a codec with a piped input stream
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream);

        Codec codec = CodecEncoding.getCodec(0, pipedInputStream, null);

        assertNull(codec);
    }

    @Test(timeout = TIMEOUT)
    public void testGetCanonicalCodecWithZero() throws Throwable {
        // Test getting canonical codec with index 0
        BHSDCodec codec = CodecEncoding.getCanonicalCodec(0);

        assertNull(codec);
    }

    @Test(timeout = TIMEOUT)
    public void testGetCanonicalCodecWithByteValue() throws Throwable {
        // Test getting canonical codec with a specific byte value
        BHSDCodec codec = CodecEncoding.getCanonicalCodec((byte) 40);

        assertEquals(2, codec.getS());
    }

    @Test(timeout = TIMEOUT)
    public void testGetCanonicalCodecWithNine() throws Throwable {
        // Test getting canonical codec with index 9
        BHSDCodec codec = CodecEncoding.getCanonicalCodec(9);

        assertFalse(codec.isSigned());
    }

    @Test(timeout = TIMEOUT)
    public void testGetSpecifierWithNullPopulationCodec() throws Throwable {
        // Test getting specifier with a null population codec
        PopulationCodec populationCodec = new PopulationCodec(null, null, null);

        try {
            CodecEncoding.getSpecifier(populationCodec, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.harmony.pack200.CodecEncoding", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testRunCodecWithSIGNED5() throws Throwable {
        // Test the specifier for a RunCodec with SIGNED5 codec
        BHSDCodec signed5Codec = Codec.SIGNED5;
        RunCodec runCodec = new RunCodec(4, signed5Codec, signed5Codec);
        int[] specifier = CodecEncoding.getSpecifier(runCodec, runCodec.SIGNED5);

        assertNotNull(specifier);
        assertArrayEquals(new int[]{125, 27}, specifier);
    }

    @Test(timeout = TIMEOUT)
    public void testDecodeAndGetSpecifier() throws Throwable {
        // Test decoding and getting specifier
        byte[] byteArray = new byte[19];
        byteArray[1] = (byte) 101;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        BHSDCodec codec = CodecEncoding.getCanonicalCodec((byte) 30);

        codec.decode(byteArrayInputStream);
        RunCodec runCodec = new RunCodec(67, codec, codec);
        Codec codecResult = CodecEncoding.getCodec((byte) 116, byteArrayInputStream, runCodec);

        CodecEncoding.getSpecifier(codecResult, null);

        assertEquals(16, byteArrayInputStream.available());
        assertEquals(1, codec.lastBandLength);
    }

    @Test(timeout = TIMEOUT)
    public void testGetSpecifierWithCHAR3() throws Throwable {
        // Test getting specifier for CHAR3 codec
        BHSDCodec char3Codec = Codec.CHAR3;
        int[] specifier = CodecEncoding.getSpecifier(char3Codec, char3Codec);

        assertArrayEquals(new int[]{116, 16, 127}, specifier);
    }

    @Test(timeout = TIMEOUT)
    public void testGetCodecWithPipedInputStream() throws Throwable {
        // Test getting a codec with a piped input stream
        PipedInputStream pipedInputStream = new PipedInputStream();
        BHSDCodec signed5Codec = Codec.SIGNED5;

        Codec codec = CodecEncoding.getCodec(152, pipedInputStream, signed5Codec);

        assertEquals(0, codec.lastBandLength);
    }

    @Test(timeout = TIMEOUT)
    public void testGetCodecWithNullInputStream() throws Throwable {
        // Test getting a codec with a null input stream
        BHSDCodec byte1Codec = Codec.BYTE1;

        try {
            CodecEncoding.getCodec(128, null, byte1Codec);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.harmony.pack200.CodecEncoding", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testGetCodecWithNullInputStreamAndSIGNED5() throws Throwable {
        // Test getting a codec with a null input stream and SIGNED5 codec
        BHSDCodec signed5Codec = Codec.SIGNED5;

        try {
            CodecEncoding.getCodec(117, null, signed5Codec);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.harmony.pack200.CodecEncoding", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testGetCodecWithNegativeEncoding() throws Throwable {
        // Test getting a codec with a negative encoding value
        byte[] byteArray = new byte[3];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray, 1024, 3294);
        BHSDCodec unsigned5Codec = Codec.UNSIGNED5;

        try {
            CodecEncoding.getCodec(-2300, byteArrayInputStream, unsigned5Codec);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.compress.harmony.pack200.CodecEncoding", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testGetSpecifierWithPopulationCodec() throws Throwable {
        // Test getting specifier for a PopulationCodec
        BHSDCodec mdelta5Codec = Codec.MDELTA5;
        PopulationCodec populationCodec = new PopulationCodec(mdelta5Codec, mdelta5Codec, mdelta5Codec);
        int[] specifier = CodecEncoding.getSpecifier(populationCodec, mdelta5Codec);

        assertEquals(2, specifier.length);
    }

    @Test(timeout = TIMEOUT)
    public void testRunCodecWithDifferentCodecs() throws Throwable {
        // Test the specifier for a RunCodec with different codecs
        BHSDCodec signed5Codec = Codec.SIGNED5;
        BHSDCodec mdelta5Codec = Codec.MDELTA5;
        RunCodec runCodec = new RunCodec(44, mdelta5Codec, signed5Codec);
        int[] specifier = CodecEncoding.getSpecifier(runCodec, signed5Codec);

        assertNotNull(specifier);
        assertArrayEquals(new int[]{137, 43, 43}, specifier);
    }

    @Test(timeout = TIMEOUT)
    public void testRunCodecWithUDELTA5() throws Throwable {
        // Test the specifier for a RunCodec with UDELTA5 codec
        BHSDCodec udelta5Codec = Codec.UDELTA5;
        RunCodec runCodec = new RunCodec(Integer.MAX_VALUE, udelta5Codec, udelta5Codec);
        PopulationCodec populationCodec = new PopulationCodec(runCodec, runCodec, runCodec);
        int[] specifier = CodecEncoding.getSpecifier(runCodec, populationCodec);

        assertNotNull(specifier);
        assertArrayEquals(new int[]{124, 524286, 41, 41}, specifier);
    }

    @Test(timeout = TIMEOUT)
    public void testRunCodecWithUNSIGNED5() throws Throwable {
        // Test the specifier for a RunCodec with UNSIGNED5 codec
        BHSDCodec unsigned5Codec = Codec.UNSIGNED5;
        RunCodec runCodec = new RunCodec(631, unsigned5Codec, unsigned5Codec);
        int[] specifier = CodecEncoding.getSpecifier(runCodec, runCodec.CHAR3);

        assertArrayEquals(new int[]{122, 38, 26, 26}, specifier);
        assertNotNull(specifier);
    }

    @Test(timeout = TIMEOUT)
    public void testRunCodecWithCanonicalCodec() throws Throwable {
        // Test the specifier for a RunCodec with a canonical codec
        BHSDCodec codec = CodecEncoding.getCanonicalCodec((byte) 48);
        RunCodec runCodec = new RunCodec(22672, codec, codec);
        int[] specifier = CodecEncoding.getSpecifier(runCodec, runCodec.CHAR3);

        assertArrayEquals(new int[]{123, 87, 48, 48}, specifier);
        assertNotNull(specifier);
    }

    @Test(timeout = TIMEOUT)
    public void testDecodeAndGetSpecifierWithPushbackStream() throws Throwable {
        // Test decoding and getting specifier with a pushback input stream
        BHSDCodec char3Codec = Codec.CHAR3;
        byte[] byteArray = new byte[5];
        byteArray[2] = (byte) 101;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        char3Codec.decode(byteArrayInputStream);
        byteArrayInputStream.read();
        PushbackInputStream pushbackInputStream = new PushbackInputStream(byteArrayInputStream, 141);
        Codec codec = CodecEncoding.getCodec((byte) 116, pushbackInputStream, char3Codec);
        PopulationCodec populationCodec = new PopulationCodec(codec, 64, char3Codec);

        CodecEncoding.getSpecifier(populationCodec, populationCodec);

        assertEquals(1, char3Codec.lastBandLength);
        assertNotSame(char3Codec, codec);
    }

    @Test(timeout = TIMEOUT)
    public void testGetSpecifierWithMDELTA5() throws Throwable {
        // Test getting specifier for MDELTA5 codec
        BHSDCodec mdelta5Codec = Codec.MDELTA5;
        int[] specifier = CodecEncoding.getSpecifier(mdelta5Codec, mdelta5Codec);

        assertEquals(1, specifier.length);
    }

    @Test(timeout = TIMEOUT)
    public void testGetCodecWithUnconnectedPipe() throws Throwable {
        // Test getting a codec with an unconnected pipe
        PipedInputStream pipedInputStream = new PipedInputStream();
        BHSDCodec udelta5Codec = Codec.UDELTA5;

        try {
            CodecEncoding.getCodec(146, pipedInputStream, udelta5Codec);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("java.io.PipedInputStream", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testGetCodecWithNullParameters() throws Throwable {
        // Test getting a codec with null parameters
        try {
            CodecEncoding.getCodec(181, null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.harmony.pack200.CodecEncoding", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testGetCodecWithByteArray() throws Throwable {
        // Test getting a codec with a byte array
        BHSDCodec mdelta5Codec = Codec.MDELTA5;
        byte[] byteArray = new byte[6];
        byteArray[0] = (byte) (-88);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);

        CodecEncoding.getCodec(141, byteArrayInputStream, mdelta5Codec);

        assertEquals(3, byteArrayInputStream.available());
    }

    @Test(timeout = TIMEOUT)
    public void testGetCodecWithByteArrayAndMDELTA5() throws Throwable {
        // Test getting a codec with a byte array and MDELTA5 codec
        BHSDCodec mdelta5Codec = Codec.MDELTA5;
        byte[] byteArray = new byte[16];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);

        CodecEncoding.getCodec(144, byteArrayInputStream, mdelta5Codec);

        assertEquals(15, byteArrayInputStream.available());
    }

    @Test(timeout = TIMEOUT)
    public void testGetCodecWithInvalidEncoding() throws Throwable {
        // Test getting a codec with an invalid encoding value
        BHSDCodec delta5Codec = Codec.DELTA5;
        byte[] byteArray = new byte[1];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);

        try {
            CodecEncoding.getCodec(257, byteArrayInputStream, delta5Codec);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.compress.harmony.pack200.CodecEncoding", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testGetCodecWithPushbackStream() throws Throwable {
        // Test getting a codec with a pushback input stream
        byte[] byteArray = new byte[10];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        BHSDCodec udelta5Codec = Codec.UDELTA5;
        PushbackInputStream pushbackInputStream = new PushbackInputStream(byteArrayInputStream, 224);
        PopulationCodec populationCodec = new PopulationCodec(udelta5Codec, (byte) 116, udelta5Codec);

        RunCodec runCodec = (RunCodec) CodecEncoding.getCodec(140, pushbackInputStream, populationCodec);

        assertEquals(8, byteArrayInputStream.available());
        assertEquals(4096, runCodec.getK());
    }

    @Test(timeout = TIMEOUT)
    public void testGetCodecWithByteArrayAndUNSIGNED5() throws Throwable {
        // Test getting a codec with a byte array and UNSIGNED5 codec
        byte[] byteArray = new byte[8];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        BHSDCodec unsigned5Codec = Codec.UNSIGNED5;

        BHSDCodec codec = (BHSDCodec) CodecEncoding.getCodec(1, byteArrayInputStream, unsigned5Codec);

        assertEquals(0L, codec.smallest());
        assertNotNull(codec);
    }

    @Test(timeout = TIMEOUT)
    public void testGetCodecWithEOFException() throws Throwable {
        // Test getting a codec with an EOFException
        byte[] byteArray = new byte[8];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        byteArrayInputStream.read(byteArray);
        BHSDCodec unsigned5Codec = Codec.UNSIGNED5;

        try {
            CodecEncoding.getCodec((byte) 116, byteArrayInputStream, unsigned5Codec);
            fail("Expecting exception: EOFException");
        } catch (EOFException e) {
            verifyException("org.apache.commons.compress.harmony.pack200.CodecEncoding", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testGetCodecWithEOFExceptionAndMDELTA5() throws Throwable {
        // Test getting a codec with an EOFException and MDELTA5 codec
        byte[] byteArray = new byte[1];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        BHSDCodec mdelta5Codec = Codec.MDELTA5;

        try {
            CodecEncoding.getCodec((byte) 116, byteArrayInputStream, mdelta5Codec);
            fail("Expecting exception: EOFException");
        } catch (EOFException e) {
            verifyException("org.apache.commons.compress.harmony.pack200.CodecEncoding", e);
        }
    }

    @Test(timeout = TIMEOUT)
    public void testGetSpecifierForDefaultCodecWithCHAR3() throws Throwable {
        // Test getting specifier for default codec with CHAR3
        BHSDCodec char3Codec = Codec.CHAR3;
        int specifier = CodecEncoding.getSpecifierForDefaultCodec(char3Codec);

        assertEquals(116, specifier);
    }

    @Test(timeout = TIMEOUT)
    public void testCodecEncodingConstructor() throws Throwable {
        // Test the constructor of CodecEncoding
        CodecEncoding codecEncoding = new CodecEncoding();
    }

    @Test(timeout = TIMEOUT)
    public void testGetCanonicalCodecWithOutOfBounds() throws Throwable {
        // Test getting canonical codec with an out-of-bounds index
        try {
            CodecEncoding.getCanonicalCodec(260);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.apache.commons.compress.harmony.pack200.CodecEncoding", e);
        }
    }
}