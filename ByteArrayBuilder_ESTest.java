package com.fasterxml.jackson.core.util;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for ByteArrayBuilder.
 *
 * Goals:
 * - Cover common usage patterns (byte writes, multi-byte appends, coalescing).
 * - Exercise both stream-style and "manual" segment APIs.
 * - Avoid brittle assertions tied to internal constants or EvoSuite specifics.
 * - Use clear Arrange/Act/Assert structure and descriptive names.
 */
public class ByteArrayBuilderTest {

    @Test
    public void writesSingleByte_incrementsSize_andPreservesValue() {
        // Arrange
        ByteArrayBuilder b = new ByteArrayBuilder();

        // Act
        b.write(0x7F);
        byte[] out = b.toByteArray();

        // Assert
        assertEquals(1, b.size());
        assertArrayEquals(new byte[] { (byte) 0x7F }, out);
    }

    @Test
    public void writeArray_copiesAllBytes_andUpdatesSize() {
        // Arrange
        ByteArrayBuilder b = new ByteArrayBuilder();
        byte[] input = { 1, 2, 3 };

        // Act
        b.write(input);
        byte[] out = b.toByteArray();

        // Assert
        assertEquals(3, b.size());
        assertArrayEquals(input, out);
    }

    @Test
    public void writeArraySlice_copiesRequestedSlice_only() {
        // Arrange
        ByteArrayBuilder b = new ByteArrayBuilder();
        byte[] input = { 0, 1, 2, 3, 4 };

        // Act
        b.write(input, 1, 3); // {1,2,3}
        byte[] out = b.toByteArray();

        // Assert
        assertEquals(3, b.size());
        assertArrayEquals(new byte[] { 1, 2, 3 }, out);
    }

    @Test
    public void appendTwoThreeFourBytes_writeBigEndian_andIncreaseSize() {
        // Arrange
        ByteArrayBuilder b = new ByteArrayBuilder(8);

        // Act
        b.appendTwoBytes(0xABCD);       // AB CD
        b.appendThreeBytes(0x010203);   // 01 02 03
        b.appendFourBytes(0x04050607);  // 04 05 06 07

        byte[] out = b.toByteArray();

        // Assert
        assertEquals(2 + 3 + 4, b.size());
        assertArrayEquals(new byte[]{
                (byte) 0xAB, (byte) 0xCD,
                (byte) 0x01, (byte) 0x02, (byte) 0x03,
                (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07
        }, out);
    }

    @Test
    public void finishCurrentSegment_afterFilling_returnsNewSegment_andKeepsSizeOfCompletedSegment() {
        // Arrange
        ByteArrayBuilder b = new ByteArrayBuilder();
        byte[] current = b.getCurrentSegment();
        int capacity = current.length;

        // Act
        // fill current segment fully using stream API
        b.write(new byte[capacity]);
        byte[] next = b.finishCurrentSegment();

        // Assert
        assertNotNull(next);
        assertNotSame("finishCurrentSegment must return a new segment", current, next);
        assertEquals("size should reflect completed segment", capacity, b.size());
        assertEquals("current segment length should be 0 after moving to a new segment", 0, b.getCurrentSegmentLength());
    }

    @Test
    public void manualApi_endToEnd_withTwoSegments_coalescesInOrder() {
        // Arrange
        ByteArrayBuilder b = new ByteArrayBuilder();

        // Segment 1: write 3 bytes manually
        byte[] seg1 = b.resetAndGetFirstSegment();
        seg1[0] = 10; seg1[1] = 11; seg1[2] = 12;
        b.setCurrentSegmentLength(3);

        // Move to segment 2
        byte[] seg2 = b.finishCurrentSegment();
        seg2[0] = 13; seg2[1] = 14;
        b.setCurrentSegmentLength(2);

        // Act
        byte[] result = b.completeAndCoalesce(2);

        // Assert
        assertArrayEquals(new byte[] { 10, 11, 12, 13, 14 }, result);
        assertEquals("coalescing should not leave residual size", 0, b.size());
    }

    @Test
    public void reset_clearsContent_andResetsLengths() {
        // Arrange
        ByteArrayBuilder b = new ByteArrayBuilder();
        b.write(new byte[] { 1, 2, 3, 4, 5 });

        // Act
        b.reset();

        // Assert
        assertEquals(0, b.size());
        assertEquals(0, b.getCurrentSegmentLength());
        assertNotNull("after reset, a current segment should be available", b.getCurrentSegment());
    }

    @Test
    public void toByteArray_onNewBuilder_returnsEmptyArray() {
        // Arrange
        ByteArrayBuilder b = new ByteArrayBuilder();

        // Act
        byte[] out = b.toByteArray();

        // Assert
        assertEquals(0, b.size());
        assertArrayEquals(new byte[] {}, out);
    }

    @Test
    public void getClearAndRelease_returnsContent_andResetsStateForReuse() {
        // Arrange
        ByteArrayBuilder b = new ByteArrayBuilder();
        b.write(new byte[] { 1, 2, 3 });

        // Act
        byte[] out = b.getClearAndRelease();

        // Assert: content returned and builder is clear for reuse
        assertArrayEquals(new byte[] { 1, 2, 3 }, out);
        assertEquals(0, b.size());
        assertArrayEquals(new byte[] {}, b.toByteArray());
    }

    @Test
    public void bufferRecycler_accessor_isNullByDefault_andNonNullWhenProvided() {
        // default constructor -> no recycler
        ByteArrayBuilder b1 = new ByteArrayBuilder();
        assertNull(b1.bufferRecycler());

        // with recycler -> same recycler back
        BufferRecycler recycler = new BufferRecycler();
        ByteArrayBuilder b2 = new ByteArrayBuilder(recycler);
        assertSame(recycler, b2.bufferRecycler());
    }

    @Test
    public void crossingSegmentBoundary_keepsBytesInOrder() {
        // Arrange: very small first block to force boundary crossing
        ByteArrayBuilder b = new ByteArrayBuilder(2);

        // Act
        b.write(new byte[] { 1, 2, 3, 4, 5 }); // crosses boundary
        byte[] out = b.toByteArray();

        // Assert
        assertEquals(5, b.size());
        assertArrayEquals(new byte[] { 1, 2, 3, 4, 5 }, out);
    }

    @Test(expected = NullPointerException.class)
    public void write_nullArray_throwsNPE() {
        // Arrange
        ByteArrayBuilder b = new ByteArrayBuilder();

        // Act
        b.write((byte[]) null);
    }

    @Test
    public void fromInitial_usesProvidedInitialBlock_andLength() {
        // Arrange
        byte[] initial = { 9, 8, 7, 6 };

        // Act
        ByteArrayBuilder b = ByteArrayBuilder.fromInitial(initial, 2);

        // Assert
        assertEquals("size should start with initial length", 2, b.size());
        assertEquals(2, b.getCurrentSegmentLength());
        assertSame("current segment should be the same as provided initial block", initial, b.getCurrentSegment());

        // And writing more should append after the initial length
        b.append(0xFF);
        byte[] out = b.toByteArray();
        assertEquals(3, out.length);
        assertArrayEquals(new byte[] { 9, 8, (byte) 0xFF }, Arrays.copyOf(out, 3));
    }
}