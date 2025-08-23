package com.itextpdf.text.io;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link GroupedRandomAccessSource}.
 * This class contains the refactored test case.
 */
// The original test class extended an EvoSuite scaffolding class. 
// This has been removed for clarity and to promote standard testing practices.
public class GroupedRandomAccessSourceTest {

    /**
     * Verifies that attempting to read from a GroupedRandomAccessSource after it has been closed
     * throws an IllegalStateException.
     */
    @Test
    public void get_whenSourceIsClosed_throwsIllegalStateException() throws IOException {
        // Arrange: Create a GroupedRandomAccessSource from two distinct underlying sources.
        RandomAccessSource source1 = new ArrayRandomAccessSource(new byte[10]);
        RandomAccessSource source2 = new ArrayRandomAccessSource(new byte[10]);
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(new RandomAccessSource[]{source1, source2});

        // Act: Close the source before attempting to read from it.
        groupedSource.close();

        // Assert: An attempt to read from the closed source should fail.
        try {
            // Attempt to read at an offset that falls within the second underlying source.
            groupedSource.get(10L);
            fail("Expected an IllegalStateException because the source is closed, but no exception was thrown.");
        } catch (IllegalStateException e) {
            // Verify that the correct exception was thrown.
            // The underlying ArrayRandomAccessSource is responsible for throwing this exception.
            assertEquals("Already closed", e.getMessage());
        }
    }
}