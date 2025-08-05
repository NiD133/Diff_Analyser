package org.apache.commons.io.input.buffer;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for CircularByteBuffer functionality.
 * Tests cover buffer operations including add, read, peek, and space management.
 */
public class CircularByteBufferTest {

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    // ========== Constructor Tests ==========

    @Test
    public void testDefaultConstructor() {
        CircularByteBuffer buffer = new CircularByteBuffer();
        
        assertEquals("Default buffer should have full space available", 
                     DEFAULT_BUFFER_SIZE, buffer.getSpace());
        assertEquals("New buffer should be empty", 0, buffer.getCurrentNumberOfBytes());
        assertFalse("New buffer should not have bytes", buffer.hasBytes());
        assertTrue("New buffer should have space", buffer.hasSpace());
    }

    @Test
    public void testConstructorWithCustomSize() {
        int customSize = 1024;
        CircularByteBuffer buffer = new CircularByteBuffer(customSize);
        
        assertEquals("Custom buffer should have specified space", 
                     customSize, buffer.getSpace());
        assertEquals("New buffer should be empty", 0, buffer.getCurrentNumberOfBytes());
    }

    @Test
    public void testConstructorWithZeroSize() {
        CircularByteBuffer buffer = new CircularByteBuffer(0);
        
        assertEquals("Zero-size buffer should have no space", 0, buffer.getSpace());
        assertFalse("Zero-size buffer should not have space", buffer.hasSpace());
    }

    @Test(expected = NegativeArraySizeException.class)
    public void testConstructorWithNegativeSize() {
        new CircularByteBuffer(-1);
    }

    // ========== Add Single Byte Tests ==========

    @Test
    public void testAddSingleByte() {
        CircularByteBuffer buffer = new CircularByteBuffer();
        
        buffer.add((byte) 42);
        
        assertEquals("Buffer should contain one byte", 1, buffer.getCurrentNumberOfBytes());
        assertEquals("Available space should decrease by one", 
                     DEFAULT_BUFFER_SIZE - 1, buffer.getSpace());
        assertTrue("Buffer should have bytes", buffer.hasBytes());
    }

    @Test
    public void testAddMultipleBytes() {
        CircularByteBuffer buffer = new CircularByteBuffer();
        
        buffer.add((byte) 1);
        buffer.add((byte) 2);
        
        assertEquals("Buffer should contain two bytes", 2, buffer.getCurrentNumberOfBytes());
        assertTrue("Buffer should have bytes", buffer.hasBytes());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddByteToFullBuffer() {
        CircularByteBuffer buffer = new CircularByteBuffer(0);
        buffer.add((byte) 1); // Should throw exception
    }

    // ========== Add Byte Array Tests ==========

    @Test
    public void testAddByteArray() {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] data = {1, 2, 3, 4, 5};
        
        buffer.add(data, 1, 3); // Add bytes at indices 1, 2, 3
        
        assertEquals("Buffer should contain three bytes", 3, buffer.getCurrentNumberOfBytes());
        assertEquals("Available space should decrease by three", 
                     DEFAULT_BUFFER_SIZE - 3, buffer.getSpace());
    }

    @Test
    public void testAddEmptyByteArray() {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] data = {1, 2, 3};
        
        buffer.add(data, 0, 0); // Add zero bytes
        
