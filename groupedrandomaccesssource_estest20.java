package com.itextpdf.text.io;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

// Note: The original test class name and inheritance are preserved for compatibility
// with the existing test suite structure.
public class GroupedRandomAccessSource_ESTestTest20 extends GroupedRandomAccessSource_ESTest_scaffolding {

    /**
     * Tests that the get() method correctly handles a read request that extends
     * beyond the end of the source data.
     *
     * <p>In this scenario, the method should read only the available bytes up to
     * the end of the source and return the count of bytes that were actually read.
     */
    @Test
    public void get_whenReadingPastEndOfSource_returnsActualBytesRead() throws IOException {
        // --- Arrange ---
        // 1. Create a single source with 5 bytes of data.
        byte[] sourceData = new byte[5];
        RandomAccessSource singleSource = new ArrayRandomAccessSource(sourceData);
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(new RandomAccessSource[]{singleSource});

        // 2. Define a read operation that attempts to read past the end of the source.
        // We want to read 3 bytes starting from position 3.
        // The source only has data at positions 3 and 4.
        long readPosition = 3;
        int requestedLength = 3;

        // 3. Prepare a destination buffer.
        byte[] destinationBuffer = new byte[10]; // A buffer large enough for the operation.
        int bufferOffset = 3;

        // 4. Determine the expected outcome.
        // Since the source is 5 bytes long, a read from position 3 can only yield 2 bytes.
        int expectedBytesRead = 2; // (source length) 5 - (read position) 3 = 2

        // --- Act ---
        // Attempt to read from the grouped source.
        int actualBytesRead = groupedSource.get(readPosition, destinationBuffer, bufferOffset, requestedLength);

        // --- Assert ---
        // Verify that the number of bytes read is what we expect.
        assertEquals("Should return the number of bytes actually read before reaching the end of the source.",
                expectedBytesRead, actualBytesRead);
    }
}