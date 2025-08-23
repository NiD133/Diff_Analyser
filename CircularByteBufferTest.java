package org.apache.commons.io.input.buffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link CircularByteBuffer}.
 */
class CircularByteBufferTest {

    /**
     * Tests adding a single byte to the smallest possible buffer.
     */
    @Test
    void testAddSingleByteToSmallestBuffer() {
        final CircularByteBuffer buffer = new CircularByteBuffer(1);
        buffer.add((byte) 1);
        assertEquals(1, buffer.read(), "Buffer should return the first byte added.");
        
        buffer.add((byte) 2);
        assertEquals(2, buffer.read(), "Buffer should return the second byte added.");
    }

    /**
     * Tests adding bytes with an invalid negative offset.
     */
    @Test
    void testAddBytesWithNegativeOffset() {
        final CircularByteBuffer buffer = new CircularByteBuffer();
        assertThrows(IllegalArgumentException.class, 
            () -> buffer.add(new byte[] { 1, 2, 3 }, -1, 3),
            "Adding with a negative offset should throw IllegalArgumentException.");
    }

    /**
     * Tests adding bytes with a negative length.
     */
    @Test
    void testAddBytesWithNegativeLength() {
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] data = { 1, 2, 3 };
        assertThrows(IllegalArgumentException.class, 
            () -> buffer.add(data, 0, -1),
            "Adding with a negative length should throw IllegalArgumentException.");
    }

    /**
     * Tests adding a null byte array.
     */
    @Test
    void testAddNullByteArray() {
        final CircularByteBuffer buffer = new CircularByteBuffer();
        assertThrows(NullPointerException.class, 
            () -> buffer.add(null, 0, 3),
            "Adding a null byte array should throw NullPointerException.");
    }

    /**
     * Tests adding valid data to the buffer.
     */
    @Test
    void testAddValidData() {
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] data = { 3, 6, 9 };
        buffer.add(data, 0, data.length);
        assertEquals(data.length, buffer.getCurrentNumberOfBytes(), 
            "Buffer should contain the correct number of bytes after adding valid data.");
    }

    /**
     * Tests clearing the buffer.
     */
    @Test
    void testClearBuffer() {
        final byte[] data = { 1, 2, 3 };
        final CircularByteBuffer buffer = new CircularByteBuffer(10);
        
        buffer.add(data, 0, data.length);
        assertEquals(3, buffer.getCurrentNumberOfBytes(), "Buffer should have 3 bytes after adding data.");
        
        buffer.clear();
        assertEquals(0, buffer.getCurrentNumberOfBytes(), "Buffer should be empty after clearing.");
        assertEquals(10, buffer.getSpace(), "Buffer should have full space after clearing.");
    }

    /**
     * Tests checking buffer space availability.
     */
    @Test
    void testBufferSpaceAvailability() {
        final CircularByteBuffer buffer = new CircularByteBuffer(1);
        assertTrue(buffer.hasSpace(), "Buffer should have space initially.");
        
        buffer.add((byte) 1);
        assertFalse(buffer.hasSpace(), "Buffer should not have space after adding a byte.");
        
        buffer.read();
        assertTrue(buffer.hasSpace(), "Buffer should have space after reading a byte.");
    }

    /**
     * Tests peeking with excessive length.
     */
    @Test
    void testPeekWithExcessiveLength() {
        final CircularByteBuffer buffer = new CircularByteBuffer();
        assertFalse(buffer.peek(new byte[] { 1, 3, 5, 7, 9 }, 0, 6), 
            "Peeking with excessive length should return false.");
    }

    /**
     * Tests peeking with an invalid negative offset.
     */
    @Test
    void testPeekWithInvalidOffset() {
        final CircularByteBuffer buffer = new CircularByteBuffer();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> buffer.peek(new byte[] { 2, 4, 6, 8, 10 }, -1, 5),
            "Peeking with a negative offset should throw IllegalArgumentException.");
        assertEquals("Illegal offset: -1", exception.getMessage());
    }

    /**
     * Tests peeking with a negative length.
     */
    @Test
    void testPeekWithNegativeLength() {
        final CircularByteBuffer buffer = new CircularByteBuffer();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> buffer.peek(new byte[] { 1, 4, 3 }, 0, -1),
            "Peeking with a negative length should throw IllegalArgumentException.");
        assertEquals("Illegal length: -1", exception.getMessage());
    }

    /**
     * Tests reading bytes into an array.
     */
    @Test
    void testReadBytesIntoArray() {
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final String inputString = "0123456789";
        final byte[] inputBytes = inputString.getBytes(StandardCharsets.UTF_8);
        
        buffer.add(inputBytes, 0, inputBytes.length);
        
        final byte[] outputBytes = new byte[10];
        buffer.read(outputBytes, 0, outputBytes.length);
        
        assertEquals(inputString, new String(outputBytes, StandardCharsets.UTF_8), 
            "Reading bytes should return the original string.");
    }

    /**
     * Tests reading bytes with invalid arguments.
     */
    @Test
    void testReadBytesWithInvalidArguments() {
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] outputBytes = new byte[10];
        
        assertThrows(IllegalArgumentException.class, 
            () -> buffer.read(outputBytes, -1, 10),
            "Reading with a negative offset should throw IllegalArgumentException.");
        
        assertThrows(IllegalArgumentException.class, 
            () -> buffer.read(outputBytes, 0, outputBytes.length + 1),
            "Reading with a length exceeding buffer size should throw IllegalArgumentException.");
    }
}