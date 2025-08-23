package com.itextpdf.text.io;

import org.junit.Test;
import java.io.IOException;

/**
 * This test class contains the refactored test case.
 * The original scaffolding and other generated tests are omitted for clarity.
 */
public class GroupedRandomAccessSource_ESTestTest14 {

    /**
     * Verifies that attempting to read from a GroupedRandomAccessSource after it has been closed
     * throws a NullPointerException. This is because the underlying source, when closed,
     * releases its internal resources, leading to an error on subsequent access attempts.
     */
    @Test(expected = NullPointerException.class)
    public void getOnClosedSourceThrowsNullPointerException() throws IOException {
        // Arrange: Create a GroupedRandomAccessSource with a single underlying source.
        byte[] sourceData = new byte[2];
        RandomAccessSource arraySource = new ArrayRandomAccessSource(sourceData);
        RandomAccessSource[] sources = { arraySource };
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);

        // Act: Close the grouped source. This action propagates to and closes all
        // underlying sources.
        groupedSource.close();

        // Assert: Attempting to read from the closed source should throw an exception.
        // The @Test(expected = ...) annotation handles the assertion, expecting a
        // NullPointerException which is thrown by the closed underlying ArrayRandomAccessSource.
        groupedSource.get(1L);
    }
}