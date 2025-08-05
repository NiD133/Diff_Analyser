package com.itextpdf.text.io;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Test suite for GetBufferedRandomAccessSource - a buffered wrapper for RandomAccessSource implementations.
 * Tests cover basic functionality, edge cases, error conditions, and buffering behavior.
 */
public class GetBufferedRandomAccessSourceTest {

    // Test data constants
    private static final byte[] EMPTY_ARRAY = new byte[0];
    private static final byte[] SMALL_ARRAY = new byte[8];
    private static final byte[] MEDIUM_ARRAY = new byte[16];
    private static final byte TEST_BYTE_VALUE = (byte) -56;
    private static final int EXPECTED_UNSIGNED_VALUE = 200; // -56 as unsigned byte

    // === Basic Functionality Tests ===

    @Test
    public void shouldReturnCorrectLengthForNormalArray() throws Exception {
        // Given: A buffered source wrapping an 8-byte array
        ArrayRandomAccessSource arraySource = new ArrayRandomAccessSource(SMALL_ARRAY);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
        
        // When: Getting the length
        long length = bufferedSource.length();
        
        // Then: Should return the correct length
        assertEquals("Length should match the underlying array size", 8L, length);
    }

    @Test
    public void shouldReadSingleByteCorrectly() throws Exception {
        // Given: An array with a specific byte value at position 0
        byte[] testArray = new byte[8];
        testArray[0] = TEST_BYTE_VALUE;
        ArrayRandomAccessSource arraySource = new ArrayRandomAccessSource(testArray);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
        
        // When: Reading the byte at position 0
        int result = bufferedSource.get(0L);
        
        // Then: Should return the unsigned byte value
        assertEquals("Should convert signed byte to unsigned int", EXPECTED_UNSIGNED_VALUE, result);
    }

    @Test
    public void shouldReadMultipleBytesCorrectly() throws Exception {
        // Given: A 16-byte array and a buffered source
        ArrayRandomAccessSource arraySource = new ArrayRandomAccessSource(MEDIUM_ARRAY);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
        
        // When: Reading all bytes into the same array
        int bytesRead = bufferedSource.get(0L, MEDIUM_ARRAY, 0, 610); // Request more than available
        
        // Then: Should read exactly the available bytes
        assertEquals("Should read all available bytes", 16, bytesRead);
    }

    // === Buffering Behavior Tests ===

    @Test
    public void shouldUseCacheForRepeatedReads() throws Exception {
        // Given: A buffered source with data
        byte[] testArray = new byte[15];
        ArrayRandomAccessSource arraySource = new ArrayRandomAccessSource(testArray);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
        
        // When: Reading the same position twice
        bufferedSource.get(0L); // First read - loads buffer
        int secondRead = bufferedSource.get(0L); // Second read - should use cache
        
        // Then: Should return the same value (0 for uninitialized array)
        assertEquals("Cached read should return same value", 0, secondRead);
    }

    // === Edge Cases and Boundary Tests ===

    @Test
    public void shouldHandleEmptySource() throws Exception {
        // Given: An empty array source
        ArrayRandomAccessSource emptySource = new ArrayRandomAccessSource(EMPTY_ARRAY);
        WindowRandomAccessSource windowSource = new WindowRandomAccessSource(emptySource, -782L, -782L);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(windowSource);
        
        // When: Reading from negative position
        int result = bufferedSource.get(-1L);
        
        // Then: Should return 0 (implementation-specific behavior)
        assertEquals("Reading from negative position should return 0", 0, result);
    }

    @Test
    public void shouldReturnNegativeLengthForInvalidWindow() throws Exception {
        // Given: A window source with negative dimensions
        ArrayRandomAccessSource emptySource = new ArrayRandomAccessSource(EMPTY_ARRAY);
        WindowRandomAccessSource windowSource = new WindowRandomAccessSource(emptySource, -782L, -782L);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(windowSource);
        
        // When: Getting length
        long length = bufferedSource.length();
        
        // Then: Should return the negative length from the window
        assertEquals("Should return negative length from invalid window", -782L, length);
    }

