package com.itextpdf.text.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.IOException;

/**
 * This class contains tests for the GroupedRandomAccessSource.
 * Note: This specific test was refactored from an auto-generated test file.
 */
public class GroupedRandomAccessSource_ESTestTest3 extends GroupedRandomAccessSource_ESTest_scaffolding {

    /**
     * Verifies that the total length of a GroupedRandomAccessSource is zero
     * when it is constructed from multiple empty underlying sources.
     */
    @Test
    public void length_shouldBeZero_whenConstructedWithEmptySources() throws IOException {
        // Arrange: Create an array of two distinct sources, each with a length of zero.
        RandomAccessSource emptySource1 = new ArrayRandomAccessSource(new byte[0]);
        RandomAccessSource emptySource2 = new ArrayRandomAccessSource(new byte[0]);
        RandomAccessSource[] sources = {emptySource1, emptySource2};

        // Act: Create a GroupedRandomAccessSource from the array of empty sources.
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);

        // Assert: The total length should be the sum of the underlying sources' lengths (0 + 0).
        assertEquals("The total length of a group of empty sources should be 0.", 0L, groupedSource.length());
    }
}