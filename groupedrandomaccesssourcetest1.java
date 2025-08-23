package com.itextpdf.text.io;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link GroupedRandomAccessSource}.
 */
public class GroupedRandomAccessSourceTest {

    private static final int SOURCE_LENGTH = 100;
    private byte[] sourceData;

    @Before
    public void setUp() {
        // Create a reusable data array with values [0, 1, 2, ..., 99]
        sourceData = new byte[SOURCE_LENGTH];
        for (int i = 0; i < SOURCE_LENGTH; i++) {
            sourceData[i] = (byte) i;
        }
    }

    @Test
    public void get_shouldReadAcrossMultipleSourcesAsSingleContiguousSource() throws IOException {
        // Arrange: Create a grouped source from three individual sources.
        // Each individual source contains the same data: bytes 0 through 99.
        ArrayRandomAccessSource source1 = new ArrayRandomAccessSource(sourceData);
        ArrayRandomAccessSource source2 = new ArrayRandomAccessSource(sourceData);
        ArrayRandomAccessSource source3 = new ArrayRandomAccessSource(sourceData);

        RandomAccessSource[] sources = {source1, source2, source3};
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);

        // Assert: The grouped source should behave like one large source of 300 bytes.

        // 1. Verify total length
        long expectedTotalLength = (long) source1.length() + source2.length() + source3.length();
        assertEquals("Total length should be the sum of individual source lengths",
                expectedTotalLength, groupedSource.length());

        // 2. Verify reads at the boundaries of the underlying sources
        // Accessing the last byte of the first source (index 99)
        assertEquals("Should read the last byte from the first source",
                source1.get(SOURCE_LENGTH - 1), groupedSource.get(SOURCE_LENGTH - 1));

        // Accessing the first byte of the second source (index 100)
        assertEquals("Should read the first byte from the second source",
                source2.get(0), groupedSource.get(SOURCE_LENGTH));

        // Accessing the second byte of the second source (index 101)
        assertEquals("Should read the second byte from the second source",
                source2.get(1), groupedSource.get(SOURCE_LENGTH + 1));

        // 3. Verify read at the very end of the grouped source
        // Accessing the last byte of the third source (index 299)
        long lastIndex = expectedTotalLength - 1;
        assertEquals("Should read the last byte from the last source",
                source3.get(SOURCE_LENGTH - 1), groupedSource.get(lastIndex));

        // 4. Verify out-of-bounds read
        assertEquals("Should return -1 for reads beyond the total length",
                -1, groupedSource.get(expectedTotalLength));
    }
}