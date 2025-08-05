package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test suite for ByteUtils class functionality.
 * Tests little-endian byte conversion operations in both directions.
 */
public class ByteUtils_ESTest {

    // Constants for test clarity
    private static final int SINGLE_BYTE = 1;
    private static final int TWO_BYTES = 2;
    private static final int FOUR_BYTES = 4;
    private static final int EIGHT_BYTES = 8;
    private static final int MAX_LONG_BYTES = 8;

    // ========== toLittleEndian Tests ==========

    @Test
    public void testToLittleEndian_OutputStreamWithLargeByteCount_WritesCorrectNumberOfBytes() throws IOException {
        // Given: An output stream and a large byte count
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        long value = -392L;
        int byteCount = 1633;
        
        // When: Converting to little endian
        ByteUtils.toLittleEndian(outputStream, value, byteCount);
        
        // Then: Correct number of bytes are written
        assertEquals("Should write exactly the requested number of bytes", 
                    byteCount, outputStream.size());
    }

    @Test
    public void testToLittleEndian_OutputStreamWithNegativeByteCount_HandlesGracefully() throws IOException {
        // Given: An output stream and negative byte count
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        // When: Converting with negative byte count
        ByteUtils.toLittleEndian(outputStream, 8L, -1);
        
        // Then: No exception is thrown (method handles negative counts gracefully)
        assertEquals("Should not write any bytes for negative count", 0, outputStream.size());
    }

    @Test
    public void testToLittleEndian_DataOutputWithNegativeByteCount_WritesNoBytes() throws IOException {
        // Given: A DataOutput and negative byte count
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataOutput = new DataOutputStream(byteStream);
        
        // When: Converting with negative byte count
        ByteUtils.toLittleEndian(dataOutput, -1257L, -2038);
        
        // Then: No bytes are written
        assertEquals("Should not write any bytes for negative count", 0, byteStream.size());
    }

    @Test
    public void testToLittleEndian_ByteArrayWithSingleByte_StoresValueCorrectly() {
        // Given: A byte array and a value to store
        byte[] bytes = new byte[2];
        long value = -30L;
        
        // When: Converting single byte to little endian
        ByteUtils.toLittleEndian(bytes, value, 1, SINGLE_BYTE);
        
        // Then: Value is stored correctly in little endian format
        assertArrayEquals("Should store -30 as little endian bytes", 
                         new byte[]{0, (byte) -30}, bytes);
    }

    @Test
    public void testToLittleEndian_ByteConsumerWithNullConsumer_HandlesGracefully() {
        // Given: A null consumer
        // When/Then: Should handle null consumer without throwing exception
        ByteUtils.toLittleEndian((ByteUtils.ByteConsumer) null, 0L, -2448);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testToLittleEndian_ByteArrayWithInsufficientSpace_ThrowsException() {
        // Given: A small byte array and request to write beyond its bounds
        byte[] smallArray = new byte[3];
        
        // When: Trying to write more bytes than array can hold
        // Then: Should throw ArrayIndexOutOfBoundsException
        ByteUtils.toLittleEndian(smallArray, 1L, 1, 63);
    }

    @Test(expected = NullPointerException.class)
    public void testToLittleEndian_NullByteArray_ThrowsNullPointerException() {
        // When/Then: Should throw NPE for null array
        ByteUtils.toLittleEndian((byte[]) null, -2448L, 8, 8);
    }

    @Test(expected = NullPointerException.class)
    public void testToLittleEndian_NullOutputStream_ThrowsNullPointerException() throws IOException {
        // When/Then: Should throw NPE for null output stream
        ByteUtils.toLittleEndian((OutputStream) null, -1L, 3);
    }

    // ========== fromLittleEndian Tests ==========

    @Test
    public void testFromLittleEndian_InputStreamWithMultipleBytes_ReadsCorrectValue() throws IOException {
        // Given: A byte array with specific pattern and input stream
        byte[] testData = new byte[7];
        testData[2] = (byte) 91;  // Set specific byte for testing
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        
        // When: Reading 4 bytes as little endian
        long result = ByteUtils.fromLittleEndian(inputStream, FOUR_BYTES);
        
        // Then: Correct value is read and stream position is updated
        assertEquals("Should read correct little endian value", 5963776L, result);
        assertEquals("Should have correct remaining bytes", 3, inputStream.available());
    }

    @Test
    public void testFromLittleEndian_DataInputWithSpecificPattern_ReadsCorrectValue() throws IOException {
        // Given: Byte array with specific pattern
        byte[] testData = new byte[5];
        testData[1] = (byte) -32;
        ByteArrayInputStream byteStream = new ByteArrayInputStream(testData);
        DataInputStream dataInput = new DataInputStream(byteStream);
        
        // When: Reading all bytes as little endian
        long result = ByteUtils.fromLittleEndian(dataInput, 5);
        
        // Then: Correct value is computed
        assertEquals("Should read correct little endian value", 57344L, result);
        assertEquals("Should consume all bytes", 0, byteStream.available());
    }

    @Test
    public void testFromLittleEndian_NegativeByteCount_ReturnsZero() throws IOException {
        // Given: Any data input with negative byte count
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[5]);
        DataInputStream dataInput = new DataInputStream(inputStream);
        
        // When: Reading with negative count
        long result = ByteUtils.fromLittleEndian(dataInput, -325);
        
        // Then: Returns zero
        assertEquals("Should return 0 for negative byte count", 0L, result);
    }

