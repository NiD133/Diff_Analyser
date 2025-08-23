package org.apache.commons.compress.utils;

import org.junit.Test;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.util.Collections;
import java.util.List;

/**
 * Tests for {@link MultiReadOnlySeekableByteChannel}.
 */
public class MultiReadOnlySeekableByteChannelTest {

    /**
     * Verifies that the size() method throws a NullPointerException
     * if the channel is constructed with a list containing a null element.
     * The size() method needs to access each underlying channel to calculate the total size,
     * and a null channel will cause a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void sizeShouldThrowNPEWhenChannelListContainsNull() throws IOException {
        // Arrange: Create a list containing a single null channel.
        List<SeekableByteChannel> channelsWithNull = Collections.singletonList(null);
        MultiReadOnlySeekableByteChannel multiChannel = new MultiReadOnlySeekableByteChannel(channelsWithNull);

        // Act: Calling size() on the multi-channel should trigger the exception.
        multiChannel.size();

        // Assert: The @Test(expected) annotation handles the exception verification.
    }
}