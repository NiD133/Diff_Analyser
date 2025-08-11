package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.core.util.JsonRecyclerPools;

/**
 * Test suite for ByteArrayBuilder functionality.
 * Tests cover construction, writing data, buffer management, and edge cases.
 */
public class ByteArrayBuilderTest {

    // Test Constants
    private static final int SMALL_BUFFER_SIZE = 4;
    private static final int LARGE_BUFFER_SIZE = 131072;
    private static final byte TEST_BYTE = 74;
    private static final int TEST_INT = 8000;

    // ========== Construction Tests ==========

    @Test
    public void testDefaultConstructor() {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        
        assertEquals("New builder should be empty", 0, builder.size());
        assertEquals("New builder should have empty current segment", 0, builder.getCurrentSegmentLength());
        assertNull("Default builder should not have buffer recycler", builder.bufferRecycler());
    }

    @Test
    public void testConstructorWithBufferRecycler() {
        BufferRecycler recycler = JsonRecyclerPools.NonRecyclingPool.GLOBAL.acquirePooled();
        ByteArrayBuilder builder = new ByteArrayBuilder(recycler);
        
        assertEquals("New builder should be empty", 0, builder.size());
        assertNotNull("Builder should have buffer recycler", builder.bufferRecycler());
    }

    @Test
    public void testConstructorWithCustomSize() {
        ByteArrayBuilder builder = new ByteArrayBuilder(LARGE_BUFFER_SIZE);
        
        assertEquals("New builder should be empty", 0, builder.size());
    }

    @Test
    public void testFromInitialWithValidData() {
        byte[] initialData = new byte[]{1, 2, 3};
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial(initialData, 2);
        
        assertEquals("Builder should have initial length", 2, builder.size());
    }

    @Test
    public void testFromInitialWithEmptyArray() {
        byte[] emptyArray = new byte[0];
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial(emptyArray, 0);
        
        assertEquals("Builder should be empty", 0, builder.size());
    }

    // ========== Writing Single Bytes Tests ==========

    @Test
    public void testAppendSingleByte() {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        
        builder.append(TEST_BYTE);
        
        assertEquals("Should contain one byte", 1, builder.size());
        assertEquals("Current segment should have one byte", 1, builder.getCurrentSegmentLength());
    }

    @Test
    public void testWriteSingleByte() {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        
        builder.write(TEST_INT);
        
        assertEquals("Should contain one byte", 1, builder.size());
    }

    // ========== Writing Multiple Bytes Tests ==========

    @Test
    public void testAppendTwoBytes() {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        
        builder.appendTwoBytes(-2767);
        
        assertEquals("Should contain two bytes", 2, builder.size());
    }

    @Test
    public void testAppendThreeBytes() {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        
        builder.appendThreeBytes(2341);
        
        assertEquals("Should contain three bytes", 3, builder.size());
    }

    @Test
    public void testAppendFourBytes() {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        
        builder.appendFourBytes(-35);
        
        assertEquals("Should contain four bytes", 4, builder.size());
    }

    // ========== Writing Byte Arrays Tests ==========

    @Test
    public void testWriteByteArray() {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        byte[] data = new byte[]{1, 2, 3, 4, 5};
        
        builder.write(data);
        
        assertEquals("Should contain all bytes from array", data.length, builder.size());
    }

    @Test
    public void testWriteByteArrayWithOffsetAndLength() {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        byte[] data = new byte[]{1, 2, 3, 4, 5};
        int offset = 1;
        int length = 3;
        
        builder.write(data, offset, length);
        
        assertEquals("Should contain specified number of bytes", length, builder.size());
    }

    // ========== Buffer Management Tests ==========

    @Test
    public void testFinishCurrentSegment() {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        
        byte[] segment = builder.finishCurrentSegment();
        
        assertNotNull("Should return current segment", segment);
        assertEquals("Should have moved to new segment", 0, builder.getCurrentSegmentLength());
    }

    @Test
    public void testResetAndGetFirstSegment() {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.append(TEST_BYTE); // Add some data
        
        byte[] segment = builder.resetAndGetFirstSegment();
        
        assertNotNull("Should return first segment", segment);
        assertEquals("Should be reset to empty", 0, builder.size());
    }

    @Test
    public void testGetCurrentSegment() {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        
        byte[] segment = builder.getCurrentSegment();
        
        assertNotNull("Should return current segment", segment);
    }

    @Test
    public void testSetCurrentSegmentLength() {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        int newLength = 5;
        
        builder.setCurrentSegmentLength(newLength);
        
        assertEquals("Should update current segment length", newLength, builder.getCurrentSegmentLength());
        assertEquals("Should update total size", newLength, builder.size());
    }

