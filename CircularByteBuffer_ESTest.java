package org.apache.commons.io.input.buffer;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class CircularByteBufferTest {

    private static final int DEFAULT_CAPACITY = IOUtils.DEFAULT_BUFFER_SIZE;

    // Constructors

    @Test
    public void defaultConstructor_hasFullSpace_andNoBytes() {
        CircularByteBuffer buf = new CircularByteBuffer();

        assertEquals(DEFAULT_CAPACITY, buf.getSpace());
        assertEquals(0, buf.getCurrentNumberOfBytes());
        assertTrue(buf.hasSpace());
        assertFalse(buf.hasBytes());
    }

    @Test
    public void zeroCapacity_hasNoSpace_andNoBytes() {
        CircularByteBuffer buf = new CircularByteBuffer(0);

        assertEquals(0, buf.getSpace());
        assertEquals(0, buf.getCurrentNumberOfBytes());
        assertFalse(buf.hasSpace());
        assertFalse(buf.hasBytes());
        assertTrue(buf.hasSpace(0));
    }

    @Test
    public void negativeCapacity_throwsNegativeArraySizeException() {
        assertThrows(NegativeArraySizeException.class, () -> new CircularByteBuffer(-1));
    }

    // Single-byte add/read

    @Test
    public void addOneByte_thenReadOneByte_roundTrip() {
        CircularByteBuffer buf = new CircularByteBuffer(1);

        buf.add((byte) 0x7F);
        assertEquals(0, buf.getSpace());
        assertTrue(buf.hasBytes());

        byte read = buf.read();
        assertEquals((byte) 0x7F, read);
        assertEquals(1, buf.getSpace());
        assertFalse(buf.hasBytes());
    }

    @Test
    public void addWhenFull_throwsIllegalStateException() {
        CircularByteBuffer buf = new CircularByteBuffer(0);

        assertThrows(IllegalStateException.class, () -> buf.add((byte) 123));
    }

    @Test
    public void readWhenEmpty_throwsIllegalStateException() {
        CircularByteBuffer buf = new CircularByteBuffer();

        assertThrows(IllegalStateException.class, buf::read);
    }

    // Bulk add/read

    @Test
    public void addArray_thenReadArray_roundTrip_noWrap() {
        CircularByteBuffer buf = new CircularByteBuffer(4);
        byte[] data = {10, 20, 30, 40};
        byte[] out = new byte[4];

        buf.add(data, 0, 4);
        assertEquals(0, buf.getSpace());
        assertEquals(4, buf.getCurrentNumberOfBytes());

        buf.read(out, 0, 4);
        assertArrayEquals(data, out);
        assertEquals(4, buf.getSpace());
        assertEquals(0, buf.getCurrentNumberOfBytes());
    }

    @Test
    public void addArray_thenReadArray_roundTrip_withWrapAround() {
        CircularByteBuffer buf = new CircularByteBuffer(4);

        // Force wrap-around: write 3, read 2, write 3
        buf.add(new byte[]{1, 2, 3}, 0, 3);
        buf.read(new byte[2], 0, 2);
        buf.add(new byte[]{4, 5, 6}, 0, 3); // wraps internally

        byte[] out = new byte[3];
        buf.read(out, 0, 3);
        assertArrayEquals(new byte[]{3, 4, 5}, out);
        assertEquals(2, buf.getSpace()); // One byte (6) still in the buffer
        assertEquals(1, buf.getCurrentNumberOfBytes());
    }

    @Test
    public void addArray_zeroLength_isNoOp() {
        CircularByteBuffer buf = new CircularByteBuffer();
        int before = buf.getSpace();

        buf.add(new byte[8], 0, 0);

        assertEquals(before, buf.getSpace());
        assertEquals(0, buf.getCurrentNumberOfBytes());
    }

    @Test
    public void readArray_zeroLength_isNoOp() {
        CircularByteBuffer buf = new CircularByteBuffer();
        int before = buf.getSpace();

        buf.read(new byte[1], 0, 0);

        assertEquals(before, buf.getSpace());
        assertEquals(0, buf.getCurrentNumberOfBytes());
    }

    @Test
    public void readArray_whenInsufficientBytes_throwsIllegalStateException() {
        CircularByteBuffer buf = new CircularByteBuffer();

        assertThrows(IllegalStateException.class, () -> buf.read(new byte[8], 0, 1));
    }

    // peek

    @Test
    public void peek_nullBuffer_throwsNullPointerException() {
        CircularByteBuffer buf = new CircularByteBuffer();

        assertThrows(NullPointerException.class, () -> buf.peek(null, 0, 0));
    }

    @Test
    public void peek_invalidOffsetOrLength_throwsIllegalArgumentException() {
        CircularByteBuffer buf = new CircularByteBuffer();
        byte[] src = new byte[4];

        assertThrows(IllegalArgumentException.class, () -> buf.peek(src, -1, 1));
        assertThrows(IllegalArgumentException.class, () -> buf.peek(src, 0, -1));
        assertThrows(IllegalArgumentException.class, () -> buf.peek(src, 5, 0));
    }

    @Test
    public void peek_offsetPlusLengthBeyondArray_throws() {
        CircularByteBuffer buf = new CircularByteBuffer();
        byte[] src = new byte[4];

        Throwable t = assertThrows(Throwable.class, () -> buf.peek(src, 3, 2));
        assertTrue(t instanceof IllegalArgumentException || t instanceof ArrayIndexOutOfBoundsException);
    }

    @Test
    public void peek_returnsTrue_whenUpcomingBytesMatch() {
        CircularByteBuffer buf = new CircularByteBuffer(4);
        buf.add(new byte[]{9, 8, 7, 6}, 0, 3);

        assertTrue(buf.peek(new byte[]{0, 9, 8, 7, 0}, 1, 3));
        assertFalse(buf.peek(new byte[]{9, 9, 7}, 0, 3)); // mismatch at second byte
        assertFalse(buf.peek(new byte[]{9, 8, 7, 6}, 0, 4)); // not enough bytes to match 4
    }

    // Validation: add

    @Test
    public void addArray_null_throwsNullPointerException() {
        CircularByteBuffer buf = new CircularByteBuffer();

        assertThrows(NullPointerException.class, () -> buf.add(null, 0, 1));
    }

    @Test
    public void addArray_negativeOffsetOrLength_throwsIllegalArgumentException() {
        CircularByteBuffer buf = new CircularByteBuffer();
        byte[] src = new byte[4];

        assertThrows(IllegalArgumentException.class, () -> buf.add(src, -1, 1));
        assertThrows(IllegalArgumentException.class, () -> buf.add(src, 0, -1));
        assertThrows(IllegalArgumentException.class, () -> buf.add(src, 5, 0));
    }

    @Test
    public void addArray_offsetPlusLengthBeyondArray_throws() {
        CircularByteBuffer buf = new CircularByteBuffer();
        byte[] src = new byte[4];

        Throwable t = assertThrows(Throwable.class, () -> buf.add(src, 3, 2));
        assertTrue(t instanceof IllegalArgumentException || t instanceof ArrayIndexOutOfBoundsException);
    }

    @Test
    public void addArray_whenInsufficientSpace_throwsIllegalStateException() {
        CircularByteBuffer buf = new CircularByteBuffer(2);
        byte[] src = new byte[4];

        // Fill up to capacity
        buf.add(src, 0, 2);
        assertEquals(0, buf.getSpace());

        // No space left
        assertThrows(IllegalStateException.class, () -> buf.add(src, 0, 1));
    }

    // Validation: read

    @Test
    public void readArray_null_throwsNullPointerException() {
        CircularByteBuffer buf = new CircularByteBuffer();

        assertThrows(NullPointerException.class, () -> buf.read(null, 0, 1));
    }

    @Test
    public void readArray_negativeOffsetOrLength_throwsIllegalArgumentException() {
        CircularByteBuffer buf = new CircularByteBuffer();
        byte[] dst = new byte[4];

        assertThrows(IllegalArgumentException.class, () -> buf.read(dst, -1, 1));
        assertThrows(IllegalArgumentException.class, () -> buf.read(dst, 0, -1));
        assertThrows(IllegalArgumentException.class, () -> buf.read(dst, 5, 0));
    }

    @Test
    public void readArray_offsetPlusLengthBeyondArray_throws() {
        CircularByteBuffer buf = new CircularByteBuffer();
        byte[] dst = new byte[4];

        Throwable t = assertThrows(Throwable.class, () -> buf.read(dst, 2, 3));
        assertTrue(t instanceof IllegalArgumentException || t instanceof ArrayIndexOutOfBoundsException);
    }

    // Space/bytes helpers

    @Test
    public void hasSpace_withCount() {
        CircularByteBuffer buf = new CircularByteBuffer();

        assertTrue(buf.hasSpace(1024));
        assertFalse(buf.hasSpace(Integer.MAX_VALUE));
        assertEquals(DEFAULT_CAPACITY, buf.getSpace());
    }

    @Test
    public void clear_resetsState() {
        CircularByteBuffer buf = new CircularByteBuffer();
        buf.add((byte) 1);
        buf.add((byte) 2);
        assertTrue(buf.hasBytes());

        buf.clear();

        assertEquals(DEFAULT_CAPACITY, buf.getSpace());
        assertFalse(buf.hasBytes());
        assertEquals(0, buf.getCurrentNumberOfBytes());
    }
}