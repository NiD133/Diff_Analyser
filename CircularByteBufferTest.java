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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link CircularByteBuffer}.
 */
class CircularByteBufferTest {

    private CircularByteBuffer circularByteBuffer;

    @BeforeEach
    void setup() {
        circularByteBuffer = new CircularByteBuffer();
    }

    @Test
    void testAddByteSmallestBuffer() {
        // Given a CircularByteBuffer with size 1
        circularByteBuffer = new CircularByteBuffer(1);

        // When a byte is added and then read
        circularByteBuffer.add((byte) 1);
        assertEquals(1, circularByteBuffer.read(), "Should read the added byte");

        // And when another byte is added and then read
        circularByteBuffer.add((byte) 2);
        assertEquals(2, circularByteBuffer.read(), "Should read the added byte");
    }

    @Test
    void testAddByte_invalidOffset_throwsIllegalArgumentException() {
        // Given a byte array and an invalid offset
        final byte[] data = { 1, 2, 3 };
        final int invalidOffset = -1;
        final int length = 3;

        // When adding the byte array with the invalid offset, then expect an IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> circularByteBuffer.add(data, invalidOffset, length),
                "Adding with a negative offset should throw IllegalArgumentException");
    }

    @Test
    void testAddByte_negativeLength_throwsIllegalArgumentException() {
        // Given a byte array and a negative length
        final byte[] data = { 1, 2, 3 };
        final int negativeLength = -1;

        // When adding the byte array with the negative length, then expect an IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> circularByteBuffer.add(data, 0, negativeLength),
                "Adding with a negative length should throw IllegalArgumentException");
    }

    @Test
    void testAddByte_nullBuffer_throwsNullPointerException() {
        // When adding a null byte array, then expect a NullPointerException
        assertThrows(NullPointerException.class, () -> circularByteBuffer.add(null, 0, 3),
                "Adding a null buffer should throw NullPointerException");
    }

    @Test
    void testAddByte_validData_updatesCurrentNumberOfBytes() {
        // Given a byte array and a valid length
        final byte[] data = { 3, 6, 9 };
        final int length = 3;

        // When adding the byte array
        circularByteBuffer.add(data, 0, length);

        // Then the current number of bytes should be updated
        assertEquals(length, circularByteBuffer.getCurrentNumberOfBytes(), "Current number of bytes should be updated");
    }

    @Test
    void testClearResetsBufferState() {
        // Given a CircularByteBuffer with some data
        final byte[] data = { 1, 2, 3 };
        circularByteBuffer = new CircularByteBuffer(10);
        circularByteBuffer.add(data, 0, data.length);

        // Initial assertions
        assertEquals(3, circularByteBuffer.getCurrentNumberOfBytes());
        assertEquals(7, circularByteBuffer.getSpace());
        assertTrue(circularByteBuffer.hasBytes());
        assertTrue(circularByteBuffer.hasSpace());

        // When the buffer is cleared
        circularByteBuffer.clear();

        // Then the buffer should be empty and have full space
        assertEquals(0, circularByteBuffer.getCurrentNumberOfBytes(), "Buffer should be empty");
        assertEquals(10, circularByteBuffer.getSpace(), "Buffer should have full space");
        assertFalse(circularByteBuffer.hasBytes(), "Buffer should not have bytes");
        assertTrue(circularByteBuffer.hasSpace(), "Buffer should have space");
    }

    @Test
    void testHasSpace_singleByte() {
        // Given a CircularByteBuffer with size 1
        circularByteBuffer = new CircularByteBuffer(1);

        // Initially, there should be space
        assertTrue(circularByteBuffer.hasSpace(), "Initially, there should be space");

        // When a byte is added
        circularByteBuffer.add((byte) 1);

        // Then there should be no space
        assertFalse(circularByteBuffer.hasSpace(), "After adding a byte, there should be no space");

        // When the byte is read
        assertEquals(1, circularByteBuffer.read(), "Should read the added byte");

        // Then there should be space again
        assertTrue(circularByteBuffer.hasSpace(), "After reading a byte, there should be space again");

        // Repeat with another byte
        circularByteBuffer.add((byte) 2);
        assertFalse(circularByteBuffer.hasSpace(), "After adding a byte, there should be no space");
        assertEquals(2, circularByteBuffer.read(), "Should read the added byte");
        assertTrue(circularByteBuffer.hasSpace(), "After reading a byte, there should be space again");
    }

    @Test
    void testHasSpace_withCount() {
        // Given a CircularByteBuffer with size 1
        circularByteBuffer = new CircularByteBuffer(1);

        // Initially, there should be space for 1 byte
        assertTrue(circularByteBuffer.hasSpace(1), "Initially, there should be space for 1 byte");

        // When a byte is added
        circularByteBuffer.add((byte) 1);

        // Then there should be no space for 1 byte
        assertFalse(circularByteBuffer.hasSpace(1), "After adding a byte, there should be no space for 1 byte");

        // When the byte is read
        assertEquals(1, circularByteBuffer.read(), "Should read the added byte");

        // Then there should be space again for 1 byte
        assertTrue(circularByteBuffer.hasSpace(1), "After reading a byte, there should be space again for 1 byte");

        // Repeat with another byte
        circularByteBuffer.add((byte) 2);
        assertFalse(circularByteBuffer.hasSpace(1), "After adding a byte, there should be no space for 1 byte");
        assertEquals(2, circularByteBuffer.read(), "Should read the added byte");
        assertTrue(circularByteBuffer.hasSpace(1), "After reading a byte, there should be space again for 1 byte");
    }

    @Test
    void testPeek_withExcessiveLength_returnsFalse() {
        // Given a CircularByteBuffer

        // When peeking with a length greater than the buffer size, then expect false
        assertFalse(circularByteBuffer.peek(new byte[] { 1, 3, 5, 7, 9 }, 0, 6),
                "Peeking with excessive length should return false");
    }

    @Test
    void testPeek_withInvalidOffset_throwsIllegalArgumentException() {
        // Given a CircularByteBuffer and an invalid offset
        final byte[] data = { 2, 4, 6, 8, 10 };
        final int invalidOffset = -1;
        final int length = 5;

        // When peeking with the invalid offset, then expect an IllegalArgumentException
        final IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> circularByteBuffer.peek(data, invalidOffset, length),
                "Peeking with a negative offset should throw IllegalArgumentException");
        assertEquals("Illegal offset: -1", e.getMessage(), "Exception message should match");
    }

    @Test
    void testPeek_withNegativeLength_throwsIllegalArgumentException() {
        // Given a CircularByteBuffer and a negative length
        final byte[] data = { 1, 4, 3 };
        final int negativeLength = -1;

        // When peeking with the negative length, then expect an IllegalArgumentException
        final IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> circularByteBuffer.peek(data, 0, negativeLength),
                "Peeking with a negative length should throw IllegalArgumentException");
        assertEquals("Illegal length: -1", e.getMessage(), "Exception message should match");
    }

    @Test
    void testPeek_withValidArguments_returnsFalse() {
        // Given a CircularByteBuffer and valid arguments

        // When peeking with valid arguments on an empty buffer, then expect false
        assertFalse(circularByteBuffer.peek(new byte[] { 5, 10, 15, 20, 25 }, 0, 5),
                "Peeking with valid arguments on an empty buffer should return false");
    }

    @Test
    void testReadByteArray_readsBytesCorrectly() {
        // Given a CircularByteBuffer with some data
        final String inputString = "0123456789";
        final byte[] bytesIn = inputString.getBytes(StandardCharsets.UTF_8);
        circularByteBuffer.add(bytesIn, 0, bytesIn.length);

        // When reading the bytes into a byte array
        final byte[] bytesOut = new byte[bytesIn.length];
        circularByteBuffer.read(bytesOut, 0, bytesOut.length);

        // Then the byte array should contain the same data
        final String outputString = new String(bytesOut, StandardCharsets.UTF_8);
        assertEquals(inputString, outputString, "Read bytes should match the input string");
    }

    @Test
    void testReadByteArray_invalidArguments_throwsIllegalArgumentException() {
        // Given a CircularByteBuffer and a byte array
        final byte[] bytesOut = new byte[10];

        // When reading with a negative target offset, then expect an IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> circularByteBuffer.read(bytesOut, -1, 10),
                "Reading with a negative target offset should throw IllegalArgumentException");

        // When reading with a target offset >= targetBuffer.length, then expect an IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> circularByteBuffer.read(bytesOut, 0, bytesOut.length + 1),
                "Reading with a target offset >= targetBuffer.length should throw IllegalArgumentException");
    }
}