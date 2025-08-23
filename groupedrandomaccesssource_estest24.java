package com.itextpdf.text.io;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link GroupedRandomAccessSource} class.
 */
public class GroupedRandomAccessSourceTest {

    /**
     * Tests that the get() method correctly handles invalid negative arguments
     * by returning -1, indicating that no bytes were read.
     */
    @Test
    public void get_withNegativeParameters_returnsNegativeOne() throws IOException {
        // Arrange: Create a GroupedRandomAccessSource from a single, simple source.
        final int sourceLength = 7;
        RandomAccessSource source = new ArrayRandomAccessSource(new byte[sourceLength]);
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(new RandomAccessSource[]{source});

        // A buffer to read data into. Its contents are not relevant for this test.
        byte[] destinationBuffer = new byte[10];
        
        // Define invalid parameters for the get() method call.
        long invalidPosition = -3L;
        int invalidOffset = -3;
        int invalidLength = -3;

        // Act: Attempt to read from the source using the negative parameters.
        int bytesRead = groupedSource.get(invalidPosition, destinationBuffer, invalidOffset, invalidLength);

        // Assert: The method should return -1, and the source's length should be unaffected.
        assertEquals("The length of the grouped source should be correctly initialized.",
                sourceLength, groupedSource.length());
        assertEquals("get() should return -1 to indicate failure when provided with a negative length.",
                -1, bytesRead);
    }
}