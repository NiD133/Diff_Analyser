/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.io.input.buffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link CircularByteBuffer}.
 */
class CircularByteBufferTest {

    // Test constants for better readability
    private static final int SINGLE_BYTE_BUFFER_SIZE = 1;
    private static final int MEDIUM_BUFFER_SIZE = 10;
    private static final byte FIRST_TEST_BYTE = 1;
    private static final byte SECOND_TEST_BYTE = 2;
    private static final String TEST_STRING = "0123456789";

    // ========== Single Byte Operations Tests ==========

    @Test
    void testAddAndReadSingleByte_WithMinimumBufferSize() {
        // Given: A circular buffer with the smallest possible size (1 byte)
        final CircularByteBuffer buffer = new CircularByteBuffer(SINGLE_BYTE_BUFFER_SIZE);
        
        // When: Adding and reading the first byte
        buffer.add(FIRST_TEST_BYTE);
        
        // Then: The byte should be readable
        assertEquals(FIRST_TEST_BYTE, buffer.read());
        
        // When: Adding and reading a second byte (buffer reuse)
        buffer.add(SECOND_TEST_BYTE);
        
        // Then: The second byte should be readable
        assertEquals(SECOND_TEST_BYTE, buffer.read());
    }

    // ========== Buffer Space Management Tests ==========

    @Test
    void testHasSpace_WithSingleByteBuffer() {
        // Given: A buffer that can hold exactly one byte
        final CircularByteBuffer buffer = new CircularByteBuffer(SINGLE_BYTE_BUFFER_SIZE);
        
        // Then: Initially should have space
        assertTrue(buffer.hasSpace());
        
        // When: Adding a byte to fill the buffer
        buffer.add(FIRST_TEST_BYTE);
        
        // Then: Should have no space left
        assertFalse(buffer.hasSpace());
        
        // When: Reading the byte to free space
        assertEquals(FIRST_TEST_BYTE, buffer.read());
        
        // Then: Should have space again
        assertTrue(buffer.hasSpace());
        
        // When: Adding another byte
        buffer.add(SECOND_TEST_BYTE);
        
        // Then: Should have no space left again
        assertFalse(buffer.hasSpace());
        
        // When: Reading the second byte
        assertEquals(SECOND_TEST_BYTE, buffer.read());
        
        // Then: Should have space again
        assertTrue(buffer.hasSpace());
    }

    @Test
    void testHasSpaceWithSpecificCount_WithSingleByteBuffer() {
        // Given: A buffer that can hold exactly one byte
        final CircularByteBuffer buffer = new CircularByteBuffer(SINGLE_BYTE_BUFFER_SIZE);
        
        // Then: Initially should have space for 1 byte
        assertTrue(buffer.hasSpace(1));
        
        // When: Filling the buffer
        buffer.add(FIRST_TEST_BYTE);
        
        // Then: Should not have space for 1 byte
        assertFalse(buffer.hasSpace(1));
        
        // When: Freeing space by reading
        assertEquals(FIRST_TEST_BYTE, buffer.read());
        
        // Then: Should have space for 1 byte again
        assertTrue(buffer.hasSpace(1));
        
        // When: Filling the buffer again
        buffer.add(SECOND_TEST_BYTE);
        
        // Then: Should not have space for 1 byte
        assertFalse(buffer.hasSpace(1));
        
        // When: Freeing space again
        assertEquals(SECOND_TEST_BYTE, buffer.read());
        
        // Then: Should have space for 1 byte
        assertTrue(buffer.hasSpace(1));
    }

    @Test
    void testClearBuffer_ResetsAllStateCorrectly() {
        // Given: A buffer with some capacity
        final byte[] testData = { 1, 2, 3 };
        final CircularByteBuffer buffer = new CircularByteBuffer(MEDIUM_BUFFER_SIZE);
        
        // Then: Initially should be empty
        assertEquals(0, buffer.getCurrentNumberOfBytes());
        assertFalse(buffer.hasBytes());

        // When: Adding some data
        buffer.add(testData, 0, testData.length);
        
        // Then: Buffer should contain the data
        assertEquals(3, buffer.getCurrentNumberOfBytes());
        assertEquals(7, buffer.getSpace()); // 10 - 3 = 7
        assertTrue(buffer.hasBytes());
        assertTrue(buffer.hasSpace());

        // When: Clearing the buffer
        buffer.clear();
        
        // Then: Buffer should be completely empty and have full capacity
        assertEquals(0, buffer.getCurrentNumberOfBytes());
        assertEquals(MEDIUM_BUFFER_SIZE, buffer.getSpace());
        assertFalse(buffer.hasBytes());
        assertTrue(buffer.hasSpace());
    }