        assertEquals("Buffer should remain empty", 0, buffer.getCurrentNumberOfBytes());
        assertEquals("Space should remain unchanged", DEFAULT_BUFFER_SIZE, buffer.getSpace());
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullByteArray() {
        CircularByteBuffer buffer = new CircularByteBuffer();
        buffer.add(null, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddByteArrayWithNegativeOffset() {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] data = {1, 2, 3};
        buffer.add(data, -1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddByteArrayWithNegativeLength() {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] data = {1, 2, 3};
        buffer.add(data, 0, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddByteArrayWithInvalidOffset() {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] data = {1, 2, 3};
        buffer.add(data, 5, 1); // Offset beyond array bounds
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testAddByteArrayWithExcessiveLength() {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] data = {1, 2, 3};
        buffer.add(data, 0, 5); // Length exceeds array size
    }

    @Test(expected = IllegalStateException.class)
    public void testAddByteArrayToFullBuffer() {
        CircularByteBuffer buffer = new CircularByteBuffer(2);
        byte[] data = {1, 2, 3, 4, 5};
        buffer.add(data, 0, 5); // Try to add more than buffer capacity
    }

    // ========== Read Single Byte Tests ==========

    @Test
    public void testReadSingleByte() {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte expectedValue = 42;
        
        buffer.add(expectedValue);
        byte actualValue = buffer.read();
        
        assertEquals("Read should return the added byte", expectedValue, actualValue);
        assertEquals("Buffer should be empty after read", 0, buffer.getCurrentNumberOfBytes());
        assertEquals("Space should be restored after read", DEFAULT_BUFFER_SIZE, buffer.getSpace());
        assertFalse("Buffer should not have bytes after read", buffer.hasBytes());
    }

    @Test
    public void testReadMultipleBytes() {
        CircularByteBuffer buffer = new CircularByteBuffer();
        
        buffer.add((byte) 1);
        buffer.add((byte) 2);
        
        assertEquals("First read should return first byte", (byte) 1, buffer.read());
        assertEquals("Buffer should have one byte remaining", 1, buffer.getCurrentNumberOfBytes());
        assertTrue("Buffer should still have bytes", buffer.hasBytes());
        
        assertEquals("Second read should return second byte", (byte) 2, buffer.read());
        assertFalse("Buffer should be empty after reading all bytes", buffer.hasBytes());
    }

    @Test(expected = IllegalStateException.class)
    public void testReadFromEmptyBuffer() {
        CircularByteBuffer buffer = new CircularByteBuffer();
        buffer.read(); // Should throw exception
    }

    // ========== Read Byte Array Tests ==========

    @Test
    public void testReadByteArray() {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] sourceData = {10, 20, 30, 40};
        byte[] targetData = new byte[10];
        
        buffer.add(sourceData, 0, sourceData.length);
        buffer.read(targetData, 2, 3); // Read 3 bytes starting at index 2
        
        assertEquals("First read byte should match", 10, targetData[2]);
        assertEquals("Second read byte should match", 20, targetData[3]);
        assertEquals("Third read byte should match", 30, targetData[4]);
        assertEquals("Buffer should have one byte remaining", 1, buffer.getCurrentNumberOfBytes());
    }

    @Test
    public void testReadZeroBytes() {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] targetData = new byte[5];
        
        buffer.read(targetData, 0, 0); // Read zero bytes
        
        assertEquals("Buffer should remain unchanged", DEFAULT_BUFFER_SIZE, buffer.getSpace());
    }

    @Test(expected = NullPointerException.class)
    public void testReadToNullArray() {
        CircularByteBuffer buffer = new CircularByteBuffer();
        buffer.read(null, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadWithNegativeOffset() {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] targetData = new byte[5];
        buffer.read(targetData, -1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadWithNegativeLength() {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] targetData = new byte[5];
        buffer.read(targetData, 0, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadWithInvalidOffset() {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] targetData = new byte[5];
        buffer.read(targetData, 10, 1); // Offset beyond array bounds
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadWithExcessiveLength() {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] targetData = new byte[5];
        buffer.read(targetData, 0, 10); // Length exceeds array size
    }

    @Test(expected = IllegalStateException.class)
    public void testReadMoreBytesThanAvailable() {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] targetData = new byte[5];
        
        buffer.add((byte) 1); // Add only one byte
        buffer.read(targetData, 0, 2); // Try to read two bytes
    }

    // ========== Peek Tests ==========

    @Test
    public void testPeekWithMatchingBytes() {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] sourceData = {1, 2, 3, 4};
        byte[] peekData = {1, 2, 3};
        
        buffer.add(sourceData, 0, sourceData.length);
        boolean matches = buffer.peek(peekData, 0, 3);
        
        assertTrue("Peek should return true for matching bytes", matches);
        assertEquals("Peek should not consume bytes", 4, buffer.getCurrentNumberOfBytes());
    }

    @Test
    public void testPeekWithNonMatchingBytes() {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] sourceData = {1, 2, 3, 4};
        byte[] peekData = {1, 2, 5}; // Third byte doesn't match
        
        buffer.add(sourceData, 0, sourceData.length);
        boolean matches = buffer.peek(peekData, 0, 3);
        
        assertFalse("Peek should return false for non-matching bytes", matches);
        assertEquals("Peek should not consume bytes", 4, buffer.getCurrentNumberOfBytes());
    }

    @Test
    public void testPeekWithInsufficientBytes() {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] peekData = {1, 2, 3};
        
        buffer.add((byte) 1); // Add only one byte
        boolean matches = buffer.peek(peekData, 0, 3); // Try to peek three bytes
        
        assertFalse("Peek should return false when insufficient bytes", matches);
    }

