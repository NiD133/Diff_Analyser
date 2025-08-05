package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PipedOutputStream;
import java.io.PipedWriter;
import java.io.StringWriter;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;
import org.evosuite.runtime.mock.java.io.MockFileWriter;
import org.evosuite.runtime.mock.java.io.MockPrintStream;

/**
 * Test suite for HexDump utility class.
 * Tests various scenarios including normal operation, error conditions, and edge cases.
 */
public class HexDump_ESTest {

    // Test data constants
    private static final byte[] EMPTY_ARRAY = new byte[0];
    private static final byte[] SMALL_ARRAY = new byte[3];
    private static final byte[] MEDIUM_ARRAY = new byte[20];
    
    // ===========================================
    // SUCCESSFUL OPERATION TESTS
    // ===========================================
    
    @Test(timeout = 4000)
    public void testDumpToOutputStream_WithNegativeOffset() throws Throwable {
        // Given: A small byte array and file output stream
        byte[] data = new byte[3];
        MockFile outputFile = new MockFile("test_output.txt");
        MockFileOutputStream fileStream = new MockFileOutputStream(outputFile);
        MockPrintStream printStream = new MockPrintStream(fileStream);
        
        // When: Dumping with negative offset
        HexDump.dump(data, -1131L, printStream, 0);
        
        // Then: Output should be written to file
        assertEquals("Expected specific file size after hex dump", 61L, outputFile.length());
    }

    @Test(timeout = 4000)
    public void testDumpToAppendable_WithNonZeroByte() throws Throwable {
        // Given: Array with a non-zero byte value
        byte[] data = new byte[7];
        data[0] = (byte) -1; // 0xFF
        MockPrintStream output = new MockPrintStream("\n");
        
        // When: Dumping to appendable
        HexDump.dump(data, output);
        
        // Then: Should complete without error
        assertEquals("Array length should remain unchanged", 7, data.length);
    }

    @Test(timeout = 4000)
    public void testDumpWithCustomParameters() throws Throwable {
        // Given: 8-byte array and custom output parameters
        byte[] data = new byte[8];
        MockFileOutputStream fileStream = new MockFileOutputStream("\n", false);
        OutputStreamWriter writer = new OutputStreamWriter(fileStream);
        
        // When: Dumping with custom offset, index, and length
        HexDump.dump(data, -1L, writer, 6, 0);
        
        // Then: Should complete successfully
        assertArrayEquals("Data should remain unchanged", 
                         new byte[8], data);
    }

    @Test(timeout = 4000)
    public void testDumpWithSpecificByteValue() throws Throwable {
        // Given: Array with specific byte value at position 4
        byte[] data = new byte[8];
        data[4] = (byte) 127; // Max positive byte value
        File tempFile = MockFile.createTempFile("hexdump_test", ".tmp");
        MockPrintStream output = new MockPrintStream(tempFile);
        
        // When: Dumping with custom parameters
        HexDump.dump(data, 59L, output, 3, 3);
        
        // Then: Should produce expected output size
        assertEquals("Expected specific output file size", 61L, tempFile.length());
    }

    @Test(timeout = 4000)
    public void testDumpWithVariousParameters() throws Throwable {
        // Given: Large array with specific byte value
        byte[] data = new byte[37];
        data[0] = (byte) 110;
        MockFileWriter writer = new MockFileWriter("\n");
        
        // When: Dumping with matching offset and custom range
        HexDump.dump(data, 110L, writer, 0, 1);
        
        // Then: Should complete successfully
        assertEquals("Array size should remain unchanged", 37, data.length);
    }

    // ===========================================
    // ERROR CONDITION TESTS
    // ===========================================
    
