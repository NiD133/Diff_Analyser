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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CircularByteBuffer}.
 *
 * The tests favor clear Arrange-Act-Assert structure and explicit names to
 * make the buffer's behavior easy to understand and maintain.
 */
class CircularByteBufferTest {

    private static final byte[] BYTES_1_2_3 = { 1, 2, 3 };
    private static final String DIGITS = "0123456789";

    // ---------------------------------------------------------------------
    // add(byte) and basic single-capacity behavior
    // ---------------------------------------------------------------------

    @Test
    void addByte_singleCapacity_addThenReadEach() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer(1);

        // Act + Assert
        buffer.add((byte) 1);
        assertEquals(1, buffer.read());

        buffer.add((byte) 2);
        assertEquals(2, buffer.read());
    }

    // ---------------------------------------------------------------------
    // add(byte[], int, int) argument validation
    // ---------------------------------------------------------------------

    @Test
    void add_withNegativeOffset_throwsIllegalArgumentException() {
        final CircularByteBuffer buffer = new CircularByteBuffer();
        assertThrows(IllegalArgumentException.class, () -> buffer.add(new byte[] { 1, 2, 3 }, -1, 3));
    }

    @Test
    void add_withNegativeLength_throwsIllegalArgumentException() {
        final CircularByteBuffer buffer = new CircularByteBuffer();
        assertThrows(IllegalArgumentException.class, () -> buffer.add(BYTES_1_2_3, 0, -1));
    }

    @Test
    void add_withNullBuffer_throwsNullPointerException() {
        final CircularByteBuffer buffer = new CircularByteBuffer();
        assertThrows(NullPointerException.class, () -> buffer.add(null, 0, 3));
    }

    @Test
    void add_arrayWithinCapacity_increasesByteCount() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final int length = 3;

        // Act
        buffer.add(new byte[] { 3, 6, 9 }, 0, length);

        // Assert
        assertEquals(length, buffer.getCurrentNumberOfBytes());
    }

    // ---------------------------------------------------------------------
    // clear()
    // ---------------------------------------------------------------------

    @Test
    void clear_resetsStateAndCapacity() {
        final byte[] data = BYTES_1_2_3;
        final CircularByteBuffer buffer = new CircularByteBuffer(10);

        assertAll(
            () -> assertEquals(0, buffer.getCurrentNumberOfBytes()),
            () -> assertFalse(buffer.hasBytes())
        );

        buffer.add(data, 0, data.length);

        assertAll(
            () -> assertEquals(3, buffer.getCurrentNumberOfBytes()),
            () -> assertEquals(7, buffer.getSpace()),
            () -> assertTrue(buffer.hasBytes()),
            () -> assertTrue(buffer.hasSpace())
        );

        buffer.clear();

        assertAll(
            () -> assertEquals(0, buffer.getCurrentNumberOfBytes()),
            () -> assertEquals(10, buffer.getSpace()),
            () -> assertFalse(buffer.hasBytes()),
            () -> assertTrue(buffer.hasSpace())
        );
    }

    // ---------------------------------------------------------------------
    // hasSpace() and hasSpace(int)
    // ---------------------------------------------------------------------

    @Test
    void hasSpace_singleCapacity_togglesWithAddAndRead() {
        final CircularByteBuffer buffer = new CircularByteBuffer(1);

        assertTrue(buffer.hasSpace());
        buffer.add((byte) 1);
        assertFalse(buffer.hasSpace());

        assertEquals(1, buffer.read());
        assertTrue(buffer.hasSpace());

        buffer.add((byte) 2);
        assertFalse(buffer.hasSpace());

        assertEquals(2, buffer.read());
        assertTrue(buffer.hasSpace());
    }

    @Test
    void hasSpace_count_singleCapacity_togglesWithAddAndRead() {
        final CircularByteBuffer buffer = new CircularByteBuffer(1);

        assertTrue(buffer.hasSpace(1));
        buffer.add((byte) 1);
        assertFalse(buffer.hasSpace(1));

        assertEquals(1, buffer.read());
        assertTrue(buffer.hasSpace(1));

        buffer.add((byte) 2);
        assertFalse(buffer.hasSpace(1));

        assertEquals(2, buffer.read());
        assertTrue(buffer.hasSpace(1));
    }

    // ---------------------------------------------------------------------
    // peek(byte[], int, int)
    // ---------------------------------------------------------------------

    @Test
    void peek_requestMoreBytesThanPresent_returnsFalse() {
        // Empty buffer cannot satisfy a request for 6 bytes.
        assertFalse(new CircularByteBuffer().peek(new byte[] { 1, 3, 5, 7, 9 }, 0, 6));
    }

    @Test
    void peek_withNegativeOffset_throwsIllegalArgumentExceptionWithMessage() {
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> buffer.peek(new byte[] { 2, 4, 6, 8, 10 }, -1, 5));
        assertEquals("Illegal offset: -1", ex.getMessage());
    }

    @Test
    void peek_withNegativeLength_throwsIllegalArgumentExceptionWithMessage() {
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> buffer.peek(new byte[] { 1, 4, 3 }, 0, -1));
        assertEquals("Illegal length: -1", ex.getMessage());
    }

    @Test
    void peek_onEmptyBuffer_returnsFalse() {
        assertFalse(new CircularByteBuffer().peek(new byte[] { 5, 10, 15, 20, 25 }, 0, 5));
    }

    // ---------------------------------------------------------------------
    // read(byte[], int, int)
    // ---------------------------------------------------------------------

    @Test
    void read_intoArray_readsBytesInOrderAndEmptiesBuffer() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] bytesIn = DIGITS.getBytes(StandardCharsets.UTF_8);
        buffer.add(bytesIn, 0, bytesIn.length);

        // Act
        final byte[] bytesOut = new byte[bytesIn.length];
        buffer.read(bytesOut, 0, bytesOut.length);

        // Assert
        assertAll(
            () -> assertEquals(DIGITS, new String(bytesOut, StandardCharsets.UTF_8)),
            () -> assertEquals(0, buffer.getCurrentNumberOfBytes()),
            () -> assertFalse(buffer.hasBytes()),
            () -> assertTrue(buffer.hasSpace())
        );
    }

    @Test
    void read_intoArray_withInvalidArgs_throwsIllegalArgumentException() {
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] target = new byte[10];

        // Negative targetOffset
        assertThrows(IllegalArgumentException.class, () -> buffer.read(target, -1, 10));

        // Negative length
        assertThrows(IllegalArgumentException.class, () -> buffer.read(target, 0, -1));

        // targetOffset + length exceeds target.length
        assertThrows(IllegalArgumentException.class, () -> buffer.read(target, 0, target.length + 1));
    }
}