    @Test
    public void testPeekZeroBytes() {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] peekData = new byte[5];
        
        boolean matches = buffer.peek(peekData, 0, 0);
        
        assertTrue("Peek of zero bytes should always return true", matches);
    }

    @Test(expected = NullPointerException.class)
    public void testPeekWithNullArray() {
        CircularByteBuffer buffer = new CircularByteBuffer();
        buffer.peek(null, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPeekWithNegativeOffset() {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] peekData = new byte[5];
        buffer.peek(peekData, -1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPeekWithNegativeLength() {
        CircularByteBuffer buffer = new CircularByteBuffer();
        byte[] peekData = new byte[5];
        buffer.peek(peekData, 0, -1);
    }

    // ========== Space Management Tests ==========

    @Test
    public void testHasSpaceWithSpecificCount() {
        CircularByteBuffer buffer = new CircularByteBuffer(10);
        
        assertTrue("Buffer should have space for 5 bytes", buffer.hasSpace(5));
        assertTrue("Buffer should have space for 10 bytes", buffer.hasSpace(10));
        assertFalse("Buffer should not have space for 15 bytes", buffer.hasSpace(15));
    }

    @Test
    public void testHasSpaceAfterAddingBytes() {
        CircularByteBuffer buffer = new CircularByteBuffer(5);
        
        buffer.add((byte) 1);
        buffer.add((byte) 2);
        
        assertTrue("Buffer should have space for 2 bytes", buffer.hasSpace(2));
        assertTrue("Buffer should have space for 3 bytes", buffer.hasSpace(3));
        assertFalse("Buffer should not have space for 4 bytes", buffer.hasSpace(4));
    }

    // ========== Clear Tests ==========

    @Test
    public void testClear() {
        CircularByteBuffer buffer = new CircularByteBuffer();
        
        // Add some data
        buffer.add((byte) 1);
        buffer.add((byte) 2);
        buffer.add((byte) 3);
        
        // Clear the buffer
        buffer.clear();
        
        assertEquals("Cleared buffer should be empty", 0, buffer.getCurrentNumberOfBytes());
        assertEquals("Cleared buffer should have full space", DEFAULT_BUFFER_SIZE, buffer.getSpace());
        assertFalse("Cleared buffer should not have bytes", buffer.hasBytes());
        assertTrue("Cleared buffer should have space", buffer.hasSpace());
    }

    // ========== Integration Tests ==========

    @Test
    public void testAddReadCycle() {
        CircularByteBuffer buffer = new CircularByteBuffer(5);
        
        // Fill buffer completely
        for (int i = 0; i < 5; i++) {
            buffer.add((byte) i);
        }
        assertFalse("Full buffer should not have space", buffer.hasSpace());
        
        // Read some bytes to make space
        assertEquals("First read should return 0", (byte) 0, buffer.read());
        assertEquals("Second read should return 1", (byte) 1, buffer.read());
        assertTrue("Buffer should have space after reading", buffer.hasSpace());
        
        // Add more bytes
        buffer.add((byte) 10);
        buffer.add((byte) 11);
        
        // Verify remaining bytes can be read in correct order
        assertEquals("Should read 2", (byte) 2, buffer.read());
        assertEquals("Should read 3", (byte) 3, buffer.read());
        assertEquals("Should read 4", (byte) 4, buffer.read());
        assertEquals("Should read 10", (byte) 10, buffer.read());
        assertEquals("Should read 11", (byte) 11, buffer.read());
        
        assertFalse("Buffer should be empty", buffer.hasBytes());
    }
}