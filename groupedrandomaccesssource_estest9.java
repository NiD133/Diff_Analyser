package com.itextpdf.text.io;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.fail;

public class GroupedRandomAccessSource_ESTestTest9 extends GroupedRandomAccessSource_ESTest_scaffolding {

    /**
     * Verifies that attempting to read from a GroupedRandomAccessSource after it has been closed
     * results in an exception.
     * <p>
     * The test expects a NullPointerException because the underlying ArrayRandomAccessSource,
     * when closed, likely nullifies its internal resources, causing subsequent access attempts to fail.
     */
    @Test(timeout = 4000)
    public void get_onClosedSource_throwsNullPointerException() throws IOException {
        // Arrange: Create a GroupedRandomAccessSource with a single underlying source.
        byte[] sourceData = new byte[3];
        RandomAccessSource singleSource = new ArrayRandomAccessSource(sourceData);
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(new RandomAccessSource[]{singleSource});

        // Act: Close the source before attempting to read from it.
        groupedSource.close();

        // Assert: Attempting to read from the closed source should throw an exception.
        try {
            byte[] destinationBuffer = new byte[3];
            groupedSource.get(1L, destinationBuffer, 1, 1);
            fail("Expected a NullPointerException when reading from a closed source, but no exception was thrown.");
        } catch (NullPointerException e) {
            // This is the expected behavior. The underlying source is closed and cannot be accessed.
        }
    }
}