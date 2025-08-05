package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link ByteArrayBuilder} class.
 */
public class ByteArrayBuilderTest {

    private static final int DEFAULT_INITIAL_BLOCK_SIZE = 500;

    // ==========================================================
    // Constructor and State Initialization Tests
    // ==========================================================

    @Test
    public void constructor_withDefault_shouldStartEmpty() {
        // Arrange & Act
        ByteArrayBuilder builder = new ByteArrayBuilder();

        // Assert
        assertEquals(0, builder.size());
        assertEquals(0, builder.getCurrentSegmentLength());
        assertNotNull(builder.getCurrentSegment());
        assertEquals(DEFAULT_INITIAL_BLOCK_SIZE, builder.getCurrentSegment().length);
    }

    @Test
    public void constructor_withInitialSize_shouldStartEmptyAndAllocateBuffer() {
        // Arrange & Act
        int initialSize = 1024;
        ByteArrayBuilder builder = new ByteArrayBuilder(initialSize);

        // Assert
        assertEquals(0, builder.size());
        assertEquals(0, builder.getCurrentSegmentLength());
        assertNotNull(builder.getCurrentSegment());
        assertEquals(initialSize, builder.getCurrentSegment().length);
    }

    @Test
    public void constructor_withBufferRecycler_shouldUseItForAllocation() {
        // Arrange
        BufferRecycler recycler = new BufferRecycler();

        // Act
        ByteArrayBuilder builder = new ByteArrayBuilder(recycler);

        // Assert
        assertEquals(0, builder.size());
        assertSame(recycler, builder.bufferRecycler());
        // BufferRecycler allocates a 2000-byte buffer for BYTE_WRITE_CONCAT_BUFFER
        assertEquals(2000, builder.getCurrentSegment().length);
    }

    @Test
    public void fromInitial_shouldUseProvidedBufferAndLength() {
        // Arrange
        byte[] initialBuffer = new byte[100];
        int initialLength = 50;

        // Act
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial(initialBuffer, initialLength);

        // Assert
        assertEquals(initialLength, builder.size());
        assertEquals(initialLength, builder.getCurrentSegmentLength());
        assertSame(initialBuffer, builder.getCurrentSegment());
    }

    // ==========================================================
    // Append and Write Operation Tests
    // ==========================================================

    @Test
    public void append_singleByte_shouldIncreaseSizeByOne() {
        // Arrange
        ByteArrayBuilder builder = new ByteArrayBuilder();

        // Act
        builder.append(0xAF);

        // Assert
        assertEquals(1, builder.size());
        assertArrayEquals(new byte[]{(byte) 0xAF}, builder.toByteArray());
    }

    @Test
    public void appendTwoBytes_shouldAddTwoBytesInBigEndian() {
        // Arrange
        ByteArrayBuilder builder = new ByteArrayBuilder();
        int value = 0xABCD;

        // Act
        builder.appendTwoBytes(value);

        // Assert
        assertEquals(2, builder.size());
        byte[] expected = {(byte) 0xAB, (byte) 0xCD};
        assertArrayEquals(expected, builder.toByteArray());
    }

    @Test
    public void appendThreeBytes_shouldAddThreeBytesInBigEndian() {
        // Arrange
        ByteArrayBuilder builder = new ByteArrayBuilder();
        int value = 0xABCDEF;

        // Act
        builder.appendThreeBytes(value);

        // Assert
        assertEquals(3, builder.size());
        byte[] expected = {(byte) 0xAB, (byte) 0xBC, (byte) 0xEF};
        assertArrayEquals(expected, builder.toByteArray());
    }

    @Test
    public void appendFourBytes_shouldAddFourBytesInBigEndian() {
        // Arrange
        ByteArrayBuilder builder = new ByteArrayBuilder();
        int value = 0xABCDEF01;

        // Act
        builder.appendFourBytes(value);

        // Assert
        assertEquals(4, builder.size());
        byte[] expected = {(byte) 0xAB, (byte) 0xCD, (byte) 0xEF, (byte) 0x01};
        assertArrayEquals(expected, builder.toByteArray());
    }

    @Test
    public void write_byteArray_shouldAppendAllBytes() {
        // Arrange
        ByteArrayBuilder builder = new ByteArrayBuilder();
        byte[] data = {1, 2, 3, 4, 5};

        // Act
        builder.write(data);

        // Assert
        assertEquals(5, builder.size());
        assertArrayEquals(data, builder.toByteArray());
    }

    @Test
    public void write_byteArrayWithOffset_shouldAppendSubarray() {
        // Arrange
        ByteArrayBuilder builder = new ByteArrayBuilder();
        byte[] data = {1, 2, 3, 4, 5};

        // Act
        builder.write(data, 1, 3); // Should write {2, 3, 4}

        // Assert
        assertEquals(3, builder.size());
        assertArrayEquals(new byte[]{2, 3, 4}, builder.toByteArray());
    }

