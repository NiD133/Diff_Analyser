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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CircularByteBuffer}, organized by method under test.
 */
class CircularByteBufferTest {

    @Nested
    class Add {

        @Test
        void add_shouldStoreByteAndWrapAroundInSmallBuffer() {
            // Arrange
            final CircularByteBuffer buffer = new CircularByteBuffer(1);

            // Act & Assert for first byte
            buffer.add((byte) 1);
            assertEquals(1, buffer.read());

            // Act & Assert for second byte (tests wrap-around)
            buffer.add((byte) 2);
            assertEquals(2, buffer.read());
        }

        @Test
        void add_shouldStoreByteArray() {
            // Arrange
            final CircularByteBuffer buffer = new CircularByteBuffer();
            final byte[] data = { 3, 6, 9 };

            // Act
            buffer.add(data, 0, data.length);

            // Assert
            assertEquals(data.length, buffer.getCurrentNumberOfBytes());
            assertTrue(buffer.hasBytes());
        }

        @Test
        void add_shouldThrowNullPointerException_forNullBuffer() {
            // Arrange
            final CircularByteBuffer buffer = new CircularByteBuffer();

            // Act & Assert
            assertThrows(NullPointerException.class, () -> buffer.add(null, 0, 3));
        }

        @Test
        void add_shouldThrowIllegalArgumentException_forNegativeOffset() {
            // Arrange
            final CircularByteBuffer buffer = new CircularByteBuffer();
            final byte[] data = { 1, 2, 3 };

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> buffer.add(data, -1, 3));
        }

        @Test
        void add_shouldThrowIllegalArgumentException_forNegativeLength() {
            // Arrange
            final CircularByteBuffer buffer = new CircularByteBuffer();
            final byte[] data = { 1, 2, 3 };

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> buffer.add(data, 0, -1));
        }
    }

    @Nested
    class Read {

        @Test
        void read_shouldFillTargetArrayWithBufferContent() {
            // Arrange
            final CircularByteBuffer buffer = new CircularByteBuffer();
            final String inputString = "0123456789";
            final byte[] bytesIn = inputString.getBytes(StandardCharsets.UTF_8);
            buffer.add(bytesIn, 0, bytesIn.length);

            // Act
            final byte[] bytesOut = new byte[bytesIn.length];
            buffer.read(bytesOut, 0, bytesOut.length);

            // Assert
            assertEquals(inputString, new String(bytesOut, StandardCharsets.UTF_8));
            assertEquals(0, buffer.getCurrentNumberOfBytes());
            assertFalse(buffer.hasBytes());
        }

        @Test
        void read_shouldThrowIllegalArgumentException_forNegativeTargetOffset() {
            // Arrange
            final CircularByteBuffer buffer = new CircularByteBuffer();
            final byte[] target = new byte[10];

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> buffer.read(target, -1, 10));
        }

        @Test
        void read_shouldThrowIllegalArgumentException_whenLengthExceedsTargetCapacity() {
            // Arrange
            final CircularByteBuffer buffer = new CircularByteBuffer();
            final byte[] target = new byte[10];

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> buffer.read(target, 0, target.length + 1));
        }
    }

    @Nested
    class Peek {

        @Test
        void peek_shouldReturnTrue_whenBytesMatch() {
            // Arrange
            final CircularByteBuffer buffer = new CircularByteBuffer();
            final byte[] dataToAdd = { 1, 2, 3, 4 };
            buffer.add(dataToAdd, 0, dataToAdd.length);
            final byte[] dataToPeek = { 1, 2, 3 };

            // Act
            final boolean result = buffer.peek(dataToPeek, 0, dataToPeek.length);

            // Assert
            assertTrue(result);
            assertEquals(4, buffer.getCurrentNumberOfBytes(), "Peek should not consume bytes");
        }

        @Test
        void peek_shouldReturnFalse_whenBufferIsEmpty() {
            // Arrange
            final CircularByteBuffer buffer = new CircularByteBuffer();
            final byte[] dataToPeek = { 5, 10, 15 };

            // Act
            final boolean result = buffer.peek(dataToPeek, 0, dataToPeek.length);

            // Assert
            assertFalse(result);
        }

        @Test
        void peek_shouldReturnFalse_whenRequestedLengthExceedsAvailableBytes() {
            // Arrange
            final CircularByteBuffer buffer = new CircularByteBuffer();
            buffer.add(new byte[] { 1, 3, 5 }, 0, 3);
            final byte[] dataToPeek = new byte[6];

            // Act
            final boolean result = buffer.peek(dataToPeek, 0, dataToPeek.length);

            // Assert
            assertFalse(result);
        }

        @Test
        void peek_shouldThrowIllegalArgumentException_forNegativeOffset() {
            // Arrange
            final CircularByteBuffer buffer = new CircularByteBuffer();
            final byte[] dataToPeek = { 2, 4, 6 };

            // Act & Assert
            final IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> buffer.peek(dataToPeek, -1, 3));
            assertEquals("Illegal offset: -1", e.getMessage());
        }

        @Test
        void peek_shouldThrowIllegalArgumentException_forNegativeLength() {
            // Arrange
            final CircularByteBuffer buffer = new CircularByteBuffer();
            final byte[] dataToPeek = { 1, 4, 3 };

            // Act & Assert
            final IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> buffer.peek(dataToPeek, 0, -1));
            assertEquals("Illegal length: -1", e.getMessage());
        }
    }

    @Nested
    class StateAndLifecycle {

        @Test
        void clear_shouldResetBufferState() {
            // Arrange
            final byte[] data = { 1, 2, 3 };
            final CircularByteBuffer buffer = new CircularByteBuffer(10);
            buffer.add(data, 0, data.length);
            // Sanity check before clearing
            assertEquals(3, buffer.getCurrentNumberOfBytes());
            assertTrue(buffer.hasBytes());

            // Act
            buffer.clear();

            // Assert
            assertEquals(0, buffer.getCurrentNumberOfBytes());
            assertEquals(10, buffer.getSpace());
            assertFalse(buffer.hasBytes());
            assertTrue(buffer.hasSpace());
        }

        @Test
        void hasSpace_shouldCorrectlyReportAvailability() {
            // Arrange
            final CircularByteBuffer buffer = new CircularByteBuffer(1);

            // Assert initial state
            assertTrue(buffer.hasSpace());
            assertTrue(buffer.hasSpace(1));

            // Act: Fill the buffer
            buffer.add((byte) 1);

            // Assert full state
            assertFalse(buffer.hasSpace());
            assertFalse(buffer.hasSpace(1));

            // Act: Empty the buffer
            buffer.read();

            // Assert empty state again
            assertTrue(buffer.hasSpace());
            assertTrue(buffer.hasSpace(1));
        }
    }
}