package com.itextpdf.text.io;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link GroupedRandomAccessSource} class.
 */
// The original test class name and inheritance are kept to match the user's context.
public class GroupedRandomAccessSource_ESTestTest25 extends GroupedRandomAccessSource_ESTest_scaffolding {

    /**
     * Tests that a GroupedRandomAccessSource created with a single underlying source
     * correctly reports its total length and can retrieve the first byte.
     */
    @Test
    public void get_fromSingleSource_returnsCorrectByteAndReportsCorrectLength() throws IOException {
        // Arrange: Create a single source with known data.
        byte[] sourceData = {(byte) 100, (byte) 150};
        RandomAccessSource singleSource = new ArrayRandomAccessSource(sourceData);
        RandomAccessSource[] sources = {singleSource};

        // Act: Create the GroupedRandomAccessSource and get the byte at position 0.
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);
        int firstByte = groupedSource.get(0L);

        // Assert: Verify the length and the retrieved byte.
        assertEquals("The total length should match the length of the single source.", 2L, groupedSource.length());
        assertEquals("The byte at position 0 should be the first byte of the source data.", 100, firstByte);
    }
}