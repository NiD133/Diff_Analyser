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
     * Tests that calling close() on a MultiReadOnlySeekableByteChannel throws a
     * NullPointerException if the underlying list of channels contains a null element.
     */
    @Test(expected = NullPointerException.class)
    public void closeShouldThrowNullPointerExceptionWhenChannelListContainsNull() throws IOException {
        // Arrange: Create a list containing a single null channel.
        List<SeekableByteChannel> channelsWithNull = Collections.singletonList(null);
        MultiReadOnlySeekableByteChannel multiChannel = new MultiReadOnlySeekableByteChannel(channelsWithNull);

        // Act: Attempt to close the multi-channel. This should trigger the exception
        // because the implementation tries to call close() on the null element.
        multiChannel.close();

        // Assert: The @Test(expected) annotation handles the exception assertion.
    }
}