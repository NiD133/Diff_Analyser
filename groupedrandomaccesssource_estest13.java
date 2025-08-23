package com.itextpdf.text.io;

import org.junit.Test;

import java.io.IOException;
import java.nio.channels.FileChannel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link GroupedRandomAccessSource} class.
 */
public class GroupedRandomAccessSourceTest {

    /**
     * Verifies that a get() call on a GroupedRandomAccessSource throws an IOException
     * if the read operation is directed to an underlying source that has not been opened.
     */
    @Test
    public void get_whenUnderlyingSourceIsNotOpen_throwsIOException() throws IOException {
        // --- Arrange ---

        // Create a valid source to act as a prefix, which will offset the subsequent source.
        RandomAccessSource prefixSource = new ArrayRandomAccessSource(new byte[5]); // length = 5

        // Create an "unopened" source by instantiating MappedChannelRandomAccessSource with a null FileChannel.
        // This simulates a source that is not ready for I/O operations.
        RandomAccessSource unopenedSource = new MappedChannelRandomAccessSource(null, 0L, 10L); // length = 10

        // Combine the sources into a group. The `unopenedSource` will be responsible for
        // handling requests for offsets starting from 5 (the length of `prefixSource`).
        // - prefixSource:   covers global offsets 0-4
        // - unopenedSource: covers global offsets 5-14
        RandomAccessSource[] sources = {prefixSource, unopenedSource};
        GroupedRandomAccessSource groupedSource = new GroupedRandomAccessSource(sources);

        // Define a read operation that targets a position within the range of the unopened source.
        long readPositionWithinUnopenedSource = 7L;
        byte[] destinationBuffer = new byte[10];

        // --- Act & Assert ---

        // Expect an IOException because the read at position 7 targets the unopened source.
        try {
            groupedSource.get(readPositionWithinUnopenedSource, destinationBuffer, 0, destinationBuffer.length);
            fail("Expected an IOException to be thrown when reading from an unopened underlying source, but none was thrown.");
        } catch (IOException e) {
            // Verify that the correct exception was thrown, indicating the source was not open.
            assertEquals("RandomAccessSource not opened", e.getMessage());
        }
    }
}