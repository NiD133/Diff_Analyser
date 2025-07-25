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

    @Test
    void testAddSingleByteToSmallestBuffer() {
        final CircularByteBuffer buffer = new CircularByteBuffer(1);
        buffer.add((byte) 1);
        assertEquals(1, buffer.read(), "Buffer should return the byte that was added");
        
        buffer.add((byte) 2);
        assertEquals(2, buffer.read(), "Buffer should return the new byte that was added");
    }

    @Test
    void testAddWithInvalidOffsetThrowsException() {
        final CircularByteBuffer buffer = new CircularByteBuffer();
        assertThrows(IllegalArgumentException.class, 
            () -> buffer.add(new byte[] { 1, 2, 3 }, -1, 3),
            "Adding with a negative offset should throw IllegalArgumentException");
    }

    @Test
    void testAddWithNegativeLengthThrowsException() {
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] data = { 1, 2, 3 };
        assertThrows(IllegalArgumentException.class, 
            () -> buffer.add(data, 0, -1),
            "Adding with a negative length should throw IllegalArgumentException");
    }

    @Test
    void testAddNullBufferThrowsException() {
        final CircularByteBuffer buffer = new CircularByteBuffer();
        assertThrows(NullPointerException.class, 
            () -> buffer.add(null, 0, 3),
            "Adding a null buffer should throw NullPointerException");
    }

    @Test
    void testAddValidDataIncreasesBufferSize() {
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final int length = 3;
        buffer.add(new byte[] { 3, 6, 9 }, 0, length);
        assertEquals(length, buffer.getCurrentNumberOfBytes(), "Buffer size should match the number of added bytes");
    }

    @Test
    void testClearEmptiesBuffer() {
        final byte[] data = { 1, 2, 3 };
        final CircularByteBuffer buffer = new CircularByteBuffer(10);
        
        buffer.add(data, 0, data.length);
        assertEquals(3, buffer.getCurrentNumberOfBytes(), "Buffer should contain 3 bytes after adding data");
        
        buffer.clear();
        assertEquals(0, buffer.getCurrentNumberOfBytes(), "Buffer should be empty after clear");
        assertEquals(10, buffer.getSpace(), "Buffer should have full space after clear");
    }

    @Test
    void testHasSpaceForSingleByte() {
        final CircularByteBuffer buffer = new CircularByteBuffer(1);
        assertTrue(buffer.hasSpace(), "Buffer should have space initially");
        
        buffer.add((byte) 1);
        assertFalse(buffer.hasSpace(), "Buffer should not have space after adding a byte");
        
        buffer.read();
        assertTrue(buffer.hasSpace(), "Buffer should have space after reading a byte");
    }

    @Test
    void testHasSpaceForSpecificNumberOfBytes() {
        final CircularByteBuffer buffer = new CircularByteBuffer(1);
        assertTrue(buffer.hasSpace(1), "Buffer should have space for 1 byte initially");
        
        buffer.add((byte) 1);
        assertFalse(buffer.hasSpace(1), "Buffer should not have space for 1 byte after adding a byte");
        
        buffer.read();
        assertTrue(buffer.hasSpace(1), "Buffer should have space for 1 byte after reading a byte");
    }

    @Test
    void testPeekWithExcessiveLengthReturnsFalse() {
        assertFalse(new CircularByteBuffer().peek(new byte[] { 1, 3, 5, 7, 9 }, 0, 6),
            "Peeking with excessive length should return false");
    }

    @Test
    void testPeekWithInvalidOffsetThrowsException() {
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> buffer.peek(new byte[] { 2, 4, 6, 8, 10 }, -1, 5),
            "Peeking with a negative offset should throw IllegalArgumentException");
        assertEquals("Illegal offset: -1", exception.getMessage());
    }

    @Test
    void testPeekWithNegativeLengthThrowsException() {
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> buffer.peek(new byte[] { 1, 4, 3 }, 0, -1),
            "Peeking with a negative length should throw IllegalArgumentException");
        assertEquals("Illegal length: -1", exception.getMessage());
    }

    @Test
    void testPeekWithValidArgumentsReturnsFalse() {
        assertFalse(new CircularByteBuffer().peek(new byte[] { 5, 10, 15, 20, 25 }, 0, 5),
            "Peeking with valid arguments should return false when buffer is empty");
    }

    @Test
    void testReadByteArrayMatchesInput() {
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final String inputString = "0123456789";
        final byte[] inputBytes = inputString.getBytes(StandardCharsets.UTF_8);
        
        buffer.add(inputBytes, 0, inputBytes.length);
        
        final byte[] outputBytes = new byte[inputBytes.length];
        buffer.read(outputBytes, 0, outputBytes.length);
        
        assertEquals(inputString, new String(outputBytes, StandardCharsets.UTF_8), 
            "Output bytes should match input string");
    }

    @Test
    void testReadByteArrayWithInvalidArgumentsThrowsException() {
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] outputBytes = new byte[10];
        
        assertThrows(IllegalArgumentException.class, 
            () -> buffer.read(outputBytes, -1, 10),
            "Reading with a negative target offset should throw IllegalArgumentException");
        
        assertThrows(IllegalArgumentException.class, 
            () -> buffer.read(outputBytes, 0, outputBytes.length + 1),
            "Reading with a length greater than buffer size should throw IllegalArgumentException");
    }
}