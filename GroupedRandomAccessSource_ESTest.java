package com.itextpdf.text.io;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Readability-focused tests for GroupedRandomAccessSource.
 *
 * These tests exercise core behaviors with small, deterministic in-memory sources.
 * They avoid brittle implementation details (e.g., specific exception messages or
 * JVM/bytebuffer quirks), and use clear Arrange–Act–Assert structure.
 */
public class GroupedRandomAccessSourceTest {

    // -----------------------------------------
    // Helpers
    // -----------------------------------------

    private static ArrayRandomAccessSource arraySource(byte... bytes) {
        return new ArrayRandomAccessSource(bytes);
    }

    /**
     * A tiny in-memory RandomAccessSource used only to verify that close() is
     * propagated to children. It also implements sensible read semantics.
     */
    private static final class TrackableRandomAccessSource implements RandomAccessSource {
        private final byte[] data;
        private boolean closed;

        TrackableRandomAccessSource(byte[] data) {
            this.data = data != null ? data.clone() : new byte[0];
        }

        boolean isClosed() {
            return closed;
        }

        @Override
        public int get(long position) throws IOException {
            ensureOpen();
            if (position < 0 || position >= data.length) {
                return -1;
            }
            return data[(int) position] & 0xFF;
        }

        @Override
        public int get(long position, byte[] bytes, int off, int len) throws IOException {
            ensureOpen();
            if (bytes == null) throw new NullPointerException("bytes");
            // mimic common array bounds checks
            if (off < 0 || len < 0 || off + len > bytes.length) {
                throw new ArrayIndexOutOfBoundsException();
            }
            if (len == 0) return 0;
            if (position < 0 || position >= data.length) return -1;

            int toRead = (int) Math.min(len, data.length - position);
            System.arraycopy(data, (int) position, bytes, off, toRead);
            return toRead;
        }

        @Override
        public long length() {
            return data.length;
        }

        @Override
        public void close() {
            closed = true;
        }

        private void ensureOpen() {
            if (closed) {
                throw new IllegalStateException("Already closed");
            }
        }
    }

    // -----------------------------------------
    // Tests
    // -----------------------------------------

    @Test
    public void length_isSumOfChildLengths() throws Exception {
        // Arrange
        RandomAccessSource s1 = arraySource((byte) 0, (byte) 1, (byte) 2, (byte) 3); // len 4
        RandomAccessSource s2 = arraySource((byte) 10, (byte) 11);                   // len 2

        // Act
        GroupedRandomAccessSource group = new GroupedRandomAccessSource(new RandomAccessSource[]{s1, s2});

        // Assert
        assertEquals(6L, group.length());
    }

    @Test
    public void singleByteRead_readsFromCorrectChildBasedOnOffset() throws Exception {
        // Arrange
        RandomAccessSource s1 = arraySource((byte) 0, (byte) 1, (byte) 2, (byte) 3);
        RandomAccessSource s2 = arraySource((byte) 10, (byte) 11);
        GroupedRandomAccessSource group = new GroupedRandomAccessSource(new RandomAccessSource[]{s1, s2});

        // Act + Assert
        assertEquals("Offset inside first child", 2, group.get(2L));
        assertEquals("Offset at the beginning of second child", 10, group.get(4L));
    }

    @Test
    public void singleByteRead_returnsMinusOneBeyondEof() throws Exception {
        // Arrange
        RandomAccessSource s1 = arraySource((byte) 1, (byte) 2, (byte) 3);
        GroupedRandomAccessSource group = new GroupedRandomAccessSource(new RandomAccessSource[]{s1});

        // Act
        int result = group.get(12L);

        // Assert
        assertEquals(-1, result);
        assertEquals(3L, group.length());
    }

    @Test
    public void bufferedRead_spansAcrossChildBoundaries() throws Exception {
        // Arrange
        // Total bytes = [1,2,3,4,5,6,7]
        RandomAccessSource s1 = arraySource((byte) 1, (byte) 2, (byte) 3, (byte) 4);
        RandomAccessSource s2 = arraySource((byte) 5, (byte) 6, (byte) 7);
        GroupedRandomAccessSource group = new GroupedRandomAccessSource(new RandomAccessSource[]{s1, s2});

        byte[] out = new byte[5];

        // Act: read starting from global position 2 => [3,4,5,6,7]
        int read = group.get(2L, out, 0, out.length);

        // Assert
        assertEquals(5, read);
        assertArrayEquals(new byte[]{3, 4, 5, 6, 7}, out);
    }

    @Test
    public void bufferedRead_returnsPartialCountAtEnd() throws Exception {
        // Arrange
        RandomAccessSource s1 = arraySource((byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4);
        GroupedRandomAccessSource group = new GroupedRandomAccessSource(new RandomAccessSource[]{s1});

        byte[] out = new byte[3];

        // Act: request 3 bytes starting at position 3 -> only [3,4] available
        int read = group.get(3L, out, 0, 3);

        // Assert
        assertEquals(2, read);
        assertArrayEquals(new byte[]{3, 4, 0}, out);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void bufferedRead_throwsWhenDestinationIndicesAreInvalid() throws Exception {
        // Arrange
        RandomAccessSource s1 = arraySource((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7);
        GroupedRandomAccessSource group = new GroupedRandomAccessSource(new RandomAccessSource[]{s1});
        byte[] out = new byte[7];

        // Act + Assert (expect AIOOBE)
        group.get(1L, out, 5239, 1);
    }

    @Test
    public void zeroLengthChild_isHandledAndSkipped() throws Exception {
        // Arrange
        RandomAccessSource empty = arraySource(); // len 0
        RandomAccessSource s2 = arraySource((byte) 9, (byte) 8);
        GroupedRandomAccessSource group = new GroupedRandomAccessSource(new RandomAccessSource[]{empty, s2});

        // Act + Assert
        assertEquals(2L, group.length());
        assertEquals(9, group.get(0L));
        assertEquals(8, group.get(1L));
        assertEquals(-1, group.get(2L));
    }

    @Test
    public void close_closesAllChildren_andFurtherReadsFail() throws Exception {
        // Arrange
        TrackableRandomAccessSource trackable = new TrackableRandomAccessSource(new byte[]{42});
        RandomAccessSource s2 = arraySource((byte) 1, (byte) 2);
        GroupedRandomAccessSource group = new GroupedRandomAccessSource(new RandomAccessSource[]{trackable, s2});

        // Act
        group.close();

        // Assert: child close was propagated
        assertTrue(trackable.isClosed());

        // And further reads should fail with some exception (type may vary by implementation)
        try {
            group.get(0L);
            fail("Expected an exception when reading after close()");
        } catch (Exception expected) {
            // OK: we only assert that an exception is thrown after close()
        }
    }
}