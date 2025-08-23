package com.itextpdf.text.io;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * This test class contains tests for {@link GroupedRandomAccessSource}.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class GroupedRandomAccessSource_ESTestTest2 extends GroupedRandomAccessSource_ESTest_scaffolding {

    /**
     * Tests that the get(long, byte[], int, int) method handles invalid arguments gracefully.
     *
     * <p>Specifically, it verifies that providing a negative length and offset results in a
     * return value of -1. This is a common convention for read methods to indicate
     * an invalid operation or end-of-stream, rather than throwing an exception like
     * {@link IndexOutOfBoundsException}.</p>
     *
     * @throws IOException if an I/O error occurs during source creation.
     */
    @Test
    public void getWithNegativeLengthAndOffsetShouldReturnMinusOne() throws IOException {
        // --- Arrange ---
        // Create a GroupedRandomAccessSource from two underlying sources.
        // Both sources are based on the same single-byte array, giving each a length of 1.
        // The resulting grouped source will have a total length of 2.
        byte[] sourceData = new byte[1];
        RandomAccessSource source1 = new ArrayRandomAccessSource(sourceData);
        RandomAccessSource source2 = new GetBufferedRandomAccessSource(source1); // A wrapper around the first source

        RandomAccessSource[] sources = {source1, source2};
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);

        // Sanity check the setup: ensure the total length is as expected.
        assertEquals("The total length of the grouped source should be the sum of its parts.", 2L, groupedSource.length());

        // Define a buffer for the read operation and invalid parameters.
        byte[] destinationBuffer = new byte[1];
        long readPosition = 0L;
        int invalidOffset = -10;
        int invalidLength = -10;

        // --- Act ---
        // Attempt to read from the source using a negative offset and length.
        int bytesRead = groupedSource.get(readPosition, destinationBuffer, invalidOffset, invalidLength);

        // --- Assert ---
        // The method is expected to return -1 when provided with invalid parameters like a negative length.
        assertEquals("get() should return -1 for a negative read length.", -1, bytesRead);
    }
}