package com.itextpdf.text.io;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link GroupedRandomAccessSource} class, focusing on data retrieval logic.
 */
public class GroupedRandomAccessSourceTest {

    /**
     * Tests that the get() method can successfully read a block of data
     * that spans across the boundary of two underlying sources.
     */
    @Test
    public void get_whenReadingAcrossSourceBoundary_writesCorrectData() throws IOException {
        // Arrange
        // Create two distinct sources with identifiable data.
        byte[] sourceData1 = "0123456789".getBytes(); // length = 10
        byte[] sourceData2 = "abcdefghij".getBytes(); // length = 10

        RandomAccessSource source1 = new ArrayRandomAccessSource(sourceData1);
        RandomAccessSource source2 = new ArrayRandomAccessSource(sourceData2);

        // Group the sources. The total length will be 20.
        // Source 1 covers indices 0-9.
        // Source 2 covers indices 10-19.
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(new RandomAccessSource[]{source1, source2});

        // We will read 4 bytes starting at position 8. This read will cross the boundary
        // between source1 and source2, reading the last 2 bytes from source1 ('8', '9')
        // and the first 2 bytes from source2 ('a', 'b').
        final long readPosition = 8;
        final int readLength = 4;
        final int bufferOffset = 2;
        byte[] destinationBuffer = new byte[10]; // A buffer larger than needed to test the offset

        // Act
        int bytesRead = groupedSource.get(readPosition, destinationBuffer, bufferOffset, readLength);

        // Assert
        // 1. Verify that the method reports the correct number of bytes read.
        assertEquals("The number of bytes read should match the requested length.", readLength, bytesRead);

        // 2. Verify that the correct data was written into the destination buffer at the correct offset.
        byte[] expectedBufferContent = {
                0, 0, // Un-touched prefix due to offset
                '8', '9', 'a', 'b', // The data read from the grouped source
                0, 0, 0, 0 // Un-touched suffix
        };
        assertArrayEquals("The data read into the buffer is incorrect.", expectedBufferContent, destinationBuffer);
    }
}