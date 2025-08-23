package com.itextpdf.text.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.IOException;

// Unused imports from the original test have been removed for clarity.

public class GroupedRandomAccessSource_ESTestTest22 extends GroupedRandomAccessSource_ESTest_scaffolding {

    /**
     * Verifies that attempting to read from a position equal to the source's length
     * correctly returns -1, indicating the end of the source has been reached.
     */
    @Test(timeout = 4000)
    public void get_whenReadingAtSourceEnd_returnsNegativeOne() throws IOException {
        // Arrange: Create a GroupedRandomAccessSource with a total length of 1 byte.
        byte[] sourceContent = new byte[1];
        RandomAccessSource singleSource = new ArrayRandomAccessSource(sourceContent);
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(new RandomAccessSource[]{singleSource});

        // Define a read operation that starts exactly at the end of the source data.
        // The source has 1 byte at index 0. A read starting at position 1 is out of bounds.
        long readPosition = 1L;
        byte[] destinationBuffer = new byte[1];
        int bufferOffset = 0;
        int readLength = 1;

        // Act: Attempt to read from the specified position.
        int bytesRead = groupedSource.get(readPosition, destinationBuffer, bufferOffset, readLength);

        // Assert: The method should return -1 (EOF) and the source length should be correct.
        assertEquals("The total length of the grouped source should be 1.", 1L, groupedSource.length());
        assertEquals("Reading at or after the end of the source should return -1.", -1, bytesRead);
    }
}