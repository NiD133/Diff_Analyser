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

/**
 * Test suite for CodecEncoding class which handles codec encoding/decoding
 * for Pack200 compression format.
 */
@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true) 
public class CodecEncoding_ESTest extends CodecEncoding_ESTest_scaffolding {

    // Test constants for better readability
    private static final int CANONICAL_CODEC_CHAR3_INDEX = 116;
    private static final int CANONICAL_CODEC_SIGNED5_INDEX = 115;
    private static final int RUN_CODEC_K_VALUE_256 = 256;
    private static final int RUN_CODEC_K_VALUE_4 = 4;
    
    // ========== Tests for getSpecifier() method ==========
    
    @Test(timeout = 4000)
    public void testGetSpecifier_WithRunCodecAndChar3Codec_ReturnsCorrectSpecifier() throws Throwable {
        // Given: A RunCodec with K=256 using CHAR3 codec for both A and B codecs
        BHSDCodec char3Codec = Codec.CHAR3;
        RunCodec runCodec = new RunCodec(RUN_CODEC_K_VALUE_256, char3Codec, char3Codec);
        
        // When: Getting specifier for the RunCodec with CHAR3 as default
        int[] specifier = CodecEncoding.getSpecifier(runCodec, char3Codec);
        
        // Then: Should return the expected specifier array
        assertNotNull("Specifier should not be null", specifier);
        assertArrayEquals("Should return correct specifier for RunCodec", 
                         new int[] {129, 255, 116, 16, 127}, specifier);
    }

    @Test(timeout = 4000)
    public void testGetSpecifier_WithRunCodecAndSigned5_ReturnsCorrectSpecifier() throws Throwable {
        // Given: A RunCodec with K=4 using SIGNED5 codec
        BHSDCodec signed5Codec = Codec.SIGNED5;
        RunCodec runCodec = new RunCodec(RUN_CODEC_K_VALUE_4, signed5Codec, signed5Codec);
        
        // When: Getting specifier with SIGNED5 as default
        int[] specifier = CodecEncoding.getSpecifier(runCodec, signed5Codec);
        
        // Then: Should return correct specifier
        assertNotNull("Specifier should not be null", specifier);
        assertArrayEquals("Should return correct specifier for SIGNED5 RunCodec", 
                         new int[] {125, 27}, specifier);
    }

    @Test(timeout = 4000)
    public void testGetSpecifier_WithBHSDCodec_ReturnsCorrectSpecifier() throws Throwable {
        // Given: A CHAR3 codec
        BHSDCodec char3Codec = Codec.CHAR3;
        
        // When: Getting specifier for the codec itself
        int[] specifier = CodecEncoding.getSpecifier(char3Codec, char3Codec);
        
        // Then: Should return the canonical specifier
        assertArrayEquals("Should return canonical specifier for CHAR3", 
                         new int[] {116, 16, 127}, specifier);
    }

    @Test(timeout = 4000)
    public void testGetSpecifier_WithPopulationCodec_ReturnsCorrectLength() throws Throwable {
        // Given: A PopulationCodec using MDELTA5
        BHSDCodec mdelta5Codec = Codec.MDELTA5;
        PopulationCodec populationCodec = new PopulationCodec(mdelta5Codec, mdelta5Codec, mdelta5Codec);
        
        // When: Getting specifier for PopulationCodec
        int[] specifier = CodecEncoding.getSpecifier(populationCodec, mdelta5Codec);
        
        // Then: Should return specifier with expected length
        assertEquals("PopulationCodec specifier should have length 2", 2, specifier.length);
    }

    @Test(timeout = 4000)
    public void testGetSpecifier_WithNullDefaultCodec_ThrowsNullPointerException() throws Throwable {
        // Given: A PopulationCodec with null codecs
        PopulationCodec populationCodec = new PopulationCodec(null, null, null);
        
        // When/Then: Getting specifier with null default should throw NPE
        try {
            CodecEncoding.getSpecifier(populationCodec, null);
            fail("Should throw NullPointerException for null default codec");
        } catch(NullPointerException e) {
            verifyException("org.apache.commons.compress.harmony.pack200.CodecEncoding", e);
        }
    }

    // ========== Tests for getSpecifierForDefaultCodec() method ==========
    
    @Test(timeout = 4000)
    public void testGetSpecifierForDefaultCodec_WithChar3_ReturnsCorrectIndex() throws Throwable {
        // Given: CHAR3 codec
        BHSDCodec char3Codec = Codec.CHAR3;
        
        // When: Getting specifier for default codec
        int specifier = CodecEncoding.getSpecifierForDefaultCodec(char3Codec);
        
        // Then: Should return the canonical index
        assertEquals("CHAR3 codec should have specifier 116", 
                    CANONICAL_CODEC_CHAR3_INDEX, specifier);
    }

    @Test(timeout = 4000)
    public void testGetSpecifierForDefaultCodec_WithNull_ReturnsZero() throws Throwable {
        // When: Getting specifier for null codec
        int specifier = CodecEncoding.getSpecifierForDefaultCodec(null);
        
        // Then: Should return 0
        assertEquals("Null codec should return specifier 0", 0, specifier);
    }

