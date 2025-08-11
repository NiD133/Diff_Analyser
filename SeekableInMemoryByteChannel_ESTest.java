package org.apache.commons.compress.utils;

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SeekableByteChannel;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link SeekableInMemoryByteChannel}.
 *
 * These tests emphasize clarity and maintainability:
 * - Descriptive test names
 * - Arrange-Act-Assert structure
 * - Focus on observable behavior rather than internal implementation details
 * - No large allocations or brittle assertions
 */
public class SeekableInMemoryByteChannelTest {

    // ---------------------------------------------------------------------
    // Constructors and basic state
    // ---------------------------------------------------------------------

    @Test
    public void defaultConstructor_hasEmptyState() {
        // Arrange + Act
        SeekableInMemoryByteChannel ch = new SeekableInMemoryByteChannel();

        // Assert
        assertTrue(ch.isOpen());
        assertEquals(0L, ch.size());
        assertEquals(0L, ch.position());
        assertEquals(0, ch.array().length);
    }

    @Test
    public void byteArrayConstructor_exposesBackingArrayAndSize() {
        // Arrange
        byte[] backing = new byte[8];

        // Act
        SeekableInMemoryByteChannel ch = new SeekableInMemoryByteChannel(backing);

        // Assert
        assertSame("array() should return the internal array reference", backing, ch.array());
        assertEquals(8L, ch.size());
        assertEquals(0L, ch.position());
    }

    @Test
    public void sizeConstructor_allocatesBackingStorage() {
        // Arrange + Act
        SeekableInMemoryByteChannel ch = new SeekableInMemoryByteChannel(5);

        // Assert
        assertEquals(5L, ch.size());
        assertEquals(0L, ch.position());
        assertEquals(5, ch.array().length);
    }

    // ---------------------------------------------------------------------
    // Read behavior
    // ---------------------------------------------------------------------

    @Test
    public void readFromEmptyChannel_returnsMinusOne() throws IOException {
        // Arrange
        SeekableInMemoryByteChannel ch = new SeekableInMemoryByteChannel();
        ByteBuffer buf = ByteBuffer.allocate(10);

        // Act
        int read = ch.read(buf);

        // Assert
        assertEquals(-1, read);
        assertEquals(0L, ch.position());
        assertEquals(0L, ch.size());
    }

    @Test
    public void readIntoEmptyBuffer_returnsZeroAndDoesNotMovePosition() throws IOException {
        // Arrange
        SeekableInMemoryByteChannel ch = new SeekableInMemoryByteChannel(new byte[5]);
        ch.position(1);
        ByteBuffer empty = ByteBuffer.allocate(0);

        // Act
        int read = ch.read(empty);

        // Assert
        assertEquals(0, read);
        assertEquals(1L, ch.position());
        assertEquals(5L, ch.size());
    }

    @Test
    public void readAllAvailableBytes_thenSubsequentReadReturnsEOF() throws IOException {
        // Arrange
        byte[] data = new byte[] {1, 2, 3};
        SeekableInMemoryByteChannel ch = new SeekableInMemoryByteChannel(data);
        ByteBuffer buf = ByteBuffer.allocate(10);

        // Act
        int first = ch.read(buf);
        int second = ch.read(buf);

        // Assert
        assertEquals(3, first);
        assertEquals(-1, second);
        assertEquals(3L, ch.position());
        assertEquals(3L, ch.size());
    }

    // ---------------------------------------------------------------------
    // Write behavior
    // ---------------------------------------------------------------------

    @Test
    public void writeEmptyBuffer_doesNotChangePositionOrSize_onPreAllocatedChannel() throws IOException {
        // Arrange
        SeekableInMemoryByteChannel ch = new SeekableInMemoryByteChannel(new byte[5]);
        ByteBuffer empty = ByteBuffer.allocate(0);

        // Act
        int written = ch.write(empty);

        // Assert
        assertEquals(0, written);
        assertEquals(0L, ch.position());
        assertEquals(5L, ch.size());
    }

