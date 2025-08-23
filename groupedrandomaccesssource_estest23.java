package com.itextpdf.text.io;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * This class contains tests for the {@link GroupedRandomAccessSource}.
 * This specific test focuses on the getStartingSourceIndex method.
 */
public class GroupedRandomAccessSource_ESTestTest23 extends GroupedRandomAccessSource_ESTest_scaffolding {

    /**
     * Verifies that getStartingSourceIndex returns the correct index (0) for an offset
     * that falls within the bounds of the first (and only) underlying source.
     */
    @Test
    public void getStartingSourceIndex_whenOffsetIsInFirstSource_shouldReturnZero() throws IOException {
        // Arrange: Create a GroupedRandomAccessSource with a single source of length 8.
        byte[] sourceData = new byte[8];
        RandomAccessSource singleSource = new ArrayRandomAccessSource(sourceData);
        RandomAccessSource[] sources = { singleSource };

        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);

        // An offset that is clearly within the bounds of the single source (0-7).
        long offsetWithinFirstSource = 1L;

        // Act: Get the index of the source that should contain the specified offset.
        int actualSourceIndex = groupedSource.getStartingSourceIndex(offsetWithinFirstSource);

        // Assert: The index should be 0, as it's the first (and only) source.
        // The method is expected to find the correct source for the given offset.
        int expectedSourceIndex = 0;
        assertEquals(expectedSourceIndex, actualSourceIndex);

        // A sanity check to ensure the total length of the grouped source is correct.
        assertEquals("The total length of the grouped source should match the single source's length.",
                8L, groupedSource.length());
    }
}