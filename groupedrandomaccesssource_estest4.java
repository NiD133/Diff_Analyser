package com.itextpdf.text.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.IOException;

/**
 * Test suite for the {@link GroupedRandomAccessSource} class.
 */
public class GroupedRandomAccessSourceTest {

    /**
     * Verifies that the length of a GroupedRandomAccessSource is zero
     * when it is composed of multiple empty sources. The total length
     * should be the sum of the lengths of its constituent parts.
     */
    @Test
    public void length_shouldBeZero_whenAllConstituentSourcesAreEmpty() throws IOException {
        // Arrange: Create a group of two distinct but empty random access sources.
        RandomAccessSource emptySource1 = new ArrayRandomAccessSource(new byte[0]);
        RandomAccessSource emptySource2 = new ArrayRandomAccessSource(new byte[0]);

        RandomAccessSource[] sources = {emptySource1, emptySource2};
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);

        // Act: Get the total length of the grouped source.
        long actualLength = groupedSource.length();

        // Assert: The total length should be the sum of the individual source lengths (0 + 0 = 0).
        long expectedLength = 0L;
        assertEquals(expectedLength, actualLength);
    }
}