    // ========== Byte Array Operations Tests ==========

    @Test
    void testAddByteArray_WithValidData() {
        // Given: A circular buffer and test data
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] testData = { 3, 6, 9 };
        final int dataLength = 3;
        
        // When: Adding the byte array to the buffer
        buffer.add(testData, 0, dataLength);
        
        // Then: Buffer should contain exactly the added bytes
        assertEquals(dataLength, buffer.getCurrentNumberOfBytes());
    }

    @Test
    void testReadByteArray_PreservesDataIntegrity() {
        // Given: A buffer with string data
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] originalBytes = TEST_STRING.getBytes(StandardCharsets.UTF_8);
        
        // When: Adding the data to buffer
        buffer.add(originalBytes, 0, originalBytes.length);
        
        // And: Reading it back into a new array
        final byte[] readBytes = new byte[originalBytes.length];
        buffer.read(readBytes, 0, originalBytes.length);
        
        // Then: The read data should match the original
        final String reconstructedString = new String(readBytes, StandardCharsets.UTF_8);
        assertEquals(TEST_STRING, reconstructedString);
    }

    // ========== Peek Operations Tests ==========

    @Test
    void testPeek_WithValidArguments_OnEmptyBuffer() {
        // Given: An empty buffer and some test data
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] testData = { 5, 10, 15, 20, 25 };
        
        // When: Peeking for data that's not in the buffer
        final boolean foundData = buffer.peek(testData, 0, testData.length);
        
        // Then: Should return false (no matching data found)
        assertFalse(foundData);
    }

    @Test
    void testPeek_WithExcessiveLength_ReturnsFalse() {
        // Given: An empty buffer
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] testData = { 1, 3, 5, 7, 9 };
        
        // When: Trying to peek more data than available
        final boolean foundData = buffer.peek(testData, 0, 6); // asking for 6 bytes from 5-byte array
        
        // Then: Should return false
        assertFalse(foundData);
    }

    // ========== Error Handling Tests ==========

    @Test
    void testAddByteArray_WithNullBuffer_ThrowsException() {
        // Given: A circular buffer
        final CircularByteBuffer buffer = new CircularByteBuffer();
        
        // When & Then: Adding null buffer should throw NullPointerException
        assertThrows(NullPointerException.class, () -> buffer.add(null, 0, 3));
    }

    @Test
    void testAddByteArray_WithNegativeOffset_ThrowsException() {
        // Given: A circular buffer and test data
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] testData = { 1, 2, 3 };
        
        // When & Then: Negative offset should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> buffer.add(testData, -1, 3));
    }

    @Test
    void testAddByteArray_WithNegativeLength_ThrowsException() {
        // Given: A circular buffer and test data
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] testData = { 1, 2, 3 };
        
        // When & Then: Negative length should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> buffer.add(testData, 0, -1));
    }

    @Test
    void testPeek_WithNegativeOffset_ThrowsExceptionWithMessage() {
        // Given: A circular buffer and test data
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] testData = { 2, 4, 6, 8, 10 };
        
        // When & Then: Negative offset should throw IllegalArgumentException with specific message
        final IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> buffer.peek(testData, -1, 5)
        );
        assertEquals("Illegal offset: -1", exception.getMessage());
    }

    @Test
    void testPeek_WithNegativeLength_ThrowsExceptionWithMessage() {
        // Given: A circular buffer and test data
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] testData = { 1, 4, 3 };
        
        // When & Then: Negative length should throw IllegalArgumentException with specific message
        final IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> buffer.peek(testData, 0, -1)
        );
        assertEquals("Illegal length: -1", exception.getMessage());
    }

    @Test
    void testReadByteArray_WithInvalidArguments_ThrowsExceptions() {
        // Given: A circular buffer and output array
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] outputArray = new byte[10];
        
        // When & Then: Negative target offset should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> buffer.read(outputArray, -1, 10));
        
        // When & Then: Length exceeding array bounds should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> buffer.read(outputArray, 0, outputArray.length + 1));
    }
}