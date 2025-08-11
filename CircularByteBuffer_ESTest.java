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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link CircularByteBuffer}.
 */
public class CircularByteBufferTest {

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    // --- Constructor Tests ---

    @Test
    public void constructor_shouldUseDefaultBufferSize_whenNoSizeIsProvided() {
        // Act
        final CircularByteBuffer buffer = new CircularByteBuffer();

        // Assert
        assertEquals(DEFAULT_BUFFER_SIZE, buffer.getSpace());
        assertEquals(0, buffer.getCurrentNumberOfBytes());
    }

    @Test
    public void constructor_shouldUseSpecifiedSize_whenSizeIsProvided() {
        // Arrange
        final int size = 128;

        // Act
        final CircularByteBuffer buffer = new CircularByteBuffer(size);

        // Assert
        assertEquals(size, buffer.getSpace());
        assertEquals(0, buffer.getCurrentNumberOfBytes());
    }

    @Test
    public void constructor_shouldCreateBufferWithZeroSpace_whenSizeIsZero() {
        // Act
        final CircularByteBuffer buffer = new CircularByteBuffer(0);

        // Assert
        assertEquals(0, buffer.getSpace());
        assertFalse(buffer.hasSpace());
    }

    @Test
    public void constructor_shouldThrowNegativeArraySizeException_whenSizeIsNegative() {
        // Assert
        thrown.expect(NegativeArraySizeException.class);

        // Act
        new CircularByteBuffer(-1);
    }

    // --- State and Status Method Tests ---

    @Test
    public void state_shouldBeCorrect_onNewBuffer() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer();

