package com.itextpdf.text.io;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

// Note: The original test class name and inheritance from a scaffolding class are preserved
// to maintain consistency with the existing test suite structure.
public class GroupedRandomAccessSource_ESTestTest7 extends GroupedRandomAccessSource_ESTest_scaffolding {

    /**
     * Tests that getStartingSourceIndex() correctly identifies the index of the source
     * that contains a byte at a specific offset, especially when the offset falls
     * exactly on the boundary between two sources.
     */
    @Test(timeout = 4000)
    public void getStartingSourceIndex_whenOffsetIsStartOfSecondSource_returnsIndexOfSecondSource() throws IOException {
        // Arrange: Create a grouped source from two distinct sources of a known length.
        // Source 1 will cover global offsets [0, 6].
        // Source 2 will cover global offsets [7, 13].
        final long source1Length = 7L;
        final long source2Length = 7L;

        RandomAccessSource source1 = new ArrayRandomAccessSource(new byte[(int) source1Length]);
        RandomAccessSource source2 = new ArrayRandomAccessSource(new byte[(int) source2Length]);

        RandomAccessSource[] sources = {source1, source2};
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);

        // A quick check to ensure the grouped source was constructed as expected.
        long expectedTotalLength = source1Length + source2Length;
        assertEquals("Pre-condition failed: Grouped source length is incorrect.",
                     expectedTotalLength, groupedSource.length());

        // Act: Get the source index for an offset that is exactly at the start of the second source.
        long offsetAtBoundary = source1Length; // This offset (7) is the first byte of the second source.
        int actualSourceIndex = groupedSource.getStartingSourceIndex(offsetAtBoundary);

        // Assert: The returned index should be 1, which is the index of the second source in the group.
        int expectedSourceIndex = 1;
        assertEquals("The index for an offset at the start of the second source should be 1.",
                     expectedSourceIndex, actualSourceIndex);
    }
}