    @Test(timeout = 4000)
    public void testDumpToReadOnlyBuffer_ThrowsException() throws Throwable {
        // Given: Read-only CharBuffer and byte array
        byte[] data = new byte[5];
        CharBuffer readOnlyBuffer = CharBuffer.wrap("\n");
        
        // When/Then: Should throw ReadOnlyBufferException
        try {
            HexDump.dump(data, readOnlyBuffer);
            fail("Expected ReadOnlyBufferException");
        } catch (ReadOnlyBufferException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testDumpToSmallBuffer_ThrowsBufferOverflow() throws Throwable {
        // Given: Buffer too small for hex dump output
        byte[] data = new byte[10];
        char[] smallCharArray = new char[2];
        CharBuffer smallBuffer = CharBuffer.wrap(smallCharArray);
        
        // When/Then: Should throw BufferOverflowException
        try {
            HexDump.dump(data, smallBuffer);
            fail("Expected BufferOverflowException");
        } catch (BufferOverflowException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testDumpToClosedWriter_ThrowsIOException() throws Throwable {
        // Given: Closed file writer
        byte[] data = new byte[42];
        MockFileWriter writer = new MockFileWriter("\n");
        writer.close(); // Close the writer
        
        // When/Then: Should throw IOException
        try {
            HexDump.dump(data, 0L, writer, 1, 1);
            fail("Expected IOException for closed writer");
        } catch (IOException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testDumpToUnconnectedPipe_ThrowsIOException() throws Throwable {
        // Given: Unconnected piped output stream
        byte[] data = new byte[37];
        PipedOutputStream unconnectedPipe = new PipedOutputStream();
        
        // When/Then: Should throw IOException
        try {
            HexDump.dump(data, 804L, unconnectedPipe, 16);
            fail("Expected IOException for unconnected pipe");
        } catch (IOException e) {
            assertEquals("Pipe not connected", e.getMessage());
        }
    }

    // ===========================================
    // NULL PARAMETER TESTS
    // ===========================================
    
    @Test(timeout = 4000)
    public void testDumpWithNullByteArray_ThrowsNullPointerException() throws Throwable {
        // Given: Null byte array
        MockPrintStream output = new MockPrintStream("\n");
        
        // When/Then: Should throw NullPointerException
        try {
            HexDump.dump(null, -2841L, output, 198);
            fail("Expected NullPointerException for null byte array");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testDumpWithNullOutputStream_ThrowsNullPointerException() throws Throwable {
        // Given: Valid byte array but null output stream
        byte[] data = new byte[9];
        
        // When/Then: Should throw NullPointerException
        try {
            HexDump.dump(data, 2185L, null, 2185);
            fail("Expected NullPointerException for null stream");
        } catch (NullPointerException e) {
            assertEquals("stream", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testDumpWithNullAppendable_ThrowsNullPointerException() throws Throwable {
        // Given: Valid byte array but null appendable
        byte[] data = new byte[20];
        
        // When/Then: Should throw NullPointerException
        try {
            HexDump.dump(data, null);
            fail("Expected NullPointerException for null appendable");
        } catch (NullPointerException e) {
            assertEquals("appendable", e.getMessage());
        }
    }

    // ===========================================
    // BOUNDARY AND EDGE CASE TESTS
    // ===========================================
    
    @Test(timeout = 4000)
    public void testDumpWithInvalidArrayIndex_ThrowsArrayIndexOutOfBounds() throws Throwable {
        // Given: Array and index beyond bounds
        byte[] data = new byte[20];
        ByteArrayOutputStream output = new ByteArrayOutputStream(146);
        
        // When/Then: Should throw ArrayIndexOutOfBoundsException
        try {
            HexDump.dump(data, -1587L, output, 146); // Index 146 > array length 20
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertTrue("Should mention illegal index", 
                      e.getMessage().contains("illegal index: 146 into array of length 20"));
        }
    }

    @Test(timeout = 4000)
    public void testDumpWithNegativeIndex_ThrowsArrayIndexOutOfBounds() throws Throwable {
        // Given: Array and negative index
        byte[] data = new byte[8];
        MockPrintStream output = new MockPrintStream("\n");
        
        // When/Then: Should throw ArrayIndexOutOfBoundsException
        try {
            HexDump.dump(data, -706L, output, -706, -706);
            fail("Expected ArrayIndexOutOfBoundsException for negative index");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertTrue("Should mention illegal negative index", 
                      e.getMessage().contains("illegal index: -706"));
        }
    }

    @Test(timeout = 4000)
    public void testDumpWithEmptyArray_ThrowsArrayIndexOutOfBounds() throws Throwable {
        // Given: Empty byte array
        PipedWriter writer = new PipedWriter();
        byte[] emptyData = new byte[0];
        
        // When/Then: Should throw ArrayIndexOutOfBoundsException
        try {
            HexDump.dump(emptyData, writer);
            fail("Expected ArrayIndexOutOfBoundsException for empty array");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertTrue("Should mention array length 0", 
                      e.getMessage().contains("array of length 0"));
        }
    }

    @Test(timeout = 4000)
    public void testDumpWithInvalidRange_ThrowsArrayIndexOutOfBounds() throws Throwable {
        // Given: Array and range that exceeds bounds
        byte[] data = new byte[25];
        MockPrintStream output = new MockPrintStream("\n");
        
        // When/Then: Should throw ArrayIndexOutOfBoundsException
        try {
            HexDump.dump(data, 1506L, output, 13, 13); // Range [13, 26) exceeds length 25
            fail("Expected ArrayIndexOutOfBoundsException for invalid range");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertTrue("Should mention range bounds", 
                      e.getMessage().contains("Range [13, 13 + 13) out of bounds"));
        }
    }

    // ===========================================
    // CONSTRUCTOR TEST
    // ===========================================
    
    @Test(timeout = 4000)
    public void testHexDumpConstructor() throws Throwable {
        // When: Creating HexDump instance
        HexDump hexDump = new HexDump();
        
        // Then: Should create successfully (utility class with public constructor)
        assertNotNull("HexDump instance should be created", hexDump);
    }
}