        // Assert
        assertFalse("A new buffer should be empty", buffer.hasBytes());
        assertTrue("A new buffer should have space", buffer.hasSpace());
        assertEquals(0, buffer.getCurrentNumberOfBytes());
        assertEquals(DEFAULT_BUFFER_SIZE, buffer.getSpace());
    }

    @Test
    public void state_shouldUpdateCorrectly_afterAdd() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer();

        // Act
        buffer.add((byte) 1);

        // Assert
        assertTrue("Buffer should have bytes after add", buffer.hasBytes());
        assertEquals(1, buffer.getCurrentNumberOfBytes());
        assertEquals(DEFAULT_BUFFER_SIZE - 1, buffer.getSpace());
    }

    @Test
    public void clear_shouldResetBufferToEmptyState() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer(10);
        buffer.add(new byte[]{1, 2, 3}, 0, 3);
        assertEquals(3, buffer.getCurrentNumberOfBytes());

        // Act
        buffer.clear();

        // Assert
        assertFalse("Buffer should be empty after clear", buffer.hasBytes());
        assertEquals(0, buffer.getCurrentNumberOfBytes());
        assertEquals(10, buffer.getSpace());
    }

    @Test
    public void hasSpace_shouldReturnFalse_whenRequestedSpaceExceedsAvailable() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer(10);

        // Act & Assert
        assertTrue(buffer.hasSpace(10));
        assertFalse(buffer.hasSpace(11));
    }

    @Test
    public void hasSpace_shouldReturnTrueForZeroBytes_evenOnFullBuffer() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer(0);

        // Act & Assert
        assertTrue(buffer.hasSpace(0));
    }

    // --- Add and Read Tests ---

    @Test
    public void addAndRead_shouldPreserveByteValue() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte expectedByte = (byte) 42;
        buffer.add(expectedByte);

        // Act
        final byte actualByte = buffer.read();

        // Assert
        assertEquals(expectedByte, actualByte);
        assertFalse("Buffer should be empty after reading the last byte", buffer.hasBytes());
    }

    @Test
    public void addAndRead_shouldWorkCorrectlyOnBufferOfSizeOne() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer(1);
        buffer.add((byte) 1);
        assertEquals(0, buffer.getSpace());

        // Act
        final byte value = buffer.read();

        // Assert
        assertEquals(1, value);
        assertEquals(1, buffer.getSpace());
    }

    @Test
    public void addAndRead_shouldHandleFullBufferCycle() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer(2);
        final byte[] sourceData = {1, 2};
        final byte[] targetData = new byte[2];

        // Act
        buffer.add(sourceData, 0, 2);
        assertFalse(buffer.hasSpace());
        buffer.read(targetData, 0, 2);

        // Assert
        assertArrayEquals(sourceData, targetData);
        assertEquals(0, buffer.getCurrentNumberOfBytes());
        assertEquals(2, buffer.getSpace());
    }

    @Test
    public void addAndRead_shouldWrapAroundCorrectly() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer(5);
        final byte[] part1 = {1, 2, 3, 4};
        final byte[] part2 = {5, 6, 7};
        final byte[] expected = {3, 4, 5, 6, 7};
        final byte[] actual = new byte[5];

        // Act
        buffer.add(part1, 0, 4); // Buffer: [1, 2, 3, 4, _]
        buffer.read(); // Buffer: [_, 2, 3, 4, _], read 1
        buffer.read(); // Buffer: [_, _, 3, 4, _], read 2
        buffer.add(part2, 0, 3); // Buffer: [7, _, 3, 4, 5, 6] (wrapped)

        // Assert
        assertEquals(5, buffer.getCurrentNumberOfBytes());
        assertFalse(buffer.hasSpace());

        buffer.read(actual, 0, 5);
        assertArrayEquals(expected, actual);
    }

    // --- Exception Tests for add() ---

    @Test
    public void add_shouldThrowIllegalStateException_whenBufferIsFull() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer(1);
        buffer.add((byte) 1);

        // Assert
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("No space available");

        // Act
        buffer.add((byte) 2);
    }

    @Test
    public void add_shouldThrowIllegalStateException_whenNotEnoughSpaceForArray() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer(5);
        final byte[] data = new byte[10];

        // Assert
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("No space available");

        // Act
        buffer.add(data, 0, 6);
    }

    @Test
    public void add_shouldThrowNullPointerException_whenSourceBufferIsNull() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer();

        // Assert
        thrown.expect(NullPointerException.class);

        // Act
        buffer.add(null, 0, 1);
    }

    @Test
    public void add_shouldThrowIllegalArgumentException_whenOffsetIsNegative() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] data = new byte[2];

        // Assert
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Illegal offset: -1");

        // Act
        buffer.add(data, -1, 1);
    }

    @Test
    public void add_shouldThrowIllegalArgumentException_whenLengthIsNegative() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] data = new byte[2];

        // Assert
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Illegal length: -1");

        // Act
        buffer.add(data, 0, -1);
    }

    @Test
    public void add_shouldThrowArrayIndexOutOfBoundsException_whenAccessingSourceBufferOutOfBounds() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] data = new byte[5];

        // Assert
        thrown.expect(ArrayIndexOutOfBoundsException.class);

        // Act
        buffer.add(data, 0, 6);
    }

    // --- Exception Tests for read() ---

    @Test
    public void read_shouldThrowIllegalStateException_whenBufferIsEmptyForSingleByteRead() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer();

        // Assert
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("No bytes available.");

        // Act
        buffer.read();
    }

    @Test
    public void read_shouldThrowIllegalStateException_whenRequestingMoreBytesThanAvailable() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer();
        buffer.add((byte) 1);

        // Assert
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Currently, there are only 1 in the buffer, not 2");

        // Act
        buffer.read(new byte[2], 0, 2);
    }

    @Test
    public void read_shouldThrowNullPointerException_whenTargetBufferIsNull() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer();

        // Assert
        thrown.expect(NullPointerException.class);

        // Act
        buffer.read(null, 0, 0);
    }

    @Test
    public void read_shouldThrowIllegalArgumentException_whenOffsetIsNegative() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] target = new byte[2];

        // Assert
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Illegal offset: -1");

        // Act
        buffer.read(target, -1, 1);
    }

    @Test
    public void read_shouldThrowIllegalArgumentException_whenLengthIsNegative() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] target = new byte[2];

        // Assert
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Illegal length: -1");

        // Act
        buffer.read(target, 0, -1);
    }

    @Test
    public void read_shouldThrowIllegalArgumentException_whenTargetArrayIsTooSmall() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] target = new byte[4];

        // Assert
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The supplied byte array contains only 4 bytes, but offset, and length would require 5");

        // Act
        buffer.read(target, 0, 5);
    }

    // --- Peek Tests ---

    @Test
    public void peek_shouldReturnTrue_whenDataMatchesAndDoesNotConsumeBytes() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] data = {'a', 'b', 'c'};
        buffer.add(data, 0, 3);

        // Act
        final boolean result = buffer.peek(data, 0, 3);

        // Assert
        assertTrue("peek should return true for matching data", result);
        assertEquals("peek should not consume bytes", 3, buffer.getCurrentNumberOfBytes());
    }

    @Test
    public void peek_shouldReturnFalse_whenDataDoesNotMatch() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer();
        buffer.add(new byte[]{'a', 'b', 'c'}, 0, 3);
        final byte[] nonMatchingData = {'x', 'y', 'z'};

        // Act
        final boolean result = buffer.peek(nonMatchingData, 0, 3);

        // Assert
        assertFalse("peek should return false for non-matching data", result);
    }

    @Test
    public void peek_shouldReturnFalse_whenBufferHasFewerBytesThanPeekLength() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer();
        buffer.add((byte) 'a');

        // Act
        final boolean result = buffer.peek(new byte[]{'a', 'b'}, 0, 2);

        // Assert
        assertFalse("peek should return false if buffer has insufficient bytes", result);
    }

    @Test
    public void peek_shouldReturnTrue_whenLengthIsZero() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer();
        buffer.add((byte) 'a');

        // Act & Assert
        assertTrue("peek with zero length should always be true", buffer.peek(new byte[1], 0, 0));
    }

    @Test
    public void peek_shouldThrowIllegalArgumentException_whenLengthIsNegative() {
        // Arrange
        final CircularByteBuffer buffer = new CircularByteBuffer();

        // Assert
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Illegal length: -1");

        // Act
        buffer.peek(new byte[1], 0, -1);
    }
}