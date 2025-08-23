package org.apache.commons.compress.utils;

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MultiReadOnlySeekableByteChannel_ESTestTest28 extends MultiReadOnlySeekableByteChannel_ESTest_scaffolding {

    /**
     * Tests that reading from a MultiReadOnlySeekableByteChannel that is composed of another
     * empty channel correctly returns -1, indicating the end of the stream.
     */
    @Test(timeout = 4000)
    public void readShouldReturnEndOfStreamWhenChannelContainsAnEmptyChannel() throws IOException {
        // Arrange: Create a composite channel that contains a single, empty sub-channel.
        // 1. The inner channel is a MultiReadOnlySeekableByteChannel created from an empty list,
        //    making it an effectively empty channel with a size of zero.
        SeekableByteChannel emptyInnerChannel = new MultiReadOnlySeekableByteChannel(Collections.emptyList());

        // 2. The outer channel is a MultiReadOnlySeekableByteChannel that contains only the empty inner channel.
        List<SeekableByteChannel> channels = Collections.singletonList(emptyInnerChannel);
        MultiReadOnlySeekableByteChannel multiChannel = new MultiReadOnlySeekableByteChannel(channels);

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // Act: Attempt to read from the composite channel.
        int bytesRead = multiChannel.read(buffer);

        // Assert: The read operation should immediately return -1, signifying the end of the stream,
        // as there is no data to read from its constituent channels.
        assertEquals("Should return -1 for end of stream", -1, bytesRead);
    }
}