/*
 * Copyright (c) 2025 Your Name/Company
 * This file has been modified from the original EvoSuite-generated code
 * to improve readability and maintainability.
 */

package com.itextpdf.text.io;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link GroupedRandomAccessSource}.
 * The tests cover its core functionality: aggregating multiple sources, calculating total length,
 * reading data across source boundaries, and handling edge cases and exceptions.
 */
public class GroupedRandomAccessSourceTest {

    /**
     * A helper class to verify that the close() method was called on an underlying source.
     */
    private static class VerifiableRandomAccessSource extends ArrayRandomAccessSource {
        private boolean closed = false;

        public VerifiableRandomAccessSource(byte[] data) {
            super(data);
        }

        @Override
        public void close() throws IOException {
            super.close();
            this.closed = true;
        }

        public boolean isClosed() {
            return closed;
        }
    }

    // --- Constructor Tests ---

    @Test(expected = NullPointerException.class)
    public void constructor_shouldThrowNullPointerException_forNullSourceArray() throws IOException {
        // Act
        new GroupedRandomAccessSource(null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void constructor_shouldThrowException_forEmptySourceArray() throws IOException {
        // Arrange
        RandomAccessSource[] emptySources = new RandomAccessSource[0];

        // Act
        // This fails because the constructor tries to access sources[sources.length - 1], which is sources[-1].
        new GroupedRandomAccessSource(emptySources);
    }

    // --- length() Tests ---

    @Test
    public void length_shouldReturnZero_whenAllSourcesAreEmpty() throws IOException {
        // Arrange
        RandomAccessSource[] sources = {
                new ArrayRandomAccessSource(new byte[0]),
                new ArrayRandomAccessSource(new byte[0])
        };
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);

        // Act
        long length = groupedSource.length();

        // Assert
        assertEquals("Length should be 0 for empty sources.", 0L, length);
    }

    @Test
    public void length_shouldReturnSumOfSourceLengths() throws IOException {
        // Arrange
        RandomAccessSource[] sources = {
                new ArrayRandomAccessSource(new byte[5]),
                new ArrayRandomAccessSource(new byte[10]),
                new ArrayRandomAccessSource(new byte[3])
        };
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);

        // Act
        long length = groupedSource.length();

        // Assert
        assertEquals("Length should be the sum of individual source lengths.", 18L, length);
    }

    // --- get(long) Tests ---

    @Test
    public void getSingleByte_shouldReturnCorrectByte_fromFirstSource() throws IOException {
        // Arrange
        byte[] data1 = {1, 2, 3};
        byte[] data2 = {4, 5, 6};
        RandomAccessSource[] sources = {
                new ArrayRandomAccessSource(data1),
                new ArrayRandomAccessSource(data2)
        };
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);

        // Act
        int byteRead = groupedSource.get(1L);

        // Assert
        assertEquals("Should read the correct byte from the first source.", 2, byteRead);
    }

    @Test
    public void getSingleByte_shouldReturnCorrectByte_fromSecondSource() throws IOException {
        // Arrange
        byte[] data1 = {1, 2, 3}; // length 3
        byte[] data2 = {4, 5, 6}; // length 3
        RandomAccessSource[] sources = {
                new ArrayRandomAccessSource(data1),
                new ArrayRandomAccessSource(data2)
        };
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);

        // Act
        // Position 4 is the second byte of the second source (0-indexed)
        int byteRead = groupedSource.get(4L);

        // Assert
        assertEquals("Should read the correct byte from a subsequent source.", 5, byteRead);
    }

    @Test
    public void getSingleByte_shouldReturnMinusOne_whenPositionIsOutOfBounds() throws IOException {
        // Arrange
        RandomAccessSource[] sources = {new ArrayRandomAccessSource(new byte[5])};
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);

        // Act
        int byteRead = groupedSource.get(10L);

        // Assert
        assertEquals("Should return -1 for out-of-bounds position.", -1, byteRead);
    }

    // --- get(long, byte[], int, int) Tests ---

    @Test
    public void get_shouldReadAcrossSourceBoundaries() throws IOException {
        // Arrange
        byte[] data1 = new byte[11]; // Source 1, indices 0-10
        byte[] data2 = new byte[11]; // Source 2, indices 11-21
        RandomAccessSource[] sources = {
                new ArrayRandomAccessSource(data1),
                new ArrayRandomAccessSource(data2)
        };
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);
        byte[] buffer = new byte[10];

        // Act
        // Read 5 bytes starting at position 10. This should read 1 byte from source 1
        // (at index 10) and 4 bytes from source 2 (at indices 0-3).
        int bytesRead = groupedSource.get(10L, buffer, 0, 5);

        // Assert
        assertEquals("Should read the requested number of bytes across a boundary.", 5, bytesRead);
    }

    @Test
    public void get_shouldReturnPartialRead_whenReadExceedsSourceLength() throws IOException {
        // Arrange
        RandomAccessSource[] sources = {new ArrayRandomAccessSource(new byte[5])};
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);
        byte[] buffer = new byte[5];

        // Act
        // Try to read 3 bytes starting at position 3. Only 2 bytes are available (at pos 3 and 4).
        int bytesRead = groupedSource.get(3L, buffer, 0, 3);

        // Assert
        assertEquals("Should return the number of bytes actually read.", 2, bytesRead);
    }

    @Test
    public void get_shouldReturnMinusOne_whenReadingPastEndOfAllSources() throws IOException {
        // Arrange
        RandomAccessSource[] sources = {new ArrayRandomAccessSource(new byte[5])};
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);
        byte[] buffer = new byte[5];

        // Act
        // Start reading at position 5, which is past the end.
        int bytesRead = groupedSource.get(5L, buffer, 0, 1);

        // Assert
        assertEquals("Should return -1 when read position is at or beyond the end.", -1, bytesRead);
    }

    @Test
    public void get_shouldReturnMinusOne_forNegativePosition() throws IOException {
        // Arrange
        RandomAccessSource[] sources = {new ArrayRandomAccessSource(new byte[5])};
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);
        byte[] buffer = new byte[5];

        // Act
        int bytesRead = groupedSource.get(-1L, buffer, 0, 1);

        // Assert
        assertEquals("Should return -1 for a negative read position.", -1, bytesRead);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void get_shouldThrowArrayIndexOutOfBounds_forInvalidBufferOffset() throws IOException {
        // Arrange
        RandomAccessSource[] sources = {new ArrayRandomAccessSource(new byte[10])};
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);
        byte[] buffer = new byte[5];

        // Act
        // An offset of 10 is out of bounds for a buffer of length 5.
        groupedSource.get(0L, buffer, 10, 1);
    }

    // --- close() Tests ---

    @Test
    public void close_shouldCloseAllUnderlyingSources() throws IOException {
        // Arrange
        VerifiableRandomAccessSource source1 = new VerifiableRandomAccessSource(new byte[1]);
        VerifiableRandomAccessSource source2 = new VerifiableRandomAccessSource(new byte[1]);
        RandomAccessSource[] sources = {source1, source2};
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);

        // Act
        groupedSource.close();

        // Assert
        assertTrue("First underlying source should be closed.", source1.isClosed());
        assertTrue("Second underlying source should be closed.", source2.isClosed());
    }

    @Test(expected = IOException.class)
    public void get_shouldFail_afterClose() throws IOException {
        // Arrange
        RandomAccessSource[] sources = {new ArrayRandomAccessSource(new byte[10])};
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);
        groupedSource.close();

        // Act
        // This should fail because the underlying source is closed.
        // ArrayRandomAccessSource throws IOException after being closed.
        groupedSource.get(0L);
    }
}