    @Test
    public void shouldReturnMinusOneForOutOfBoundsRead() throws Exception {
        // Given: An empty windowed source
        ArrayRandomAccessSource emptySource = new ArrayRandomAccessSource(EMPTY_ARRAY);
        WindowRandomAccessSource windowSource = new WindowRandomAccessSource(emptySource, -782L, -782L);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(windowSource);
        
        // When: Reading beyond bounds
        int result = bufferedSource.get(3322L);
        
        // Then: Should return -1 indicating end of stream
        assertEquals("Out of bounds read should return -1", -1, result);
    }

    @Test
    public void shouldReturnZeroForZeroLengthRead() throws Exception {
        // Given: A buffered source with data
        byte[] testArray = new byte[15];
        ArrayRandomAccessSource arraySource = new ArrayRandomAccessSource(testArray);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
        
        // When: Reading zero bytes
        int bytesRead = bufferedSource.get(0L, testArray, 0, 0);
        
        // Then: Should return 0
        assertEquals("Zero-length read should return 0", 0, bytesRead);
    }

    // === Error Condition Tests ===

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionForNullSource() {
        // When: Creating buffered source with null
        // Then: Should throw NullPointerException
        new GetBufferedRandomAccessSource(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionForNullByteArray() throws Exception {
        // Given: A valid buffered source
        ArrayRandomAccessSource arraySource = new ArrayRandomAccessSource(new byte[4]);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
        
        // When: Reading into null array
        // Then: Should throw NullPointerException
        bufferedSource.get(-1780L, null, -213, -213);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenAccessingClosedSource() throws Exception {
        // Given: A closed source
        ArrayRandomAccessSource arraySource = new ArrayRandomAccessSource(new byte[1]);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
        bufferedSource.close();
        
        // When: Trying to get length after closing
        // Then: Should throw IllegalStateException
        bufferedSource.length();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenReadingFromClosedSource() throws Exception {
        // Given: A closed source
        ArrayRandomAccessSource arraySource = new ArrayRandomAccessSource(new byte[8]);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
        bufferedSource.close();
        
        // When: Trying to read after closing
        // Then: Should throw IllegalStateException
        bufferedSource.get(-155L);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void shouldThrowExceptionForInvalidArrayAccess() throws Exception {
        // Given: A buffered source
        ArrayRandomAccessSource arraySource = new ArrayRandomAccessSource(new byte[1]);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
        
        // When: Reading with invalid array indices
        // Then: Should throw ArrayIndexOutOfBoundsException
        bufferedSource.get(0L, new byte[1], -328, 762);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void shouldThrowExceptionForNegativeBufferAccess() throws Exception {
        // Given: A buffered source that has been read from (buffer initialized)
        ArrayRandomAccessSource arraySource = new ArrayRandomAccessSource(new byte[8]);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(arraySource);
        bufferedSource.get(0L); // Initialize buffer
        
        // When: Accessing with negative position
        // Then: Should throw ArrayIndexOutOfBoundsException
        bufferedSource.get(-1L);
    }

    @Test(expected = IOException.class)
    public void shouldThrowIOExceptionForUnopenedMappedChannel() throws Exception {
        // Given: An unopened mapped channel source
        MappedChannelRandomAccessSource mappedSource = new MappedChannelRandomAccessSource(null, 2782L, 2782L);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(mappedSource);
        
        // When: Trying to read from unopened source
        // Then: Should throw IOException
        bufferedSource.get(2782L, new byte[1], -27, 0);
    }

    // === Integration Tests with Different Source Types ===

    @Test
    public void shouldHandleZeroLengthWindowSource() throws Exception {
        // Given: A zero-length window source
        WindowRandomAccessSource windowSource = new WindowRandomAccessSource(null, 0L, 0L);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(windowSource);
        
        // When: Getting length
        long length = bufferedSource.length();
        
        // Then: Should return 0
        assertEquals("Zero-length window should have length 0", 0L, length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForPositionTooLarge() throws Exception {
        // Given: A ByteBuffer source
        ByteBuffer buffer = ByteBuffer.wrap(new byte[2]);
        ByteBufferRandomAccessSource byteBufferSource = new ByteBufferRandomAccessSource(buffer);
        GetBufferedRandomAccessSource bufferedSource = new GetBufferedRandomAccessSource(byteBufferSource);
        
        // When: Accessing position larger than Integer.MAX_VALUE
        // Then: Should throw IllegalArgumentException
        bufferedSource.get(2147483651L); // Integer.MAX_VALUE + 4
    }
}