    @Test
    public void write_whenBufferExceeded_shouldAllocateNewSegment() {
        // Arrange
        ByteArrayBuilder builder = new ByteArrayBuilder(4); // Small initial buffer
        builder.write(new byte[]{1, 2, 3, 4});

        // Act
        builder.write(new byte[]{5, 6});

        // Assert
        assertEquals(6, builder.size());
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 6}, builder.toByteArray());
    }

    // ==========================================================
    // Content Retrieval and Buffer Management Tests
    // ==========================================================

    @Test
    public void toByteArray_onEmptyBuilder_shouldReturnEmptyArray() {
        // Arrange
        ByteArrayBuilder builder = new ByteArrayBuilder();

        // Act
        byte[] result = builder.toByteArray();

        // Assert
        assertArrayEquals(new byte[0], result);
    }

    @Test
    public void toByteArray_afterMultipleAppends_shouldReturnCoalescedArray() {
        // Arrange
        ByteArrayBuilder builder = new ByteArrayBuilder(3); // Force segmentation
        builder.append(1);
        builder.append(2);
        builder.append(3);
        builder.append(4); // New segment

        // Act
        byte[] result = builder.toByteArray();

        // Assert
        assertArrayEquals(new byte[]{1, 2, 3, 4}, result);
    }

    @Test
    public void getClearAndRelease_shouldReturnDataAndResetBuilder() {
        // Arrange
        ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.write(new byte[]{1, 2, 3});

        // Act
        byte[] result = builder.getClearAndRelease();

        // Assert
        assertArrayEquals(new byte[]{1, 2, 3}, result);
        assertEquals(0, builder.size());
        assertNull(builder.getCurrentSegment()); // Buffers are released
    }
    
    @Test
    public void getClearAndRelease_whenCalledTwice_shouldReturnEmptyArrayOnSecondCall() {
        // Arrange
        ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.write(new byte[]{1, 2, 3});
        builder.getClearAndRelease(); // First call

        // Act
        byte[] secondResult = builder.getClearAndRelease();

        // Assert
        assertArrayEquals(new byte[0], secondResult);
    }

    @Test
    public void reset_shouldClearContentAndKeepInitialBuffer() {
        // Arrange
        ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.write(new byte[]{1, 2, 3});

        // Act
        builder.reset();

        // Assert
        assertEquals(0, builder.size());
        assertEquals(0, builder.getCurrentSegmentLength());
        assertNotNull(builder.getCurrentSegment()); // Buffer is not released
        assertArrayEquals(new byte[0], builder.toByteArray());
    }

    @Test
    public void release_shouldDeallocateBuffers() {
        // Arrange
        BufferRecycler recycler = new BufferRecycler();
        ByteArrayBuilder builder = new ByteArrayBuilder(recycler);
        builder.write(1);

        // Act
        builder.release();

        // Assert
        assertEquals(0, builder.size());
        assertNull(builder.getCurrentSegment());
    }

    @Test
    public void close_shouldBehaveLikeRelease() {
        // Arrange
        ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.write(1);

        // Act
        builder.close();

        // Assert
        assertEquals(0, builder.size());
        // With no recycler, close() just nulls out the current block
        assertNull(builder.getCurrentSegment());
    }

    @Test
    public void finishCurrentSegment_shouldChainSegments() {
        // Arrange
        ByteArrayBuilder builder = new ByteArrayBuilder(10);
        builder.write(new byte[]{1, 2, 3, 4, 5});
        assertEquals(5, builder.size());

        // Act
        byte[] nextSegment = builder.finishCurrentSegment();
        builder.write(new byte[]{6, 7});

        // Assert
        assertEquals(7, builder.size());
        assertNotNull(nextSegment);
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 6, 7}, builder.toByteArray());
    }

    // ==========================================================
    // Exception and Edge Case Tests
    // ==========================================================

    @Test(expected = NullPointerException.class)
    public void write_withNullArray_shouldThrowNullPointerException() {
        // Arrange
        ByteArrayBuilder builder = new ByteArrayBuilder();
        byte[] nullArray = null;

        // Act
        builder.write(nullArray);
    }

    @Test(expected = NegativeArraySizeException.class)
    public void toByteArray_whenInternallySetToNegativeSize_shouldThrowException() {
        // Arrange: Create a builder in an invalid state with a negative size.
        // This tests internal robustness against inconsistent states.
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial(new byte[0], -100);

        // Act
        builder.toByteArray(); // Should throw
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void write_withInvalidOffset_shouldThrowIndexOutOfBounds() {
        // Arrange
        ByteArrayBuilder builder = new ByteArrayBuilder();
        byte[] data = {1, 2, 3};

        // Act
        builder.write(data, -1, 2); // Negative offset
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void write_withInvalidLength_shouldThrowIndexOutOfBounds() {
        // Arrange
        ByteArrayBuilder builder = new ByteArrayBuilder();
        byte[] data = {1, 2, 3};

        // Act
        builder.write(data, 0, 4); // Length > array size
    }
    
    @Test
    public void write_withNegativeLength_shouldBeANoOp() {
        // Arrange
        ByteArrayBuilder builder = new ByteArrayBuilder();
        byte[] data = {1, 2, 3};
        
        // Act
        builder.write(data, 0, -5);
        
        // Assert
        assertEquals(0, builder.size());
    }
}