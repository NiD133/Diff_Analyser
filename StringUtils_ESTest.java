/*
 * Test suite for StringUtils class - PDF string manipulation utilities
 * Tests character-to-byte conversion and string escaping functionality
 */

package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.itextpdf.text.pdf.ByteBuffer;
import com.itextpdf.text.pdf.StringUtils;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class StringUtils_ESTest extends StringUtils_ESTest_scaffolding {

    // ========== Character to Byte Conversion Tests ==========
    
    @Test(timeout = 4000)
    public void testConvertCharsToBytes_WithNonEmptyArray_DoublesLength() throws Throwable {
        // Given: A char array with 8 elements, first element is 'h'
        char[] inputChars = new char[8];
        inputChars[0] = 'h';
        
        // When: Converting chars to bytes
        byte[] resultBytes = StringUtils.convertCharsToBytes(inputChars);
        
        // Then: Byte array should be twice the length (16-bit chars -> 2 bytes each)
        assertEquals("Byte array should be twice the length of char array", 16, resultBytes.length);
    }

    @Test(timeout = 4000)
    public void testConvertCharsToBytes_WithEmptyArray_ReturnsEmptyArray() throws Throwable {
        // Given: An empty char array
        char[] emptyChars = new char[0];
        
        // When: Converting empty array to bytes
        byte[] resultBytes = StringUtils.convertCharsToBytes(emptyChars);
        
        // Then: Result should also be empty
        assertEquals("Empty char array should produce empty byte array", 0, resultBytes.length);
    }

    @Test(timeout = 4000)
    public void testConvertCharsToBytes_WithNullInput_ThrowsNullPointerException() throws Throwable {
        // Given: Null char array
        char[] nullChars = null;
        
        // When & Then: Should throw NullPointerException
        try { 
            StringUtils.convertCharsToBytes(nullChars);
            fail("Expected NullPointerException for null input");
        } catch(NullPointerException e) {
            verifyException("com.itextpdf.text.pdf.StringUtils", e);
        }
    }

    // ========== String Escaping Tests ==========
    
    @Test(timeout = 4000)
    public void testEscapeString_WithNullByteBuffer_ThrowsNullPointerException() throws Throwable {
        // Given: Valid byte array but null ByteBuffer
        byte[] validBytes = new byte[3];
        ByteBuffer nullBuffer = null;
        
        // When & Then: Should throw NullPointerException
        try { 
            StringUtils.escapeString(validBytes, nullBuffer);
            fail("Expected NullPointerException for null ByteBuffer");
        } catch(NullPointerException e) {
            verifyException("com.itextpdf.text.pdf.StringUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testEscapeString_WithInvalidBufferState_ThrowsArrayIndexOutOfBoundsException() throws Throwable {
        // Given: Empty byte array and ByteBuffer with invalid negative count
        byte[] emptyBytes = new byte[0];
        ByteBuffer corruptedBuffer = new ByteBuffer();
        corruptedBuffer.count = -27; // Invalid state
        
        // When & Then: Should throw ArrayIndexOutOfBoundsException
        try { 
            StringUtils.escapeString(emptyBytes, corruptedBuffer);
            fail("Expected ArrayIndexOutOfBoundsException for corrupted buffer");
        } catch(ArrayIndexOutOfBoundsException e) {
            verifyException("com.itextpdf.text.pdf.ByteBuffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testEscapeString_WithNullByteArray_ThrowsNullPointerException() throws Throwable {
        // Given: Null byte array
        byte[] nullBytes = null;
        
        // When & Then: Should throw NullPointerException
        try { 
            StringUtils.escapeString(nullBytes);
            fail("Expected NullPointerException for null byte array");
        } catch(NullPointerException e) {
            verifyException("com.itextpdf.text.pdf.StringUtils", e);
        }
    }

    // ========== Special Character Escaping Tests ==========
    
    @Test(timeout = 4000)
    public void testEscapeString_WithFormFeedCharacter_EscapesCorrectly() throws Throwable {
        // Given: Byte array containing form feed character (byte 12)
        byte[] bytesWithFormFeed = new byte[6];
        bytesWithFormFeed[4] = (byte) 12; // Form feed character
        ByteBuffer outputBuffer = new ByteBuffer((byte) -35);
        
        // When: Escaping the string
        StringUtils.escapeString(bytesWithFormFeed, outputBuffer);
        
        // Then: Buffer size should increase due to escaping
        assertEquals("Buffer should contain escaped form feed", 9, outputBuffer.size());
    }

    @Test(timeout = 4000)
    public void testEscapeString_WithBackspaceCharacter_EscapesCorrectly() throws Throwable {
        // Given: Byte array containing backspace character (byte 8)
        byte[] bytesWithBackspace = new byte[8];
        bytesWithBackspace[3] = (byte) 8; // Backspace character
        ByteBuffer outputBuffer = new ByteBuffer();
        
        // When: Escaping the string
        StringUtils.escapeString(bytesWithBackspace, outputBuffer);
        
        // Then: Buffer size should increase due to escaping
        assertEquals("Buffer should contain escaped backspace", 11, outputBuffer.size());
    }

    @Test(timeout = 4000)
    public void testEscapeString_WithCarriageReturnCharacter_EscapesCorrectly() throws Throwable {
        // Given: Byte array containing carriage return character (byte 13)
        byte[] bytesWithCarriageReturn = new byte[5];
        bytesWithCarriageReturn[2] = (byte) 13; // Carriage return character
        ByteBuffer outputBuffer = new ByteBuffer();
        
        // When: Escaping the string
        StringUtils.escapeString(bytesWithCarriageReturn, outputBuffer);
        
        // Then: Buffer size should increase due to escaping
        assertEquals("Buffer should contain escaped carriage return", 8, outputBuffer.size());
    }

    @Test(timeout = 4000)
    public void testEscapeString_WithTabCharacter_ProducesExpectedEscapeSequence() throws Throwable {
        // Given: Byte array containing tab character (byte 9)
        byte[] bytesWithTab = new byte[4];
        bytesWithTab[1] = (byte) 9; // Tab character
        ByteBuffer outputBuffer = new ByteBuffer(71);
        
        // When: First escape to byte array, then escape again to buffer
        byte[] firstEscapeResult = StringUtils.escapeString(bytesWithTab);
        StringUtils.escapeString(firstEscapeResult, outputBuffer);
        
        // Then: Should produce expected escape sequence and buffer size
        assertEquals("Buffer should contain double-escaped tab", 12, outputBuffer.size());
        assertArrayEquals("First escape should produce expected sequence", 
            new byte[] {(byte)40, (byte)0, (byte)92, (byte)116, (byte)0, (byte)0, (byte)41}, 
            firstEscapeResult);
    }

    @Test(timeout = 4000)
    public void testEscapeString_WithNewlineCharacter_EscapesCorrectly() throws Throwable {
        // Given: Byte array containing newline character (byte 10)
        ByteBuffer outputBuffer = new ByteBuffer((byte) 10);
        byte[] bytesWithNewline = new byte[7];
        bytesWithNewline[5] = (byte) 10; // Newline character
        
        // When: Escaping the string
        StringUtils.escapeString(bytesWithNewline, outputBuffer);
        
        // Then: Buffer size should increase due to escaping
        assertEquals("Buffer should contain escaped newline", 10, outputBuffer.size());
    }

    @Test(timeout = 4000)
    public void testEscapeString_WithTabCharacterDirectly_EscapesCorrectly() throws Throwable {
        // Given: Byte array containing tab character (byte 9)
        byte[] bytesWithTab = new byte[4];
        bytesWithTab[1] = (byte) 9; // Tab character
        ByteBuffer outputBuffer = new ByteBuffer(71);
        
        // When: Escaping the string directly
        StringUtils.escapeString(bytesWithTab, outputBuffer);
        
        // Then: Buffer size should increase due to escaping
        assertEquals("Buffer should contain escaped tab", 7, outputBuffer.size());
    }
}