    // ========== Tests for getCanonicalCodec() method ==========
    
    @Test(timeout = 4000)
    public void testGetCanonicalCodec_WithZero_ReturnsNull() throws Throwable {
        // When: Getting canonical codec for index 0
        BHSDCodec codec = CodecEncoding.getCanonicalCodec(0);
        
        // Then: Should return null (index 0 is reserved)
        assertNull("Canonical codec at index 0 should be null", codec);
    }

    @Test(timeout = 4000)
    public void testGetCanonicalCodec_WithValidIndex_ReturnsCodec() throws Throwable {
        // When: Getting canonical codec for valid index
        BHSDCodec codec = CodecEncoding.getCanonicalCodec(40);
        
        // Then: Should return a codec with expected properties
        assertNotNull("Should return a codec for valid index", codec);
        assertEquals("Codec should have S parameter of 2", 2, codec.getS());
    }

    @Test(timeout = 4000)
    public void testGetCanonicalCodec_WithAnotherValidIndex_ReturnsUnsignedCodec() throws Throwable {
        // When: Getting canonical codec for index 9
        BHSDCodec codec = CodecEncoding.getCanonicalCodec(9);
        
        // Then: Should return unsigned codec
        assertNotNull("Should return a codec for index 9", codec);
        assertFalse("Codec at index 9 should be unsigned", codec.isSigned());
    }

    @Test(timeout = 4000)
    public void testGetCanonicalCodec_WithInvalidIndex_ThrowsArrayIndexOutOfBounds() throws Throwable {
        // When/Then: Getting canonical codec for invalid index should throw exception
        try {
            CodecEncoding.getCanonicalCodec(260);
            fail("Should throw ArrayIndexOutOfBoundsException for invalid index");
        } catch(ArrayIndexOutOfBoundsException e) {
            assertEquals("Exception should contain the invalid index", "260", e.getMessage());
        }
    }

    // ========== Tests for getCodec() method ==========
    
    @Test(timeout = 4000)
    public void testGetCodec_WithZeroValue_ReturnsNull() throws Throwable {
        // Given: A piped input stream and null default codec
        PipedOutputStream pipedOut = new PipedOutputStream();
        PipedInputStream pipedIn = new PipedInputStream(pipedOut);
        
        // When: Getting codec with value 0
        Codec codec = CodecEncoding.getCodec(0, pipedIn, null);
        
        // Then: Should return null (use default)
        assertNull("Codec value 0 should return null", codec);
    }

    @Test(timeout = 4000)
    public void testGetCodec_WithCanonicalValue_ReturnsCanonicalCodec() throws Throwable {
        // Given: Empty sequence input stream and CHAR3 default codec
        @SuppressWarnings("unchecked")
        Enumeration<BufferedInputStream> emptyEnum = mock(Enumeration.class, new ViolatedAssumptionAnswer());
        doReturn(false).when(emptyEnum).hasMoreElements();
        SequenceInputStream sequenceIn = new SequenceInputStream(emptyEnum);
        BHSDCodec defaultCodec = Codec.CHAR3;
        
        // When: Getting codec with canonical value 115
        BHSDCodec resultCodec = (BHSDCodec) CodecEncoding.getCodec(CANONICAL_CODEC_SIGNED5_INDEX, 
                                                                   sequenceIn, defaultCodec);
        
        // Then: Should return signed codec
        assertNotNull("Should return a codec", resultCodec);
        assertTrue("Codec should be signed", resultCodec.isSigned());
    }