    // ========== Output Generation Tests ==========

    @Test
    public void testToByteArrayEmpty() {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        
        byte[] result = builder.toByteArray();
        
        assertNotNull("Should return non-null array", result);
        assertEquals("Should return empty array", 0, result.length);
    }

    @Test
    public void testToByteArrayWithData() {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        byte[] testData = new byte[]{1, 2, 3};
        builder.write(testData);
        
        byte[] result = builder.toByteArray();
        
        assertNotNull("Should return non-null array", result);
        assertEquals("Should return array with correct length", testData.length, result.length);
    }

    @Test
    public void testGetClearAndRelease() {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        byte[] testData = new byte[]{1, 2, 3};
        builder.write(testData);
        
        byte[] result = builder.getClearAndRelease();
        
        assertNotNull("Should return non-null array", result);
        assertEquals("Should return array with correct length", testData.length, result.length);
        // Note: After getClearAndRelease(), builder state is cleared
    }

    @Test
    public void testCompleteAndCoalesceEmpty() {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        
        byte[] result = builder.completeAndCoalesce(0);
        
        assertNotNull("Should return non-null array", result);
        assertEquals("Should return empty array", 0, result.length);
    }

    // ========== State Management Tests ==========

    @Test
    public void testReset() {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.append(TEST_BYTE);
        
        builder.reset();
        
        assertEquals("Should be empty after reset", 0, builder.size());
    }

    @Test
    public void testRelease() {
        BufferRecycler recycler = JsonRecyclerPools.NonRecyclingPool.GLOBAL.acquirePooled();
        ByteArrayBuilder builder = new ByteArrayBuilder(recycler);
        
        builder.release();
        
        // After release, the builder should be in a clean state
        assertEquals("Should be empty after release", 0, builder.size());
    }

    @Test
    public void testFlush() {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        
        // flush() is a no-op for ByteArrayBuilder, but should not throw
        builder.flush();
        
        assertEquals("Flush should not change size", 0, builder.size());
    }

    @Test
    public void testClose() {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        
        // close() should work like release()
        builder.close();
        
        assertEquals("Should be empty after close", 0, builder.size());
    }

    // ========== Edge Cases and Error Conditions ==========

    @Test(expected = NullPointerException.class)
    public void testWriteNullByteArray() {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        
        builder.write((byte[]) null);
    }

    @Test(expected = NullPointerException.class)
    public void testWriteNullByteArrayWithOffset() {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        
        builder.write(null, 0, 1);
    }

    @Test(expected = NegativeArraySizeException.class)
    public void testConstructorWithNegativeSize() {
        new ByteArrayBuilder(-1);
    }

    @Test(expected = NegativeArraySizeException.class)
    public void testCompleteAndCoalesceWithNegativeLength() {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        
        builder.completeAndCoalesce(-1);
    }

    @Test
    public void testFromInitialWithNullArray() {
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial(null, 0);
        
        // Should handle null gracefully for zero length
        assertEquals("Should handle null array with zero length", 0, builder.size());
    }

    // ========== Integration Tests ==========

    @Test
    public void testMultipleOperationsSequence() {
        ByteArrayBuilder builder = new ByteArrayBuilder();
        
        // Perform multiple operations
        builder.append(1);
        builder.appendTwoBytes(512);
        builder.appendThreeBytes(131072);
        builder.appendFourBytes(16777216);
        
        int expectedSize = 1 + 2 + 3 + 4; // 10 bytes total
        assertEquals("Should accumulate all bytes", expectedSize, builder.size());
        
        byte[] result = builder.toByteArray();
        assertEquals("Result array should have correct length", expectedSize, result.length);
    }

    @Test
    public void testSegmentTransitions() {
        ByteArrayBuilder builder = new ByteArrayBuilder(SMALL_BUFFER_SIZE);
        
        // Fill beyond initial capacity to trigger segment creation
        byte[] largeData = new byte[SMALL_BUFFER_SIZE * 2];
        builder.write(largeData);
        
        assertEquals("Should handle segment transitions", largeData.length, builder.size());
        
        byte[] result = builder.toByteArray();
        assertEquals("Result should have correct length", largeData.length, result.length);
    }

    @Test
    public void testBufferRecyclerIntegration() {
        BufferRecycler recycler = new BufferRecycler();
        ByteArrayBuilder builder = new ByteArrayBuilder(recycler);
        
        builder.append(TEST_BYTE);
        assertEquals("Should work with buffer recycler", 1, builder.size());
        
        builder.release();
        assertEquals("Should be clean after release", 0, builder.size());
    }
}