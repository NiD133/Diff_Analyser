/*
 * Copyright (C) 2011 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.common.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import com.google.common.hash.Crc32cHashFunction.Crc32cHasher;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link AbstractStreamingHasher}.
 *
 * <p>This test suite uses {@link Crc32cHashFunction.Crc32cHasher} as a concrete implementation
 * to test the behavior of the abstract base class.
 */
public class AbstractStreamingHasherTest {

    private Crc32cHasher hasher;

    @Before
    public void setUp() {
        hasher = new Crc32cHashFunction.Crc32cHasher();
    }

    // --- Test Group 1: Basic Hashing Functionality and Fluent API ---

    @Test
    public void putByte_shouldReturnSameHasherInstance() {
        Hasher result = hasher.putByte((byte) 0);
        assertSame("The 'put' methods should support a fluent API.", hasher, result);
    }

    @Test
    public void putBytesFromByteBuffer_shouldOnlyHashRemainingBytes() {
        // This test verifies that the hasher correctly handles a ByteBuffer
        // whose position is not zero.
        
        // Arrange
        byte[] fullData = "hello world".getBytes(StandardCharsets.UTF_8);
        byte[] firstPart = "hello".getBytes(StandardCharsets.UTF_8);
        byte[] secondPart = " world".getBytes(StandardCharsets.UTF_8);

        ByteBuffer buffer = ByteBuffer.wrap(fullData);
        buffer.get(new byte[firstPart.length]); // Advance buffer position past "hello"

        // Act
        HashCode hashOfSecondPart = new Crc32cHashFunction.Crc32cHasher().putBytes(secondPart).hash();
        HashCode hashFromBuffer = new Crc32cHashFunction.Crc32cHasher().putBytes(buffer).hash();

        // Assert
        assertEquals(hashOfSecondPart, hashFromBuffer);
    }

    // --- Test Group 2: Input Validation ---

    @Test(expected = NullPointerException.class)
    public void putBytes_withNullArray_shouldThrowNullPointerException() {
        hasher.putBytes(null, 0, 1);
    }

    @Test(expected = NullPointerException.class)
    public void putBytes_withNullByteBuffer_shouldThrowNullPointerException() {
        hasher.putBytes((ByteBuffer) null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void putBytes_withNegativeOffset_shouldThrowIndexOutOfBoundsException() {
        hasher.putBytes(new byte[10], -1, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void putBytes_withNegativeLength_shouldThrowIndexOutOfBoundsException() {
        hasher.putBytes(new byte[10], 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void putBytes_withLengthTooLarge_shouldThrowIndexOutOfBoundsException() {
        hasher.putBytes(new byte[10], 0, 11);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void putBytes_withOffsetAndLengthTooLarge_shouldThrowIndexOutOfBoundsException() {
        hasher.putBytes(new byte[10], 5, 6);
    }

    // --- Test Group 3: State Management After Calling hash() ---

    /**
     * When hash() is called, the hasher is considered "finished". For implementations like
     * Crc32cHasher, if any data was processed, an internal "done" flag is set. Any subsequent
     * attempts to add data should result in an IllegalStateException.
     */
    @Test(expected = IllegalStateException.class)
    public void putByte_afterHashCalledOnNonEmptyHasher_shouldThrowIllegalStateException() {
        // Arrange: Add data and finalize the hash.
        hasher.putByte((byte) 1);
        hasher.hash();

        // Act: Attempt to add more data.
        hasher.putByte((byte) 2);
    }

    @Test(expected = IllegalStateException.class)
    public void putBytes_afterHashCalledOnNonEmptyHasher_shouldThrowIllegalStateException() {
        hasher.putBytes(new byte[]{1, 2, 3});
        hasher.hash();
        hasher.putBytes(new byte[]{4, 5, 6});
    }

    @Test(expected = IllegalStateException.class)
    public void putLong_afterHashCalledOnNonEmptyHasher_shouldThrowIllegalStateException() {
        hasher.putLong(1L);
        hasher.hash();
        hasher.putLong(2L);
    }

    /**
     * This group covers a subtle implementation detail. When hash() is called on a hasher that is
     * empty, the final processing step that sets the "done" flag may be skipped. Subsequent `put`
     * operations will then fail with a BufferOverflowException from the underlying ByteBuffer
     * (which has been flipped and has a limit of 0), not an IllegalStateException.
     */
    @Test(expected = BufferOverflowException.class)
    public void putByte_afterHashCalledOnEmptyHasher_shouldThrowBufferOverflowException() {
        // Arrange: Call hash() on a new, empty hasher.
        hasher.hash();

        // Act: Attempt to add data.
        hasher.putByte((byte) 1);
    }

    @Test(expected = BufferOverflowException.class)
    public void putShort_afterHashCalledOnEmptyHasher_shouldThrowBufferOverflowException() {
        hasher.hash();
        hasher.putShort((short) 1);
    }

    @Test(expected = BufferOverflowException.class)
    public void putInt_afterHashCalledOnEmptyHasher_shouldThrowBufferOverflowException() {
        hasher.hash();
        hasher.putInt(1);
    }

    // --- Test Group 4: Correctness of Hashing ---

    @Test
    public void hash_withVariousInputs_producesConsistentResult() {
        // This test verifies that different `put` methods contribute correctly to the final hash
        // by comparing the result with a hash generated from an equivalent single byte array.

        // Arrange: Create a hash from individual primitive puts.
        Crc32cHasher hasher1 = new Crc32cHashFunction.Crc32cHasher();
        hasher1.putByte((byte) 1)
               .putShort((short) 258) // 0x0102 in big-endian
               .putInt(67305985)      // 0x04030201 in big-endian
               .putBoolean(true);

        // Create an equivalent byte array. AbstractStreamingHasher uses LITTLE_ENDIAN byte order.
        ByteBuffer bb = ByteBuffer.allocate(1 + 2 + 4 + 1).order(ByteOrder.LITTLE_ENDIAN);
        bb.put((byte) 1);
        bb.putShort((short) 258);
        bb.putInt(67305985);
        bb.put((byte) 1); // for boolean true
        byte[] equivalentData = bb.array();
        
        Crc32cHasher hasher2 = new Crc32cHashFunction.Crc32cHasher();
        hasher2.putBytes(equivalentData);
        
        // Act
        HashCode hash1 = hasher1.hash();
        HashCode hash2 = hasher2.hash();
        
        // Assert
        assertNotNull(hash1);
        assertEquals("Hashes from equivalent inputs should be identical.", hash1, hash2);
    }
}