    @Test(timeout = 4000)
    public void testGetCodec_WithNegativeValue_ThrowsIllegalArgumentException() throws Throwable {
        // Given: Byte array input stream and UNSIGNED5 codec
        byte[] data = new byte[3];
        ByteArrayInputStream byteIn = new ByteArrayInputStream(data, 1024, 3294);
        BHSDCodec defaultCodec = Codec.UNSIGNED5;
        
        // When/Then: Negative encoding value should throw exception
        try {
            CodecEncoding.getCodec(-2300, byteIn, defaultCodec);
            fail("Should throw IllegalArgumentException for negative encoding");
        } catch(IllegalArgumentException e) {
            assertEquals("Should have correct error message", 
                        "Encoding cannot be less than zero", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testGetCodec_WithNullInputStream_ThrowsNullPointerException() throws Throwable {
        // Given: Null input stream and BYTE1 codec
        BHSDCodec defaultCodec = Codec.BYTE1;
        
        // When/Then: Null input stream should throw NPE
        try {
            CodecEncoding.getCodec(128, null, defaultCodec);
            fail("Should throw NullPointerException for null input stream");
        } catch(NullPointerException e) {
            verifyException("org.apache.commons.compress.harmony.pack200.CodecEncoding", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetCodec_WithInvalidEncodingByte_ThrowsIOException() throws Throwable {
        // Given: Single byte input stream and DELTA5 codec
        byte[] data = new byte[1];
        ByteArrayInputStream byteIn = new ByteArrayInputStream(data);
        BHSDCodec defaultCodec = Codec.DELTA5;
        
        // When/Then: Invalid encoding byte should throw IOException
        try {
            CodecEncoding.getCodec(257, byteIn, defaultCodec);
            fail("Should throw IOException for invalid encoding byte");
        } catch(IOException e) {
            assertEquals("Should have correct error message", 
                        "Invalid codec encoding byte (257) found", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testGetCodec_WithEmptyInputStream_ThrowsEOFException() throws Throwable {
        // Given: Empty byte array input stream
        byte[] emptyData = new byte[8];
        ByteArrayInputStream byteIn = new ByteArrayInputStream(emptyData);
        byteIn.read(emptyData); // Consume all data
        BHSDCodec defaultCodec = Codec.UNSIGNED5;
        
        // When/Then: Reading from empty stream should throw EOFException
        try {
            CodecEncoding.getCodec(116, byteIn, defaultCodec);
            fail("Should throw EOFException when stream is empty");
        } catch(EOFException e) {
            assertEquals("Should have correct error message", 
                        "End of buffer read whilst trying to decode codec", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testGetCodec_WithDisconnectedPipe_ThrowsIOException() throws Throwable {
        // Given: Disconnected piped input stream
        PipedInputStream pipedIn = new PipedInputStream();
        BHSDCodec defaultCodec = Codec.UDELTA5;
        
        // When/Then: Reading from disconnected pipe should throw IOException
        try {
            CodecEncoding.getCodec(146, pipedIn, defaultCodec);
            fail("Should throw IOException for disconnected pipe");
        } catch(IOException e) {
            assertEquals("Should have correct error message", 
                        "Pipe not connected", e.getMessage());
        }
    }

    // ========== Tests for successful codec creation ==========
    
    @Test(timeout = 4000)
    public void testGetCodec_WithValidData_CreatesRunCodec() throws Throwable {
        // Given: Byte array with valid data for RunCodec creation
        byte[] data = new byte[10];
        ByteArrayInputStream byteIn = new ByteArrayInputStream(data);
        BHSDCodec defaultCodec = Codec.UDELTA5;
        PushbackInputStream pushbackIn = new PushbackInputStream(byteIn, 224);
        PopulationCodec populationCodec = new PopulationCodec(defaultCodec, (byte)116, defaultCodec);
        
        // When: Creating RunCodec with encoding 140
        RunCodec runCodec = (RunCodec) CodecEncoding.getCodec(140, pushbackIn, populationCodec);
        
        // Then: Should create RunCodec with expected properties
        assertNotNull("Should create RunCodec", runCodec);
        assertEquals("RunCodec should have K value of 4096", 4096, runCodec.getK());
        assertEquals("Should consume 2 bytes from stream", 8, byteIn.available());
    }

    @Test(timeout = 4000)
    public void testGetCodec_WithCanonicalEncoding_CreatesBHSDCodec() throws Throwable {
        // Given: Byte array input stream with sufficient data
        byte[] data = new byte[8];
        ByteArrayInputStream byteIn = new ByteArrayInputStream(data);
        BHSDCodec defaultCodec = Codec.UNSIGNED5;
        
        // When: Creating codec with canonical encoding 1
        BHSDCodec resultCodec = (BHSDCodec) CodecEncoding.getCodec(1, byteIn, defaultCodec);
        
        // Then: Should create codec with expected properties
        assertNotNull("Should create BHSDCodec", resultCodec);
        assertEquals("Codec should have smallest value of 0", 0L, resultCodec.smallest());
    }

    // ========== Constructor test ==========
    
    @Test(timeout = 4000)
    public void testConstructor_CreatesInstance() throws Throwable {
        // When: Creating CodecEncoding instance
        CodecEncoding codecEncoding = new CodecEncoding();
        
        // Then: Should create instance successfully
        assertNotNull("Should create CodecEncoding instance", codecEncoding);
    }

    // ========== Complex scenario tests ==========
    
    @Test(timeout = 4000)
    public void testComplexScenario_WithMultipleCodecOperations() throws Throwable {
        // Given: Complex setup with multiple codec operations
        BHSDCodec char3Codec = Codec.CHAR3;
        byte[] data = new byte[19];
        data[1] = (byte) 101; // Set specific byte value
        ByteArrayInputStream byteIn = new ByteArrayInputStream(data);
        
        // Perform initial decode operation
        BHSDCodec canonicalCodec = CodecEncoding.getCanonicalCodec(30);
        canonicalCodec.decode(byteIn);
        
        // Create RunCodec and get another codec
        RunCodec runCodec = new RunCodec(67, canonicalCodec, canonicalCodec);
        Codec resultCodec = CodecEncoding.getCodec(116, byteIn, runCodec);
        
        // When: Getting specifier for the result codec
        int[] specifier = CodecEncoding.getSpecifier(resultCodec, null);
        
        // Then: Operations should complete successfully
        assertNotNull("Should return specifier", specifier);
        assertEquals("Should have consumed correct amount of data", 16, byteIn.available());
        assertEquals("Canonical codec should have processed 1 band", 1, canonicalCodec.lastBandLength);
    }
}