    @Test
    public void testFromLittleEndian_ByteArrayWithSpecificValue_ReadsCorrectly() {
        // Given: Byte array with specific negative value
        byte[] testData = new byte[8];
        testData[1] = (byte) -68;
        
        // When: Reading single byte from offset
        long result = ByteUtils.fromLittleEndian(testData, 1, SINGLE_BYTE);
        
        // Then: Negative byte is read as unsigned value
        assertEquals("Should read -68 as unsigned 188", 188L, result);
    }

    @Test
    public void testFromLittleEndian_FullByteArray_HandlesLargeValues() {
        // Given: 8-byte array with value in specific position
        byte[] testData = new byte[8];
        testData[5] = (byte) -5;
        
        // When: Reading entire array as long
        long result = ByteUtils.fromLittleEndian(testData);
        
        // Then: Correct large value is computed
        assertEquals("Should compute correct large value", 275977418571776L, result);
    }

    @Test
    public void testFromLittleEndian_ByteArrayWithNegativeResult_HandlesSignBit() {
        // Given: 8-byte array with high bit set
        byte[] testData = new byte[8];
        testData[7] = (byte) -72;  // Sets the sign bit
        
        // When: Reading entire array
        long result = ByteUtils.fromLittleEndian(testData);
        
        // Then: Result is negative due to sign bit
        assertEquals("Should handle sign bit correctly", -5188146770730811392L, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromLittleEndian_TooManyBytes_ThrowsIllegalArgumentException() {
        // Given: Byte array larger than max long size
        byte[] oversizedArray = new byte[18];
        
        // When/Then: Should throw exception for arrays > 8 bytes
        ByteUtils.fromLittleEndian(oversizedArray);
    }

    @Test(expected = EOFException.class)
    public void testFromLittleEndian_InsufficientData_ThrowsEOFException() throws IOException {
        // Given: Small data array and request for more bytes
        byte[] smallData = new byte[2];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(smallData);
        DataInputStream dataInput = new DataInputStream(inputStream);
        
        // When/Then: Should throw EOFException when not enough data
        ByteUtils.fromLittleEndian(dataInput, 6);
    }

    @Test
    public void testFromLittleEndian_InsufficientStreamData_ThrowsIOException() throws IOException {
        // Given: Stream with insufficient data
        byte[] smallData = new byte[6];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(smallData);
        
        try {
            // When: Requesting more bytes than available
            ByteUtils.fromLittleEndian(inputStream, EIGHT_BYTES);
            fail("Should throw IOException for insufficient data");
        } catch (IOException e) {
            // Then: Correct error message
            assertEquals("Should indicate premature end of data", 
                        "Premature end of data", e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testFromLittleEndian_NullByteArray_ThrowsNullPointerException() {
        // When/Then: Should throw NPE for null array
        ByteUtils.fromLittleEndian((byte[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromLittleEndian_ExcessiveByteCount_ThrowsIllegalArgumentException() {
        // When/Then: Should throw exception for byte count > 8
        ByteUtils.fromLittleEndian((ByteUtils.ByteSupplier) null, 1781);
    }

    // ========== OutputStreamByteConsumer Tests ==========

    @Test
    public void testOutputStreamByteConsumer_AcceptByte_WritesToStream() throws IOException {
        // Given: Output stream and consumer
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteUtils.OutputStreamByteConsumer consumer = 
            new ByteUtils.OutputStreamByteConsumer(outputStream);
        
        // When: Accepting a byte value
        consumer.accept(1);
        
        // Then: Byte is written to stream
        assertEquals("Should write single byte to stream", "\u0001", outputStream.toString());
    }

    @Test
    public void testOutputStreamByteConsumer_WithDataOutputStream_WritesCorrectly() throws IOException {
        // Given: DataOutputStream wrapped consumer
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataOutput = new DataOutputStream(byteStream);
        ByteUtils.OutputStreamByteConsumer consumer = 
            new ByteUtils.OutputStreamByteConsumer(dataOutput);
        
        // When: Writing multiple bytes through consumer
        ByteUtils.toLittleEndian(consumer, -2038L, 20);
        
        // Then: Correct number of bytes written
        assertEquals("Should write exactly 20 bytes", 20, byteStream.size());
    }

    // ========== Edge Cases and Boundary Tests ==========

    @Test
    public void testFromLittleEndian_ZeroLengthByteArray_ReturnsZero() {
        // Given: Empty byte array (2 bytes, all zeros)
        byte[] emptyData = new byte[2];
        
        // When: Reading as little endian
        long result = ByteUtils.fromLittleEndian(emptyData);
        
        // Then: Returns zero
        assertEquals("Should return 0 for zero-filled array", 0L, result);
    }

    @Test
    public void testFromLittleEndian_NegativeLength_ReturnsZero() throws IOException {
        // Given: Input stream with negative read length
        byte[] testData = new byte[5];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        
        // When: Reading with negative length
        long result = ByteUtils.fromLittleEndian(inputStream, -1);
        
        // Then: Returns zero and doesn't consume stream
        assertEquals("Should return 0 for negative length", 0L, result);
        assertEquals("Should not consume any bytes", 5, inputStream.available());
    }

    @Test
    public void testToLittleEndian_NegativeLength_NoOperation() {
        // Given: Byte array and negative length
        byte[] testData = new byte[5];
        
        // When: Writing with negative length
        ByteUtils.toLittleEndian(testData, -10L, 8, -2448);
        
        // Then: Array remains unchanged (all zeros)
        assertArrayEquals("Array should remain unchanged", new byte[5], testData);
    }
}