    @Test
    public void writeGrowsBufferAndUpdatesPositionAndSize() throws IOException {
        // Arrange
        SeekableInMemoryByteChannel ch = new SeekableInMemoryByteChannel();
        ByteBuffer buf = ByteBuffer.wrap(new byte[] {10, 20, 30, 40, 50});

        // Act
        int written = ch.write(buf);

        // Assert
        assertEquals(5, written);
        assertEquals(5L, ch.position());
        assertEquals(5L, ch.size());

        byte[] array = ch.array();
        assertTrue("Internal array should be at least as big as size", array.length >= 5);
        assertArrayEquals(new byte[] {10, 20, 30, 40, 50}, copyPrefix(array, 5));
    }

    @Test
    public void writeFromDirectBuffer_works() throws IOException {
        // Arrange
        SeekableInMemoryByteChannel ch = new SeekableInMemoryByteChannel();
        ByteBuffer direct = ByteBuffer.allocateDirect(2);
        direct.put((byte) 1).put((byte) 2).flip();

        // Act
        int written = ch.write(direct);

        // Assert
        assertEquals(2, written);
        assertEquals(2L, ch.position());
        assertEquals(2L, ch.size());
        assertArrayEquals(new byte[] {1, 2}, copyPrefix(ch.array(), 2));
    }

    // ---------------------------------------------------------------------
    // Positioning
    // ---------------------------------------------------------------------

    @Test
    public void setPositionWithinBounds_movesPositionOnly() throws IOException {
        // Arrange
        SeekableInMemoryByteChannel ch = new SeekableInMemoryByteChannel();

        // Act
        ch.position(5);

        // Assert
        assertEquals(5L, ch.position());
        assertEquals(0L, ch.size()); // size should not grow until write occurs
        ByteBuffer buf = ByteBuffer.allocate(1);
        assertEquals(-1, ch.read(buf)); // nothing to read at that position
    }

    @Test
    public void setPosition_negative_throwsIOException() {
        // Arrange
        SeekableInMemoryByteChannel ch = new SeekableInMemoryByteChannel();

        // Act + Assert
        try {
            ch.position(-1);
            fail("Expected IOException for negative position");
        } catch (IOException expected) {
            // expected
        }
    }

    @Test
    public void setPosition_greaterThanIntegerMax_throwsIOException() {
        // Arrange
        SeekableInMemoryByteChannel ch = new SeekableInMemoryByteChannel();

        // Act + Assert
        try {
            ch.position(1L + Integer.MAX_VALUE);
            fail("Expected IOException for too-large position");
        } catch (IOException expected) {
            // expected
        }
    }

    // ---------------------------------------------------------------------
    // Truncation
    // ---------------------------------------------------------------------

    @Test
    public void truncateShrinksAndAdjustsPosition() {
        // Arrange
        byte[] data = new byte[10];
        SeekableInMemoryByteChannel ch = new SeekableInMemoryByteChannel(data);
        try {
            ch.position(8);
        } catch (IOException e) {
            fail("Unexpected IOException setting position: " + e.getMessage());
        }

        // Act
        SeekableByteChannel result = ch.truncate(5);

        // Assert
        assertSame(ch, result);
        assertEquals(5L, ch.size());
        assertEquals(5L, ch.position()); // position is moved to new size if it was beyond it
    }

    @Test
    public void truncateLargerThanCurrentSize_keepsSizeUnchanged() {
        // Arrange
        byte[] data = new byte[3];
        SeekableInMemoryByteChannel ch = new SeekableInMemoryByteChannel(data);

        // Act
        SeekableByteChannel result = ch.truncate(Integer.MAX_VALUE);

        // Assert
        assertSame(ch, result);
        assertEquals(3L, ch.size());
        assertEquals(0L, ch.position());
    }

    @Test
    public void truncateWithNegativeSize_throwsIllegalArgumentException() {
        // Arrange
        SeekableInMemoryByteChannel ch = new SeekableInMemoryByteChannel();

        // Act + Assert
        try {
            ch.truncate(-1);
            fail("Expected IllegalArgumentException for negative truncate size");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    @Test
    public void truncateWithSizeGreaterThanIntegerMax_throwsIllegalArgumentException() {
        // Arrange
        SeekableInMemoryByteChannel ch = new SeekableInMemoryByteChannel();

        // Act + Assert
        try {
            ch.truncate(1L + Integer.MAX_VALUE);
            fail("Expected IllegalArgumentException for too-large truncate size");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    // ---------------------------------------------------------------------
    // Closing behavior
    // ---------------------------------------------------------------------

    @Test
    public void isOpenReflectsClosedState() throws IOException {
        // Arrange
        SeekableInMemoryByteChannel ch = new SeekableInMemoryByteChannel(1);

        // Act + Assert
        assertTrue(ch.isOpen());
        ch.close();
        assertFalse(ch.isOpen());
    }

    @Test
    public void readAfterClose_throwsClosedChannelException() throws IOException {
        // Arrange
        SeekableInMemoryByteChannel ch = new SeekableInMemoryByteChannel();
        ch.close();

        // Act + Assert
        try {
            ch.read(ByteBuffer.allocate(1));
            fail("Expected ClosedChannelException");
        } catch (ClosedChannelException expected) {
            // expected
        }
    }

    @Test
    public void writeAfterClose_throwsClosedChannelException() throws IOException {
        // Arrange
        SeekableInMemoryByteChannel ch = new SeekableInMemoryByteChannel();
        ch.close();

        // Act + Assert
        try {
            ch.write(ByteBuffer.allocate(1));
            fail("Expected ClosedChannelException");
        } catch (ClosedChannelException expected) {
            // expected
        }
    }

    @Test
    public void setPositionAfterClose_throwsClosedChannelException() throws IOException {
        // Arrange
        SeekableInMemoryByteChannel ch = new SeekableInMemoryByteChannel();
        ch.close();

        // Act + Assert
        try {
            ch.position(0);
            fail("Expected ClosedChannelException");
        } catch (ClosedChannelException expected) {
            // expected
        }
    }

    @Test
    public void sizeAndPositionRemainQueryableAfterClose() throws IOException {
        // Arrange
        SeekableInMemoryByteChannel ch = new SeekableInMemoryByteChannel();
        ch.write(ByteBuffer.wrap(new byte[] {42, 43, 44})); // size=3, pos=3
        ch.position(2); // pos=2

        // Act
        ch.close();

        // Assert (spec says size() and position() do not throw on closed channel)
        assertEquals(3L, ch.size());
        assertEquals(2L, ch.position());
    }

    // ---------------------------------------------------------------------
    // Argument validation
    // ---------------------------------------------------------------------

    @Test
    public void constructorWithNullArray_throwsNullPointerException() {
        try {
            new SeekableInMemoryByteChannel((byte[]) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException expected) {
            // expected
        }
    }

    @Test
    public void constructorWithNegativeSize_throwsNegativeArraySizeException() {
        try {
            new SeekableInMemoryByteChannel(-1);
            fail("Expected NegativeArraySizeException");
        } catch (NegativeArraySizeException expected) {
            // expected
        }
    }

    @Test
    public void writeWithNullBuffer_throwsNullPointerException() throws IOException {
        // Arrange
        SeekableInMemoryByteChannel ch = new SeekableInMemoryByteChannel();

        // Act + Assert
        try {
            ch.write(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException expected) {
            // expected
        }
    }

    @Test
    public void readWithNullBuffer_throwsNullPointerException() throws IOException {
        // Arrange
        SeekableInMemoryByteChannel ch = new SeekableInMemoryByteChannel();

        // Act + Assert
        try {
            ch.read(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException expected) {
            // expected
        }
    }

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------

    private static byte[] copyPrefix(byte[] src, int length) {
        byte[] out = new byte[length];
        System.arraycopy(src, 0, out, 0, length);
        return out;
    }
}